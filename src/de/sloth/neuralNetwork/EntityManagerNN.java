package de.sloth.neuralNetwork;

import java.util.List;

import de.sloth.core.EntityManager;
import de.sloth.entity.Entity;
import de.sloth.neuralNetwork.component.NeuralNetworkComp;

public class EntityManagerNN extends EntityManager {
	private Entity NNInformation;

	public EntityManagerNN() {
		super();
		NNInformation = null;
	}
	
	@Override
	public void addEntity(Entity entity) {
		if(entity.getComponent(NeuralNetworkComp.class) != null) {
			this.NNInformation = entity;
		} else {
			super.addEntity(entity);
		}
	}
	
	@Override
	public List<Entity> getAllEntities() {
		List<Entity> returningEntities = super.getAllEntities();
		returningEntities.add(NNInformation);
		return returningEntities;
	}

	public Entity getNNInformation() {
		return NNInformation;
	}

	public void setNNInformation(Entity nNInformation) {
		NNInformation = nNInformation;
	}
	
	
	
}
