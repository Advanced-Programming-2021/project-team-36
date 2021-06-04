package edu.sharif.nameless.in.seattle.yugioh.utils;

import edu.sharif.nameless.in.seattle.yugioh.model.enums.Color;

public class CustomPrinter {
    private static String lastBuffer;

    static {
        lastBuffer = "";
    }

    public static String getLastBuffer(){
        String cp = lastBuffer;
        lastBuffer = "";
        return cp;
    }
    public static void println(Object o, Color color){
        String ret = o.toString();
        lastBuffer += ret + "\n";
        System.out.println(getANSI(color) + ret + getANSI(Color.Default));
    }
    public static void println(){
        lastBuffer += "\n";
        System.out.println();
    }
    private static String getANSI(Color color) {
        if (color.equals(Color.Black))
            return "\u001B[30m";
        else if (color.equals(Color.Red))
            return "\u001B[31m";
        else if (color.equals(Color.Green))
            return "\u001B[32m";
        else if (color.equals(Color.Yellow))
            return "\u001B[33m";
        else if (color.equals(Color.Blue))
            return "\u001B[34m";
        else if (color.equals(Color.Purple))
            return "\u001B[35m";
        else if (color.equals(Color.Cyan))
            return "\u001B[36m";
        else
            return "\u001B[0m";
    }
}
