package application.view.controllers;

import application.Main;
import application.logic.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
//эта же форма используется для изменения уже созданных записей
public class AddSceneViewController{
	@FXML
	private Button saveButton, cancelButton, randomButton;
	@FXML
	private TextField nameField, passwordField,descriptionField;
	@FXML
	private Label infoLabel;

	private static Line inChanging; //переменная, содержащая линию, которую мы меняем
	
	@FXML
	public void initialize(){
		
		DBViewController.lines.remove(Main.LNULL); //убираем пустую отладочную линию, чтобы не копились
		
		if (inChanging!=null){ //если у нас есть линия на замену - извлекаем из неё данные
			nameField.setText(inChanging.getName());
			passwordField.setText(inChanging.getPass());
			descriptionField.setText(inChanging.getDesc());
		}
		
		randomButton.setOnAction( (e)->{
			passwordField.setText(RandomPassword.generate(20));
		});
		
		saveButton.setOnAction( (e)->{
			save();
		});
		
		descriptionField.setOnAction((e) ->{
			save();
		});
		
		nameField.setOnAction((e) ->{
			save();
		});
		
		passwordField.setOnAction((e) ->{
			save();
		});
		
		cancelButton.setOnAction( (e)->{ //простая отмена всего
			inChanging=null;
			Main.functionalStage.close();
		});
	}
	
	private void save(){
		String name = nameField.getText();
		String pass = passwordField.getText();
		String desc = descriptionField.getText();
		if (!Checker.noPipe(name)||!Checker.noPipe(pass)||!Checker.noPipe(desc)){
			infoLabel.setText("Please, do not use '|' (pipe) in any field");
			
		} else if (inChanging!=null){ //если есть линия на замену - вставляем изменения в неё
			inChanging.setProp(name, pass, desc);
			DBViewController.lines.add(Main.LNULL); //добавляем пустую линию для обновления ObservableList
			inChanging=null;//зануляем линию
			Main.functionalStage.close();
		} else { //если нет линии на замену - пишем новую и сразу добавляем
			DBViewController.lines.add(
				new Line(/**/name, pass, desc/**/) 
			);
			Main.functionalStage.close();
		}
	}
	
	protected static void setLine(Line l){ //сюда передаём линию, которую желаем изменить
		inChanging=l;
	}
	
	
}
