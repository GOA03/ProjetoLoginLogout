package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.ClienteModel;

public class ClienteView extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField enderecoServidor;
	private JTextField portaField;
	private JButton btnConectar;
	private String ip;
	private String porta;
	private ClienteModel clienteModel;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClienteView frame = new ClienteView();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ClienteView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 275);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.windowBorder);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(248, 248, 255));
		panel.setBounds(77, 10, 282, 222);
		contentPane.add(panel);
		panel.setLayout(null);

		enderecoServidor = new JTextField();
		enderecoServidor.setFont(new Font("Poppins", Font.PLAIN, 12));
		enderecoServidor.setBackground(new Color(220, 220, 220));
		enderecoServidor.setBounds(24, 72, 234, 30);
		panel.add(enderecoServidor);
		enderecoServidor.setColumns(10);

		JLabel lblNewLabel = new JLabel("Cliente");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Poppins", Font.PLAIN, 16));
		lblNewLabel.setBounds(24, 15, 234, 30);
		panel.add(lblNewLabel);

		JLabel lblIp = new JLabel("IP");
		lblIp.setHorizontalAlignment(SwingConstants.LEFT);
		lblIp.setFont(new Font("Poppins", Font.PLAIN, 12));
		lblIp.setBounds(23, 45, 217, 30);
		panel.add(lblIp);

		btnConectar = new JButton("ENTRAR");
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ip = enderecoServidor.getText();
				porta = portaField.getText();
				conectarServidor(ip, porta);
			}
		});
		btnConectar.setFont(new Font("Poppins", Font.PLAIN, 12));
		btnConectar.setBounds(73, 175, 136, 30);
		panel.add(btnConectar);

		JLabel lblPorta = new JLabel("PORTA");
		lblPorta.setHorizontalAlignment(SwingConstants.LEFT);
		lblPorta.setFont(new Font("Poppins", Font.PLAIN, 12));
		lblPorta.setBounds(23, 105, 217, 30);
		panel.add(lblPorta);

		portaField = new JTextField();
		portaField.setFont(new Font("Poppins", Font.PLAIN, 12));
		portaField.setBackground(new Color(220, 220, 220));
		portaField.setColumns(10);
		portaField.setBounds(24, 130, 234, 30);
		panel.add(portaField);
	}

	// Método para conectar ao servidor
	private void conectarServidor(String ip, String porta) {
		try {

			int portaServidor = Integer.parseInt(porta);
			clienteModel = new ClienteModel(); // Cria uma instância do ClienteModel
			clienteModel.conectar(ip, portaServidor); // Conecta ao servidor com IP e porta

			// Se a conexão for bem-sucedida, abre a tela de login
			JOptionPane.showMessageDialog(this, "Conectado ao servidor com sucesso!");
			this.dispose(); // Fecha a janela atual
			new LoginView().setVisible(true); // Abre a janela de login

		} catch (NumberFormatException e) {

			JOptionPane.showMessageDialog(this, "Porta inválida!");

		} catch (Exception e) {

			JOptionPane.showMessageDialog(this, "Erro ao conectar ao servidor: " + e.getMessage());
		}
	}

	public JTextField getEnderecoServidor() {
		return enderecoServidor;
	}

	public JTextField getPortaField() {
		return portaField;
	}

	public JButton getBtnConectar() {
		return btnConectar;
	}
}
