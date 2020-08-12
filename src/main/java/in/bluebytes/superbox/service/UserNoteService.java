package in.bluebytes.superbox.service;

import in.bluebytes.superbox.mapper.UserNoteMapper;
import in.bluebytes.superbox.model.UserNote;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserNoteService {

    private final UserNoteMapper userNoteMapper;

    public UserNote getUserNote(int noteId) {
        return userNoteMapper.getUserNote(noteId);
    }

    public List<UserNote> getUserNotes(int userId) {
        return userNoteMapper.getUserNotes(userId);
    }

    public List<UserNote> getUserNotesByTitle(String title) {
        return userNoteMapper.getUserNotesByTitle(title);
    }

    public int createUserNote(UserNote userNote) {
        return userNoteMapper.insertNote(userNote);
    }

    public int updateUserNote(UserNote userNote) {
        return userNoteMapper.updateNote(userNote);
    }

    public int deleteUserNote(int noteId) {
        return userNoteMapper.deleteNote(noteId);
    }

    public boolean isValidUserNote(int noteId, int userId) {
        return userNoteMapper.isValidUserNote(noteId, userId) == null;
    }
}
