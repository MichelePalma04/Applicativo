package model;

import java.util.ArrayList;

public class Team {
    private String nomeTeam;
    private ArrayList<Documento> documenti;

    public Team (String nomeTeam) {
        this.nomeTeam = nomeTeam;
        this.documenti = new ArrayList<>();
    }

    public void addDocumento(Documento newDoc) {
        if (!this.documenti.contains(newDoc)) {
            this.documenti.add(newDoc);
        }
    }

    public String getNomeTeam() {
        return nomeTeam;
    }

    public ArrayList<Documento> getDocumenti() {
        return documenti;
    }
}
