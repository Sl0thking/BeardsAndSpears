package de.sloth.core;

import java.util.List;

import de.sloth.components.NeuralNetworkComp;
import de.sloth.entity.Entity;

public class NNEntityManager extends EntityManager {
	private Entity NNInformation;

	public NNEntityManager() {
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
