package Array_List;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * TODO1.
 * Mikey Arias
 * Prof. Kurt Johnson
 * CSE383
 * Lab2 S24
 * Client for cse383 Lab2/HW2
 * This client will: communicate with a server
 */
public class socketClient {

    int port = 0;
    String hostname = "";
    String muid = "";
    String formula="";

    Socket socket = null;
    PrintWriter out = null;

    BufferedReader in = null;
    private static Logger LOGGER = Logger.getLogger("info");
    FileHandler fh = null;



    /**
      main method - DO NO WORK IN PSVM (Public Static Void Main)
      just invoke other classes
      invocation java PGM serverName serverPort Values...
     */
    public static void main(String[] args) {

        int port = 0;
        String hostname = "";
        String muid = "";

        // Helper code to store all of the arguments after
        // 0(muid) 1(IP Address) and 2 (Port)
        java.util.ArrayList<String> values = new java.util.ArrayList<String>();
        for (int i = 3;i < args.length;i++) {
            values.add(args[i]);
        }

	try {
		// Replace with correct information from assignment
		muid = "ariasm";
		hostname = "172.17.14.146";
		port = 5002;

                /* get muid, hostname (IP/Hostname) and port from command line (uncomment next 3 lines)
	
		*/
	} catch (Exception e) {

	}

        // used so we can access a non-static method from a static one
        socketClient client = new socketClient(muid,port,hostname);
        client.process(values);
    }

    //Constructor
    socketClient(String muid,int port, String hostname) {

        // Set up logging
        try {
            fh = new FileHandler("client.log");
            LOGGER.addHandler(fh);
            LOGGER.setUseParentHandlers(false);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

        } catch (IOException err) {
            System.err.println("Error - can't open log file");
            //continue here since this is not a blocking error
        }

        LOGGER.info("ClientMAIN - Port = " + port + " hostname = " + hostname);
        this.hostname = hostname;
        this.port = port;
        this.muid = muid;
    }

    /**
      process - takes an arraylist of arguments
        each argument is type, number.
     */
    public void process(java.util.ArrayList<String> values) {
        boolean result = false;
        String greeting = "";
        String response = "";
        for (int retry = 1; retry > 0 && !result; retry--) {
            try {
                LOGGER.info("Connecting");
                connect();
                //  Send your MUID (not the empty string)
		sendString("");

                greeting = readResponse();
		sendValues(values);
                response = readResponse();
                result = true;
                socket.close();
            } catch (IOException err) {
		System.out.println(err.toString());
                LOGGER.log(Level.SEVERE,"error during connection", err);
            }
        }
        if (result) {
            System.out.println("Success");
            System.out.println("Greeting => " + greeting);

	    /*  Uncomment lines below
            System.out.println("Formula  => " + formula);
            System.out.println("Answer   => " + response);
	    */

        } else {
            System.out.println("Failed");
        }
    }

    /**
        connects to server.
     */
    public void connect() throws IOException {
        //
        // MAKE THE SOCKET CONNECTION AND SET BufferedReader and PrintWriter
        //
        LOGGER.log(Level.INFO,"Connect => hostname = " + hostname + " port = " + port);
        socket = new Socket(hostname,port);
        //  set timeout to 5 seconds
        /*  set tcp no delay
        socket.setSoTimeout();
        socket.setTcpNoDelay();
	*/

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	out = new PrintWriter(socket.getOutputStream(), true);
    }



    /**
        send values to server.
     */
    public void sendValues(java.util.ArrayList<String> values)
        throws IOException {
        // System.out.println("In sendvalues");
        java.util.Iterator<String> itr = values.iterator();
        while (itr.hasNext()) {
	    String data=itr.next();

	    /*  uncomment line below and add the current element being sent to formula
	    	formula +=
	    */
	    sendString(data);
        }
        sendString("=");


    }


    /**
      send string values to server.
     */
    public void sendString(String data) throws IOException {

	out.write(data + "\n");
	out.flush();

    }

    /**
      read response.
     */
    public String readResponse() throws IOException {
        String response = "";
        response = in.readLine();
        return response;
    }
}
