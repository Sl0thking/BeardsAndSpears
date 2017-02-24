package de.sloth.spearSystems;

import de.sloth.components.SpearBagComp;
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
		SpearBagComp spComp = (SpearBagComp) cevent.getCollisionSrc().getComponent(SpearBagComp.class);
		spComp.setSpears(spComp.getSpears()+1);
		system.getEntityManager().removeEntity(cevent.getCollisionTarget());
	}
}