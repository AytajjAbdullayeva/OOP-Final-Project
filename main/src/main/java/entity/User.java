import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class User {
    private String name,surname;
    private int age;
    private int UID;
    private boolean gender;
    private String username;
    private byte[][] hashPassw = new byte[3][32]; // we will use 3 different hashes, 1 being correct. This will trick hackers and if database is leaked we will know



    public int getAge() {return age;}
    public void setAge(int age) {this.age = age;}

    public String getSurname() {return surname;}
    public void setSurname(String surname) {this.surname = surname;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public int getUID() {return UID;}
    public void setUID(int UID) {this.UID = UID;}

    public boolean getGender() {return gender;}
    public void setGender(boolean gender) {this.gender = gender;}

    public byte[][] getHashPassw() {return hashPassw;}
    public void setHashPassw(byte[][] hashPassw) {this.hashPassw = hashPassw;}

    User(String name, String surname, int age, boolean gender, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.gender = gender;
        this.username = username;
        this.UID = UserDAO.getNumberOfUsers()+1;

        try {
            MessageDigest md= MessageDigest.getInstance("SHA-256");
            int index = new Random().nextInt(3);

            hashPassw[0]=md.digest(("random"+new Random().nextInt()).getBytes()); // all the passwords are some random hashes
            hashPassw[1]=md.digest(("random"+new Random().nextInt()).getBytes());
            hashPassw[2]=md.digest(("random"+new Random().nextInt()).getBytes());

            hashPassw[index] = md.digest(password.getBytes());
            HoneyIndex honey = new HoneyIndex(UID,index); // we are adding the real index of password to our honeyIndex database
            HoneyIndexDAO.addHoneyInex(honey);


            Logger.DebugLog("Password Hashed");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        finally {
            Logger.DebugLog("User named" + username + "created, UID" + UID);
        }
    }
    public User() {};//for ObjectMapper
}
