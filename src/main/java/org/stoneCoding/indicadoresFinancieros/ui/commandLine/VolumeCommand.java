package org.stoneCoding.indicadoresFinancieros.ui.commandLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;

import org.stoneCoding.indicadoresFinancieros.internet.APIBuilder;
import org.stoneCoding.indicadoresFinancieros.internet.APILiveData;
import org.stoneCoding.indicadoresFinancieros.main.tools;
import org.stoneCoding.indicadoresFinancieros.market.Chart;
import org.stoneCoding.indicadoresFinancieros.ui.CommandLine.Options;

public class VolumeCommand extends Command {
	public static String name = "Volume";
	
	public static List<Options> requiredOptions = new ArrayList<Options>() {{
		add(Options.PAIR);
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
		// Argumentos requeridos para Volume
		// PAIR
		////////////////////////////////////
		
		String[] argPair = pairToArray(argumentsMap.get(Options.PAIR).toString());
		String ApiKey = ((JSONObject)tools.getConfig().get("APIKeys")).get(tools.API).toString();
		
		if (argPair!=null) {
			APILiveData API = APIBuilder.buildAPILiveData(tools.API);
			Chart Mercado = new Chart();
				
			API.updatePairPrice(tools.StringToJSON(API.getMarketPrice(argPair[0], argPair[1], ApiKey)), Mercado);
			
			Double resultado = Mercado.getToken(argPair[0]).getVolume(argPair[1]);
			
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
