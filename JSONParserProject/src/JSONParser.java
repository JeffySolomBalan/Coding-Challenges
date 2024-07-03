import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Stack;

public class JSONParser
{

    public static void main(String[] args)
    {
        String fileName = null;
        boolean valid = false;
        fileName = "C:\\Documents\\Personal\\Workspaces\\Coding_Challenge_Workspace\\CC#2\\tests\\step1\\valid.json"; // Specify the file path
        valid = validateJSONFile(fileName);
        printResult(fileName, valid);
        
        fileName = "C:\\Documents\\Personal\\Workspaces\\Coding_Challenge_Workspace\\CC#2\\tests\\step1\\invalid.json"; // Specify the file path
        valid = validateJSONFile(fileName);
        printResult(fileName, valid);
        
        fileName = "C:\\Documents\\Personal\\Workspaces\\Coding_Challenge_Workspace\\CC#2\\tests\\step2\\invalid.json"; // Specify the file path
        valid = validateJSONFile(fileName);
        printResult(fileName, valid);
        
        fileName = "C:\\Documents\\Personal\\Workspaces\\Coding_Challenge_Workspace\\CC#2\\tests\\step2\\valid.json"; // Specify the file path
        valid = validateJSONFile(fileName);
        printResult(fileName, valid);
        
        fileName = "C:\\Documents\\Personal\\Workspaces\\Coding_Challenge_Workspace\\CC#2\\tests\\step2\\invalid2.json"; // Specify the file path
        valid = validateJSONFile(fileName);
        printResult(fileName, valid);
        
        fileName = "C:\\Documents\\Personal\\Workspaces\\Coding_Challenge_Workspace\\CC#2\\tests\\step2\\valid2.json"; // Specify the file path
        valid = validateJSONFile(fileName);
        printResult(fileName, valid);
        
        fileName = "C:\\Documents\\Personal\\Workspaces\\Coding_Challenge_Workspace\\CC#2\\tests\\step3\\valid.json"; // Specify the file path
        valid = validateJSONFile(fileName);
        printResult(fileName, valid);
        
        fileName = "C:\\Documents\\Personal\\Workspaces\\Coding_Challenge_Workspace\\CC#2\\tests\\step3\\invalid.json"; // Specify the file path
        valid = validateJSONFile(fileName);
        printResult(fileName, valid);
        
        fileName = "C:\\Documents\\Personal\\Workspaces\\Coding_Challenge_Workspace\\CC#2\\tests\\step4\\invalid.json"; // Specify the file path
        valid = validateJSONFile(fileName);
        printResult(fileName, valid);
        
        fileName = "C:\\Documents\\Personal\\Workspaces\\Coding_Challenge_Workspace\\CC#2\\tests\\step4\\valid.json"; // Specify the file path
        valid = validateJSONFile(fileName);
        printResult(fileName, valid);
        
        fileName = "C:\\Documents\\Personal\\Workspaces\\Coding_Challenge_Workspace\\CC#2\\tests\\step4\\valid2.json"; // Specify the file path
        valid = validateJSONFile(fileName);
        printResult(fileName, valid);

        fileName = "C:\\Documents\\Personal\\Workspaces\\Coding_Challenge_Workspace\\CC#2\\tests\\step5\\valid.json"; // Specify the file path
        valid = validateJSONFile(fileName);
        printResult(fileName, valid);
    }

    private static void printResult(String fileName, boolean valid)
    {
        System.out.println("");
        System.out.println(
                "file is " + (valid ? "Valid " : "InValid ") + fileName);
    }

    private static boolean validateJSONFile(String fileName)
    {
        String json = null;
        json = getDataFromSingleLineFile(fileName);
        if (json == null)
        {
            json = convertToSingleLine(fileName);
        }
        if (!validateJSON(json))
        {
            return false;
        }
        return true;
    }

    private static boolean validateJSON(String json)
    {
        if (!parseStartAndEndOfTheJSON(json))
        {
            return false;
        }
        json = trimFirstAndLastChar(json);

        if (json.trim().length() <= 0)
        {
            return true; //Empty JSON
        }
        if (!parseKeyAndValue(json))
        {
            return false;
        }
        return true;
    }

    private static boolean parseKeyAndValue(String json)
    {
        if (json.endsWith(","))
        {
            return false;
        }
        StringBuilder key = new StringBuilder();
        StringBuilder value = new StringBuilder();
        int i = 0;
        while (i < json.length())
        {
            for (; i < json.length(); i++)
            {
                if (json.charAt(i) == ':')
                {
                    break;
                }
                else
                {
                    if (json.charAt(i) == ',')
                    {
                        continue;
                    }
                    key.append(json.charAt(i));
                }
            }
            i++;
            if (!validateKey(key.toString()))
            {
                return false;
            }
            i = buildValue(json, value, i);
            if (!validateValue(value.toString().trim()))
            {
                return false;
            }
            i++;
            key = new StringBuilder();
            value = new StringBuilder();
        }

        return true;
    }

    private static int buildValue(String json, StringBuilder value, int i)
    {
        boolean isArray = false;
        boolean isObject = false;
        Stack<Character> stack = new Stack<>();
        for (; i < json.length(); i++)
        {
            if (json.charAt(i) == '[')
            {
                value.append(json.charAt(i));
                isArray = true;
                stack.push(json.charAt(i));
            }
            else if (isArray && json.charAt(i) == ']')
            {
                value.append(json.charAt(i));
                stack.pop();
                if(stack.size() == 0)
                {
                    break;    
                }
            }
            else if (json.charAt(i) == '{')
            {
                value.append(json.charAt(i));
                isObject = true;
                stack.push(json.charAt(i));
            }
            else if (isObject && json.charAt(i) == '}')
            {
                value.append(json.charAt(i));
                stack.pop();
                if(stack.size() == 0)
                {
                    break;    
                }
            }
            else if (!isObject && !isArray && json.charAt(i) == ',')
            {
                break;
            }
            else
            {
                value.append(json.charAt(i));
            }
        }
        return i;
    }

    private static boolean validateKey(String key)
    {
        return key.trim().startsWith("\"") && key.trim().endsWith("\"");
    }

    private static boolean validateValue(String value)
    {
        value = value.trim();
        if (value.equals("null"))
        {
            return true;
        }
        else if (validateValueAsString(value))
        {
            return true;
        }
        else if (validateValueAsNumber(value))
        {
            return true;
        }
        else if (validateValueAsBoolean(value))
        {
            return true;
        }
        else if (isValueAnArray(value))
        {
            return true;
        }
        else if (isValueAnObject(value))
        {
            return true;
        }
        return false;
    }

    private static boolean isValueAnObject(String value)
    {
        if (!value.startsWith("{"))
        {
            return false;
        }
        if (value.startsWith("{") && value.endsWith("}"))
        {
            if (!validateJSON(value))
            {
                return false;
            }
        }
        return true;
    }

    private static boolean isValueAnArray(String value)
    {
        value = value.trim();
        if (!value.startsWith("["))
        {
            return false;
        }
        if (value.startsWith("[") && value.endsWith("]"))
        {
            value = trimFirstAndLastChar(value);
            if (value.endsWith(","))
            {
                return false;
            }
            if (value.trim().length() <= 0)
            {
                return true; //Empty JSON
            }
            if(isArrayOfObject(value))
            {
                if(!segregateObjectByObjectAndParse(value))
                {
                    return false;
                }
            }
            else
            {
                String[] array = value.split(",");
                for (String item : array)
                {
                    if (!validateValue(item))
                    {
                        return false;
                    }
                }    
            }
        }
        return true;
    }

    private static boolean segregateObjectByObjectAndParse(String value)
    {
        StringBuilder object = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < value.length(); i++)
        {
            switch(value.charAt(i))
            {
                case '{':
                {
                    stack.push(value.charAt(i));
                    object.append(value.charAt(i));
                    break;
                }
                case '}':
                {
                    stack.pop();
                    object.append(value.charAt(i));
                    if(stack.size() == 0)
                    {
                        if(!validateJSON(object.toString().trim()))
                        {
                            return false;
                        }
                        object = new StringBuilder();
                        continue;
                    }
                    break;
                }
                case ',':
                    if(stack.size()>0)
                    {
                        object.append(value.charAt(i));    
                    }
                    break;
                default : 
                    object.append(value.charAt(i));
                    break;
            }
        }
        return true;
    }

    private static boolean isArrayOfObject(String value)
    {
        return value.startsWith("{");
    }

    private static boolean validateValueAsBoolean(String value)
    {
        return value.equals("true") || value.equals("false");
    }

    private static boolean validateValueAsNumber(String value)
    {
        return value.trim().matches("\\d{1,}");
    }

    private static boolean validateValueAsString(String value)
    {
        return value.trim().startsWith("\"") && value.trim().endsWith("\"");
    }

    private static String trimFirstAndLastChar(String json)
    {
        //trim root level brackets
        json = json.substring(1, json.length());
        json = json.substring(0, json.length() - 1);
        return json;
    }

    private static boolean parseStartAndEndOfTheJSON(String json)
    {
        return json.length() > 0 && json.charAt(0) == '{'
                && json.charAt(json.length() - 1) == '}';
    }

    private static String getDataFromSingleLineFile(String fileName)
    {
        try
        {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            if (lines != null && lines.size() == 1)
            {
                return lines.get(0);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static String convertToSingleLine(String fileName)
    {
        List<String> lines;
        StringBuilder json = new StringBuilder();
        try
        {
            lines = Files.readAllLines(Paths.get(fileName));
            if (lines != null && lines.size() > 0)
            {
                for (String line : lines)
                {
                    json.append(line.trim());
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return json.toString();
    }

}
