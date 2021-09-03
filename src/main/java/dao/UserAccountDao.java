package dao;

import models.User;

import java.util.HashMap;
import java.util.List;

public interface UserAccountDao {
    String getUserRole(Integer roleId);
    List<User> getListOfUserAccount();
    User getUserAccountInfo(User user);
    Boolean addNewUserAccount(User user);
    Boolean editUserAccount(User user);
}
