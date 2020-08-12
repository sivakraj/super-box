package in.bluebytes.superbox.controller;

import in.bluebytes.superbox.model.User;
import in.bluebytes.superbox.model.UserNote;
import in.bluebytes.superbox.service.UserNoteService;
import in.bluebytes.superbox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/note")
@RequiredArgsConstructor
public class UserNoteController {

    private final UserService userService;
    private final UserNoteService userNoteService;

    @PostMapping("/saveNote")
    public String saveOrUpdateNote(Authentication authentication, UserNote userNote, Model model) {

        String noteError = null;
        int rowsAddedOrUpdated = -2; //set some negative value for error detection

        boolean isUpdate = userNote.getNoteId() > 0;

        User user = userService.getUser(authentication.getName());
        userNote.setUserId(user.getUserId()); //set it anyways for update

        if(isUpdate) {
            rowsAddedOrUpdated = userNoteService.updateUserNote(userNote);
        } else {
            rowsAddedOrUpdated = userNoteService.createUserNote(userNote);
        }

        if(rowsAddedOrUpdated < 0) {
            noteError = "Error in adding/updating note. Please try again later.";
        }

        if(null == noteError) {
            model.addAttribute("modalMessage", "Note has been added/updated successfully.");
            model.addAttribute("modalMsgTitle", "Note Add/Update Success");
        } else {
            model.addAttribute("modalMessage", noteError);
            model.addAttribute("modalMsgTitle", "Note Add/Update Failure");
        }

        return "home";
    }

    @GetMapping("/deleteNote/{id}")
    public String deleteNote(Authentication authentication, @PathVariable String id, Model model) {
        String deleteNoteError = null;
        int rowsDeleted = -2;

        User user = userService.getUser(authentication.getName());

        int noteId = ControllerHelper.sanitizeId(id);

        if(noteId < 0 && userNoteService.isValidUserNote(noteId, user.getUserId())) {
            deleteNoteError = "Note delete cancelled due to invalid note id.";
        }

        if(null == deleteNoteError) {
            rowsDeleted = userNoteService.deleteUserNote(noteId);
        }

        if(rowsDeleted < 0) {
            deleteNoteError = "Note delete failed. Please try again later.";
        }

        if(null == deleteNoteError) {
            model.addAttribute("modalMessage", "Note has been deleted successfully.");
            model.addAttribute("modalMsgTitle", "Note Delete Success");
        } else {
            model.addAttribute("modalMessage", deleteNoteError);
            model.addAttribute("modalMsgTitle", "Note Delete Failure");
        }

        return "home";
    }

}
