package enums;

public enum LoginEnum {
	SUCESSO(200),
	ERRO_USUARIO_E_SENHA(401);

	private final int valor;
	
	LoginEnum(int valor) {
		this.valor = valor;
	}
}