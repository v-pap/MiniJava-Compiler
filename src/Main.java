import syntaxtree.*;
import visitor.*;
import java.io.*;
import java.util.*;

public class Main {
	public static MySymbolTable SymbolTable;
	public static void emit(String line)
	{
		 System.out.println(line);
	}
    public static void main (String [] args){
		if(args.length < 1){
			System.err.println("Usage: java Driver <inputFile1> <inputFile2> ... <inputFileN>");
			System.exit(1);
		}
		FileInputStream fis = null;
		for(String arg: args) {
			try{
				String output = arg.replace(arg.substring(arg.length()-5), "");
				PrintStream out = new PrintStream(new FileOutputStream(output + ".ll"));
				System.setOut(out);
				fis = new FileInputStream(arg);
				SymbolTable = new MySymbolTable();
				MiniJavaParser parser = new MiniJavaParser(fis);
				FirstVisitor first = new FirstVisitor();
				SecondVisitor second = new SecondVisitor();
				ThirdVisitor third = new ThirdVisitor();
				Goal root = parser.Goal();
				try{
					root.accept(first, null);
					root.accept(second, null);
					MySymbolTable.Vtable();
					emit("");
					emit("");
					emit("declare i8* @calloc(i32, i32)");
					emit("declare i32 @printf(i8*, ...)");
					emit("declare void @exit(i32)");
					emit("");
					emit("@_cint = constant [4 x i8] c\"%d\\0a\\00\"");
					emit("@_cOOB = constant [15 x i8] c\"Out of bounds\\0a\\00\"");
					emit("define void @print_int(i32 %i) {");
					emit("\t%_str = bitcast [4 x i8]* @_cint to i8*");
					emit("\tcall i32 (i8*, ...) @printf(i8* %_str, i32 %i)");
					emit("\tret void");
					emit("}");
					emit("");
					emit("define void @throw_oob() {");
					emit("\t%_str = bitcast [15 x i8]* @_cOOB to i8*");
					emit("\tcall i32 (i8*, ...) @printf(i8* %_str)");
					emit("\tcall void @exit(i32 1)");
					emit("\tret void");
					emit("}");
					emit("");
					root.accept(third, null);
					System.err.println(arg + " parsed & compiled successfully.");
					//MySymbolTable.PrintOffset();
				}
				catch(Exception ex){
					System.err.println(arg + " Semantic Error: " + ex.getMessage());
					ex.printStackTrace();
				}
			}
			catch(ParseException ex){
				System.out.println(ex.getMessage());
			}
			catch(FileNotFoundException ex){
				System.err.println(ex.getMessage());
			}
		}
		try{
			if(fis != null) fis.close();
		}
		catch(IOException ex){
			System.err.println(ex.getMessage());
		}
    }
}
