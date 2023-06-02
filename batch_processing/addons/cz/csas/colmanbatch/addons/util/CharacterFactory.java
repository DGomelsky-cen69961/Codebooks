package cz.csas.colmanbatch.addons.util;

public class CharacterFactory {
	public static char getCharForAscii(int ascii){
		return (char) ascii;
	}
	
	public static char[] getCharForAsciiArray(int[] asciiArray){
		if (asciiArray == null) {
			return null;
		}
		char[] result = new char[asciiArray.length];
		for (int i = 0; i < asciiArray.length; i++) {
			result[i] = (char) asciiArray[i];
		}
		return result;
	}

	public static String getStringForAsciiArray(int[] asciiArray){
		if (asciiArray == null) {
			return null;
		}
		return new String(getCharForAsciiArray(asciiArray));
	}
}
