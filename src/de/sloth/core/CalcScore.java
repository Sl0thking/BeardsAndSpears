package de.sloth.core;

import de.sloth.component.ScoreComp;
import de.sloth.entity.Entity;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;

public class CalcScore implements IBehavior {

	private int calls = 0;
	
	@Override
	public void execute(GameSystem system) {}

	@Override
	public void execute(GameSystem system, GameEvent expectedEvent) {
		CalcScoreEvent cse = (CalcScoreEvent) expectedEvent;
		Entity player = system.getEntityManager().getActivePlayabaleEntity();
		if(player != null) {
			ScoreComp scComp = (ScoreComp) player.getComponent(ScoreComp.class);
			if(cse.getsType().equals(ScoreType.SURVIVAL)) {
				this.calls += 1;
				if(calls > 199) {
					scComp.setScore(scComp.getScore() + 1);
					calls = 0;
				}
			} else if (cse.getsType().equals(ScoreType.KILL)) {
				scComp.setScore(scComp.getScore() + 100);
				//collect
			} else {
				scComp.setScore(scComp.getScore() + 50);
			}
		}
	}
}