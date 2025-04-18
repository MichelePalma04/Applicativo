package model;

import java.util.Date;

public class Evento {
    private String titolo;
    private String sede;
    private Date dataInizio;
    private Date dataFine;
    private int n_Max_Iscritti;
    private int dim_max_team;
    public Date inizio_registrazioni;
    public Date fine_registrazioni;
    public String problema;

    public Evento (String titolo, String sede, Date dataInizio, Date dataFine, int n_Max_Iscritti, int dim_max_team, Date inizio_registrazioni, Date fine_registrazioni, String problema) {
        this.titolo = titolo;
        this.sede = sede;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.n_Max_Iscritti = n_Max_Iscritti;
        this.dim_max_team = dim_max_team;
        this.inizio_registrazioni = inizio_registrazioni;
        this.fine_registrazioni = fine_registrazioni;
        this.problema = problema;
    }

}