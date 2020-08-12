package in.bluebytes.superbox.controller;

import in.bluebytes.superbox.model.Credential;
import in.bluebytes.superbox.model.User;
import in.bluebytes.superbox.model.UserFile;
import in.bluebytes.superbox.model.UserNote;
import in.bluebytes.superbox.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home")
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final UserFileService userFileService;
    private final UserService userService;
    private final UserNoteService userNoteService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    @GetMapping()
    public String showHome(Authentication authentication, Model model) {

        //Get logged in user from persistence
        User user = userService.getUser(authentication.getName());

        //Get list of files of logged in user
        List<UserFile> userFiles = userFileService.getFiles(user.getUserId());
        model.addAttribute("userFiles", userFiles);

        List<UserNote> notes =  userNoteService.getUserNotes(user.getUserId());
        model.addAttribute("notes", notes);

        List<Credential> credentials = credentialService.getCredentials(user.getUserId());
        credentials = credentials.stream().map(this::setPlainPwd).collect(Collectors.toList());
        model.addAttribute("credentials", credentials);

        return "home";
    }

    private Credential setPlainPwd(Credential credential) {
        credential
                .setPlainPassword(encryptionService.decrypt(credential.getPassword(),
                        credential.getKey()));
        return credential;
    }
}
