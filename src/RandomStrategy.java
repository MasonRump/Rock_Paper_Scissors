import java.util.Random;

public class RandomStrategy implements Strategy {

    private Random rand = new Random();

    @Override
    public String getMove(String playerMove) {
        int choice = rand.nextInt(3);

        if (choice == 0) return "R";
        else if (choice == 1) return "P";
        else return "S";
    }
}
