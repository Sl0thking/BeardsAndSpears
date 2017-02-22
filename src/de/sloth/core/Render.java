package de.sloth.core;

import de.sloth.component.Position3DComp;
import de.sloth.component.SpriteComp;
import de.sloth.entity.Entity;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;
import de.sloth.system.hmi.HMIGameSystem;

public class Render implements IBehavior {

	private int screenWidth;
	private int screenHeight;
	
	public Render(int screenWidth, int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	@Override
	public void execute(GameSystem system) {
		HMIGameSystem hmiSys = (HMIGameSystem) system;
		hmiSys.getGameHMI().getCanvas().clear();
		for(Entity renderingEntity : hmiSys.getEntityManager().getAllEntities()) {
			//System.out.println(renderingEntity);
			Position3DComp comp = (Position3DComp) renderingEntity.getComponent(Position3DComp.class);
			SpriteComp sprite = (SpriteComp) renderingEntity.getComponent(SpriteComp.class);
			int z_c = comp.getZ();
			int y_c = comp.getY();
			int x_c = comp.getX();
			int transformedPosY = screenHeight-y_c-64;
			int transformedPosX = x_c;
			if(transformedPosX >= 0 && transformedPosX < screenWidth &&
			   transformedPosY >= 0 && transformedPosY < screenHeight) {
					hmiSys.getGameHMI().getCanvas().getLayer(z_c).drawSprite(sprite.getSpritePath(), transformedPosX, transformedPosY);
			}
		}
	}
	@Override
	public void execute(GameSystem system, GameEvent expectedEvent) {}
}
