package ua.setko.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Artem Setko 
 */
public class MainClass extends Thread {

    private static int port;
    private static Server server;

    public static void main(String[] args) {

        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Wrong port number. Using default port 8080");
                port = 8080;
            }
        } else {
            port = 8080;
        }
        new MainClass().start();
        System.out.println("Server starting at port " + port);
        System.out.println("To stop the server type <s>");
        try {
            server = new Server(port);
            server.start();
        } catch (BindException e) {
            System.err.println("Starting Failed! Port " + port + " is busy");
            System.exit(1);
        } catch (Exception ex) {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Expect the stop command in console
    @Override
    public void run() {
        while (true) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                String request = null;
                try {
                    request = reader.readLine();
                } catch (IOException e) {
                }
                if (request != null) {
                    if (request.equalsIgnoreCase("s")) {
                        System.out.println("Stopping the server...");
                        server.stop();
                        System.out.println("Server stopped");
                        System.exit(0);
                    }
                }
                try {
                    Thread.sleep(60);
                } catch (InterruptedException e) {
                }
            } catch (IOException e) {
            }

        }
    }
}
