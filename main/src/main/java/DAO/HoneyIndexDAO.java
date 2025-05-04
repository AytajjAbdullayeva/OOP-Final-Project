package DAO;

import Logging.Logger;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.HoneyIndex;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class HoneyIndexDAO {
    static private ArrayList<HoneyIndex> honeyIndexList = new ArrayList<HoneyIndex>();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static boolean loadHoneyDatabase()
    {
        Path path = Paths.get("Databases/");
        Path filePath = path.resolve("HoneyIndex.json");

        try {
            Files.createDirectories(path);

            if (!Files.exists(filePath)) {
                mapper.writeValue(filePath.toFile(), honeyIndexList);
            }

            honeyIndexList = mapper.readValue(filePath.toFile(),new TypeReference<ArrayList<HoneyIndex>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            Logger.DebugLog("HoneyIndexDAO: Database couldn't be loaded failed");
            return false;
        }
        Logger.DebugLog("HoneyIndexDAO: Database loaded successfully");
        return true;
    }

    public static boolean addHoneyInex(HoneyIndex honeyIndex)
    {
        honeyIndexList.add(honeyIndex);
        try {

            mapper.writeValue(new File("Databases/HoneyIndex.json"),honeyIndexList);
            Logger.DebugLog("HoneyIndexDAO: HoneyIndex added successfully");
        } catch (IOException e) {
            e.printStackTrace();
            Logger.DebugLog("HoneyIndexDAO: HoneyIndex couldn't be added");
            return false;
        }
        return true;
    }

    public static boolean removeHoneyIndex(int UID)
    {
        int index = -1;
        for(int i=0; i<honeyIndexList.size(); i++)
        {
            if(honeyIndexList.get(i).getUID() == UID)
            {
                index = i;
                break;
            }
        }


        if(index==-1)
        {
            Logger.DebugLog("HoneyIndexDAO: HoneyIndex not found");
            return false;
        }
        honeyIndexList.remove(index);

        try {
            mapper.writeValue(new File("Databases/HoneyIndex.json"),honeyIndexList);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.DebugLog("HoneyIndexDAO: HoneyIndex arraylist couldn't be added after deletion");
            return false;
        }
        return true;
    }

    public static int getpassIndex(int UID)
    {
        for(HoneyIndex honeyIndex : honeyIndexList)
        {
            if(honeyIndex.getUID() == UID)
            {
                Logger.DebugLog("HoneyIndexDAO: HoneyIndex found");
                return honeyIndex.getIndex();
            }
        }
        Logger.DebugLog("HoneyIndexDAO: HoneyIndex not found");
        return -1;
    }

    public static boolean clean() // for clearing the database
    {
        honeyIndexList.clear();
        try {

            mapper.writeValue(new File("Databases/HoneyIndex.json"),honeyIndexList);
            Logger.DebugLog("HoneyIndexDAO: Database deleted successfully");
        } catch (IOException e) {
            e.printStackTrace();
            Logger.DebugLog("HoneyIndexDAO: Database couldn't be deleted");
            return false;
        }
        return true;
    }
}
