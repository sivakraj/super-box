package in.bluebytes.superbox.controller;

import in.bluebytes.superbox.model.Credential;
import in.bluebytes.superbox.model.User;
import in.bluebytes.superbox.service.CredentialService;
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
@RequiredArgsConstructor
@RequestMapping("/credential")
public class CredentialController {

    private final UserService userService;
    private final CredentialService credentialService;

    @PostMapping("/saveCredential")
    public String saveOrUpdateCredential(Authentication authentication, Credential credential, Model model) {

        String credError = null;
        int rowsAddedOrUpdated = -2; //set some negative value for error detection

        boolean isUpdate = credential.getCredentialId() > 0;

        User user = userService.getUser(authentication.getName());

        rowsAddedOrUpdated = credentialService.persistCredential(credential, user, isUpdate);

        if(rowsAddedOrUpdated < 0) {
            credError = "Error in saving/updating credentials. Please try after some time.";
        }

        if(null == credError) {
            model.addAttribute("modalMessage", "Credential has been added/updated successfully.");
            model.addAttribute("modalMsgTitle", "Credential Add/Update Success");

        } else {
            model.addAttribute("modalMessage", credError);
            model.addAttribute("modalMsgTitle", "Credential Add/Update Failure");
        }

        return "home";
    }

    @GetMapping("/deleteCredential/{id}")
    public String deleteCredential(Authentication authentication, @PathVariable String id, Model model) {
        String deleteCredError = null;
        int rowsDeleted = -2;

        User user = userService.getUser(authentication.getName());

        int credentialId = ControllerHelper.sanitizeId(id);

        if(credentialId < 0 && credentialService.isValidCredential(credentialId, user.getUserId())) {
            deleteCredError = "Credential delete cancelled due to invalid credential id.";
        }

        if(null == deleteCredError) {
            rowsDeleted = credentialService.deleteCredential(credentialId);
        }

        if(rowsDeleted < 0) {
            deleteCredError = "Credential delete failed. Please try again later.";
        }

        if(null == deleteCredError) {
            model.addAttribute("modalMessage", "Credential has been deleted successfully.");
            model.addAttribute("modalMsgTitle", "Credential Delete Success");
        } else {
            model.addAttribute("modalMessage", deleteCredError);
            model.addAttribute("modalMsgTitle", "Credential Delete Failure");
        }

        return "home";
    }

}
