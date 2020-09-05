package org.stoneCoding.indicadoresFinancieros.market;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.stoneCoding.indicadoresFinancieros.main.tools;

public class Asset {
	final protected String baseSymbol;
	
	protected HashMap<String, Double> priceHash = new HashMap<String, Double>();
	protected HashMap<String, List<Candlestick>> candleHash = new HashMap<String, List<Candlestick>>();
	protected HashMap<String, Double> volumeHash = new HashMap<String, Double>();
	protected HashMap<String, DecimalFormat> formatHash = new HashMap<String, DecimalFormat>();
	
	protected DecimalFormat defaultFormat = tools.FIATFormat;
	
	protected Long timeFrom = Long.MAX_VALUE;
	protected Long timeTo = Long.MIN_VALUE;
	
	/**
	 * Activo que almacena su valor en el tiempo en relación a otro.
	 */
	Asset() {
		baseSymbol = "";
	}
	
	/**
	 * Constructor.
	 * @param BaseSymbol Símbolo base del activo.
	 */
	public Asset(String BaseSymbol) {
		baseSymbol = BaseSymbol;
	}
	
	/**
	 * Devuelve el precio contra un activo.
	 * @param quoteSymbol Símbolo del activo contra el que se coteja.
	 * @return Double
	 */
	public Double getPrice(String quoteSymbol) {
		return priceHash.get(quoteSymbol);
	}
	
	/**
	 * Devuelve histórico de precios contra un activo.
	 * @param quoteSymbol Símbolo del activo contra el que se coteja.
	 * @return Listado de Candlestick
	 */
	public List<Candlestick> getCandles(String quoteSymbol) {
		return candleHash.get(quoteSymbol);
	}

	/**
	 * Devuelve el volumen contra un activo.
	 * @param quoteSymbol Símbolo del activo contra el que se coteja.
	 * @return Double
	 */
	public Double getVolume(String quoteSymbol) {
		return volumeHash.get(quoteSymbol);
	}
	
	/**
	 * Devuelve el volumen contra un activo.
	 * @param quoteSymbol Símbolo del activo contra el que se coteja.
	 * @return Double
	 */
	public DecimalFormat getFormat(String quoteSymbol) {
		return formatHash.get(quoteSymbol)!=null?formatHash.get(quoteSymbol):defaultFormat;	
	}
	
	/**
	 * Devuelve el tiempo inicial de valoración del activo.
	 * @return Timestamp.
	 */
	public Long getTimeFrom() {
		return timeFrom;
	}

	/**
	 * Devuelve el tiempo final de valoración del activo.
	 * @return Timestamp.
	 */
	public Long getTimeTo() {
		return timeTo;
	}
	
	/**
	 * Establece el precio del activo contra otro.
	 * @param quoteSymbol Símbolo del activo contra el que se coteja.
	 * @param price Precio del activo.
	 * @return Precio anterior del activo o null.
	 */
	public Double setPrice(String quoteSymbol, Double price) {
		updateFormat(quoteSymbol);
		return priceHash.put(quoteSymbol, price);
	}
	
	/**
	 * Establece el histórico del activo contra otro.
	 * @param quoteSymbol Símbolo del activo contra el que se coteja.
	 * @param candles Listado de Candlesticks
	 * @return Listado anterior de Candlesticks o null.
	 */
	public List<Candlestick> setCandles(String quoteSymbol, List<Candlestick> candles) {
		List<Candlestick> last = candleHash.put(quoteSymbol, candles);
		updateFormat(quoteSymbol);
		updateTimeFromTo();
		return last;
	}

	/**
	 * Establece el volumen del activo contra otro.
	 * @param quoteSymbol Símbolo del activo contra el que se coteja.
	 * @param price Volumen del activo.
	 * @return Volumen anterior del activo o null.
	 */
	public Double setVolume(String quoteSymbol, Double Volume) {
		updateFormat(quoteSymbol);
		return volumeHash.put(quoteSymbol, Volume);
	}

	/**
	 * Actualiza el formato a mostrar en función de si el activo contra el
	 * que se conteja se considera FIAT o no.
	 * @param quoteSymbol Símbolo del activo contra el que se coteja.
	 */
	private void updateFormat(String quoteSymbol) {
		if (tools.FIATList.contains(quoteSymbol)) {
			formatHash.put(quoteSymbol, tools.FIATFormat);	
		} else {
			formatHash.put(quoteSymbol, tools.TokenFormat);
		}
	}
	
	/**
	 * Actualiza los timestamps del activo.
	 */
	private void updateTimeFromTo() {
		Long earliestTimestamp = null;
		Long latestTimestamp = null;
		
		Iterator<String> itr = candleHash.keySet().iterator();
		while(itr.hasNext()) {
			String Key = itr.next();
			List<Candlestick> Value = candleHash.get(Key);
			
			if (!Value.isEmpty()) {
				Integer firstIndex = 0;
				Integer lastIndex = Value.size()-1;
				lastIndex = lastIndex>=0?lastIndex:0;

				if (earliestTimestamp!=null) {
					earliestTimestamp = earliestTimestamp<Value.get(firstIndex).getTime()?earliestTimestamp:Value.get(firstIndex).getTime();
				} else {
					earliestTimestamp = Value.get(firstIndex).getTime();
				}
				
				if (latestTimestamp!=null) {
					latestTimestamp = latestTimestamp>Value.get(lastIndex).getTime()?latestTimestamp:Value.get(lastIndex).getTime();
				} else {
					latestTimestamp = Value.get(lastIndex).getTime();
				}			
				
				timeFrom = earliestTimestamp<timeFrom?earliestTimestamp:timeFrom;
				timeTo = latestTimestamp>timeTo?latestTimestamp:timeTo;				
			}
		}
	}
}
