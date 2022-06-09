package task.sirma.data;

public class TableData implements Comparable<TableData> {
	private Integer empId1; 
	private Integer empId2; 
	private Integer projId; 
	private Integer days;
	
	public TableData (Integer empId1, Integer empId2, Integer projId, Integer days) {
		super ();
		this.empId1 = empId1;
		this.empId2 = empId2;
		this.projId = projId;
		this.days = days;
	}

	public Integer getEmpId1() {
		return this.empId1;
	}

	public Integer getEmpId2() {
		return this.empId2;
	}

	public Integer getProjId() {
		return this.projId;
	}

	public Integer getDays() {
		return this.days;
	}

	@Override
	public int compareTo (TableData other) {
		int delta = this.getEmpId1() - other.getEmpId1();
		if (delta != 0) {
			return delta;
		} else {
			delta = this.getEmpId2() - other.getEmpId2();
		}

		if (delta != 0) {
			return delta;
		} else {
			delta = this.getEmpId2() - other.getEmpId2();
		}

		if (delta != 0) {
			return delta;
		} else {
			delta = this.getProjId() - other.getProjId();
		}
	
		if (delta != 0) {
			return delta;
		} else {
			return other.getDays() - this.getDays();  // descending order
		}
	}

}
