package model;

import java.io.*;
import java.net.*;
import org.json.simple.JSONObject;

import controller.JSONController;
import view.AvisosView;

public class ClienteModel {

    private Socket socketEcho;
    private PrintWriter saida;
    private BufferedReader entrada;
    private Thread threadEscuta;
    private JSONController jsonController;
    private int token;
	private AvisosView avisosView;

    // Conectar ao servidor (método sincronizado)
    public synchronized void conectar(String ip, int porta) throws IOException {
        socketEcho = new Socket(ip, porta);
        saida = new PrintWriter(socketEcho.getOutputStream(), true);
        entrada = new BufferedReader(new InputStreamReader(socketEcho.getInputStream()));
        jsonController = new JSONController();
        
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
                        		String mensagem;
                        		if(status == 200) {
                        			System.out.println("Token/Ra: "+ token);
                        			logarAvisos(token);
                        			fecharLoginView();
                        		}else if(status == 401) {
                        			mensagem = "Login inválido";
                        			respostaTelaLogin(mensagem);
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

    // Enviar mensagem ao servidor (método sincronizado)
    public synchronized void enviarMensagem(String mensagem) throws IOException {
        saida.println(mensagem);
    }

    // Receber resposta do servidor (método sincronizado)
    public synchronized String receberResposta() throws IOException {
        return entrada.readLine();
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
    
    public void logarAvisos(int token) {
    	
		this.token = token;
		AvisosView avisosView = new AvisosView(this, this.token);
		this.avisosView = avisosView;
		this.avisosView.setVisible(true);
		RespostaModel resposta = new RespostaModel();
		resposta.setOperacao("receberMensagem");
		resposta.setToken(this.token);
		JSONController json = new JSONController();
		JSONObject resp = json.changeResponseToJson(resposta);
		this.enviarMensagem(resp);
	}
}
