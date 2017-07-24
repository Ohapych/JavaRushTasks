package CHAT;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by ohape on 23.07.2017.
 */
public class ConsoleHelper {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

   public static void writeMessage(String message){
       System.out.println(message);
   }

   public static String readString(){
       String result = null;
       do {
           try {
               result = reader.readLine();


           } catch (Exception e) {
               System.out.println("Произошла ошибка при попытке ввода текста. Попробуйте еще раз.");
           }
       } while (result == null);
       return result;
    }

    public static int readInt(){
       Integer result = null;
       do {
           String line = readString();
           try {
               result = Integer.valueOf(line);
           } catch (NumberFormatException e){
               System.out.println("Произошла ошибка при попытке ввода числа. Попробуйте еще раз.");
           }
       } while (result == null);
       return result.intValue();
    }
}
