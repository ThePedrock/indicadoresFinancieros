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
	 * Devuelve la variación de precio entre el máximo y el mínimo.
	 * @return double entre 1 y -1.
	 */
	public double getHighLow() {
		return (high/low)-1;
	}
	
	/**
	 * Devuelve si se detecta Pump o FatFinger (ambos o ninguno) en el Candlestick.
	 * @return true o false
	 */
	public boolean getPumpFatFiger() {
		double _nearLow = (close-low)/close;
		
		if (_nearLow>0.2 && getSpread()>0.3) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Devuelve si se detecta grindeo en el Candlestick.
	 * @return true o false
	 */
	public boolean getGrinding() {
		double _nearLow = (close-low)/close;
		
		if (_nearLow<0.02 && getSpread()>0.3) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Devuelve la propagación (liquidez).
	 * @return double
	 */
	public double getSpread() {
		return (high-low)/close;
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
