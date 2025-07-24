package model;

public class CommentoGiudice {
    private String nomeGiudice;
    private String testo;

    public CommentoGiudice(String nomeGiudice, String testo) {
        this.nomeGiudice = nomeGiudice;
        this.testo = testo;
    }

    public String getNomeGiudice() {
        return nomeGiudice;
    }

    public String getTesto() {
        return testo;
    }
}
