package controller;
import model .*;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private static final List<Utente> utentiRegistrati = new ArrayList<Utente>();

    public static boolean registraUtente (String email, String password) {
        for (Utente u : utentiRegistrati) {
            if (u.getLogin().equals(email)){
                return false; //utente già presente
            }
        }
        utentiRegistrati.add(new Utente(email, password));
        return true;
    }
}
