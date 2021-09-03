package controller;

import models.Reimbursement;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import services.ReimbursementService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReimbursementControllerTest {
    ReimbursementController reimbursementController;

    // mocking object
    ReimbursementService reimbursementService = Mockito.mock(ReimbursementService.class);

    @BeforeEach
    void setUp() {
        reimbursementController = new ReimbursementController();
    }


    @Test
    void getListOfOwnReimbursement() {
        // assign
        List<Reimbursement> reimbursements = new ArrayList<>();
        User user = new User();
        user.setUserId(1);
        reimbursements.add( new Reimbursement(1, 250, null, null, "apartment rental", 1, 0, 1,1));
        boolean expectedResult = reimbursements.size() == 1;

        // mocking the object value
        Mockito.when(reimbursementService.getListOfOwnReimbursement(user)).thenReturn(reimbursements);

        // act
        boolean actionResult = reimbursementService.getListOfOwnReimbursement(user).size() == 1;

        // assert or check if method got called
        assertEquals(expectedResult, actionResult);

        // verify
        Mockito.verify(reimbursementService,Mockito.times(1)).getListOfOwnReimbursement(user);
    }

    @Test
    void getOneReimbursement() {
        // assign
        Reimbursement expectedResult = new Reimbursement(1, 250, null, null, "apartment rental", 1, 0, 1,1);

        // mocking the object value
        Mockito.when(reimbursementService.getASpecificReimbursementTicket(expectedResult)).thenReturn(expectedResult);

        // act
        Reimbursement actionResult = reimbursementService.getASpecificReimbursementTicket(expectedResult);

        // assert or check if method got called
        assertEquals(expectedResult, actionResult);

        // verify
        Mockito.verify(reimbursementService,Mockito.times(1)).getASpecificReimbursementTicket(expectedResult);
    }

    @Test
    void addNewReimbursement() {
        // assign
        Reimbursement expectedResult= new Reimbursement(1, 250, null, null, "apartment rental", 1, 0, 1,1);

        // mocking the object value
        Mockito.when(reimbursementService.addNewReimbursement(expectedResult)).thenReturn(expectedResult);

        // act
        Reimbursement actionResult = reimbursementService.addNewReimbursement(expectedResult);

        // assert or check if method got called
        assertEquals(expectedResult, actionResult);

        // verify
        Mockito.verify(reimbursementService,Mockito.times(1)).addNewReimbursement(expectedResult);
    }

    @Test
    void editAReimbursement() {
        // assign
        Reimbursement expectedResult= new Reimbursement(1, 250, null, null, "apartment rental", 1, 0, 1,1);

        // mocking the object value
        Mockito.when(reimbursementService.editAReimbursement(expectedResult)).thenReturn(expectedResult);

        // act
        Reimbursement actionResult = reimbursementService.editAReimbursement(expectedResult);

        // assert or check if method got called
        assertEquals(expectedResult, actionResult);

        // verify
        Mockito.verify(reimbursementService,Mockito.times(1)).editAReimbursement(expectedResult);
    }

    @Test
    void deleteAReimbursement() {
        // assign
        User user = new User();
        user.setUserId(1);
        Reimbursement reimbursement = new Reimbursement(1, 250, null, null, "apartment rental", 1, 0, 1,1);
        boolean expectedResult = true;

        // mocking the object value
        //Mockito.doNothing().when(reimbursementService.deleteAReimbursement(reimbursement, user));

        // verify
        Mockito.verify(reimbursementService,Mockito.times(0)).deleteAReimbursement(reimbursement, user);
    }

    @Test
    void resolveAReimbursement() {
        // assign
        Reimbursement expectedResult= new Reimbursement(1, 250, null, null, "apartment rental", 1, 0, 1,1);

        // mocking the object value
        Mockito.when(reimbursementService.resolveAReimbursement(expectedResult)).thenReturn(expectedResult);

        // act
        Reimbursement actionResult = reimbursementService.resolveAReimbursement(expectedResult);

        // assert or check if method got called
        assertEquals(expectedResult, actionResult);

        // verify
        Mockito.verify(reimbursementService,Mockito.times(1)).resolveAReimbursement(expectedResult);
    }

    @Test
    void getListOfAllPendingReimbursement() {
        // assign
        List<Reimbursement> reimbursements = new ArrayList<>();
        reimbursements.add( new Reimbursement(1, 250, null, null, "apartment rental", 1, 0, 1,1));
        boolean expectedResult = reimbursements.size() == 1;

        // mocking the object value
        Mockito.when(reimbursementService.getListOfAllPendingReimbursement()).thenReturn(reimbursements);

        // act
        boolean actionResult = reimbursementService.getListOfAllPendingReimbursement().size() == 1;

        // assert or check if method got called
        assertEquals(expectedResult, actionResult);

        // verify
        Mockito.verify(reimbursementService,Mockito.times(1)).getListOfAllPendingReimbursement();
    }
    @Test
    void getListOfAllResolvedReimbursement() {
        // assign
        List<Reimbursement> reimbursements = new ArrayList<>();
        User user = new User();
        user.setUserId(1);
        reimbursements.add( new Reimbursement(1, 250, null, null, "apartment rental", 1, 0, 1,1));
        boolean expectedResult = reimbursements.size() == 1;

        // mocking the object value
        Mockito.when(reimbursementService.getListOfAllResolvedReimbursement(user)).thenReturn(reimbursements);

        // act
        boolean actionResult = reimbursementService.getListOfAllResolvedReimbursement(user).size() == 1;

        // assert or check if method got called
        assertEquals(expectedResult, actionResult);

        // verify
        Mockito.verify(reimbursementService,Mockito.times(1)).getListOfAllResolvedReimbursement(user);
    }
}