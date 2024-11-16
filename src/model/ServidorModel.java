package model;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.sql.SQLException;

public class ServidorModel {
    private ServerSocket serverSocket;
    private Connection connection;

    // Conectar ao banco de dados
    public void conectarBD() throws SQLException {
        try {
            String url = "jdbc:mysql://localhost:3306/sistemaavisos";
            String usuario = "root";
            String senha = "";
            connection = DriverManager.getConnection(url, usuario, senha);
        } catch (SQLException e) {
            throw new SQLException("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    // Validar login
    public boolean validarLogin(int ra, String senha) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE ra = ? AND senha = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, ra);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Se encontrar o usuário, retorna true
        }
    }

    // Realizar cadastro
    public boolean cadastrarUsuario(int ra, String senha, String nome) throws SQLException {
        String sql = "INSERT INTO usuarios (ra, senha, nome) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, ra);
            stmt.setString(2, senha);
            stmt.setString(3, nome);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public void iniciarServidor(int porta) throws IOException {
        serverSocket = new ServerSocket(porta, 50, InetAddress.getByName("0.0.0.0"));
    }

    public Socket esperarConexao() throws IOException {
        return serverSocket.accept();
    }

    public void fecharServidor() throws IOException {
        if (serverSocket != null) {
            serverSocket.close();
        }
    }

    public void lidarComCliente(Socket socketCliente, ServidorListener listener) {
        new Thread(() -> {
            try (
                PrintWriter saida = new PrintWriter(socketCliente.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()))
            ) {
                String linha;
                while ((linha = entrada.readLine()) != null) {
                    // Processa a mensagem JSON do cliente
                    String resposta = processarMensagem(linha);
                    saida.println(resposta);
                }
            } catch (IOException e) {
                listener.onErro("Erro na comunicação com o cliente: " + e.getMessage());
            } finally {
                try {
                    socketCliente.close();
                } catch (IOException e) {
                    listener.onErro("Erro ao fechar a conexão com o cliente: " + e.getMessage());
                }
            }
        }).start();
    }

    private String processarMensagem(String mensagem) {
        // Exemplo de parsing da mensagem JSON (usando biblioteca como org.json ou Gson)
        // Aqui você irá converter a mensagem JSON para um objeto e processá-la de acordo
        // com a operação (login ou cadastro)
        JSONObject json = new JSONObject(mensagem);
        String operacao = json.getString("operacao");

        switch (operacao) {
            case "login":
                int raLogin = json.getInt("ra");
                String senhaLogin = json.getString("senha");
                try {
                    if (validarLogin(raLogin, senhaLogin)) {
                        return "{\"status\": 200, \"token\": \"username\"}";
                    } else {
                        return "{\"status\": 401, \"mensagem\": \"Erro ao realizar login.\"}";
                    }
                } catch (SQLException e) {
                    return "{\"status\": 500, \"mensagem\": \"Erro ao acessar o banco de dados.\"}";
                }

            case "cadastrarUsuario":
                int raCadastro = json.getInt("ra");
                String senhaCadastro = json.getString("senha");
                String nomeCadastro = json.getString("nome");
                try {
                    if (cadastrarUsuario(raCadastro, senhaCadastro, nomeCadastro)) {
                        return "{\"status\": 201, \"mensagem\": \"Cadastro realizado com sucesso.\"}";
                    } else {
                        return "{\"status\": 401, \"mensagem\": \"Erro ao cadastrar.\"}";
                    }
                } catch (SQLException e) {
                    return "{\"status\": 500, \"mensagem\": \"Erro ao acessar o banco de dados.\"}";
                }

            default:
                return "{\"status\": 400, \"mensagem\": \"Operação inválida.\"}";
        }
    }

    public interface ServidorListener {
        void onConexao(String clienteInfo);
        void onMensagemRecebida(String mensagem);
        void onDesconexao();
        void onErro(String erro);
    }
}
