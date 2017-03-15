package application;
	
import application.logic.Hasher;
import application.logic.Line;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
	final private String ALGORITHM = "SHA-512"; //алгоритм хеширования, используемый в программе, достаточно поменять его тут
	
	public static Stage primaryStage, dbStage, functionalStage;//все фреймы
	public static Loader loginViewLoader,dbViewLoader; //загрузчики сцен
	public static Line LNULL; //константная пустая линия, нужна для обновления ObservableList
	
	@Override
	public void start(Stage pS) {
		Hasher.setAlgorithm(ALGORITHM);
		
		LNULL = new Line();
		//объявляем все фреймы, чтобы потом ссылаться на них из разных частей приложения
		primaryStage=pS;
		primaryStage.setTitle("Novac Password");
		primaryStage.getIcons().add(new Image("application/icon.png"));
		dbStage=new Stage();
		dbStage.setTitle("Novac Password Database");
		dbStage.setMinHeight(350); dbStage.setMinWidth(660); //минимальные размеры стейджа базы данных
		dbStage.getIcons().add(new Image("application/icon.png"));
		functionalStage=new Stage();
		functionalStage.setTitle("Novac Password Function");
		functionalStage.getIcons().add(new Image("application/icon.png"));
		
		functionalStage.initModality(Modality.WINDOW_MODAL);
		functionalStage.initOwner(dbStage);
		
		//показываем окошко входа
		loginViewLoader = new Loader(primaryStage, "view/LoginView.fxml", false);
		dbViewLoader = new Loader(dbStage, "view/DBView.fxml",true);
		loginViewLoader.setUp();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
