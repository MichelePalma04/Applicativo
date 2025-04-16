package model;

public class Evento {
    private String titolo;
    private String sede;
    private String dataInizio;
    private String dataFine;
    private int n_Max_Iscritti;
    private int dim_max_team;
    public String inizio_registrazione;
    public String fine_registrazione;
    public String problema;

    public Evento (String titolo, String sede, String dataInizio, String dataFine, int n_Max_Iscritti, int dim_max_team, String inizio_registrazione, String fine_registrazione, String problema) {
        this.titolo = titolo;
        this.sede = sede;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.n_Max_Iscritti = n_Max_Iscritti;
        this.dim_max_team = dim_max_team;
        this.inizio_registrazione = inizio_registrazione;
        this.fine_registrazione = fine_registrazione;
        this.problema = problema;
    }

}