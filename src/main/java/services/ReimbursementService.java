package services;

import dao.ReimbursementDao;
import dao.ReimbursementDaoImpl;
import models.Reimbursement;
import models.User;

import java.util.List;

public class ReimbursementService {

    private ReimbursementDao reimbursementDao;

    public ReimbursementService(){
        this.reimbursementDao = new ReimbursementDaoImpl();
    }

    public List<Reimbursement> getListOfOwnReimbursement(User user){
        return this.reimbursementDao.getListOfOwnReimbursement(user);
    }

    public Reimbursement getASpecificReimbursementTicket(Reimbursement reimbursement){
        return this.reimbursementDao.getASpecificReimbursementTicket(reimbursement);
    }

    public Reimbursement addNewReimbursement(Reimbursement reimbursement){
        if(this.reimbursementDao.addNewReimbursement(reimbursement)) {
            return this.reimbursementDao.getASpecificReimbursementTicket(reimbursement);
        } else {
            return null;
        }
    }

    public Reimbursement editAReimbursement(Reimbursement reimbursement){
        if (this.reimbursementDao.editAReimbursement(reimbursement)){
            return this.reimbursementDao.getASpecificReimbursementTicket(reimbursement);
        } else {
            return null;
        }
    }

    public void deleteAReimbursement(Reimbursement reimbursement, User user){
        this.reimbursementDao.deleteAReimbursement(reimbursement, user);
    }

    public Reimbursement resolveAReimbursement(Reimbursement reimbursement){
        if (this.reimbursementDao.resolveAReimbursement(reimbursement)){
            return this.reimbursementDao.getASpecificReimbursementTicket(reimbursement);
        } else {
            return null;
        }
    }

    public List<Reimbursement> getListOfAllPendingReimbursement(){
        return this.reimbursementDao.getListOfAllPendingReimbursement();
    }

    public List<Reimbursement> getListOfAllResolvedReimbursement(User user){
        return this.reimbursementDao.getListOfAllResolvedReimbursement(user);
    }

}
