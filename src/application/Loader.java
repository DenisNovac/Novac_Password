package application;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

//класс для загрузки и показа макетов
//при создании экземпляра класса все переменные сохраняются в нём

public class Loader {
	private Stage stage;
	private FXMLLoader loader;
	private Boolean resizable;
	private AnchorPane root;
	private String fxmlpath;
	
	private Scene scene;
	
	public Loader (Stage stage, String fxmlpath, Boolean resizable){
		this.stage=stage; //получаем ссылку на мейн сцену
		this.fxmlpath=fxmlpath;
		this.resizable=resizable;
	}

	public void setUp() {
		//пересоздаём сцену заново каждый раз
		
		loader = new FXMLLoader(); 
		loader.setLocation(Loader.class.getResource(fxmlpath));
		root = new AnchorPane();
		//создаём сцену
		try {
			root = loader.load();
		} catch (IOException e) {
			System.out.println("Loading layout error!");
		}
		scene = new Scene(root);
		scene.getStylesheets().add("application/view/arra.css");
		//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.sizeToScene();
		stage.centerOnScreen();
		stage.setResizable(resizable);
		stage.show();
	}
}
