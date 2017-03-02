package Views;

import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by James on 27/01/17.
 * A view which shows all relevant information about the ships resources (health, shields and fuel levels).
 */
public class ResourcesView extends JPanel {
    // Resource type constants
    public static final int SHIELDS = 0;
    public static final int HULL = 1;
    public static final int ENGINE = 2;

    // Each component has a resource label, resource bar and 2 buttons to increment / decrement (to be removed later).
    private ResourceComponent shieldsComponent;
    private ResourceComponent hullComponent;
    private ResourceComponent engineComponent;

    private EngineerView parent;

    /**
     * Instantiates a new ResourceView which shows details about the amount of shields, hull health and engine fuel
     * to the user
     */
    public ResourcesView(EngineerView parent) {
        super();

        this.parent = parent;
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

    public void setMaximumResourceLevel(int type, int level) {
        switch (type) {
            case SHIELDS:
                shieldsComponent.setMaximumResourceLevel(level);
                break;

            case HULL:
                hullComponent.setMaximumResourceLevel(level);
                break;

            case ENGINE:
                engineComponent.setMaximumResourceLevel(level);
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
        shieldsComponent.setReplenishAction(ShipState.SHIELD_REPLENISH);
        add(shieldsComponent);

        engineComponent = new ResourceComponent("Engines");
        engineComponent.setResourceBarColor(Color.YELLOW);
        engineComponent.setReplenishAction(ShipState.FUEL_REPLENISH);
        add(engineComponent);

        hullComponent = new ResourceComponent("Hull");
        hullComponent.setResourceBarColor(Color.red);
        add(hullComponent);
    }

    public int getMaxFuelLevel() {
        return engineComponent.resourceProgressBar.getMaximum();
    }

    /**
     * A ResourceComponent is a constituent of the ResourceView. Each ResourceComponent shows the details about just
     * one type of resource
     */
    private class ResourceComponent extends JPanel {
        public JProgressBar resourceProgressBar;
        public JButton replenishButton;

        /**
         * Creates a ResourceComponent which displays info about a particular resource to the player.
         * @param name The name of the resource.
         */
        public ResourceComponent(String name) {
            super();

            this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

            this.resourceProgressBar = new JProgressBar();
            resourceProgressBar.setString(name);
            resourceProgressBar.setStringPainted(true);
            resourceProgressBar.setUI(new BasicProgressBarUI());
            resourceProgressBar.setIndeterminate(false);

            // TODO: Find the maximum value for this kind of resource
            resourceProgressBar.setMaximum(10);

            this.replenishButton = new JButton("Replenish");
            replenishButton.setEnabled(false);
            replenishButton.setFocusable(false);

            add(resourceProgressBar);
            add(replenishButton);
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

        public void setMaximumResourceLevel(int maximumResourceLevel) {
            this.resourceProgressBar.setMaximum(maximumResourceLevel);
        }

        public void setReplenishAction(ShipState state) {
            replenishButton.addActionListener(actionEvent -> parent.setState(state));
            replenishButton.setEnabled(true);
        }
    }
}