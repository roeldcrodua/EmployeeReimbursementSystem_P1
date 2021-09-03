package dao;

import models.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserAccountDaoImpl implements UserAccountDao{

    String url = "jdbc:postgresql://" + System.getenv("PostgresDb_Url") + "/ers_project1_db";
    String username = System.getenv("PostgresDb_Username");
    String password = System.getenv("PostgresDb_Password");

    public UserAccountDaoImpl() {
        try{
            Class.forName("org.postgresql.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    @Override
    public String getUserRole(Integer roleId) {
        String userRole = "";
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            String sql = "SELECT * FROM ers_user_roles WHERE user_role_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, roleId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                userRole =  rs.getString("user_role_value");
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return userRole;
    }


    @Override
    public List<User> getListOfUserAccount() {

        List<User> userAccounts= new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(url, username, password)){
            String sql = "SELECT ers_users_id, ers_username, user_first_name, user_last_name, " +
                    "user_email, user_role_id  FROM ers_users;";
            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                userAccounts.add(
                        new User(
                            rs.getInt("ers_users_id"),
                            rs.getString("ers_username"),
                            "",
                            rs.getString("user_first_name"),
                            rs.getString("user_last_name"),
                            rs.getString("user_email"),
                            rs.getInt("user_role_id")
                ));
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return userAccounts;
    }

    @Override
    public User getUserAccountInfo(User user) {

        User userInfo = new User();

        try(Connection connection = DriverManager.getConnection(url, username, password)){
            String sql = "SELECT * FROM ers_users WHERE ers_username = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, user.getUserName());
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                userInfo = new User(
                        rs.getInt("ers_users_id"),
                        rs.getString("ers_username"),
                        rs.getString("ers_password"),
                        rs.getString("user_first_name"),
                        rs.getString("user_last_name"),
                        rs.getString("user_email"),
                        rs.getInt("user_role_id"));
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return userInfo;
    }

    @Override
    public Boolean addNewUserAccount(User user) {
        try(Connection connect = DriverManager.getConnection(url, username, password)) {
            String sql = "INSERT INTO ers_users VALUES (DEFAULT, ?, ?, ?, ?, ?, ?);";
            PreparedStatement ps = connect.prepareStatement(sql);

            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setString(5, user.getEmail());
            ps.setInt(6, user.getRoleId());
            return ps.executeUpdate() != 0;
        }catch(Exception ex){
            ex.printStackTrace();
            return  false;
        }
    }

    @Override
    public Boolean editUserAccount(User user) {
        try(Connection connect = DriverManager.getConnection(url, username, password)) {
            String sql = "UPDATE ers_users SET ers_password = ? WHERE ers_username = ? ;";
            PreparedStatement ps = connect.prepareStatement(sql);

            ps.setString(1, user.getPassword());
            ps.setString(2, user.getUserName());
            return ps.executeUpdate() != 0;

        }catch(Exception ex){
            ex.printStackTrace();
            return  false;
        }
    }
}
