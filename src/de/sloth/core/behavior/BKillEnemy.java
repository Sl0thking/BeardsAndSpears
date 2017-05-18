package de.sloth.core.behavior;

import de.sloth.score.component.ScoreType;
import de.sloth.score.event.CalcScoreEvent;
import de.sloth.system.game.collision.commonBehavior.Despawn;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.soundSystem.PlaySoundEvent;

/**
 * Behavior which extends the KillEnemy behavior. 
 * Calls CalcScore and PlaySound System for their behavior.
 * 
 * @author Kevin Jolitz
 * @version 1.0.0
 * @date 18.05.2017
 *
 */
public class BKillEnemy extends Despawn {

	@Override
	public void execute(GameSystem system, GameEvent expectedEvent) {
		system.getEventQueue().add(new CalcScoreEvent(ScoreType.KILL));
		system.getEventQueue().add(new PlaySoundEvent("pain"));
		super.execute(system, expectedEvent);
	}
}
