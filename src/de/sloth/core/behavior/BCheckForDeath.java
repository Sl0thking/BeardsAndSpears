package de.sloth.core.behavior;

import de.sloth.component.HealthComp;
import de.sloth.entity.Entity;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;

/**
 * Checks if player died and exit system.
 * @author Kevin Jolitz
 * @version 1.0.0
 * @date 18.05.2017
 */
public class BCheckForDeath implements IBehavior {
	
	@Override
	public void execute(GameSystem system) {
		Entity player = system.getEntityManager().getActivePlayabaleEntity();
		HealthComp hComp = (HealthComp) player.getComponent(HealthComp.class);
		if(hComp.getLifes() == 0) {
			System.exit(0);
		}
	}

	@Override
	public void execute(GameSystem arg0, GameEvent arg1) {}
}