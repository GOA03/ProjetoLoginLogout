package model;

import java.io.*;
import java.net.*;

public class ClienteModel {
    private Socket socketEcho;
    private PrintWriter saida;
    private BufferedReader entrada;

    // Conectar ao servidor (método sincronizado)
    public synchronized void conectar(String ip, int porta) throws IOException {
        socketEcho = new Socket(ip, porta);
        saida = new PrintWriter(socketEcho.getOutputStream(), true);
        entrada = new BufferedReader(new InputStreamReader(socketEcho.getInputStream()));
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
}
