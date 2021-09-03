package services;

import dao.UserAccountDao;
import dao.UserAccountDaoImpl;
import models.User;

import java.util.HashMap;
import java.util.List;

public class UserAccountService {

    private UserAccountDao userAccountDao;

    public UserAccountService(){
        this.userAccountDao = new UserAccountDaoImpl();
    }

    public String getUserRole(Integer roleId){
         return this.userAccountDao.getUserRole(roleId);
    }

    public List<User> getListOfUserAccount(){
        return this.userAccountDao.getListOfUserAccount();
    }

    public User getUserAccountInfo(User user){
        User tempUser = this.userAccountDao.getUserAccountInfo(user);
        if (tempUser == null) {
            return null;
        }

        // Use BCrypt to see if it matched the hashed value of the password
        if (BCrypt.checkpw(user.getPassword(), tempUser.getPassword())){
            return tempUser;
        } else {
            return null;
        }

    }

    public User addNewUserAccount(User user){
        //check if username exists in the system
        String tempUser = userAccountDao.getUserAccountInfo(user).getUserName();
        if (tempUser != null) {
            return null;
        } else {
            // Uses BCrypt to hash the password.
            String tempPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
            user.setPassword(tempPassword);
            this.userAccountDao.addNewUserAccount(user);
        }
        return this.userAccountDao.getUserAccountInfo(user);
    }

    public User editUserPassword(User user) {
        User tempUser = this.userAccountDao.getUserAccountInfo(user);
        if (tempUser == null) {
            return null;
        }

        // Use BCrypt to see if it matched the hashed value of the password
        if (BCrypt.checkpw(user.getPassword(), tempUser.getPassword())){
            // If old password match to the system then change to anew one.
            String newPassword = user.getEmail(); // store temporarily the new password in email field.
            String hashPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
            user.setPassword(hashPassword);
            this.userAccountDao.editUserAccount(user);
        } else {
            return null;
        }
        return this.userAccountDao.getUserAccountInfo(user);
    }
}
