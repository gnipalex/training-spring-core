package ua.epam.spring.hometask.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Yuriy_Tkach
 */
public class User extends DomainObject {

    private LocalDateTime birthday;
    private String firstName;
    private String lastName;
    private String email;
    private double balance;
    
    public User() {
    }
    
    public User(User user) {
        super(user);
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.email = user.email;
        this.birthday = user.birthday;
        this.balance = user.balance;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }
    
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email);
    }

    @Override
    public boolean equals(Object obj) {
        User other = (User) obj;
        return super.equals(other) && Objects.equals(email, other.email) &&
            Objects.equals(firstName, other.firstName) &&
            Objects.equals(lastName, other.lastName);
    }

}
