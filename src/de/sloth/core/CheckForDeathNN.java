package de.sloth.core;

import de.sloth.component.HealthComp;
import de.sloth.components.NeuralNetworkComp;
import de.sloth.entity.Entity;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;

public class CheckForDeathNN implements IBehavior {

	@Override
	public void execute(GameSystem system) {
		Entity player = system.getEntityManager().getActivePlayabaleEntity();
		Entity nnEntity = ((NNEntityManager) system.getEntityManager()).getNNInformation();
		HealthComp hComp = (HealthComp) player.getComponent(HealthComp.class);
		NeuralNetworkComp nnComp = (NeuralNetworkComp) nnEntity.getComponent(NeuralNetworkComp.class);
		if(hComp.getLifes() == 0) {
			System.out.println("DIED...");
			nnComp.setIterations(nnComp.getIterations()-1);
			if(nnComp.getIterations() > 0) {
				system.getEventQueue().add(new StartGameEvent());
			} else {
				System.out.println("Played: " + nnComp.getIterations() + " times.");
				System.exit(0);
			}
		}
	}

	@Override
	public void execute(GameSystem arg0, GameEvent arg1) {}
}