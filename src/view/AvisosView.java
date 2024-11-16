package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AvisosView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JCheckBox chkEsportes;
    private JCheckBox chkClima;
    private JCheckBox chkPolitica;
    private JCheckBox chkEconomia;
    private JCheckBox chkEntretenimento;
    private JTextArea avisosArea;
    private JButton btnAtualizar;

    public AvisosView() {
        setTitle("Avisos e Notícias");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 500);
        contentPane = new JPanel();
        contentPane.setBackground(SystemColor.windowBorder); // Cor de fundo da janela
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(248, 248, 255)); // Cor de fundo do painel
        panel.setBounds(20, 15, 400, 433);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblTitulo = new JLabel("Avisos e Notícias");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Poppins", Font.PLAIN, 16));
        lblTitulo.setBounds(80, 10, 240, 25);
        panel.add(lblTitulo);

        // Criação das caixas de seleção (CheckBoxes) para os tópicos pré-definidos
        chkEsportes = new JCheckBox("Esportes");
        chkEsportes.setFont(new Font("Poppins", Font.PLAIN, 12));
        chkEsportes.setBounds(20, 45, 130, 25);
        panel.add(chkEsportes);

        chkClima = new JCheckBox("Clima");
        chkClima.setFont(new Font("Poppins", Font.PLAIN, 12));
        chkClima.setBounds(150, 45, 130, 25);
        panel.add(chkClima);

        chkPolitica = new JCheckBox("Política");
        chkPolitica.setFont(new Font("Poppins", Font.PLAIN, 12));
        chkPolitica.setBounds(280, 45, 100, 25);
        panel.add(chkPolitica);

        chkEconomia = new JCheckBox("Economia");
        chkEconomia.setFont(new Font("Poppins", Font.PLAIN, 12));
        chkEconomia.setBounds(80, 72, 130, 25);
        panel.add(chkEconomia);

        chkEntretenimento = new JCheckBox("Entretenimento");
        chkEntretenimento.setFont(new Font("Poppins", Font.PLAIN, 12));
        chkEntretenimento.setBounds(210, 72, 130, 25);
        panel.add(chkEntretenimento);

        // Reposicionamento do botão para abaixo das caixas de seleção
        btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setFont(new Font("Poppins", Font.PLAIN, 12));
        btnAtualizar.setBounds(150, 107, 100, 25);
        panel.add(btnAtualizar);

        avisosArea = new JTextArea();
        avisosArea.setFont(new Font("Poppins", Font.PLAIN, 12));
        avisosArea.setEditable(false);
        avisosArea.setLineWrap(true);
        avisosArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(avisosArea); // Adiciona barra de rolagem
        scrollPane.setBounds(20, 147, 360, 265);
        panel.add(scrollPane);
    }

    // Getters para os checkboxes e botão de cadastro
    public JCheckBox getChkEsportes() {
        return chkEsportes;
    }

    public JCheckBox getChkClima() {
        return chkClima;
    }

    public JCheckBox getChkPolitica() {
        return chkPolitica;
    }

    public JCheckBox getChkEconomia() {
        return chkEconomia;
    }

    public JCheckBox getChkEntretenimento() {
        return chkEntretenimento;
    }

    public JButton getBtnAtualizar() {
        return btnAtualizar;
    }

    public JTextArea getAvisosArea() {
        return avisosArea;
    }

    // Método para adicionar avisos/notícias à área de texto
    public void adicionarAviso(String aviso) {
        avisosArea.append(aviso + "\n");
    }

    // Método main para executar a view
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AvisosView frame = new AvisosView();
                    frame.setLocationRelativeTo(null); // Centraliza a janela
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
