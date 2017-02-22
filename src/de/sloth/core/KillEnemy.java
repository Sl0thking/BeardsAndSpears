package de.sloth.core;

import de.sloth.component.ScoreComp;
import de.sloth.entity.Entity;
import de.sloth.system.game.collision.commonBehavior.Despawn;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;

public class KillEnemy extends Despawn {

	@Override
	public void execute(GameSystem system, GameEvent expectedEvent) {
		Entity player = system.getEntityManager().getActivePlayabaleEntity();
		ScoreComp scoreComp = (ScoreComp) player.getComponent(ScoreComp.class);
		scoreComp.setScore(scoreComp.getScore()+1);
		super.execute(system, expectedEvent);
	}
}
