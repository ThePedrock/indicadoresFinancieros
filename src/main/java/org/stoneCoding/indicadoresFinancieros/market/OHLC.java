package org.stoneCoding.indicadoresFinancieros.market;

public class OHLC {

	protected final double high;
	protected final double open;
	protected final double close;
	protected final double low;
	
	/**
	 * Segmento que contempla valor de apertura, mínimo, máximo y cierre.
	 */
	OHLC() {
		high = 0;
		open = 0;
		close = 0;
		low = 0;
	}
	
	/**
	 * Constructor.
	 * @param _open Valor de apertura.
	 * @param _high Valor máximo.
	 * @param _low Valor mínimo.
	 * @param _close Valor de cierre.
	 */
	public OHLC(double _open, double _high, double _low, double _close) {
		high = _high;
		open = _open;
		close = _close;
		low = _low;
	}
	
	/**
	 * Devuelve el máximo.
	 * @return double
	 */
	public double getHigh() {
		return high;
	}
	
	/**
	 * Devuelve la apertura.
	 * @return double
	 */
	public double getOpen() {
		return open;
	}
	
	/**
	 * Devuelve el cierre.
	 * @return double
	 */
	public double getClose() {
		return close;
	}
	
	/**
	 * Devuelve el mínimo.
	 * @return double
	 */
	public double getLow() {
		return low;
	}	
}
