package in.bluebytes.superbox.controller;

import in.bluebytes.superbox.model.User;
import in.bluebytes.superbox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignUpController {

    private final UserService userService;

    @GetMapping()
    public String showSignUp(User user, Model model) {
        return "signup";
    }

    @PostMapping()
    public String processSignUp(User user, Model model, RedirectAttributes redirectAttributes) {
        String signUpError = null;
        String gotoURL = "signup";
        //bail out early
        if(!userService.isUserAvailable(user.getUserName())) {
            signUpError = "The username already exists. Please try another one.";
        }

        if(signUpError == null) {
            int rowsAdded = userService.createUser(user);
            if(rowsAdded < 0) {
                signUpError = "There was an error in signing you up. Please try after some time.";
            }
        }

        if(signUpError == null) {
            redirectAttributes.addFlashAttribute("signupSuccess", "success");
            gotoURL = "redirect:/login";

        } else {
            model.addAttribute("signUpError", signUpError);
        }

        return gotoURL;
    }
}
