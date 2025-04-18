package model;

import java.util.ArrayList;

public class Team {
    private String nomeTeam;
    private ArrayList<Documento> documenti;
    private ArrayList<Partecipante> partecipanti;

    public Team (String nomeTeam, ArrayList<Partecipante> partecipanti) {
        this.nomeTeam = nomeTeam;
        this.partecipanti = partecipanti;
        this.documenti = new ArrayList<>();
    }

    public Team (String nomeTeam, Partecipante p) {
        this.nomeTeam = nomeTeam;
        partecipanti = new ArrayList<>();
        partecipanti.add(p);
        this.documenti = new ArrayList<>();

    }

    public void addPartecipanti(Partecipante newP) {
        if (!partecipanti.contains(newP)) {
            partecipanti.add(newP);
        }
    }

    public void addDocumento(Documento newDoc) {
        if (!this.documenti.contains(newDoc)) {
            this.documenti.add(newDoc);
        }
    }

    public ArrayList<Partecipante> getPartecipanti() {
        return partecipanti;
    }

    public String getNomeTeam() {
        return nomeTeam;
    }

    public ArrayList<Documento> getDocumenti() {
        return documenti;
    }
}
