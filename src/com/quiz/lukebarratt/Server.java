package com.quiz.lukebarratt;

import java.util.Map;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        XMLParser xmlparser = new XMLParser("questionaire.xml");

        Map<Integer, Question> questions = xmlparser.getXMLParser();
        int players = xmlparser.getNumOfPlayers();

        System.out.println("Number of players: " + players);

        ClientHandler[] playerThreads = new ClientHandler[players];

        try {
            ServerSocket serverSocket = new ServerSocket(5036);
            int playerCounter = 0;
            while (true) {
                Socket server = serverSocket.accept();
                System.out.println("Player " + playerCounter + " has joined the server");

                playerThreads[playerCounter] = new ClientHandler(server, playerCounter, questions);
                playerCounter++;
                if(playerCounter == players){
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (ClientHandler playerThread : playerThreads){
            playerThread.start();
        }

        for (ClientHandler playerThread : playerThreads){
            playerThread.join();
        }

        int max = -1;
        String player = null;

        for (ClientHandler playerThread : playerThreads) {
            playerThread.sendFinalMessage(player + " has won the quiz!");
        }

        System.out.println("Exiting game...");

    }
/*        //server is listening on port 5036
        ServerSocket serverSocket = new ServerSocket(5036);
        //running infinite lop to wait for client server request
        while (true) {
            Socket socket = null;

            try {
                //socket object to receive incoming client requests
                socket = serverSocket.accept();
                System.out.println("New player connected: " + socket);

                //obtaining input and output streams
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                System.out.println("Player assigned to a new thread");

                //create a new thread object
                Thread thread = new ClientHandler(socket, dataInputStream, dataOutputStream);
                //start the client handler
                thread.start();

            } catch (Exception e) {
                socket.close();
                e.printStackTrace();
            }
        }
    }*/
}
