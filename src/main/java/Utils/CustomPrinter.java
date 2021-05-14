package Utils;


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
    public static void println(Object o){
        String ret = o.toString();
        lastBuffer += ret + "\n";
        System.out.println(ret);
    }
    public static void println(){
        lastBuffer += "\n";
        System.out.println();
    }
}
