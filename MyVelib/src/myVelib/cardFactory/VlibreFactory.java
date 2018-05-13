package myVelib.cardFactory;

import myVelib.userAndCard.*;

/**
 * Creates Vilbre cards automatically
 * @author Ahmed Djermani
 *
 */
public class VlibreFactory extends CardFactory {

	@Override
	public Vlibre createCard() {
		Vlibre vlibre = new Vlibre();
		return vlibre;
	}

}
