package in.bluebytes.superbox.mapper;

import in.bluebytes.superbox.model.UserNote;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserNoteMapper {

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteId}")
    public UserNote getUserNote(int noteId);

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    public List<UserNote> getUserNotes(int userId);

    @Select("SELECT * FROM NOTES WHERE notetitle = #{title}")
    public List<UserNote> getUserNotesByTitle(String title);

    @Select("SELECT * FRIM NOTES WHERE noteid = #{noteId} and userid = #{userId}")
    public UserNote isValidUserNote(int noteId, int userId);

    @Insert("INSERT INTO NOTES(notetitle, notedescription, userid) VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    public int insertNote(UserNote userNote);

    @Update("UPDATE NOTES SET notetitle = #{noteTitle}, notedescription = #{noteDescription}, userid = #{userId} WHERE noteid = #{noteId}")
    public int updateNote(UserNote userNote);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId}")
    public int deleteNote(int noteId);

}
