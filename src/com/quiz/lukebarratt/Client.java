package com.quiz.lukebarratt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {

        try {
            Scanner scanner = new Scanner(System.in);

            //getting localhost ip address
            InetAddress ip = InetAddress.getByName("localhost");

            //establish the connection with server port 5036
            Socket socket = new Socket(ip, 5036);

            //obtaining input and output streams
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            //following loop performs the exchange of information between client and client handler
            while (true) {
                System.out.println(dataInputStream.readUTF());
                String toSend = scanner.nextLine();
                dataOutputStream.writeUTF(toSend);

                if (toSend.equals("Exit")) {
                    System.out.println("Closing this connection: " + socket);
                    socket.close();
                    System.out.println("Connection closed");
                    break;
                } else {
                    System.out.println(dataInputStream.readUTF());
                    toSend = scanner.nextLine();
                    dataOutputStream.writeUTF(toSend);

                    if (toSend.equals("Exit")) {
                        System.out.println("Closing this connection: " + socket);
                        socket.close();
                        System.out.println("Connection closed");
                        break;
                    }
                }
            }

            scanner.close();
            dataInputStream.close();
            dataOutputStream.close();

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
