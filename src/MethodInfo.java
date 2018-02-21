import java.util.*;
public class MethodInfo
{
   String Name;
   String ParentName;
   String ReturnType;
   Map<String ,String> Arguments;
   Map<String ,String> Variables;

   public MethodInfo(String NewName, String NewParentName, String NewReturnType)
   {
       Name = NewName;
       ParentName = NewParentName;
       ReturnType = NewReturnType;
       Arguments = new LinkedHashMap<String ,String>();
       Variables = new LinkedHashMap<String ,String>();
   }

   public void AddArgument(String ArgName, String ArgType)
   {
       Arguments.put(ArgName, ArgType);
   }

   public void AddVariable(String VarName, String VarType)
   {
       Variables.put(VarName, VarType);
   }

   public boolean VariableExistsIntoMethod(String VarName)
   {
       return Variables.containsKey(VarName);
   }

   public boolean ArgumentExistsIntoMethod(String Argument)
   {
       return Arguments.containsKey(Argument);
   }

   public String VariableType(String VarName)
   {
       if(Arguments.containsKey(VarName))
           return Arguments.get(VarName);
       else if(Variables.containsKey(VarName))
           return Variables.get(VarName);
       else
           return null;

   }

   public ArrayList<String> MethodParams()
   {
       ArrayList<String> ParamsList = new ArrayList<String>(Arguments.values());
       return ParamsList;
   }

   public Map<String ,String> NameMethodParams()
   {
       return Arguments;
   }

   public Map<String ,String> LocalVariables()
   {
       return Variables;
   }

   public String MethodReturnType()
   {
       return ReturnType;
   }

   public void Print()
   {
        System.out.println("            ReturnType: " + ReturnType);
        System.out.println("            Arguments: ");
        Iterator<Map.Entry<String, String>> it = Arguments.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<String, String> pair = it.next();
            System.out.println("                " + pair.getValue() + ": " + pair.getKey());
        }

        System.out.println("            Variables: ");
        Iterator<Map.Entry<String, String>> it2 = Variables.entrySet().iterator();
        while (it2.hasNext())
        {
            Map.Entry<String, String> pair2 = it2.next();
            System.out.println("                " + pair2.getValue() + ": " + pair2.getKey());
        }

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

}