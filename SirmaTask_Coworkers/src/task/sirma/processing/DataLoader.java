package task.sirma.processing;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;

import task.sirma.data.InputData;

public class DataLoader {
	private DateTimeFormatter dateFormatter = null;
	
	public DataLoader (DateTimeFormatter fmt) {
		this.dateFormatter = fmt;
	}
	
	public Set<InputData> processFile (String fileName) throws Exception {
		if (fileName == null)
			throw new NullPointerException ("File name not supplied");
		
		Set<InputData> res = new HashSet<>();
		
		File file = new File (fileName);
		if (! file.exists()) {
			throw new FileNotFoundException ("File \"" + fileName + "\" does not exist.");
		}
		
		try (BufferedReader rdr = new BufferedReader (new FileReader (file))) {
			String line = rdr.readLine();
		
			while (line != null) {
				line = line.trim();
				if (line.length() > 0) {
					InputData data = parseLine (line);
					res.add (data);
				}
				line = rdr.readLine();	
			}
			
			return res;
			
		} catch (Exception ex) {
			throw (new Exception (ex.getMessage() + " at line " + (res.size() + 1)));
		}
	}

	private InputData parseLine (String line) throws Exception {
		Integer projId, empId;
		LocalDate fromDate, toDate;
		
		String parts [] = line.trim().split (",");
		if (parts.length < 3)
			throw new IllegalArgumentException ("Wrong data format");
		
		try {
			empId = Integer.parseInt (parts[0].trim());
		} catch (Exception e) {
			throw new NumberFormatException ("Bad format of employee id");
		}
		
		try {
			projId = Integer.parseInt (parts[1].trim());
		} catch (Exception e) {
			throw new NumberFormatException ("Bad format of project id");
		}

		parts[2] = parts[2].trim();
		if (this.dateFormatter == null) {
			DateTimeFormatter dtf = DateFormatFactory.detectFormatter (parts[2]);
			if (dtf == null) {
				throw new Exception ("Wrong from-date format");
			}
			this.dateFormatter = dtf;
		}

		try {
			fromDate = LocalDate.from (this.dateFormatter.parse (parts[2]));
		} catch (DateTimeParseException e) {
			throw new Exception ("Wrong from-date format");
		}

		if (parts.length == 3  // does not exists 
				|| parts[3].trim().length() == 0 // is empty string
				|| "NULL".equals (parts[3].trim().toUpperCase())) {  
			toDate = LocalDate.now();
			
		} else {
			try {
				toDate = LocalDate.from (this.dateFormatter.parse (parts[3].trim()));
			} catch (DateTimeParseException e) {
				throw new Exception ("Wrong to-date format");
			}
		}
		
		if (fromDate.isAfter (toDate)) {  //  negative time period
			throw new Exception ("Bad date interval");
		}
		
		return new InputData (projId, empId, fromDate, toDate);
	}
}
