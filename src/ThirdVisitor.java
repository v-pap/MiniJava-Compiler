import syntaxtree.*;
import visitor.GJDepthFirst;
import java.util.*;
import java.io.*;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class ThirdVisitor extends GJDepthFirst<ArrayList<String>, ArrayList<String>>{
   //
   // User-generated visitor methods below
   //

   public Stack<ArrayList<String>> ParameterStack = new Stack<>();

   public Integer NextVar = -1;

   public Integer NumOfTabs = 0;

   public Integer ArrayAlloc = -1;

   public Integer OutOfBounds = -1;

   public Integer IfLabel = -1;

   public Integer LoopLabel = -1;

   public Integer AndLabel = -1;

   public void emit(String line)
   {
       String tabs = "";
       for(int i = 0; i < this.NumOfTabs; i++)
          tabs += "\t";

       System.out.println(tabs + line);
   }

   public void emitNoTabs(String line)
   {
       System.out.println(line);
   }

   public String NextVar()
   {
       this.NextVar++;
       return "%_" + this.NextVar.toString();
   }

   public String NextArray()
   {
       this.ArrayAlloc++;
       return "arr_alloc" + this.ArrayAlloc.toString();
   }

   public String NextOOB()
   {
       this.OutOfBounds++;
       return "oob" + this.OutOfBounds.toString();
   }

   public String NextIfLabel()
   {
       this.IfLabel++;
       return "if" + this.IfLabel.toString();
   }

   public String NextAndLabel()
   {
       this.AndLabel++;
       return "if" + this.AndLabel.toString();
   }

   public String NextLoopLabel()
   {
       this.LoopLabel++;
       return "loop" + this.LoopLabel.toString();
   }

   public String PrevVar()
   {
       Integer PrevVar = (this.NextVar)-1;
       return "%_" + PrevVar.toString();
   }

   public String CurVar()
   {
       return "%_" + this.NextVar.toString();
   }

   public void ResetVar()
   {
       this.NextVar = -1;
   }

   public void AddTabs()
   {
       this.NumOfTabs++;
   }

   public void RemoveTabs()
   {
       this.NumOfTabs--;
   }

   public Boolean isInteger(String str)
   {
       return str.matches("-?\\d+");
   }

   public String PrepareOperand(String Operand, String ClassName, String MethodName, String VarType) throws Exception
   {
       String FinalResult = Operand;
       if(Operand != null && !Operand.startsWith("%") && !isInteger(Operand))
       {
         String Type;
         if(VarType.equals("<id>"))
           Type = MySymbolTable.VariableType(ClassName, MethodName, Operand);
         else
           Type = VarType;
         String DataType = DataTypellvm(Type);
         if(MySymbolTable.VariableExistsIntoMethod(ClassName, MethodName, Operand))
         {  
            emit(NextVar() + " = load " + DataType + ", " + DataType + "* %" + Operand);
            FinalResult = CurVar();
         }
         else if(MySymbolTable.ArgumentExistsIntoMethod(ClassName, MethodName, Operand))
         {
            emit(NextVar() + " = load " + DataType + ", " + DataType + "* %" + Operand);
            FinalResult = CurVar();
         }
         else if(Operand.equals("this"))
         {
           FinalResult = "%" + Operand;
         }
         else
         {
            emit(NextVar() + " = getelementptr i8, i8* %this, i32 " + (MySymbolTable.GetVariableOffset(ClassName, Operand) + 8));
            emit(NextVar() + " = bitcast i8* " + PrevVar() + " to " + DataType + "*");
            emit(NextVar() + " = load " + DataType + ", " + DataType + "* " + PrevVar());
            FinalResult = CurVar();
         }
       }
       return FinalResult;
   }

   public String PrepareOperandAssignment(String Operand, String ClassName, String MethodName, String VarType) throws Exception
   {
       String FinalResult = Operand;
       if(Operand != null && !Operand.startsWith("%") && !isInteger(Operand))
       {
         String Type;
         if(VarType.equals("<id>"))
           Type = MySymbolTable.VariableType(ClassName, MethodName, Operand);
         else
           Type = VarType;
         String DataType = DataTypellvm(Type);
         if(MySymbolTable.VariableExistsIntoMethod(ClassName, MethodName, Operand))
         {  
            FinalResult = "%" + Operand;
         }
         else if(MySymbolTable.ArgumentExistsIntoMethod(ClassName, MethodName, Operand))
         {
            FinalResult = "%" + Operand;
         }
         else if(Operand.equals("this"))
         {
           FinalResult = "%" + Operand;
         }
         else
         {
            emit(NextVar() + " = getelementptr i8, i8* %this, i32 " + (MySymbolTable.GetVariableOffset(ClassName, Operand) + 8));
            emit(NextVar() + " = bitcast i8* " + PrevVar() + " to " + DataType + "*");
            FinalResult = CurVar();
         }
       }
       return FinalResult;
   }

   public String DataTypellvm(String DataType)
   {
       if(DataType.equals("int"))
          return "i32";
       else if(DataType.equals("boolean"))
          return "i1";
       else if(DataType.equals("int []"))
          return "i32*";
       else
          return "i8*";
   }

   /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
   public ArrayList<String> visit(Goal n, ArrayList<String> argu) throws Exception {
      ArrayList<String> _ret=null;
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
   public ArrayList<String> visit(MainClass n, ArrayList<String> argu) throws Exception {
      ArrayList<String> _ret=null;
      n.f0.accept(this, argu);
      String ClassName = n.f1.accept(this, argu).get(0);
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      n.f5.accept(this, argu);
      n.f6.accept(this, argu);
      n.f7.accept(this, argu);
      n.f8.accept(this, argu);
      n.f9.accept(this, argu);
      n.f10.accept(this, argu);
      n.f11.accept(this, argu);
      n.f12.accept(this, argu);
      n.f13.accept(this, argu);
      ResetVar();
      emit("");
      emit("define i32 @main() {");
      AddTabs();
      ArrayList<String> myList = new ArrayList<String>();
      myList.add(ClassName);
      myList.add("main");
      n.f14.accept(this, myList);
      emit("");
      for(String Argument: MySymbolTable.InitializeVariablesllvm(ClassName, "main"))
      {
          emit(Argument);
      }
      emit("");
      n.f15.accept(this, myList);
      n.f16.accept(this, argu);
      emit("ret i32 0");
      RemoveTabs();
      n.f17.accept(this, argu);
      emit("}");
      return _ret;
   }

   /**
    * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
   public ArrayList<String> visit(TypeDeclaration n, ArrayList<String> argu) throws Exception {
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
   public ArrayList<String> visit(ClassDeclaration n, ArrayList<String> argu) throws Exception {
      ArrayList<String> _ret=null;
      n.f0.accept(this, argu);
      String ClassName = n.f1.accept(this, argu).get(0);
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      ArrayList<String> myList = new ArrayList<String>();
      myList.add(ClassName);
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
   public ArrayList<String> visit(ClassExtendsDeclaration n, ArrayList<String> argu) throws Exception {
      ArrayList<String> _ret=null;
      n.f0.accept(this, argu);
      String ClassName = n.f1.accept(this, argu).get(0);
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      n.f5.accept(this, argu);
      ArrayList<String> myList = new ArrayList<String>();
      myList.add(ClassName);
      n.f6.accept(this, myList);
      n.f7.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public ArrayList<String> visit(VarDeclaration n, ArrayList<String> argu) throws Exception {
      ArrayList<String> _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
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
   public ArrayList<String> visit(MethodDeclaration n, ArrayList<String> argu) throws Exception {
      ArrayList<String> _ret=null;
      n.f1.accept(this, argu);
      String MethodName = n.f2.accept(this, argu).get(0);
      String ClassName = argu.get(0);
      ResetVar();
      emit("");
      emit("define " + DataTypellvm(MySymbolTable.MethodReturnType(ClassName, MethodName)) + " @" + ClassName + "." + MethodName + "(" + MySymbolTable.ArgumentsDecllvm(ClassName, MethodName) + ")" + "{");
      AddTabs();
      for(String Argument: MySymbolTable.InitializeArgumentsllvm(ClassName, MethodName))
      {
          emit(Argument);
      }
      emit("");
      for(String Argument: MySymbolTable.InitializeVariablesllvm(ClassName, MethodName))
      {
          emit(Argument);
      }
      emit("");
      ArrayList<String> myList = new ArrayList<String>();
      myList.add(ClassName);
      myList.add(MethodName);
      n.f4.accept(this, myList);
      n.f7.accept(this, myList);
      n.f8.accept(this, myList);
      ArrayList<String> ValReturnList = n.f10.accept(this, myList);
      String ValToReturn = ValReturnList.get(0);
      String ValToReturnType = ValReturnList.get(1);

      String Type = MySymbolTable.MethodReturnType(ClassName, MethodName);
      String DataType = "void";
      if(Type != null)
        DataType = DataTypellvm(Type);   
      
      ValToReturn = PrepareOperand(ValToReturn, ClassName, MethodName, ValToReturnType);
        

      emit("ret " + DataType + " " + ValToReturn);


      RemoveTabs();
      emit("}");
      return _ret;
   }

   /**
    * f0 -> FormalParameter()
    * f1 -> FormalParameterTail()
    */
   public ArrayList<String> visit(FormalParameterList n, ArrayList<String> argu) throws Exception {
      ArrayList<String> _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
   public ArrayList<String> visit(FormalParameter n, ArrayList<String> argu) throws Exception {
      ArrayList<String> _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> ( FormalParameterTerm() )*
    */
   public ArrayList<String> visit(FormalParameterTail n, ArrayList<String> argu) throws Exception {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
   public ArrayList<String> visit(FormalParameterTerm n, ArrayList<String> argu) throws Exception {
      ArrayList<String> _ret=null;
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
   public ArrayList<String> visit(Type n, ArrayList<String> argu) throws Exception {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public ArrayList<String> visit(ArrayType n, ArrayList<String> argu) throws Exception {
      ArrayList<String> _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "boolean"
    */
   public ArrayList<String> visit(BooleanType n, ArrayList<String> argu) throws Exception {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> "int"
    */
   public ArrayList<String> visit(IntegerType n, ArrayList<String> argu) throws Exception {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> Block()
    *       | AssignmentStatement()
    *       | ArrayAssignmentStatement()
    *       | IfStatement()
    *       | WhileStatement()
    *       | PrintStatement()
    */
   public ArrayList<String> visit(Statement n, ArrayList<String> argu) throws Exception {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
   public ArrayList<String> visit(Block n, ArrayList<String> argu) throws Exception {
      ArrayList<String> _ret=null;
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
    */
   public ArrayList<String> visit(AssignmentStatement n, ArrayList<String> argu) throws Exception {
      ArrayList<String> _ret=null;
      ArrayList<String> VarNameList = n.f0.accept(this, argu);
      String VarName = VarNameList.get(0);
      String VarNameType = VarNameList.get(1);

      String DataType = "";
      if(VarNameType.equals("<id>"))
        DataType = DataTypellvm(MySymbolTable.VariableType(argu.get(0), argu.get(1), VarName));
      else
        DataType = VarNameType;

      VarName = PrepareOperandAssignment(VarName, argu.get(0), argu.get(1), VarNameType);

      ArrayList<String> ValStoreList = n.f2.accept(this, argu);
      String ValToStore = ValStoreList.get(0);
      String ValToStoreType = ValStoreList.get(1);

      ValToStore = PrepareOperand(ValToStore, argu.get(0), argu.get(1), ValToStoreType);      

      emit("store " + DataType + " " + ValToStore + ", " + DataType + "* " + VarName);
      emit("");
      
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
    */
   public ArrayList<String> visit(ArrayAssignmentStatement n, ArrayList<String> argu) throws Exception {
      ArrayList<String> _ret = null;

      ArrayList<String> GetList3 = n.f0.accept(this, argu);
      String ArrayName = GetList3.get(0);
      String ArrayNameType = GetList3.get(1);

      ArrayName = PrepareOperand(ArrayName, argu.get(0), argu.get(1), ArrayNameType);

      ArrayList<String> GetList1 = n.f2.accept(this, argu);
      String ArrayPos = GetList1.get(0);
      String ArrayPosType = GetList1.get(1);

      ArrayPos = PrepareOperand(ArrayPos, argu.get(0), argu.get(1), ArrayPosType);

      ArrayList<String> GetList2 = n.f5.accept(this, argu);
      String ValToStore = GetList2.get(0);
      String ValToStoreType = GetList2.get(1);

      ValToStore = PrepareOperand(ValToStore, argu.get(0), argu.get(1), ValToStoreType);
      
      emit(NextVar() + " = load i32, i32* " + ArrayName);
      emit(NextVar() + " = icmp ult i32 " + ArrayPos + ", " + PrevVar());
      String FirstLabel = NextOOB();
      String SecondLabel = NextOOB();
      String ThirdLabel = NextOOB();
      emit("br i1" + CurVar() + ", label %" + FirstLabel + ", label %" + SecondLabel);
      emitNoTabs(FirstLabel + ":");
      emit(NextVar() + " = add i32 " + ArrayPos + ", 1");
      emit(NextVar() + " = getelementptr i32, i32* " + ArrayName +", i32 " + PrevVar());
      emit("store i32 " + ValToStore + ", i32* " + CurVar());
      emit("br label %" + ThirdLabel);
      emitNoTabs(SecondLabel + ":");
      emit("call void @throw_oob()");
      emit("br label %" + ThirdLabel);
      emitNoTabs(ThirdLabel + ":");
      emit("");

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
    */
   public ArrayList<String> visit(IfStatement n, ArrayList<String> argu) throws Exception {
      ArrayList<String> _ret=null;
      ArrayList<String> GetList = n.f2.accept(this, argu);
      String VarName = GetList.get(0);
      String VarType = GetList.get(1);

      VarName = PrepareOperand(VarName, argu.get(0), argu.get(1), VarType);

      String FirstLabel = NextIfLabel();
      String SecondLabel = NextIfLabel();
      String ThirdLabel = NextIfLabel();

      emit("br i1 " + VarName + ", label %" + FirstLabel + ", label %" + SecondLabel);
      AddTabs();
      emitNoTabs(FirstLabel + ":");      
      n.f4.accept(this, argu);
      emit("br label %" + ThirdLabel);
      emitNoTabs(SecondLabel + ":");
      n.f6.accept(this, argu);
      emit("br label %" + ThirdLabel);
      emitNoTabs(ThirdLabel + ":");
      RemoveTabs();      
      return _ret;
   }

   /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
   public ArrayList<String> visit(WhileStatement n, ArrayList<String> argu) throws Exception {
      ArrayList<String> _ret=null;

      String FirstLabel = NextLoopLabel();
      String SecondLabel = NextLoopLabel();
      String ThirdLabel = NextLoopLabel();

      emit("br label %" + FirstLabel);
      emitNoTabs(FirstLabel + ":");

      ArrayList<String> GetList = n.f2.accept(this, argu);
      String VarName = GetList.get(0);
      String VarType = GetList.get(1);
      
      VarName = PrepareOperand(VarName, argu.get(0), argu.get(1), VarType);

      emit("br i1 " + VarName + ", label %" + SecondLabel + ", label %" + ThirdLabel);
      AddTabs();      
      emitNoTabs(SecondLabel + ":");
      n.f4.accept(this, argu);
      emit("br label %" + FirstLabel);
      RemoveTabs();
      emitNoTabs(ThirdLabel + ":");      

      return _ret;
   }

   /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
   public ArrayList<String> visit(PrintStatement n, ArrayList<String> argu) throws Exception {
      ArrayList<String> _ret = null;
      ArrayList<String> GetList =  n.f2.accept(this, argu);
      String ValToPrint = GetList.get(0);
      String ValToPrintType = GetList.get(1);

      ValToPrint = PrepareOperand(ValToPrint, argu.get(0), argu.get(1), ValToPrintType);
      
      emit("call void (i32) @print_int(i32 " + ValToPrint + ")");
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
    */
   public ArrayList<String> visit(Expression n, ArrayList<String> argu) throws Exception {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> Clause()
    * f1 -> "&&"
    * f2 -> Clause()
    */
   public ArrayList<String> visit(AndExpression n, ArrayList<String> argu) throws Exception {
      String FirstLabel = NextAndLabel();
      String SecondLabel = NextAndLabel();
      String ThirdLabel = NextAndLabel();
      String FourthLabel = NextAndLabel();
      ArrayList<String> GetList1 = n.f0.accept(this, argu);
      String First = GetList1.get(0);
      String FirstType = GetList1.get(1);
      First = PrepareOperand(First, argu.get(0), argu.get(1), FirstType);
      emit("br label %" + FirstLabel);
      emitNoTabs(FirstLabel + ":");
      emit("br i1 " + First + ", label %" + SecondLabel + ", label %" + FourthLabel);
      emitNoTabs(SecondLabel + ":");
      ArrayList<String> GetList2 = n.f2.accept(this, argu);
      String Second = GetList2.get(0);
      String SecondType = GetList2.get(1);
      Second = PrepareOperand(Second, argu.get(0), argu.get(1), SecondType);
      emit("br label %" + ThirdLabel);      
      emitNoTabs(ThirdLabel + ":");
      emit("br label %" + FourthLabel);
      emitNoTabs(FourthLabel + ":");
      emit(NextVar() + " = phi i1 [ 0, %" + FirstLabel + " ], [ " + Second + ", %" + ThirdLabel + " ]");
    
      ArrayList<String> ReturnList = new ArrayList<String>();
      ReturnList.add(CurVar());
      ReturnList.add("boolean");
      return ReturnList;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
   public ArrayList<String> visit(CompareExpression n, ArrayList<String> argu) throws Exception {
      ArrayList<String> getList1 = n.f0.accept(this, argu);
      String First = getList1.get(0);
      String FirstType = getList1.get(1);

      First = PrepareOperand(First, argu.get(0), argu.get(1), FirstType);

      ArrayList<String> getList2 = n.f2.accept(this, argu);
      String Second = getList2.get(0);
      String SecondType = getList2.get(1);

      Second = PrepareOperand(Second, argu.get(0), argu.get(1), SecondType);
      
      emit(NextVar() + " = icmp slt i32 " + First + ", " + Second);
      ArrayList<String> ReturnList = new ArrayList<String>();
      ReturnList.add(CurVar());
      ReturnList.add("boolean");

      return ReturnList;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
   public ArrayList<String> visit(PlusExpression n, ArrayList<String> argu) throws Exception {
      ArrayList<String> getList1 = n.f0.accept(this, argu);
      String First = getList1.get(0);
      String FirstType = getList1.get(1);

      First = PrepareOperand(First, argu.get(0), argu.get(1), FirstType);

      ArrayList<String> getList2 = n.f2.accept(this, argu);
      String Second = getList2.get(0);
      String SecondType = getList2.get(1);

      Second = PrepareOperand(Second, argu.get(0), argu.get(1), SecondType);
      
      emit(NextVar() + " = add i32 " + First + ", " + Second);
      ArrayList<String> ReturnList = new ArrayList<String>();
      ReturnList.add(CurVar());
      ReturnList.add("int");

      return ReturnList;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
   public ArrayList<String> visit(MinusExpression n, ArrayList<String> argu) throws Exception {
      ArrayList<String> getList1 = n.f0.accept(this, argu);
      String First = getList1.get(0);
      String FirstType = getList1.get(1);

      First = PrepareOperand(First, argu.get(0), argu.get(1), FirstType);

      ArrayList<String> getList2 = n.f2.accept(this, argu);
      String Second = getList2.get(0);
      String SecondType = getList2.get(1);

      Second = PrepareOperand(Second, argu.get(0), argu.get(1), SecondType);
      
      emit(NextVar() + " = sub i32 " + First + ", " + Second);
      ArrayList<String> ReturnList = new ArrayList<String>();
      ReturnList.add(CurVar());
      ReturnList.add("int");

      return ReturnList;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public ArrayList<String> visit(TimesExpression n, ArrayList<String> argu) throws Exception {
      ArrayList<String> getList1 = n.f0.accept(this, argu);
      String First = getList1.get(0);
      String FirstType = getList1.get(1);

      First = PrepareOperand(First, argu.get(0), argu.get(1), FirstType);

      ArrayList<String> getList2 = n.f2.accept(this, argu);
      String Second = getList2.get(0);
      String SecondType = getList2.get(1);

      Second = PrepareOperand(Second, argu.get(0), argu.get(1), SecondType);
      
      emit(NextVar() + " = mul i32 " + First + ", " + Second);
      ArrayList<String> ReturnList = new ArrayList<String>();
      ReturnList.add(CurVar());
      ReturnList.add("int");

      return ReturnList;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
   public ArrayList<String> visit(ArrayLookup n, ArrayList<String> argu) throws Exception {
      ArrayList<String> getList1 = n.f0.accept(this, argu);
      String ArrayName = getList1.get(0);
      String ArrayType = getList1.get(1);

      ArrayName = PrepareOperand(ArrayName, argu.get(0), argu.get(1), ArrayType);      

      ArrayList<String> getList2 = n.f2.accept(this, argu);
      String ArrayPos = getList2.get(0);
      String ArrayPosType = getList2.get(1);
      
      ArrayPos = PrepareOperand(ArrayPos, argu.get(0), argu.get(1), ArrayPosType);

      emit(NextVar() + " = load i32, i32* " + ArrayName);
      emit(NextVar() + " = icmp ult i32 " + ArrayPos + ", " + PrevVar());
      String FirstLabel = NextOOB();
      String SecondLabel = NextOOB();
      String ThirdLabel = NextOOB();
      emit("br i1 " + CurVar() + ", label %" + FirstLabel + ", label %" + SecondLabel);
      emitNoTabs(FirstLabel + ":");
      emit(NextVar() + " = add i32 " + ArrayPos + ", 1");
      emit(NextVar() + " = getelementptr i32, i32* " + ArrayName + ", i32 " + PrevVar());
      emit(NextVar() + " = load i32, i32* " + PrevVar());
      emit("br label %" + ThirdLabel);
      emitNoTabs(SecondLabel + ":");
      emit("call void @throw_oob()");
      emit("br label %" + ThirdLabel);
      emitNoTabs(ThirdLabel + ":");

      ArrayList<String> ReturnList = new ArrayList<String>();
      ReturnList.add(CurVar());
      ReturnList.add("int");

      return ReturnList;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
   public ArrayList<String> visit(ArrayLength n, ArrayList<String> argu) throws Exception {
      ArrayList<String> GetList = n.f0.accept(this, argu);
      String ArrayName = GetList.get(0);
      String ArrayType = GetList.get(1);

      ArrayName = PrepareOperand(ArrayName, argu.get(0), argu.get(1), ArrayType);
      
      emit(NextVar() + " = load i32, i32* " + ArrayName);
      ArrayList<String> ReturnList = new ArrayList<String>();
      ReturnList.add(CurVar());
      ReturnList.add("int");

      return ReturnList;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
   public ArrayList<String> visit(MessageSend n, ArrayList<String> argu) throws Exception {
      ArrayList<String> GetList = n.f0.accept(this, argu);
      String ObjectName = GetList.get(0);
      String ObjectType = GetList.get(1);
      String ClassName = "";
      if(ObjectType.equals("<id>"))
        ClassName = MySymbolTable.VariableType(argu.get(0), argu.get(1), ObjectName);
      else
        ClassName = ObjectType;
      ObjectName = PrepareOperand(ObjectName, argu.get(0), argu.get(1), ObjectType);      
      String MethodName = n.f2.accept(this, argu).get(0);
      ArrayList<String> ParameterTypes = new ArrayList<String>();
      ArrayList<String> ParameterNames;
    
      ParameterTypes.addAll(MySymbolTable.MethodParams(ClassName, MethodName));
      ParameterStack.push(new ArrayList<String>());
      n.f4.accept(this, argu);

      String ParamCall = "";
      String BitCastPrepare = "";
      ParameterNames = (ArrayList<String>) ParameterStack.pop();
      for(int i = 0; i < ParameterNames.size(); i++)
      {
         String VarType = DataTypellvm(ParameterTypes.get(i));
         BitCastPrepare += ", " + VarType;
         String Var = ParameterNames.get(i); 
         ParamCall += ", " + VarType + " " + Var;
      }
      
      emit(NextVar() + " = bitcast i8* " + ObjectName + " to i8***");
      emit(NextVar() + " = load i8**, i8*** " + PrevVar());
      emit(NextVar() + " = getelementptr i8*, i8** " + PrevVar() + ", i32 " + MySymbolTable.GetMethodOffset(ClassName, MethodName)/8);
      emit(NextVar() + " = load i8*, i8** " + PrevVar());
      String MethodReturnType = DataTypellvm(MySymbolTable.MethodReturnType(ClassName, MethodName));
      emit(NextVar() + " = bitcast i8* " + PrevVar() + " to " + MethodReturnType +" (i8*" + BitCastPrepare + ")*");
      emit(NextVar() + " = call " + MethodReturnType + " " + PrevVar() +"(i8* " + ObjectName + ParamCall + ")");

      ArrayList<String> ReturnList = new ArrayList<String>();
      ReturnList.add(CurVar());
      ReturnList.add(MySymbolTable.MethodReturnType(ClassName, MethodName));

      return ReturnList;
   }

   /**
    * f0 -> Expression()
    * f1 -> ExpressionTail()
    */
   public ArrayList<String> visit(ExpressionList n, ArrayList<String> argu) throws Exception {
      ArrayList<String> _ret=null;
      ArrayList<String> myList = n.f0.accept(this, argu);
      String Param = myList.get(0);
      String ParamType = myList.get(1);

      Param = PrepareOperand(Param, argu.get(0), argu.get(1), ParamType);

      ArrayList<String> ParameterTemp = (ArrayList<String>) ParameterStack.peek();
      ParameterTemp.add(Param);

      n.f1.accept(this, argu);      
      return _ret;
   }

   /**
    * f0 -> ( ExpressionTerm() )*
    */
   public ArrayList<String> visit(ExpressionTail n, ArrayList<String> argu) throws Exception {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> ","
    * f1 -> Expression()
    */
   public ArrayList<String> visit(ExpressionTerm n, ArrayList<String> argu) throws Exception {
      ArrayList<String> _ret=null;
      ArrayList<String> myList = n.f1.accept(this, argu);
      String Param = myList.get(0);
      String ParamType = myList.get(1);

      Param = PrepareOperand(Param, argu.get(0), argu.get(1), ParamType);

      ArrayList<String> ParameterTemp = (ArrayList<String>) ParameterStack.peek();
      ParameterTemp.add(Param);
      return _ret;
   }

   /**
    * f0 -> NotExpression()
    *       | PrimaryExpression()
    */
   public ArrayList<String> visit(Clause n, ArrayList<String> argu) throws Exception {
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
    */
   public ArrayList<String> visit(PrimaryExpression n, ArrayList<String> argu) throws Exception {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public ArrayList<String> visit(IntegerLiteral n, ArrayList<String> argu) throws Exception {
      ArrayList<String> myList = new ArrayList<String>();
      n.f0.accept(this, argu);
      myList.add(n.f0.toString());
      myList.add("int");
      return myList;
   }

   /**
    * f0 -> "true"
    */
   public ArrayList<String> visit(TrueLiteral n, ArrayList<String> argu) throws Exception {
      ArrayList<String> myList = new ArrayList<String>();
      n.f0.accept(this, argu);
      myList.add("1");
      myList.add("boolean");
      return myList;
   }

   /**
    * f0 -> "false"
    */
   public ArrayList<String> visit(FalseLiteral n, ArrayList<String> argu) throws Exception {
      ArrayList<String> myList = new ArrayList<String>();        
      n.f0.accept(this, argu);
      myList.add("0");
      myList.add("boolean");
      return myList;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public ArrayList<String> visit(Identifier n, ArrayList<String> argu) throws Exception {
      ArrayList<String> myList = new ArrayList<String>();    
      n.f0.accept(this, argu);
      myList.add(n.f0.toString());
      myList.add("<id>");
      return myList;
   }

   /**
    * f0 -> "this"
    */
   public ArrayList<String> visit(ThisExpression n, ArrayList<String> argu) throws Exception {
      ArrayList<String> myList = new ArrayList<String>();  
      n.f0.accept(this, argu);
      myList.add(n.f0.toString());
      myList.add(argu.get(0));
      return myList;
   }

   /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
   public ArrayList<String> visit(ArrayAllocationExpression n, ArrayList<String> argu) throws Exception {
      ArrayList<String> GetList = n.f3.accept(this, argu);

      String ArraySize = GetList.get(0);
      String ArrayType = GetList.get(1);

      ArraySize = PrepareOperand(ArraySize, argu.get(0), argu.get(1), ArrayType);

      emit(NextVar() + " = icmp slt i32 " + ArraySize + ", 0");
      String FirstLabel = NextArray();
      String SecondLabel = NextArray();
      emit("br i1 " + CurVar() + ", label %" + FirstLabel + ", label %" + SecondLabel);
      emitNoTabs(FirstLabel + ":");
      emit("call void @throw_oob()");
      emit("br label %" + SecondLabel);
      emitNoTabs(SecondLabel + ":");
      emit(NextVar() + " = add i32 " + ArraySize + ", 1");
      emit(NextVar() + " = call i8* @calloc(i32 4, i32 " + PrevVar() + ")");
      emit(NextVar() + " = bitcast i8* " + PrevVar() + " to i32*");
      emit("store i32 " + ArraySize + ", i32* " + CurVar());

      ArrayList<String> ReturnList = new ArrayList<String>();
      ReturnList.add(CurVar());
      ReturnList.add("int []");      

      return ReturnList;
   }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
   public ArrayList<String> visit(AllocationExpression n, ArrayList<String> argu) throws Exception {
      String ClassName = n.f1.accept(this, argu).get(0);
      emit("");
      String MemoryLocation = NextVar();
      emit(MemoryLocation + " = call i8* @calloc(i32 1, i32 " + (MySymbolTable.ObjectSize(ClassName)).toString() + ")");
      emit(NextVar() + " = bitcast i8* " + MemoryLocation + " to i8***");
      emit(NextVar() + " = getelementptr [" + MySymbolTable.GetNumOfMethods(ClassName) + " x i8*], ["
       + MySymbolTable.GetNumOfMethods(ClassName) + " x i8*]* @." + ClassName + "_vtable, i32 0, i32 0");
      emit("store i8** " + CurVar() + ", i8*** " + PrevVar());
      ArrayList<String> ReturnList = new ArrayList<String>();
      ReturnList.add(MemoryLocation);
      ReturnList.add(ClassName);

      return ReturnList;
   }

   /**
    * f0 -> "!"
    * f1 -> Clause()
    */
   public ArrayList<String> visit(NotExpression n, ArrayList<String> argu) throws Exception {
      ArrayList<String> GetList =  n.f1.accept(this, argu);
      String VarName = GetList.get(0);
      String VarType = GetList.get(1);

      VarName = PrepareOperand(VarName, argu.get(0), argu.get(1), VarType);

      emit(NextVar() + " = xor i1 1, " + VarName);

      ArrayList<String> ReturnList = new ArrayList<String>();
      ReturnList.add(CurVar());
      ReturnList.add("boolean");

      return ReturnList;
   }

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
   public ArrayList<String> visit(BracketExpression n, ArrayList<String> argu) throws Exception {
      return n.f1.accept(this, argu);
   }

}
