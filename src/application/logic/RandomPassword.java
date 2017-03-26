package application.logic;


public abstract class RandomPassword { //метод рандомит пароль из n символов
	public static String generate (int n){
		String password=" ";
		int underlines=2;
		int chars = n;
		while (chars>=0){
			int choice = (int)(Math.random()*5);//0,1,2,3
			switch(choice){
				case 0:
					if (password.length()<2) break;
					if (password.length()==n) break;
					if (password.charAt(n-chars)=='_')break;//последний добавленный символ
					int chance = (int)(Math.random()*underlines);
					if (chance!=1) break;
					password+="_";
					underlines*=2;
					chars--;
					break;
				case 2:
					int latin=(int) (Math.random()*91);
					if (latin<65) break;
					if (password.charAt(n-chars)==(char)latin)break;//последний добавленный символ
					password+=(char)latin;
					chars--;
					break;
				case 4:
					int sLatin= (int) (Math.random()*123);
					if (sLatin<97) break;
					if (password.charAt(n-chars)==(char)sLatin)break;//последний добавленный символ
					password+=(char)sLatin;
					chars--;
					break;

				case 1:
				case 3:
					int number = (int)(Math.random()*58);
					if (number<48) break;
					if (password.charAt(n-chars)==(char)number)break;//последний добавленный символ
					password+=(char)number;
					chars--;
					break;

			}//end of switch
		}//end of while
		password=password.trim();
		return password;
	}//end of generate()
	
}//end of class
