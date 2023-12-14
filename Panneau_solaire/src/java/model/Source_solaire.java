package model;

import dbaccess.PGSQLConnection;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Time;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Source_solaire {
    int id;
    String nom;
    double puissance;
    
    //Getter
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public double getPuissance() {
        return puissance;
    }
    
    //Setter
    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPuissance(double puissance) {
        this.puissance = puissance;
    }
    
    //Constructor
    public Source_solaire() {
    }
    
    public Source_solaire(int id, String nom, double puissance) {
        this.setId(id);
        this.setNom(nom);
        this.setPuissance(puissance);
    }
    
    //Fonction
    public static List<Source_solaire> getAll() throws Exception{
        List<Source_solaire> listss = new ArrayList<>();
        Connection connection = PGSQLConnection.getConnection();
        String sql = "SELECT id , nom , puissance FROM source_solaire";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Source_solaire ss = new Source_solaire();
            ss.setId(resultSet.getInt("id"));
            ss.setNom(resultSet.getString("nom"));
            ss.setPuissance(resultSet.getDouble("puissance"));
            listss.add(ss);
        }
        connection.close();
        return listss;
    }
    
    public static Source_solaire getSourceSolaire(int idss,Connection connection) throws Exception {
        if(connection==null)connection = PGSQLConnection.getConnection();
        Source_solaire ss = null;
        String sql = "SELECT id, nom, puissance FROM source_solaire WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idss);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    ss = new Source_solaire();
                    ss.setId(resultSet.getInt("id"));
                    ss.setNom(resultSet.getString("nom"));
                    ss.setPuissance(resultSet.getDouble("puissance"));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ss;
    }
    
     public static List<Double> getCapaciteLuminosite(Date date,Connection connection) {
        List<Double> listcapacite = new ArrayList<>();
        try {
            if(connection==null)connection = PGSQLConnection.getConnection();
            String sql = "SELECT date_luminosite, capacite " +
                         "FROM luminosite " +
                         "WHERE date_luminosite::DATE = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setDate(1, date);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        double capacite = resultSet.getDouble("capacite");
                        listcapacite.add(capacite * 10);
                    }
                }
            }
            return listcapacite;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listcapacite;
    }
    
    public static List<Double> getSourceSolaireDispo(Date date, int idss,Connection connection) throws Exception {
        List<Double> listsspuissance = new ArrayList<>();
        double ss_puissance = Source_solaire.getSourceSolaire(idss,connection).getPuissance();
        List<Double> listlumcapacite = Source_solaire.getCapaciteLuminosite(date,connection);
        if (listlumcapacite != null && !listlumcapacite.isEmpty()) {
            for (int i = 0; i < listlumcapacite.size(); i++) {
                listsspuissance.add((ss_puissance * listlumcapacite.get(i))/100);
            }
        } else {
            listsspuissance = null;
        }
        return listsspuissance;
    }

    public static double getBatterieDispo(int idss) throws Exception{
        return Batterie.getBatterieByIdss(idss).getCapacite()/2;
    }
    
    public static double getBesoinetudiant(double besoin,int nbr_etudiant){
        return besoin*nbr_etudiant;
    }

    public int getNumberOfPresentStudents(Timestamp time ,Connection connection){
        if(time.toLocalDateTime().getHour() >= 12){
            //PM
            try {
                if(connection==null)connection = PGSQLConnection.getConnection();
                String sql = "SELECT nbr_presence as nbr " +
                             "FROM v_presence " +
                             "WHERE date_coupure::DATE = ? AND source_solaire = ? AND dj_presence = ? ";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setDate(1, new Date(time.getTime()));
                    preparedStatement.setString(2, this.getNom());
                    preparedStatement.setString(3, "apres_midi");

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            int nbr = resultSet.getInt("nbr");
                            return nbr;
                        }
                        else{
                            return getNumberOfPresentStudentsAvg(time, connection);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            //AM
            try {
                if(connection==null)connection = PGSQLConnection.getConnection();
                String sql = "SELECT nbr_presence as nbr " +
                             "FROM v_presence " +
                             "WHERE date_coupure::DATE = ? AND source_solaire = ? AND dj_presence = ? ";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setDate(1, new Date(time.getTime()));
                    preparedStatement.setString(2, this.getNom());
                    preparedStatement.setString(3, "matin");

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            int nbr = resultSet.getInt("nbr");
                            return nbr;
                        }
                        else{
                            return getNumberOfPresentStudentsAvg(time, connection);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return 0;
    }
    
    public static String getJourPardate(Timestamp date){ //ny 1 no alahady fa tsy 0
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String[] daysOfWeek = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
        return daysOfWeek[dayOfWeek - 1];
    }
    
    public int getNumberOfPresentStudentsAvg(Timestamp time ,Connection connection){
        if(time.toLocalDateTime().getHour() >= 12){
            //PM
            try {
                if(connection==null)connection = PGSQLConnection.getConnection();
                String sql = "select avg(nbr_presence) as nbr from v_presence where lower(trim(jour)) = lower(?) AND source_solaire = ? AND dj_presence = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, getJourPardate(time));
                    preparedStatement.setString(2, this.getNom());
                    preparedStatement.setString(3, "apres_midi");

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            int nbr = resultSet.getInt("nbr");
                            return nbr;
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            //AM
            try {
                if(connection==null)connection = PGSQLConnection.getConnection();
                String sql = "select avg(nbr_presence) as nbr from v_presence where lower(trim(jour)) = lower(?) AND source_solaire = ? AND dj_presence = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, getJourPardate(time));
                    preparedStatement.setString(2, this.getNom());
                    preparedStatement.setString(3, "matin");

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            int nbr = resultSet.getInt("nbr");
                            return nbr;
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return 0;
    }
    
    public double guessConsommationStudents(String day,Date date_arret) throws Exception {
        List<V_coupure> dd = V_coupure.getAllCoupure(day, this.getNom(),date_arret);
        ArrayList<Double> wattList = new ArrayList<>();
        Connection con = PGSQLConnection.getConnection();

        double totalConsumption = 0;
        for (V_coupure d : dd) {
            double low = 0;
            double high = 240;

            while (true) { 
                double mid = (low + high) / 2;

                Timestamp powerOutageTime = findPowerOutageTime( mid,
                                            new Date(d.getDate_coupure().getTime()), con);
                System.out.println(powerOutageTime);
//                if (powerOutageTime != null && Math.abs(powerOutageTime.getTime() - d.getDate_coupure().getTime()) < 1000 ) {
                  if(Math.abs(low-high)<0.1){ // parce que amin'ny farany entre 0.01 ny difference an'ilay low sy hight tonga dia break
                    break; 
                } else
                if ((powerOutageTime != null && powerOutageTime.getTime() < d.getDate_coupure().getTime())) {
                    high = mid; 
                } else if((powerOutageTime!=null && powerOutageTime.getTime() > d.getDate_coupure().getTime()) || powerOutageTime == null){
                    low = mid; 
                }
            }

            double guess = (low + high) / 2; 
            wattList.add(guess);
        }

        for (Double d : wattList) {
            totalConsumption += d.doubleValue();
        }
        con.close();
        return totalConsumption/wattList.size();
    }

    
    public Timestamp findPowerOutageTime(double studentWatt, Date date,Connection connection)throws Exception{
        if(connection==null)connection=PGSQLConnection.getConnection();
        List<Double> ssdispo = getSourceSolaireDispo(date, this.getId(),connection);
        
        double batteriedispo = getBatterieDispo(this.getId());
        
        double besoinbatt = 0;
        double ssbaterie = 0;
        double time = 0;
        double t = 0;
        LocalDate localDate = date.toLocalDate();
        LocalDate resetTimeDate = localDate.atStartOfDay().toLocalDate();
        Timestamp currentTime = new Timestamp(Date.valueOf(resetTimeDate).getTime());

        LocalDateTime localDateTime = currentTime.toLocalDateTime();
        LocalDateTime modifiedDateTime = localDateTime.plusHours(7);

        Timestamp timestampWithHourAdded = Timestamp.valueOf(modifiedDateTime);
        for (int i = 0; i<ssdispo.size(); i++){
            //mi boucle date coupure dia ny hidirany voalohany dia ny 08h
            timestampWithHourAdded = Timestamp.valueOf(timestampWithHourAdded.toLocalDateTime().plusHours(1));
            if(timestampWithHourAdded.getHours()==13)continue;
            //donc maka ny nbr eleves amin'ny 08h amin'ny boucle voalohany
            double current_nbr_student = getNumberOfPresentStudents(timestampWithHourAdded, connection);
            if(studentWatt*current_nbr_student > ssdispo.get(i) && batteriedispo > 0){
                besoinbatt = (studentWatt*current_nbr_student) - ssdispo.get(i);
                double batteriePris = 0;
                if(batteriedispo>=besoinbatt){
                    batteriedispo -= besoinbatt;
                    batteriePris = besoinbatt;
                } else {
                    batteriePris = batteriedispo;
                    batteriedispo = 0;
                }
                ssbaterie = batteriePris + ssdispo.get(i);
                if( studentWatt*current_nbr_student > ssbaterie ){
                    t = ssbaterie / (studentWatt*current_nbr_student);
                    time = (t)*3600;
                    return new Timestamp(timestampWithHourAdded.getTime()+new Time((long)time*1000).getTime());
                }
            }
        }
        return null;
    }
}