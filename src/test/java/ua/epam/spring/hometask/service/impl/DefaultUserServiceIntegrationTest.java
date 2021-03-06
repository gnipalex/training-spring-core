package ua.epam.spring.hometask.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.UserService;

//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DefaultUserServiceIntegrationTest extends AbstractServiceIntegrationTest {

    private static final LocalDateTime BIRTHDAY = LocalDateTime.now();
    private static final int BALANCE = 99999;
    private static final String LAST_NAME = "Hnyp";
    private static final String FIRST_NAME = "Alex";
    private static final String EMAIL = "alex.hnyp@email.com";
    
    @Resource
    private UserService userService;
    
    @Before
    public void setUp() {
        saveUser(FIRST_NAME, LAST_NAME, EMAIL, BALANCE, BIRTHDAY);
    }
    
    private void saveUser(String name, String lastName, String email, double balance, LocalDateTime birthday) {
        User createdUser = new User();
        createdUser.setBalance(balance);
        createdUser.setBirthday(birthday);
        createdUser.setEmail(email);
        createdUser.setFirstName(name);
        createdUser.setLastName(lastName);
        userService.save(createdUser);
    }
    
    @Test
    public void shouldReturnUser() {
        User actualUser = userService.getUserByEmail(EMAIL); 
        assertThat(actualUser)
            .isNotNull()
            .matches(u -> Objects.equals(u.getEmail(), EMAIL));
    }
    
    @Test
    public void shouldSaveUser() {
        saveUser("user1", "user1", "user1", 0, null);
        User actualUser = userService.getUserByEmail("user1");
        assertThat(actualUser).isNotNull();
        assertThat(userService.getAll()).hasSize(2);
    }
    
    @Test
    public void shouldRemoveUser() {
        User userToRemove = userService.getUserByEmail(EMAIL);
        userService.remove(userToRemove);
        User actualUser = userService.getUserByEmail(EMAIL);
        assertThat(actualUser).isNull();
    }
    
    @Test
    public void shouldSaveAllFields() {
        User user = userService.getUserByEmail(EMAIL);
        assertThat(user.getBirthday()).isEqualTo(BIRTHDAY);
        assertThat(user.getEmail()).isEqualTo(EMAIL);
        assertThat(user.getFirstName()).isEqualTo(FIRST_NAME);
        assertThat(user.getLastName()).isEqualTo(LAST_NAME);
    }

}
