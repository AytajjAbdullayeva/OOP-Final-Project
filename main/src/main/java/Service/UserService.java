import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class UserService {

    public static boolean checkHash(String username, byte[] passhash) { // checks if our password hash is our actual password
        User user = UserDAO.findUserByUsername(username);
        if (user == null) {
            Logger.DebugLog(String.format("User %s not found", username));
            return false;
        }
        Logger.DebugLog(String.format("User %s found", username));

        int index=HoneyIndexDAO.getpassIndex(user.getUID());
        if(Arrays.equals(user.getHashPassw()[index],passhash))
        {
            Logger.DebugLog(String.format("User %s's hash matched", username));
            return true;
        }
        for(int i=0;i<user.getHashPassw().length;i++) {
            if (passhash == user.getHashPassw()[i]) {
                Logger.DebugLog("OUR DATABASE IS LEAKED!, FLARK!");
            }
        }
        return false;
    }

    public static boolean addUser(String name, String surname, int age, boolean gender, String username, String password) {
        User user = new User(name, surname, age, gender, username, password);
        return UserDAO.addUser(user);
    }

    public static boolean updateUserDetails(String name, String surname, int age, boolean gender, String username) { // Username is not being changed
        User user = UserDAO.findUserByUsername(username);
        if (user == null) {
            Logger.DebugLog(String.format("User %s not found", username));
            return false;
        }
        user.setSurname(surname);
        user.setAge(age);
        user.setGender(gender);
        user.setUsername(username);
        user.setName(name);
        Logger.DebugLog(String.format("User %s updated", username));
        return true;
    }
    public static boolean updateUserUsername(String oldUsername, String newUsername) {
        User user = UserDAO.findUserByUsername(oldUsername);
        if (user == null) {
            Logger.DebugLog(String.format("User %s not found", oldUsername));
            return false;
        }
        user.setUsername(newUsername);
        Logger.DebugLog(String.format("User %s updated", oldUsername));
        return true;
    }
    public static boolean updateUserPassword(String username,byte[] oldPassword, byte[] newPassword) {
        User user = UserDAO.findUserByUsername(username);
        if (user == null) {
            Logger.DebugLog(String.format("User %s not found", username));
            return false;
        }
        int index=HoneyIndexDAO.getpassIndex(user.getUID());
        if(Arrays.equals(user.getHashPassw()[index], oldPassword))
        {
            byte[][] temp = user.getHashPassw();
            temp[index]=newPassword;
            user.setHashPassw(temp);
            Logger.DebugLog(String.format("User %s' password updated", username));
            return true;
        }
        Logger.DebugLog(String.format("User %s' password is false", username));
        return false;
    }

    public static String getUserDetails(String username) {
        User user = UserDAO.findUserByUsername(username);
        if (user == null) {
            Logger.DebugLog(String.format("User %s not found", username));
            return null;
        }
        String result = "Name: " + user.getName() + "\nSurname: " + user.getSurname();
        result += "\nAge: " + Integer.toString(user.getAge()) + "\nUID: " + Integer.toString(user.getUID());
        result += "\nUsername: " + username;
        if(user.getGender()) result += "\nGender: Male";
        else result += "\nGender: Female";
        Logger.DebugLog(String.format("User %s's details is shared", username));
        return result;
    }

    public static boolean deleteUser(String username) {
        User user = UserDAO.findUserByUsername(username);
        if (user == null) {
            Logger.DebugLog(String.format("User %s not found", username));
            return false;
        }
        return UserDAO.removeUser(user.getUID());
    }
}
