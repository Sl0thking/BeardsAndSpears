package de.sloth.spears.behavior;

import de.sloth.score.component.ScoreType;
import de.sloth.score.event.CalcScoreEvent;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;

/**
 * Behavior to get points when collecting spears.
 * 
 * @author Kevin Jolitz
 * @version 1.0.0
 * @date 22.05.2017
 */
public class BCollectSpearPoints extends BCollectSpear {

	@Override
	public void execute(GameSystem system, GameEvent expectedEvent) {
		system.getEventQueue().add(new CalcScoreEvent(ScoreType.COLLECT));
		super.execute(system, expectedEvent);
	}
}