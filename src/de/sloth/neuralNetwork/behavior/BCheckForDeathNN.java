package de.sloth.neuralNetwork.behavior;

import de.sloth.component.HealthComp;
import de.sloth.component.ScoreComp;
import de.sloth.entity.Entity;
import de.sloth.neuralNetwork.EntityManagerNN;
import de.sloth.neuralNetwork.component.NeuralNetworkComp;
import de.sloth.neuralNetwork.event.GeneticalEvent;
import de.sloth.score.component.ScoreType;
import de.sloth.score.event.CalcScoreEvent;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;

/**
 * System to check player death in neural network mode.
 * 
 * @author Kevin Jolitz
 * @version 1.0.0
 * @date 20.05.2017
 *
 */
public class BCheckForDeathNN implements IBehavior {

	@Override
	public void execute(GameSystem system) {
		Entity player = system.getEntityManager().getActivePlayabaleEntity();
		Entity nnEntity = ((EntityManagerNN) system.getEntityManager()).getNNInformation();
		HealthComp hComp = (HealthComp) player.getComponent(HealthComp.class);
		NeuralNetworkComp nnComp = (NeuralNetworkComp) nnEntity.getComponent(NeuralNetworkComp.class);
		ScoreComp scComp = (ScoreComp) player.getComponent(ScoreComp.class);
		if(hComp.getLifes() <= 0) {
			nnComp.getNetwork().getSequence().setFitnessLvl(scComp.getScore());
			if(!system.isQuiet()) {
				System.out.println("[EndConditionSys::CheckForDeathNN] Current candidate scored: " + nnComp.getNetwork().getSequence().getFitnessLvl());
			}
			system.getEventQueue().add(new GeneticalEvent());
		} else {
			system.getEventQueue().add(new CalcScoreEvent(ScoreType.SURVIVAL));
		}
	}

	@Override
	public void execute(GameSystem arg0, GameEvent arg1) {}
}