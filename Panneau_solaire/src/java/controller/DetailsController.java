package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Source_solaire;
import model.V_coupure;

public class DetailsController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try{
            String consommation = request.getParameter("consommation");
            String dateString = request.getParameter("date_coupure");
            String source_solaire = request.getParameter("source_solaire");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = dateFormat.parse(dateString);
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            
            // Convert the day of the week to its corresponding name
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sqlDate);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            String[] daysOfWeek = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
            String dayName = daysOfWeek[dayOfWeek - 1];
            
            // Call the function getAllCoupure
            List<V_coupure> listcoupure = V_coupure.getAllCoupure(dayName,source_solaire,sqlDate);
            request.setAttribute("consommation", consommation);           
            request.setAttribute("listcoupure", listcoupure);
            RequestDispatcher dispatcher = request.getRequestDispatcher("details.jsp");
            dispatcher.forward(request, response);
        }catch(Exception ex){
            ex.printStackTrace(response.getWriter());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
