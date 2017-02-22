package de.sloth.testgame.main;

import de.sloth.system.game.collision.CollisionEvent;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;

public class CollectSpear implements IBehavior {

	@Override
	public void execute(GameSystem system) {}

	@Override
	public void execute(GameSystem system, GameEvent expectedEvent) {
		CollisionEvent cevent = (CollisionEvent) expectedEvent;
		SlothComp scomp = (SlothComp) cevent.getCollisionSrc().getComponent(SlothComp.class);
		scomp.setSpears(scomp.getSpears()+1);
		system.getEntityManager().removeEntity(cevent.getCollisionTarget());
	}
}