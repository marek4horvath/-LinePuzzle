package sk.tuke.kpi.kp.linepuzzle.service;

import sk.tuke.kpi.kp.linepuzzle.entity.Registration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/*CREATE TABLE user_registration (
    ident int,
    LastName varchar(255),
    FirstName varchar(255),
	create_registration DATE,
    password varchar(255)
);*/
@Transactional
public class UserRegistrationJPA implements UserRegistration {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addUser(Registration registration) throws UserRegistrationException {
        entityManager.persist(registration);

    }

    public List getUser(String firstName, String password) throws UserRegistrationException {
        return entityManager.createNamedQuery("Registration.selectToRegistration")
                .setParameter("firstName", firstName).setParameter("password",password).getResultList();

    }


}
