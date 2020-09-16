package org.stoneCoding.indicadoresFinancieros.main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.stoneCoding.indicadoresFinancieros.market.Candlestick;

public class tools {
	
	public static final long ms_1hr = 3600;
	public static final long ms_4hrs = 14400;
	public static final long ms_1day = 86400;
	public static final long ms_1wek = 604800;
	
	public static final List<String> FIATList = Arrays.asList("USD", "EUR");
	
	public static String encode = "UTF-8";
	
	public static final DecimalFormat FIATFormat = new DecimalFormat( "#,###,###,##0.00" );
	public static final DecimalFormat TokenFormat = new DecimalFormat ( "#,###,###,##0.00000000" );
	public static final DecimalFormat PercentageFormat = new DecimalFormat ( "#,##0.00 '%'" );
	
	public static String API = "cryptocompare";
	
	public static Long ms_period = (long)14400;
	public static Integer periods = 90;
	
	public static Short minEMAPeriods = 15;
	
	   /**
	   * Fusiona todas las velas indicadas en el rango.
	   * @param Estudio : Listado de velas.
	   * @param TimeRange : Array de Timestamps.
	   * @return Candlestick : Vela.
	   */		
	public static Candlestick fuseCandles(List<Candlestick> Estudio, Long[] TimeRange) {
		List<Candlestick> lCandlesticks = new ArrayList<Candlestick>();
		if (!(TimeRange.length>1) || Estudio.isEmpty()) {
			return new Candlestick();
		}
		if (!(Estudio.size()>1)) {
			return Estudio.get(0);
		}
		
		Long timeFrom = TimeRange[0]<TimeRange[TimeRange.length-1]?TimeRange[0]:TimeRange[TimeRange.length-1];
		Long timeTo = TimeRange[TimeRange.length-1]>TimeRange[0]?TimeRange[TimeRange.length-1]:TimeRange[0];
		
		for (int i=0; i<Estudio.size(); i++) {
			if (timeFrom<=Estudio.get(i).getTime() && timeTo>=Estudio.get(i).getTime()) {
				lCandlesticks.add(Estudio.get(i));
			}
		}
		lCandlesticks = sortCandlesticksByTimestamp(lCandlesticks);
		
		Long nTime;
		Double nHigh;
		Double nOpen;
		Double nClose;
		Double nLow;
		
		nTime = lCandlesticks.get(lCandlesticks.size()-1).getTime();
		nHigh = lCandlesticks.get(0).getHigh();
		nOpen = lCandlesticks.get(0).getOpen();
		nClose = lCandlesticks.get(lCandlesticks.size()-1).getClose();
		nLow = lCandlesticks.get(0).getLow();
		
		for (int i=0; i<lCandlesticks.size(); i++) {
			nHigh = nHigh<lCandlesticks.get(i).getHigh()?lCandlesticks.get(i).getHigh():nHigh;
			nLow = nLow<lCandlesticks.get(i).getLow()?nLow:lCandlesticks.get(i).getLow();
		}
		
		return new Candlestick(nOpen, nHigh, nLow, nClose, nTime);
		
	}
	   /**
	   * Fusiona todas las velas indicadas en el rango.
	   * @param Estudio : Listado de velas.
	   * @param IndexRange : Array de Índices.
	   * @return Candlestick : Vela.
	   */		
	public static Candlestick fuseCandles(List<Candlestick> Estudio, Integer[] IndexRange) {
		List<Candlestick> lCandlesticks = new ArrayList<Candlestick>();
		if (!(IndexRange.length>1)) {
			return new Candlestick();
		}
		
		for (int i=IndexRange[0]; i<=IndexRange[1]; i++) {
			lCandlesticks.add(Estudio.get(i));
		}
		lCandlesticks = sortCandlesticksByTimestamp(lCandlesticks);
		
		Long nTime;
		Double nHigh;
		Double nOpen;
		Double nClose;
		Double nLow;
		
		nTime = lCandlesticks.get(lCandlesticks.size()-1).getTime();
		nHigh = lCandlesticks.get(0).getHigh();
		nOpen = lCandlesticks.get(lCandlesticks.size()-1).getOpen();
		nClose = lCandlesticks.get(lCandlesticks.size()-1).getClose();
		nLow = lCandlesticks.get(0).getLow();
		
		for (int i=0; i<lCandlesticks.size(); i++) {
			nHigh = nHigh<lCandlesticks.get(i).getHigh()?lCandlesticks.get(i).getHigh():nHigh;
			nLow = nLow<lCandlesticks.get(i).getLow()?nLow:lCandlesticks.get(i).getLow();
		}
		
		return new Candlestick(nOpen, nHigh, nLow, nClose, nTime);
		
	}	

	   /**
	   * Ajusta el listado de velas según los milisegundos configurados como periodo en el botconfig.json
	   * @param lCandle Listado de velas.
	   * @param ms_period milisegundos del nuevo periodo. Puede ser nulo.
	   * @return List(candle) Listado de velas resultante.
	   */	
	public static List<Candlestick> fixCandlesToPeriod(List<Candlestick> lCandle, Long ms_period) {
		Long period = ms_period!=null?ms_period:tools.ms_period;
		List<Candlestick> _lCandle = new ArrayList<Candlestick>();
		List<Candlestick> resultReverso = new ArrayList<Candlestick>();
		List<Candlestick> resultado = new ArrayList<Candlestick>();
		
		if (!(lCandle.size()>1)) {
			return lCandle;
		}
		
		if (lCandle.get(0).getTime()>lCandle.get(1).getTime()) {
			for (int i=lCandle.size()-1; i>=0; i--) {
				_lCandle.add(lCandle.get(i));
			}
		} else {
			_lCandle = lCandle;
		}
		
		if (period<=_lCandle.get(1).getTime()-_lCandle.get(0).getTime()) {
			return lCandle;
		}
		
		Integer index01 = 0;
		Integer index02 = null;
		Long accumulatedPeriod = Long.valueOf(_lCandle.get(1).getTime()-_lCandle.get(0).getTime());
		for (int i=1; i<_lCandle.size(); i++) {
			if (index01==null) {
				index01 = i;
			}
			
			accumulatedPeriod+=(_lCandle.get(i).getTime()-_lCandle.get(i-1).getTime());
			
			if (!(accumulatedPeriod<period) && (i<_lCandle.size())) {
				index02 = i;
			}
			
			if (index01!=null && index02!=null) {
				Candlestick nCandle = fuseCandles(_lCandle, new Integer[] {index01.intValue(), index02.intValue()});
				resultado.add(nCandle);
				index01=null;
				index02=null;
				accumulatedPeriod = Long.valueOf(0);
			}
		}
		
		
		if (lCandle.get(0).getTime()>lCandle.get(1).getTime()) {
			for (int i=resultado.size()-1; i>=0; i--) {
				resultReverso.add(resultado.get(i));
			}
			return resultReverso;
		}

		return resultado;
	}		
	
	/**
	 * Ordena un listado de Candlesticks por su Timestamp.
	 * @param Candlesticks
	 * @return List<Candlestick> ordenado.
	 */
	public static List<Candlestick> sortCandlesticksByTimestamp(List<Candlestick> Candlesticks) {	// ?? .. Revisar
		List<Candlestick> nCandlesticks = Candlesticks;
		nCandlesticks.sort(Comparator.comparing(Candlestick::getTime));
		return nCandlesticks;
	}
	
	/**
	 * Parsea String en objeto JSON de la librería JSONsimple.
	 * @param JSONString JSON en formato String
	 * @return JSONObject
	 */
	public static JSONObject StringToJSON(String JSONString) {
		JSONParser parse = new JSONParser();
		try {
			return (JSONObject)parse.parse(JSONString);
		} catch (ParseException e) {
			e.printStackTrace();
			return new JSONObject();
		}
	}
	
	/**
	 * Solicita fichero config en formato JSONObject
	 * @return Config en formato JSONObject
	 */
	public static JSONObject getConfig() {
		JSONObject resultado = new JSONObject();
		String path = "./json/appconfig.json";
		
	    try (FileReader reader = new FileReader(path)) {
	       	JSONParser parser = new JSONParser();
	      	Object obj = parser.parse(reader);
	      	resultado = (JSONObject)obj;
	      	reader.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	    
	    return resultado;
	}
	
	
}
