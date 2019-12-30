package com.quiz.lukebarratt;

import java.util.Map;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws Exception {

        //Create an instance of the XMLParser class and pass in the questions file
        XMLParser xmlparser = new XMLParser("questionnaire.xml");

        //Call the readQuestions method to read the xml file
        Map<Integer, Question> questions = xmlparser.getXmlParser();

        //Variable to store the number of players the quiz requires to start.
        int players = xmlparser.getNoOfPlayers();

        System.out.println("Number of players required to start the quiz: " + players);

        ClientHandler[] playerThreads = new ClientHandler[players];

        try {
            //server set up to listen for client connections on port 5036.
            ServerSocket serverSocket = new ServerSocket(5036);
            //variable to track number of players connected to the server.
            int playerCounter = 0;
            //Infinite loop to wait for players to connect to the server.
            while (true) {
                Socket server = serverSocket.accept();
                System.out.println("Player " + playerCounter + " has joined the server");

                playerThreads[playerCounter] = new ClientHandler(server, playerCounter, questions);
                playerCounter++;
                //Once maximum number of players is reached break out the loop.
                if(playerCounter == players){
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        //Start threads for each player.
        for (ClientHandler playerThread : playerThreads){
            playerThread.start();
        }
        //Wait for current thread to execute before starting new player thread.
        for (ClientHandler playerThread : playerThreads) {
            playerThread.join();
        }


        int max = -1;
        String player = null;

        //Store player name with highest score in player variable.
        for (ClientHandler playerThread : playerThreads) {
            if (playerThread.getPlayerScore() > max) {
                max = playerThread.getPlayerScore();
                player = playerThread.getFullName();
            }
        }
        //Display the winners name for all client so all player know who has won.
        for (ClientHandler playerThread : playerThreads) {
            playerThread.sendMessage(player + " has won the quiz!");
        }

        System.out.println("Exiting game...");

    }
}
