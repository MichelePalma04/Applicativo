package model;

/**
 * Rappresenta un commento scritto da un giudice su un documento.
 * Ogni commento è associato a un giudice, un documento e contiene il testo del commento stesso.
 */
public class CommentoGiudice {
    /** Giudice che ha scritto il commento. */
    private Giudice giudice;

    /** Documento su cui è stato scritto il commento. */
    private Documento documento;

    /** Testo del commento. */
    private String testo;

    /**
     * Costruttore della classe CommentoGiudice.
     * @param giudice Giudice autore del commento
     * @param documento Documento a cui si riferisce il commento
     * @param testo Testo del commento
     */
    public CommentoGiudice(Giudice giudice, Documento documento, String testo) {
        this.giudice = giudice;
        this.documento = documento;
        this.testo = testo;
    }

    /**
     * Restituisce il giudice autore del commento.
     * @return Giudice che ha scritto il commento
     */
    public Giudice getGiudice() {
        return giudice;
    }

    /**
     * Restituisce il documento associato al commento.
     * @return Documento su cui è stato scritto il commento
     */
    public Documento getDocumento() {
        return documento;
    }

    /**
     * Restituisce il testo del commento.
     * @return Testo del commento
     */
    public String getTesto() {
        return testo;
    }
}
