import java.util.*;
public class ClassInfo
{
   String Name;
   String ParentClass;
   Map<String ,String> Variables;
   Map<String ,MethodInfo> Methods;
   Map<String ,Integer> VariablesOffsetMap;
   Map<String ,Integer> MethodsOffsetMap;
   Integer VariablesOffset;
   Integer MethodsOffset;
   Integer NumOfMethods;
   
   public ClassInfo(String NewName, String NewParentClass)
   {
       Name = NewName;
       ParentClass = NewParentClass;
       Variables = new LinkedHashMap<String ,String>();
       Methods = new LinkedHashMap<String ,MethodInfo>();
       VariablesOffsetMap = new LinkedHashMap<String ,Integer>();
       MethodsOffsetMap = new LinkedHashMap<String ,Integer>();
       if(ParentClass != null)
       {
            VariablesOffset = MySymbolTable.GetParentClassVarOffset(ParentClass);
            MethodsOffset = MySymbolTable.GetParentClassMethodOffset(ParentClass);
       }
       else
       {
            VariablesOffset = 0; 
            MethodsOffset = 0;
       }
       NumOfMethods = 0;
   }

   public void AddVariable(String VarName, String VarType)
   {
       Variables.put(VarName, VarType);
       VariablesOffsetMap.put(VarName, VariablesOffset);
       VariablesOffset += CalculateOffset(VarType);
   }

   public void AddMethod(String MethodName, String ReturnType)
   {
       Methods.put(MethodName, new MethodInfo(MethodName, Name, ReturnType));
       if(((ParentClass != null) && MySymbolTable.MethodExistsParent(ParentClass, MethodName)) || (ReturnType.equals("void")))
       {
          MethodsOffsetMap.put(MethodName, -1);
       }
       else
       {
          MethodsOffsetMap.put(MethodName, MethodsOffset);
          MethodsOffset += 8;
       }
   }

   public void AddArgumentToMethod(String MethodName, String Argument, String Type)
   {
       Methods.get(MethodName).AddArgument(Argument, Type);
   }

   public void AddVariableToMethod(String MethodName, String Variable, String Type)
   {
       Methods.get(MethodName).AddVariable(Variable, Type);
   }
   
   public boolean VariableExists(String ClassName, String VarName)
   {
       return Variables.containsKey(VarName);
   }

   public boolean MethodExists(String ClassName, String MethodName)
   {
       return Methods.containsKey(MethodName);
   }

   public boolean VariableExistsIntoMethod(String MethodName, String VarName)
   {
       return Methods.get(MethodName).VariableExistsIntoMethod(VarName);
   }

   public boolean ArgumentExistsIntoMethod(String MethodName, String Argument)
   {
       return Methods.get(MethodName).ArgumentExistsIntoMethod(Argument);
   }

   public ArrayList<String> MethodParams(String MethodName)
   {
       return Methods.get(MethodName).MethodParams();
   }

   public String MethodReturnType(String MethodName)
   {
       return Methods.get(MethodName).MethodReturnType();
   }

   public String GetParentClass()
   {
       return ParentClass;
   }

   public Integer GetDataSize()
   {
       return VariablesOffset;
   }

   public String VariableType(String MethodName, String VarName)
   {
       String SearchResult = null;
       if(MethodName != null)
            SearchResult = Methods.get(MethodName).VariableType(VarName);
       if(SearchResult == null)
       {
           if(Variables.containsKey(VarName))
               SearchResult = Variables.get(VarName);
           else
               SearchResult = null;
       }
       return SearchResult;
   }

   public void Print()
   {
       System.out.println("    Parent Class: " + ParentClass);
       System.out.println("    Variables: ");
       Iterator<Map.Entry<String, String>> it = Variables.entrySet().iterator();
       while (it.hasNext())
       {
            Map.Entry<String, String> pair = it.next();
            System.out.println("        " + pair.getValue() + ": " + pair.getKey());
       }

       System.out.println("    Methods: ");
       Iterator<Map.Entry<String, MethodInfo>> it2 = Methods.entrySet().iterator();
       while (it2.hasNext())
       {
           Map.Entry<String, MethodInfo> pair2 = it2.next();
           System.out.println("        " + pair2.getKey());
           pair2.getValue().Print();
       }

   }

   public void PrintOffset()
   {
       System.out.println("-----------Class " + Name + "-----------");
       System.out.println("--Variables---");
       Iterator<Map.Entry<String, Integer>> it = VariablesOffsetMap.entrySet().iterator();
       while (it.hasNext())
       {
            Map.Entry<String, Integer> pair = it.next();
            System.out.println(Name + "." + pair.getKey() + " : " + pair.getValue());
       }

       System.out.println("---Methods---");
       Iterator<Map.Entry<String, Integer>> it2 = MethodsOffsetMap.entrySet().iterator();
       while (it2.hasNext())
       {
           Map.Entry<String, Integer> pair2 = it2.next();
           if(pair2.getValue() >= 0)
           {
               System.out.println(Name + "." + pair2.getKey() + " : " + pair2.getValue());
           }
       }
       System.out.println("");

   }

   public int CalculateOffset(String DataType)
   {
       if(DataType.equals("int"))
          return 4;
       else if(DataType.equals("boolean"))
          return 1;
       else
          return 8;
   }

   public Map<String ,String> AllParentMethods()
   {
       Map<String ,String> AllMethods;
       if(ParentClass != null)
       {
          AllMethods = MySymbolTable.Table.get(ParentClass).AllParentMethods();
          Iterator<Map.Entry<String, String>> it = AllMethods.entrySet().iterator();
          while (it.hasNext())
          {
              Map.Entry<String, String> pair = it.next();
              if(MethodsOffsetMap.containsKey(pair.getKey()))
                 AllMethods.put(pair.getKey(), Name);
          }
          Iterator<Map.Entry<String, Integer>> it2 = MethodsOffsetMap.entrySet().iterator();
          while (it2.hasNext())
          {
              Map.Entry<String, Integer> pair2 = it2.next();
              if(pair2.getValue() >= 0)
                AllMethods.put(pair2.getKey(), Name);
          }
       }
       else
       {
          AllMethods = new LinkedHashMap<String ,String>();
          Iterator<Map.Entry<String, Integer>> it = MethodsOffsetMap.entrySet().iterator();
          while (it.hasNext())
          {
              Map.Entry<String, Integer> pair = it.next();
              if(pair.getValue() > -1)
                 AllMethods.put(pair.getKey(), Name);
          }          
       }

       return AllMethods;
   }

   public String VtableMethods()
   {
       Map<String ,String> AllMethods = AllParentMethods();
       NumOfMethods = AllMethods.size();
       Iterator<Map.Entry<String, String>> it = AllMethods.entrySet().iterator();
       String result ="[";
       while (it.hasNext())
       {
           Map.Entry<String, String> pair = it.next();
           result += "i8* bitcast (" + DataTypellvm(MySymbolTable.MethodReturnType(Name, pair.getKey())) + " " + Argumentsllvm(MySymbolTable.MethodParams(Name, pair.getKey())) + " @" + pair.getValue() + "." + pair.getKey() + " to i8*)";
           if(it.hasNext()) result += ", ";
       }
       result += "]";
       result = "[" + NumOfMethods.toString() + " x i8*] " + result;
       return result;
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

   public String Argumentsllvm(ArrayList<String> Arguments)
   {
        String result = "(i8*";
        for(String argument : Arguments)
        {
            result += "," + DataTypellvm(argument); // i can add a space here
        }
        result += ")*";
        return result;
   }

   public String ArgumentsDecllvm(String MethodName)
   {
        Map<String ,String> AllMethods = Methods.get(MethodName).NameMethodParams();
        Iterator<Map.Entry<String, String>> it = AllMethods.entrySet().iterator();
        String result ="i8* %this";
        if(it.hasNext()) result += ", ";
        while (it.hasNext())
        {
            Map.Entry<String, String> pair = it.next();
            result += DataTypellvm(pair.getValue()) + " %." + pair.getKey();
            if(it.hasNext()) result += ", ";
        }

        return result;
   }

   public ArrayList<String> InitializeArgumentsllvm(String MethodName)
   {
        Map<String ,String> AllMethods = Methods.get(MethodName).NameMethodParams();
        ArrayList<String> InitializedArg = new ArrayList<String>();
        Iterator<Map.Entry<String, String>> it = AllMethods.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<String, String> pair = it.next();
            InitializedArg.add("%" + pair.getKey() + " = alloca " + DataTypellvm(pair.getValue()));
            InitializedArg.add("store " + DataTypellvm(pair.getValue()) + " %." + pair.getKey() + ", " + DataTypellvm(pair.getValue()) + "* %" + pair.getKey());            
        }

        return InitializedArg;
   }

   public ArrayList<String> InitializeVariablesllvm(String MethodName)
   {
        Map<String ,String> AllVariables = Methods.get(MethodName).LocalVariables();
        ArrayList<String> InitializedVar = new ArrayList<String>();
        Iterator<Map.Entry<String, String>> it = AllVariables.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<String, String> pair = it.next();
            InitializedVar.add("%" + pair.getKey() + " = alloca " + DataTypellvm(pair.getValue()));
        }

        return InitializedVar;
   }

   public Integer MethodOffset(String MethodName)
   {
       return MethodsOffsetMap.get(MethodName);
   }

   public Integer VariableOffset(String VarName)
   {
       return VariablesOffsetMap.get(VarName);
   }

   public Integer GetNumOfMethods()
   {
       return NumOfMethods;
   }

}