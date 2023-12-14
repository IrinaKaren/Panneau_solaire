package model;

import dbaccess.PGSQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class Coupure {

    private int id;
    private int idSS;
    private Timestamp dateCoupure;
    private String jour;

    // Constructor
    public Coupure() {
    }

    public Coupure(int id, int idSS, Timestamp dateCoupure, String jour) {
        this.id = id;
        this.idSS = idSS;
        this.dateCoupure = dateCoupure;
        this.jour = jour;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdSS() {
        return idSS;
    }

    public void setIdSS(int idSS) {
        this.idSS = idSS;
    }

    public Timestamp getDateCoupure() {
        return dateCoupure;
    }

    public void setDateCoupure(Timestamp dateCoupure) {
        this.dateCoupure = dateCoupure;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    @Override
    public String toString() {
        return "Coupure{" +
                "id=" + id +
                ", idSS=" + idSS +
                ", dateCoupure=" + dateCoupure +
                ", jour='" + jour + '\'' +
                '}';
    }
    
    public void insert() {
        try (Connection connection = PGSQLConnection.getConnection()) {
            String sql = "INSERT INTO coupure (idSS, date_coupure, jour) " +
                         "VALUES " +
                         "(?, ?, lower(to_char(?::timestamp, 'Day')))";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, this.getId());                
                preparedStatement.setTimestamp(2, this.getDateCoupure());
                preparedStatement.setTimestamp(3, this.getDateCoupure());

                preparedStatement.executeUpdate();
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

