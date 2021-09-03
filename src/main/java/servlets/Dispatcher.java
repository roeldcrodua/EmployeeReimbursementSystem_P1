package servlets;

import com.sun.xml.internal.ws.streaming.PrefixFactoryImpl;
import controller.ReimbursementController;
import controller.UserAccountController;
import models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//endpoint routing
@WebServlet(name="dispatcher", urlPatterns = "/api/*")
public class Dispatcher extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String URI = request.getRequestURI();
        System.out.println(URI);
        System.out.println(request.getMethod());

        switch(URI){
            case "/api/user":
                if(request.getMethod().equals("POST"))
                    UserAccountController.getInstance().getUserFullName(request, response);
                break;
            case "/api/users":
                if(request.getMethod().equals("GET"))
                    UserAccountController.getInstance().getUsersList(request, response);
                break;
            case "/api/role":
                if(request.getMethod().equals("GET"))
                    UserAccountController.getInstance().getUserRole(request, response);
                break;
            case "/api/login":
                if(request.getMethod().equals("POST"))
                    UserAccountController.getInstance().login(request, response);
                break;
            case "/api/signup":
                if(request.getMethod().equals("POST"))
                    UserAccountController.getInstance().register(request, response);
                break;
            case "/api/reimbursements":
                if(request.getMethod().equals("GET"))
                    ReimbursementController.getInstance().getListOfOwnReimbursement(request,response);
                break;
            case "/api/reimbursement":
                switch (request.getMethod()){
                    case "GET":
                        ReimbursementController.getInstance().getOneReimbursement(request,response);
                        break;
                    case "POST":
                        ReimbursementController.getInstance().addNewReimbursement(request,response);
                        break;
                    case "DELETE":
                        ReimbursementController.getInstance().deleteAReimbursement(request, response);
                        break;
                    case "PUT":
                        ReimbursementController.getInstance().editAReimbursement(request, response);
                }
                break;
            case "/api/manager":
                if(request.getMethod().equals("GET"))
                    ReimbursementController.getInstance().getListOfAllPendingReimbursement(request,response);
                break;
            case "/api/resolve":
                switch (request.getMethod()){
                    case "GET":
                        ReimbursementController.getInstance().getListOfAllResolvedReimbursement(request,response);
                        break;
                    case "PATCH":
                        ReimbursementController.getInstance().resolveAReimbursement(request,response);
                        break;
                }
                break;
            case "/api/check-session":
                if(request.getMethod().equals("GET"))
                    UserAccountController.getInstance().checkSession(request, response);
                break;
            case "/api/logout":
                if(request.getMethod().equals("GET"))
                    UserAccountController.getInstance().logout(request, response);
                break;
            case "/api/reset":
                switch (request.getMethod()) {
                    case "GET":
                        UserAccountController.getInstance().generatePassword(request, response);
                        break;
                    case "POST":
                        UserAccountController.getInstance().resetPassword(request, response);
                        break;
                }
                break;
        }


    }
}
