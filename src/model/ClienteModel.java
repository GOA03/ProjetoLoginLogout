package model;

import java.io.*;
import java.net.*;
import java.text.ParseException;

import javax.swing.JOptionPane;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import controller.JSONController;
import view.AvisosView;
import view.CadastroView;
import view.LoginView;

public class ClienteModel {

    private Socket socketEcho;
    private PrintWriter saida;
    private BufferedReader entrada;
    private Thread threadEscuta;
    private JSONController jsonController;
    private String token;
    private AvisosView avisosView;
    private LoginView loginView;
	private CadastroView cadastroView;

    // Conectar ao servidor (método sincronizado)
    public synchronized void conectar(String ip, int porta) throws IOException {
        this.socketEcho = new Socket(ip, porta);
        this.saida = new PrintWriter(socketEcho.getOutputStream(), true);
        this.entrada = new BufferedReader(new InputStreamReader(socketEcho.getInputStream()));
        this.jsonController = new JSONController();
        
        final ClienteModel clienteModel = this; 
        
        // Criar e iniciar a thread de escuta
        threadEscuta = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Escutando mensagens do servidor");
                try {
                    String msg;
                    while ((msg = entrada.readLine()) != null) {
                        System.out.println("SERVIDOR -> CLIENTE: " + msg);
                        
                        RespostaModel resposta = jsonController.changeResponseToJson(msg);
                        
                        int status = resposta.getStatus();
                        String mensagem = resposta.getMsg();
                        if (status == 0) {
                            System.out.println("Não há status");
                        }
                        
                        String token = resposta.getToken();
                        String operacao = resposta.getOperacao();
                        if (operacao == null) {
                            System.out.println("Operação não encontrada ou inválida.");
                        } else {
                            switch (operacao) {
                                case "login": {
                                    if (status == 200) {
                                        logarAvisosView(token);
                                    } else if (status == 401) {
                                    	
                                    	if (mensagem == null || mensagem.trim().isEmpty()) { // Mensagem padrão
                                            mensagem = "Erro desconhecido. Sem mensagem disponível."; 
                                        }
                                    	
                                    	JOptionPane.showMessageDialog(loginView, mensagem, "Erro de Login", JOptionPane.ERROR_MESSAGE);
                                    } else {
                                        System.out.println("Status informado não está cadastrado");
                                    }
                                    break;
                                }
                                case "cadastrarUsuario": {
                                    if (status == 201) {
                                        JOptionPane.showMessageDialog(null, "Cadastro realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                                        FecharTelaCadastro();
                                        new LoginView(clienteModel).setVisible(true);
                                    } else if (status == 404) {
                                        if (mensagem == null || mensagem.trim().isEmpty()) {
                                            mensagem = "Os campos recebidos não são válidos.";
                                        }
                                        JOptionPane.showMessageDialog(null, mensagem, "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
                                    } else if (status == 422) {
                                        if (mensagem == null || mensagem.trim().isEmpty()) {
                                            mensagem = "O RA informado já está cadastrado."; 
                                        }
                                        JOptionPane.showMessageDialog(null, mensagem, "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
                                    } else {
                                        System.out.println("Status informado não está cadastrado");
                                    }
                                    break;
                                }
                                case "logout": {
                                    if (status == 200) {
                                        System.out.println("LOGOUT -> " + token);
                                        JOptionPane.showMessageDialog(null, mensagem);
                                    } else {
                                        // Obtenha a mensagem do servidor
                                        mensagem = resposta.getMsg();

                                        // Verifique se a mensagem está vazia ou nula
                                        if (mensagem == null || mensagem.trim().isEmpty()) {
                                            mensagem = "Erro desconhecido. Sem mensagem disponível."; // Mensagem padrão
                                        }

                                        // Exiba a mensagem de erro
                                        JOptionPane.showMessageDialog(loginView, mensagem, "Erro de Logout", JOptionPane.ERROR_MESSAGE);
                                    }
                                    break;
                                }

                                default:
                                	JOptionPane.showMessageDialog(null, mensagem + ": " + operacao);
                                    System.out.println("Operação inválida ou não reconhecida.");
                                    break;
                            }
                        }
                    }
                } catch (IOException e) {
                	if (e.getMessage() != null && e.getMessage().contains("Connection reset")) {
                        JOptionPane.showMessageDialog(null, "O servidor foi fechado.", "Servidor Fechado", JOptionPane.WARNING_MESSAGE);
                	
                        System.exit(0); // Fecha a aplicação
                	} else {
                        e.printStackTrace();
                    }
                }
            }
        });
        threadEscuta.start();
    }

    protected void FecharTelaCadastro() {
    	if (this.cadastroView != null) {
    		this.cadastroView.dispose();
    	}
	}

	// Enviar mensagem ao servidor (método sincronizado)
    public synchronized void enviarMensagem(String mensagem) throws IOException {
        saida.println(mensagem);
    }

    // Receber resposta do servidor (método sincronizado)
    public synchronized String receberResposta() throws IOException {
        return entrada.readLine();
    }
    
    public JSONObject receberRespostaJSON() throws IOException, ParseException, org.json.simple.parser.ParseException {
        String resposta = receberResposta(); // Chama o método que lê a resposta do servidor
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(resposta); // Converte a resposta para JSONObject
    }

    // Fechar a conexão (método sincronizado)
    public synchronized void fecharConexao() throws IOException {
        if (saida != null) saida.close();
        if (entrada != null) entrada.close();
        if (socketEcho != null) socketEcho.close();
    }

    // Método para enviar um objeto JSONObject
    public void enviarMensagem(JSONObject msg) {
    	System.out.println("CLIENTE -> SERVIDOR: " + msg.toString());
        this.saida.println(msg.toString()); // Convertendo JSONObject para String
    }
    
    public void logarAvisosView(String token) {
        this.token = token;
        
        // Fechar a tela de login antes de abrir a tela de avisos
        if (this.loginView != null) {
            this.loginView.dispose(); // Fecha a tela de login
        }

        // Cria e torna a janela de AvisosView visível
        this.avisosView = new AvisosView(this, this.token);
        this.avisosView.setVisible(true);
    }

	public void setLoginView(LoginView loginView) {
		this.loginView = loginView;
	}

	public void setCadastroView(CadastroView cadastroView) {
		this.cadastroView = cadastroView;
	}
}