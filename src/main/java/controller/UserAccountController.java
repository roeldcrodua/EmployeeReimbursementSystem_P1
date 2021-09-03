package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.User;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import services.EmailUtility;
import services.UserAccountService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;
import org.json.simple.JSONObject;

public class UserAccountController {
    //singleton
    private  static UserAccountController userAccountController;
    UserAccountService userAccountService = new UserAccountService();
    public static UserAccountController getInstance(){
        if (userAccountController == null){
            userAccountController = new UserAccountController();
        }
        return userAccountController;
    }

    public void getUserRole(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("/application/json");
        PrintWriter out = response.getWriter();

        String role = userAccountService.getUserRole(Integer.parseInt(request.getParameter("roleId")));

        if (role != null) {
            out.println(new ObjectMapper().writeValueAsString(role));
        }
    }

    public void getUsersList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("/application/json");
        PrintWriter out = response.getWriter();

        List<User> users = userAccountService.getListOfUserAccount();

        if (users != null) {
            out.println(new ObjectMapper().writeValueAsString(users));
        }
    }

    public void getUserFullName(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("/application/json");
        PrintWriter out = response.getWriter();

        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        User user = new ObjectMapper().readValue(requestBody, User.class);
        User userFullName = new User();
        List<User> users = userAccountService.getListOfUserAccount();
        for (User tempUser: users){
            if (user.getUserId() == tempUser.getUserId()){
                userFullName.setFirstName(tempUser.getFirstName());
                userFullName.setLastName(tempUser.getLastName());
            }
        }
        if (userFullName != null) {
            out.println(new ObjectMapper().writeValueAsString(userFullName));
        }
    }

    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("/application/json");
        PrintWriter out = response.getWriter();

        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        User user = new ObjectMapper().readValue(requestBody, User.class);

        User tempUser = userAccountService.getUserAccountInfo(user);

        if (tempUser != null) {
            // create session
            HttpSession httpSession = request.getSession(true);
            httpSession.setAttribute("employee", tempUser);

            out.println(new ObjectMapper().writeValueAsString(tempUser));
        }
    }

    public void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("/application/json");
        PrintWriter out = response.getWriter();

        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        User user = new ObjectMapper().readValue(requestBody, User.class);
        String password = user.getPassword();
        User tempUser = userAccountService.addNewUserAccount(user);

        if (tempUser != null) {

            System.out.println(tempUser);
            System.out.println(password);
            EmailUtility.sendConfirmationEmail(tempUser.getEmail(), tempUser.getUserName(), password);
            out.println(new ObjectMapper().writeValueAsString(tempUser));
        }
    }

    public void checkSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        User user = (User) request.getSession().getAttribute("employee");

        if(user != null){
            out.println(new ObjectMapper().writeValueAsString(user));
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().setAttribute("employee", null);
    }

    public void generatePassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println(EmailUtility.generatePassword());
    }

    public void resetPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("/application/json");
        PrintWriter out = response.getWriter();

        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        User user = new ObjectMapper().readValue(requestBody, User.class);

        User tempUser = userAccountService.editUserPassword(user);

        if (tempUser != null) {
            EmailUtility.sendResetPasswordEmail(tempUser.getEmail(), tempUser.getUserName());
            out.println(new ObjectMapper().writeValueAsString(tempUser));
        }
    }
}
