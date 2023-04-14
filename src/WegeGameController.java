import java.util.List;

public class WegeGameController {

    private WegeButton currentButton;

    private WegeButton nextCardButton;

    private WegeCard nextCard;

    private WegeCard currentCard;

    private List<WegeCard> adjacentCards;

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

            }
            case BRIDGE -> {
                return true;
            }
        }
        return false;
    }

    public void handle() {
        switch (nextCard.getCardType()) {
            case COSSACK -> {
                if (!adjacentCards.isEmpty()) {
                    placeCard(currentButton, nextCardButton);
                }
            }
            case BRIDGE -> {
                if (currentCard.getCardType() != WegeCard.CardType.COSSACK) {
                    swapCard(currentButton, nextCardButton);
                } else if (!adjacentCards.isEmpty()) {
                    placeCard(currentButton, nextCardButton);
                }
            }
            case LAND, WATER -> {
                for (WegeCard adjacentCard : adjacentCards) {
                    WegeCard.CardType adjacentCardType = adjacentCard.getCardType();
                    if (nextCard.getCardType() == adjacentCardType
                            || adjacentCardType == WegeCard.CardType.BRIDGE)
                        placeCard(currentButton, nextCardButton);
                }
            }
        }
    }

    /**
     * Place a card to a board button then clear the card in the next card button.
     *
     * @param boardButton the button on the playing board to place a card.
     * @param nextCardButton the next card button.
     */
    private void placeCard(WegeButton boardButton, WegeButton nextCardButton) {
        WegeCard nextCard = nextCardButton.getCard();
        if (nextCard != null) {
            boardButton.setCard(nextCard);
            nextCardButton.setCard(null);
        }
    }

    /**
     * Swap a card from the board button with the next card button.
     *
     * @param boardButton the button on the playing board to swap a card
     * @param nextCardButton the next card button.
     */
    private void swapCard(WegeButton boardButton, WegeButton nextCardButton) {
        WegeCard nextCard = nextCardButton.getCard();
        if (nextCard != null) {
            nextCardButton.setCard(boardButton.getCard());
            boardButton.setCard(nextCard);
        }
    }

}
