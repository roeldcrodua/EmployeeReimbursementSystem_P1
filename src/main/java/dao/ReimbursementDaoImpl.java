package dao;

import models.Reimbursement;
import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementDaoImpl implements ReimbursementDao {

    public ReimbursementDaoImpl() {
        try{
            Class.forName("org.postgresql.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Reimbursement> getListOfOwnReimbursement(User user) {
        List<Reimbursement> reimbursements = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(ConnectionUtility.url,ConnectionUtility.username,ConnectionUtility.password)){
            String sql = "SELECT * FROM ers_reimbursement WHERE reimb_author = ? ";

            // adding any filters or sorting orders.
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, user.getUserId());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                reimbursements.add(
                        new Reimbursement(
                                rs.getInt("reimb_id"),
                                rs.getInt("reimb_amount"),
                                rs.getDate("reimb_submitted"),
                                rs.getDate("reimb_resolved"),
                                rs.getString("reimb_description"),
                                rs.getInt("reimb_author"),
                                rs.getInt("reimb_resolver"),
                                rs.getInt("reimb_status_id"),
                                rs.getInt("reimb_type_id")
                        ));
            }
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return reimbursements;
    }


    @Override
    public Reimbursement getASpecificReimbursementTicket(Reimbursement reimb) {

        Reimbursement reimbursement = new Reimbursement();

        try(Connection connection = DriverManager.getConnection(ConnectionUtility.url,ConnectionUtility.username,ConnectionUtility.password)){
            String sql = "SELECT * FROM ers_reimbursement WHERE reimb_id = ? ";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, reimb.getReimbId());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                reimbursement = new Reimbursement(
                        rs.getInt("reimb_id"),
                        rs.getInt("reimb_amount"),
                        rs.getDate("reimb_submitted"),
                        rs.getDate("reimb_resolved"),
                        rs.getString("reimb_description"),
                        rs.getInt("reimb_author"),
                        rs.getInt("reimb_resolver"),
                        rs.getInt("reimb_status_id"),
                        rs.getInt("reimb_type_id")
                );
            }
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return reimbursement;
    }


    @Override
    public boolean addNewReimbursement(Reimbursement reimbursement) {

        try(Connection connection = DriverManager.getConnection(ConnectionUtility.url,ConnectionUtility.username,ConnectionUtility.password)){
            String sql = "INSERT INTO ers_reimbursement VALUES (DEFAULT, ?, DEFAULT, NULL, ?, NULL, ?, NULL, 1, ?);";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, reimbursement.getAmount());
            ps.setString(2, reimbursement.getDescription());
            ps.setInt(3, reimbursement.getAuthor());
            ps.setInt(4, reimbursement.getTypeId());

            return ps.executeUpdate() != 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean editAReimbursement(Reimbursement reimbursement) {

        try(Connection connection = DriverManager.getConnection(ConnectionUtility.url,ConnectionUtility.username,ConnectionUtility.password)){

            String sql = "UPDATE ers_reimbursement " +
                    "SET reimb_amount = ?, reimb_description = ?, " +
                    "reimb_type_id = ? WHERE reimb_id = ?";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, reimbursement.getAmount());
            ps.setString(2, reimbursement.getDescription());
            ps.setInt(3, reimbursement.getTypeId());
            ps.setInt(4, reimbursement.getReimbId());

            return ps.executeUpdate() != 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteAReimbursement(Reimbursement reimbursement, User user) {

        try(Connection connection = DriverManager.getConnection(ConnectionUtility.url,ConnectionUtility.username,ConnectionUtility.password)){

            String sql = "DELETE FROM ers_reimbursement  WHERE reimb_id = ? AND reimb_author = ? ";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, reimbursement.getReimbId());
            ps.setInt(2, user.getUserId());

            return ps.executeUpdate() != 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean resolveAReimbursement(Reimbursement reimbursement) {

        try(Connection connection = DriverManager.getConnection(ConnectionUtility.url,ConnectionUtility.username,ConnectionUtility.password)){

            String sql = "UPDATE ers_reimbursement " +
                    "SET reimb_resolved = DEFAULT, reimb_resolver = ?, reimb_status_id = ?" +
                    "WHERE reimb_id = ?";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, reimbursement.getResolver());
            ps.setInt(2, reimbursement.getStatusId());
            ps.setInt(3, reimbursement.getReimbId());

            return ps.executeUpdate() != 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Reimbursement> getListOfAllPendingReimbursement() {
        List<Reimbursement> reimbursements = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(ConnectionUtility.url,ConnectionUtility.username,ConnectionUtility.password)){
            String sql = "SELECT * FROM ers_reimbursement WHERE reimb_status_id = 1 ";

            // adding any filters or sorting orders.
            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                reimbursements.add(
                        new Reimbursement(
                                rs.getInt("reimb_id"),
                                rs.getInt("reimb_amount"),
                                rs.getDate("reimb_submitted"),
                                rs.getDate("reimb_resolved"),
                                rs.getString("reimb_description"),
                                rs.getInt("reimb_author"),
                                rs.getInt("reimb_resolver"),
                                rs.getInt("reimb_status_id"),
                                rs.getInt("reimb_type_id")
                        ));
            }
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return reimbursements;
    }

    @Override
    public List<Reimbursement> getListOfAllResolvedReimbursement(User user) {
        List<Reimbursement> reimbursements = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(ConnectionUtility.url,ConnectionUtility.username,ConnectionUtility.password)){
            String sql = "SELECT * FROM ers_reimbursement WHERE reimb_resolver = ? ";

            // adding any filters or sorting orders.
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, user.getUserId() );
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                reimbursements.add(
                        new Reimbursement(
                                rs.getInt("reimb_id"),
                                rs.getInt("reimb_amount"),
                                rs.getDate("reimb_submitted"),
                                rs.getDate("reimb_resolved"),
                                rs.getString("reimb_description"),
                                rs.getInt("reimb_author"),
                                rs.getInt("reimb_resolver"),
                                rs.getInt("reimb_status_id"),
                                rs.getInt("reimb_type_id")
                        ));
            }
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return reimbursements;
    }

}