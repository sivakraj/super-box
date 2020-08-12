package in.bluebytes.superbox.service;

import in.bluebytes.superbox.mapper.UserFileMapper;
import in.bluebytes.superbox.model.User;
import in.bluebytes.superbox.model.UserFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserFileService {

    private final UserFileMapper userFileMapper;

    public boolean isFileAvailable(String name, int userId) {
        return getFileByName(name, userId) == null;
    }

    public UserFile getFile(int id) {
        return userFileMapper.getFileById(id);
    }

    public List<UserFile> getFiles(int userId) {
        return userFileMapper.getFileByUserId(userId);
    }

    public int insertFile(UserFile userFile) {
        return userFileMapper.insertFile(userFile);
    }

    public UserFile getFileByName(String name, int userId) {
        return userFileMapper.getFileByName(name, userId);
    }

    public int deleteFile(int id) {
        return userFileMapper.deleteFile(id);
    }

    /**
     * Prepares file to be persisted
     * @param file
     * @param user
     * @return entity object of UserFile ready to be pushed to persistence
     */
    public UserFile createUserFile(MultipartFile file, User user) {
        UserFile userFile = null;
        try {
            userFile = UserFile.builder().fileName(StringUtils.cleanPath(file.getOriginalFilename())).contentType(file.getContentType())
                    .fileSize(String.valueOf(file.getSize())).userId(user.getUserId())
                    .fileData(file.getBytes()).build();
        } catch(IOException e) {
            log.error("Error in reading file content: ", e);
        }
        return userFile;
    }

}
