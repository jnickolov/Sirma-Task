package task.sirma.processing;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import task.sirma.data.CoworkerPeriod;
import task.sirma.data.InputData;
import task.sirma.data.TableData;

public class DataProcessor {
	// key: empId
	private Map<Integer, List<InputData>> periodsPerEmployee = new TreeMap<>();
	
	// key: (empId1, empId2), value: list of (projId, days)
	private Map<OrderedPair, List<ProjectPeriod>> coworkingPeriods = new TreeMap<>();  
	
	public DataProcessor (Set<InputData> inputData) {
		inputData.forEach (dat -> {
			Integer empId = dat.getEmpId();
			if (periodsPerEmployee.get (empId) == null) {  // first occurrence
				periodsPerEmployee.put (empId, new LinkedList<>());
			}
			periodsPerEmployee.get (empId).add (dat);
		});
		
		Set<Integer> empIds = periodsPerEmployee.keySet ();
		empIds.forEach (empId1 -> {
			empIds.stream()
				.filter (eId -> eId > empId1)  // keep only bigger empIds
				.forEach (empId2 -> {
					handleData (empId1, empId2); 
			});
		});
	}

	//  Returns the calculated result
	public List<CoworkerPeriod> getResult() {
		// Accumulated result(s). 
		// It is possible that more than one pair has the same period of co-working
		List<CoworkerPeriod> res = new LinkedList<>();
		
		Set<OrderedPair> pairs = coworkingPeriods.keySet();
		int max = -1;
		
		for (OrderedPair pair: pairs) {
			List<ProjectPeriod> periods = coworkingPeriods.get (pair);
			
			Integer total = periods.stream().map (p -> p.days). reduce ((x, y) -> x + y).get();
			if (total != null) {
				if (total > max) {  // start a new list of winners
					res.clear ();
					max = total;
					res.add (new CoworkerPeriod (pair.empId1, pair.empId2, total));
				
				} else if (total == max) {  // add to the existing list  
					res.add (new CoworkerPeriod (pair.empId1, pair.empId2, total));
				}
			}
		};
		
		return res;
	}

	public List<TableData> getTableData () {
		List<TableData> res = new LinkedList<>();
		
		Set<OrderedPair> keys = coworkingPeriods.keySet ();
		keys.forEach (key -> {
			coworkingPeriods.get (key).forEach (pp -> {
				res.add (new TableData (key.empId1, key.empId2, pp.projId, pp.days));
			});
		});
		
		return res;
	}
	
	private void handleData (Integer empId1, Integer empId2) {
		List<InputData> data1 = periodsPerEmployee.get (empId1);
		List<InputData> data2 = periodsPerEmployee.get (empId2);
		
		data1.forEach (d1 -> {
			Integer projId = d1.getProjId (); 
			data2.forEach (d2 -> {
				if (projId.equals (d2.getProjId())) {
					LocalDate dFrom = (d1.getFromDate().isAfter (d2.getFromDate())) 
							? d1.getFromDate()
							: d2.getFromDate();
					LocalDate dTo = (d1.getToDate().isBefore (d2.getToDate())) 
							? d1.getToDate()
							: d2.getToDate();
					
					int days = (int)(ChronoUnit.DAYS.between (dFrom, dTo)) + 1;  // +1 because last day is included
					if (days > 0) {  // save coworking period
						OrderedPair empPair = new OrderedPair (d1.getEmpId(), d2.getEmpId());
						List<ProjectPeriod> lst = coworkingPeriods.get (empPair);
						if (lst == null) { // first entry
							lst = new LinkedList<>();
							coworkingPeriods.put (empPair, lst);
						}
						lst.add (new ProjectPeriod (projId, days));
					}
				}
			});
		});
	}
}

// Helper non-public classes

class ProjectPeriod {
	Integer projId;
	Integer days;
	
	ProjectPeriod (Integer prjId, Integer days) {
		this.projId = prjId;
		this.days = days;
	}

	@Override
	public String toString() {
		return "(" + this.projId + ": " + this.days + ") ";
	}
}

// A helper class of ordered pair of empIds, used as a key in data maps (simplified syntax)
class OrderedPair implements Comparable<OrderedPair> {
	Integer empId1, empId2;
	
	OrderedPair (Integer e1, Integer e2) {
		if (e1 > e2) {  //  assure e1 < e2
			Integer tmp = e1;
			e1 = e2;
			e2 = tmp;
		}
		this.empId1 = e1;
		this.empId2 = e2;
	}

	@Override
	public int compareTo (OrderedPair other) {
		int delta = this.empId1 - other.empId1;
		return (delta != 0) ? delta : this.empId2 - other.empId2;
	}
	
	@Override
	public String toString() {
		return "[" + this.empId1 + ", " + this.empId2 + "]";
	}
}
