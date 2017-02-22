package de.sloth.spearSystems;

import de.sloth.components.SlothComp;
import de.sloth.core.EntityGenerator;
import de.sloth.entity.Entity;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;

public class ThrowSpear implements IBehavior {

	@Override
	public void execute(GameSystem system) {}

	@Override
	public void execute(GameSystem system, GameEvent expectedEvent) {
		ThrowSpearEvent tse = (ThrowSpearEvent) expectedEvent;
		if(tse.getThrowingEntity() == null) {
			//tse.setThrowingEntity(system.getEntityManager().getActivePlayabaleEntity());
			Entity sloth = system.getEntityManager().getActivePlayabaleEntity();
			SlothComp scomp = (SlothComp) sloth.getComponent(SlothComp.class);
			if(scomp.getSpears() > 0) {
				system.getEntityManager().addEntity(EntityGenerator.getInstance().generateFlyingSpear(sloth));
				scomp.setSpears(scomp.getSpears()-1);
			}
		} else {
			system.getEntityManager().addEntity(EntityGenerator.getInstance().generateFlyingSpear(tse.getThrowingEntity()));
		}
	}
}