package model;

import java.util.ArrayList;

public class Team {
    private String nomeTeam;
    private ArrayList<Documento> documenti;
    private ArrayList<Partecipante> partecipanti;
    private ArrayList<Voto> voti;

    public Team (String nomeTeam, ArrayList<Partecipante> partecipanti, ArrayList<Voto> voti) {
        this.nomeTeam = nomeTeam;
        this.partecipanti = partecipanti;
        this.documenti = new ArrayList<>();
        this.voti = voti;
    }

    public Team (String nomeTeam, Partecipante p, Voto v) {
        this.nomeTeam = nomeTeam;
        partecipanti = new ArrayList<>();
        partecipanti.add(p);
        this.documenti = new ArrayList<>();
        voti = new ArrayList<>();
        voti.add(v);
    }

    public void addVoto (Voto newV) {
        if (!voti.contains(newV)) {
            voti.add(newV);
        }
    }

    public ArrayList<Voto> getVoti() {
        return voti;
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

    @Override
    public String toString(){
        return "Team: " + getNomeTeam() + "n partecipanti= " +partecipanti.size()+ "n_voti= "+voti.size();
    }
}
