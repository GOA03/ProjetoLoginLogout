package controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import model.RespostaModel;
import model.UsuarioModel;

public class JSONController {

    @SuppressWarnings("unchecked") // Suprime o alerta de tipo não parametrizado
    // Método para converter um objeto UsuarioModel em JSON
    public JSONObject changeToJSON(UsuarioModel usuario) {
        JSONObject user = new JSONObject();
        
        user.put("operacao", usuario.getOperacao());
        
        if (usuario.getRa() != null) {
            user.put("ra", usuario.getRa());
        }
        
        if (usuario.getRa() != null) {
            user.put("token", usuario.getRa());
        }
        
        if (usuario.getSenha() != null) {
            user.put("senha", usuario.getSenha());
        }
        
        if (usuario.getNome() != null) {
            user.put("nome", usuario.getNome());
        }
        
        return user;
    }

    // Método para converter um JSON em um objeto UsuarioModel
    public UsuarioModel changeToObject(String jsonString) throws ParseException {
        JSONParser parser = new JSONParser();
        
        // Convertendo a string JSON para um objeto JSONObject
        JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
        
        // Criando um novo objeto UsuarioModel e preenchendo com os dados do JSON
        UsuarioModel usuario = new UsuarioModel();
        
        if (jsonObject.containsKey("ra")) {
            usuario.setRa((String) jsonObject.get("ra"));
        }
        
        if (jsonObject.containsKey("senha")) {
            usuario.setSenha((String) jsonObject.get("senha"));
        }
        
        if (jsonObject.containsKey("nome")) {
            usuario.setNome((String) jsonObject.get("nome"));
        }
        
        if (jsonObject.containsKey("operacao")) {
            usuario.setOperacao((String) jsonObject.get("operacao"));
        }
        
        return usuario;
    }

    public String getOperacao(String mensagemRecebida) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(mensagemRecebida);
            String operacao = (String) jsonObject.get("operacao");
            return operacao;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mensagemRecebida;
    }

    public UsuarioModel changeLoginToJSON(String mensagemRecebida) {
        UsuarioModel usuario = new UsuarioModel();
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(mensagemRecebida);

            String ra = (String) jsonObject.get("ra"); 
            usuario.setRa(ra);

            String senha = (String) jsonObject.get("senha");
            usuario.setSenha(senha);

            return usuario;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked") // Suprime o alerta de tipo não parametrizado
    public JSONObject changeResponseToJson(RespostaModel resposta) {
        JSONObject res = new JSONObject();
        
        res.put("operacao", resposta.getOperacao());
        res.put("ra", resposta.getRa());
        
        if (resposta.getSenha() != null) {
            res.put("senha", resposta.getSenha());
        }
        if (resposta.getNome() != null) {
            res.put("nome", resposta.getNome());
        }
        res.put("status", resposta.getStatus());
        if (resposta.getToken() != null) {
            res.put("token", resposta.getToken());
        }
        
        if(resposta.getMsg() != null) {
        	res.put("mensagem", resposta.getMsg());
        }
        return res;
    }
    
    @SuppressWarnings("unchecked") // Suprime o alerta de tipo não parametrizado
    public JSONObject changeLogoutToJson(RespostaModel resposta) {
    	
        JSONObject res = new JSONObject(); // Usando JSONObject que internamente é um HashMap<String, Object>
        
        res.put("operacao", resposta.getOperacao());
        
        res.put("status", resposta.getStatus());
        
        res.put("token", resposta.getToken());
        
        return res;
    }
    
    public UsuarioModel changeLogoutToJSON(String mensagemRecebida) {
        UsuarioModel usuario = new UsuarioModel();
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(mensagemRecebida);

            String ra = (String) jsonObject.get("ra"); 
            usuario.setRa(ra);

            return usuario;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public RespostaModel changeResponseToJson(String msg) {
        RespostaModel respostaModel = new RespostaModel();
        JSONParser parser = new JSONParser();
        
        try {
            // Parse a string JSON para um objeto JSONObject
            JSONObject jsonObject = (JSONObject) parser.parse(msg);
            
            // Mapeando os dados do JSON para o modelo RespostaModel
            if (jsonObject.containsKey("operacao")) {
                respostaModel.setOperacao((String) jsonObject.get("operacao"));
            }
            
            if (jsonObject.containsKey("ra")) {
                respostaModel.setRa((String) jsonObject.get("ra"));
            }
            
            if (jsonObject.containsKey("senha")) {
                respostaModel.setSenha((String) jsonObject.get("senha"));
            }
            
            if (jsonObject.containsKey("nome")) {
                respostaModel.setNome((String) jsonObject.get("nome"));
            }
            
            if (jsonObject.containsKey("status")) {
                respostaModel.setStatus(Integer.parseInt(jsonObject.get("status").toString()));
            }
            
            if (jsonObject.containsKey("token")) {
                respostaModel.setToken((String) jsonObject.get("token"));
            }
            
            if (jsonObject.containsKey("mensagem")) {
                respostaModel.setMsg((String) jsonObject.get("mensagem"));
            }
            
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        return respostaModel;
    }

	public UsuarioModel changeRegisterJSON(String mensagemRecebida) {
		
		UsuarioModel usuario = new UsuarioModel();
		JSONParser parser = new JSONParser();
		
		try {
			
			JSONObject jsonObject = (JSONObject) parser.parse(mensagemRecebida);
			
			usuario.setNome((String) jsonObject.get("nome"));
			usuario.setRa((String) jsonObject.get("ra"));
			usuario.setSenha((String) jsonObject.get("senha"));
			
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
		return usuario;
	}
	
	 @SuppressWarnings("unchecked") // Suprime o alerta de tipo não parametrizado
	public JSONObject changeErroGlobalToJson(RespostaModel resposta) {
		 JSONObject res = new JSONObject(); // Usando JSONObject que internamente é um HashMap<String, Object>
	        
	        res.put("operacao", resposta.getOperacao());
	        
	        res.put("status", resposta.getStatus());
	        
	        res.put("mensagem", resposta.getMsg());
	        
	        return res;
	}
}