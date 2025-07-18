
package model;

import java.io.File;
import java.time.LocalDate;

public class Documento {
    private LocalDate data;
    private File file;
    private String commentoGiudice;
    private boolean commentato;
    private Team team;

    public Documento (LocalDate data, File file, Team team) {
        this.data = data;
        this.team = team;
        this.file = file;
        this.commentato = false;
        this.commentoGiudice = "";
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
}
