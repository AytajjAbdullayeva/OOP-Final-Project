package controller;

import DAO.UserDAO;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserControllerTest {
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
    public void TestLogin() {
        User user = new User("TestUser","Surtest",26,true,"TestUserName","TestPassword");
        UserDAO.addUser(user);
        boolean result = UserController.Login("TestUserName","TestPassword");
        assertTrue(result);
    }

    @Test
    public void TestChangePassword()
    {
        User user = new User("TestUser","Surtest",26,true,"TestUserName","TestPassword");
        UserDAO.addUser(user);
        boolean result = UserController.ChangePassword(user.getUsername(),"TestPassword","TestPassword1");
        assertTrue(result);
        assertTrue(UserController.Login("TestUserName","TestPassword1"));
    }


}
