package de.sloth.spears.behavior;

import de.sloth.components.SpearBagComp;
import de.sloth.system.game.collision.CollisionEvent;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;

/**
 * Behavior for a system which works with spears.
 * Is executed when a CollisionEvent between a spear 
 * and a entity who can carry spears (entitty contains SpearBagComp) 
 * occured. Removes spear from the game 
 * and increment spears in SpearBag.
 * 
 * @author Kevin Jolitz
 * @version 1.0.0
 * @date 16.05.2017
 */
public class BCollectSpear implements IBehavior {

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