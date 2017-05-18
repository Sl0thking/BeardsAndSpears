package de.sloth.core.behavior;

import java.util.List;
import java.util.Random;

import de.sloth.core.EntityGenerator;
import de.sloth.core.component.VikingEnemyComp;
import de.sloth.entity.Entity;
import de.sloth.spears.event.ThrowSpearEvent;
import de.sloth.system.game.core.ConfigLoader;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;
import de.sloth.system.game.core.IEntityManagement;
import de.sloth.system.game.moveSystem.Direction;
import de.sloth.system.game.moveSystem.PossibleMoveEvent;

/**
 * Behavior to controll enemies on the field.
 * @author Kevin Jolitz
 * @version 1.0.0
 * @date 18.05.2017
 *
 */
public class BControllEnemy implements IBehavior {

	@Override
	public void execute(GameSystem system) {
		Random rand = new Random();
		List<Entity> enemies = IEntityManagement.filterEntitiesByComponent(system.getEntityManager().getAllEntities(), VikingEnemyComp.class);
		if(enemies.size() < Integer.valueOf(ConfigLoader.getInstance().getConfig("maxEnemies", "7"))) {
			int nenemy = rand.nextInt(200);
			if(nenemy == 0) {
				Direction direct = Direction.CENTER;
				int nDirection = rand.nextInt(3);
				if(nDirection == 0) {
					direct = Direction.LEFT;
				} else if(nDirection == 1) {
					direct = Direction.RIGHT;
				}
				system.getEntityManager().addEntity(EntityGenerator.getInstance().generateEnemy(direct));
			} 
		}
		
		for(Entity enemy : enemies) {
			VikingEnemyComp seo = (VikingEnemyComp) enemy.getComponent(VikingEnemyComp.class);
			if(seo.getCurrTickDelay() == 0) {
				Direction direct = Direction.RIGHT;
				boolean isLeft = rand.nextBoolean();
				if(isLeft) {
					direct = Direction.LEFT;
				}
				system.getEventQueue().add(new PossibleMoveEvent(enemy, direct));
				int throwSpear = rand.nextInt(35);
				if(throwSpear == 0) {
					system.getEventQueue().add(new ThrowSpearEvent(enemy));
				}
				seo.setCurrTickDelay(seo.getTickDelay());
			} else {
				seo.setCurrTickDelay(seo.getCurrTickDelay()-1);
			}
		}
	}

	@Override
	public void execute(GameSystem system, GameEvent expectedEvent) {}
}