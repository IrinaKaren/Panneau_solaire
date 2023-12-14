package model;

import java.sql.Timestamp;

public class Presence {

    private Long id;
    private int idClasse;
    private Timestamp datePresence;
    private String demiJournee;
    private int nbr;

    // Constructor
    public Presence() {
    }

    public Presence(Long id, int idClasse, Timestamp datePresence, String demiJournee, int nbr) {
        this.id = id;
        this.idClasse = idClasse;
        this.datePresence = datePresence;
        this.demiJournee = demiJournee;
        this.nbr = nbr;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIdClasse() {
        return idClasse;
    }

    public void setIdClasse(int idClasse) {
        this.idClasse = idClasse;
    }

    public Timestamp getDatePresence() {
        return datePresence;
    }

    public void setDatePresence(Timestamp datePresence) {
        this.datePresence = datePresence;
    }

    public String getDemiJournee() {
        return demiJournee;
    }

    public void setDemiJournee(String demiJournee) {
        this.demiJournee = demiJournee;
    }

    public int getNbr() {
        return nbr;
    }

    public void setNbr(int nbr) {
        this.nbr = nbr;
    }

    @Override
    public String toString() {
        return "Presence{" +
                "id=" + id +
                ", idClasse=" + idClasse +
                ", datePresence=" + datePresence +
                ", demiJournee='" + demiJournee + '\'' +
                ", nbr=" + nbr +
                '}';
    }
}

