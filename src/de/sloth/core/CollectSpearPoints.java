package de.sloth.core;

import de.sloth.score.event.CalcScoreEvent;
import de.sloth.spears.behavior.BCollectSpear;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;

public class CollectSpearPoints extends BCollectSpear {

	@Override
	public void execute(GameSystem system, GameEvent expectedEvent) {
		system.getEventQueue().add(new CalcScoreEvent(ScoreType.COLLECT));
		super.execute(system, expectedEvent);
	}
	
}
