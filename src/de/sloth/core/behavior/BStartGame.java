package de.sloth.core.behavior;

import java.util.Random;

import de.sloth.component.HealthComp;
import de.sloth.component.ScoreComp;
import de.sloth.core.EntityGenerator;
import de.sloth.entity.Entity;
import de.sloth.hmi.PlayerStatusLayer;
import de.sloth.spears.component.SpearBagComp;
import de.sloth.system.game.core.ConfigLoader;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;
import de.sloth.system.game.moveSystem.Direction;
import de.sloth.system.game.systemActivation.SystemActivationEvent;
import de.sloth.system.hmi.HMIGameSystem;
import javafx.beans.property.SimpleStringProperty;

/**
 * Behavior for StartGameSystem. Executes necessary operations
 * to prepare a new game.
 * 
 * @author Kevin Jolitz
 * @version 1.0.0
 * @date 18.05.2017
 *
 */
public class BStartGame implements IBehavior {

	@Override
	public void execute(GameSystem system) {}

	@Override
	public void execute(GameSystem system, GameEvent expectedEvent) {
		System.out.println("[StartGameSys::StartGame] Start game round...");
		system.getEntityManager().clear();
		Entity player = EntityGenerator.getInstance().generatePlayer();
		HMIGameSystem hmiSystem = (HMIGameSystem) system;
		PlayerStatusLayer psl = (PlayerStatusLayer) hmiSystem.getGameHMI().getGameInterfaceLayer("psl");
			
		HealthComp heComp = (HealthComp) player.getComponent(HealthComp.class);
		SpearBagComp spComp = (SpearBagComp) player.getComponent(SpearBagComp.class);
		ScoreComp scoreComp = (ScoreComp) player.getComponent(ScoreComp.class);
		psl.getLifeProperty().bind(heComp.getLifeProperty());
		psl.getSpearProperty().bind(spComp.getSpearProperty());
		psl.getScoreLabel().textProperty().bind(new SimpleStringProperty("Ruhm ").concat(scoreComp.getScoreProperty().asString()));
		system.getEntityManager().addEntity(player);
		int enemyCount = Integer.parseInt(ConfigLoader.getInstance().getConfig("enemyCount", "3"));
		Random rand = new Random();
		for(int i = 0; i < enemyCount; i++) {
			Direction direct = Direction.CENTER;
			int nDirection = rand.nextInt(3);
			if(nDirection == 0) {
				direct = Direction.LEFT;
			} else if(nDirection == 1) {
				direct = Direction.RIGHT;
			}
			system.getEntityManager().addEntity(EntityGenerator.getInstance().generateEnemy(direct));
		}
		system.getEventQueue().add(new SystemActivationEvent("single", "endCondition", true));
	}
}