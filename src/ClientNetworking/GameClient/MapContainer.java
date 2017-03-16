package ClientNetworking.GameClient;

import GameLogic.Map;

import java.util.Observable;

/**
 * Created by Svetlin on 16/03/17.
 */
public class MapContainer extends Observable
{
    private Map map = new Map(10000,10000,10000);

    public MapContainer()
    {
    }

    public void setMap(Map map)
    {
        this.map = map;
        setChanged();
        notifyObservers();
    }

    public Map getMap()
    {
        return map;
    }
}
