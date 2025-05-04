import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {

    @BeforeEach
    public void cleanBeforeEachTest() {
        try {
            Files.deleteIfExists(Paths.get("Databases/Users.json")); // fayli silir
            UserDAO.loadUserDatabase();
            UserDAO.setNumberOfUsers(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAddUser() {
        User user = new User("TestUser","Surtest",26,true,"TestUserName","TestPassword");
        boolean result = UserDAO.addUser(user);
        assertEquals(1,UserDAO.getNumberOfUsers());
        assertEquals("TestUser",UserDAO.getUsers().get(0).getName());
        assertTrue(result);
    }

    @Test
    public void TestremoveUser() {
        User user = new User("TestUser","Surtest",26,true,"TestUserName","TestPassword");
        UserDAO.addUser(user);
        int uid=user.getUID();
        boolean result = UserDAO.removeUser(uid);
        assertEquals(0,UserDAO.getNumberOfUsers());
    }

    @Test
    public void TestfindUserbyUID()
    {
        User user = new User("TestUser","Surtest",26,true,"TestUserName","TestPassword");
        UserDAO.addUser(user);
        int uid=user.getUID();
        User foundUser = UserDAO.findUserbyUID(uid);
        assertNotEquals(null, foundUser);
        assertEquals(user.getUsername(),foundUser.getUsername());
    }

    @Test
    public void TestfindUserByUsername()
    {
        User user = new User("TestUser","Surtest",26,true,"TestUserName","TestPassword");
        UserDAO.addUser(user);
        int uid=user.getUID();
        User foundUser = UserDAO.findUserByUsername(user.getUsername());
        assertNotEquals(null, foundUser);
        assertEquals(user.getUID(),foundUser.getUID());
    }

};;
