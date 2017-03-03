package Views;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Random;

public class InstructionGenerator {
	private final String FILE_NAME = System.getProperty("user.dir") + "/instuctions.txt";
	
	private Reader fileReader;
	private Random randomGenerator = new Random();
	private ArrayList<Object[]> data;
	
	public InstructionGenerator() throws FileNotFoundException{
			fileReader = new FileReader(FILE_NAME);
	}
	
	private void readFromFile(){
		
	}
	
	private void generateKeySeqs(int num, String instruction){
		
	}
	
	private void addToData(int num, String instruction){
		
	}
	
	public Object[][] getData(){
		Object[][] o = new Object[data.size()][ManualInstructionsView.columnNames.length];
		for(int i = 0; i<data.size();i++){
			o[i] = data.get(i);
		}
		return o;
	}
	
}
