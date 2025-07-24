package model;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private String nomeTeam;
    private List<Documento> documenti;
    private List<Partecipante> partecipanti;
    private List<Voto> voti;

    public Team (String nomeTeam, List<Partecipante> partecipanti, List<Voto> voti) {
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

    public List<Partecipante> getPartecipanti() {
        return partecipanti;
    }

    public String getNomeTeam() {
        return nomeTeam;
    }

    public List<Documento> getDocumenti() {
        return documenti;
    }

   @Override
    public String toString() {
        return nomeTeam;
    }

    public double mediaVoti() {
        if(voti == null || voti.isEmpty()) {
            return 0.0;
        }
        int somma = 0;
        for (Voto v: voti) {
            somma += v.getVotazione();
        }
        return (double) somma / voti.size();
    }
}
