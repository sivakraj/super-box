package in.bluebytes.superbox.service;

import in.bluebytes.superbox.mapper.CredentialMapper;
import in.bluebytes.superbox.model.Credential;
import in.bluebytes.superbox.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public Credential getCredential(int credentialId) {
        return credentialMapper.getCredential(credentialId);
    }

    public List<Credential> getCredentials(int userId) {
        return credentialMapper.getCredentials(userId);
    }

    public boolean isValidCredential(int credentialId, int userId) {
        return credentialMapper.isValidCredential(credentialId, userId) == null;
    }

    public int persistCredential(Credential credential, User user, boolean isUpdate) {
        prepareCredential(credential, user);
        if(isUpdate) {
            return updateCredential(credential);
        }
        return insertCredential(credential);
    }

    public String decrypt(String value, String key) {
        return encryptionService.decrypt(value, key);
    }

    private void prepareCredential(Credential credential, User user) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encrypt(credential.getPassword(), encodedKey);
        credential.setKey(encodedKey);
        credential.setPassword(encryptedPassword);
        credential.setUserId(user.getUserId());
    }

    public int insertCredential(Credential credential) {
        return credentialMapper.insertCredential(credential);
    }

    public int updateCredential(Credential credential) {
        return credentialMapper.updateCredential(credential);
    }

    public int deleteCredential(int credentialId) {
        return credentialMapper.deleteCredential(credentialId);
    }
}
