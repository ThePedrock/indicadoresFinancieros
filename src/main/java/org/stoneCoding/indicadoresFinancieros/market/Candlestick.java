package org.stoneCoding.indicadoresFinancieros.market;

public class Candlestick extends OHLC {
	final long time;
	
	/**
	 * Objeto vela de tipo OHLC
	 */
	public Candlestick() {
		super();
		time = 0;
	}
	
	/**
	 * Constructor.
	 * @param Open Precio de apertura del periodo.
	 * @param High Precio máximo del periodo.
	 * @param Low Precio mínimo del periodo.
	 * @param Close Precio de cierre del periodo.
	 * @param Time Timestamp (del momento final del periodo).
	 */
	public Candlestick(double Open, double High, double Low, double Close, long Time) {
		super(Open, High, Low, Close);
		time = Time;
	}
	
	/**
	 * Devuelve el timestamp.
	 * @return
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Devuelve la propagación del activo en el periodo.
	 * @return double entre 1 y -1.
	 */
	public double getSpread() {
		return (high/low)-1;
	}
	
	/**
	 * Devuelve si se detecta Pump o FatFinger (ambos o ninguno) en el Candlestick.
	 * @param minNearLow Porcentaje mínimo aceptado de posición del cierre con respecto a su mínimo.
	 * @param minOscillation Porcentaje mínimo aceptado de oscilación del cierre con respecto a sus extremos.
	 * @return true o false
	 */
	public boolean getPumpFatFiger(double minNearLow, double minOscillation) {
		double candleNearLow = getNearLow();
		double candlePriceOscillation = getPriceOscillation();
		
		if (candleNearLow>minNearLow && candlePriceOscillation>minOscillation) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Devuelve si se detecta grindeo en el Candlestick.
	 * @param maxNearLow Porcentaje máximo aceptado de posición del cierre con respecto a su mínimo.
	 * @param minOscillation Porcentaje mínimo aceptado de oscilación del cierre con respecto a sus extremos.
	 * @return true o false
	 */
	public boolean getGrinding(double maxNearLow, double minOscillation) {
		double candleNearLow = getNearLow();
		double candlePriceOscillation = getPriceOscillation();
		
		if (candleNearLow<maxNearLow && candlePriceOscillation>minOscillation) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Devuelve la oscilación del precio con respecto a sus extremos del periodo.
	 * @return double
	 */
	public double getPriceOscillation() {
		return (high-low)/close;
	}
	
	/**
	 * Devuelve la cercanía al valor más bajo.
	 * @return
	 */
	public double getNearLow() {
		return (close-low)/close;
	}
	
	/**
	 * Devuelve los niveles de resistencia.
	 * @return Arreglo de doubles de longitud 3. 
	 */
	public double[] getResistanceLevels() {
		double[] resultado = {0,0,0};
		double pivot = (high+low+close)/3;
		resultado[0]=(pivot*2)-low;
		resultado[1]=pivot+(high-low);
		resultado[2]=pivot+(2*(high-low));
		return resultado;
	}
	
	/**
	 * Devuelve los niveles de soporte.
	 * @return Arreglo de doubles de longitud 3. 
	 */
	public double[] getSupportLevels() {
		double[] resultado = {0,0,0};
		double pivot = (high+low+close)/3;
		resultado[0]=(pivot*2)-high;
		resultado[1]=pivot-(high-low);
		resultado[2]=pivot-(2*(high-low));
		return resultado;		
	}
	
	/**
	 * Devuelve los niveles de resistencia.
	 * @return Arreglo de doubles de longitud 3. 
	 */
	public double[] getResistanceSupportLevels() {
		double[] resultado = {0,0,0,0,0,0};
		double[] Soporte = getSupportLevels();
		double[] Resistencia = getResistanceLevels();
		resultado[0] = Soporte[2]; resultado[1] = Soporte[1];
		resultado[2] = Soporte[0]; resultado[3] = Resistencia[0];
		resultado[4] = Resistencia[1];	resultado[5] = Resistencia[2];
		return resultado;
	}
	
	/**
	 * Devuelve los niveles de resistencia fibonacci.
	 * @return Arreglo de doubles de longitud 3. 
	 */
	public double[] getFibonacciResistanceLevels() {
		double[] resultado = {0,0,0};
		double[] FiboLevels = getFibonacciResistanceSupportLevels();
		resultado[0] = FiboLevels[3]; resultado[1] = FiboLevels[4];
		resultado[2] = FiboLevels[5];
		return resultado;
	}
	
	/**
	 * Devuelve los niveles de resistencia fibonacci.
	 * @return Arreglo de doubles de longitud 3. 
	 */
	public double[] getFibonacciSupportLevels() {
		double[] resultado = {0,0,0};
		double[] FiboLevels = getFibonacciResistanceSupportLevels();
		resultado[0] = FiboLevels[2]; resultado[1] = FiboLevels[1];
		resultado[2] = FiboLevels[0];
		return resultado;
	}
	
	/**
	 * Devuelve los niveles de fibonacci.
	 * @return Arreglo de doubles de longitud 6. 
	 */
	public double[] getFibonacciResistanceSupportLevels() {
		double[] ratios = {0.236, 0.382, 0.5, 0.618, 0.786, 1};
		double[] resultado = {0,0,0,0,0,0};
		
		for(int i=0; i<ratios.length; i++) {
			resultado[i]=((high-low)*ratios[i])+low;
		}
		return resultado;
	}
	
	/**
	 * Devuelve Candlestick vacía.
	 * @return Candlestick
	 */
	public static Candlestick getDefault() {
		return new Candlestick();
	}
}
