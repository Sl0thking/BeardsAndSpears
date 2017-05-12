package de.sloth.core;

import java.util.List;
import java.util.Random;

import de.sloth.components.SlothEnemyComp;
import de.sloth.entity.Entity;
import de.sloth.spearSystems.ThrowSpearEvent;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;
import de.sloth.system.game.core.IEntityManagement;
import de.sloth.system.game.moveSystem.Direction;
import de.sloth.system.game.moveSystem.PossibleMoveEvent;

public class ControllEnemy implements IBehavior {

	@Override
	public void execute(GameSystem system) {
		Random rand = new Random();
		List<Entity> enemies = IEntityManagement.filterEntitiesByComponent(system.getEntityManager().getAllEntities(), SlothEnemyComp.class);
		Entity player = system.getEntityManager().getActivePlayabaleEntity();
		if(enemies.size() < 5) {
			int nenemy = rand.nextInt(100);
			if(nenemy == 0) {
				Direction direct = Direction.RIGHT;
				boolean isLeft = rand.nextBoolean();
				if(isLeft) {
					direct = Direction.LEFT;
				}
				system.getEntityManager().addEntity(EntityGenerator.getInstance().generateEnemy(direct));
			} 
		}
		
		for(Entity enemy : enemies) {
			SlothEnemyComp seo = (SlothEnemyComp) enemy.getComponent(SlothEnemyComp.class);
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
