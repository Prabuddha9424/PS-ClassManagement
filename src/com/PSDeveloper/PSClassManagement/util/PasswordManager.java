package com.PSDeveloper.PSClassManagement.util;

import org.mindrot.BCrypt;

public class PasswordManager {
    public static String encryptPassword(String palinText){
        return BCrypt.hashpw(palinText,BCrypt.gensalt(10));
    }
    public static boolean checkPassword(String plainText,String hash){
        return BCrypt.checkpw(plainText,hash);
    }
}
