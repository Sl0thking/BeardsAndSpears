package de.sloth.spears.behavior;

import de.sloth.score.component.ScoreType;
import de.sloth.score.event.CalcScoreEvent;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;

public class BCollectSpearPoints extends BCollectSpear {

	@Override
	public void execute(GameSystem system, GameEvent expectedEvent) {
		system.getEventQueue().add(new CalcScoreEvent(ScoreType.COLLECT));
		super.execute(system, expectedEvent);
	}
}