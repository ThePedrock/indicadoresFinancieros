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

public class PriceCommand extends Command {
	public static String name = "Price";
	
	public static List<Options> requiredOptions = new ArrayList<Options>() {{
		add(Options.PAIR);
		add(Options.MSPERIOD);
		add(Options.NPERIODS);
	}};
	
	public static String helpMessage = defaultHelpMessage(name, requiredOptions);
	
	public static String errorMessage = "Error en las opciones de comando.";
	
	/**
	 * Ejecuta el comando.
	 * @param arguments Mapa de argumentos.
	 * @return Resultado en formato String.
	 */
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
		// Argumentos requeridos para Price
		// PAIR
		// MSPERIOD
		// NPERIODS
		////////////////////////////////////
		
		String[] argPair = pairToArray(argumentsMap.get(Options.PAIR).toString());
		Long argMsPeriod = Long.valueOf(argumentsMap.get(Options.MSPERIOD).toString());
		Short argNumPeriods = Short.valueOf(argumentsMap.get(Options.NPERIODS).toString());
		String ApiKey = ((JSONObject)tools.getConfig().get("APIKeys")).get(tools.API).toString();
		
		if (argPair!=null && argMsPeriod!=null && argNumPeriods!=null) {
			APILiveData API = APIBuilder.buildAPILiveData(tools.API);
			Chart Mercado = new Chart();
			Double resultado;
			if (argNumPeriods==0) {
				API.updatePairPrice(tools.StringToJSON(API.getMarketPrice(argPair[0], argPair[1], ApiKey)), Mercado);
				resultado = Mercado.getToken(argPair[0]).getPrice(argPair[1]);				
			} else {
				API.updatePairHistcal(tools.StringToJSON(API.getMarketHistory(argPair[0], argPair[1], 
						argMsPeriod, argNumPeriods, ApiKey)), argPair[1], argPair[0], Mercado);
				
				List<Candlestick> Velas = Mercado.getToken(argPair[0]).getCandles(argPair[1]);
				if (!Velas.isEmpty()) {
					resultado = Velas.get(0).getClose();
				} else {
					resultado = 0.0;
				}
			}
			
			return Mercado.getToken(argPair[0]).getFormat(argPair[1]).format(resultado);
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
