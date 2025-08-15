package Assembler;
import java.io.*;
import java.util.HashMap;
import java.util.regex.*;

public class Assembly {

    private String file;
    private static int pc = 0;

    public Assembly(String fin) {
        file = fin;
    }

    public static void main(String[] args) {
        try {
            if (args.length < 2) {
            throw new IllegalArgumentException("Usage: Main <input_file.asm> <output_file.hack>");

            }

            // CREATE INSTANCES OF OTHER MODULES
            Parser fp = new Parser(args[0]);
            Parser sp = new Parser(args[0]);
            Code code = new Code();
            HashMap<String, String> symbolTable = new HashMap<>();

            // SYMBOL TABLE INITIALIZATION
            symbolTable.put("R0", "0");
            symbolTable.put("R1", "1");
            symbolTable.put("R2", "2");
            symbolTable.put("R3", "3");
            symbolTable.put("R4", "4");
            symbolTable.put("R5", "5");
            symbolTable.put("R6", "6");
            symbolTable.put("R7", "7");
            symbolTable.put("R8", "8");
            symbolTable.put("R9", "9");
            symbolTable.put("R10", "10");
            symbolTable.put("R11", "11");
            symbolTable.put("R12", "12");
            symbolTable.put("R13", "13");
            symbolTable.put("R14", "14");
            symbolTable.put("R15", "15");
            symbolTable.put("SP", "0");
            symbolTable.put("LCL", "1");
            symbolTable.put("ARG", "2");
            symbolTable.put("THIS", "3");
            symbolTable.put("THAT", "4");

            // FIRST PASS
            fp.advance();
            while (fp.command != null) {
                if (fp.commandType().equals("L_COMMAND")) {
                    symbolTable.put(fp.symbol(), Integer.toString(pc));
                    pc--;
                }
                fp.advance();
                pc++;
            }

            // SECOND PASS
            FileWriter writer = null;
            int rAllocation = 16; // Keeps a record of the last register allocated to a variable.

            try {
                // CREATE FILE, FILE WRITER
                File nf = new File(args[1].replaceAll("\\.asm", ".hack"));
                nf.createNewFile();
                writer = new FileWriter(nf);

                // SECOND PASS
                sp.advance();
                while (sp.command != null) {
                    if (sp.commandType().equals("L_COMMAND")) {
                        // Do nothing.
                    } else if (sp.commandType().equals("A_COMMAND")) {
                        if (Pattern.compile("[a-zA-Z]").matcher(sp.symbol()).find()) {
                            if (symbolTable.get(sp.symbol()) == null) {
                                symbolTable.put(sp.symbol(), Integer.toString(rAllocation));
                                rAllocation++;
                            }
                            writer.write(convertAddr(symbolTable.get(sp.symbol())) + "\n");
                        } else {
                            writer.write(convertAddr(sp.symbol()) + "\n");
                        }
                    } else if (sp.commandType().equals("C_COMMAND")) {
                        String d = code.dest(sp.dest());
                        String c = code.comp(sp.comp());
                        String j = code.jump(sp.jump());
                    //writes the combined c instruction
                        writer.write("111" + c + d + j + "\n");
                    }
                    sp.advance();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // CLOSE WRITER
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 // a instruction converter
    private static String convertAddr(String addr) throws Exception {
        if (addr != null) {
            String bin = Integer.toBinaryString(Integer.parseInt(addr));
            return "0".repeat(16 - bin.length()) + bin;
        } else {
            throw new Exception("Null Parameter.");
        }
    }
}

class Code {
    // Create HashMaps
    HashMap<String, String> cMap = new HashMap<>();
    HashMap<String, String> dMap = new HashMap<>();
    HashMap<String, String> jMap = new HashMap<>();

    public Code() {
        cMap.put("0","0101010");
        cMap.put("1","0111111");
        cMap.put("-1","0111010");
        cMap.put("A","0110000");
        cMap.put("!D","0001101");
        cMap.put("!A","0001111");
        cMap.put("-D","0001111");
        cMap.put("-A","0110011");
        cMap.put("D+1","0011111");
        cMap.put("A+1","0110111");
        cMap.put("D-1","0001110");
        cMap.put("A-1","0110010");
        cMap.put("D+A","0000010");
        cMap.put("D-A","0010011");
        cMap.put("A-D","0000111");
        cMap.put("D&A","0000000");
        cMap.put("D|A","0010101");
        cMap.put("M","1110000");
        cMap.put("!M","1110001");
        cMap.put("-M","1110011");
        cMap.put("M+1","1110111");
        cMap.put("M-1","1110010");
        cMap.put("D+M","1000010");
        cMap.put("D-M","1010011");
        cMap.put("M-D","1000111");
        cMap.put("D&M","1000000");
        cMap.put("D|M","1010101");

        dMap.put("M","000");
        dMap.put("D","010");
        dMap.put("DM","011");
        dMap.put("A","100");
        dMap.put("AM","101");
        dMap.put("AD","110");
        dMap.put("ADM","111");

        jMap.put("JGT","001");
        jMap.put("JEQ","010");
        jMap.put("JGE","011");
        jMap.put("JLT","100");
        jMap.put("JNE","101");
        jMap.put("JLE","110");
        jMap.put("JMP","111");

    }
   //convertiing c instruction to binary
    public String dest(String d) {
        return dMap.getOrDefault(d, "000");
    }

    public String comp(String c) {
        return cMap.getOrDefault(c, "0000000");
    }

    public String jump(String j) {
        return jMap.getOrDefault(j, "000");
    }
}

class Parser {
    private BufferedReader br;
    String command;

    public Parser(String file) {
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void advance() throws IOException {
        String line;
        while (true) {
            line = br.readLine();
            if (line == null) {
                try {
                    if (br != null) {
                        br.close();
                        command = null;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } 
            }
            line = line.replaceAll("\\s", "").replaceAll("//.*", "");
            if (line.length() != 0) {
                command = line;
                break;
            }
        }
    }
   //Instruction identifier
    public String commandType() {
        if (command.charAt(0) == '(') {
            return "L_COMMAND";
        } else if (command.charAt(0) == '@') {
            return "A_COMMAND";
        } else {
            return "C_COMMAND";
        }
    }
    // a instruction identifier
    public String symbol() {
        if (command.indexOf("@") == -1) {
            return command.replaceAll("\\(", "").replaceAll("\\)", "");
        } else {
            return command.replaceAll("@", "");
        }
    }
    // c instruction parts identifier
    public String comp() {
        return command.replaceAll(".=", "").replaceAll(";.", "");
    }

    public String dest() {
        if (command.indexOf("=") == -1) {
            return "";
        } else {
            return command.replaceAll("=.*", "");
        }
    }

    public String jump() {
        if (command.indexOf(";") == -1) {
            return "";
        } else {
            return command.replaceAll(".*;", "");
        }
    }
}