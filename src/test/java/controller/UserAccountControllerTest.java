package controller;

import models.Reimbursement;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import services.UserAccountService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserAccountControllerTest {
    UserAccountController userAccountController;

    //mocking object
    UserAccountService userAccountService = Mockito.mock(UserAccountService.class);

    @BeforeEach
    void setUp() { userAccountController = new UserAccountController(); }

    @Test
    void getUserRole() {
        // assign
        User user = (new User(1, "roel", "crodua", "Roel", "Crodua", "roel@mail.com", 1));
        String expectedResult = "employee";

        // mocking the object value
        Mockito.when(userAccountService.getUserRole(user.getRoleId()));

        // act
        String actionResult = userAccountService.getUserRole(user.getRoleId());

        // assert or check if method got called
        assertEquals(expectedResult, actionResult);

        // verify
        Mockito.verify(userAccountService,Mockito.times(1)).getUserRole(user.getRoleId());

    }

    @Test
    void getUsersList() {
        // assign
        List<User> user = new ArrayList<>();
        user.add(new User(1, "roel", "crodua", "Roel", "Crodua", "roel@mail.com", 1));
        boolean expectedResult = user.size() == 1;

        // mocking the object value
        Mockito.when(userAccountService.getListOfUserAccount()).thenReturn(user);

        // act
        boolean actionResult = userAccountService.getListOfUserAccount().size() == 1;

        // assert or check if method got called
        assertEquals(expectedResult, actionResult);
        // verify
        Mockito.verify(userAccountService, Mockito.times(1)).getListOfUserAccount();
    }


    @Test
    void login() {
        // assign
        User expectedResult = (new User(1, "roel", "crodua", "Roel", "Crodua", "roel@mail.com", 1));

        // mocking the object value
        Mockito.when(userAccountService.getUserAccountInfo(expectedResult)).thenReturn(expectedResult);

        // act
        User actionResult = userAccountService.getUserAccountInfo(expectedResult);

        // assert or check if method got called
        assertEquals(expectedResult, actionResult);

        // verify
        Mockito.verify(userAccountService,Mockito.times(1)).getUserAccountInfo(expectedResult);
    }

    @Test
    void register() {
        // assign
        User expectedResult = (new User(1, "roel", "crodua", "Roel", "Crodua", "roel@mail.com", 1));

        // mocking the object value
        Mockito.when(userAccountService.addNewUserAccount(expectedResult)).thenReturn(expectedResult);

        // act
        User actionResult = userAccountService.addNewUserAccount(expectedResult);

        // assert or check if method got called
        assertEquals(expectedResult, actionResult);

        // verify
        Mockito.verify(userAccountService,Mockito.times(1)).addNewUserAccount(expectedResult);

    }

}