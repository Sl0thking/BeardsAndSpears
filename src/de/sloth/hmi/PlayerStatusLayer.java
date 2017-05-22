package de.sloth.hmi;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.sloth.hmi.GameInterfaceLayer;
import de.sloth.system.game.core.GameEvent;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Layer for displaying player relevant information,
 * like remaining health or spears.
 * 
 * @author Kevin Jolitz
 * @version 1.0.1
 * @date 19.05.2017
 *
 */
public class PlayerStatusLayer extends GameInterfaceLayer {
	
	private SimpleIntegerProperty spearsProperty;
	private SimpleIntegerProperty lifeProperty;
	private Label scoreLabel;
	private HBox lifeBox;
	private HBox spearBox;
	
	public PlayerStatusLayer(ConcurrentLinkedQueue<GameEvent> eventQueue) {
		super("psl", eventQueue);
		this.spearsProperty = new SimpleIntegerProperty();
		this.lifeProperty = new SimpleIntegerProperty();
		this.lifeBox = new HBox();
		this.spearBox = new HBox();
		this.scoreLabel = new Label();
		this.scoreLabel.setFont(Font.font("Arial Black", 24));
		this.scoreLabel.setTextFill(Color.FIREBRICK);
		this.spearsProperty.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				spearBox.getChildren().clear();
				for(int i = 0; i < newValue.intValue(); i++) {
					spearBox.getChildren().add(new ImageView(new Image("file:./sprites/spear.png")));
				}
			}
		});
		this.lifeProperty.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				lifeBox.getChildren().clear();
				for(int i = 0; i < newValue.intValue(); i++) {
					lifeBox.getChildren().add(new ImageView(new Image("file:./sprites/images/health_potion.png")));
				}
			}
		});
		BorderPane constPane = new BorderPane();
		BorderPane bPane = new BorderPane();
		BorderPane topPane = new BorderPane();
		VBox vb = new VBox();
		vb.getChildren().add(spearBox);
		vb.getChildren().add(lifeBox);
		bPane.setLeft(vb);
		topPane.setCenter(scoreLabel);
		constPane.setBottom(bPane);
		constPane.setTop(topPane);
		this.getChildren().add(constPane);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {}

	public SimpleIntegerProperty getLifeProperty() {
		return this.lifeProperty;
	}

	public SimpleIntegerProperty getSpearProperty() {
		return this.spearsProperty;
	}

	public Label getScoreLabel() {
		return scoreLabel;
	}

	public void setScoreLabel(Label scoreLabel) {
		this.scoreLabel = scoreLabel;
	}
}