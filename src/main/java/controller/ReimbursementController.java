package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Reimbursement;
import models.User;
import services.ReimbursementService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

public class ReimbursementController {
    // singleton
    private static ReimbursementController reimbursementController;
    ReimbursementService reimbursementService = new ReimbursementService();

    public static ReimbursementController getInstance() {
        if (reimbursementController == null) {
            reimbursementController = new ReimbursementController();
        }
        return reimbursementController;
    }

    public void getListOfOwnReimbursement(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("/application/json");
        PrintWriter out = response.getWriter();
        User user = new User();
        user.setUserId(Integer.parseInt(request.getParameter("userId")));
        List<Reimbursement> reimbursements = reimbursementService.getListOfOwnReimbursement(user);
        out.println(new ObjectMapper().writeValueAsString(reimbursements));
    }

    public void getOneReimbursement(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("/application/json");
        PrintWriter out = response.getWriter();
        Reimbursement tempReimbursement = new Reimbursement();
        tempReimbursement.setReimbId(Integer.parseInt(request.getParameter("reimbId")));
        Reimbursement reimbursement = reimbursementService.getASpecificReimbursementTicket(tempReimbursement);
        out.println(new ObjectMapper().writeValueAsString(reimbursement));
    }

    public void addNewReimbursement(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        String requestBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Reimbursement reimbursement = reimbursementService.addNewReimbursement(new ObjectMapper().readValue(requestBody, Reimbursement.class));
        out.println(new ObjectMapper().writeValueAsString(reimbursement));
    }

    public void editAReimbursement(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        String requestBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Reimbursement inputReimbursement = new ObjectMapper().readValue(requestBody, Reimbursement.class);

        Reimbursement editedReimbursement = reimbursementService.editAReimbursement(inputReimbursement);
        out.println(new ObjectMapper().writeValueAsString(editedReimbursement));
    }

    public void deleteAReimbursement(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        User user = new User();
        user.setUserId(Integer.parseInt(req.getParameter("userId")));

        Reimbursement reimbursement = new Reimbursement();
        reimbursement.setReimbId(Integer.parseInt(req.getParameter("reimbId")));

        reimbursementService.deleteAReimbursement(reimbursement, user);
    }

    public void resolveAReimbursement(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        String requestBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Reimbursement inputReimbursement = new ObjectMapper().readValue(requestBody, Reimbursement.class);

        Reimbursement editedReimbursement = reimbursementService.resolveAReimbursement(inputReimbursement);

        out.println(new ObjectMapper().writeValueAsString(editedReimbursement));
    }

    public void getListOfAllPendingReimbursement(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        List<Reimbursement> reimbursements = reimbursementService.getListOfAllPendingReimbursement();

        out.println(new ObjectMapper().writeValueAsString(reimbursements));
    }

    public void getListOfAllResolvedReimbursement(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        User user = new User();
        user.setUserId(Integer.parseInt(request.getParameter("fmId")));

        List<Reimbursement> reimbursements = reimbursementService.getListOfAllResolvedReimbursement(user);

        out.println(new ObjectMapper().writeValueAsString(reimbursements));
    }
}
