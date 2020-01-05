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

    //Declare variable to hold number of players and the player scores.
    private int numOfPlayers;
    private int playerScore;

    //Getter to return value of player score variable.
    public int getPlayerScore () {
        return playerScore;
    }

    //Method to get players full name.
    public String getFullName () {
        return firstName + " " + secondName;
    }

    //Declare variable to store players first name, last name and age.
    private String firstName;
    private String secondName;
    private String age;

    private Map<Integer, Question> questions;

    ClientHandler(Socket cliSocket, int playerScore, Map<Integer, Question> questions) {
        server = cliSocket;
        numOfPlayers = playerScore;
        this.playerScore = 0;
        this.questions = questions;

        try {
            dataInputStream = new DataInputStream(server.getInputStream());
            dataOutputStream = new DataOutputStream(server.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            //Set server message to "firstname"
            //Ask for player first name.
            dataOutputStream.writeUTF("firstname");
            dataOutputStream.flush();

            //Take input from client and store in firstName variable.
            String clientInput = dataInputStream.readUTF().trim();
            firstName = clientInput;

            //Set server message to "secondname"
            //Ask player for second name.
            dataOutputStream.writeUTF("secondname");
            dataOutputStream.flush();

            //Take input from client and store in secondName variable.
            clientInput = dataInputStream.readUTF().trim();
            secondName = clientInput;

            //Set server message to "age".
            //Ask player for age.
            dataOutputStream.writeUTF("age");
            dataOutputStream.flush();

            //Take input from client and store in age variable.
            clientInput = dataInputStream.readUTF().trim();
            age = clientInput;

            //Print out client first and last name to server.
            System.out.println(firstName + " " + secondName + " " + "(" + age + ")" + " has joined.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run () {
        try {
            String clientInput = "";
            //
            for (Map.Entry<Integer, Question> questionEntry : questions.entrySet()) {
                Question question = questionEntry.getValue();

                String optionList = "";

                //Get values for each option and store in optionList variable.
                for (Map.Entry<Integer, String> option : question.getOptions().entrySet()) {
                    optionList += String.format("\t%d.%s\n", option.getKey(), option.getValue());
                }

                //Output question and options list to client
                dataOutputStream.writeUTF(String.format("%d.%s\n%s", question.getQuestionID(), question.getText(), optionList));
                dataOutputStream.flush();

                //Get player input (answer to current question).
                clientInput = dataInputStream.readUTF().trim();

                //If client input is the same as the answerID for the current question increment player score by 1.
                if (!clientInput.equals("timeout")) {
                    if (clientInput.equals(String.valueOf(question.getAnswerID()))) {
                        playerScore++;
                    }
                }
            }
            dataOutputStream.writeUTF("finished");
            dataOutputStream.flush();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            //print out number assigned to player when they have finished answering.
            System.out.println("Player " + numOfPlayers + " has finished");
        }
    }

    //Method to send message to client.
    public void sendMessage (String message){
        try {
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
            //close resources
            dataInputStream.close();
            dataOutputStream.close();
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
