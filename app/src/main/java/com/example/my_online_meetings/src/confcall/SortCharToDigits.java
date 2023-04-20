/**
 * 
 */
/**
 * @ author: Peter Albertsson | Team mobile solutions | Capgemini Sverige AB
 * @ email: peter.albertsson@capgemini.com 
 * @ Date: 12 mar 2015
 * 
 * class-name: SortCharToDigits.java
 * Project:  
 * Conference call
 * 
 * Description of the java-class:
 * This java-class will sort out all characters and return a string of digits.
 *
 * TODO: 
 * Nothing to do, the java-class is ready
 * 
 * 
 */
package com.example.my_online_meetings.src.confcall;
/**
 * @author pealbert
 *
 */
public class SortCharToDigits {
	
	// For the sort method
	private String[] subStr;

	public SortCharToDigits() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * IN USED MODE in the App
	 * The Method will sort out all characters and return a string with digits
	 * */
	// Metoden sorterar bort tecken och visar siffror
	public String sortCharToDigits(String s) {

		String sString = s;

		// Get the string in to a stringbuffer
		StringBuffer bTecken = new StringBuffer(sString);

		// Count the hole string who will be sorted out of any characters.
		for (int i = 0; i < bTecken.length(); i++) {

			char tecken = bTecken.charAt(i); 

			/**
			 * The character includes following languages. German, Dutch,
			 * Swedish, Enlish, Norwegian, Danish. Spanish, Italy, Finish,
			 * France
			 */

			if ('A' <= tecken && tecken <= 'Z' || 'a' <= tecken
					&& tecken <= 'z' || tecken == '-' || tecken == '/'
					|| tecken == '\\' || tecken == '?' || tecken == '%'
					|| tecken == ' ' || tecken == ':' || tecken == ';'
					|| tecken == '.' || tecken == ',' || tecken == '_'
					|| tecken == '!' || tecken == '*' || tecken == '|'
					|| tecken == '<' || tecken == '>' || tecken == '+'
					|| tecken == '(' || tecken == '"' || tecken == '~'
					|| tecken == ')' || tecken == '�' || tecken == '�'
					|| tecken == '�' || tecken == '�' || tecken == '�'
					|| tecken == '�' || tecken == '�' || tecken == '�'
					|| tecken == '�' || tecken == '�' || tecken == '�'
					|| tecken == '�' || tecken == '�' || tecken == '�'
					|| tecken == '�' || tecken == '�' || tecken == '�'
					|| tecken == '�' || tecken == '�' || tecken == '�'
					|| tecken == '�' || tecken == '�' || tecken == '�'
					|| tecken == '�' || tecken == '�' || tecken == '�'
					|| tecken == '�' || tecken == '�' || tecken == '�'
					|| tecken == '�' || tecken == '�') {

				// Get blanc ' ' into the string
				bTecken.setCharAt(i, ' ');
			}

		}

		// Add blanc ' ' into the last index int the string
		bTecken.append(' ');

		// Parse char-string to string
		String setString = new String(bTecken);

		// Add the separator ' ' to sort
		int antal = 0;
		char separator = ' ';

		int index = 0;

		do { // Do-statement will sort out every blanc step ' ' 
			
			++antal;
			++index;

			index = setString.indexOf(separator, index);
		} while (index != -1);

		subStr = new String[antal];
		index = 0;
		int slutindex = 0;

		for (int j = 0; j < antal; j++) {

			slutindex = setString.indexOf(separator, index);

			if (slutindex == -1) {
				subStr[j] = setString.substring(index);
			}

			else {
				subStr[j] = setString.substring(index, slutindex);
			}

			index = slutindex + 1;

		}
		String setNumber = "";
		for (int k = 0; k < subStr.length; k++) {

			setNumber += subStr[k]; 
		}

		String sortedString = setNumber;

		return sortedString;
	}

}
