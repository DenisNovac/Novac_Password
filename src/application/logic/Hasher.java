package application.logic;

import java.math.BigInteger;
import java.security.MessageDigest;

public abstract class Hasher {
	final private static int RADIX=16; //основание системы - всегда константа
	
	private static String ALGORITHM;
	private static MessageDigest MD;
	private static int DIGLENGTH; //16 md5 32 sha-256 64 sha-512
	
	public static void setAlgorithm(String alg){ //метод вызывается из Main и устанавливает алгоритм хеширования для программы
		ALGORITHM=alg;
		try {
			MD = MessageDigest.getInstance(ALGORITHM); 
		}catch(Exception e){System.out.println(e);}
		DIGLENGTH=MD.getDigestLength();
		
		FileWorker.setDigLength(DIGLENGTH); //отдаёт длину хеша классу FileWorker, которому она необходима
	}
	
	protected static String toHash(String s){ //метод ввода в хеш
		//alg tested: md5 SHA-256 SHA-512
		
		byte[] bytes = s.getBytes();
		bytes = MD.digest(bytes); //хешируем
		BigInteger bigInt = new BigInteger(1, bytes); 
		s=bigInt.toString(RADIX); //основание системы - КОНСТАНТА

		while( s.length() < DIGLENGTH*2 ){//дописывает нули, если хеш меньше стандартного
        	s = "0" + s;
    	}

    	return s;
	}
}
