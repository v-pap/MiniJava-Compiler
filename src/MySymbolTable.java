import java.util.*;
public class MySymbolTable
{
    static Map<String ,ClassInfo> Table;

   public static void emit(String line)
   {
        System.out.println(line);
   }
   
   public MySymbolTable()
   {
       Table = new LinkedHashMap<String ,ClassInfo>();
   }

   public static void AddClass(String ClassName, String ParentName)
   {
       Table.put(ClassName, new ClassInfo(ClassName, ParentName));
   }

   public static void AddVariableToClass(String ClassName, String VarName, String VarType)
   {
       Table.get(ClassName).AddVariable(VarName, VarType);
   }

   public static void AddMethodToClass(String ClassName, String MethodName, String ReturnType)
   {
       Table.get(ClassName).AddMethod(MethodName, ReturnType);
   }

   public static void AddArgumentToMethod(String ClassName, String MethodName, String Argument, String Type)
   {
       Table.get(ClassName).AddArgumentToMethod(MethodName, Argument, Type);
   }
   
   public static void AddVariableToMethod(String ClassName, String MethodName, String VarName, String Type)
   {
       Table.get(ClassName).AddVariableToMethod(MethodName, VarName, Type);
   }

   public static boolean ClassExists(String ClassName)
   {
       return Table.containsKey(ClassName);
   }

   public static boolean VariableExists(String ClassName, String VarName)
   {
       return Table.get(ClassName).VariableExists(ClassName, VarName);
   }
   
   public static boolean MethodExists(String ClassName, String MethodName)
   {
       return Table.get(ClassName).MethodExists(ClassName, MethodName);
   }

   public static boolean MethodExistsExtended(String ClassName, String MethodName)
   {
        String TempName = ClassName;
        while(TempName != null && !Table.get(TempName).MethodExists(TempName, MethodName))
            TempName = Table.get(TempName).GetParentClass();
        
        if(TempName == null)
            return false;
        else
            return true;
   }

   public static boolean VariableExistsIntoMethod(String ClassName, String MethodName, String VarName)
   {
       return Table.get(ClassName).VariableExistsIntoMethod(MethodName, VarName);
   }

   public static boolean ArgumentExistsIntoMethod(String ClassName, String MethodName, String Argument)
   {
       return Table.get(ClassName).ArgumentExistsIntoMethod(MethodName, Argument);
   }

   public static String VariableType(String ClassName, String MethodName, String VarName)
   {
       String TypeResult = Table.get(ClassName).VariableType(MethodName, VarName);
       if(TypeResult != null)
       {
           return TypeResult;
       }

       String TempName = ClassName;
       while(TempName != null && !VariableExists(TempName, VarName))
           TempName = Table.get(TempName).GetParentClass();
        
       if(TempName == null)
           return null;
       else
           return Table.get(TempName).VariableType(null, VarName);
   }

   public static ArrayList<String> MethodParams(String ClassName, String MethodName)
   {
        String TempName = ClassName;
        while(TempName != null && !Table.get(TempName).MethodExists(TempName, MethodName))
            TempName = Table.get(TempName).GetParentClass();
        
        if(TempName == null)
            return null;
        else
            return Table.get(TempName).MethodParams(MethodName);
   }

   public static String MethodReturnType(String ClassName, String MethodName)
   {
        String TempName = ClassName;
        while(TempName != null && !Table.get(TempName).MethodExists(TempName, MethodName))
            TempName = Table.get(TempName).GetParentClass();
        
        if(TempName == null)
            return null;
        else
            return Table.get(TempName).MethodReturnType(MethodName);
   }

   public static boolean MethodisValid(String ClassName, String MethodName)
   {
        String TempName = Table.get(ClassName).GetParentClass();
        while(TempName != null && !Table.get(TempName).MethodExists(TempName, MethodName))
            TempName = Table.get(TempName).GetParentClass();
        
        if(TempName == null)
            return true;
        else
            return CompareMethods(TempName, ClassName, MethodName);
   }

   public static boolean CompareMethods(String ParentClass, String ClassName, String MethodName)
   {
       ArrayList<String> ParentClassParams = MethodParams(ParentClass, MethodName);
       ArrayList<String> ClassParams = MethodParams(ClassName, MethodName);
       if(ParentClassParams.size() != ClassParams.size())
            return false;
       boolean Result = true;
       for(int i = 0; i < ParentClassParams.size(); i++) 
       {
            if(!ClassParams.get(i).equals(ParentClassParams.get(i)))
            {
                Result = false;
                break;
            }
       }
       String ReturnTypeParent = MethodReturnType(ParentClass, MethodName);
       String ReturnType = MethodReturnType(ClassName, MethodName);
       if(!ReturnTypeParent.equals(ReturnType))
            Result = false;
       return Result;
   }

   public static boolean isParent(String ParentClass, String ClassName)
   {
       if(isPrimitive(ParentClass) || isPrimitive(ClassName))
            return false;
       String TempName = ClassName;
       while(TempName != null && !ParentClass.equals(TempName))
            TempName = Table.get(TempName).GetParentClass();
        
        if(TempName == null)
            return false;
        else
            return true;
   }

   public static boolean isPrimitive(String DataType)
   {
       if(DataType.equals("int") || DataType.equals("int []") || DataType.equals("boolean"))
            return true;
       else
            return false;
   }

   public static void Print()
   {
       Iterator<Map.Entry<String, ClassInfo>> it = Table.entrySet().iterator();
       while (it.hasNext())
       {
           Map.Entry<String, ClassInfo> pair = it.next();
           System.out.println("Class: " + pair.getKey());
           pair.getValue().Print();
       }
   }

   public static int GetParentClassVarOffset(String ParentClass)
   {
        return Table.get(ParentClass).VariablesOffset;
   }

   public static int GetParentClassMethodOffset(String ParentClass)
   {
        return Table.get(ParentClass).MethodsOffset;
   }

   public static void PrintOffset()
   {
       Iterator<Map.Entry<String, ClassInfo>> it = Table.entrySet().iterator();
       it.next();
       while (it.hasNext())
       {
           Map.Entry<String, ClassInfo> pair = it.next();
           pair.getValue().PrintOffset();
       }
   }

   public static boolean MethodExistsParent(String ParentClassName, String MethodName)
   {
        String TempName = ParentClassName;
        while(TempName != null && !Table.get(TempName).MethodExists(TempName, MethodName))
            TempName = Table.get(TempName).GetParentClass();
        
        if(TempName == null)
            return false;
        else
            return true;
   }

   public static void Vtable()
   {
       Iterator<Map.Entry<String, ClassInfo>> it = Table.entrySet().iterator();
       while (it.hasNext())
       {
           Map.Entry<String, ClassInfo> pair = it.next();
           //System.out.println("Class: " + pair.getKey());
           emit("@." + pair.getKey() + "_vtable = global " + pair.getValue().VtableMethods());
           //pair.getValue().Print();
       }
   }

   public static Integer ObjectSize(String ClassName)
   {
      Integer DataSize = (Table.get(ClassName)).GetDataSize();
      return DataSize + 8;  //plus 8 for the vtable
   }

   public static Integer GetMethodOffset(String ClassName, String MethodName)
   {
      String TempName = ClassName;
      while(TempName != null && (!Table.get(TempName).MethodExists(TempName, MethodName) || Table.get(TempName).MethodOffset(MethodName) == -1))
          TempName = Table.get(TempName).GetParentClass();
      return Table.get(TempName).MethodOffset(MethodName);
   }

   public static Integer GetVariableOffset(String ClassName, String VarName)
   {
      String TempName = ClassName;
      while(TempName != null && !Table.get(TempName).VariableExists(TempName, VarName))
          TempName = Table.get(TempName).GetParentClass();
      return Table.get(TempName).VariableOffset(VarName);
   }

   public static String ArgumentsDecllvm(String ClassName, String MethodName)
   {
      return Table.get(ClassName).ArgumentsDecllvm(MethodName);
   }

   public static String GetNumOfMethods(String ClassName)
   {
      return Table.get(ClassName).GetNumOfMethods().toString();
   }

   public static ArrayList<String> InitializeArgumentsllvm(String ClassName, String MethodName)
   {
      return Table.get(ClassName).InitializeArgumentsllvm(MethodName);
   }

   public static ArrayList<String> InitializeVariablesllvm(String ClassName, String MethodName)
   {
      return Table.get(ClassName).InitializeVariablesllvm(MethodName);
   }

}