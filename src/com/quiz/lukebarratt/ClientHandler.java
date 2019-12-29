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
            //ASK PLAYER FOR FIRST NAME
            dataOutputStream.writeUTF("firstname");
            dataOutputStream.flush();

            //TAKE INPUT FROM CLIENT AND STORE IN 'firstName' VARIABLE
            String clientInput = dataInputStream.readUTF().trim();
            firstName = clientInput;

            //ASK PLAYER FOR SECOND NAME
            dataOutputStream.writeUTF("secondname");
            dataOutputStream.flush();

            //TAKE INPUT FROM CLIENT AND STORE IN 'secondName' VARIABLE
            clientInput = dataInputStream.readUTF().trim();
            secondName = clientInput;

            //ASK PLAYER FOR AGE
            dataOutputStream.writeUTF("age");
            dataOutputStream.flush();

            //TAKE INPUT FROM CLIENT AND STORE IN 'age' VARIABLE
            clientInput = dataInputStream.readUTF().trim();
            age = clientInput;

            System.out.println(firstName + " " + secondName + "has joined.");
            System.out.println("please wait for the game to begin...");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run () {
        try {
            String clientInput = "";
            for (Map.Entry<Integer, Question> questionEntry : questions.entrySet()) {
                Question question = questionEntry.getValue();

                String optionList = "";

                for (Map.Entry<Integer, String> option : question.getOptions().entrySet()) {
                    optionList += String.format("\t%d.%s\n", option.getKey(), option.getValue());
                }
                dataOutputStream.writeUTF(String.format("%d.%s\n%s", question.getQuestionID(), question.getText(), optionList));
                dataOutputStream.flush();

                clientInput = dataInputStream.readUTF().trim();

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
            System.out.println("Player " + numOfPlayers + " has finished");
        }
    }

    public int getPlayerScore () {
        return playerScore;
    }

    public String getFullName () {
        return firstName + " " + secondName;
    }

    public void sendMessage (String message){
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
}
