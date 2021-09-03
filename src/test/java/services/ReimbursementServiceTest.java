package services;

import dao.ReimbursementDao;
import dao.ReimbursementDaoImpl;
import models.Reimbursement;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReimbursementServiceTest {

    ReimbursementService reimbursementService;

    // mocking object
    ReimbursementDao reimbursementDao = Mockito.mock(ReimbursementDaoImpl.class);

    @BeforeEach
    void setUp() {
        reimbursementService = new ReimbursementService();
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
        Mockito.when(reimbursementDao.getListOfOwnReimbursement(user)).thenReturn(reimbursements);

        // act
        boolean actionResult = reimbursementDao.getListOfOwnReimbursement(user).size() == 1;

        // assert or check if method got called
        assertEquals(expectedResult, actionResult);
    }

    @Test
    void getASpecificReimbursementTicket() {
        // assign
        Reimbursement expectedResult = new Reimbursement(1, 250, null, null, "apartment rental", 1, 0, 1,1);

        // mocking the object value
        Mockito.when(reimbursementDao.getASpecificReimbursementTicket(expectedResult)).thenReturn(expectedResult);

        // act
        Reimbursement actionResult = reimbursementDao.getASpecificReimbursementTicket(expectedResult);

        // assert or check if method got called
        assertEquals(expectedResult, actionResult);
    }

    @Test
    void addNewReimbursement() {
        // assign
        Reimbursement reimbursement = new Reimbursement(1, 250, null, null, "apartment rental", 1, 0, 1,1);
        boolean expectedResult = true;

        // mocking the object value
        Mockito.when(reimbursementDao.addNewReimbursement(reimbursement)).thenReturn(true);

        // act
        boolean actionResult = reimbursementDao.addNewReimbursement(reimbursement);

        // assert or check if method got called
        assertEquals(expectedResult, actionResult);
    }

    @Test
    void editAReimbursement() {
        // assign
        Reimbursement reimbursement = new Reimbursement(1, 250, null, null, "apartment rental", 1, 0, 1,1);
        Reimbursement expectedResult = reimbursement;

        // mocking the object value
        Mockito.when(reimbursementDao.editAReimbursement(reimbursement)).thenReturn(true);

        // act
        Reimbursement actionResult = reimbursementDao.editAReimbursement(reimbursement) ? reimbursement : null;

        // assert or check if method got called
        assertEquals(expectedResult, actionResult);
    }

    @Test
    void deleteAReimbursement() {
        // assign
        User user = new User();
        user.setUserId(1);
        Reimbursement reimbursement = new Reimbursement(1, 250, null, null, "apartment rental", 1, 0, 1,1);
        boolean expectedResult = true;

        // mocking the object value
        Mockito.when(reimbursementDao.deleteAReimbursement(reimbursement, user)).thenReturn(true);

        // act
        boolean actionResult = reimbursementDao.deleteAReimbursement(reimbursement,user);

        // assert or check if method got called
        assertEquals(expectedResult, actionResult);
    }

    @Test
    void resolveAReimbursement() {
        // assign
        Reimbursement reimbursement = new Reimbursement(1, 250, null, null, "apartment rental", 1, 0, 1,1);
        Reimbursement expectedResult = reimbursement;

        // mocking the object value
        Mockito.when(reimbursementDao.resolveAReimbursement(reimbursement)).thenReturn(true);

        // act
        Reimbursement actionResult = reimbursementDao.resolveAReimbursement(reimbursement) ? reimbursement : null;

        // assert or check if method got called
        assertEquals(expectedResult, actionResult);
    }

    @Test
    void getListOfAllPendingReimbursement() {
        List<Reimbursement> reimbursements = new ArrayList<>();
        reimbursements.add( new Reimbursement(1, 250, null, null, "apartment rental", 1, 0, 1,1));
        boolean expectedResult = reimbursements.size() == 1;

        // mocking the object value
        Mockito.when(reimbursementDao.getListOfAllPendingReimbursement()).thenReturn(reimbursements);

        // act
        boolean actionResult = reimbursementDao.getListOfAllPendingReimbursement().size() == 1;

        // assert or check if method got called
        assertEquals(expectedResult, actionResult);
    }

    @Test
    void getListOfAllResolvedReimbursement() {
        List<Reimbursement> reimbursements = new ArrayList<>();
        User user = new User();
        user.setUserId(1);
        reimbursements.add( new Reimbursement(1, 250, null, null, "apartment rental", 1, 0, 2,1));
        Integer expectedResult = reimbursements.size();

        // mocking the object value
        Mockito.when(reimbursementDao.getListOfAllResolvedReimbursement(user)).thenReturn(reimbursements);

        // act
        Integer actionResult = reimbursementDao.getListOfAllResolvedReimbursement(user).size();

        // assert or check if method got called
        assertEquals(expectedResult, actionResult);
    }
}