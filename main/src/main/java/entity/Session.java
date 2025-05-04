import java.time.LocalDateTime;
import java.util.Random;

public class Session {
    private String username;
    private LocalDateTime createdAt;
    private int sessionID;

    public Session(String username)
    {
        this.username = username;
        this.createdAt = LocalDateTime.now();
        this.sessionID = new Random().nextInt(10000);
    }

    public String getUserName() {return username;}
    public LocalDateTime getCreationTime() {return createdAt;}
    public int getSessionID() {return sessionID;}
}
