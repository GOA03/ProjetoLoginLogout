package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Pattern;

import dao.BancoDados;
import dao.UsuarioDAO;
import enums.CadastroEnum;
import enums.RaEnum;
import model.UsuarioModel;

public class CadastroController {

	public CadastroEnum validarCadastro(UsuarioModel usuario) throws IOException {

		Boolean nomeValido = validarNome(usuario.getNome());
		RaEnum raValido = validarRa(usuario.getRa());
		Boolean senhaValida = validarSenha(usuario.getSenha());
		System.out.println("USUARIO: " + nomeValido + " RA: " + raValido + " Senha: " + senhaValida);

		if (!nomeValido) {
			return CadastroEnum.ERRO;
		} else if (raValido == RaEnum.JA_CADASTRADO) {
			return CadastroEnum.RA_CADASTRADO;
		} else if (raValido == RaEnum.CARACTERES_INVALIDOS) {
			return CadastroEnum.ERRO;
		} else if (!senhaValida) {
			return CadastroEnum.ERRO;
		} else {
			return CadastroEnum.SUCESSO;
		}
	}

	public boolean validarNome(String nomeUsuario) throws IOException {
		// Nome deve ser preenchido, no máximo 50 caracteres, apenas letras maiúsculas
		if (nomeUsuario == null || nomeUsuario.isEmpty() || nomeUsuario.length() > 50) {
			return false;
		}

		// Verificar se o nome contém apenas letras maiúsculas, sem acentuação
		String regexNome = "^[A-Z]+$";
		if (!Pattern.matches(regexNome, nomeUsuario)) {
			return false;
		}
		return true;
	}

	private RaEnum validarRa(String ra) throws IOException {
		// Verificar se o RA é válido (7 dígitos numéricos sem "a" como prefixo)
		if (ra == null || ra.length() != 7 || !ra.matches("\\d{7}")) {
			return RaEnum.CARACTERES_INVALIDOS;
		}

		try (Connection conn = BancoDados.conectar()) {
			Boolean resposta = new UsuarioDAO(conn).validarRa(ra); // Valida se o RA já existe
			if (resposta) {
				return RaEnum.SUCESSO;
			} else {
				return RaEnum.JA_CADASTRADO;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return RaEnum.ERRO;
		}
	}

	private Boolean validarSenha(String senha) {
		// Verificar se a senha está entre 8 e 20 caracteres e contém apenas letras (sem
		// acentuação)
		if (senha == null || senha.length() < 8 || senha.length() > 20) {
			return false;
		}

		// Verificar se a senha contém apenas letras (sem caracteres especiais)
		String regexSenha = "^[A-Za-z]{8,20}$"; // Apenas letras, entre 8 e 20 caracteres
		if (!Pattern.matches(regexSenha, senha)) {
			return false;
		}
		return true;
	}
}
