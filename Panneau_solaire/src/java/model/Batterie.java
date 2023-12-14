package model;

import dbaccess.PGSQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Batterie {

    private int id;
    private int idSS;
    private double capacite;

    // Constructor
    public Batterie() {
    }

    public Batterie(int id, int idSS, double capacite) {
        this.id = id;
        this.idSS = idSS;
        this.capacite = capacite;
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

    public double getCapacite() {
        return capacite;
    }

    public void setCapacite(double capacite) {
        this.capacite = capacite;
    }

    @Override
    public String toString() {
        return "Batterie{" +
                "id=" + id +
                ", idSS=" + idSS +
                ", capacite=" + capacite +
                '}';
    }
    
    public static Batterie getBatterieByIdss(int idss) throws Exception {
        Connection connection = PGSQLConnection.getConnection();
        Batterie batterie = null;
        String sql = "SELECT id, idss, capacite FROM batterie WHERE idss = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idss);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    batterie = new Batterie();
                    batterie.setId(resultSet.getInt("id"));
                    batterie.setIdSS(resultSet.getInt("idss"));
                    batterie.setCapacite(resultSet.getDouble("capacite"));
                }
            }
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return batterie;
    }
}

