package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField raField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    public LoginView() {
        setTitle("Login");
        setSize(400, 184);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        getContentPane().add(panel);

        JLabel lblRa = new JLabel("RA:");
        raField = new JTextField();
        raField.setPreferredSize(new Dimension(150, 25)); // Ajusta o tamanho do campo

        JLabel lblSenha = new JLabel("Senha:");
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(150, 25)); // Ajusta o tamanho do campo

        loginButton = new JButton("Login");
        registerButton = new JButton("Cadastrar");

        // Usando GroupLayout para garantir compatibilidade com WindowBuilder
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lblRa)
                    .addComponent(lblSenha))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(raField)
                    .addComponent(passwordField)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(loginButton)
                        .addComponent(registerButton)))
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGap(30) // Espaço do topo
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRa)
                    .addComponent(raField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSenha)
                    .addComponent(passwordField))
                .addGap(20) // Espaço entre os campos e os botões
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(loginButton)
                    .addComponent(registerButton))
                .addGap(30) // Espaço do fundo
        );

        // Adicionar ActionListener para o botão Cadastrar
        registerButton.addActionListener(e -> {
            this.dispose(); // Fecha a tela de login
            new CadastroView().setVisible(true); // Abre a tela de cadastro
        });
    }

    public String getRa() {
        return raField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void adicionarActionListenerLogin(ActionListener listener) {
        loginButton.addActionListener(listener);
    }
}
