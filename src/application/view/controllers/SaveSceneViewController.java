package application.view.controllers;

import java.io.File;
import java.nio.charset.Charset;

import application.Main;
import application.logic.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;


public class SaveSceneViewController {
	@FXML
	Button saveButton, pathButton, cancelButton;
	@FXML
	TextField passField,confirmField;
	@FXML
	TextField pathField;
	@FXML
	Label infoLabel, encodingLabel;
	
	@FXML
	public void initialize(){
		DBViewController.lines.remove(Main.LNULL); //убираем пустую отладочную линию, чтобы не копились
		infoLabel.setText("Be sure to use strong password");
		encodingLabel.setText("System encoding: "+Charset.defaultCharset().toString());
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select file .npdb or create new");
		fileChooser.setInitialDirectory(new File("."));
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Database file", "*.npdb")); //выбираем разрешение. НЕ ЗАБУДЬ ЗВЁЗДОЧКУ ПИДР
		
		
		pathButton.setOnAction( (e) -> {
			String path = ( fileChooser.showSaveDialog(Main.functionalStage) ).getPath(); //открываем сейв-диалог
	        pathField.setText(path);
		});
		
		saveButton.setOnAction( (e) -> {
			//проверям, что все строки заполнены
			if (! Checker.passwordOk(passField.getText())){
				infoLabel.setText("password must conatin only latin, numbers and underline");
				return;
			}
			
			if (passField.getText().length()<10){
				infoLabel.setText("password must be 10 or more letters");
				return;
			}
			if ( ! passField.getText().equals( confirmField.getText() )  ) {
				infoLabel.setText("password and confirm isnt equals");
				return;
			}
			if (pathField.getText().length()<3){
				infoLabel.setText("Enter the path to save your database");
				return;
			}
			
			if (  !Checker.pathOk( (pathField.getText()) )  ) {
				infoLabel.setText("Incorrect path: Input some .npdb file name!\n");
				return;
			}
			
			String answer=FileWorker.save( //преобразуем наш observable в arraylist
					ArrayListConverter.toArray( DBViewController.lines ), 
					pathField.getText(),passField.getText());
			
			infoLabel.setText(answer);
		});
		
		
		cancelButton.setOnAction( (e)->{
			Main.functionalStage.close();
		});
		
	}
}
