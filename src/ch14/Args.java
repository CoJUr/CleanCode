package ch14;
//package ch14;
//
//import java.util.Map;
//import java.util.*;
//
//import static ch14.ArgsException.ErrorCode.INVALID_ARGUMENT_FORMAT;
//import static ch14.ArgsException.ErrorCode.INVALID_ARGUMENT_NAME;
////import static com.objectmentor.utilities.args.ArgsException.ErrorCode.*;
//
//
//public class Args {
//
//    private Map<Character, ArgumentMarshaler> marshalers;
//    private Set<Character> argsFound;
//    private ListIterator<String> currentArgument;
//
//    public Args(String schema, String[] args) throws ArgsException {
//        marshalers = new HashMap<Character, ArgumentMarshaler>();
//        argsFound = new HashSet<Character>();
//
//        parseSchema(schema);
//        parseArgumentStrings(Arrays.asList(args));
//    }
//
//    private void parseSchema(String schema) throws ArgsException {
//        for (String element : schema.split(","))
//            if (element.length() > 0)
//                parseSchemaElement(element.trim());
//    }
//
//    private void parseSchemaElement(String element) throws ArgsException {
//        char elementId = element.charAt(0);
//        String elementTail = element.substring(1);
//        validateSchemaElementId(elementId);
//
//        if (elementTail.length() == 0)
//            marshalers.put(elementId, new BooleanArgumentMarshaler());
//        else if (elementTail.equals("*"))
//            marshalers.put(elementId, new StringArgumentMarshaler());
//        else if (elementTail.equals("#"))
//            marshalers.put(elementId, new IntegerArgumentMarshaler());
//        else if (elementTail.equals("##"))
//            marshalers.put(elementId, new DoubleArgumentMarshaler());
//        else if (elementTail.equals("[*]"))
//            marshalers.put(elementId, new StringArrayArgumentMarshaler());
//        else
//            throw new ArgsException(INVALID_ARGUMENT_FORMAT, elementId, elementTail);
//    }
//
//    private void validateSchemaElementId(char elementId) throws ArgsException {
//        if (!Character.isLetter(elementId))
//            throw new ArgsException(INVALID_ARGUMENT_NAME, elementId, null);
//    }
//
//    private void parseArgumentStrings(List<String> argsList) throws ArgsException {
//        for (currentArgument = argsList.listIterator(); currentArgument.hasNext();)
//        {
//            String argString = currentArgument.next();
//            if (argString.startsWith("-"))
//                parseArgumentCharacters(argString.substring(1));
//            else {
//                currentArgument.previous();
//                break;
//            }
//        }
//    }
//
//    private void parseArgumentCharacters(String argChars) throws ArgsException {
//        for (int i = 0; i < argChars.length(); i++)
//            parseArgumentCharacter(argChars.charAt(i));
//    }
//
//    private void parseArgumentCharacter(char argChar) throws ArgsException {
//        ArgumentMarshaler m = marshalers.get(argChar);
//        if (m == null)
//            throw new ArgsException(UNEXPECTED_ARGUMENT, argChar, null);
//        else {
//            argsFound.add(argChar);
//            try {
//                m.set (currentArgument);
//            } catch (ArgsException e) {
//                e.setErrorArgumentId(argChar);
//                throw e;
//            }
//        }
//    }
//
//    public boolean has(char arg) {
//        return argsFound.contains(arg);
//    }
//
//    public int nextArgument() {
//        return currentArgument.nextIndex();
//    }
//
//    public boolean getBoolean(char arg) {
//        return BooleanArgumentMarshaler.getValue(marshalers.get(arg));
//    }
//
//    public String getString(char arg) {
//        return StringArgumentMarshaler.getValue(marshalers.get(arg));
//    }
//
//    public int getInt(char arg) {
//        return IntegerArgumentMarshaler.getValue(marshalers.get(arg));
//    }
//
//    public double getDouble(char arg) {
//        return DoubleArgumentMarshaler.getValue(marshalers.get(arg));
//    }
//
//    public String[] getStringArray(char arg) {
//        return StringArrayArgumentMarshaler.getValue(marshalers.get(arg));
//    }
//
//}

import java.util.*;

public class Args {
//    earlier version only booleans work

    private String schema;
    private String[] args;
    private boolean valid = true;
    private Set<Character> unexpectedArguments = new TreeSet<Character>();
    private Map<Character, Boolean> booleanArgs = new HashMap<Character, Boolean>();
    private int numberOfArguments = 0;

    public Args(String schema, String[] args) {
        this.schema = schema;
        this.args = args;
        valid = parse();
    }

    public boolean isValid() {
        return valid;
    }

    private boolean parse() {
        if (schema.length() == 0 && args.length == 0)
            return true;
        parseSchema();
        parseArguments();
        return unexpectedArguments.size() == 0;
    }

    private boolean parseSchema() {
        for (String element : schema.split(",")) {
            parseSchemaElement(element);
        }
        return true;
    }

    private void parseSchemaElement(String element) {
        if (element.length() == 1)
            parseBooleanSchemaElement(element);
    }

    private void parseBooleanSchemaElement(String element) {
        char c = element.charAt(0);
        if (Character.isLetter(c))
            booleanArgs.put(c, false);
    }

    private boolean parseArguments() {
        for (String arg : args) {
            parseArgument(arg);
        }
        return true;
    }

    private void parseArgument(String arg) {
        if (arg.startsWith("-"))
            parseElements(arg);
    }

    private void parseElements(String arg) {
        for (int i = 1; i < arg.length(); i++)
            parseElement(arg.charAt(i));
    }

    private void parseElement(char argChar) {
        if (isBoolean(argChar)) {
            numberOfArguments++;
            setBooleanArg(argChar, true);
        } else
            unexpectedArguments.add(argChar);
    }

    private void setBooleanArg(char argChar, boolean value) {
        booleanArgs.put(argChar, value);
    }

    private boolean isBoolean(char argChar) {
        return booleanArgs.containsKey(argChar);
    }

    public int cardinality() {
        return numberOfArguments;
    }

    public String usage() {
        if (schema.length() > 0)
            return "-[" + schema + "]";
        else
            return "";
    }

    public String errorMessage() {
        if (unexpectedArguments.size() > 0) {
            return unexpectedArgumentMessage();
        } else
            return "";
    }

    private String unexpectedArgumentMessage() {
        StringBuffer message = new StringBuffer("Argument(s) -");
        for (char c : unexpectedArguments) {
            message.append(c);
        }
        message.append(" unexpected.");

        return message.toString();
    }

    public boolean getBoolean(char arg) {
        return booleanArgs.get(arg);
    }

}




