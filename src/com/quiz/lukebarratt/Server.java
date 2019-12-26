package com.quiz.lukebarratt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {

        //server is listening on port 5036
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
    }
}
