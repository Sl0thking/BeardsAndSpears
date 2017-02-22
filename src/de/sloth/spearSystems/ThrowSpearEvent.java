package de.sloth.spearSystems;

import de.sloth.entity.Entity;
import de.sloth.system.game.core.GameEvent;

public class ThrowSpearEvent extends GameEvent {
	private Entity throwingEntity;
	
	
	public ThrowSpearEvent() {}
	
	public ThrowSpearEvent(Entity throwingEntity) {
		this.setThrowingEntity(throwingEntity);
	}

	public Entity getThrowingEntity() {
		return throwingEntity;
	}

	public void setThrowingEntity(Entity throwingEntity) {
		this.throwingEntity = throwingEntity;
	}
}
