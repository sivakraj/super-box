package in.bluebytes.superbox.service;

import in.bluebytes.superbox.mapper.UserMapper;
import in.bluebytes.superbox.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final HashService hashService;

    /**
     * Checks if the new username is already taken by someone
     * by querying the database
     * @param userName
     * @return true if specified username is not available
     */
    public boolean isUserAvailable(String userName) {
        return userMapper.get(userName) == null;
    }

    /**
     * Creates a new user in the persistence aka registration
     * @param user to be persisted in the database
     * @return unique user id returned from the database
     */
    public int createUser(User user) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
        return userMapper.insert(User.builder().firstName(user.getFirstName())
                .lastName(user.getLastName()).userName(user.getUserName())
                .password(hashedPassword).salt(encodedSalt).build());
    }

    /**
     * Get an user from the persistence from supplied username
     * @param userName
     * @return user retrieved from the database
     */
    public User getUser(String userName) {
        return userMapper.get(userName);
    }

}
