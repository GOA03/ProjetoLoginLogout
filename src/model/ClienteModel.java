package model;

import java.io.*;
import java.net.*;
import java.text.ParseException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import controller.JSONController;
import view.AvisosView;
import view.LoginView;

public class ClienteModel {

    private Socket socketEcho;
    private PrintWriter saida;
    private BufferedReader entrada;
    private Thread threadEscuta;
    private JSONController jsonController;
    private int token;
	private AvisosView avisosView;
	private LoginView loginView;

    // Conectar ao servidor (método sincronizado)
    public synchronized void conectar(String ip, int porta) throws IOException {
    	this.socketEcho = new Socket(ip, porta);
    	this.saida = new PrintWriter(socketEcho.getOutputStream(), true);
    	this.entrada = new BufferedReader(new InputStreamReader(socketEcho.getInputStream()));
    	this.jsonController = new JSONController();
    	this.loginView = new LoginView(this);
        
        // Criar e iniciar a thread de escuta
        threadEscuta = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Escutando mensagens do servidor");
                try {
                    String msg;
                    while ((msg = entrada.readLine()) != null) {
                    	
                        System.out.println("Mensagem recebida: " + msg);
                        
                        RespostaModel resposta = new RespostaModel();
                        
                        resposta = jsonController.changeResponseToJson(msg);
                        
                        int status = resposta.getStatus();
                        if (status == 0) {System.out.println("Não há status");}
                        
                        Integer token = resposta.getToken();
                        
                        String operacao = resposta.getOperacao();
                        switch(operacao){    
                        	case "login":{
                        		if(status == 200) {
                        			System.out.println("Token/Ra: "+ token);
                        			logarAvisosView(token);
                        			fecharLoginView();
                        		}else if(status == 401) {
                        			//respostaTelaLogin("Login inválido");
                        		}else {
                        			System.out.println("Status informado não está cadastrado");
                        		}
                        	break;
                        	}
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        threadEscuta.start();
    }

	protected void fecharLoginView() {
		
		this.loginView.dispose();
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
        this.saida.println(msg.toString()); // Convertendo JSONObject para String
        System.out.println("MENSAGEM ENVIADA AO SERVIDOR: " + msg.toString());
    }
    
    public void logarAvisosView(int token) {
    	
    	this.token = token;
    	
    	// Fechar a tela de login antes de abrir a tela de avisos
        if (this.loginView != null) {
            this.loginView.dispose(); // Fecha a tela de login
        }

        // Cria e torna a janela de AvisosView visível
        this.avisosView = new AvisosView(this, this.token);
        this.avisosView.setVisible(true);
	}
}
