package de.sloth.testgame.main;

import de.sloth.component.Component;
import javafx.beans.property.SimpleIntegerProperty;

public class SlothComp extends Component {
	private SimpleIntegerProperty spears;
	private SimpleIntegerProperty lifes;

	public SlothComp(int spears, int lifes) {
		super();
		this.spears = new SimpleIntegerProperty(spears);
		this.lifes = new SimpleIntegerProperty(lifes);
	}

	public int getSpears() {
		return spears.get();
	}

	public void setSpears(int spears) {
		this.spears.set(spears);
	}

	public int getLifes() {
		return lifes.get();
	}

	public void setLifes(int lifes) {
		this.lifes.set(lifes);
	}	
	
	public SimpleIntegerProperty getSpearProperty() {
		return this.spears;
	}
	
	public SimpleIntegerProperty getLifeProperty() {
		return this.lifes;
	}
}