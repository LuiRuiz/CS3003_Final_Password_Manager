import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;


public interface AESFileManager {
    // constants for key
    int KEY_SIZE = 128;
    int GCM_TAG_LENGTH = 128;

    String KEY_FILE= "key.txt";
    String IV_FILE = "iv.txt";
    String DATA_FILE = "passwords_encrypted.txt";

    /*
     *A function to generate the Key and IV for AES encryption that
     * should only run if KEY_FILE and IV_FILE doesnt exist;
     */

    static void generateKeyAndIV() throws Exception {
        File key_file = new File(KEY_FILE);
        File iv_file = new File(IV_FILE);

        if (key_file.isFile()){
            //System.out.println("Files found");
            return;
        }
        // generate AES key
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(KEY_SIZE);
        SecretKey key = keyGen.generateKey();

        //Generate IV
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] iv = cipher.getIV();
        // save to files
        Files.writeString(Path.of(KEY_FILE), Base64.getEncoder().encodeToString(key.getEncoded()));
        Files.writeString(Path.of(IV_FILE), Base64.getEncoder().encodeToString(iv));


    }
    /*
    * Function to send arg of list of passwords to be sent to a file and
    * then have that file encrypted
    */
    static void sendPasswordList(ArrayList<Password> passwordList) throws Exception{
        SecretKey key = readKey();
        byte[] iv = readIV();
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key ,new GCMParameterSpec(GCM_TAG_LENGTH, iv) );
        // this allows the files to close properly without a read error later
        try (
                FileOutputStream fos = new FileOutputStream(DATA_FILE);
                CipherOutputStream cos = new CipherOutputStream(fos, cipher);
                ObjectOutputStream oos = new ObjectOutputStream(cos);
        ) {
            oos.writeObject(passwordList);
        }
    }

    static ArrayList<Password> readPasswordList() throws Exception{
        SecretKey key = readKey();
        byte[] iv = readIV();
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key ,new GCMParameterSpec(GCM_TAG_LENGTH, iv) );
        // this allows the files to close properly without a read error later
        try (
            FileInputStream fis = new FileInputStream(DATA_FILE);
            CipherInputStream cis = new CipherInputStream(fis, cipher);
            ObjectInputStream ois = new ObjectInputStream(cis);
        ){
            return (ArrayList<Password>) ois.readObject();
        }

    }
    // Read The key from Key file and then convert to secretKey
    private static SecretKey readKey() throws IOException{
        String keyBase64 = Files.readString(Path.of(KEY_FILE)).trim();
        byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
        return new SecretKeySpec(keyBytes, "AES");
    }
    // read the key into a byte array
    private static byte[] readIV() throws IOException {
        String ivBase64 = Files.readString(Path.of(IV_FILE)).trim();
        byte[] ivBytes = Base64.getDecoder().decode(ivBase64);
        return ivBytes;
    }
}
