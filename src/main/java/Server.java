import controller.ProgramController;
import utils.CustomPrinter;
import utils.CustomScanner;
import utils.Debugger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
    private static ProgramController controller;
    private static ServerSocket Server;
    private static BufferedReader inFromClient;

    public static void runUntilNoInput() throws IOException {
        try {
            controller.control();
        } catch (Error error) {
            if (!error.getMessage().equals("end of test"))
                throw error;
        }
    }

    public static void run(String data) throws IOException {
        CustomScanner.injectString(data);
        runUntilNoInput();
    }

    public static void main(String[] args) throws IOException {
        controller = new ProgramController();
        Server = new ServerSocket (5000);
        System.out.println ("TCPServer Waiting for client on port 5000");
        Debugger.setTestMode(true);
        Socket connected = Server.accept();

        System.setOut(new PrintStream(connected.getOutputStream()));
        inFromClient = new BufferedReader(new InputStreamReader(connected.getInputStream()));
        while(true) {
            System.out.println( " THE CLIENT"+" "+ connected.getInetAddress() +":"+connected.getPort()+" IS CONNECTED ");
            while ( true ) {
                run(inFromClient.readLine());
                System.out.println("tamaaam");
            }
        }
    }
}