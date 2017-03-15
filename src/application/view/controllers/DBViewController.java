package application.view.controllers;

import application.Loader;
import application.Main;
import application.logic.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.input.MouseButton;

public class DBViewController{
	static protected ObservableList<Line> lines;
	@FXML
	private MenuItem menuSave, menuOpen, menuAbout;
	@FXML
	private TableView<Line> mainTable;
	@FXML
	private TableColumn<Line,String> tableName, tablePassword,tableDesc, numberColumn;
	@FXML
	private Button addButton, removeButton, editButton;
	@FXML
	private Label infoLabel;
	protected Loader addSceneLoader, saveSceneLoader, aboutSceneLoader; //загрузчики сцен
	
	
	@FXML
	public void initialize(){
		//загружаем все экраны, необходимые для работы
		saveSceneLoader = new Loader(Main.functionalStage,"view/SaveSceneView.fxml",false);
		addSceneLoader = new Loader(Main.functionalStage,"view/AddSceneView.fxml",false);
		aboutSceneLoader = new Loader(Main.functionalStage, "view/AboutSceneView.fxml",false);
		
        // устанавливаем тип и значение которое должно хранится в колонке
        tableName.setCellValueFactory(new PropertyValueFactory<Line,String>("name"));
        tablePassword.setCellValueFactory(new PropertyValueFactory<Line,String>("pass"));
        tableDesc.setCellValueFactory(new PropertyValueFactory<Line,String>("desc"));
        mainTable.setItems(lines);
        
        //объекты для копирования в буфер обмена
        Clipboard clipboard = Clipboard.getSystemClipboard();
		ClipboardContent content = new ClipboardContent();
		String defS="Double-click to change; Right-click to save password to clipboard";
        infoLabel.setText(defS);
        
        addButton.setOnAction((e)->{
        	infoLabel.setText(defS);
        	addSceneLoader.setUp();
        });
        
        mainTable.setOnMouseClicked((e)->{ //открывает по двойному клику окошко edit
        	if ( (e.getButton()).equals(MouseButton.SECONDARY) ) { //если нажата пкм
        		Line l =mainTable.getSelectionModel().getSelectedItem();
        		content.putString(l.getPass());
        		clipboard.setContent(content);
        		infoLabel.setText("Password copied to clipboard");
        	}
        	
        	if (e.getClickCount()>1){ //считаем количество кликов
        		infoLabel.setText(defS);
	        	editEvent();
        	}
        	
        });
        
        removeButton.setOnAction((e)->{
        	infoLabel.setText(defS);
        	Line l = mainTable.getSelectionModel().getSelectedItem();
        	lines.remove(l);
        });
        
        editButton.setOnAction( (e) -> {
        	infoLabel.setText(defS);
        	editEvent();
        });
        
        menuOpen.setOnAction( (e) -> {
        	Main.loginViewLoader.setUp();
        	Main.dbStage.close();
        });
        
        menuSave.setOnAction( (e) -> {
        	infoLabel.setText(defS);
        	saveSceneLoader.setUp();
        });
        
        menuAbout.setOnAction((e) ->{
        	aboutSceneLoader.setUp();
        });
        
	}//end of initialize()
	
	private void editEvent(){ //метод для изменения наполнения строчки
		Line l = mainTable.getSelectionModel().getSelectedItem();
		//этот if нужен для избежание ошибки, при которой пользователь пытается изменить
		//строку LNULL, нужную лишь для отладки листа
		if (l.equals(Main.LNULL)) {
			lines.remove(Main.LNULL);
			addSceneLoader.setUp();
		}else {
	    	AddSceneViewController.setLine(l);
	    	addSceneLoader.setUp();
		}
	}//end of editEvent()
}
