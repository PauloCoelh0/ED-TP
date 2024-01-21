package capturetheflag.game;

public class GameRules {
    public static boolean isLocationOccupied(int location, Player player1, Player player2, int enemyFlagLocation) {
        // Pode ser ocupada se for a localização da bandeira inimiga
        if (location == enemyFlagLocation) {
            return false;
        }

        // Verificar bots do player1
        for (int i = 0; i < player1.getBots().size(); i++) {
            Bot bot = player1.getBots().get(i);
            if (bot.getLocation() == location) {
                return true;
            }
        }

        // Verificar bots do player2
        for (int i = 0; i < player2.getBots().size(); i++) {
            Bot bot = player2.getBots().get(i);
            if (bot.getLocation() == location) {
                return true;
            }
        }

        return false;
    }
}
