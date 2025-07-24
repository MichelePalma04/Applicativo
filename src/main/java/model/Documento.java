
package model;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Documento {
    private LocalDate data;
    private File file;
    private List<CommentoGiudice> commentiGiudici = new ArrayList<>();
    private Team team;
    private int id;
    private String loginPartecipante;

    public Documento (LocalDate data, File file, Team team, String loginPartecipante) {
        this.data = data;
        this.team = team;
        this.file = file;
        this.loginPartecipante = loginPartecipante;
    }

    public Documento (LocalDate data, File file, Team team) {
        this.data = data;
        this.team = team;
        this.file = file;
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

    public void aggiungiCommentoGiudice(CommentoGiudice commento) {
        this.commentiGiudici.add(commento);
    }
    public String getLoginPartecipante() { return loginPartecipante; }
    public void setLoginPartecipante(String loginPartecipante) { this.loginPartecipante = loginPartecipante; }
}

