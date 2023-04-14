import java.util.List;

public class WegeGameLegalMove {

    public static boolean isLegalPlacement(WegeCard currentCard, WegeCard nextCard, List<WegeCard> adjacentCards) {
        switch (nextCard.getCardType()) {
            case COSSACK, BRIDGE -> {
                if (!adjacentCards.isEmpty()) return true;
            }
            case LAND, WATER -> {
                for (WegeCard adjacentCard : adjacentCards) {
                    WegeCard.CardType adjacentCardType = adjacentCard.getCardType();
                    if (nextCard.getCardType() == adjacentCardType
                            || adjacentCardType == WegeCard.CardType.BRIDGE)
                        return true;
                }
            }
        }
        return false;
    }

    public static boolean isLegalSwap(WegeCard currentCard, WegeCard nextCard) {
        if (nextCard.getCardType() != WegeCard.CardType.BRIDGE) return false;
        switch (currentCard.getCardType()) {
            case LAND, WATER -> {
                // TODO: Implement for rule for facing gnome
            }
            case BRIDGE -> {
                return true;
            }
        }
        return false;
    }

}
