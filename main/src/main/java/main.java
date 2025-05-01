import com.fasterxml.jackson.databind.ObjectMapper;


public class main {
    public static void main(String[] args) {
<<<<<<< Updated upstream
        System.out.println("Hello World");
        ObjectMapper mapper = new ObjectMapper();
=======
        // Load databases
        UserDAO.loadUserDatabase();
        HoneyIndexDAO.loadHoneyDatabase();

        System.out.println("=== SIGNING UP USERS ===");
        UserController.SignUp("Alice", "Smith", 25, false, "alice", "alicepass");
        UserController.SignUp("Bob", "Brown", 30, true, "bob", "bobpass");
        UserController.SignUp("Charlie", "White", 22, true, "charlie", "charliepass");

        System.out.println("=== LOGIN TESTS ===");
        System.out.println("Login Alice: " + UserController.Login("alice", "alicepass")); // true
        System.out.println("Login Bob (wrong password): " + UserController.Login("bob", "wrongpass")); // false

        System.out.println("=== GET USER DETAILS ===");
        System.out.println(UserController.GetUserDetails("charlie"));

        System.out.println("=== CHANGE DETAILS ===");
        UserController.ChangeUserDetails("Alice", "Johnson", 26, false, "alice");
        System.out.println(UserController.GetUserDetails("alice"));

        System.out.println("=== CHANGE USERNAME ===");
        UserController.ChangeUserName("charlie", "charles");
        System.out.println(UserController.GetUserDetails("charles"));

        System.out.println("=== CHANGE PASSWORD ===");
        System.out.println("Old password valid: " + UserController.Login("bob", "bobpass"));
        UserController.ChangePassword("bob", "bobpass", "newbobpass");
        System.out.println("New password valid: " + UserController.Login("bob", "newbobpass"));
        System.out.println("Old password invalid: " + UserController.Login("bob", "bobpass"));

        System.out.println("=== DELETE ACCOUNT ===");
        System.out.println("Delete Alice (wrong password): " + UserController.DeleteAccount("alice", "wrongpass"));
        System.out.println("Delete Alice (correct password): " + UserController.DeleteAccount("alice", "alicepass"));

        System.out.println("Remaining Users:");
        for (User u : UserDAO.getUsers()) {
            System.out.println("- " + u.getUsername() + " (UID: " + u.getUID() + ")");
        }

        System.out.println("=== SYSTEM TEST COMPLETED ===");
>>>>>>> Stashed changes
    }
}
