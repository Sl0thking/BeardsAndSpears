package de.sloth.spearSystems;

import de.sloth.components.SpearBagComp;
import de.sloth.core.EntityGenerator;
import de.sloth.entity.Entity;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;
import de.sloth.system.game.core.IEntityManagement;
import de.sloth.system.game.flying.FlyingComp;

public class ThrowSpear implements IBehavior {

	@Override
	public void execute(GameSystem system) {}

	@Override
	public void execute(GameSystem system, GameEvent expectedEvent) {
		ThrowSpearEvent tse = (ThrowSpearEvent) expectedEvent;
		int spearsInGame = IEntityManagement.filterEntitiesByComponent(system.getEntityManager().getAllEntities(), FlyingComp.class).size();
		if(spearsInGame < 10) {
			if(tse.getThrowingEntity() == null) {
				Entity sloth = system.getEntityManager().getActivePlayabaleEntity();
				SpearBagComp scomp = (SpearBagComp) sloth.getComponent(SpearBagComp.class);
				if(scomp.getSpears() > 0) {
					system.getEntityManager().addEntity(EntityGenerator.getInstance().generateFlyingSpear(sloth));
					scomp.setSpears(scomp.getSpears()-1);
				}
			} else {
				system.getEntityManager().addEntity(EntityGenerator.getInstance().generateFlyingSpear(tse.getThrowingEntity()));
			}
		}
	}
}