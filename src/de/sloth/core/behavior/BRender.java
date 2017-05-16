package de.sloth.core.behavior;

import de.sloth.component.AnimationComp;
import de.sloth.component.MovableComp;
import de.sloth.component.Position3DComp;
import de.sloth.component.SpriteComp;
import de.sloth.entity.Entity;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;
import de.sloth.system.hmi.HMIGameSystem;

public class BRender implements IBehavior {

	private int screenWidth;
	private int screenHeight;
	
	public BRender(int screenWidth, int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	@Override
	public void execute(GameSystem system) {
		HMIGameSystem hmiSys = (HMIGameSystem) system;
		hmiSys.getGameHMI().getCanvas().clear();
		for(Entity renderingEntity : hmiSys.getEntityManager().getAllEntities()) {
			Position3DComp comp = (Position3DComp) renderingEntity.getComponent(Position3DComp.class);
			SpriteComp sprite = (SpriteComp) renderingEntity.getComponent(SpriteComp.class);
			AnimationComp aniComp = (AnimationComp) renderingEntity.getComponent(AnimationComp.class);
			MovableComp mvComp = (MovableComp) renderingEntity.getComponent(MovableComp.class);
			if(comp != null) {
				int z_c = comp.getZ();
				int y_c = comp.getY();
				int x_c = comp.getX();
				int transformedPosY = screenHeight-y_c;
				int transformedPosX = x_c;
				if(transformedPosX >= 0 && transformedPosX < screenWidth &&
						transformedPosY >= 0 && transformedPosY < screenHeight) {
					if(aniComp != null) {
						hmiSys.getGameHMI().getCanvas().getLayer(z_c).drawSprite(sprite.getSpritePath() + "_" + mvComp.getDirection().toString().toLowerCase() + ".png_" + aniComp.getAnimationPhase() + "_" + aniComp.getPhaseNr(), transformedPosX, transformedPosY);
						aniComp.setTicksForNextPhase(aniComp.getTicksForNextPhase()-1);
						if(aniComp.getTicksForNextPhase() <= 0) {
							aniComp.setPhaseNr(aniComp.getPhaseNr()+1);
							if(aniComp.getPhaseNr() > 3 ) {
								aniComp.setPhaseNr(0);
							} 
							aniComp.setTicksForNextPhase(15);
						}
					} else {
						hmiSys.getGameHMI().getCanvas().getLayer(z_c).drawSprite(sprite.getSpritePath(), transformedPosX, transformedPosY);
					}
				}
			}
		}
	}
	@Override
	public void execute(GameSystem system, GameEvent expectedEvent) {}
}
