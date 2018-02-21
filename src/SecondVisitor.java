import syntaxtree.*;
import visitor.GJDepthFirst;
import java.util.*;
import java.io.*;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class SecondVisitor extends GJDepthFirst<String, ArrayList<String>>{
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
        ClassName = ClassName.substring(0, ClassName.length() - 3);
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
        n.f14.accept(this, argu);
        ArrayList<String> myList = new ArrayList<String>();
        myList.add(ClassName);
        myList.add("main");
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
      ClassName = ClassName.substring(0, ClassName.length() - 3);
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
    @Override
   public String visit(ClassExtendsDeclaration n, ArrayList<String> argu) throws Exception {
      String _ret=null;
      n.f0.accept(this, argu);
      String ClassName = n.f1.accept(this, argu);
      ClassName = ClassName.substring(0, ClassName.length() - 3);
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
    *
   public R visit(VarDeclaration n, A argu) throws Exception {
      R _ret=null;
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
    @Override
   public String visit(MethodDeclaration n, ArrayList<String> argu) throws Exception {
      String _ret=null;
      n.f0.accept(this, argu);
      String ReturnType = n.f1.accept(this, argu);
      if(ReturnType.endsWith(".id"))
            ReturnType = ReturnType.substring(0, ReturnType.length() - 3);
      String MethodName = n.f2.accept(this, argu);
      MethodName = MethodName.substring(0, MethodName.length() - 3);
      if(!MySymbolTable.MethodisValid(argu.get(0), MethodName))
            throw new Exception("Method: " + MethodName + " is not defined properly (respecting parent class)");
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      n.f5.accept(this, argu);
      n.f6.accept(this, argu);
      n.f7.accept(this, argu);
      ArrayList<String> myList = new ArrayList<String>();
      myList.add(argu.get(0));
      myList.add(MethodName);
      n.f8.accept(this, myList);
      n.f9.accept(this, argu);
      String TypeResult = n.f10.accept(this, myList);
      if(!TypeResult.equals(ReturnType))
      {
        if(TypeResult.endsWith(".id"))
        {
            TypeResult = TypeResult.substring(0, TypeResult.length() - 3);
            TypeResult = MySymbolTable.VariableType(argu.get(0), MethodName, TypeResult);
        }
        if(TypeResult == null || (!TypeResult.equals(ReturnType) && !MySymbolTable.isParent(ReturnType, TypeResult)))
            throw new Exception("Variable: " + TypeResult + " is not " + ReturnType + " 'return Exp'");
      }
      n.f11.accept(this, argu);
      n.f12.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> FormalParameter()
    * f1 -> FormalParameterTail()
    *
   public R visit(FormalParameterList n, A argu) throws Exception {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    *
   public R visit(FormalParameter n, A argu) throws Exception {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> ( FormalParameterTerm() )*
    *
   public R visit(FormalParameterTail n, A argu) throws Exception {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> ","
    * f1 -> FormalParameter()
    *
   public R visit(FormalParameterTerm n, A argu) throws Exception {
      R _ret=null;
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
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      return "int []";
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
    */
    @Override
   public String visit(Statement n, ArrayList<String> argu) throws Exception {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
    @Override
   public String visit(Block n, ArrayList<String> argu) throws Exception {
      String _ret=null;
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
    @Override
   public String visit(AssignmentStatement n, ArrayList<String> argu) throws Exception {
      String _ret=null;
      String VarName = n.f0.accept(this, argu);
      VarName = VarName.substring(0, VarName.length() - 3);
      String VarType = MySymbolTable.VariableType(argu.get(0), argu.get(1), VarName);
      n.f1.accept(this, argu);
      String TypeResult = n.f2.accept(this, argu);
      if(VarType == null)
            throw new Exception("Variable: " + VarName + " doesn't exist 'Identifier = Exp'");
      if(!TypeResult.equals(VarType))
      {
        if(TypeResult.endsWith(".id"))
        {
            TypeResult = TypeResult.substring(0, TypeResult.length() - 3);
            TypeResult = MySymbolTable.VariableType(argu.get(0), argu.get(1), TypeResult);
        }
        if(TypeResult == null || (!TypeResult.equals(VarType) && !MySymbolTable.isParent(VarType, TypeResult)))
            throw new Exception("Type: " + TypeResult + " instead of " + VarType + " 'Identifier = Exp'");
      }
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
    */
    @Override
   public String visit(ArrayAssignmentStatement n, ArrayList<String> argu) throws Exception {
      String _ret=null;
      String VarName = n.f0.accept(this, argu);
      VarName = VarName.substring(0, VarName.length() - 3);
      String VarType = MySymbolTable.VariableType(argu.get(0), argu.get(1), VarName);
      if(VarType == null)
         throw new Exception("Variable: " + VarName + " doesn't exist ' Identifier[Exp] = Exp'");
      if(!VarType.equals("int []"))
         throw new Exception("Type: " + VarType + " instead of int [] ' Identifier[Exp] = Exp'");
      n.f1.accept(this, argu);
      String TypeResult = n.f2.accept(this, argu);
      if(!TypeResult.equals("int"))
      {
        if(TypeResult.endsWith(".id"))
        {
            TypeResult = TypeResult.substring(0, TypeResult.length() - 3);
            TypeResult = MySymbolTable.VariableType(argu.get(0), argu.get(1), TypeResult);
        }
        if(TypeResult == null || !TypeResult.equals("int"))
            throw new Exception("Type: " + TypeResult + " instead of int 'Identifier[Exp] = Exp'");
      }
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      TypeResult = n.f5.accept(this, argu);
      if(!TypeResult.equals("int"))
      {
        if(TypeResult.endsWith(".id"))
        {
            TypeResult = TypeResult.substring(0, TypeResult.length() - 3);
            TypeResult = MySymbolTable.VariableType(argu.get(0), argu.get(1), TypeResult);
        }
        if(TypeResult == null || !TypeResult.equals("int"))
            throw new Exception("Type: " + TypeResult + " instead of int 'Identifier[Exp] = Exp'");
      }
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
    */
    @Override
   public String visit(IfStatement n, ArrayList<String> argu) throws Exception {
      String _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String TypeResult = n.f2.accept(this, argu);
      if(!TypeResult.equals("boolean"))
      {
        if(TypeResult.endsWith(".id"))
        {
            TypeResult = TypeResult.substring(0, TypeResult.length() - 3);
            TypeResult = MySymbolTable.VariableType(argu.get(0), argu.get(1), TypeResult);
        }
        if(TypeResult == null || !TypeResult.equals("boolean"))
            throw new Exception("Type: " + TypeResult + " instead of int 'if(Exp)'");
      }
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
    */
    @Override
   public String visit(WhileStatement n, ArrayList<String> argu) throws Exception {
      String _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String TypeResult = n.f2.accept(this, argu);
      if(!TypeResult.equals("boolean"))
      {
        if(TypeResult.endsWith(".id"))
        {
            TypeResult = TypeResult.substring(0, TypeResult.length() - 3);
            TypeResult = MySymbolTable.VariableType(argu.get(0), argu.get(1), TypeResult);
        }
        if(TypeResult == null || !TypeResult.equals("boolean"))
            throw new Exception("Type: " + TypeResult + " instead of int 'while(EXP)'");
      }
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
    */
    @Override
   public String visit(PrintStatement n, ArrayList<String> argu) throws Exception {
      String _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String TypeResult = n.f2.accept(this, argu);
      if(!TypeResult.equals("int"))
      {
        if(TypeResult.endsWith(".id"))
        {
            TypeResult = TypeResult.substring(0, TypeResult.length() - 3);
            TypeResult = MySymbolTable.VariableType(argu.get(0), argu.get(1), TypeResult);
        }
        if(TypeResult == null || !TypeResult.equals("int"))
            throw new Exception("Type: " + TypeResult + " instead of int 'System.out.println(EXP)'");
      }
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
    */
    @Override
   public String visit(Expression n, ArrayList<String> argu) throws Exception {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> Clause()
    * f1 -> "&&"
    * f2 -> Clause()
    */
    @Override
   public String visit(AndExpression n, ArrayList<String> argu) throws Exception {
      String TypeResult = n.f0.accept(this, argu);
      if(!TypeResult.equals("int"))
      {
        if(TypeResult.endsWith(".id"))
        {
            TypeResult = TypeResult.substring(0, TypeResult.length() - 3);
            TypeResult = MySymbolTable.VariableType(argu.get(0), argu.get(1), TypeResult);
        }
        if(TypeResult == null || !TypeResult.equals("boolean"))
            throw new Exception("Type: " + TypeResult + " instead of boolean 'Clause && Clause'");
      }
      n.f1.accept(this, argu);
      TypeResult = n.f2.accept(this, argu);
      if(!TypeResult.equals("int"))
      {
        if(TypeResult.endsWith(".id"))
        {
            TypeResult = TypeResult.substring(0, TypeResult.length() - 3);
            TypeResult = MySymbolTable.VariableType(argu.get(0), argu.get(1), TypeResult);
        }
        if(TypeResult == null || !TypeResult.equals("boolean"))
            throw new Exception("Type: " + TypeResult + " instead of boolean 'Clause && Clause'");
      }
      return "boolean";
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
    @Override
   public String visit(CompareExpression n, ArrayList<String> argu) throws Exception {
      String TypeResult = n.f0.accept(this, argu);
      if(!TypeResult.equals("int"))
      {
        if(TypeResult.endsWith(".id"))
        {
            TypeResult = TypeResult.substring(0, TypeResult.length() - 3);
            TypeResult = MySymbolTable.VariableType(argu.get(0), argu.get(1), TypeResult);
        }
        if(TypeResult == null || !TypeResult.equals("int"))
            throw new Exception("Type: " + TypeResult + " instead of int 'PrimaryExp < PrimaryExp'");
      }
      n.f1.accept(this, argu);
      TypeResult = n.f2.accept(this, argu);
      if(!TypeResult.equals("int"))
      {
        if(TypeResult.endsWith(".id"))
        {
            TypeResult = TypeResult.substring(0, TypeResult.length() - 3);
            TypeResult = MySymbolTable.VariableType(argu.get(0), argu.get(1), TypeResult);
        }
        if(TypeResult == null || !TypeResult.equals("int"))
            throw new Exception("Type: " + TypeResult + " instead of int 'PrimaryExp < PrimaryExp'");
      }
      return "boolean";
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
    @Override
   public String visit(PlusExpression n, ArrayList<String> argu) throws Exception {
       String TypeResult = n.f0.accept(this, argu);
       if(!TypeResult.equals("int"))
       {
         if(TypeResult.endsWith(".id"))
         {
             TypeResult = TypeResult.substring(0, TypeResult.length() - 3);
             TypeResult = MySymbolTable.VariableType(argu.get(0), argu.get(1), TypeResult);
         }
         if(TypeResult == null || !TypeResult.equals("int"))
            throw new Exception("Type: " + TypeResult + " instead of int 'PrimaryExp + PrimaryExp'");
       }
       n.f1.accept(this, argu);
       TypeResult = n.f2.accept(this, argu);
       if(!TypeResult.equals("int"))
       {
         if(TypeResult.endsWith(".id"))
         {
             TypeResult = TypeResult.substring(0, TypeResult.length() - 3);
             TypeResult = MySymbolTable.VariableType(argu.get(0), argu.get(1), TypeResult);
         }
         if(TypeResult == null || !TypeResult.equals("int"))
            throw new Exception("Type: " + TypeResult + " instead of int 'PrimaryExp + PrimaryExp'");
       }
       return "int";
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
    @Override
   public String visit(MinusExpression n, ArrayList<String> argu) throws Exception {
      String TypeResult = n.f0.accept(this, argu);
      if(!TypeResult.equals("int"))
      {
        if(TypeResult.endsWith(".id"))
        {
            TypeResult = TypeResult.substring(0, TypeResult.length() - 3);
            TypeResult = MySymbolTable.VariableType(argu.get(0), argu.get(1), TypeResult);
        }
        if(TypeResult == null || !TypeResult.equals("int"))
            throw new Exception("Type: " + TypeResult + " instead of int 'PrimaryExp - PrimaryExp'");
      }
      n.f1.accept(this, argu);
      TypeResult = n.f2.accept(this, argu);
      if(!TypeResult.equals("int"))
      {
        if(TypeResult.endsWith(".id"))
        {
            TypeResult = TypeResult.substring(0, TypeResult.length() - 3);
            TypeResult = MySymbolTable.VariableType(argu.get(0), argu.get(1), TypeResult);
        }
        if(TypeResult == null || !TypeResult.equals("int"))
            throw new Exception("Type: " + TypeResult + " instead of int 'PrimaryExp - PrimaryExp'");
      }
      return "int";
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
    @Override
   public String visit(TimesExpression n, ArrayList<String> argu) throws Exception {
      String TypeResult = n.f0.accept(this, argu);
      if(!TypeResult.equals("int"))
      {
        if(TypeResult.endsWith(".id"))
        {
            TypeResult = TypeResult.substring(0, TypeResult.length() - 3);
            TypeResult = MySymbolTable.VariableType(argu.get(0), argu.get(1), TypeResult);
        }
        if(TypeResult == null || !TypeResult.equals("int"))
            throw new Exception("Type: " + TypeResult + " instead of int 'PrimaryExp * PrimaryExp'");
      }
      n.f1.accept(this, argu);
      TypeResult = n.f2.accept(this, argu);
      if(!TypeResult.equals("int"))
      {
        if(TypeResult.endsWith(".id"))
        {
            TypeResult = TypeResult.substring(0, TypeResult.length() - 3);
            TypeResult = MySymbolTable.VariableType(argu.get(0), argu.get(1), TypeResult);
        }
        if(TypeResult == null || !TypeResult.equals("int"))
            throw new Exception("Type: " + TypeResult + " instead of int 'PrimaryExp * PrimaryExp'");
      }
      return "int";
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
    @Override
   public String visit(ArrayLookup n, ArrayList<String> argu) throws Exception {
      String TypeResult = n.f0.accept(this, argu);
      if(!TypeResult.equals("int []"))
      {
        if(TypeResult.endsWith(".id"))
        {
            TypeResult = TypeResult.substring(0, TypeResult.length() - 3);
            TypeResult = MySymbolTable.VariableType(argu.get(0), argu.get(1), TypeResult);
        }
        if(TypeResult == null || !TypeResult.equals("int []"))
            throw new Exception("Type: " + TypeResult + " instead of int [] 'PrimaryExp [ PrimaryExp ]'");
      }
      n.f1.accept(this, argu);
      TypeResult = n.f2.accept(this, argu);
      if(!TypeResult.equals("int"))
      {
        if(TypeResult.endsWith(".id"))
        {
            TypeResult = TypeResult.substring(0, TypeResult.length() - 3);
            TypeResult = MySymbolTable.VariableType(argu.get(0), argu.get(1), TypeResult);
        }
        if(TypeResult == null || !TypeResult.equals("int"))
            throw new Exception("Type: " + TypeResult + " instead of int 'PrimaryExp [ PrimaryExp ]'");
      }
      n.f3.accept(this, argu);
      return "int";
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
    @Override
   public String visit(ArrayLength n, ArrayList<String> argu) throws Exception {
      String TypeResult = n.f0.accept(this, argu);
      if(!TypeResult.equals("int []"))
      {
        if(TypeResult.endsWith(".id"))
        {
            TypeResult = TypeResult.substring(0, TypeResult.length() - 3);
            TypeResult = MySymbolTable.VariableType(argu.get(0), argu.get(1), TypeResult);
        }
        if(TypeResult == null || !TypeResult.equals("int []"))
            throw new Exception("Type: " + TypeResult + " instead of int [] 'PrimaryExp.length'");
      }
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      return "int";
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
    @Override
   public String visit(MessageSend n, ArrayList<String> argu) throws Exception {
      String TypeResult = n.f0.accept(this, argu);
      String ClassName;

      if(TypeResult.endsWith(".id"))
      {
        TypeResult = TypeResult.substring(0, TypeResult.length() - 3);
        ClassName = MySymbolTable.VariableType(argu.get(0), argu.get(1), TypeResult);
        if(ClassName == null)
            throw new Exception("Variable: " + TypeResult + " is not a valid variable 'PrimaryExp.id(PrimaryExp)'");
      }
      else
      {
        ClassName = TypeResult;
      }

      if(!MySymbolTable.ClassExists(ClassName))
      {
         throw new Exception("Type: " + ClassName + " is not a valid class 'PrimaryExp.id(PrimaryExp)'");
      }

      n.f1.accept(this, argu);
      String MethodName = n.f2.accept(this, argu);
      MethodName = MethodName.substring(0, MethodName.length() - 3);
      if(!MySymbolTable.MethodExistsExtended(ClassName, MethodName))
      {
         throw new Exception("Method: " + MethodName + " is not a valid method for class: " + ClassName + " 'PrimaryExp.id(PrimaryExp)'");
      }
      n.f3.accept(this, argu);
      ArrayList<String> myList = new ArrayList<String>();
      myList.add(argu.get(0));
      myList.add(argu.get(1));
      myList.addAll(MySymbolTable.MethodParams(ClassName, MethodName));     
      n.f4.accept(this, myList);    
      if(myList.size() != 2)
         throw new Exception("Method: " + MethodName + " mismatched parameters on call ");
      n.f5.accept(this, argu);
      return MySymbolTable.MethodReturnType(ClassName, MethodName);
   }

   /**
    * f0 -> Expression()
    * f1 -> ExpressionTail()
    */
    @Override
   public String visit(ExpressionList n, ArrayList<String> argu) throws Exception {
      String _ret=null;
      String TypeResult = n.f0.accept(this, argu);
      if(TypeResult.endsWith(".id"))
      {
          TypeResult = TypeResult.substring(0, TypeResult.length() - 3);
          TypeResult = MySymbolTable.VariableType(argu.get(0), argu.get(1), TypeResult);
      }

      if(TypeResult == null || (!TypeResult.equals(argu.get(2)) && !MySymbolTable.isParent(argu.get(2), TypeResult)))
      {
          throw new Exception("Type: " + TypeResult +" is not a valid type for argument of type " + argu.get(2) + " on method: " + argu.get(1) + " '(Exp, .. ,Exp)'");        
      }
      argu.remove(2);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> ( ExpressionTerm() )*
    */
    @Override
   public String visit(ExpressionTail n, ArrayList<String> argu) throws Exception {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> ","
    * f1 -> Expression()
    */
    @Override
   public String visit(ExpressionTerm n, ArrayList<String> argu) throws Exception {
      String _ret=null;
      n.f0.accept(this, argu);
      String TypeResult = n.f1.accept(this, argu);
      if(TypeResult.endsWith(".id"))
      {
          TypeResult = TypeResult.substring(0, TypeResult.length() - 3);
          TypeResult = MySymbolTable.VariableType(argu.get(0), argu.get(1), TypeResult);
      }

      if(TypeResult == null || (!TypeResult.equals(argu.get(2)) && !MySymbolTable.isParent(argu.get(2), TypeResult)))
      {
          throw new Exception("Type: " + TypeResult +" is not a valid type for argument of type " + argu.get(2) + " on method: " + argu.get(1) + " '(Exp, .. ,Exp)'");        
      }
      argu.remove(2);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> NotExpression()
    *       | PrimaryExpression()
    */
    @Override
   public String visit(Clause n, ArrayList<String> argu) throws Exception {
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
    @Override
   public String visit(PrimaryExpression n, ArrayList<String> argu) throws Exception {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
    @Override
   public String visit(IntegerLiteral n, ArrayList<String> argu) throws Exception {
      n.f0.accept(this, argu);
      return "int";
   }

   /**
    * f0 -> "true"
    */
    @Override
   public String visit(TrueLiteral n, ArrayList<String> argu) throws Exception {
      n.f0.accept(this, argu);
      return "boolean";
   }

   /**
    * f0 -> "false"
    */
    @Override
   public String visit(FalseLiteral n, ArrayList<String> argu) throws Exception {
      n.f0.accept(this, argu);
      return "boolean";
   }

   /**
    * f0 -> <IDENTIFIER>
    */
    @Override
    public String visit(Identifier n, ArrayList<String> argu) throws Exception {		
       n.f0.accept(this, argu);
       return n.f0.toString()+".id";
    }

   /**
    * f0 -> "this"
    */
    @Override
   public String visit(ThisExpression n, ArrayList<String> argu) throws Exception {
      n.f0.accept(this, argu);
      return argu.get(0);
   }

   /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
    @Override
   public String visit(ArrayAllocationExpression n, ArrayList<String> argu) throws Exception {
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      String TypeResult = n.f3.accept(this, argu);
      String TranslatedType = TypeResult;
      if(TypeResult.endsWith(".id"))
      {
          TypeResult = TypeResult.substring(0, TypeResult.length() - 3);
          TranslatedType = MySymbolTable.VariableType(argu.get(0), argu.get(1), TypeResult);
      }
      if(TranslatedType == null || !TranslatedType.equals("int"))
            throw new Exception("Type: " + TranslatedType + " instead of int inside of 'new int [TypeInt]'");
      n.f4.accept(this, argu);
      return "int []";
   }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
    @Override
   public String visit(AllocationExpression n, ArrayList<String> argu) throws Exception {
      n.f0.accept(this, argu);
      String ClassName = n.f1.accept(this, argu);
      ClassName = ClassName.substring(0, ClassName.length() - 3);
      if(!MySymbolTable.ClassExists(ClassName))
      {
          throw new Exception("Class: " + ClassName + " doesn't exist (can't create object)");
      }
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      return ClassName;
   }

   /**
    * f0 -> "!"
    * f1 -> Clause()
    */
    @Override
   public String visit(NotExpression n, ArrayList<String> argu) throws Exception {
      n.f0.accept(this, argu);
      String TypeResult = n.f1.accept(this, argu);
      String TranslatedType = TypeResult;
      if(TypeResult.endsWith(".id"))
      {
          TypeResult = TypeResult.substring(0, TypeResult.length() - 3);
          TranslatedType = MySymbolTable.VariableType(argu.get(0), argu.get(1), TypeResult);
      }
      if(TranslatedType == null || !TranslatedType.equals("boolean"))
            throw new Exception("Type: " + TranslatedType + " instead of boolean '! Clause'");
      return "boolean";
   }

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
    @Override
   public String visit(BracketExpression n, ArrayList<String> argu) throws Exception {
      n.f0.accept(this, argu);
      String TypeResult = n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      return TypeResult;
   }

}