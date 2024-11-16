package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import dao.BancoDados;
import dao.UsuarioDAO;
import enums.LoginEnum;
import model.UsuarioModel;

public class LoginController {
	
	public LoginEnum validarLogin(UsuarioModel usuario) throws IOException {
		Boolean isCorrectLogin = validarDados(usuario);
		if(isCorrectLogin) {
			return LoginEnum.SUCESSO;
		}else {
			return LoginEnum.ERRO_USUARIO_E_SENHA;
		}
	}
	
	public Boolean validarDados(UsuarioModel usuario) throws IOException {
			try {
    			Connection conn = BancoDados.conectar();
    			Integer ra = usuario.getRa();
    			String senha = usuario.getSenha();
    			Boolean response = new UsuarioDAO(conn).loginUsuario(ra, senha);
    			BancoDados.desconectar();
    			return response;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return false;
	}

	public Integer getRa(Integer ra) throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		Integer token = new UsuarioDAO(conn).getRA(ra);
		if(token == null) {
			System.out.println("TOKEN NULO");
		}
		BancoDados.desconectar();
		
		return token;
	}


}
