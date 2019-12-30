package com.quiz.lukebarratt;

import java.io.*;
import java.net.Socket;


public class Client {
    //Declare variable to store client and server messages.
    private String cMessage = "";
    private String sMessage = "";

    public void launchClient() {

        try {
            //Establish client connection to host
            Socket socket = new Socket("localhost", 5036);
            //Obtaining input and output streams.
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            //Setup buffered reader to read input from player.
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            //while loop to determine what happens next in the quiz depending on what string the server message
            //variable is set to. sMessage variable is determined in the ClientHandler class.
            while (!sMessage.equals("finished")) {
                //read the data output stream from the ClientHandler class and store value in server message variable.
                sMessage = dataInputStream.readUTF();

                //If server message is set to "finished" break out the loop
                if (sMessage.equals("finished")) {
                    System.out.println("All questions completed.\n");
                    break;
                }
                //If server message is set to "wait", make player continue to wait for all players to connect to the server.
                if (sMessage.equals("wait")) {
                    System.out.println("Please wait for all players to connect...");
                    continue;
                }
                //If server message is set to "firstname", welcome player to game and request player
                //to enter their name.
                if (sMessage.equals("firstname")) {
                    System.out.println("Welcome to the quiz!");
                    System.out.println("Enter your first name: ");
                }
                //If server message is set to "secondname", request player to enter second name.
                else if (sMessage.equals("secondname")) {
                    System.out.println("Enter your second name: ");
                }
                //If server message is set to "age", request player to enter their age.
                else if (sMessage.equals("age")) {
                    System.out.println("Enter your age: ");
                }
                //Once player has entered first name, last name and age, request answer to current question displayed.
                else {
                    System.out.println(sMessage);
                    System.out.println("Please give an answer: ");
                }

                cMessage = userInput(bufferedReader);

                dataOutputStream.writeUTF(cMessage);
                dataOutputStream.flush();
                System.out.println();
            }

            sMessage = dataInputStream.readUTF();

            System.out.println(sMessage);
            System.out.println();

            //closing resources
            //close output stream
            dataOutputStream.close();
            //close input stream
            dataInputStream.close();
            //close socket
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Method to timeout player if no user input is receive in 30 seconds.
    public String userInput(BufferedReader in) throws IOException {
        long start = System.currentTimeMillis();
        while ((System.currentTimeMillis() - start) < 30 * 1000 && !in.ready()) {
        }
        String input;
        if (in.ready()) {
            input = in.readLine();
        } else {
            input = "timeout";
        }
        return input;
    }

    //Method to run client side
    public static void main(String[] args) {
        new Client().launchClient();
    }
}

