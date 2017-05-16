package de.sloth.core;

import de.sloth.score.event.CalcScoreEvent;
import de.sloth.system.game.collision.commonBehavior.Despawn;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;

public class KillEnemy extends Despawn {

	@Override
	public void execute(GameSystem system, GameEvent expectedEvent) {
		system.getEventQueue().add(new CalcScoreEvent(ScoreType.KILL));
		super.execute(system, expectedEvent);
	}
}
