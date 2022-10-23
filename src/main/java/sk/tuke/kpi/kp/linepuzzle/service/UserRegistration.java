package sk.tuke.kpi.kp.linepuzzle.service;

import sk.tuke.kpi.kp.linepuzzle.entity.Comment;
import sk.tuke.kpi.kp.linepuzzle.entity.Registration;

import java.util.List;


public interface UserRegistration {
    void addUser(Registration registration) throws UserRegistrationException;
    List<Registration> getUser(String firstName, String password) throws UserRegistrationException;

}
