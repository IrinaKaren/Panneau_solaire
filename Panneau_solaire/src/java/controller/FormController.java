package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Source_solaire;

public class FormController extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            //cast date that I get to sql.date
            response.setContentType("text/html;charset=UTF-8");
            String dateString = request.getParameter("date");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = dateFormat.parse(dateString);
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            
            // Convert the day of the week to its corresponding name
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sqlDate);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            String[] daysOfWeek = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
            String dayName = daysOfWeek[dayOfWeek - 1];
            
            //call all of the source solaire
            List<Source_solaire> listss = Source_solaire.getAll();
            double[] listBesoinEstim = new double[listss.size()];
            Timestamp[] listDateEstim = new Timestamp[listss.size()];
            String[] listSourceSolaire = new String[listss.size()];

            for (int i = 0; i < listss.size(); i++) {
                listBesoinEstim[i] = listss.get(i).guessConsommationStudents(dayName,sqlDate);
                listDateEstim[i] = listss.get(i).findPowerOutageTime(listBesoinEstim[i], sqlDate, null);
                listSourceSolaire[i] = listss.get(i).getNom();
            }

            request.setAttribute("listBesoinEstim", listBesoinEstim);
            request.setAttribute("listSourceSolaire", listSourceSolaire);
            request.setAttribute("listDateCoupureEstim", listDateEstim);
            RequestDispatcher dispatcher = request.getRequestDispatcher("coupure.jsp");
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
