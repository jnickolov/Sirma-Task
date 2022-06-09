package task.sirma.processing;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DateFormatFactory {
	private static Set<String> formats; 
	
	private static Pattern yyyymmdd = Pattern.compile (
		      "^((2000|2400|2800|(19|2[0-9])(0[48]|[2468][048]|[13579][26]))-02-29)$" 
		      + "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"
		      + "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$" 
		      + "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$");

	private static Pattern mmddyyyy = Pattern.compile (
		      "^02/29/((2000|2400|2800|(19|2[0-9])(0[48]|[2468][048]|[13579][26])))$" 
		      + "|^02/(0[1-9]|1[0-9]|2[0-8])/(((19|2[0-9])[0-9]{2}))$"
		      + "|^(0[13578]|10|12)/(0[1-9]|[12][0-9]|3[01])/(((19|2[0-9])[0-9]{2}))$" 
		      + "|^(0[469]|11)/(0[1-9]|[12][0-9]|30)/(((19|2[0-9])[0-9]{2}))$");

	private static Pattern ddmmyyyy = Pattern.compile (
		      "^29.02.((2000|2400|2800|(19|2[0-9])(0[48]|[2468][048]|[13579][26])))$" 
		      + "|^(0[1-9]|1[0-9]|2[0-8]).02.(((19|2[0-9])[0-9]{2}))$"
		      + "|^(0[1-9]|[12][0-9]|3[01]).(0[13578]|10|12).(((19|2[0-9])[0-9]{2}))$" 
		      + "|^(0[1-9]|[12][0-9]|30).(0[469]|11).(((19|2[0-9])[0-9]{2}))$");

	private static Map<String, Pattern> patterns = new HashMap<>();
	
	static {
		patterns.put ("yyyy-MM-dd", yyyymmdd);
		patterns.put ("MM/dd/yyyy", mmddyyyy);
		patterns.put ("dd.MM.yyyy", ddmmyyyy);
		
		formats = patterns.keySet ();
	}
	
	public static List<String> getNames() {
		return formats.stream()
			.map (String::toUpperCase)
			.collect (Collectors.toList());
	}
	
	public static DateTimeFormatter getFormatter (String afmt) {
		afmt = afmt.toUpperCase ();
		for (String fmt: formats) {
			if (fmt.toUpperCase().equals (afmt))
				return DateTimeFormatter.ofPattern (fmt);
		}
		
		return null;
	}

	public static DateTimeFormatter detectFormatter (String sDate) {
		for (String fmt: formats) {
			if (patterns.get (fmt).matcher (sDate).matches()) {
				return getFormatter (fmt);
			}
		}
		
		return null;
	}
}
