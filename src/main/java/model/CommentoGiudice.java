package model;

public class CommentoGiudice {
    private Giudice giudice;
    private Documento documento;
    private String testo;

    public CommentoGiudice(Giudice giudice, Documento documento, String testo) {
        this.giudice = giudice;
        this.documento = documento;
        this.testo = testo;
    }

    public Giudice getGiudice() {
        return giudice;
    }

    public Documento getDocumento() {
        return documento;
    }

    public String getTesto() {
        return testo;
    }
}
