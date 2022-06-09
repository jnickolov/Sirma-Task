package task.sirma.application;

import java.util.LinkedList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import task.sirma.data.TableData;

public class ResultTable extends TableView<TableData> {

	public ResultTable () {
		super ();
		
		TableColumn<TableData, Integer> colEmp1 = new TableColumn<> ("Emp 1");
		colEmp1.setCellValueFactory (new PropertyValueFactory<TableData, Integer> ("empId1"));
		colEmp1.setEditable (false);
		colEmp1.setCellValueFactory (new PropertyValueFactory<TableData, Integer> ("empId1"));

		TableColumn<TableData, Integer> colEmp2 = new TableColumn<> ("Emp 2");
		colEmp2.setCellValueFactory (new PropertyValueFactory<TableData, Integer> ("empId2"));
		colEmp2.setEditable (false);
		colEmp2.setCellValueFactory (new PropertyValueFactory<TableData, Integer> ("empId2"));

		TableColumn<TableData, Integer> colProj = new TableColumn<> ("Project");
		colProj.setCellValueFactory (new PropertyValueFactory<TableData, Integer> ("projId"));
		colProj.setEditable (false);
		colProj.setCellValueFactory (new PropertyValueFactory<TableData, Integer> ("projId"));

		TableColumn<TableData, Integer> colDays = new TableColumn<> ("Days");
		colDays.setCellValueFactory (new PropertyValueFactory<TableData, Integer> ("days"));
		colDays.setEditable (false);
		colDays.setCellValueFactory (new PropertyValueFactory<TableData, Integer> ("days"));

		this.getColumns().addAll (colEmp1, colEmp2, colProj, colDays);
	}
	
	public void setData (List<TableData> data) {
		if (data == null)
			data = new LinkedList<>();
		ObservableList<TableData> tableData = FXCollections.observableArrayList (data);
		this.setItems (tableData);
	}
	
}
