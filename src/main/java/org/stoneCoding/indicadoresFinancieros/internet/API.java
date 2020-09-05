package org.stoneCoding.indicadoresFinancieros.internet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;

import org.stoneCoding.indicadoresFinancieros.main.tools;

public abstract class API {
	
	////// MÉTODOS CON FUNCIONALIDAD //////
	
	   /**
	   * Envía una petición HTTP
	   * @param URL : URL base.
	   * @param Method : Método [GET, POST, DELETE]
	   * @param header : Mapeo de la cabecera.
	   * @param Properties : Mapeo de las propiedades.
	   * @return Object[] : Se recibe respuesta.
	   */	
	protected static Object[] HTTPRequest(String URL, String Method, JSONObject header, JSONObject Properties) throws Exception {
		Integer Code = null;
		String ResultString = null;
		
		if(URL==null) { return null; }
		String _Method = Method!=null?Method:"GET";
		JSONObject _Header = header!=null?header:new JSONObject();
		JSONObject _Properties = Properties!=null?Properties:new JSONObject();
		
		StringBuilder resultado = new StringBuilder();
		URL url = new URL(URL);
		HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
		conexion.setRequestMethod(_Method);
		
		switch(_Method) {
		case "GET":
			conexion.setDoInput(true);
			break;
		case "POST":
			conexion.setDoInput(true);
			conexion.setDoOutput(true);
			break;
		case "DELETE":
			conexion.setDoInput(true);
			break;
		default:
			conexion.setDoInput(true);
			break;
		}
		
		if(!_Header.isEmpty()) {
			Set keySet = _Header.keySet();
			keySet.forEach(item -> {
				conexion.setRequestProperty(item.toString(), _Header.get(item).toString());
			});
		}
		
		if (!_Properties.isEmpty() && conexion.getDoOutput()) {
			OutputStream os = conexion.getOutputStream();
	        BufferedWriter writer = new BufferedWriter(
	                new OutputStreamWriter(os, tools.encode));
	        writer.write(_Properties.toJSONString());

	        writer.flush();
	        writer.close();
	        os.close();			
		}
		
		Code = Integer.valueOf(conexion.getResponseCode());
		BufferedReader rd;
		switch(conexion.getResponseCode()) {
		case 200:
			rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
			break;
		case 201:
			rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
			break;
		case 401:
			rd = new BufferedReader(new InputStreamReader(conexion.getErrorStream()));
			break;
		default:
			rd = new BufferedReader(new InputStreamReader(conexion.getErrorStream()));
			break;
		}

		String linea;
		while ((linea = rd.readLine()) != null) {
			resultado.append(linea);
		}
		rd.close();
		ResultString = resultado.toString();
		
		return new Object[] {Code, ResultString};
	}	
	
	   /**
	   * Indica si la respuesta ha sido positiva o no.
	   * @param value : Código de respuesta.
	   * @return boolean : true o false.
	   */	
	public boolean isResponseSuccess(int value) {
		switch (value) {
			case 200:
				return true;
			case 201:
				return true;
			default:
				return false;
		}
	}	
	
	   /**
	   * Transforma JSONObject+Base URL en una Query
	   * @param obj : Orden.
	   * @param base_url : URL base.
	   * @return String : Query
	   */	
	protected String objToQuery(JSONObject obj, String base_url) {
		JSONObject input = obj!=null?obj:new JSONObject();
		List<String> parts = new ArrayList<String>();
		Set<String> keys = input.keySet();
		
		String command;
		switch (base_url.substring(base_url.length()-1)) {
		case "/":
			command = "";
			break;
		default:
			command = "?";
			break;
		}
		
		keys.forEach(item -> {
			try {
				parts.add(URLEncoder.encode(item, tools.encode) + "=" + 
						URLEncoder.encode(input.get(item).toString(), tools.encode) + "&");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		if(parts.isEmpty()) {
			return base_url;
		} else {
			String query_url = "";
			for(int i=0; i<parts.size(); i++) {
				query_url+=parts.get(i);
			}
			return base_url + command + query_url.substring(0, query_url.length()-1);
		}
	}	
}
