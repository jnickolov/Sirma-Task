package task.sirma.ctrl;

import java.util.List;
import java.util.Set;

import task.sirma.data.CoworkerPeriod;
import task.sirma.data.InputData;
import task.sirma.data.TableData;
import task.sirma.processing.DataLoader;
import task.sirma.processing.DataProcessor;
import task.sirma.processing.DateFormatFactory;

public class Controller {
	private List<CoworkerPeriod> result;
	private List<TableData> tableData;
	
	public void process (String dataFileName, String dateFormat) throws Exception {
		DataLoader loader = new DataLoader (DateFormatFactory.getFormatter (dateFormat));
		Set<InputData> inputData = loader.processFile (dataFileName);
		
		DataProcessor dp = new DataProcessor (inputData);
		
		result = dp.getResult();
		tableData = dp.getTableData ();
	}

	public List<CoworkerPeriod> getResultList() {
		return this.result;
	}

	public List<TableData> getTableData() {
		return this.tableData;
	}

}
