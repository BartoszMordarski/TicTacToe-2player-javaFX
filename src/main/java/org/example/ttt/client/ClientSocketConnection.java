package org.example.ttt.client;

import org.example.ttt.model.Command;

import java.io.*;
import java.net.Socket;

public class ClientSocketConnection {
    public static Socket socket;
    public static ObjectOutputStream objectOutputStream;
    public static ObjectInputStream objectInputStream;
    public static BufferedReader bufferedReader;
    public static BufferedWriter bufferedWriter;


    public static void initialize() {
        try {
            socket = new Socket("localhost", 2222);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            close();
        }

    }

    public static void sendCommandMessage(Command command) {
        try {
            objectOutputStream.writeObject(command);
        } catch (IOException e) {
            System.out.println("exception while sending command");
            close();
        }
    }

    public static Command readCommandMessage(){
        try {
            return (Command)objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("exception while receiving command");
            close();
            return null;
        }
    }


    public static void close() {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}