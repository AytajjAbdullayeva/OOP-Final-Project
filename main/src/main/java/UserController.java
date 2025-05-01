import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserController {
    public static boolean Login(String username, String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            return UserService.checkHash(username, md.digest(password.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Logger.DebugLog("Error while checking hash");
            return false;
        }
    }

    public static boolean SignUp(String name, String surname, int age, boolean gender, String username, String password) {
        return UserService.addUser(name, surname, age, gender, username, password);
    }

    public static boolean DeleteAccount(String username, String password) {
        if(Login(username, password)) return UserService.deleteUser(username);
        else return false;
    }

    public static boolean ChangeUserDetails(String name, String surname, int age, boolean gender, String username) {
        return  UserService.updateUserDetails(name, surname, age, gender, username);
    }

    public static boolean ChangePassword(String UserName ,String oldPassword, String newPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            return UserService.updateUserPassword(UserName,md.digest(oldPassword.getBytes()),md.digest(newPassword.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            Logger.DebugLog("Error while checking hash");
            return false;
        }
    }

    public static boolean ChangeUserName(String oldUsername, String newUsername) {
        return UserService.updateUserUsername(oldUsername, newUsername);
    }

    public static String GetUserDetails(String username) {
        return UserService.getUserDetails(username);
    }
}
