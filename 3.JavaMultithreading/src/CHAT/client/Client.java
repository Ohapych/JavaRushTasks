package CHAT.client;

import CHAT.Connection;
import CHAT.ConsoleHelper;
import CHAT.Message;
import CHAT.MessageType;
import com.javarush.task.task30.task3008.*;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by ohape on 23.07.2017.
 */
public class Client {
    protected Connection connection;
    private String userName;
    private volatile boolean clientConnected = false;

    protected String getServerAddress(){
        System.out.println("введите адрес сервера");
      return ConsoleHelper.readString();
    }

    protected int getServerPort(){
        System.out.println("введите номер порта");
       return ConsoleHelper.readInt();
    }

    protected String getUserName(){
        System.out.println("введите имя пользователя");
        userName = ConsoleHelper.readString();
        return userName;
    }

    protected boolean shouldSendTextFromConsole(){
        return true;
    }

    protected SocketThread getSocketThread(){
        return new SocketThread();
    }

    protected void sendTextMessage(String text){
        try {
            connection.send(new Message(MessageType.TEXT,text));
        } catch (IOException e) {
            e.printStackTrace();
           clientConnected = false;
        }
    }

    public void run(){
        try {

            SocketThread socketThread = getSocketThread();
            socketThread.setDaemon(true);
            socketThread.start();


            synchronized (this) {
                this.wait();

            }


        if (clientConnected) System.out.println("соединение установлено. Для выхода наберите команду ‘exit’.");
        else {
            System.out.println("Произошла ошибка во время работы клиента.");
            }

            while (clientConnected){
                String text = ConsoleHelper.readString();

                if (text.equals("exit")) {
                    connection.send(new Message(MessageType.USER_REMOVED,userName));
                    break;
                }
                else if (shouldSendTextFromConsole()) connection.send(new Message(MessageType.TEXT,text));
            }

    } catch (Exception e){
        e.printStackTrace();
    }
    }

    public class SocketThread extends Thread{

      protected   void processIncomingMessage(String message){
          System.out.println(message);
      }

       protected void informAboutAddingNewUser(String userName){
           System.out.printf("%s присоединился к чату\n",userName);
       }

       protected void informAboutDeletingNewUser(String userName){
           System.out.println(userName+" покинул чат");
       }

       protected void notifyConnectionStatusChanged(boolean clientConnected){

            synchronized (Client.this){
                Client.this.clientConnected = clientConnected;
                Client.this.notify();
            }
       }

      protected  void   clientHandshake() throws IOException, ClassNotFoundException{
           while (true){
               Message message = connection.receive();
               if (message.getType() == MessageType.NAME_REQUEST) connection.send(new Message(MessageType.USER_NAME,getUserName()));
               else if (message.getType() == MessageType.NAME_ACCEPTED) {
                   notifyConnectionStatusChanged(true);
                   break;
               } else {
                   throw new IOException("Unexpected MessageType");
               }
           }
      }

     protected void clientMainLoop() throws IOException, ClassNotFoundException{
           while (true){
           Message message = connection.receive();
           if (message.getType() == MessageType.TEXT) processIncomingMessage(message.getData());
           else if (message.getType() == MessageType.USER_ADDED) informAboutAddingNewUser(message.getData());
           else if (message.getType() == MessageType.USER_REMOVED) informAboutDeletingNewUser(message.getData());
           else throw new IOException("Unexpected MessageType");}
     }

     public void run(){
           try {

               String serverAddress = getServerAddress();
               int serverPort = getServerPort();
               Socket socket = new Socket(serverAddress, serverPort);
                connection = new Connection(socket);
                clientHandshake();
                clientMainLoop();

           }catch (IOException | ClassNotFoundException e){
               System.out.println("clientMainLoop exception");
               e.printStackTrace();
               notifyConnectionStatusChanged(false);


           }
     }


    }

    public static void main(String[] args) {
        new Client().run();
    }
}
