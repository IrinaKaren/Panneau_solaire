package model;

import dbaccess.PGSQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class V_coupure {
    private String jour_semaine;
    private Timestamp date_coupure;
    private String source_solaire;
    private int present_matin;
    private int present_aprem;
    private String heure_coupure;

    // Getters and setters
    public String getJour_semaine() {
        return jour_semaine;
    }

    public void setJour_semaine(String jour_semaine) {
        this.jour_semaine = jour_semaine;
    }

    public Timestamp getDate_coupure() {
        return date_coupure;
    }

    public void setDate_coupure(Timestamp date_coupure) {
        this.date_coupure = date_coupure;
    }

    public String getSource_solaire() {
        return source_solaire;
    }

    public void setSource_solaire(String source_solaire) {
        this.source_solaire = source_solaire;
    }

    public int getPresent_matin() {
        return present_matin;
    }

    public void setPresent_matin(int present_matin) {
        this.present_matin = present_matin;
    }

    public int getPresent_aprem() {
        return present_aprem;
    }

    public void setPresent_aprem(int present_aprem) {
        this.present_aprem = present_aprem;
    }

    public String getHeure_coupure() {
        return heure_coupure;
    }

    public void setHeure_coupure(String heure_coupure) {
        this.heure_coupure = heure_coupure;
    }
    
    // Constructor
    public V_coupure(){}
    
    public V_coupure(String jour_semaine, Timestamp date_coupure, String source_solaire, int present_matin, int present_aprem) {
        this.setJour_semaine(jour_semaine);
        this.setDate_coupure(date_coupure);
        this.setSource_solaire(source_solaire);
        this.setPresent_matin(present_matin);
        this.setPresent_aprem(present_aprem);
        this.setHeure_coupure(getHeure(date_coupure));
    }
  
    // Functions
    public static List<Timestamp> getDates(String jour_semaine, String ss, String dj,Date date_arret) {
        List<Timestamp> dates = new ArrayList<>();
        try {
            Connection connection = PGSQLConnection.getConnection();
            String sql = "SELECT date_coupure " +
                         "FROM v_presence " +
                         "WHERE lower(trim(jour)) = lower(?) AND source_solaire = ? AND dj_presence = ? AND date_coupure <= ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, jour_semaine);
                preparedStatement.setString(2, ss);
                preparedStatement.setString(3, dj);
                preparedStatement.setTimestamp(4, V_coupure.addTimeToSqlDate(date_arret));
                
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Timestamp date = resultSet.getTimestamp("date_coupure");
                        dates.add(date);
                    }
                }
            }
            connection.close();
            return dates;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dates;
    }

    public static List<Integer> getNbrPresents(String jour_semaine, String ss, String dj) {
        List<Integer> nbrPresents = new ArrayList<>();
        try {
            Connection connection = PGSQLConnection.getConnection();
            String sql = "SELECT  nbr_presence as nbr " +
                         "FROM v_presence " +
                         "WHERE lower(trim(jour)) = lower(?) AND source_solaire = ? AND dj_presence = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, jour_semaine);
                preparedStatement.setString(2, ss);
                preparedStatement.setString(3, dj);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int nbr_present = resultSet.getInt("nbr");
                        nbrPresents.add(nbr_present);
                    }
                }
            }
            connection.close();
            return nbrPresents;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return nbrPresents;
    }

    public static String getHeure(Timestamp date) {
        String heure = "";
        try {
            Connection connection = PGSQLConnection.getConnection();
            String sql = "SELECT EXTRACT(HOUR FROM CAST(? AS TIMESTAMP)) || ':' || EXTRACT(MINUTE FROM CAST(? AS TIMESTAMP)) || ':' || EXTRACT(SECOND FROM CAST(? AS TIMESTAMP)) as heure";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setTimestamp(1, date);
                preparedStatement.setTimestamp(2, date);
                preparedStatement.setTimestamp(3, date);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        heure = resultSet.getString("heure");
                    }
                }
            }
            connection.close();
            return heure;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return heure;
    }
    
    public static List<V_coupure> getNbrpresentByTemp(int idss){
        List<V_coupure> listpresence = new ArrayList<>();
        return listpresence;
    }
    
    public static Timestamp addTimeToSqlDate(Date sqlDate) {
        try {
            // Convert java.sql.Date to java.util.Date
            java.util.Date utilDate = new java.util.Date(sqlDate.getTime());

            // Add '23:59:59' to the date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = dateFormat.format(utilDate) + " 23:59:59";
            java.util.Date extendedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);

            // Convert back to java.sql.Timestamp
            return new Timestamp(extendedDate.getTime());
        } catch (ParseException e) {
            // Handle exception (e.g., invalid date format)
            e.printStackTrace();
            return null;
        }
    }
    
    public static List<V_coupure> getAllCoupure(String jour, String ss,Date date_arret) throws Exception {
        List<V_coupure> listcoupure = new ArrayList<>();
        List<Timestamp> listDateCoupure = V_coupure.getDates(jour, ss, "matin",date_arret);
        List<Integer> listNbrPresentMatin = V_coupure.getNbrPresents(jour, ss, "matin");
        List<Integer> listNbrPresentAprem = V_coupure.getNbrPresents(jour, ss, "apres_midi");


        for (int i = 0; i < listDateCoupure.size(); i++) {
            V_coupure coupure = new V_coupure(jour, listDateCoupure.get(i), ss, listNbrPresentMatin.get(i), listNbrPresentAprem.get(i));
            listcoupure.add(coupure);
        }

        return listcoupure;
    }
    
    public static List<V_coupure> getAllClasse(String jour, String ss) throws Exception {
        List<V_coupure> listclasse = new ArrayList<>();
        Connection connection = PGSQLConnection.getConnection();
        String sql = "SELECT " +
                    "    source_solaire," +
                    "    classe," +
                    "    nbr_personne," +
                    "    date_presence," +
                    "    dj_presence," +
                    "    nbr_presence" +
                    "    FROM" +
                    "    v_coupure " +
                    "    WHERE" +
                    "    source_solaire = ? " +
                    "    GROUP BY " +
                    "    source_solaire, " +
                    "    classe, " +
                    "    nbr_personne, " +
                    "    date_presence, " +
                    "    dj_presence, " +
                    "    nbr_presence";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            V_coupure classe = new V_coupure();
            classe.setSource_solaire(resultSet.getString("classe"));
            classe.setDate_coupure(resultSet.getTimestamp("date_presence"));
            classe.setPresent_matin(resultSet.getInt("nbr_presence"));
            listclasse.add(classe);
        }
        connection.close();
        return listclasse;
    }
}

