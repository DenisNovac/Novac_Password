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
			byte[] passwordBytes = password.getBytes();
			RandomAccessFile raf = new RandomAccessFile (inputFile,"rw"); //RAF, чтобы писать в тот же бит, который только что прочли
			int read=0; //сюда пишем ввод
			int car=0; //каретка
			int j=0; // счетчик положения в массиве пароля
			while(true){
				raf.seek(car);
				read = raf.read();
				if (read==-1) break;
				// если пароль закончился - прогоняем по новой
				if (j==passwordBytes.length) j=0;
				read=read^passwordBytes[j];
				
				raf.seek(car);
				raf.write(read);
				j++;
				car++;
				
			}
			raf.close();
		}catch (Exception e){
			System.out.println("Error in encodeFile(): "+e);
		}
	}//end of encodeFile method
	
	
	
	
	public static boolean decodeFile(File inputFile, String password){ // метод для проверки пароля, вписанного в начало файла
		// проверяем хэш
		byte[] isPassHashBytes;
		try(BufferedInputStream br = new BufferedInputStream(   new FileInputStream(inputFile) ) ){
			isPassHashBytes=new byte[DIGLENGTH*2]; // массив для будущего хэша, считанного из файла 
			//128 - длина в char файла, шифрованного алгоритмом SHA-512
			
			for (int i=0; i<DIGLENGTH*2;i++){
				isPassHashBytes[i]=(byte) br.read(); // читаем хеш пароля из файла
			}
			br.close();
			
			// выводит пароль в массив чаров
			byte[] passwordBytes=password.getBytes(); 
			int j=0; // счетчик для массива паролей
			// декодируем взятый из файла хэш
			for (int i=0; i<isPassHashBytes.length; i++){ 
				if (j==passwordBytes.length) j=0;
				isPassHashBytes[i]=(byte) (isPassHashBytes[i]^passwordBytes[j]);
				j++;	
			}
			
			
		} catch (Exception e){
			System.out.println("Error in decoding file: "+e);
			return false;
		}
		
		byte[] passwordHashBytes = Hasher.toHash(password).getBytes();
		
		boolean r=true;
		for (int i=0; i<DIGLENGTH*2; i++){
			if (passwordHashBytes[i]!=isPassHashBytes[i]) {
				r=false;
				break;
			}
		}
		
		return r;
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
