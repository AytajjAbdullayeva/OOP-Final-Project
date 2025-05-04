package DAO;

import logging.Logger;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

public class UserDAO {
    private static ArrayList<User> users = new ArrayList<>();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static int numberOfUsers = 0;

    public static int getNumberOfUsers() {return numberOfUsers;}
    public static void setNumberOfUsers(int number) {numberOfUsers=number;}

    public static ArrayList<User> getUsers() {return new ArrayList<>(users);} // we are making a copy so it wouldn't affect the original one

    public static boolean loadUserDatabase() {
        Path dir = Paths.get("main/Databases/");
        Path file = dir.resolve("Users.json");
        users.clear();

        try {
            Files.createDirectories(dir);

            if(!Files.exists(file)) {
                mapper.writeValue(file.toFile(), users);
                HoneyIndexDAO.clean();
            }

            users = mapper.readValue(file.toFile(), new TypeReference<ArrayList<User>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            Logger.DebugLog("UserDAO: Error loading users from database");
            return false;
        }
        finally {
            numberOfUsers = users.size();
        }
        Logger.DebugLog("UserDAO: Loaded " + numberOfUsers + " users");
        return true;
    }

    public static boolean addUser(User user) {
        for(User u : users) {
            if(u.getUsername().equals(user.getUsername())) {
                return false;
            }
        }

        users.add(user);
        numberOfUsers++;
        try {
            mapper.writeValue(new File("main/Databases/Users.json"), users);

        } catch (IOException e) {
            e.printStackTrace();
            Logger.DebugLog("UserDAO: Error writing user with UID" + user.getUID() + "to database");
            return false;
        }
        Logger.DebugLog("UserDAO: Added user with UID " + user.getUID());
        return true;
    }

    public static boolean removeUser(int uid) {
        int index=-1;
        for(int i=0; i<users.size(); i++) {
            if(users.get(i).getUID() == uid) {
                index = i;
                break;
            }
        }
        if(index==-1) {
            Logger.DebugLog("UserDAO: User with UID " + uid + " not found");
            return false;
        }
        User u=users.get(index);
        users.remove(index);
        numberOfUsers--;
        try {
            mapper.writeValue(new File("main/Databases/Users.json"),users);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.DebugLog("Error writing new users arraylist to database");
            return false;
        }
        if(!HoneyIndexDAO.removeHoneyIndex(u.getUID())) {
            Logger.DebugLog("UserDAO: User with UID " + uid + " Honey index could not be removed");
            return false;
        }
        Logger.DebugLog("UserDAO: User with UID " + uid + " successfully removed");
        return true;
    }

    public static User findUserbyUID(int id) {
        for(User user : users) {
            if (user.getUID() == id) {
                Logger.DebugLog("User with ID " + id + " found");
                return user;
            }
        }
        Logger.DebugLog("User with ID " + id + " not found");
        return null;
    }

    public static User findUserByUsername(String username) {
        for(User user : users) {
            if (user.getUsername().equals(username)) {
                Logger.DebugLog("User with Username " + username + " found");
                return user;
            }
        }
        Logger.DebugLog("User with Username " + username + " not found");
        return null;
    }
}
