package controller;

import model.UsuarioModel;
import view.LoginView;
import org.json.JSONObject;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginController {

    private UsuarioModel usuario;
    private LoginView loginView;

    // URL do servidor para autenticação
    private static final String SERVIDOR_URL = "http://localhost:8080/api/login";

    public LoginController() {
        this.usuario = new UsuarioModel();
        this.loginView = new LoginView();

        this.loginView.setVisible(true);

        this.loginView.adicionarActionListenerLogin(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processarLogin();
            }
        });
    }

    private void processarLogin() {
        String ra = loginView.getRa();
        String senha = loginView.getSenha();

        try {
            String token = autenticar(ra, senha);

            if (token != null) {
                usuario.setRa(Integer.parseInt(ra));
                usuario.setToken(token);
                JOptionPane.showMessageDialog(loginView, "Login realizado com sucesso!");
                // Redireciona o usuário para a próxima etapa
            } else {
                JOptionPane.showMessageDialog(loginView, "Credenciais incorretas.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(loginView, "Erro ao se conectar com o servidor: " + e.getMessage());
        }
    }

    private String autenticar(String ra, String senha) throws Exception {
        URL url = new URL(SERVIDOR_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // Construindo JSON com dados de login
        JSONObject loginData = new JSONObject();
        loginData.put("ra", ra);
        loginData.put("senha", senha);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(loginData.toString().getBytes("UTF-8"));
        }

        // Verifica o código de resposta
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Lê o token retornado
            try (InputStream is = connection.getInputStream()) {
                byte[] responseBytes = is.readAllBytes();
                String responseBody = new String(responseBytes, "UTF-8");
                JSONObject responseJson = new JSONObject(responseBody);
                return responseJson.getString("token"); // Retorna o token
            }
        } else {
            return null;
        }
    }
}
