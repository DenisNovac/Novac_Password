package application.logic;

//класс инкапсулирует строки из файла, каждый экземпляр - строка
public class Line {
	String name;
	String pass;
	String desc;
	
	public Line(){
		
	}
	public Line(String name, String pass, String desc){
		this.name=name;
		this.pass=pass;
		this.desc=desc;
	}
	//следующие методы необходимы
	//для работы DBViewController
	public void setProp(String name, String pass, String desc){
		this.name=name;
		this.pass=pass;
		this.desc=desc;
	}
	
	
	public String getName(){
		return name;
	}
	
	public String getPass(){
		return pass;
	}
	
	public String getDesc(){
		return desc;
	}
}
