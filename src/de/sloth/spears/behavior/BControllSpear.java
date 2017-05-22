package de.sloth.spears.behavior;

import java.util.List;

import de.sloth.component.Position3DComp;
import de.sloth.entity.Entity;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;
import de.sloth.system.game.core.IEntityManagement;
import de.sloth.system.game.flying.FlyingComp;
import de.sloth.system.game.moveSystem.Direction;
import de.sloth.system.game.moveSystem.PossibleMoveEvent;

/**
 * Behavior of system who works with spears.
 * Is called every tick and moves spears in the game
 * in the desired direction.
 * 
 * @author Kevin Jolitz
 * @version 1.0.0
 * @date 16.05.2017
 *
 */
public class BControllSpear implements IBehavior {

	@Override
	public void execute(GameSystem system) {
		List<Entity> spears = IEntityManagement.filterEntitiesByComponent(system.getEntityManager().getAllEntities(), FlyingComp.class);
		for(Entity spear : spears) {
			FlyingComp flyComp = (FlyingComp) spear.getComponent(FlyingComp.class);
			Position3DComp posComp = (Position3DComp) spear.getComponent(Position3DComp.class);
			Direction direct = flyComp.getDirect();
			if(direct.equals(Direction.TOP)) {
				if(flyComp.isFlying()) {
					if(posComp.getY() < (480-64)) {
						system.getEventQueue().add(new PossibleMoveEvent(spear, direct));
					} else {
						flyComp.setFlying(false);
					}
				} else {
					if(flyComp.getTickDuration() > 0) {
						flyComp.setTickDuration(flyComp.getTickDuration()-1);
					} else {
						system.getEntityManager().removeEntity(spear);
					}
				}
			}
			if(direct.equals(Direction.BOTTOM)) {
				if(flyComp.isFlying()) {
					if(posComp.getY() > 64) {
						system.getEventQueue().add(new PossibleMoveEvent(spear, direct));
					} else {
						flyComp.setFlying(false);
					}
				} else {
					if(flyComp.getTickDuration() > 0) {
						flyComp.setTickDuration(flyComp.getTickDuration()-1);
					} else {
						system.getEntityManager().removeEntity(spear);
					}
				}
			}
		}
	}

	@Override
	public void execute(GameSystem system, GameEvent expectedEvent) {}
}