package Views;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Random;

public class InstructionGenerator {
	private final String FILE_NAME = System.getProperty("user.dir") + "/instuctions.txt";
	
	private Reader fileReader;
	private Random randomGenerator;
	private ArrayList<Object[]> data;
	
	public InstructionGenerator(){
		try {
			fileReader = new FileReader(FILE_NAME);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void readFromFile(){
		
	}
	
	private void generateKeySeqs(int num, String instruction){
		
	}
	
	private void addToData(int num, String instruction){
		
	}
	
}
