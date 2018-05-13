package myVelib.cardFactory;

import myVelib.userAndCard.*;

/**
 * Creates noCards automatically
 * @author Ahmed Djermani
 *
 */
public class NoCardFactory extends CardFactory {

	@Override
	public NoCard createCard() {
		NoCard noCard = new NoCard();
		return noCard;
	}

}
