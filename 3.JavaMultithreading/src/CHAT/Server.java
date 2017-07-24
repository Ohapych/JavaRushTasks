package CHAT;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ohape on 23.07.2017.
 */
public class Server {
    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    public static void sendBroadcastMessage(Message message){
        for (Connection connection: connectionMap.values()){
            try {
                connection.send(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Введите номер порта");
        try(  ServerSocket serverSocket = new ServerSocket(ConsoleHelper.readInt())) {


            System.out.println("Сервер запущен");
            System.out.println(serverSocket.getInetAddress());
            while (true) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
                if (socket != null) {
                    new Handler(socket).start();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static class Handler extends Thread{
        private Socket socket;

       private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException{
           String userName = null;
           boolean recieved = false;
        do {
            connection.send(new Message(MessageType.NAME_REQUEST)); //send name request to client
            Message message = connection.receive(); // receive name

           if (message.getType().equals(MessageType.USER_NAME)&& !message.getData().isEmpty()){
               userName = message.getData();
               if (!connectionMap.containsKey(userName)){
                   connectionMap.put(userName, connection);
                   connection.send(new Message(MessageType.NAME_ACCEPTED));
                   recieved = true;
               }
           }} while (!recieved);
            return userName;
       }

        public Handler(Socket socket){
            this.socket = socket;
        }

       private void sendListOfUsers(Connection connection, String userName) throws IOException{
            for (String target : connectionMap.keySet()){
                if (!target.equals(userName))connection.send(new Message(MessageType.USER_ADDED,target));
            }
       }

       private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException {

           while (true){
               Message recievedMessage = connection.receive();
           if (recievedMessage.getType()==MessageType.TEXT){
               String text = userName+": "+recievedMessage.getData();
               sendBroadcastMessage(new Message(MessageType.TEXT,text));
           }
           else System.out.println("Ошибочка: сообщение  - не !текст!");
       }


       }

    public void run(){
        Connection connection = null;
        String userName = null;
            try {

                System.out.println(socket.getRemoteSocketAddress()+" - присоединился к чату");
                connection = new Connection(socket);
                userName = serverHandshake(connection);
                sendBroadcastMessage(new Message(MessageType.USER_ADDED,userName));
                sendListOfUsers(connection,userName);
                serverMainLoop(connection,userName);


            }catch (Exception e){
               // e.printStackTrace();
                connectionMap.remove(userName);
                sendBroadcastMessage(new Message(MessageType.USER_REMOVED,userName));
                System.out.println("Соединение c"+socket.getRemoteSocketAddress()+" : "+ userName +" закрыто");

            }finally {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

    }

    }
}
