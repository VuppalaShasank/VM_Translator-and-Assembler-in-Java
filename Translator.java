package VmTranslator;

import java.util.*;
import java.io.*;
 class CodeWriter {
	public BufferedWriter out;
	public String file;
	public int i = 1;

	public CodeWriter(String outFile, boolean bootStrap) {
		if (outFile.contains(".vm")) {
			outFile = outFile.split(".vm")[0];
		}
		// sets file name
		setFileName(outFile);
		outFile = outFile + ".asm";
		try{
			out = new BufferedWriter(new FileWriter(new File(outFile)));
		}
		catch (IOException e) {
			System.out.println(e);
		}
	}

	public void setFileName(String fileName){
		// if a path
		if (fileName.contains("\\")) {
			// to get Pointer/BasicTest.vm as file name
			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
		}
		// if contains .vm at the end
		if (fileName.contains(".vm")) {
			fileName = fileName.split(".vm")[0];
		}
		file = fileName;
	}
	
	public void writeLabel(String label) throws IOException{
			out.write("("+label.toUpperCase()+")");
	}
	
	public void writeIf(String l) throws IOException{
		out.write("@SP\n");
		out.write("M=M-1\n");
		out.write("A=M\n");
        out.write("D=M\n");
        out.write("@" + l + "\n");
        out.write("D;JNE\n");
    }
	
	public void writeGoto(String g) throws IOException{
		out.write("@" + g+"\n");
		out.write("0;JMP\n");	
	}
	public void writeArithmetic(String arth) throws IOException {

	}

	// write the translation of push/pop command
	public void writePushPop(String type, String arg1, String index) throws IOException{
		String seg = "";

		// store the appropriate segment
		// @segment
		if (arg1.equals("argument")) {
			seg = "ARG";
		} 
		else if (arg1.equals("local")){
			seg = "LCL";
		} 
		else if (arg1.equals("this")){
			seg = "THIS";
		}
		else if (arg1.equals("that")){
			seg = "THAT";
		} 
		else if (arg1.equals("temp")){
			seg = "5";
		}
			// Pop command
			// addr = segmentPointer + i
			// SP--
			// *addr = *SP
			if (type.equals("C_POP")) {

				if (arg1.equals("pointer")) {
					if (index.equals("0")){
						out.write("@SP\n");
						out.write("M=M-1\n");
						out.write("A=M\n");
						out.write("D=M\n");
						out.write("@THIS\n");
						out.write("M=D\n");
					} 
					else {
						out.write("@SP\n");
						out.write("M=M-1\n");
						out.write("A=M\n");
						out.write("D=M\n");
						out.write("@THAT\n");
						out.write("M=D\n");
					}
				}
				else if (arg1.equals("static")){
					out.write("@SP\n");
					out.write("M=M-1\n");
					out.write("A=M\n");
					out.write("D=M\n");
					out.write("@" + file + "." + index + "\n");
					out.write("M=D\n");
				} 
				else if (arg1.equals("temp")){
					out.write("@"+index+"\n");
					out.write("D=A\n");
					out.write("@"+seg+"\n");
					out.write("D=D+M\n");
					out.write("@R13\n");
					out.write("M=D\n");
					out.write("@SP\n");
					out.write("M=M-1\n");
					out.write("A=M\n");
					out.write("D=M\n");
					out.write("@R13\n");
					out.write("A=M\n");
					out.write("D=M\n");
				} 
				else if (arg1.equals("that")){
					out.write("@"+index+"\n");
					out.write("D=A\n");
					out.write("@"+seg+"\n");
					out.write("D=D+M\n");
					out.write("@R13\n");
					out.write("M=D\n");
					out.write("@SP\n");
					out.write("M=M-1\n");
					out.write("A=M\n");
					out.write("D=M\n");
					out.write("@R13\n");
					out.write("A=M\n");
					out.write("D=M\n");
				} 
				else if (arg1.equals("this")) {
					out.write("@"+index+"\n");
					out.write("D=A\n");
					out.write("@"+seg+"\n");
					out.write("D=D+M\n");
					out.write("@R13\n");
					out.write("M=D\n");
					out.write("@SP\n");
					out.write("M=M-1\n");
					out.write("A=M\n");
					out.write("D=M\n");
					out.write("@R13\n");
					out.write("A=M\n");
					out.write("D=M\n");
				} 
				else if (arg1.equals("arguement")){
					out.write("@"+index+"\n");
					out.write("D=A\n");
					out.write("@"+seg+"\n");
					out.write("D=D+M\n");
					out.write("@R13\n");
					out.write("M=D\n");
					out.write("@SP\n");
					out.write("M=M-1\n");
					out.write("A=M\n");
					out.write("D=M\n");
					out.write("@R13\n");
					out.write("A=M\n");
					out.write("D=M\n");
				}
				else {
					out.write("@"+index+"\n");
					out.write("D=A\n");
					out.write("@"+seg+"\n");
					out.write("D=D+M\n");
					out.write("@R13\n");
					out.write("M=D\n");
					out.write("@SP\n");
					out.write("M=M-1\n");
					out.write("A=M\n");
					out.write("D=M\n");
					out.write("@R13\n");
					out.write("A=M\n");
					out.write("D=M\n");
				}
			}

			// Push command
			// addr = segmentPointer + i
			// *SP = *addr
			// SP++
			else {
				if (arg1.equals("pointer")){
				    if (index.equals("0")) {
						out.write("@THIS\n");
						out.write("D=M\n");
						out.write("@SP\n");
						out.write("A=M\n");
						out.write("M=D\n");
						out.write("@SP\n");
						out.write("M=M+1\n");
					} else {
						out.write("@THIS\n");
						out.write("D=M\n");
						out.write("@SP\n");
						out.write("A=M\n");
						out.write("M=D\n");
						out.write("@SP\n");
						out.write("M=M+1\n");
					}
				}
				else if (arg1.equals("static")) {
					out.write("@" + file + "." + index + "\n");
					out.write("D=M\n");
					out.write("@SP\n");
					out.write("A=M\n");
					out.write("M=D\n");
					out.write("@SP\n");
					out.write("M=M+1\n");
				} 
				else if (arg1.equals("temp")) {
					out.write("@" + index + "\n");
					out.write("D=A\n");
					out.write("@" + seg + "\n");
					out.write("A=D+M\n");
					out.write("D=M\n");
					out.write("@SP\n");
					out.write("A=M\n");
					out.write("M=D\n");
					out.write("@SP\n");
					out.write("M=M+1\n");
				} 
				else if (arg1.equals("that")){
					out.write("@" + index + "\n");
					out.write("D=A\n");
					out.write("@" + seg + "\n");
					out.write("A=D+M\n");
					out.write("D=M\n");
					out.write("@SP\n");
					out.write("A=M\n");
					out.write("M=D\n");
					out.write("@SP\n");
					out.write("M=M+1\n");
				} 
				else if (arg1.equals("this")){
					out.write("@" + index + "\n");
					out.write("D=A\n");
					out.write("@" + seg + "\n");
					out.write("A=D+M\n");
					out.write("D=M\n");
					out.write("@SP\n");
					out.write("A=M\n");
					out.write("M=D\n");
					out.write("@SP\n");
					out.write("M=M+1\n");
				} 
				else if (arg1.equals("arguement")){
					out.write("@" + index + "\n");
					out.write("D=A\n");
					out.write("@" + seg + "\n");
					out.write("A=D+M\n");
					out.write("D=M\n");
					out.write("@SP\n");
					out.write("A=M\n");
					out.write("M=D\n");
					out.write("@SP\n");
					out.write("M=M+1\n");
				}
				else if (arg1.equals("constant")){
					out.write("@" + index + "\n");
					out.write("D=A\n");
					out.write("@SP\n");
					out.write("A=M\n");
					out.write("M=D\n");
					out.write("@SP\n");
					out.write("M=M+1\n");
				}
				else{
					out.write("@" + index + "\n");
					out.write("D=A\n");
					out.write("@" + seg + "\n");
					out.write("A=D+M\n");
					out.write("D=M\n");
					out.write("@SP\n");
					out.write("A=M\n");
					out.write("M=D\n");
					out.write("@SP\n");
					out.write("M=M+1\n");
				}
			}
		} 
		

	// close the output file
	public void close(){
		try{
			out.close();
		} 
		catch (IOException e){
			System.out.println(e);
		}
	}
}
 class Parser {
	public String command;
	public String type;
	public BufferedReader in;

	// Opens the file and gets ready to parse it
	public Parser(File inFile) {
        try{
            in = new BufferedReader(new FileReader(inFile));
        } 
        catch (IOException e) {
            System.out.println(e);
        }
    }

	public boolean hasMoreCommands(){
		try {
			while ((command = in.readLine()) != null) {
				// skip empty lines
				if (command.isEmpty()) {
					continue;
				}
				//to get what type the command is
				type = command.trim().split(" ")[0];

				//to check for one line and multi line comments and skip past them
				command = command.trim().toLowerCase();

				//Single line comments
				if (command.charAt(0) == '/' && command.charAt(1) == '/'){
					continue;
				}
				//multi-line comments
				else if (command.charAt(0) == '/' && command.charAt(1) == '*') {
					while ((command = in.readLine()) != null) {
						int len = command.length();
						if (command.charAt(len - 1) == '/' && command.charAt(len - 2) == '*'){
							break;
						}
						continue;
					}
					continue;
				}
				//in-line comments
				if (command.contains("//")){
					command = command.split("//")[0].trim();
				}
				return true; //no-errors
			}

			in.close();
		} 
		catch (IOException e) {
			System.out.println(e);
		}
		return false;
	}

	public String advance(){
		return command;
	}
//command identifier{
	public String commandType() { // provide about the type of instruction 
		if (type.equals("push")){
			type = "C_PUSH";
		} 
		else if (type.equals("pop")) {
			type = "C_POP";
		}
		else if (type.equals("label")){
			type="C_LABEL";
		}
		else if(type.equals("if-goto")){
			type="C_IF";
		}
		else if(type.equals("goto")){
			type="C_GOTO";
		}
		else {
			type = "C_ARITHMETIC";
		}
		return type;
	}

	public String arg1() {   //provides info about the memory segment for push and pop
		if (type.equals("C_ARITHMETIC")){
			return command;
		} 
		else if (type.equals("C_PUSH") || type.equals("C_POP")){
			return command.split(" ")[1]; //
		}
		else{
			return command.split(" ")[1];
		}
	}

	public String arg2(){     //provides index for push and pop
		return command.split(" ")[2];
	}
//command identifier }

}

public class Translator {

	public Parser parser;
	public CodeWriter code;
	private String currentFunction = "";

	private void parse(File in) throws IOException {
		// construct a parser with the file
		parser = new Parser(in);

		// iterate through each command
		while (parser.hasMoreCommands()) {
			String ctype = parser.commandType();

			// Arithmetic command
			if (ctype.equals("C_ARITHMETIC")) {
				code.writeArithmetic(parser.arg1());
			}
			// Push/Pop command
			else if (ctype.equals("C_PUSH") || ctype.equals("C_POP")){
				code.writePushPop(ctype, parser.arg1(), parser.arg2());
			} 
			else if (ctype.equals("C_LABEL") || ctype.equals("C_IF")) {
				if (ctype.equals("C_LABEL"))
					code.writeLabel(parser.arg1());
				else if (ctype.equals("C_IF"))
					code.writeIf(parser.arg1());
				else
					code.writeGoto(parser.arg1());
			}
			
			else if(ctype.equals("C_GOTO")) { 
				code.writeGoto(parser.arg1()); 
			}
		}
	}

	public static void main(String[] args) throws IOException {

		File path = new File("C:\Users\swapn\OneDrive\Desktop\Btech 2023 to 2027\BTECH SEMESTER 2\EOC 2\nand2tetris\projects\07\MemoryAccess\BasicTest\BasicTest.vm");

		Translator vmt = new Translator();

		// create CodeWriter
		vmt.code = new CodeWriter(
			"C:\Users\swapn\OneDrive\Desktop\Btech 2023 to 2027\BTECH SEMESTER 2\EOC 2\nand2tetris\projects\07\MemoryAccess\PointerTest\PointerTest.vm",
				false);
		vmt.code.setFileName(path.getPath());
		vmt.parse(path);
		vmt.code.close();
	}
}