package org.stoneCoding.indicadoresFinancieros.internet;

import java.util.List;

import org.json.simple.JSONObject;

import org.stoneCoding.indicadoresFinancieros.market.Chart;

public abstract class APILiveData extends API {

	/**
	 * Solicita precio de mercado
	 * @param base Activo solicitado
	 * @param quote Activo par comparativo
	 * @param ApiKey APIKey de Cryptocompare. Puede ser nula.
	 * @return String del JSON
	 */
	public abstract String getMarketPrice(String base, String quote, String ApiKey);
	
	/**
	 * Solicita histórico de precios de mercado.
	 * @param base Activo solicitado
	 * @param quote Activo par comparativo
	 * @param msPeriod Milisegundos del periodo
	 * @param nPeriods Número de periodos
	 * @param ApiKey APIKey de Cryptocompare. Puede ser nula.
	 * @return String del JSON
	 */
	public abstract String getMarketHistory(String base, String quote, Long msPeriod, Short nPeriods, String ApiKey);
	
	   /**
	   * Realiza una llamada para pedir información.
	   * @param comando : Introducir comando.
	   * @param base : Listado de tokens base.
	   * @param market : Listado de tokens objetivo.
	   * @param ApiKey : Clave privada.
	   * @return List(String) : Listado.
	   */
	public abstract List<String> getMarketInfo(String comando, List<String> base, List<String> market, String ApiKey);

	   /**
	   * Realiza una llamada para pedir el top de tokens.
	   * @param num : Número de tokens del top.
	   * @param baseSymbol : Base del token.
	   * @param ApiKey : Clave privada.
	   * @return List(String) : Listado.
	   */
	public abstract List<String> getTopTokens(int num, String baseSymbol, String ApiKey);
	
	   /**
	   * Actualiza los precios de las tokens en el market.
	   * @param ApiKeys : Clave privada.
	   * @param symbolFrom : Listado de símbolos base.
	   * @param symbolTo : Listado de símbolos objetivo.
	   * @param market : Objeto mercado.
	   */
	public abstract void updatePairPrice(JSONObject data, Chart nMarket);
	
	   /**
	   * Actualiza los precios de las tokens en el market.
	   * @param ApiKeys : Clave privada.
	   * @param symbolFrom : Listado de símbolos base.
	   * @param symbolTo : Listado de símbolos objetivo.
	   * @param market : Objeto mercado.
	   */	
	public abstract void updatePairHistcal(JSONObject data, String symbolFrom, String symbolTo, Chart nMarket);
}
