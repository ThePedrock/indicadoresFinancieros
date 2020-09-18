package org.stoneCoding.indicadoresFinancieros.ui.commandLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;

import org.stoneCoding.indicadoresFinancieros.internet.APIBuilder;
import org.stoneCoding.indicadoresFinancieros.internet.APILiveData;
import org.stoneCoding.indicadoresFinancieros.main.tools;
import org.stoneCoding.indicadoresFinancieros.market.Candlestick;
import org.stoneCoding.indicadoresFinancieros.market.Chart;
import org.stoneCoding.indicadoresFinancieros.ui.CommandLine.Options;

public class FibonacciRetracementCommand extends Command {
	public static String name = "FibonacciRetracement";
	
	public static List<Options> requiredOptions = new ArrayList<Options>() {{
		add(Options.PAIR);
		add(Options.SPERIOD);
		add(Options.TIMESTAMPRANGE);
	}};
	
	public static String helpMessage = defaultHelpMessage(name, requiredOptions);
	
	public static String errorMessage = "Error en las opciones de comando.";
	
	public static String run(Map<Options, Object> arguments) {
		Set<Options> received = arguments.keySet();
		if (IsAskingHelp(received)) {
			return runHelp();
		}		
		
		Map<Options, Object> argumentsMap = addDefaults(name, requiredOptions, arguments);
		received = argumentsMap.keySet();
		
		if (!checkIfValid(requiredOptions, received)) {
			return errorMessage;
		}
		
		////////////////////////////////////
		// Argumentos requeridos para SMA
		// PAIR
		// MSPERIOD
		// TIMESTAMPRANGE
		////////////////////////////////////
		
		String[] argPair = pairToArray(argumentsMap.get(Options.PAIR).toString());
		Long argMsPeriod = Long.valueOf(argumentsMap.get(Options.SPERIOD).toString());
		Short argNumPeriods;
		Long[] argTmstmpRange;
		
		try {
			argTmstmpRange = (Long[])argumentsMap.get(Options.TIMESTAMPRANGE);
			
			if (argMsPeriod!=null) {
				argNumPeriods = Double.valueOf((tools.getDate()-argTmstmpRange[0])/argMsPeriod).shortValue();
			} else {
				argNumPeriods = null;
			}
		//} catch (ClassCastException e) {
		} catch (Exception e) {
			argTmstmpRange = null;
			argNumPeriods = null;
			argMsPeriod = null;
		}
		
		
		String ApiKey = ((JSONObject)tools.getConfig().get("APIKeys")).get(tools.API).toString();
		
		if (argPair!=null && argMsPeriod!=null && argNumPeriods!=null) {
			APILiveData API = APIBuilder.buildAPILiveData(tools.API);
			Chart Mercado = new Chart();
			API.updatePairHistcal(tools.StringToJSON(API.getMarketHistory(argPair[0], argPair[1], 
					argMsPeriod, argNumPeriods, ApiKey)), argPair[1], argPair[0], Mercado);
			
			List<Candlestick> Velas = Mercado.getToken(argPair[0]).getCandles(argPair[1]);
			double[] resultado = Mercado.getFibonacciRetracementLevels(Velas, argTmstmpRange);
			
			String Message = "";
			Message += "Resistance Lvl.3: " + Mercado.getToken(argPair[0]).getFormat(argPair[1]).format(resultado[5]) + "\n";
			Message += "Resistance Lvl.2: " + Mercado.getToken(argPair[0]).getFormat(argPair[1]).format(resultado[4]) + "\n";
			Message += "Resistance Lvl.1: " + Mercado.getToken(argPair[0]).getFormat(argPair[1]).format(resultado[3]) + "\n";
			Message += "Support Lvl.1: " + Mercado.getToken(argPair[0]).getFormat(argPair[1]).format(resultado[2]) + "\n";
			Message += "Support Lvl.2: " + Mercado.getToken(argPair[0]).getFormat(argPair[1]).format(resultado[1]) + "\n";
			Message += "Support Lvl.3: " + Mercado.getToken(argPair[0]).getFormat(argPair[1]).format(resultado[0]) + "\n";
			return Message;
		} else {
			return errorMessage;
		}
	}
	
	/**
	 * Solicita el mensaje de ayuda.
	 * @return Mensaje de ayuda en formato String.
	 */
	public static String runHelp() {
		return helpMessage;
	}
}
