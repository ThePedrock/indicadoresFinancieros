package org.stoneCoding.indicadoresFinancieros.market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.stoneCoding.indicadoresFinancieros.market.Candlestick;
import org.stoneCoding.indicadoresFinancieros.main.tools;

public class Chart {

	private HashMap<String, Asset> TokenMap = new HashMap<String, Asset>();
	
	/**
	 * Carta sobre la que se apoyan activos. Realiza cálculos financieros.
	 */
	public Chart() {
		
	}
	
	/**
	 * Solicita activo.
	 * @param baseSymbol Símbolo de activo base.
	 * @return Activo
	 */
	public Asset getToken(String baseSymbol) {
		return TokenMap.get(baseSymbol);
	}
	
	/**
	 * Solicita activo o lo carga si no lo estuviese.
	 * @param baseSymbol Símbolo de activo base.
	 * @return Activo
	 */
	public Asset getOrLoadToken(String baseSymbol) {
		if (TokenMap.containsKey(baseSymbol)) {
			return TokenMap.get(baseSymbol);
		} else {
			Asset nAsset = new Asset(baseSymbol);
			TokenMap.put(baseSymbol, nAsset);
			return nAsset;
		}
	}
	
	//// FUNCIONES DE INDICADORES ////
	
	   /**
	   * Calcula el EMA
	   * @param values Listado de precios.
	   * @param totalperiods Número de periodos del EMA.
	   * @return double Valor del EMA
	   */
	public double EMA(List<Double> values, short totalperiods) {
		/*
		 * Preparado de variables
		 */
		Double preEMA;
		Double smoothing = (2/((double)totalperiods+1));
		List<Double> lValues = new ArrayList<Double>();
		lValues.addAll(values);
		
		Double lastClose;		
		try {
			lastClose = lValues.get(lValues.size()-1);
			lValues.remove(lValues.size()-1);
		} catch (IndexOutOfBoundsException e) {
			lastClose = Double.valueOf(0);
			lValues = new ArrayList<Double>();	
		}
		
		/*
		 * Cálculo de datos
		 */
		if (lValues.size()<totalperiods) {
			preEMA = SMA(lValues);
		} else {
			preEMA = EMA(lValues, totalperiods);	
		}			
		
		/*
		 * Retorno de valor de indicador
		 */
		return (lastClose*(smoothing))+(preEMA*(1-(smoothing)));
			
	}
	   /**
	   * Calcula el SMA
	   * @param _estudio Listado de valores.
	   * @return double Valor del SMA
	   */	
	public double SMA(List<Double> values) {
		/*
		 * Preparado de variables
		 */
		double resultado = 0;
		
		/*
		 * Cálculo de datos
		 */
		for (int i=0; i<values.size(); i++) {
			resultado+=values.get(i);
		}
		
		/*
		 * Retorno del valor del indicador
		 */
		return Double.valueOf(resultado/values.size()).isNaN()?
				Double.valueOf(0):resultado/values.size();
	}
	   /**
	   * Volumen de un Asset.
	   * @param tokenfrom token base
	   * @param tokento token symbol
	   * @return Double Devuelve el valor del Volumen.
	   */
	public Double Volume(String AssetFrom, String AssetTo) {
		Asset nAsset = this.TokenMap.get(AssetTo);
		return nAsset.getVolume(AssetFrom);	
	}
	
	   /**
	   * Diferencia de precio entre dos velas.
	   * @param Candlestick01 Primera vela.
	   * @param Candlestick02 Segunda vela.
	   * @return Double Devuelve la proporción de la diferencia de precio.
	   */	
	public Double PriceChange(Candlestick Candlestick01, Candlestick Candlestick02) {
		return Double.valueOf((Candlestick02.getClose()/Candlestick01.getClose())-1);
	}
	
	   /**
	   * Indica si en un periodo puede haber FatFinger o Pumps.
	   * @param Estudio Array de velas.
	   * @param Timestamp Rango de timestamps.
	   * @param minNearLow Porcentaje mínimo aceptado de posición del cierre con respecto a su mínimo.
	   * @param minOscillation Porcentaje mínimo aceptado de oscilación del cierre con respecto a sus extremos.
	   * @return Boolean True si existe. False si no.
	   */	
	public Boolean FatFinger(List<Candlestick> Estudio, Long[] Timestamp,
			Double minNearLow, Double minOscillation) {
		return Boolean.valueOf(tools.fuseCandles(Estudio, Timestamp)
				.getPumpFatFiger(minNearLow, minOscillation));
	}
	
	/**
	 * Indica si en un periodo puede haber Grinding.
	 * @param Estudio Array de velas.
	 * @param Timestamp Rango de timestamps.
	 * @param maxNearLow Porcentaje máximo aceptado de posición del cierre con respecto a su mínimo.
	 * @param minOscillation Porcentaje mínimo aceptado de oscilación del cierre con respecto a sus extremos.
	 * @return Boolean True si existe. False si no.
	 */
	public Boolean Grinding(List<Candlestick> Estudio, Long[] Timestamp,
			Double maxNearLow, Double minOscillation) {
		return Boolean.valueOf(tools.fuseCandles(Estudio, Timestamp)
				.getGrinding(maxNearLow, minOscillation));
	}
	
	   /**
	   * Recibir niveles de Resistencia y Soporte del Punto Pivote
	   * @param Estudio : Listado de simbolos.
	   * @param TimeRange : Array de rango de tiempo en timestamp
	   * @return double[] : Arreglo de 6 double indicando los niveles.
	   */
	public double[] getPivotPointRetracementLevels(List<Candlestick> Estudio, Long[] TimeRange) {
		Candlestick candle = tools.fuseCandles(Estudio, TimeRange);
		
		double[] resistance = candle.getResistanceLevels();
		double[] support = candle.getResistanceSupportLevels();
		
		double[] resultado = {support[2], support[1], support[0],
				resistance[0], resistance[1], resistance[2]};
	
		return resultado;
	}

	   /**
	   * Recibir niveles de Resistencia y Soporte de Fibonacci
	   * @param Estudio : Listado de simbolos.
	   * @param TimeRange : Array de rango de tiempo en timestamp
	   * @return double[] : Arreglo de 6 double indicando los niveles.
	   */	
	public double[] getFibonacciRetracementLevels(List<Candlestick> Estudio, Long[] TimeRange) {
		Candlestick candle = tools.fuseCandles(Estudio, TimeRange);
	
		return candle.getFibonacciResistanceSupportLevels();
	}
}
