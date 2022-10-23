package sk.tuke.kpi.kp.linepuzzle.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.util.Date;

@Entity
@NamedQuery(name = "Registration.selectToRegistration", query = "SELECT r.firstName FROM Registration r WHERE r.firstName = :firstName and r.password = : password ")
public class Registration {
    @Id
    @GeneratedValue
    private int ident;

    private String firstName;

    private String lastName;

    private String password;

    private Date createRegistration;

    public Registration() {
    }

    public Registration( String firstName, String lastName, String password,Date createRegistration) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.createRegistration = createRegistration;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public Date getCreateRegistration() {
        return createRegistration;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCreateRegistration(Date createRegistration) {
        this.createRegistration = createRegistration;
    }

    @Override
    public String toString() {
        return "Registration{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", createRegistration=" + createRegistration +
                '}';
    }
}
