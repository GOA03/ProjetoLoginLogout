package model;

public class UsuarioModel {
    private int id;
    private int ra;
    private String nome;
    private String senha;
    private String token;

    // Construtor
    public UsuarioModel() {}

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getRa() { return ra; }
    public void setRa(int ra) { this.ra = ra; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
