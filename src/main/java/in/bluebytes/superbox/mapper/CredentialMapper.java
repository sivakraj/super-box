package in.bluebytes.superbox.mapper;

import in.bluebytes.superbox.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    public Credential getCredential(int credentialId);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
    public List<Credential> getCredentials(int userId);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialId} and userid = #{userId}")
    public Credential isValidCredential(int credentialId, int userId);

    @Insert("INSERT INTO CREDENTIALS(url, username, key, password, userid) VALUES(#{url}, #{userName}, #{key}, #{password}, #{userId})")
    public int insertCredential(Credential credential);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{userName}, userid = #{userId}, key = #{key}, password = #{password} WHERE credentialid = #{credentialId}")
    public int updateCredential(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    public int deleteCredential(int credentialId);

}
