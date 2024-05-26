package otg.stoneCoding.indicadoresFinancieros.json;

import java.util.Map;

public class ApiCommand {

	private final String commandType;
	private final String[][] options;

	public ApiCommand(String command, String[][] options) {
		this.commandType = command;
		this.options = options;
	}

	public String getCommandType() {
		return commandType;
	}
	
	public String[][] getOptions() {
		return options;
	}
	
	public String stringifyOptions() {
		String result = "";
		for (int i=0; i<options.length; i++) {
			if (options[i].length==2) {
				result+="-"+options[i][0]+" "+options[i][1]+" ";
			}
		}
		return result.trim();
	}
	
	public String stringifyCommand() {
		return commandType+" "+stringifyOptions();
	}
}
