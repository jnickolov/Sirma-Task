package task.sirma.data;

import java.time.LocalDate;
import java.util.Objects;

public class InputData {
	private Integer projId;
	private Integer empId;
	private LocalDate fromDate;
	private LocalDate toDate;
	
	public InputData (Integer projId, Integer empId, LocalDate fromDate, LocalDate toDate) {
		super ();
		this.projId = projId;
		this.empId = empId;
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	public Integer getProjId() {
		return this.projId;
	}
	
	public Integer getEmpId() {
		return this.empId;
	}

	public LocalDate getFromDate() {
		return this.fromDate;
	}
	
	public LocalDate getToDate() {
		return this.toDate;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash (empId, fromDate, projId, toDate);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass () != obj.getClass ())
			return false;
		InputData other = (InputData) obj;
		return Objects.equals (this.empId, other.empId) && Objects.equals (this.fromDate, other.fromDate) && Objects.equals (this.projId, other.projId)
				&& Objects.equals (this.toDate, other.toDate);
	}

	@Override
	public String toString() {
		return "InputData [projId=" + this.projId + ", empId=" + this.empId + ", fromDate=" + this.fromDate + ", toDate=" + this.toDate + "]";
	}
}
