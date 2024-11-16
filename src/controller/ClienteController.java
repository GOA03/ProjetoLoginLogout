package controller;

import model.ClienteModel;
import view.ClienteView;
import view.LoginView;
import view.AvisosView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

public class ClienteController {
    private ClienteModel model;
    private ClienteView view;
    private LoginView loginView;

    public ClienteController(ClienteView view, ClienteModel model) {
        this.view = view;
        this.model = model;
        
        // Adicionando listener para o botão de conectar
        this.view.getBtnConectar().addActionListener(new ConectarServidorListener());
    }

    class ConectarServidorListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String ip = view.getEnderecoServidor().getText();
            String porta = view.getPortaField().getText();
            try {
                model.conectar(ip, Integer.parseInt(porta));
                view.dispose(); // Fechar tela de conexão
                loginView = new LoginView();
                loginView.setVisible(true);
                loginView.adicionarActionListenerLogin(new LoginListener());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String ra = loginView.getRa();
            String senha = loginView.getPassword();
            try {
                // Enviar mensagem de login ao servidor
                String mensagemLogin = String.format(
                    "{\"operacao\":\"login\",\"ra\":%s,\"senha\":\"%s\"}", ra, senha
                );
                model.enviarMensagem(mensagemLogin);
                
                // Receber resposta do servidor
                String resposta = model.receberResposta();
                if (resposta != null && resposta.contains("\"status\":200")) {
                    loginView.dispose(); // Fechar a tela de login
                    AvisosView avisosView = new AvisosView();
                    avisosView.setVisible(true); // Abre a tela de avisos
                } else {
                    JOptionPane.showMessageDialog(loginView, "Login falhou. Verifique suas credenciais.");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

