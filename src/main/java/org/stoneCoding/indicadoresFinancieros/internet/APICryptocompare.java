package org.stoneCoding.indicadoresFinancieros.internet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.stoneCoding.indicadoresFinancieros.main.tools;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.stoneCoding.indicadoresFinancieros.market.Candlestick;
import org.stoneCoding.indicadoresFinancieros.market.Chart;
import org.stoneCoding.indicadoresFinancieros.market.Asset;

public class APICryptocompare extends APILiveData {
	
	public String BaseURL = "https://min-api.cryptocompare.com/data/";
	
	/**
	 * Solicita precio de mercado
	 * @param base Activo solicitado
	 * @param quote Activo par comparativo
	 * @param ApiKey APIKey de Cryptocompare. Puede ser nula.
	 * @return String del JSON
	 */
	public String getMarketPrice(String base, String quote, String ApiKey) {
		Integer fsymsMax = 1000;
		Integer tsymsMax = 100;
		List<String> _marketCalls = new ArrayList<String>();
		List<String> _baseCalls = new ArrayList<String>();
		
		String URL = BaseURL + "pricemultifull";
		
		JSONObject properties = new JSONObject();
		properties.put("fsyms", base);
		properties.put("tsyms", quote);
		if (ApiKey!=null) {
			properties.put("api_key", ApiKey);
		}
		Object[] response;
		try {
			response = HTTPRequest(objToQuery(properties, URL), null, null, null);
			if (isResponseSuccess(Integer.valueOf(response[0].toString()))) {
				return response[1].toString();
			} else {
				return new JSONObject().toJSONString();
			}
		} catch (Exception e) {
			return new JSONObject().toJSONString();
		}
	}
	
	/**
	 * Solicita histórico de precios de mercado.
	 * @param base Activo solicitado
	 * @param quote Activo par comparativo
	 * @param msPeriod Milisegundos del periodo
	 * @param nPeriods Número de periodos
	 * @param ApiKey APIKey de Cryptocompare. Puede ser nula.
	 * @return String del JSON
	 */
	public String getMarketHistory(String base, String quote, Long msPeriod, Short nPeriods, String ApiKey) {
		Integer fixedTotalperiods;
		String URL = BaseURL + "v2/";
		if (msPeriod<tools.ms_1day) {
			URL+="histohour";
			fixedTotalperiods = Double.valueOf((msPeriod/tools.ms_1hr)*nPeriods).intValue();
		} else {
			URL+="histoday";
			fixedTotalperiods = Double.valueOf((msPeriod/tools.ms_1day)*nPeriods).intValue();
		}
		fixedTotalperiods = fixedTotalperiods>2000?2000:fixedTotalperiods;
		
		JSONObject properties = new JSONObject();
		properties.put("fsym", base);
		properties.put("tsym", quote);
		properties.put("limit", fixedTotalperiods);
		if (ApiKey!=null) {
			properties.put("api_key", ApiKey);
		}
		Object[] response;
		try {
			response = HTTPRequest(objToQuery(properties, URL), null, null, null);
			if (isResponseSuccess(Integer.valueOf(response[0].toString()))) {
				return response[1].toString();
			} else {
				return new JSONObject().toJSONString();
			}
		} catch (Exception e) {
			return new JSONObject().toJSONString();
		}		
	}
	
	
	   /**
	   * Realiza una llamada pública a Cryptocompare para pedir información.
	   * @param comando : Comandos disponibles -> "precio", "historico".
	   * @param base : Listado de tokens base.
	   * @param market : Listado de tokens objetivo.
	   * @param ApiKey : Clave privada. Puede ser null.
	   * @return List(String) : Listado.
	   */
	public List<String> getMarketInfo(String comando, List<String> base, List<String> market, String ApiKey) {
		List<String> resultado = new ArrayList<String>();
		
		switch(comando) {
		case "precio":
			Integer fsymsMax = 1000;
			Integer tsymsMax = 100;
			List<String> _marketCalls = new ArrayList<String>();
			List<String> _baseCalls = new ArrayList<String>();
			
			String argument = "";
			for (int i=0; i<base.size(); i++) {
				if (argument.length()==0) {
					argument+=base.get(i);
				} else if ((argument.length()+base.get(i).length()+1)<=tsymsMax) {
					argument+=","+base.get(i);
				} else {
					_baseCalls.add(argument);
					argument = "";
				}
			}
			_baseCalls.add(argument);
			
			argument = "";
			for (int i=0; i<market.size(); i++) {
				if (argument.length()==0) {
					argument+=market.get(i);
				} else if ((argument.length()+market.get(i).length()+1)<=fsymsMax) {
					argument+=","+market.get(i);
				} else {
					_marketCalls.add(argument);
					argument = "";
				}
			}
			_marketCalls.add(argument);
			
			_baseCalls.forEach(baseItem -> {
				_marketCalls.forEach(marketItem -> {
					String URL = BaseURL + "pricemultifull";
					
					JSONObject properties = new JSONObject();
					properties.put("fsyms", marketItem);
					properties.put("tsyms", baseItem);
					if (ApiKey!=null) {
						properties.put("api_key", ApiKey);
					}
					Object[] response;
					try {
						response = HTTPRequest(objToQuery(properties, URL), null, null, null);
						if (isResponseSuccess(Integer.valueOf(response[0].toString()))) {
							resultado.add(response[1].toString());
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
				});
			});
			break;
		case "historico":
			Long period = tools.ms_period;
			Integer totalperiods = tools.periods;
			
			base.forEach(baseItem -> {
				market.forEach(marketItem -> {
					Integer fixedTotalperiods;
					String URL = BaseURL + "v2/";
					if (period<tools.ms_1day) {
						URL+="histohour";
						fixedTotalperiods = Double.valueOf((period/tools.ms_1hr)*totalperiods).intValue();
					} else {
						URL+="histoday";
						fixedTotalperiods = Double.valueOf((period/tools.ms_1day)*totalperiods).intValue();
					}
					fixedTotalperiods = fixedTotalperiods>2000?2000:fixedTotalperiods;
					
					JSONObject properties = new JSONObject();
					properties.put("fsym", marketItem);
					properties.put("tsym", baseItem);
					properties.put("limit", fixedTotalperiods);
					if (ApiKey!=null) {
						properties.put("api_key", ApiKey);
					}
					Object[] response;
					try {
						response = HTTPRequest(objToQuery(properties, URL), null, null, null);
						if (isResponseSuccess(Integer.valueOf(response[0].toString()))) {
							resultado.add(response[1].toString());
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			});
			break;
		}
		
		return resultado;
		
	}

	   /**
	   * Realiza una llamada pública a Cryptocompare para pedir el top de tokens.
	   * @param num : Número de tokens del top.
	   * @param baseSymbol : Base del token.
	   * @param ApiKey : Clave privada. Puede ser null.
	   * @return List(String) : Listado.
	   */
	public List<String> getTopTokens(int num, String baseSymbol, String ApiKey) {
		String _baseURL = BaseURL + "top/mktcapfull";
		JSONObject arguments = new JSONObject();
		
		arguments.put("limit", num);
		arguments.put("tsym", baseSymbol);
		if (ApiKey!=null) {
			arguments.put("api_key", ApiKey);
		}
		
		String URL = objToQuery(arguments, _baseURL);
		Object[] response = new Object[] {Integer.valueOf(0), new String()};
		try {
			response = HTTPRequest(URL, null, null, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<String> resultado = new ArrayList<String>();
		if (isResponseSuccess(Integer.valueOf(response[0].toString()))) {
			JSONObject topTokens = tools.StringToJSON(response[1].toString());
			
			if (topTokens.containsKey("Message") && topTokens.get("Message").toString().contentEquals("Success")) {
				((JSONArray)topTokens.get("Data")).forEach(item -> {
					JSONObject _sublista = (JSONObject)((JSONObject)item).get("CoinInfo");
					resultado.add(((JSONObject)_sublista).get("Name").toString());
				});			
			}			
		}
		
		return resultado;
	}
	
	   /**
	   * Actualiza los precios de las tokens en el market.
	   * @param data : JSONObject con los datos de los precios
	   */	
	public void updatePairPrice(JSONObject data, Chart nMarket) {
		final JSONObject raw;
		Set<String> responseKeyData = data.keySet();
		if (responseKeyData.contains("RAW")) {
			raw = (JSONObject)data.get("RAW");
		} else {
			raw = new JSONObject();
		}
		Set<String> keyData = raw.keySet();
		
		keyData.forEach(item -> {
			Asset iToken = nMarket.getOrLoadToken(item);
			JSONObject JSONprices = (JSONObject)raw.get(item);
			Set<String> keyPriceData = JSONprices.keySet();
			keyPriceData.forEach(itemPrice -> {
				Double price = Double.valueOf(((JSONObject)JSONprices.get(itemPrice)).get("PRICE").toString());
				iToken.setPrice(itemPrice, price);
				
				Double volume = Double.valueOf(((JSONObject)JSONprices.get(itemPrice)).get("VOLUME24HOURTO").toString());
				iToken.setVolume(itemPrice, volume);
			});
			
		});
	}
	
	   /**
	   * Actualiza los precios de las tokens en el market.
	   * @param ApiKeys : Clave privada.
	   * @param symbolFrom : Listado de símbolos base.
	   * @param symbolTo : Listado de símbolos objetivo.
	   * @param market : Objeto mercado.
	   */
	public void updatePairPrice(String ApiKeys, List<String> symbolFrom, List<String> symbolTo, Chart nMarket) {
		List<String> JSONStrings = getMarketInfo("precio", symbolFrom, symbolTo, ApiKeys);
		JSONStrings.forEach(item -> {
			JSONObject response = tools.StringToJSON(item);
			updatePairPrice((JSONObject)response.get("RAW"), nMarket);
		});
	}
	
	   /**
	   * Actualiza los precios de las tokens en el market.
	   * @param data : JSONObject con los datos históricos.
	   * @param symbolFrom : Símbolo base.
	   * @param symbolTo : Símbolo objetivo.
	   * @param nMarket : Objeto mercado.
	   */	
	public void updatePairHistcal(JSONObject data, String symbolFrom, String symbolTo, Chart nMarket) {
		if (!data.get("Response").toString().toLowerCase().contentEquals("error")) {
			Asset nToken = nMarket.getOrLoadToken(symbolTo);
			
			JSONArray ArrayCandles = (JSONArray)((JSONObject)data.get("Data")).get("Data");
			List<Candlestick> lCandles = new ArrayList<Candlestick>();
			///////////////////////////////////////////////////////////
			// Nota: La iteración empezará desde 1 dado que Cryptocompare
			// devuelve siempre un periodo más de inicio del que se le
			// solicita por alguna razón...
			///////////////////////////////////////////////////////////
			for (int i=1; i<ArrayCandles.size(); i++) {
				JSONObject candle = (JSONObject)ArrayCandles.get(i);
				lCandles.add(new Candlestick(
						Double.valueOf(candle.get("open").toString()),
						Double.valueOf(candle.get("high").toString()),
						Double.valueOf(candle.get("low").toString()),
						Double.valueOf(candle.get("close").toString()),
						Long.valueOf(candle.get("time").toString())
						));
			}
			
			lCandles = tools.fixCandlesToPeriod(lCandles, null);			
			nToken.setCandles(symbolFrom, lCandles);		
		}
	}
	
	   /**
	   * Actualiza los precios de las tokens en el market.
	   * @param ApiKeys : Clave privada.
	   * @param symbolFrom : Listado de símbolos base.
	   * @param symbolTo : Listado de símbolos objetivo.
	   * @param market : Objeto mercado.
	   */	
	public void updatePairHistcal(String ApiKeys, List<String> symbolFrom, List<String> symbolTo, Chart nMarket) {
		symbolFrom.forEach(itemFrom -> {
			symbolTo.forEach(itemTo -> {
				List<String> bases = new ArrayList<String>();
				List<String> markets = new ArrayList<String>();
				bases.add(itemFrom);
				markets.add(itemTo);
				List<String> htcoData = getMarketInfo("historico", bases, markets, ApiKeys);
				if (!htcoData.isEmpty()) {
					JSONObject data = tools.StringToJSON(htcoData.get(0));
					updatePairHistcal(data, itemFrom, itemTo, nMarket);
				}
			});
		});
	}
}
