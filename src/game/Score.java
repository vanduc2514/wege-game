package game;

import javafx.util.Pair;

import java.util.Map;
import java.util.stream.Collectors;

public record Score(Player player) {

    public int getSideConnectedScore() {
        return scoreSideConnected(player.getMaximumEdgesTouched());
    }

    public int getCentralGroundScore() {
        return scoreCentralGround(player.getCentralGround());
    }

    public Map<Integer, Pair<Integer, Integer>> getIntersectionOfGnomeScore() {
        return player.getFacingGnomeGroup().entrySet().stream()
                .filter(e -> e.getKey() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                    int gnomeGroup = e.getKey();
                    int groupCount = e.getValue();
                    int score = scoreCentralGround(gnomeGroup) * groupCount;
                    return new Pair<>(groupCount, score);
                }));
    }

    public int getCossackScore() {
        return player.getCossackCardsPlayed();
    }

    public int total() {
        int gnomeScores = getIntersectionOfGnomeScore().values().stream()
                .map(Pair::getValue).mapToInt(Integer::intValue).sum();
        return getSideConnectedScore()
                + getCentralGroundScore()
                + gnomeScores
                + player.getCossackCardsPlayed();
    }

    public static int scoreCentralGround(int numberOfCentralGroundCreated) {
        return 4 * numberOfCentralGroundCreated;
    }

    public static int scoreSideConnected(int maximumEdgesTouched) {
        return switch (maximumEdgesTouched) {
            case 2 -> 4;
            case 3 -> 7;
            case 4 -> 12;
            default -> 0;
        };
    }

    public static int scoreGnomeFacing(int gnomeInGroup) {
        return switch (gnomeInGroup) {
            case 2 -> 2;
            case 3 -> 5;
            case 4 -> 8;
            default -> 0;
        };
    }
}
