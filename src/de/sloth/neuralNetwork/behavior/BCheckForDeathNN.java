package de.sloth.neuralNetwork.behavior;

import de.sloth.component.HealthComp;
import de.sloth.component.ScoreComp;
import de.sloth.components.NeuralNetworkComp;
import de.sloth.core.ScoreType;
import de.sloth.entity.Entity;
import de.sloth.neuralNetwork.EntityManagerNN;
import de.sloth.neuralNetwork.event.GeneticalEvent;
import de.sloth.score.event.CalcScoreEvent;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;

public class BCheckForDeathNN implements IBehavior {

	@Override
	public void execute(GameSystem system) {
		Entity player = system.getEntityManager().getActivePlayabaleEntity();
		Entity nnEntity = ((EntityManagerNN) system.getEntityManager()).getNNInformation();
		HealthComp hComp = (HealthComp) player.getComponent(HealthComp.class);
		NeuralNetworkComp nnComp = (NeuralNetworkComp) nnEntity.getComponent(NeuralNetworkComp.class);
		ScoreComp scComp = (ScoreComp) player.getComponent(ScoreComp.class);
		if(hComp.getLifes() == 0) {
			nnComp.getNetwork().getSequence().setFitnessLvl(scComp.getScore());
			System.out.println("SCORED: " + nnComp.getNetwork().getSequence());
			system.getEventQueue().add(new GeneticalEvent("CheckOrMutate"));
		} else {
			system.getEventQueue().add(new CalcScoreEvent(ScoreType.SURVIVAL));
		}
	}

	@Override
	public void execute(GameSystem arg0, GameEvent arg1) {}
}