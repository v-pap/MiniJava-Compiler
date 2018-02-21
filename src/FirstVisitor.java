import syntaxtree.*;
import visitor.GJDepthFirst;
import java.util.*;
import java.io.*;

public class FirstVisitor extends GJDepthFirst<String, ArrayList<String>>{
   //
   // User-generated visitor methods below
   //

   /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
	*/
	@Override
	public String visit(Goal n, ArrayList<String> argu) throws Exception {
		String _ret=null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> "class"
	  * f1 -> Identifier()
	  * f2 -> "{"
	  * f3 -> "public"
	  * f4 -> "static"
	  * f5 -> "void"
	  * f6 -> "main"
	  * f7 -> "("
	  * f8 -> "String"
	  * f9 -> "["
	  * f10 -> "]"
	  * f11 -> Identifier()
	  * f12 -> ")"
	  * f13 -> "{"
	  * f14 -> ( VarDeclaration() )*
	  * f15 -> ( Statement() )*
	  * f16 -> "}"
	  * f17 -> "}"
	  */
	 @Override
	 public String visit(MainClass n, ArrayList<String> argu) throws Exception {
		String _ret=null;
		n.f0.accept(this, argu);
		String ClassName = n.f1.accept(this, argu);
		MySymbolTable.AddClass(ClassName, null);
		n.f2.accept(this, argu);
		n.f3.accept(this, argu);
		n.f4.accept(this, argu);
		n.f5.accept(this, argu);
		n.f6.accept(this, argu);
		n.f7.accept(this, argu);
		n.f8.accept(this, argu);
		n.f9.accept(this, argu);
		n.f10.accept(this, argu);
		String Argument = n.f11.accept(this, argu);
		MySymbolTable.AddMethodToClass(ClassName, "main", "void");
		MySymbolTable.AddArgumentToMethod(ClassName, "main", Argument, "String []");
		n.f12.accept(this, argu);
		n.f13.accept(this, argu);
		ArrayList<String> myList = new ArrayList<String>();
		myList.add(ClassName);
		myList.add("main");
		n.f14.accept(this, myList);
		n.f15.accept(this, myList);
		n.f16.accept(this, argu);
		n.f17.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> ClassDeclaration()
	  *       | ClassExtendsDeclaration()
	  */
	 @Override
	 public String visit(TypeDeclaration n, ArrayList<String> argu) throws Exception {
		return n.f0.accept(this, argu);
	 }
  
	 /**
	  * f0 -> "class"
	  * f1 -> Identifier()
	  * f2 -> "{"
	  * f3 -> ( VarDeclaration() )*
	  * f4 -> ( MethodDeclaration() )*
	  * f5 -> "}"
	  */
	 @Override
	 public String visit(ClassDeclaration n, ArrayList<String> argu) throws Exception {
		String _ret=null;
		n.f0.accept(this, argu);
		String ClassName = n.f1.accept(this, argu);
		if(MySymbolTable.ClassExists(ClassName))
			throw new Exception("Class: " + ClassName + " has already been declared");
		MySymbolTable.AddClass(ClassName, null);
		n.f2.accept(this, argu);
		ArrayList<String> myList = new ArrayList<String>();
		myList.add(ClassName);
		n.f3.accept(this, myList);
		n.f4.accept(this, myList);
		n.f5.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> "class"
	  * f1 -> Identifier()
	  * f2 -> "extends"
	  * f3 -> Identifier()
	  * f4 -> "{"
	  * f5 -> ( VarDeclaration() )*
	  * f6 -> ( MethodDeclaration() )*
	  * f7 -> "}"
	  */
	 @Override
	 public String visit(ClassExtendsDeclaration n, ArrayList<String> argu) throws Exception { 
		String _ret=null;
		n.f0.accept(this, argu);
		String ClassName = n.f1.accept(this, argu);
		if(MySymbolTable.ClassExists(ClassName))
			throw new Exception("Class: " + ClassName + " has already been declared");
		n.f2.accept(this, argu);
		String ParentClass = n.f3.accept(this, argu);
		if(!MySymbolTable.ClassExists(ParentClass))
			throw new Exception("Class: " + ClassName +" Extends Class: " + ParentClass +" that doesn't exist (or hasn't been declared yet)");
		MySymbolTable.AddClass(ClassName, ParentClass);
		n.f4.accept(this, argu);
		ArrayList<String> myList = new ArrayList<String>();
		myList.add(ClassName);
		n.f5.accept(this, myList);
		n.f6.accept(this, myList);
		n.f7.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> Type()
	  * f1 -> Identifier()
	  * f2 -> ";"
	  */
	 @Override
	 public String visit(VarDeclaration n, ArrayList<String> argu) throws Exception {
		String _ret=null;
		String VarType = n.f0.accept(this, argu);
		String VarName = n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		if(argu.size() == 1)
		{
			if(MySymbolTable.VariableExists(argu.get(0), VarName))
				throw new Exception("Variable: " + VarName + " has already been declared");
			MySymbolTable.AddVariableToClass(argu.get(0), VarName, VarType);
		}
		else if(argu.size() == 2)
		{
			if(MySymbolTable.VariableExistsIntoMethod(argu.get(0), argu.get(1), VarName))
				throw new Exception("Variable: " + VarName + " has already been declared");
			if(MySymbolTable.ArgumentExistsIntoMethod(argu.get(0), argu.get(1), VarName))
				throw new Exception("Variable: " + VarName + " has already been declared as an argument");
			MySymbolTable.AddVariableToMethod(argu.get(0), argu.get(1), VarName, VarType);
		}
		else
			throw new Exception("Error on VarDeclaration");
		return _ret;
	 }
  
	 /**
	  * f0 -> "public"
	  * f1 -> Type()
	  * f2 -> Identifier()
	  * f3 -> "("
	  * f4 -> ( FormalParameterList() )?
	  * f5 -> ")"
	  * f6 -> "{"
	  * f7 -> ( VarDeclaration() )*
	  * f8 -> ( Statement() )*
	  * f9 -> "return"
	  * f10 -> Expression()
	  * f11 -> ";"
	  * f12 -> "}"
	  */
	 @Override
	 public String visit(MethodDeclaration n, ArrayList<String> argu) throws Exception {
		String _ret=null;
		n.f0.accept(this, argu);
		String ReturnType = n.f1.accept(this, argu);
		String MethodName = n.f2.accept(this, argu);
		if(MySymbolTable.MethodExists(argu.get(0), MethodName))
			throw new Exception("Method: " + MethodName + " has already been declared");
		MySymbolTable.AddMethodToClass(argu.get(0), MethodName, ReturnType);
		ArrayList<String> myList = new ArrayList<String>();
		myList.add(argu.get(0));
		myList.add(MethodName);
		n.f3.accept(this, argu);
		n.f4.accept(this, myList);
		n.f5.accept(this, argu);
		n.f6.accept(this, argu);
		n.f7.accept(this, myList);
		n.f8.accept(this, argu);
		n.f9.accept(this, argu);
		n.f10.accept(this, argu);
		n.f11.accept(this, argu);
		n.f12.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> FormalParameter()
	  * f1 -> FormalParameterTail()
	  */
	 @Override
	 public String visit(FormalParameterList n, ArrayList<String> argu) throws Exception {
		String _ret=null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> Type()
	  * f1 -> Identifier()
	  */
	 @Override
	 public String visit(FormalParameter n, ArrayList<String> argu) throws Exception {
		String _ret=null;
		String Type = n.f0.accept(this, argu);
		String Argument = n.f1.accept(this, argu);
		if(MySymbolTable.ArgumentExistsIntoMethod(argu.get(0), argu.get(1), Argument))
			throw new Exception("Argument: " + Argument + " has already been declared");
		MySymbolTable.AddArgumentToMethod(argu.get(0), argu.get(1), Argument, Type);
		return _ret;
	 }
  
	 /**
	  * f0 -> ( FormalParameterTerm() )*
	  */
	 @Override
	 public String visit(FormalParameterTail n, ArrayList<String> argu) throws Exception {
		return n.f0.accept(this, argu);
	 }
  
	 /**
	  * f0 -> ","
	  * f1 -> FormalParameter()
	  */
	 @Override
	 public String visit(FormalParameterTerm n, ArrayList<String> argu) throws Exception {
		String _ret=null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> ArrayType()
	  *       | BooleanType()
	  *       | IntegerType()
	  *       | Identifier()
	  */
	 @Override
	 public String visit(Type n, ArrayList<String> argu) throws Exception {
		return n.f0.accept(this, argu);
	 }
  
	 /**
	  * f0 -> "int"
	  * f1 -> "["
	  * f2 -> "]"
	  */
	 @Override
	 public String visit(ArrayType n, ArrayList<String> argu) throws Exception {
		String _ret="int []";
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> "boolean"
	  */
	 @Override
	 public String visit(BooleanType n, ArrayList<String> argu) throws Exception {
		n.f0.accept(this, argu);
		return "boolean";
	 }
  
	 /**
	  * f0 -> "int"
	  */
	 @Override
	 public String visit(IntegerType n, ArrayList<String> argu) throws Exception {
		n.f0.accept(this, argu);
		return "int";
	 }
  
	 /**
	  * f0 -> Block()
	  *       | AssignmentStatement()
	  *       | ArrayAssignmentStatement()
	  *       | IfStatement()
	  *       | WhileStatement()
	  *       | PrintStatement()
	  *
	 public R visit(Statement n, A argu) {
		return n.f0.accept(this, argu);
	 }
  
	 /**
	  * f0 -> "{"
	  * f1 -> ( Statement() )*
	  * f2 -> "}"
	  *
	 public R visit(Block n, A argu) {
		R _ret=null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> Identifier()
	  * f1 -> "="
	  * f2 -> Expression()
	  * f3 -> ";"
	  *
	 public R visit(AssignmentStatement n, A argu) {
		R _ret=null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		n.f3.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> Identifier()
	  * f1 -> "["
	  * f2 -> Expression()
	  * f3 -> "]"
	  * f4 -> "="
	  * f5 -> Expression()
	  * f6 -> ";"
	  *
	 public R visit(ArrayAssignmentStatement n, A argu) {
		R _ret=null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		n.f3.accept(this, argu);
		n.f4.accept(this, argu);
		n.f5.accept(this, argu);
		n.f6.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> "if"
	  * f1 -> "("
	  * f2 -> Expression()
	  * f3 -> ")"
	  * f4 -> Statement()
	  * f5 -> "else"
	  * f6 -> Statement()
	  *
	 public R visit(IfStatement n, A argu) {
		R _ret=null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		n.f3.accept(this, argu);
		n.f4.accept(this, argu);
		n.f5.accept(this, argu);
		n.f6.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> "while"
	  * f1 -> "("
	  * f2 -> Expression()
	  * f3 -> ")"
	  * f4 -> Statement()
	  *
	 public R visit(WhileStatement n, A argu) {
		R _ret=null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		n.f3.accept(this, argu);
		n.f4.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> "System.out.println"
	  * f1 -> "("
	  * f2 -> Expression()
	  * f3 -> ")"
	  * f4 -> ";"
	  *
	 public R visit(PrintStatement n, A argu) {
		R _ret=null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		n.f3.accept(this, argu);
		n.f4.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> AndExpression()
	  *       | CompareExpression()
	  *       | PlusExpression()
	  *       | MinusExpression()
	  *       | TimesExpression()
	  *       | ArrayLookup()
	  *       | ArrayLength()
	  *       | MessageSend()
	  *       | Clause()
	  *
	 public R visit(Expression n, A argu) {
		return n.f0.accept(this, argu);
	 }
  
	 /**
	  * f0 -> Clause()
	  * f1 -> "&&"
	  * f2 -> Clause()
	  *
	 public R visit(AndExpression n, A argu) {
		R _ret=null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> PrimaryExpression()
	  * f1 -> "<"
	  * f2 -> PrimaryExpression()
	  *
	 public R visit(CompareExpression n, A argu) {
		R _ret=null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> PrimaryExpression()
	  * f1 -> "+"
	  * f2 -> PrimaryExpression()
	  *
	 public R visit(PlusExpression n, A argu) {
		R _ret=null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> PrimaryExpression()
	  * f1 -> "-"
	  * f2 -> PrimaryExpression()
	  *
	 public R visit(MinusExpression n, A argu) {
		R _ret=null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> PrimaryExpression()
	  * f1 -> "*"
	  * f2 -> PrimaryExpression()
	  *
	 public R visit(TimesExpression n, A argu) {
		R _ret=null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> PrimaryExpression()
	  * f1 -> "["
	  * f2 -> PrimaryExpression()
	  * f3 -> "]"
	  *
	 public R visit(ArrayLookup n, A argu) {
		R _ret=null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		n.f3.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> PrimaryExpression()
	  * f1 -> "."
	  * f2 -> "length"
	  *
	 public R visit(ArrayLength n, A argu) {
		R _ret=null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> PrimaryExpression()
	  * f1 -> "."
	  * f2 -> Identifier()
	  * f3 -> "("
	  * f4 -> ( ExpressionList() )?
	  * f5 -> ")"
	  *
	 public R visit(MessageSend n, A argu) {
		R _ret=null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		n.f3.accept(this, argu);
		n.f4.accept(this, argu);
		n.f5.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> Expression()
	  * f1 -> ExpressionTail()
	  *
	 public R visit(ExpressionList n, A argu) {
		R _ret=null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> ( ExpressionTerm() )*
	  *
	 public R visit(ExpressionTail n, A argu) {
		return n.f0.accept(this, argu);
	 }
  
	 /**
	  * f0 -> ","
	  * f1 -> Expression()
	  *
	 public R visit(ExpressionTerm n, A argu) {
		R _ret=null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> NotExpression()
	  *       | PrimaryExpression()
	  *
	 public R visit(Clause n, A argu) {
		return n.f0.accept(this, argu);
	 }
  
	 /**
	  * f0 -> IntegerLiteral()
	  *       | TrueLiteral()
	  *       | FalseLiteral()
	  *       | Identifier()
	  *       | ThisExpression()
	  *       | ArrayAllocationExpression()
	  *       | AllocationExpression()
	  *       | BracketExpression()
	  *
	 public R visit(PrimaryExpression n, A argu) {
		return n.f0.accept(this, argu);
	 }
  
	 /**
	  * f0 -> <INTEGER_LITERAL>
	  *
	 public R visit(IntegerLiteral n, A argu) {
		return n.f0.accept(this, argu);
	 }
  
	 /**
	  * f0 -> "true"
	  *
	 public R visit(TrueLiteral n, A argu) {
		return n.f0.accept(this, argu);
	 }
  
	 /**
	  * f0 -> "false"
	  *
	 public R visit(FalseLiteral n, A argu) {
		return n.f0.accept(this, argu);
	 }
  
	 /**
	  * f0 -> <IDENTIFIER>
	  */
	 @Override
	 public String visit(Identifier n, ArrayList<String> argu) throws Exception {		
		n.f0.accept(this, argu);
		return n.f0.toString();
	 }
  
	 /**
	  * f0 -> "this"
	  *
	 public R visit(ThisExpression n, A argu) {
		return n.f0.accept(this, argu);
	 }
  
	 /**
	  * f0 -> "new"
	  * f1 -> "int"
	  * f2 -> "["
	  * f3 -> Expression()
	  * f4 -> "]"
	  *
	 public R visit(ArrayAllocationExpression n, A argu) {
		R _ret=null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		n.f3.accept(this, argu);
		n.f4.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> "new"
	  * f1 -> Identifier()
	  * f2 -> "("
	  * f3 -> ")"
	  *
	 public R visit(AllocationExpression n, A argu) {
		R _ret=null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		n.f3.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> "!"
	  * f1 -> Clause()
	  *
	 public R visit(NotExpression n, A argu) {
		R _ret=null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		return _ret;
	 }
  
	 /**
	  * f0 -> "("
	  * f1 -> Expression()
	  * f2 -> ")"
	  *
	 public R visit(BracketExpression n, A argu) {
		R _ret=null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		return _ret;
	 }
	 */
}
