package VendingMachine;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class PasswordHash {

    /**
     * Method to create a salt for the hash.
     * @throws NoSuchAlgorithmException
     * @return salt
     */
    public static byte[] createSalt() throws NoSuchAlgorithmException{
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;

    }

    
    /**
     * Method to create a salted hash.
     * @param password
     * @param salt
     * @return
     */
    public static byte[] getSaltedHashSHA512(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA512");
            md.update(salt);
            byte byteData[] = md.digest(password.getBytes());
            md.reset();
            return byteData;

        } catch (NoSuchAlgorithmException NSAE) {
            NSAE.printStackTrace();
            return null;

        }

    }


    /**
     * Method to source from hex.
     * @param hex
     * @return binary
     */
    public static byte[] fromHex(String hex) {
        byte[] binary = new byte[hex.length() / 2];

        for(int i = 0; i < binary.length; i++) {
            binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }

        return binary;
    }


    /**
     * Method to source to hex.
     * @param array
     * @return hex
     */
    public static String toHex(byte[] array) {

        BigInteger bigInt = new BigInteger(1, array);
        String hex = bigInt.toString(16);

        int paddingLength = (array.length * 2) - hex.length();

        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }

    }


    /**
     * Method to hash password.
     * @param password
     * @return pw
     */
    public static Password createHashedPassword(String password) {

        byte salt[] = null;

        try {
            salt = createSalt();

        } catch (NoSuchAlgorithmException NSAE) {
            NSAE.printStackTrace();

        }

        byte[] byteDigestPassword = getSaltedHashSHA512(password, salt);


        String strHashPassword = toHex(byteDigestPassword);
        String strSalt = toHex(salt);

        Password pw = new Password(strSalt, strHashPassword);

        return pw;

    }


    /**
     * Method to check if user is valid.
     * @param salt
     * @param hashPassword
     * @param enteredPassword
     * @return
     */
    public static boolean isValidUser(String salt, String hashPassword, String enteredPassword) {
        byte[] byteSalt = fromHex(salt);

        byte[] byteEnteredPassword = getSaltedHashSHA512(enteredPassword, byteSalt);
        byte[] byteHashPassword = fromHex(hashPassword);

        return Arrays.equals(byteHashPassword, byteEnteredPassword);
    }

    /**
     * Main method for password hash.
     * @param argv
     */
    public static void main(String[] argv) {
        Password pass = createHashedPassword("pass");
        System.out.println(isValidUser(pass.strSalt(), pass.hashPassword(), "sdf"));
    }

}

record Password(String strSalt, String hashPassword) {

    /**
     * Constructor for record of password salt and hash password.
     * @param strSalt
     * @param hashPassword
     */
    public Password(String strSalt, String hashPassword) {
        this.strSalt = strSalt;
        this.hashPassword = hashPassword;
    }

}
