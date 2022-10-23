package sk.tuke.kpi.kp.linepuzzle.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.kpi.kp.linepuzzle.core.Board;
import sk.tuke.kpi.kp.linepuzzle.core.Level;
import sk.tuke.kpi.kp.linepuzzle.core.LibraryLevels;
import sk.tuke.kpi.kp.linepuzzle.core.Player;
import sk.tuke.kpi.kp.linepuzzle.entity.Registration;
import sk.tuke.kpi.kp.linepuzzle.entity.User;
import sk.tuke.kpi.kp.linepuzzle.service.UserRegistration;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {

    private User loggedUser;
    private boolean reg = false;

    @Autowired
    private UserRegistration userRegistration;


    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String login(String login, String password) {
        List<Registration> registrations = userRegistration.getUser(login, getMD5(password));
        if (!registrations.isEmpty()) {
            loggedUser = new User(login);
            return "redirect:/linepuzzle";
        }
        return "redirect:/";
    }

    @RequestMapping("/logout")
    public String logout() {
        loggedUser = null;
        return "redirect:/";
    }

    @RequestMapping("/registration")
    public String registration(String firstName, String lastName, String password) {
        userRegistration.addUser(new Registration(firstName, lastName, getMD5(password), new Date()));
        reg = true;
        return "redirect:/";
    }

    public boolean isRegistration() {
        return reg;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public boolean isLogged() {
        return loggedUser != null;
    }

    private String getMD5(String inp) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(inp.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
