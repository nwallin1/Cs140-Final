package project;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FullAssembler implements Assembler {
	private boolean readingCode = true;
	private String makeOutputCode(String[] parts) {
		if(parts.length == 1) {
			return InstrMap.toCode.get(parts[0]) + "\n" + 0;
		}
		return InstrMap.toCode.get(parts[0]) + "\n" + Integer.parseInt(parts[1], 16);
	}
	private String makeOutputData(String[] parts) {
		return Integer.parseInt(parts[0],16) + "\n" + 
			 Integer.parseInt(parts[1],16);
	}
	
	@Override
	public int assemble(String inputFileName, String outputFileName, StringBuilder error) {
		
		ArrayList<String> codeLines = new ArrayList<>(); //stores code lines
		
		ArrayList<String> dataLines = new ArrayList<>(); //stores data lines
		
		int lineNumber = 0; //instantiate to keep track of line number
		
		boolean foundBlank = false; // keeps track of if a blank line has been found
		
		int blankLine = 0; //instantiate in case a blank line is found to keep track of where it is
		
		int retVal = 0; //return value of the function , returns 0 if works, -1 if not
		
		File file = new File(inputFileName);
		try(Scanner inp = new Scanner(new FileReader(inputFileName))){
			//if error is null throws exception
			if(error == null) {
				throw new IllegalArgumentException("Error buffer is null");
			}
			//run through the file line by line
			while(inp.hasNext()) {
				String line = inp.nextLine();
	
				lineNumber += 1; //keeps track of what line we are on
				
				// checks for illegal whitespace
				if(line.startsWith(" ") || line.startsWith("\t")) {
					error.append("\nError At line: " + lineNumber + "\nLine has illegal whitespace" );
				}
				//checks where data starts, sets readingCode to false 
				if(line.trim().toUpperCase().equals("DATA")) { 

					//sees if DATA is capitalized
					if(!line.trim().equals("DATA")) {
						error.append("\nError At Line: " + lineNumber + "\nDATA is not capitalized");
					}
					
					//sees if there is two instances of DATA lines
					if(readingCode == false) {
						error.append("\nError At Line: " + lineNumber + "\nShouldnt be a second DATA seperator");
					}
					
					//sets readingCode to False after checks
					readingCode = false;
				}
					
				//checks for illegal blank line if one blank line has already been found
				if(line.trim().length() != 0 && foundBlank == true &&
						error.lastIndexOf("\nError At line:" + blankLine + "\nIllegal blank line in the source file") == -1) {
					error.append("\nError At line:" 
				+ blankLine + "\nIllegal blank line in the source file");
					foundBlank = false;
				}
				
				//checks for first instance of a blank line, saves line number to blankLine
				if(line.trim().length() == 0 && foundBlank == false) { foundBlank = true; blankLine = lineNumber;}
				
				//adds the lines that are code to the code Arraylists
				if(readingCode == true) { codeLines.add(line);}
				
				//adds lines that are data to the data Arraylist
				else {dataLines.add(line);}
				
			} // end while loop
			
		} catch(IllegalArgumentException e) { //catches the error from error == null
			System.out.println(e + "\n" + e.getMessage());
		} catch(FileNotFoundException e) {
			System.out.println("File " + inputFileName + "is not found");
		}
		
		lineNumber = 0; //resets lineNumber
		
		//iterate through the code lines
		for(String line: codeLines) {
			
			lineNumber += 1; //increment lineNumber
			//splits code instructions into two parts  EX: "J , 9"
			String[] parts = line.trim().split("\\s+");
			
			//checks if the instruction is in the instruction map
			if(InstrMap.toCode.keySet().contains(parts[0]) == true) {
				
				//checks if instruction is capitalized
				if(parts[0].toUpperCase() != parts[0]) {
					error.append("\nError At Line: " + lineNumber + "\nThe mnemonic of the instructions is incorrect");
				}
				
				//if parts[0] is in noArgument, it should only have length of 1
				if(Assembler.noArgument.contains(parts[0]) == true && parts.length != 1) {
					error.append("\nError At Line: " + lineNumber + "\nThe instruction should not have an argument");
				}
				
				//if parts[0] is not in noArgument it should have length 2
				if(Assembler.noArgument.contains(parts[0]) == false && parts.length != 2) {
					error.append("\nError At Line: " + lineNumber + "\nThis instruction must have one and only one argument");
				}
				

				if(Assembler.noArgument.contains(parts[0]) == false && parts.length == 2) {
					try{
					//... all the code to compute the correct flags
						int arg = Integer.parseInt(parts[1],16);
					//.. the rest of setting up the opPart
					} catch(NumberFormatException e) {
						error.append("\nError on line " + lineNumber + 
								": argument is not a hex number");				
					}
				}
				
			//if it isn't in the map, its an error
			}	else{
					//checks if instruction is capitalized
					if(parts[0].toUpperCase() != parts[0]) {
						error.append("\nError At Line: " + lineNumber + "\nThe mnemonic of the instructions is incorrect");
					}
					if(line.trim().length() != 0 && parts[0].toUpperCase() == parts[0]) {
						error.append("\nError At Line: " + lineNumber + "\nThe instruction doesn't exist in the set");
					}
				}
			
		} //end code line for loop

		//loop through data lines
		if(dataLines.size() > 0) {
			dataLines.remove(0); 
			lineNumber++;// get rid of DATA;
		}
		 for(String line: dataLines) {
			 lineNumber++;
			 String[] parts = line.trim().split("\\s+");
			 
			 if(parts.length == 2) {
				 try {
					 int address = Integer.parseInt(parts[0],16); 
					 int value = Integer.parseInt(parts[1],16);
				 } catch(NumberFormatException e) {
					 error.append("\nError on line " + lineNumber + 
							 ": data has non-numeric memory address");
				 }
			 } else { error.append("\nError on Line: " + lineNumber + "Data line doesnt have 2 parts");}
		 } //end data lines for loop
		 
		 
		 //check to see if we have any errors
		 // if errors exist, exits function with failure
		 if(error.length() > 0) {
			 System.out.println(error);
			 return -1;
		 }
		
		readingCode = true; //resets value
		Map<Boolean, List<String>> lists = null;
		try (Stream<String> lines = Files.lines(Paths.get(inputFileName))) {
			lists = lines
				.filter(line -> line.trim().length() > 0) // << CORRECTION <<
				.map(line -> line.trim())
				.peek(line -> {if(line.toUpperCase().equals("DATA")) readingCode = false;})
				.map(line -> line.trim())
				.collect(Collectors.partitioningBy(line -> readingCode));
//				System.out.println("true List " + lists.get(true)); // these lines can be uncommented 
//				System.out.println("false List " + lists.get(false)); // for checking the code
		} catch (IOException e) {
			e.printStackTrace();
		}
		lists.get(false).remove("DATA");
		List<String> outputCode = lists.get(true).stream()
				.map(line -> line.split("\\s+"))
				.map(this::makeOutputCode) // note how we use an instance method in the same class
				.collect(Collectors.toList());
		
		List<String> outputData = lists.get(false).stream()
				.map(line -> line.split("\\s+"))
				.map(this::makeOutputData)
				.collect(Collectors.toList());
		try (PrintWriter output = new PrintWriter(outputFileName)){
			for(String s : outputCode) output.println(s);
			output.println(-1); // signal for the "DATA" separating code and data
			output.println(0); // filler for the 2-line pattern
			for(String s : outputData) output.println(s);
		} catch (FileNotFoundException e) {
			error.append("\nError: Unable to write the assembled program to the output file");
			retVal = -1;
		} catch (IOException e) {
			error.append("\nUnexplained IO Exception");
			retVal = -1;
		}
		return retVal;
		}
	public static void main(String[] args) {
		StringBuilder error = new StringBuilder();
		System.out.println("Enter the name of the file without extension: ");
		try (Scanner keyboard = new Scanner(System.in)) { 
			String filename = keyboard.nextLine();
			int i = new FullAssembler().assemble(filename + ".pasm", 
					filename + ".pexe", error);
			System.out.println("result = " + i);
		}
	}
}
