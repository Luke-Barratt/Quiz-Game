package com.quiz.lukebarratt;

import java.io.*;
import java.net.Socket;


public class Client {

    private String cMessage = "";
    private String sMessage = "";

    public void launchClient() {

        try {
            Socket socket = new Socket("localhost", 5036);
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            while (!sMessage.equals("finished")) {
                sMessage = dataInputStream.readUTF();

                if (sMessage.equals("finished")) {
                    System.out.println("All questions completed.\n");
                    break;
                }

                if (sMessage.equals("wait")) {
                    System.out.println("Please wait for all players to connect...");
                    continue;
                }

                if (sMessage.equals("firstname")) {
                    System.out.println("Welcome to the quiz!");
                    System.out.println("Enter your first name: ");
                } else if (sMessage.equals("secondname")) {
                    System.out.println("Enter your second name: ");
                } else if (sMessage.equals("age")) {
                    System.out.println("Enter your age: ");
                } else {
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

            dataOutputStream.close();
            dataInputStream.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public static void main(String[] args) {
        new Client().launchClient();
    }
}

