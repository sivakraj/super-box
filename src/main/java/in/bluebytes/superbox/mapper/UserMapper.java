package in.bluebytes.superbox.mapper;

import in.bluebytes.superbox.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM USERS WHERE username = #{userName}")
    public User get(String userName);

    @Insert("INSERT INTO USERS(firstname, lastname, username, password, salt) VALUES(#{firstName}, #{lastName}, #{userName}, #{password}, #{salt})")
    public int insert(User user);

}
