package in.bluebytes.superbox.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Service
@Slf4j
public class HashService {

    private static String HASH_ALGORITHM = "PBKDF2WithHmacSHA1";

    public String getHashedValue(String data, String salt) {
        byte[] hashedValue = null;

        KeySpec keySpec = new PBEKeySpec(data.toCharArray(), salt.getBytes(), 5000, 128);
        try{
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(HASH_ALGORITHM);
            hashedValue = secretKeyFactory.generateSecret(keySpec).getEncoded();
        } catch(InvalidKeySpecException | NoSuchAlgorithmException e) {
            log.error("Error in hashing: ", e);
        }
        return Base64.getEncoder().encodeToString(hashedValue);
    }

}
