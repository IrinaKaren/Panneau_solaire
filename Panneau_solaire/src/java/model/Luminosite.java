package model;

import dbaccess.PGSQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class Luminosite {
    private Long id;
    private Timestamp dateLuminosite;
    private double capacite;

    // Constructor
    public Luminosite() {
    }

    public Luminosite(Long id, Timestamp dateLuminosite, double capacite) {
        this.id = id;
        this.dateLuminosite = dateLuminosite;
        this.capacite = capacite;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getDateLuminosite() {
        return dateLuminosite;
    }

    public void setDateLuminosite(Timestamp dateLuminosite) {
        this.dateLuminosite = dateLuminosite;
    }

    public double getCapacite() {
        return capacite;
    }

    public void setCapacite(double capacite) {
        this.capacite = capacite;
    }

    @Override
    public String toString() {
        return "Luminosite{" +
                "id=" + id +
                ", dateLuminosite=" + dateLuminosite +
                ", capacite=" + capacite +
                '}';
    }
}

