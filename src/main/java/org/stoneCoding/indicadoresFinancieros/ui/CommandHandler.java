package org.stoneCoding.indicadoresFinancieros.ui;

import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.stoneCoding.indicadoresFinancieros.ui.CommandLine.Commands;
import org.stoneCoding.indicadoresFinancieros.ui.CommandLine.DataType;
import org.stoneCoding.indicadoresFinancieros.ui.CommandLine.Options;
import org.stoneCoding.indicadoresFinancieros.ui.commandLine.*;

import otg.stoneCoding.indicadoresFinancieros.json.ApiCommand;

public class CommandHandler {
	public final static char optionChar = '-';
	public final static char openArray = '[';
	public final static char closeArray = ']';
	public final static char separator = ',';
	public final static char space = ' ';
	public final static char slash = '/';
	
	public static enum Commands {
		EXIT,
		HELP,
		PRICE,
		SMA,
		EMA,
		PRICECHANGE,
		FATFINGER,
		GRINDING,
		FIBONACCIRETRACEMENT,
		PIVOTPOINTS
	}	
	public static enum Options {
		SPERIOD,
		PAIR,
		NPERIODS,
		EMAPERIODS,
		MINNEARLOW,
		MAXNEARLOW,
		PRICEOSCILLATION,
		TIMESTAMPRANGE,
		HELP
	}
	
	public final static Map<Options, String> globalOptionHelpMap = Stream.of (new Object[][] {
		{Options.SPERIOD, "Segundos del periodo."},
		{Options.PAIR, "BASE/QUOTE del par separados por '" + CommandLine.slash + "'."},
		{Options.NPERIODS, "Número de periodos"},
		{Options.EMAPERIODS, "Número de periodos para cálculo del EMA."},
		{Options.MINNEARLOW, "Diferencia mínima permitida del cierre con respecto a su mínimo en tanto por uno."},
		{Options.MAXNEARLOW, "Diferencia máxima permitida del cierre con respecto a su mínimo en tanto por uno."},
		{Options.PRICEOSCILLATION, "Oscillación mínima exigida del precio con respecto a sus extremos."},
		{Options.TIMESTAMPRANGE, "Rango de timestamps del acote."},
		{Options.HELP, "Ayuda."}
	}).collect(Collectors.toMap(data -> (Options) data[0], data -> data[1].toString()));	
	
	public static enum DataType {
		Short,
		Long,
		TmstmpList,
		Double,
		String
	}
	
	public final static Map<DataType, String> globalDataTypeHelpMap = Stream.of (new Object[][] {
		{DataType.Short, "[Short]"},
		{DataType.Long, "[Long]"},
		{DataType.TmstmpList, "[[Long, Long]]"},
		{DataType.Double, "[Double]"},
		{DataType.String, "[String]"}
	}).collect(Collectors.toMap(data -> (DataType) data[0], data -> data[1].toString()));
	
	public final static Map<Options, DataType> globalOptionMap = Stream.of (new Object[][] {
		{Options.SPERIOD, DataType.Long},
		{Options.PAIR, DataType.String},
		{Options.NPERIODS, DataType.Short},
		{Options.EMAPERIODS, DataType.Short},
		{Options.MINNEARLOW, DataType.Double},
		{Options.MAXNEARLOW, DataType.Double},
		{Options.PRICEOSCILLATION, DataType.Double},
		{Options.TIMESTAMPRANGE, DataType.TmstmpList},
		{Options.HELP, DataType.String}
	}).collect(Collectors.toMap(data -> (Options) data[0], data -> (DataType) data[1]));

	public final static String helpMessage = helpMessage();
	
	public final static String errorMessage = "No se reconoce el comando";
	

	/**
	 * Parsea el input recibido en comando y opciones.
	 * @param data input recibido.
	 * @return Arreglo de dos dimensiones con comando y opciones
	 */
	public static String[] parseCommand(String data) {
		String upperData = data.toUpperCase();
		String[] Comando = new String[] {"", ""};

		boolean cmd = true;
		for (int i=0; i<upperData.length(); i++) {
			cmd = upperData.substring(i, i+1).contentEquals(" ")?false:cmd;
			if (cmd) {
				Comando[0]+=upperData.subSequence(i, i+1);
			} else {
				Comando[1]+=upperData.subSequence(i, i+1);
			}
		}
		
		return Comando;
	}
	
	/**
	 * Ejecuta el comando.
	 * @param ApiCommand - Comando formato JSON POJO
	 */
	public static String runCommand(ApiCommand cmd) {
		
		try {
			switch(Commands.valueOf(cmd.getCommandType())) {
			case EXIT:
				return "Saliendo...";
			case HELP:
				return helpMessage;
			case PRICE:
				return PriceCommand.run(OptionHandler.parseOptions(cmd.stringifyCommand()));
			case EMA:
				return EMACommand.run(OptionHandler.parseOptions(cmd.stringifyCommand()));
			case SMA:
				return SMACommand.run(OptionHandler.parseOptions(cmd.stringifyCommand()));
			case PRICECHANGE:
				return PriceChangeCommand.run(OptionHandler.parseOptions(cmd.stringifyCommand()));
			case FATFINGER:
				return FatFingerCommand.run(OptionHandler.parseOptions(cmd.stringifyCommand()));
			case GRINDING:
				return GrindingCommand.run(OptionHandler.parseOptions(cmd.stringifyCommand()));
			case FIBONACCIRETRACEMENT:
				return FibonacciRetracementCommand.run(OptionHandler.parseOptions(cmd.stringifyCommand()));
			case PIVOTPOINTS:
				return PivotPointsCommand.run(OptionHandler.parseOptions(cmd.stringifyCommand()));
			default:
				return "";
			}		
		} catch (IllegalArgumentException e) {
			return errorMessage;
		}
	}
	
	/**
	 * Ejecuta el comando.
	 * @param cmd Comando en formato String.
	 * @param args Argumentos en formato String.
	 */
	public static String runCommand(String cmd, String args) {
		
		try {
			switch(Commands.valueOf(cmd)) {
			case EXIT:
				return "Saliendo...";
			case HELP:
				return helpMessage;
			case PRICE:
				return PriceCommand.run(OptionHandler.parseOptions(args));
			case EMA:
				return EMACommand.run(OptionHandler.parseOptions(args));
			case SMA:
				return SMACommand.run(OptionHandler.parseOptions(args));
			case PRICECHANGE:
				return PriceChangeCommand.run(OptionHandler.parseOptions(args));
			case FATFINGER:
				return FatFingerCommand.run(OptionHandler.parseOptions(args));
			case GRINDING:
				return GrindingCommand.run(OptionHandler.parseOptions(args));
			case FIBONACCIRETRACEMENT:
				return FibonacciRetracementCommand.run(OptionHandler.parseOptions(args));
			case PIVOTPOINTS:
				return PivotPointsCommand.run(OptionHandler.parseOptions(args));
			default:
				return "";
			}		
		} catch (IllegalArgumentException e) {
			return errorMessage;
		}
	}
	
	/**
	 * Construye el mensaje de ayuda de la linea de comando con los comandos disponibles.
	 * @return Mensaje en formato String.
	 */
	private static String helpMessage() {
		String help = "Comandos disponibles: ";
		for(int i=0; i<Commands.values().length; i++) {
			if ((i%7)==0) {
				help+="\n";
			}
			help += Commands.values()[i].name() + " ";
		}
		help += "\n \n Ejemplo: PRICE -PAIR BTC/EUR";
		return help;		
	}
}
