package myVelib.cardFactory;

import myVelib.userAndCard.*;

/**
 * Creates Vmax cards automatically
 * @author Ahmed Djermani
 *
 */
public class VmaxFactory extends CardFactory {
	
	@Override
	public Vmax createCard() {
		Vmax vmax = new Vmax();
		return vmax;
	}

}
