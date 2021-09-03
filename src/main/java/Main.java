import models.User;
import services.EmailUtility;
import services.UserAccountService;

public class Main {
    public static void main(String[] args) {
        // Initialize the User table as financial manager
        UserAccountService userAccountService = new UserAccountService();
        User user = new User(1, "manager", "manager", "Manager", "Manager","roel.crodua@revature.net", 2);
        userAccountService.addNewUserAccount(user);
    }
}
