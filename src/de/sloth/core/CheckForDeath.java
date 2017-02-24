package de.sloth.core;

import de.sloth.component.HealthComp;
import de.sloth.entity.Entity;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;

public class CheckForDeath implements IBehavior {

	
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
