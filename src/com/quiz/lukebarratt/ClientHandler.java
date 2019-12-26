package com.quiz.lukebarratt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPlayerAge(String playerAge) {
        this.playerAge = playerAge;
    }

    //Declare variables to store player name and age
    String playerName;
    String playerAge;
    String received;

    final DataInputStream dataInputStream;
    final DataOutputStream dataOutputStream;
    final Socket socket;

    //Constructor
    public ClientHandler(Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        this.socket = socket;
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
    }

    @Override
    public void run() {
        //Infinite loop
        while (true) {
            try {
                //Ask user for their name
                dataOutputStream.writeUTF("Enter your name: \n" + "Type 'Exit' to terminate");
                //Receive name as input from the user and store in playerName variable
                received = dataInputStream.readUTF();
                setPlayerName(received);
                System.out.println("Player name: " + playerName);

                if (received.equals("Exit")) {
                    System.out.println("Client " + this.socket + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.socket.close();
                    System.out.println("Connection closed");
                    break;
                } else {
                    //Ask user for their age
                    dataOutputStream.writeUTF("Enter your age: \n" + "Type 'Exit' to terminate");
                    //Receive name as input from the user and store in playerAge variable
                    received = dataInputStream.readUTF();
                    setPlayerAge(received);
                    System.out.println("Player age: " + playerAge);

                    if (received.equals("Exit")){
                        System.out.println("Client " + this.socket + " sends exit...");
                        System.out.println("Closing this connection.");
                        this.socket.close();
                        System.out.println("Connection closed");
                        break;
                    }
                }
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            try{
                this.dataInputStream.close();
                this.dataOutputStream.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
