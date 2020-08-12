package in.bluebytes.superbox.mapper;

import in.bluebytes.superbox.model.UserFile;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserFileMapper {

    @Select("SELECT * FROM FILES WHERE filename = #{name} and userid = #{userId}")
    public UserFile getFileByName(String name, int userId);

    @Select("SELECT * FROM FILES WHERE fileId = #{id}")
    public UserFile getFileById(int id);

    @Select("SELECT * FROM FILES WHERE userid = #{id}")
    public List<UserFile> getFileByUserId(int id);

    @Delete("DELETE FROM FILES WHERE fileId = #{id}")
    public int deleteFile(int id);

    @Insert("INSERT INTO FILES(filename, contenttype, filesize, userid, filedata) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    public int insertFile(UserFile userFile);

}
