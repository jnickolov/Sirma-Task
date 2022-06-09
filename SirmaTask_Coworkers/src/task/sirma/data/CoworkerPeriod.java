package task.sirma.data;

public class CoworkerPeriod {
	private Integer empId1;
	private Integer empId2;
	private Integer days;

	public CoworkerPeriod (Integer empId1, Integer empId2, Integer days) {
		super ();
		this.empId1 = empId1;
		this.empId2 = empId2;
		this.days = days;
	}
	
	public Integer getEmpId1() {
		return this.empId1;
	}
	
	public Integer getEmpId2() {
		return this.empId2;
	}
	
	public Integer getDays() {
		return this.days;
	}

	@Override
	public String toString() {
		return "CoworkerPeriod [empId1=" + this.empId1 + ", empId2=" + this.empId2 + ", days=" + this.days + "]";
	}
	
}
