package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.UsuarioModel;

public class UsuarioDAO {

    private Connection conn;

    public UsuarioDAO(Connection conn) {
        this.conn = conn;
    }

    // Método para adicionar um usuário no banco
    public void adicionarUsuario(UsuarioModel usuario) throws SQLException, IOException {
        String query = "INSERT INTO usuario (ra, senha, nome) VALUES (?, ?, ?)";
        
        try (Connection conn = BancoDados.conectar();  // Usando a conexão do BancoDados
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, usuario.getRa());  // Alterado para String
            stmt.setString(2, usuario.getSenha());  // Setando a senha
            stmt.setString(3, usuario.getNome());  // Setando o nome

            stmt.executeUpdate();  // Executando a inserção
            
        } catch (SQLException e) {
            throw new SQLException("Erro ao adicionar o usuário", e);
        }
    }

    // Método para remover um usuário
    public void removerUsuarioCandidato(UsuarioModel usuario) throws SQLException, IOException {
        String query = "DELETE FROM usuario WHERE ra = ?";
        
        try (Connection conn = BancoDados.conectar();  // Usando a conexão do BancoDados
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, usuario.getRa());  // Alterado para String

            stmt.executeUpdate();  // Executando a remoção
        } catch (SQLException e) {
            throw new SQLException("Erro ao remover o usuário", e);
        }
    }
    
    // Método para atualizar as informações de um usuário
    public void atualizarUsuario(UsuarioModel usuario) throws SQLException, IOException {
        String query = "UPDATE usuario SET senha = ?, nome = ? WHERE ra = ?";

        try (Connection conn = BancoDados.conectar();  // Usando a conexão do BancoDados
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, usuario.getSenha());  // Setando a nova senha
            stmt.setString(2, usuario.getNome());  // Setando o novo nome
            stmt.setString(3, usuario.getRa());  // Alterado para String

            stmt.executeUpdate();  // Executando a atualização

        } catch (SQLException e) {
            throw new SQLException("Erro ao atualizar o usuário", e);
        }
    }

    public Boolean loginUsuario(String ra, String senha) { // Alterado para String
        String query = "SELECT * FROM usuario WHERE ra = ? AND senha = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, ra); // Alterado para String
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Retorna true se houver um registro correspondente
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retorna false em caso de erro
        }
    }

    public Connection getConn() {
        return conn;
    }

    public String getRA(String ra) throws SQLException { // Alterado para String
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM usuario WHERE ra = ?");
            st.setString(1, ra); // Alterado para String
            rs = st.executeQuery();
            if (rs.next()) {
                System.out.println("=== Ra Encontrado ===");
                String token = rs.getString("ra"); // Alterado para String
                return token;
            } else {        
                System.out.println("=== Ra Não Encontrado ===");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            BancoDados.finalizarStatement(st);
            BancoDados.desconectar();
        }
    }
}