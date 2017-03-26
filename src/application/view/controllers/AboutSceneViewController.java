package application.view.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;

public class AboutSceneViewController {
	@FXML
	RadioButton ruRadio, engRadio;
	@FXML
	TextArea textArea;
	
	String eng = "\nNovac Password Database."+
				"\n"+
				"\nUtility for keeping secure passwords."+
				"\nUses .npdb files."+
				"\nUse menu to add, change or remove passwords."+
				"\nYou can use included password generator to generate strong passwords in change menu."+
				"\nClick File-Save to save your database or File-Open to open another one."+
				"\nBe sure to use strong password when saving database."+
				"\nClosing database through \"X\"-mark will not damage saved version."+
				"\nWhen open databases, choose \"backup\" to save extra copy of your database before changing it."+
				"\nDue to JVM, if you want to use app in different OSs with different encodings (like windows-1251 and UTF-8), use latinic characters.";
	String ru = "\nNovac Password Database."+
			"\n"+
			"\nУтилита для безопасного хранения паролей."+
			"\nСтандартное расширение - .npdb."+
			"\nИспользуйте меню для добавления, изменения или удаления паролей."+
			"\nВы можете использовать встроенный генератор сильных паролей в меню изменения пароля."+
			"\nИспользуйте меню Файл для сохранения текущей или открытия новой базы паролей."+
			"\nИспользуйте максимально сильный пароль для сохранения базы данных."+
			"\nЗакрытие базы данных путём закрытия приложения не повредит её содержимого."+
			"\nОтметьте \"backup\" при загрузке базы, чтобы сохранить запасную копию этой базы до её изменения."+
			"\nИз-за отличий в JVM, если вы хотите использовать приложение в разных ОС с разными кодировками (вроде windows-1251 и UTF-8), используйте латиницу.";
	
	@FXML
	public void initialize() {
		textArea.setText(eng);
		ruRadio.setOnAction((e)->{textArea.setText(ru);});
		engRadio.setOnAction((e)->{textArea.setText(eng);});
	}
}
