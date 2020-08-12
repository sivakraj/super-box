package in.bluebytes.superbox.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@Slf4j
public class EncryptionService {

    private final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private final String ALGORITHM_SPEC = "AES";
    private final String ENCODING_FORMAT = "UTF-8";

    public String encrypt(String data, String key) {
        byte[] encryptedValue = null;

        try{
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM_SPEC);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            encryptedValue = cipher.doFinal(data.getBytes(ENCODING_FORMAT));
        } catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e) {
            log.error("Error in encrypting the data", e);
        }

        return Base64.getEncoder().encodeToString(encryptedValue);
    }

    public String decrypt(String data, String key) {
        byte[] decryptedValue = null;

        try{
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM_SPEC);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            decryptedValue = cipher.doFinal(Base64.getDecoder().decode(data));
        } catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | IllegalBlockSizeException | BadPaddingException e) {
            log.error("Error in decrypting the data", e);
        }

        return new String(decryptedValue);

    }
}
