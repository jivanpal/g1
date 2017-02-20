package Views;

import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;

/**
 * Created by James on 27/01/17.
 * A view which shows all relevant information about the ships resources (health, shields and fuel levels).
 */
public class ResourcesView extends JLayeredPane {
    // Resource type constants
    public static final int SHIELDS = 0;
    public static final int HULL = 1;
    public static final int ENGINE = 2;

    // Each component has a resource label, resource bar and 2 buttons to increment / decrement (to be removed later).
    private ResourceComponent shieldsComponent;
    private ResourceComponent hullComponent;
    private ResourceComponent engineComponent;

    /**
     * Instantiates a new ResourceView which shows details about the amount of shields, hull health and engine fuel
     * to the user
     */
    public ResourcesView() {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        makeUI();
    }

    /**
     * Updates the resource level shown to the player for a given resource type.
     * @param type Integer corresponding to the type of resource - see the constants defined in ResourceModel.java
     * @param level The new resource level
     */
    public void updateResourceLevels(int type, float level) {
        switch (type) {
            case SHIELDS:
                shieldsComponent.updateResourceLevel(level);
                break;

            case HULL:
                hullComponent.updateResourceLevel(level);
                break;

            case ENGINE:
                engineComponent.updateResourceLevel(level);
                break;
        }
    }

    /**
     * Instantiates all of the components of the UI and adds them to the JPanel.
     * Must be called before displaying, otherwise nothing will showup!
     */
    public void makeUI() {
        shieldsComponent = new ResourceComponent("Shields");
        shieldsComponent.setResourceBarColor(Color.blue);
        add(shieldsComponent);

        hullComponent = new ResourceComponent("Hull");
        hullComponent.setResourceBarColor(Color.red);
        add(hullComponent);

        engineComponent = new ResourceComponent("Engines");
        engineComponent.setResourceBarColor(Color.yellow);
        add(engineComponent);
    }

    /**
     * A ResourceComponent is a constituent of the ResourceView. Each ResourceComponent shows the details about just
     * one type of resource
     */
    private class ResourceComponent extends JPanel {
        private JProgressBar resourceProgressBar;

        /**
         * Creates a ResourceComponent which displays info about a particular resource to the player.
         * @param name The name of the resource.
         */
        public ResourceComponent(String name) {
            super();

            this.resourceProgressBar = new JProgressBar();
            resourceProgressBar.setString(name);
            resourceProgressBar.setStringPainted(true);
            resourceProgressBar.setUI(new BasicProgressBarUI());
            resourceProgressBar.setIndeterminate(false);

            // Find the maximum value for this kind of resource
            // TODO
            resourceProgressBar.setMaximum(10);

            add(resourceProgressBar);
        }

        /**
         * Sets the resource bar to the appropriate amount
         * @param currentLevel The level to set the resource bar to
         */
        public void updateResourceLevel(float currentLevel) {
            resourceProgressBar.setValue((int) currentLevel);
        }

        public void setResourceBarColor(Color c) {
            this.resourceProgressBar.setForeground(c);
        }
    }
}
