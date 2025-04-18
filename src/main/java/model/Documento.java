
package model;

import java.time.LocalDate;

public class Documento {
    private LocalDate data;
    private String documento;
    private Team team;

    public Documento (LocalDate data, String documento, Team team) {
        this.data = data;
        this.documento = documento;
        this.team = team;
    }
    public LocalDate getData() {
        return data;
    }
    public String getDocumento() {
        return documento;
    }



}
