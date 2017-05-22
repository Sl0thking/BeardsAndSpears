package de.sloth.hmi;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.sloth.hmi.GameInterfaceLayer;
import de.sloth.system.game.core.GameEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Layer for displaying game relevant information,
 * like fps.
 * 
 * @author Kevin Jolitz
 * @version 1.0.0
 * @date 19.05.2017
 *
 */
public class GameStatusLayer extends GameInterfaceLayer {
	
	private Label fpsLabel;

	public GameStatusLayer(ConcurrentLinkedQueue<GameEvent> eventQueue) {
		super("gsl", eventQueue);
		this.fpsLabel = new Label();
		this.fpsLabel.setFont(Font.font("Arial", 18));
		this.fpsLabel.setTextFill(Color.WHITE);

		BorderPane constPane = new BorderPane();
		BorderPane bPane = new BorderPane();
		BorderPane topPane = new BorderPane();
		topPane.setLeft(fpsLabel);
		constPane.setBottom(bPane);
		constPane.setTop(topPane);
		this.getChildren().add(constPane);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {}
	
	public Label getFPSLabel() {
		return this.fpsLabel;
	}
}