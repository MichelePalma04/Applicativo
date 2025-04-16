import model.Giudice;
import model.Utente;
import model.Team;
import model.Voto;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Utente u1 = new Utente("mike@gmail.com", "mikemike");

        System.out.println(u1.getLogin());
        System.out.println(u1.getPassword());

        Giudice g1 = new Giudice("micheleoalma@gmail.com", "mike");
        Team team1 = new Team("Sport");
        Voto v1 = new Voto(g1, team1, 9);

    }
}