package de.sloth.core;

import de.sloth.component.HealthComp;
import de.sloth.component.ScoreComp;
import de.sloth.components.SpearBagComp;
import de.sloth.entity.Entity;
import de.sloth.hmi.PlayerStatusLayer;
import de.sloth.system.game.core.ConfigLoader;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;
import de.sloth.system.game.moveSystem.Direction;
import de.sloth.system.game.systemActivation.SystemActivationEvent;
import de.sloth.system.hmi.HMIGameSystem;
import javafx.beans.property.SimpleStringProperty;

public class StartGame implements IBehavior {

	@Override
	public void execute(GameSystem system) {}

	@Override
	public void execute(GameSystem system, GameEvent expectedEvent) {
		system.getEntityManager().clear();
		Entity player = EntityGenerator.getInstance().generatePlayer();
		if(Boolean.valueOf(ConfigLoader.getInstance().getConfig("showGui", "false"))) {
			HMIGameSystem hmiSystem = (HMIGameSystem) system;
			PlayerStatusLayer psl = (PlayerStatusLayer) hmiSystem.getGameHMI().getGameInterfaceLayer("psl");
			
			HealthComp heComp = (HealthComp) player.getComponent(HealthComp.class);
			SpearBagComp spComp = (SpearBagComp) player.getComponent(SpearBagComp.class);
			ScoreComp scoreComp = (ScoreComp) player.getComponent(ScoreComp.class);
			psl.getLifeProperty().bind(heComp.getLifeProperty());
			psl.getSpearProperty().bind(spComp.getSpearProperty());
			psl.getScoreLabel().textProperty().bind(new SimpleStringProperty("Ruhm ").concat(scoreComp.getScoreProperty().asString()));
		}
		system.getEntityManager().addEntity(player);
		int enemyCount = Integer.parseInt(ConfigLoader.getInstance().getConfig("enemyCount", "1"));
		for(int i = 0; i < enemyCount; i++) {
			system.getEntityManager().addEntity(EntityGenerator.getInstance().generateEnemy(Direction.LEFT));
		}
		system.getEventQueue().add(new SystemActivationEvent("single", "endCondition", true));
	}
}