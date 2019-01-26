import java.io.IOException;

public class ReadFromServer {

    public static void main(String[] args) throws InterruptedException, IOException {
        String message = "";
        for(String arg: args){
            message += arg + " ";
        }
        System.out.println(message);
        Thread.sleep(15000);
        Runtime.getRuntime().exec("taskkill /f /im cmd.exe");
    }

}
