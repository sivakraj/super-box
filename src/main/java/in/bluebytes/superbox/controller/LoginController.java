package in.bluebytes.superbox.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping()
    public String showLogin(Model model, @ModelAttribute("flashAttribute") Object flashAttribute) {
        if("success".equals(flashAttribute)) { //to know if it's a redirection after sign up
            model.addAttribute("signupSuccess", flashAttribute);
        }
        return "login";
    }
}
