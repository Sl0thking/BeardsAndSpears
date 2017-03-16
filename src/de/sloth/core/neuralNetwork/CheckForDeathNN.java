package de.sloth.core.neuralNetwork;

import de.sloth.component.HealthComp;
import de.sloth.component.ScoreComp;
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
		ScoreComp scComp = (ScoreComp) player.getComponent(ScoreComp.class);
		if(hComp.getLifes() == 0) {
			System.out.println("DIED...");
			nnComp.getNetwork().getSequence().setFitnessLvl(scComp.getScoreProperty().intValue());
			system.getEventQueue().add(new GeneticalEvent("CheckOrMutate"));
		} else {
			scComp.getScoreProperty().set(scComp.getScore()+1);
		}
	}

	@Override
	public void execute(GameSystem arg0, GameEvent arg1) {}
}