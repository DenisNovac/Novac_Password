package application.logic;

import java.io.*;
import java.util.ArrayList;


/*
Для работы с этим классом в графическом режиме потребуется некоторое преобразвование в аналог ObservableList в JavaFX.
В классе используются исключительно ArrayList для поддержания переносимости и простоты разработки. 
*/

public abstract class FileWorker {
	private static int DIGLENGTH; //стандартная длина хеш-сообщения, установленного по умолчанию
	public static boolean isBackupNeeded=false;
	
	protected static void setDigLength(int dl){ //метод вызывается при установке Hasher.setAlgorithm() из метода Main. 
		DIGLENGTH=dl;
	}

	
	
	public static String save(ArrayList<Line> list, String path, String password){ //метод для сохранения БД
		File bdFile;
		try { //записываем бд в файл
			bdFile = new File(path); //получаем файл по полученному пути
			
			try (
				 BufferedWriter bw = new BufferedWriter(  new OutputStreamWriter(new FileOutputStream(bdFile), "UTF-8")  )
				){
				bw.write(Hasher.toHash(password)+"\n"); //вписываем хешированный пароль в начало БД
				for (Line l:list){
					//если пользователь оставил поля пустыми - то пишем туда пробелы
					String name = l.getName();
					if (name.length()<1) name=" ";
					String pass = l.getPass();
					if (pass.length()<1) pass=" ";
					String desc = l.getDesc();
					if (desc.length()<1) desc=" ";
					String LINE = name+"|"+pass+"|"+desc+"\n"; //создаём строку, пригодную для обратного парсинга - с пайпами-разделителями
					bw.write(LINE);
				}	
			}
		} catch (IOException e) {
			System.out.println("Error in save(): "+e);
			return e.getMessage();
		}
		encodeFile(bdFile,password);//кодируем бд, что закодирует и наш хеш-пароль
		return ("Saving success");
	}//end of save method
	
	
	
	
	
	
	
	public static ArrayList<Line> openDB(String path, String password) {
		File bdFile = new File(path);
		//проверяем пароль
		if (!decodeFile(bdFile,password)){
			return null;
		}
		
		encodeFile(bdFile,password);//расшифровываем файл если пароль подошёл
		
		ArrayList<Line> lines = new ArrayList<Line>();//создаём лист - по сути, это наша бд
		
		try (
				BufferedReader br = new BufferedReader(  new InputStreamReader(new FileInputStream(bdFile), "UTF-8")  )
			){
			StringToWords stringToWords; //добавляем экземпляр-декодер
			String readed=br.readLine(); //читаем первую линию и пропускаем её - это хешкод пароля
			while(true){
				readed = br.readLine();
				if (readed==null) break;
				
				stringToWords = new StringToWords(2,readed); //создаём конкретный парсер
				ArrayList<String> readedWords= stringToWords.parse(); //парсим строку, получая массив
				String name = readedWords.get(0); //зная порядок значений в массиве, записываем их
				String pass = readedWords.get(1);
				String desc = readedWords.get(2);
				lines.add(new Line(name,pass,desc)); //добавляем в лист новую строчку
			}

		}catch (Exception e){
			System.out.println("Error in openDb(): "+e);
		}	
		
		
		encodeFile(bdFile,password);//шифруем обратно, во время работы пользователя с бд, файл на пк остаётся зашифрованным
		
		if (isBackupNeeded) makeBackUp(bdFile); //делаем бекап файла, если нужно
		return lines;
	}//end of openDb method

	
	
	
	
	
	
	public static void encodeFile(File inputFile, String password){ //метод просто шифрует файл исключающим или
		try {
			//password=Hasher.toHash(password); //мы шифруем, используя хэш, чтобы даже самые слабые пароли стали сильнее
			//отказался от такого метода, т.к. пароль test123456 по какой-то причине ломал программу
			char[] passwordChar = password.toCharArray();
			RandomAccessFile raf = new RandomAccessFile (inputFile,"rw"); //RAF, чтобы писать в тот же бит, который только что прочли
			int read=0; //сюда пишем ввод
			int car=0; //каретка
			while(true){
				raf.seek(car);
				read = raf.read();
				if (read==-1) break;
				for (int i=0; i<passwordChar.length; i++)
					read^=(passwordChar[i]+i);
				
				raf.seek(car);
				raf.write(read);
				car++;
			}
			raf.close();
		}catch (Exception e){
			System.out.println("Error in encodeFile(): "+e);
		}
	}//end of encodeFile method
	
	
	
	
	
	
	public static boolean decodeFile(File inputFile, String password){ //метод для проверки пароля, вписанного в начало файла
		String passHash;
		String isPassHash;
		//проверяем хэш
		try(BufferedReader br = new BufferedReader(   new InputStreamReader(new FileInputStream(inputFile), "UTF-8"))   ){
			char[] isPassHashCh=new char[DIGLENGTH*2]; //Массив для будущего хэша, считанного из файла 
			//128 - длина в char файла, шифрованного алгоритмом SHA-512
			
			for (int i=0; i<DIGLENGTH*2;i++){
				isPassHashCh[i]=(char) br.read();
			}
			//убрал защиту через хеш от пароля, ибо программу ломает пароль test123456
			char[] passwordCh=(/*Hasher.toHash(password)*/password).toCharArray(); //выводит пароль в массив чаров
			
			for (int i=0; i<isPassHashCh.length; i++){ //декодируем взятый из файла хэш
				
				for (int j=0; j<passwordCh.length; j++)
					isPassHashCh[i]^=(passwordCh[j]+j);
				
			}
			isPassHash=new String(isPassHashCh);	//пишем взятый из файла хэш в строку
			passHash = Hasher.toHash(password); //получаем хэш от пароля
			
		} catch (Exception e){
			System.out.println("Error in decodeFile(): "+e);
			return false;
		}
		if (passHash.equals(isPassHash)){ //сравниваем пароль и декодированный хэш
			return true;
		}else {
			return false;
		}
	}//end of decodeFile method
	
	
	
	
	
	
	
	private static void makeBackUp(File file){ //метод создаёт бэкап открываемого файла на случай, если пользователь повредит пароли
		String path = file.getPath();//составляем путь для бэкапа
		String newpath = path.substring(0, path.lastIndexOf('.'));
		newpath+="BCK.npdb"; 
		try (BufferedInputStream bis = new BufferedInputStream (new FileInputStream(file));
			 BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newpath));){
			int read;
			
			while(true){ //простое копирование исходного файла
				read=bis.read();
				if (read==-1) break;
				bos.write(read);
			}
		}catch (IOException e){
			System.out.println("Error in makeBackUp(): "+e);
		}
	}//end of makeBackUp()
	
	
	
}//end of FileWorker class
