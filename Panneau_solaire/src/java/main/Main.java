package main;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import model.Source_solaire;
import model.V_coupure;
import static model.V_coupure.getAllCoupure;

public class Main {
    public static void main(String[] args) throws Exception {
        String dateString = "2023-11-01";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date utilDate = dateFormat.parse(dateString);
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
//        List<Source_solaire> listss = Source_solaire.getAll();
//
//        try{
//            double c = listss.get(0).guessConsommationStudents("thursday");
//            System.out.println(c);
//            System.out.print(listss.get(0).findPowerOutageTime(55,sqlDate,null));
//        }
//        catch(Exception ex){
//            ex.printStackTrace();
//        }
   
        
        List<Source_solaire> listss = Source_solaire.getAll();
        double listBesoinEstim = 0.;
        Timestamp[] listDateEstim = new Timestamp[listss.size()];
        String[] listSourceSolaire = new String[listss.size()];

        for (int i = 0; i < listss.size(); i++) {
            listBesoinEstim = listss.get(i).guessConsommationStudents("monday",sqlDate);
            listDateEstim[i] = listss.get(i).findPowerOutageTime(listBesoinEstim, sqlDate, null);
            listSourceSolaire[i] = listss.get(i).getNom();
            System.out.println("reponse "+listDateEstim[i]);
        }
            System.out.println("debug "+listss.get(0).findPowerOutageTime(05.08, sqlDate, null));
    }   
}
