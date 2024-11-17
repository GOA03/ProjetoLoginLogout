package dao;

import java.io.*;
import java.sql.*;
import java.util.Properties;

import javax.swing.JOptionPane;

public class BancoDados {

	private static Connection conn = null;
	
	public static Connection conectar() throws SQLException, IOException{
		try {
			
			if (conn == null) {
				
				Properties props = carregarPropriedades();
				String url = props.getProperty("dburl");
				conn = DriverManager.getConnection(url, props);
			}
			
			return conn;
		} catch (SQLException e) {
	        JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados: " + e.getMessage(), "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
	        throw e;
	    }
	}
	
	public static Connection desconectar() throws SQLException{
		
		if (conn != null) {
			
			conn.close();
			conn = null;
		}
		
		return conn;
	}
	
	private static Properties carregarPropriedades() throws IOException{
		
		FileInputStream propriedadesBanco = new FileInputStream("database.properties");
		
		Properties props = new Properties();
		props.load(propriedadesBanco);
		
		return props;
	}
	
	public static void finalizarStatement(Statement st) throws SQLException{
		
		if (st != null) {
			
			st.close();
		}
	}
	
	public static void finalizarResultSet(ResultSet rs) throws SQLException{
		
		if (rs != null) {
			
			rs.close();
		}
	}
}
