package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CadastroView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField nomeField, raField;
    private JPasswordField passwordField;
    private JButton cadastrarButton, voltarButton;

    public CadastroView() {
        setTitle("Cadastro");
        setSize(400, 213);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        getContentPane().add(panel);

        JLabel lblNome = new JLabel("Nome:");
        nomeField = new JTextField();
        nomeField.setPreferredSize(new Dimension(150, 25)); // Ajusta o tamanho do campo

        JLabel lblRa = new JLabel("RA:");
        raField = new JTextField();
        raField.setPreferredSize(new Dimension(150, 25)); // Ajusta o tamanho do campo

        JLabel lblSenha = new JLabel("Senha:");
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(150, 25)); // Ajusta o tamanho do campo

        cadastrarButton = new JButton("Cadastrar");
        voltarButton = new JButton("Voltar");

        // Usando GroupLayout para garantir compatibilidade com WindowBuilder
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lblNome)
                    .addComponent(lblRa)
                    .addComponent(lblSenha))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(nomeField)
                    .addComponent(raField)
                    .addComponent(passwordField)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cadastrarButton)
                        .addComponent(voltarButton)))
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGap(20) // Espaço do topo
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNome)
                    .addComponent(nomeField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRa)
                    .addComponent(raField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSenha)
                    .addComponent(passwordField))
                .addGap(20) // Espaço entre os campos e os botões
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(cadastrarButton)
                    .addComponent(voltarButton))
                .addGap(20) // Espaço do fundo
        );

        // Adicionar ActionListener para o botão Voltar
        voltarButton.addActionListener(e -> {
            this.dispose(); // Fecha a tela de cadastro
            new LoginView().setVisible(true); // Abre a tela de login
        });
    }

    public String getNome() {
        return nomeField.getText();
    }

    public String getRa() {
        return raField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void adicionarActionListenerCadastrar(ActionListener listener) {
        cadastrarButton.addActionListener(listener);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CadastroView cadastroView = new CadastroView();
                cadastroView.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
