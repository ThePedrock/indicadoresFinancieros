package org.stoneCoding.indicadoresFinancieros.ui;

import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.stoneCoding.indicadoresFinancieros.ui.commandLine.*;

public class CommandLine {
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
		GRINDING
	}	
	public static enum Options {
		MSPERIOD,
		PAIR,
		NPERIODS,
		EMAPERIODS,
		MINNEARLOW,
		MAXNEARLOW,
		PRICEOSCILLATION,
		HELP
	}
	
	public final static Map<Options, String> globalOptionHelpMap = Stream.of (new Object[][] {
		{Options.MSPERIOD, "Segundos del periodo."},
		{Options.PAIR, "BASE/QUOTE del par separados por '" + CommandLine.slash + "'."},
		{Options.NPERIODS, "Número de periodos"},
		{Options.EMAPERIODS, "Número de periodos para cálculo del EMA."},
		{Options.MINNEARLOW, "Diferencia mínima permitida del cierre con respecto a su mínimo en tanto por uno."},
		{Options.MAXNEARLOW, "Diferencia máxima permitida del cierre con respecto a su mínimo en tanto por uno."},
		{Options.PRICEOSCILLATION, "Oscillación mínima exigida del precio con respecto a sus extremos."},
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
		{Options.MSPERIOD, DataType.Long},
		{Options.PAIR, DataType.String},
		{Options.NPERIODS, DataType.Short},
		{Options.EMAPERIODS, DataType.Short},
		{Options.MINNEARLOW, DataType.Double},
		{Options.MAXNEARLOW, DataType.Double},
		{Options.PRICEOSCILLATION, DataType.Double},
		{Options.HELP, DataType.String}
	}).collect(Collectors.toMap(data -> (Options) data[0], data -> (DataType) data[1]));

	public final static String helpMessage = helpMessage();
	
	public final static String errorMessage = "No se reconoce el comando";
	
	/**
	 * Línea de comando de soporte para la aplicación.
	 */
	public CommandLine() {
		while(true) {
			String cmd;
			System.out.println("Introduzca comando: ");
			Scanner sc = new Scanner(System.in);
			while(sc.hasNextLine()) {
				cmd = sc.nextLine();
				String[] aCmd = parseCommand(cmd);
				runCommand(aCmd[0], aCmd[1]);
			}
			sc.close();
		}
	}

	/**
	 * Parsea el input recibido en comando y opciones.
	 * @param data input recibido.
	 * @return Arreglo de dos dimensiones con comando y opciones
	 */
	public String[] parseCommand(String data) {
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
	 * @param cmd Comando en formato String.
	 * @param args Argumentos en formato String.
	 */
	public void runCommand(String cmd, String args) {
		
		try {
			switch(Commands.valueOf(cmd)) {
			case EXIT:
				System.out.println("Saliendo...");
				System.exit(0);
				break;
			case HELP:
				System.out.println(helpMessage);
				break;
			case PRICE:
				System.out.println(PriceCommand.run(OptionHandler.parseOptions(args)));
				break;
			case EMA:
				System.out.println(EMACommand.run(OptionHandler.parseOptions(args)));
				break;
			case SMA:
				System.out.println(SMACommand.run(OptionHandler.parseOptions(args)));
				break;
			case PRICECHANGE:
				System.out.println(PriceChangeCommand.run(OptionHandler.parseOptions(args)));
				break;
			case FATFINGER:
				System.out.println(FatFingerCommand.run(OptionHandler.parseOptions(args)));
				break;
			case GRINDING:
				System.out.println(GrindingCommand.run(OptionHandler.parseOptions(args)));
				break;
			default:
				break;
			}		
		} catch (IllegalArgumentException e) {
			System.out.println(errorMessage);
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
