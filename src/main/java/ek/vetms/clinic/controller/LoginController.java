package ek.vetms.clinic.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
@Controller
public class LoginController {
    @GetMapping("/loginPage")
    public String loginPage(){
        return "login/login-page";
    }

    @GetMapping("/access-denied")
    public String deniedPage(){
        return "login/access-denied";
    }
}
