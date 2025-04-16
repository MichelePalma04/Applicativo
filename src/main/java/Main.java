import model.Utente;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

<<<<<<< Updated upstream
        Utente u1 = new Utente("mike@gmail.com", "mikemike");

=======

        Utente u1 = new Utente("Utente 1", "mike@gmail.com", "mikemike");
>>>>>>> Stashed changes
        System.out.println(u1.getLogin());
        System.out.println(u1.getPassword());

    }
}