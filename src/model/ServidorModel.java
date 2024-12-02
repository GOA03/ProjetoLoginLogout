package model;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.SQLException;

import org.json.simple.JSONObject;

import controller.CadastroController;
import controller.JSONController;
import controller.LoginController;
import dao.BancoDados;
import dao.UsuarioDAO;
import enums.CadastroEnum;
import enums.LoginEnum;

public class ServidorModel {
	private ServerSocket serverSocket;

	// Método para iniciar o servidor
	public void iniciarServidor(int porta) throws IOException {
		try {
			serverSocket = new ServerSocket(porta, 50, InetAddress.getByName("0.0.0.0"));
			System.out.println("Servidor iniciado na porta " + porta);
		} catch (IOException e) {
			System.err.println("Erro ao iniciar o servidor na porta " + porta + ": " + e.getMessage());
			throw new IOException("Não foi possível iniciar o servidor.");
		}
	}

	// Método para esperar a conexão de um cliente
	public Socket esperarConexao() throws IOException {
		System.out.println("Aguardando conexão de um cliente...");
		try {
			Socket socketCliente = serverSocket.accept();
			System.out.println("Cliente conectado: " + socketCliente.getInetAddress());
			return socketCliente;
		} catch (IOException e) {
			System.err.println("Erro ao aguardar conexão de um cliente: " + e.getMessage());
			throw new IOException("Erro ao aceitar a conexão do cliente.");
		}
	}

	// Método para fechar o servidor
	public void fecharServidor() throws IOException {
		if (serverSocket != null && !serverSocket.isClosed()) {
			try {
				serverSocket.close();
				System.out.println("Servidor fechado com sucesso.");
			} catch (IOException e) {
				System.err.println("Erro ao fechar o servidor: " + e.getMessage());
				throw new IOException("Não foi possível fechar o servidor.");
			}
		}
	}

	// Método para lidar com a comunicação com o cliente
	public void lidarComCliente(Socket socketCliente, ServidorListener listener) {
		new Thread(() -> {
			JSONController jsonController = new JSONController();
			LoginController loginController = new LoginController();
			CadastroController cadastroController = new CadastroController();

			try (PrintWriter saida = new PrintWriter(socketCliente.getOutputStream(), true);
					BufferedReader entrada = new BufferedReader(
							new InputStreamReader(socketCliente.getInputStream()));) {
				listener.onConexao(socketCliente.getInetAddress().toString());
				System.out.println("Novo cliente conectado: " + socketCliente.getInetAddress());

				String mensagemRecebida;
				while ((mensagemRecebida = entrada.readLine()) != null) {
					System.out.println("C -> S: " + mensagemRecebida);
					listener.onMensagemRecebida(mensagemRecebida);
					String op = jsonController.getOperacao(mensagemRecebida);

					switch (op) {
					case "login": {
						UsuarioModel usuario = jsonController.changeLoginToJSON(mensagemRecebida);
						RespostaModel resposta = new RespostaModel();
						resposta.setOperacao("login");

						// Tratando erros de leitura do JSON
						if (usuario == null) {
							resposta.setStatus(401);
							resposta.setMsg("Não foi possível ler o JSON recebido.");
							enviarResposta(resposta, saida);
							break;
						}

						// Verificando se todos os campos foram preenchidos
						if (usuario.getRa() == null || usuario.getSenha() == null) {
							resposta.setStatus(401);
							resposta.setMsg("Os campos recebidos não são válidos.");
							enviarResposta(resposta, saida);
							break;
						}

						// Validações específicas dos campos
						if (!usuario.getRa().matches("\\d{7}")) { // RA deve ter 7 dígitos
							resposta.setStatus(401);
							resposta.setMsg("RA inválido. Deve conter apenas números e ter 7 dígitos.");
							enviarResposta(resposta, saida);
							break;
						}

						if (!usuario.getSenha().matches("[a-zA-Z]{8,20}")) { // Senha entre 8-20 caracteres, apenas
																				// letras
							resposta.setStatus(401);
							resposta.setMsg(
									"Senha inválida. Deve conter entre 8 e 20 caracteres, apenas letras sem acentos.");
							enviarResposta(resposta, saida);
							break;
						}

						// Tentando validar as credenciais
						LoginEnum loginValido = loginController.validarLogin(usuario);
						resposta.setStatus(401); // Definido o status 401 para todos os erros

						switch (loginValido) {
							case SUCESSO: {
								resposta.setMsg("Login bem-sucedido.");
								try {
									String token = loginController.getRa(usuario.getRa());
									resposta.setToken(token);
									resposta.setStatus(200);
									resposta.setRa(token);
									enviarResposta(resposta, saida);
								} catch (SQLException e) {
									resposta.setMsg("O servidor não conseguiu conectar com o banco de dados.");
									enviarResposta(resposta, saida);
								}
								break;
							}
							case ERRO_USUARIO_E_SENHA: {
								resposta.setMsg("Credenciais incorretas.");
								enviarResposta(resposta, saida);
								break;
							}
							case ERRO_JSON: {
								resposta.setMsg("Erro ao processar o JSON.");
								enviarResposta(resposta, saida);
								break;
							}
							case ERRO_VALIDACAO: {
								resposta.setMsg("Os campos recebidos não são válidos.");
								enviarResposta(resposta, saida);
								break;
							}
							case ERRO_BANCO: {
								resposta.setMsg("O servidor não conseguiu conectar com o banco de dados.");
								enviarResposta(resposta, saida);
								break;
							}
							default: {
								resposta.setMsg("Erro desconhecido.");
								enviarResposta(resposta, saida);
								break;
							}
						}
						break;
					}
					case "cadastrarUsuario": {

						UsuarioModel usuario = jsonController.changeRegisterJSON(mensagemRecebida);
						CadastroEnum resposta = cadastroController.validarCadastro(usuario);
						RespostaModel res = new RespostaModel();
						res.setOperacao("cadastrarUsuario");

						switch (resposta) {

							case SUCESSO: {
	
								try {
									Connection conn = BancoDados.conectar();
									new UsuarioDAO(conn).adicionarUsuario(usuario);
									res.setMsg("Cadastro realizado com sucesso!");
									res.setStatus(201);
									String ra;
									try {
										ra = loginController.getRa(usuario.getRa());
										res.setToken(ra);
									} catch (SQLException e) {
										e.printStackTrace();
									}
	
									JSONObject respostaJSON = jsonController.changeResponseToJson(res);
									System.out.println("S -> C: " + respostaJSON);
									BancoDados.desconectar();
									saida.println(respostaJSON);
								} catch (SQLException e) {
									e.printStackTrace();
								}
								break;
	
							}
							case ERRO: {
	
								res.setMsg("Os campos recebidos nao sao validos.");
								res.setStatus(404);
								res.setToken(usuario.getRa());
								JSONObject respostaJSON = jsonController.changeResponseToJson(res);
								saida.println(respostaJSON);
								System.out.println("S -> C: " + respostaJSON);
								break;
	
							}
							case RA_CADASTRADO: {
								res.setMsg("Não foi cadastrar pois o usuario informado ja existe");
								res.setStatus(422);
								res.setToken(usuario.getRa());
								JSONObject respostaJSON = jsonController.changeResponseToJson(res);
								saida.println(respostaJSON);
								System.out.println("S -> C: " + respostaJSON);
								break;
							}
						}
					}
					case "logout":{
						UsuarioModel usuario = jsonController.changeLogoutToJSON(mensagemRecebida);
		            	  RespostaModel resposta = new RespostaModel();
		            	  resposta.setOperacao("logout");
		            	  resposta.setStatus(200);
		            	  resposta.setToken(usuario.getRa());
		            	  JSONObject respostaJSON = jsonController.changeLogoutToJson(resposta);
		            	  saida.println(respostaJSON);
		            	  System.out.println("S -> C: " + respostaJSON);
		            	  break;
		              }
					default:
						// Caso a operação não exista, retorna erro.
						System.out.println("Operação desconhecida: " + op);
						break;
					}
				}
			} catch (IOException e) {
				listener.onErro("Erro na comunicação com o cliente: " + e.getMessage());
				System.err.println("Erro na comunicação com o cliente: " + e.getMessage());
			} finally {
				try {
					socketCliente.close();
					System.out.println("Conexão com o cliente fechada.");
				} catch (IOException e) {
					listener.onErro("Erro ao fechar a conexão com o cliente: " + e.getMessage());
					System.err.println("Erro ao fechar a conexão com o cliente: " + e.getMessage());
				}
			}
		}).start();
	}

	private void enviarResposta(RespostaModel resposta, PrintWriter saida) {
		JSONController jsonController = new JSONController();
		JSONObject respostaJSON = jsonController.changeResponseToJson(resposta);
		System.out.println("S -> C: " + respostaJSON);
		saida.println(respostaJSON);
	}

	// Interface que define os métodos que o ouvinte (listener) deve implementar
	public interface ServidorListener {
		void onConexao(String clienteInfo);

		void onMensagemRecebida(String mensagem);

		void onDesconexao();

		void onErro(String erro);
	}
}
