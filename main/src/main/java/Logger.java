import java.nio.file.*;
import java.io.IOException;
import java.time.LocalDateTime;

public class Logger
{
    public static boolean DebugLog(String message)
    {
        Path path = Paths.get("logs/");
        Path dlog = path.resolve("log.txt");

        LocalDateTime now = LocalDateTime.now(); // adding time to our log message
        message=now.toString()+"-->"+message+"\n";


        try {
            Files.createDirectories(path); // will create our logs/directory if needed

            if(!Files.exists(dlog)) Files.write(dlog, message.getBytes()); // if file doesn't exists it will create
            else Files.write(dlog, message.getBytes(), StandardOpenOption.APPEND);


        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}