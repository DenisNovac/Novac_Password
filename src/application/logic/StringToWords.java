package application.logic;

import java.util.ArrayList;

public class StringToWords {
	private ArrayList<String> answer;
	private int iterator;
	private String string;
	private int space=-1; //начальное значение должно быть -1, т.к. при первом проходе будет +1

	//принимает количество нужных отдельных слов и строку, которую необходимо обработать
	protected StringToWords(int i, String s){ 
		answer=new ArrayList<>();
		iterator = i;
		string = s;
	}

	protected ArrayList<String> parse(){ //метод необходим, чтобы отдать нам массив ответа, но рекурсию из него сделать нельзя - нужен void
		recurse();
		return answer;
	}

	private void recurse(){//void рекурсия, ничего не отдаёт и заменяет глобальные переменные
		while (iterator>=0){
			//System.out.println("Recursing "+iterator); //для отладки
			string = string.substring(space+1, string.length());
			string = string.trim();//срезаем пробелы
			//итератор равен 0 когда мы уже достали нужное колиечество слов,
			//но нужно в последний раз обработать строку и срезать последнее слово
			if (iterator==0){
				answer.add(string);
				iterator--;
				return;
			}
			space = string.indexOf('|'); //использую разделитель |
			String oneWord = string.substring(0,space);
			//System.out.println("One word:["+oneWord+"]"); //для отладки
			answer.add(oneWord);
			iterator--;
			recurse();
		}
	}
}
