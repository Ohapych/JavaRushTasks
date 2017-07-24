package CHAT.client;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ohape on 24.07.2017.
 */
public class BotClient extends Client {


    protected String getUserName() {

        int number = (int)(Math.random()*100);
       String name = "date_bot_"+number;
        return name;
    }

    @Override
    protected boolean shouldSendTextFromConsole() {
        return false;
    }

    @Override
    protected SocketThread getSocketThread() {
        return new BotSocketThread();
    }

    public class BotSocketThread extends SocketThread{

        @Override
        protected void processIncomingMessage(String message) {
            super.processIncomingMessage(message);
            if (message.contains(":")) {
                String senderName = message.substring(0, message.indexOf(":"));
                String textMessage = message.substring(message.indexOf(":") + 2);
                switch (textMessage) {
                    case "дата":
                        sendTextMessage(String.format("Информация для %s: %s", senderName,
                                new SimpleDateFormat("d.MM.YYYY").format(Calendar.getInstance().getTime())));
                        break;
                    case "день":
                        sendTextMessage(String.format("Информация для %s: %s", senderName,
                                new SimpleDateFormat("d").format(Calendar.getInstance().getTime())));
                        break;
                    case "месяц":
                        sendTextMessage(String.format("Информация для %s: %s", senderName,
                                new SimpleDateFormat("MMMM").format(Calendar.getInstance().getTime())));
                        break;
                    case "год":
                        sendTextMessage(String.format("Информация для %s: %s", senderName,
                                new SimpleDateFormat("YYYY").format(Calendar.getInstance().getTime())));
                        break;
                    case "время":
                        sendTextMessage(String.format("Информация для %s: %s", senderName,
                                new SimpleDateFormat("H:mm:ss").format(Calendar.getInstance().getTime())));
                        break;
                    case "час":
                        sendTextMessage(String.format("Информация для %s: %s", senderName,
                                new SimpleDateFormat("H").format(Calendar.getInstance().getTime())));
                        break;
                    case "минуты":
                        sendTextMessage(String.format("Информация для %s: %s", senderName,
                                new SimpleDateFormat("m").format(Calendar.getInstance().getTime())));
                        break;
                    case "секунды":
                        sendTextMessage(String.format("Информация для %s: %s", senderName,
                                new SimpleDateFormat("s").format(Calendar.getInstance().getTime())));
                        break;
                }
            }
        }



        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            String text = "Привет чатику. Я бот. Понимаю команды: дата, день, месяц, год, время, час, минуты, секунды.";
            sendTextMessage(text);
            super.clientMainLoop();

    }
    }

    public static void main(String[] args) {

        new BotClient().run();

    }

}
