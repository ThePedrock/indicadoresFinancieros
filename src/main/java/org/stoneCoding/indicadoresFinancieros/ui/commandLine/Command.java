package org.stoneCoding.indicadoresFinancieros.ui.commandLine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;

import org.stoneCoding.indicadoresFinancieros.main.tools;
import org.stoneCoding.indicadoresFinancieros.ui.CommandLine;
import org.stoneCoding.indicadoresFinancieros.ui.CommandLine.Options;

public abstract class Command {
	
	/**
	 * Ejecuta el comando.
	 * @return
	 */
	public static String run() {
		return runHelp();
	}
	
	/**
	 * Ejecuta la ayuda.
	 * @return
	 */
	public static String runHelp() {
		return new String();
	}
	
	/**
	 * Comprueba si las opciones recibidas son válidas.
	 * @param required Listado de opciones requeridas.
	 * @param received Set de opciones recibidas.
	 * @return true si es válido. false si es inválido.
	 */
	protected static boolean checkIfValid(List<Options> required, Set<Options> received) {
		Iterator<Options> it = received.iterator();
		while(it.hasNext()) {
			if (!required.contains(it.next())) {
				return false;
			}
		}
		
		it = required.iterator();
		while(it.hasNext()) {
			if (!received.contains(it.next())) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Considera si el usuario está solicitando ayuda ya sea
	 * porque no se han recibido argumentos con el comando o
	 * se ha añadido el parámetro "-HELP"
	 * @param received Set de opciones recibidas.
	 * @return true o false.
	 */
	protected static boolean IsAskingHelp(Set<Options> received) {
		if (received.isEmpty()) {
			return true;
		}
		
		if (received.size()==1 && received.contains(Options.HELP)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Obtiene los parámetros por defecto de las opciones según el comando.
	 * @param Command Nombre del comando en formato String.
	 * @return Recibe mapa de opciones y valores.
	 */
	protected static Map<Options, Object> getDefaults(String Command) {
		JSONObject appconfig = tools.getConfig();
		Map<Options, Object> defaultsMap = new HashMap<Options, Object>();
		
		JSONObject defaults = new JSONObject();
		JSONObject cmdDefaults = new JSONObject();
		JSONObject sumDefaults = new JSONObject();
		
		if (appconfig.containsKey("indicators")) {
			appconfig = (JSONObject)appconfig.get("indicators");
			
			defaults = appconfig.containsKey("default")?(JSONObject)appconfig.get("default"):defaults;
			cmdDefaults = appconfig.containsKey(Command.toUpperCase())?
					(JSONObject)appconfig.get(Command.toUpperCase()):cmdDefaults;
					
			Set<String> defaultsKeySet = defaults.keySet();
			Set<String> cmdDefaultsKeySet = cmdDefaults.keySet();
			
			Iterator<String> keys = defaultsKeySet.iterator();
			while(keys.hasNext()) {
				String Key = keys.next();
				sumDefaults.put(Key, defaults.get(Key));
			}
			
			keys = cmdDefaultsKeySet.iterator();
			while(keys.hasNext()) {
				String Key = keys.next();
				sumDefaults.put(Key, cmdDefaults.get(Key));
			}			
			
			keys = sumDefaults.keySet().iterator();
			while(keys.hasNext()) {
				String Key = keys.next();
				defaultsMap.put(Options.valueOf(Key), sumDefaults.get(Key));
			}
			
			return defaultsMap;
		} else {
			return defaultsMap;
		}
	}
	
	/**
	 * Añade elementos por defecto al mapa de argumentos.
	 * @param arguments Mapa de argumentos.
	 * @return Mapa de argumentos revisado.
	 */
	protected static Map<Options, Object> addDefaults(String commandName, 
			List<Options> requiredOptions, Map<Options, Object> arguments) {
		Map<Options, Object> defaults = getDefaults(commandName.toUpperCase());
		Map<Options, Object> argumentsMap = arguments;
		
		Iterator<Options> iRequired = requiredOptions.iterator();
		while(iRequired.hasNext()) {
			Options Option = iRequired.next();
			if (!argumentsMap.containsKey(Option)) {
				argumentsMap.put(Option, defaults.get(Option));				
			}
		}
		
		return argumentsMap;
	}
	
	/**
	 * Convierte el String del par en un array de dos Strings.
	 * @param pair String de ambos tokens separados por el char slash
	 * @return Array de 2 String o nulo si no fuese válido.
	 */
	protected static String[] pairToArray(String pair) {
		String[] symbols = pair.split(String.valueOf(CommandLine.slash));
		if (symbols.length!=2) {
			return null;
		} else {
			return symbols;
		}
	}
	
	/**
	 * Construye mensaje de ayuda por defecto.
	 * @param commandName Nombre del comando.
	 * @param requiredOptions Listado de opciones requeridas por el comando.
	 * @return Texto de ayuda en formato String.
	 */
	protected static String defaultHelpMessage(String commandName, List<Options> requiredOptions) {
		String cmdFormat = commandName + " ";
		String cmdHelp = "";
		
		Iterator<Options> iOptions = requiredOptions.iterator();
		while(iOptions.hasNext()) {
			Options Option = iOptions.next();
			String strOption = "-" + Option.name() + " " + 
			CommandLine.globalDataTypeHelpMap.get(CommandLine.globalOptionMap.get(Option)) + " ";
			
			cmdFormat += strOption;
			
			cmdHelp += Option.name() + ": " + CommandLine.globalOptionHelpMap.get(Option) + "\n";
		}
		return (cmdFormat + "\n" + cmdHelp);
	}
}
