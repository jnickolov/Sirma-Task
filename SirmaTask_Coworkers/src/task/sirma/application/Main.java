package task.sirma.application;
	
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import task.sirma.ctrl.Controller;
import task.sirma.data.CoworkerPeriod;
import task.sirma.processing.DateFormatFactory;

public class Main extends Application {
	private Controller ctrl = new Controller ();
	
	private String dataFileName = null;

	private Label lblResult, lblError;
	
	private Label lblFile;
	private ChoiceBox<String> cbDateFormat; 
	private Button btnRun;
	private Button btnSelectFile;
	private ResultTable table;
	
	private String fileNotSelected = "File: <not selected>";

	public static void main (String[] args) {
		launch (args);
	}

	@Override
	public void start (Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			root.setTop (createHeader (primaryStage));
			root.setCenter (this.createBody());
			root.setBottom (this.createFooter());
			
			Scene scene = new Scene (root, 800, 600);
			scene.getStylesheets().add (getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene (scene);
			primaryStage.setTitle ("SIRMA coworkers task");
			primaryStage.show();

			assignSelectFileHandler (primaryStage);
			assignRunHandler ();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void assignSelectFileHandler (Stage stage) {
		FileChooser fileChooser = new FileChooser ();
		fileChooser.setTitle ("Open Resource File");
		fileChooser.getExtensionFilters().add (new FileChooser.ExtensionFilter ("CSV Files", "*.csv"));
		
		btnSelectFile.setOnAction (e -> {
			File file = fileChooser.showOpenDialog (stage);
			if (file != null) {
				try {
					clearData();
					clearViewport();
					this.dataFileName = file.getAbsolutePath(); 
					lblFile.setText ("File: " + this.dataFileName);
				} catch (Exception ex) {
					ex.printStackTrace ();
					lblFile.setText (fileNotSelected);
				}
			}
		});
	}
	private void assignRunHandler () {
		btnRun.setOnAction (e -> {
			if (this.dataFileName == null) {
				this.setError ("Select data file");
				return;
			}
			
			try {
				ctrl.process (this.dataFileName, this.cbDateFormat.getValue());
				
				this.table.setData (ctrl.getTableData());

				List<CoworkerPeriod> res = ctrl.getResultList ();
				if (res.size() == 0) {
					this.setResult ("No coworkers at all.");
				} else {
					StringBuilder sb = new StringBuilder ();
					sb.append ("Max coworking days: ").append (res.get(0).getDays())
					  .append (". Pair(s): ");
					res.forEach (cp -> sb.append ("(")
							   	.append (cp.getEmpId1()).append(", ")
					   			.append (cp.getEmpId2()).append(") "));
					this.setResult (sb.toString());
				}
		
			} catch (Exception e1) {
				this.setError (e1.getMessage());
			}
			
		});
	}

	private Node createHeader (Stage stage) {

		lblFile = new Label (fileNotSelected);
		lblFile.getStyleClass ().add ("menu-gap");

		btnSelectFile = new Button ("Select file");

		Label lblDateFormat = new Label ("Date format");

		cbDateFormat = new ChoiceBox<>();
		String autoDetect = "Auto detect";
		cbDateFormat.getItems().add (autoDetect);
		cbDateFormat.getItems().addAll (DateFormatFactory.getNames());
		cbDateFormat.setValue (autoDetect);

		try {
			btnRun = new Button ("RUN", new ImageView (new Image (new FileInputStream ("resources/ok.png"), 16.0, 16.0, true, true)));
		} catch (FileNotFoundException e) {
			btnRun = new Button ("RUN");
		}

		Region sep = new Region ();
		sep.setMaxWidth (99999.9);
		HBox.setHgrow (sep, Priority.ALWAYS);

		HBox menu = new HBox (4);
		menu.setPadding (new Insets (6, 8, 6, 8));
		menu.setBackground (new Background (new BackgroundFill (Color.LIGHTGRAY, null, null)));
		menu.setBorder (new Border (new BorderStroke (Color.DARKGRAY, BorderStrokeStyle.SOLID, null,BorderWidths.DEFAULT)));
		menu.setAlignment (Pos.CENTER_LEFT);
		
		menu.getChildren ().addAll (lblDateFormat, cbDateFormat, lblFile, btnSelectFile, sep, btnRun);
		return menu;
	}

	private Node createBody () {
		this.lblResult = new Label();
		this.lblResult.getStyleClass().add ("result");

		this.table = new ResultTable ();

		VBox body = new VBox (this.lblResult, this.table);
		body.setPadding (new Insets (2, 8, 4, 8));
		
		return body;
	}

	private Node createFooter () {
		this.lblError = new Label();
		this.lblError.getStyleClass().addAll ("result", "error");

		return new HBox (8, this.lblError);
	}

	private void setResult (String msg) {
		this.lblError.setVisible (false);
		this.lblResult.setVisible (true);
		this.lblResult.setText (msg);
	}
	
	private void setError (String msg) {
		this.lblError.setVisible (true);
		this.lblResult.setVisible (false);
		this.lblError.setText (msg);
	}
	
	private void clearData() {
		this.dataFileName = null;
		this.lblFile.setText (this.fileNotSelected);
		this.table.setData (null);
	}


	private void clearViewport() {
		this.setResult ("");
	}
	
}
