package model;

public class Classe {

    private Long id;
    private String nom;
    private int nbrPersonne;

    // Constructor
    public Classe() {
    }

    public Classe(Long id, String nom, int nbrPersonne) {
        this.id = id;
        this.nom = nom;
        this.nbrPersonne = nbrPersonne;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNbrPersonne() {
        return nbrPersonne;
    }

    public void setNbrPersonne(int nbrPersonne) {
        this.nbrPersonne = nbrPersonne;
    }

    @Override
    public String toString() {
        return "Classe{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", nbrPersonne=" + nbrPersonne +
                '}';
    }
}

