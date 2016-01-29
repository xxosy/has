package com.has.data;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by YoungWon on 2015-12-15.
 */
public class ClientSocket {
    private String serverIP = "221.159.48.200";
    private int serverPort = 8888;
    private static ClientSocket instance;
    private Socket socket;
    private InputStream inputStream;
    private DataInputStream reader;
    private OutputStream outputStream;
    private DataOutputStream writer;

    ClientSocket() {
        try {
            socket = new Socket(serverIP, serverPort);
            Log.i("ClientSocket", "Connected");
            inputStream = socket.getInputStream();
            reader = new DataInputStream(inputStream);
            outputStream = socket.getOutputStream();
            writer = new DataOutputStream(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ClientSocket getInstance(){
        if(instance == null){
            instance = new ClientSocket();
        }
        return instance;
    }
    public InputStream getInputStream() {
        return inputStream;
    }
    public OutputStream getOutputStream() {
        return outputStream;
    }
    public String getServerRequest(String command){
        String result=null;
        command+="\n";
        try {
            writer.writeUTF(command);
            StringBuilder builder = new StringBuilder();
            String str;
            if((str = reader.readUTF()) != null) {
                builder.append(str);
            }
            result = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    public void onDestroy(){
        try {
            reader.close();
            writer.close();
            inputStream.close();
            outputStream.close();
            socket.close();
            socket = null;
            instance = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
