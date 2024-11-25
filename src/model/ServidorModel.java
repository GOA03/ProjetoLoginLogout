package model;

import java.io.*;
import java.net.*;
import java.sql.SQLException;

import org.json.simple.JSONObject;

import controller.JSONController;
import controller.LoginController;
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
            
            try (
                PrintWriter saida = new PrintWriter(socketCliente.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            ) {
                // Notificando a conexão do cliente
                listener.onConexao(socketCliente.getInetAddress().toString());
                System.out.println("Novo cliente conectado: " + socketCliente.getInetAddress());

                String mensagemRecebida;
                // Laço para ler as mensagens do cliente
                while ((mensagemRecebida = entrada.readLine()) != null) {
                    System.out.println("Mensagem recebida: " + mensagemRecebida);
                    listener.onMensagemRecebida(mensagemRecebida);
                    String op = jsonController.getOperacao(mensagemRecebida);
                    switch(op) {
                        case "login": {
                            UsuarioModel usuario = jsonController.changeLoginToJSON(mensagemRecebida);
                            LoginEnum loginValido = loginController.validarLogin(usuario);
                            RespostaModel resposta = new RespostaModel();
                            resposta.setOperacao("login");
                            switch(loginValido) {
                                case SUCESSO: {
                                    resposta.setStatus(200);
                                    String token; // Alterado para String
                                    try {
                                        token = loginController.getRa(usuario.getRa());
                                        resposta.setToken(token);
                                        JSONObject respostaJSON = jsonController.changeResponseToJson(resposta);
                                        System.out.println("Servidor -> Cliente: " + respostaJSON);
                                        saida.println(respostaJSON);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                } 
                                case ERRO_USUARIO_E_SENHA: {
                                    resposta.setMsg("Login ou senha incorreto");
                                    resposta.setStatus(401);
                                    JSONObject respostaJSON = jsonController.changeResponseToJson(resposta);
                                    saida.println(respostaJSON);
                                    break;
                                }
                            }
                            break;                            
                        }
                    }
                }
            } catch (IOException e) {
                // Tratamento de erro na comunicação
                listener.onErro("Erro na comunicação com o cliente: " + e.getMessage());
                System.err.println("Erro na comunicação com o cliente: " + e.getMessage());
            } finally {
                try {
                    socketCliente.close();
                    System.out.println("Conexão com o cliente fechada.");
                } catch (IOException e) {
                    // Erro ao fechar o socket
                    listener.onErro("Erro ao fechar a conexão com o cliente: " + e.getMessage());
                    System.err.println("Erro ao fechar a conexão com o cliente: " + e.getMessage());
                }
            }
        }).start();
    }

	// Interface que define os métodos que o ouvinte (listener) deve implementar
	public interface ServidorListener {
		void onConexao(String clienteInfo);

		void onMensagemRecebida(String mensagem);

		void onDesconexao();

		void onErro(String erro);
	}
}
