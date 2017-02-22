package de.sloth.core;

import de.sloth.component.ScoreComp;
import de.sloth.component.SlothComp;
import de.sloth.entity.Entity;
import de.sloth.hmi.PlayerStatusLayer;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;
import de.sloth.system.game.moveSystem.Direction;
import de.sloth.system.hmi.HMIGameSystem;
import javafx.beans.property.SimpleStringProperty;

public class StartGame implements IBehavior {

	@Override
	public void execute(GameSystem system) {}

	@Override
	public void execute(GameSystem system, GameEvent expectedEvent) {
		HMIGameSystem hmiSystem = (HMIGameSystem) system;
		PlayerStatusLayer psl = (PlayerStatusLayer) hmiSystem.getGameHMI().getGameInterfaceLayer("psl");
		Entity player = EntityGenerator.getInstance().generatePlayer();
		SlothComp scomp = (SlothComp) player.getComponent(SlothComp.class);
		ScoreComp scoreComp = (ScoreComp) player.getComponent(ScoreComp.class);
		psl.getLifeProperty().bind(scomp.getLifeProperty());
		psl.getSpearProperty().bind(scomp.getSpearProperty());
		psl.getScoreLabel().textProperty().bind(new SimpleStringProperty("Ruhm ").concat(scoreComp.getScoreProperty().asString()));
		system.getEntityManager().addEntity(player);
		system.getEntityManager().addEntity(EntityGenerator.getInstance().generateEnemy(Direction.LEFT));
	}
}