package org.stoneCoding.indicadoresFinancieros.internet;

import org.json.simple.JSONObject;

public class APIBuilder {
	/**
	 * Construye API según JSONObject
	 * @param keys JSONObject
	 * @return API
	 */
	public static API build(JSONObject keys) {
		String APIName = keys.get("APIName").toString();
		
		switch(APIName.toLowerCase()) {
			case "cryptocompare":
				return new APICryptocompare();
			default:
				return null;
		}
	}
	
	/**
	 * Construye API de datos según JSONObject
	 * @param keys JSONObject
	 * @return APILiveData
	 */
	public static APILiveData buildAPILiveData(JSONObject keys) {
		String APIName = keys.get("APIName").toString();
		
		switch(APIName.toLowerCase()) {
			case "cryptocompare":
				return new APICryptocompare();
			default:
				return null;
		}
	}
	
	/**
	 * Construye API de datos según nombre.
	 * @param company Nombre de la entidad.
	 * @return APILiveData
	 */
	public static APILiveData buildAPILiveData(String company) {
		switch(company) {
		case "cryptocompare":
			return new APICryptocompare();
		default:
			return null;
		}
	}
}
