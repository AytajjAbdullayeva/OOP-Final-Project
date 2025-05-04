package service;

import DAO.HoneyIndexDAO;
import DAO.UserDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
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
    public void TestcheckHash()
    {
        User user = new User("TestUser","Surtest",26,true,"TestUserName","TestPassword");
        UserDAO.addUser(user);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] passhash= md.digest("TestPassword".getBytes());

            assertTrue(UserService.checkHash("TestUserName",passhash));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void TestupdateUserDetails()
    {
        User user = new User("TestUser","Surtest",26,true,"TestUserName","TestPassword");
        UserDAO.addUser(user);
        UserService.updateUserDetails("TestUser1","Surtest1",24,false,"TestUserName");
        assertEquals("TestUser1",UserDAO.findUserByUsername("TestUserName").getName());
        assertEquals(24,UserDAO.findUserByUsername("TestUserName").getAge());
        assertEquals("Surtest1",UserDAO.findUserByUsername("TestUserName").getSurname());
    }

    @Test
    public void TestUpdateUserUsername()
    {
        User user = new User("TestUser","Surtest",26,true,"TestUserName","TestPassword");
        UserDAO.addUser(user);
        int uid=user.getUID();
        UserService.updateUserUsername("TestUserName","TestUserUsername1");
        assertEquals("TestUserUsername1",UserDAO.findUserbyUID(uid).getUsername());
    }

    @Test
    public void TestupdateUserPassword()
    {
        User user = new User("TestUser","Surtest",26,true,"TestUserName","TestPassword");
        UserDAO.addUser(user);
        int uid=user.getUID();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] oldpsw = md.digest("TestPassword".getBytes());
            byte[] newpsw = md.digest("TestPassword2".getBytes());
            UserService.updateUserPassword(user.getUsername(),oldpsw,newpsw);

            assertEquals(newpsw,UserDAO.findUserbyUID(uid).getHashPassw()[HoneyIndexDAO.getpassIndex(user.getUID())]);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void TestdeleteUser()
    {
        User user = new User("TestUser","Surtest",26,true,"TestUserName","TestPassword");
        UserDAO.addUser(user);
        UserService.deleteUser("TestUserName");
        assertEquals(0, UserDAO.getNumberOfUsers());
    }

}
