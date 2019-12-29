package com.quiz.lukebarratt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class ClientHandler extends Thread {

    private Socket server;

    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;

    private int numOfPlayers;
    private int playerScore;

    private String firstName;
    private String secondName;
    private String age;

    private Map<Integer, Question> questions;

    int i = 0;

    ClientHandler(Socket cliSocket, int playerScore, Map<Integer, Question> questions) {
        server = cliSocket;
        numOfPlayers = playerScore;
        this.playerScore = 0;
    }
    this.questions = questions;

    try {
        dataInputStream = new DataInputStream(server.getInputStream());
        dataOutputStream = new DataOutputStream(server.getOutputStream());
    } catch(IOException e) {
        e.printStackTrace();
    }

    try {
        //ASK PLAYER FOR FIRST NAME
        dataOutputStream.writeUTF("Enter first name: ");
        dataOutputStream.flush();

        //TAKE INPUT FROM CLIENT AND STORE IN 'firstName' VARIABLE
        String clientInput = dataInputStream.readUTF().trim();
        firstName = clientInput;

        //ASK PLAYER FOR SECOND NAME
        dataOutputStream.writeUTF("Enter second name: ");
        dataOutputStream.flush();

        //TAKE INPUT FROM CLIENT AND STORE IN 'secondName' VARIABLE
        clientInput = dataInputStream.readUTF().trim();
        secondName = clientInput;

        //ASK PLAYER FOR AGE
        dataOutputStream.writeUTF("Enter age: ");
        dataOutputStream.flush();

        //TAKE INPUT FROM CLIENT AND STORE IN 'age' VARIABLE
        clientInput = dataInputStream.readUTF().trim();
        age = clientInput;

        System.out.println("Hello " + firstName + " " + secondName);
        System.out.println("please wait for the game to begin...");

    } catch(IOException e) {
        e.printStackTrace();
    }

    @Override
    public void run(){
        try {
            String clientInput = "";
            for (Map.Entry<Integer, Question> questionEntry : questions.entrySet()) {
                Question question = questionEntry.getValue();

                String optionList = "";

                for (Map.Entry<Integer, String> option : question.getOptions().entrySet()) {
                    optionList += String.format("\t%d.%s\n", option.getKey(), option.getValue());
                }
                dataOutputStream.writeUTF(String.format("%d.%s\n%s", question.getQuestionID, question.getText(), optionList));
                dataOutputStream.flush();

                clientInput = dataInputStream.readUTF().trim();

                if (!clientInput.equals("timeout")) {
                    if (clientInput.equals(String.valueOf(question.getAnswerID()))) {
                        playerScore++;
                    }
                }
            }
            dataOutputStream.writeUTF("done");
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Player " + numOfPlayers + " has finished");
        }
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public String getFullName() {
        return firstName + " " + secondName;
    }

    public void sendMessage(String message) {
        try {
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
            dataInputStream.close();
            dataOutputStream.close();
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

/*    public void setPlayerName(String playerName) {
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
    }*/
}
