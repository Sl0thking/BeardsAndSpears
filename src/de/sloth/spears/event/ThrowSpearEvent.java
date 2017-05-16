package de.sloth.spears.event;

import de.sloth.entity.Entity;
import de.sloth.system.game.core.GameEvent;

/**
 * Event for spawning a new spear from src entity.
 * 
 * [Engine candidate]
 * 
 * @author Kevin Jolitz
 * @version 1.0.0
 * @date 16.05.2017
 * 
 */
public class ThrowSpearEvent extends GameEvent {
	/**
	 * Src of spear 
	 */
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