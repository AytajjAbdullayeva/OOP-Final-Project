import javax.swing.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class SessionManager {
    private static ArrayList<Session> sessions = new ArrayList<Session>();

    public static void addSession(String username)
    {
        Session session = new Session(username);
        sessions.add(session);
        Logger.DebugLog("Session created");
    }
    public static boolean removeSession(String username)
    {
        int index=-1;
        for(int i=0; i<sessions.size(); i++)
        {
            if(sessions.get(i).getUserName()==username)
            {
                index=i;
                break;
            }
        }
        if(index==-1)
        {
            Logger.DebugLog("Session not found for username: "+username);
            return false;
        }
        int sessionID=sessions.get(index).getSessionID();
        sessions.remove(index);
        Logger.DebugLog("Session removed with sessionid: "+Integer.toString(sessionID));
        return true;
    }

    public static LocalDateTime StartTime(String username)
    {
        int index=-1;
        for(int i=0; i<sessions.size(); i++)
        {
            if(sessions.get(i).getUserName()==username)
            {
                index=i;
                break;
            }
        }
        if(index==-1)
        {
            Logger.DebugLog("Session not found for username: "+username);
            return null;
        }
        return sessions.get(index).getCreationTime();
    }
}
