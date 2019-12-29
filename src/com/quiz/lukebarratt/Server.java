package com.quiz.lukebarratt;

import java.util.Map;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws Exception {
        XMLParser xmlparser = new XMLParser("questionaire.xml");

        Map<Integer, Question> questions = xmlparser.getXmlParser();
        int players = xmlparser.getNoOfPlayers();

        System.out.println("Number of players required to start the quiz: " + players);

        ClientHandler[] playerThreads = new ClientHandler[players];

        try {
            ServerSocket serverSocket = new ServerSocket(5036);
            int playerCounter = 1;
            while (true) {
                Socket server = serverSocket.accept();
                System.out.println("Player " + playerCounter + " has joined the server");

                playerThreads[playerCounter] = new ClientHandler(server, playerCounter, questions);
                playerCounter++;
                if(playerCounter == players){
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        for (ClientHandler playerThread : playerThreads){
            playerThread.start();
        }

        for (ClientHandler playerThread : playerThreads) {
            playerThread.join();
        }

        int max = -1;
        String player = null;

        for (ClientHandler playerThread : playerThreads) {
            if (playerThread.getPlayerScore() > max) {
                max = playerThread.getPlayerScore();
                player = playerThread.getFullName();
            }
        }
        for (ClientHandler playerThread : playerThreads) {
            playerThread.sendMessage(player + " has won the quiz!");
        }

        System.out.println("Exiting game...");

    }
}
