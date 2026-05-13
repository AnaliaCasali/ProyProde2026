package utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
    // instancia encoder de password
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // metodo para hashear la contraseña
    public static String hashPassword(String plainPassword) {
        return encoder.encode(plainPassword);
    }

    // metodo para verificar la contraseña
    public static boolean verifyPassword(String plainPassword, String hashPassword) {
        return encoder.matches(plainPassword, hashPassword);
    }
}
