package model;

public class UsuarioModel {

	private Integer ra;
	private String senha;
	private String nome;
	private String operacao;
	
	public Integer getRa() {
		return ra;
	}
	public void setRa(Integer ra) {
		this.ra = ra;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getOperacao() {
		return operacao;
	}
	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}
	
	@Override
	public String toString() {
		return "UsuarioModel [ra=" + ra + ", senha=" + senha + ", nome=" + nome + ", operacao=" + operacao + "]";
	}
}