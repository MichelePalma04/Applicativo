package model;

public class Voto {
    private Giudice giudice;
    private Team team;
    private int votazione;

    public Voto(Giudice giudice, Team team, int votazione) {
        this.giudice = giudice;
        this.team = team;
        this.votazione = votazione;
    }
}
