package services;

import dao.UserAccountDao;
import dao.UserAccountDaoImpl;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserAccountServiceTest {
    UserAccountService userAccountService;

    // mocking object
    UserAccountDao userAccountDao = Mockito.mock(UserAccountDaoImpl.class);

    @BeforeEach
    void setUp() {
        userAccountService = new UserAccountService();
    }

    @Test
    void getUserRole() {
        // assign
        String expectedResult = "employee";

        // mocking the object value
        Mockito.when(userAccountDao.getUserRole(1)).thenReturn(expectedResult);

        // act
        String actionResult = userAccountDao.getUserRole(1);

        // assert or check if method got called
        assertEquals(expectedResult, actionResult);
    }

    @Test
    void getListOfUserAccount() {
        // assign
        List<User> user = new ArrayList<>();
        user.add( new User(1, "roel", "crodua", "Roel", "Crodua", "roel@mail.com", 1));
        boolean expectedResult = user.size() == 1;

        // mocking the object value
        Mockito.when(userAccountDao.getListOfUserAccount()).thenReturn(user);

        // act
        boolean actionResult = userAccountDao.getListOfUserAccount().size() == 1;

        // assert or check if method got called
        assertEquals(expectedResult, actionResult);
    }

    @Test
    void getUserAccountInfo() {
        // assign
        User expectedResult = new User(1, "roel", "crodua", "Roel", "Crodua", "roel@mail.com", 1);

        // mocking the object value
        Mockito.when(userAccountDao.getUserAccountInfo(expectedResult)).thenReturn(expectedResult);

        // act
        User actionResult = userAccountDao.getUserAccountInfo(expectedResult);

        // assert or check if method got called
        assertEquals(expectedResult, actionResult);
    }

    @Test
    void addNewUserAccount() {
        // assign
        User user = new User(1, "roel", "crodua", "Roel", "Crodua", "roel@mail.com", 1);
        boolean expectedResult = true;

        // mocking the object value
        Mockito.when(userAccountDao.addNewUserAccount(user)).thenReturn(true);

        // act
        boolean actionResult = userAccountDao.addNewUserAccount(user);

        // assert or check if method got called
        assertEquals(expectedResult, actionResult);
    }
}