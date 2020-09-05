package org.stoneCoding.indicadoresFinancieros.ui.commandLine;

import java.util.HashMap;
import java.util.Map;

import org.stoneCoding.indicadoresFinancieros.ui.CommandLine;
import org.stoneCoding.indicadoresFinancieros.ui.CommandLine.DataType;
import org.stoneCoding.indicadoresFinancieros.ui.CommandLine.Options;

public class OptionHandler {

	/**
	 * Parsea los argumentos en un mapa de opciones.
	 * @param data String de argumentos
	 * @return Map<String, Object> con las opciones parseadas
	 * 		o vacío si no se hubiesen encontrado opciones
	 * 		válidas.
	 */
	public static Map<Options, Object> parseOptions(String data) {
		Map<Options, Object> optionsMap = new HashMap<>();
		String[] args = data.split(String.valueOf(CommandLine.optionChar));
		
		for (int i=0; i<args.length; i++) {
			Object[] aOption = parseOption(args[i]);
			if (aOption!=null) {
				Options Option = (Options)aOption[0];
				DataType format = CommandLine.globalOptionMap.get(Option);
				Object value = formatArgument(aOption[1].toString(), format);
				
				if (value!=null) {
					optionsMap.put(Option, value);
				}
			}
		}
		
		return optionsMap;
	}
		
	/**
	 * Parseador de opciones
	 * @param data String con opción y argumento
	 * @return Arreglo de tamaño 2 con opción y argumento en formato String 
	 * o nulo si la opción es inválida.
	 */
	public static Object[] parseOption(String data) {
		String key = findOption(data);
		Options Option;
		
		try {
			Option = CommandLine.Options.valueOf(key);			
		} catch (IllegalArgumentException e) {
			Option = null;
		}
		
		if (Option!=null) {
			String value;
			value = data.replace(key, "");
			value = value.replace(String.valueOf(CommandLine.space), "");
			return new Object[] {Option, value};
		} else {
			return null;
		}
	}
	
	/**
	 * Encuentra opción en un segmento de String
	 * @param data
	 * @return
	 */
	private static String findOption(String data) {
		String result = "";
		
		if (data.length()>0) {
			int i=1;
			while(i<=data.length() && !data.substring(i-1, i).contentEquals(String.valueOf(CommandLine.space))) {
				result+=data.substring(i-1, i);
				i++;
			}
		}
		
		return result;
	}
	
	/**
	 * Formatea un String en un array de string según el separador definido.
	 * @param data String a formatear.
	 * @return String[]
	 */
	public static String[] formatStringArray(String data) {
		String arrayData = "";
		
		boolean read = false;
		for (int i=0; i<data.length()-1; i++) {
			if (!read) {
				read = data.substring(i, i+1).contentEquals(String.valueOf(CommandLine.openArray))?true:read;
			} else {
				read = data.substring(i, i+1).contentEquals(String.valueOf(CommandLine.closeArray))?false:read;
			}
			
			if (read) {
				arrayData+=data.substring(i, i+1);
			}
		}
		
		return arrayData.split(String.valueOf(CommandLine.separator));
	}
	
	/**
	 * Parsea los argumentos recibidos en un Object.
	 * @param data Datos a formatear.
	 * @param format Formato en String.
	 * @return Object
	 */
	private static Object formatArgument(String data, DataType format) {
		switch(format) {
		case String:
			return data.replace(String.valueOf(CommandLine.space), "");
		case Long:
			String LData = data.replace(String.valueOf(CommandLine.space), "");
			Long LValue = null;
			try {
				LValue = Long.valueOf(LData);
			} catch (NumberFormatException e) {
				return null;
			}
			return LValue;
		case TmstmpList:
			String aLData = data.replace(String.valueOf(CommandLine.space), "");
			
			String[] aData = formatStringArray(aLData);
			Long[] aLong = null;
			if (aData!=null) {
				aLong = new Long[aData.length];
				
				try {
					for(int i=0; i<aData.length; i++) {
						aLong[i] = Long.valueOf(aData[i]);
					}
				} catch (NumberFormatException e) {
					return null;					
				}

			}
			return aLong;
		case Short:
			String SData = data.replace(String.valueOf(CommandLine.space), "");
			Short SValue = null;
			try {
				SValue = Short.valueOf(SData);
			} catch (NumberFormatException e) {
				return null;
			}
			return SValue;
		default:
			return null;
		}
	}
}
