package Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import Graphics.Screen;
import UI.ClientShipObservable;

/**
 * Created by James on 01/02/17.
 */
public class EngineerView extends JPanel implements Observer, KeyListener {

    Screen screen;
    ResourcesView resourcesView;

    public EngineerView() {
        this.setLayout(new BorderLayout());

        screen = new Screen();
        screen.setSize(1000, 800);
        screen.setMaximumSize(new Dimension(1000, 800));
        screen.setMinimumSize(new Dimension(1000, 800));
        screen.setPreferredSize(new Dimension(1000, 800));
        this.add(screen, BorderLayout.CENTER);

        Container UIPanel = new Container();
        UIPanel.setLayout(new BoxLayout(UIPanel, BoxLayout.X_AXIS));

        resourcesView = new ResourcesView();
        resourcesView.setSize(new Dimension(1000, 200));
        resourcesView.setMaximumSize(new Dimension(1000, 200));
        resourcesView.setMinimumSize(new Dimension(1000, 200));
        resourcesView.setPreferredSize(new Dimension(1000, 200));

        UIPanel.add(resourcesView);

        this.add(UIPanel, BorderLayout.SOUTH);
    }

    @Override
    public void update(Observable observable, Object o) {
        ClientShipObservable shipObservable = (ClientShipObservable) observable;

        resourcesView.updateResourceLevels(ResourcesView.SHIELDS, shipObservable.getShipShields());
        resourcesView.updateResourceLevels(ResourcesView.HULL, shipObservable.getShipHealth());
        resourcesView.updateResourceLevels(ResourcesView.ENGINE, shipObservable.getShipFuel());
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
