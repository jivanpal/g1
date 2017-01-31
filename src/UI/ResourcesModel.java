package UI;

import java.util.ArrayList;

/**
 * Models all of the resources a ship can have (shields, hull, engine).
 */
public class ResourcesModel {
	// Resource type constants
	public static final int SHIELDS = 0;
	public static final int HULL = 1;
	public static final int ENGINE = 2;

	// The current resource values of the ship
	private final ArrayList<Float> resources;

	// Please don't modify me - I would like to make this immutable. When I tried I couldn't get it to compile. -James
	private final ArrayList<Float> MAX_RESOURCE_LEVELS;

	/**
	 * Instantiates a new model of the ships resources. All resource values default to 0.
	 * @param maxShieldLevel The maximum value of shields the ship can have.
	 * @param maxHullLevel The maximum value of hull health the ship can have.
	 * @param maxEngineLevel The maximum value of engine fuel the ship can have.
	 */
	public ResourcesModel(float maxShieldLevel, float maxHullLevel, float maxEngineLevel) {
	    super();

        resources = new ArrayList<>();
        resources.add(0f);	// Shields
        resources.add(0f);	// Hull
        resources.add(0f);  // Engine

		MAX_RESOURCE_LEVELS = new ArrayList<Float>();
		MAX_RESOURCE_LEVELS.add(maxShieldLevel);
		MAX_RESOURCE_LEVELS.add(maxHullLevel);
		MAX_RESOURCE_LEVELS.add(maxEngineLevel);
    }

	/**
	 * Changes a given resource for the ship by delta. If delta causes the result to exceed the bounds given, the resource
	 * amount is set to it's higher or lower bound appropriately.
	 * @param type Integer corresponding to the type of resource - see the constants defined in ResourceModel.java
	 * @param delta The amount to change the resource by.
	 */
	public void updateResource(int type, float delta) {
	    resources.set(type, resources.get(type) + delta);

	    // Check we haven't exceeded our bounds. If we have, reset
	    if(resources.get(type) < 0) {
	        resources.set(type, 0f);
        }

        if (resources.get(type) > MAX_RESOURCE_LEVELS.get(type)) {
	        resources.set(type, MAX_RESOURCE_LEVELS.get(type));
        }
	}

	/**
	 * Returns the corresponding resource value
	 * @param resourceType Integer corresponding to the type of resource - see the constants defined in ResourceModel.java
	 * @return The resource value
	 */
	public float getCurrentResourceLevel(int resourceType) {
		return resources.get(resourceType);
	}

	public float getMaximumResourceValue(int resourceType) {
		return MAX_RESOURCE_LEVELS.get(resourceType);
	}
}
