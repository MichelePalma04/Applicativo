
package model;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Documento {
    private LocalDate data;
    private File file;
    private List<CommentoGiudice> commentiGiudici = new ArrayList<>();
    private boolean commentato;
    private Team team;
    private int id;

    public Documento (LocalDate data, File file, Team team) {
        this.data = data;
        this.team = team;
        this.file = file;
        this.commentato = false;
    }
    public LocalDate getData() {
        return data;
    }

    public File getFile() {
        return file;
    }

    public Team getTeam() {
        return team;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<CommentoGiudice> getCommentiGiudici() {
        return commentiGiudici;
    }

    public void setCommentiGiudici(List<CommentoGiudice> commentiGiudici) {
        this.commentiGiudici = commentiGiudici;
    }

    public List<CommentoGiudice> getCommentiGiudice() {
        return commentiGiudici;
    }

    public void aggiungiCommentoGiudice(CommentoGiudice commento) {
        this.commentiGiudici.add(commento);
    }
}

