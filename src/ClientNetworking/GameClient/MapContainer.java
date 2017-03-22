package ClientNetworking.GameClient;

import GameLogic.Map;

import java.util.Observable;

/**
 * Created by Svetlin on 16/03/17.
 * A container for the locally stored map
 * Purpose: to retain the observers if the map changes
 */
public class MapContainer extends Observable
{
    private Map map = new Map(1000, 1000, 500);
	/**
	 * An empty constructor
	 */
    public MapContainer()
    {
    }
    /**
     * set a new map and notify the observers
     * @param map the new map
     */
    public void setMap(Map map)
    {
        this.map = map;
        setChanged();
        notifyObservers();
    }
    /**
     * Retrieve the map
     * @return the map
     */
    public Map getMap()
    {
        return map;
    }
}
