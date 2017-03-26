package application.view.controllers;

import java.util.ArrayList;
import application.logic.Line;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

//Класс для преобразования стандартных ArrayList в Java в нестандартные для разных платформ. Поэтому он создан в пакете view.
//В этом классе преобразование до ObservableList, стаднартного для JavaFX. 
//Другому разработчику предстоит написать свой класс для работы с удобными для него видами листов.

abstract class ArrayListConverter {
	protected static ObservableList<Line> toObservable(ArrayList<Line> al){ 
		ObservableList<Line> r;
		try {
			r = FXCollections.observableArrayList(al);
		} catch (Exception e){ //Бросит эксепшн, если arrayList получится пустым, что означает, что пароль неверен
			return null; 
		}
		return r;
	}
	
	protected static ArrayList<Line> toArray (ObservableList<Line> ol){ 
		ArrayList<Line> r= new ArrayList<Line>();
		for (Line l: ol){
			r.add(l);
		}
		return r;
	}
}
