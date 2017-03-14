package Views;

import GameLogic.GameOptions;

import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by James on 27/01/17.
 * A view which shows all relevant information about the ships resources (health, shields and fuel levels).
 */
public class ResourcesView extends JPanel {
    // Resource type constants
    static final int SHIELDS = 0;
    static final int HULL = 1;
    static final int ENGINE = 2;

    // Each component has a resource label, resource bar and 2 buttons to increment / decrement (to be removed later).
    private ResourceComponent shieldsComponent;
    private ResourceComponent hullComponent;
    private ResourceComponent engineComponent;

    private EngineerView parent;

    private static final Color HULL_COLOR = new Color(255, 0, 0);
    private static final Color ENGINE_COLOR = new Color(255, 252, 25);
    private static final Color SHIELD_COLOR = new Color(20, 133, 204);

    /**
     * Instantiates a new ResourceView which shows details about the amount of shields, hull health and engine fuel
     * to the user
     */
    public ResourcesView(EngineerView parent, String shieldReplenishNumber, String fuelReplenishNumber, ArrayList<JButton> buttons) {
        super();

        this.parent = parent;

        makeUI(shieldReplenishNumber, fuelReplenishNumber, buttons);
    }

    /**
     * Returns the current level of the specified resource
     *
     * @param type The type of resource to get the current level of
     * @return The current level of the resource specified
     */
    public int getResourceLevel(int type) {
        switch (type) {
            case SHIELDS:
                return shieldsComponent.resourceProgressBar.getValue();

            case HULL:
                return hullComponent.resourceProgressBar.getValue();

            case ENGINE:
                return engineComponent.resourceProgressBar.getValue();

            default:
                return 0;
        }
    }

    /**
     * Updates the resource level shown to the player for a given resource type.
     *
     * @param type  Integer corresponding to the type of resource - see the constants defined in ResourceModel.java
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

    private void makeUI(String shieldReplenishNumber, String fuelReplenishNumber, ArrayList<JButton> buttons) {
        this.setOpaque(false);

        this.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridwidth = 0;
        c.gridheight = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 10;

        shieldsComponent = new ResourceComponent("Shields", shieldReplenishNumber, buttons);

        shieldsComponent.setResourceBarColor(SHIELD_COLOR);
        shieldsComponent.setReplenishAction(ShipState.SHIELD_REPLENISH);
        c.anchor = GridBagConstraints.NORTH;
        add(shieldsComponent, c);

        engineComponent = new ResourceComponent("Engines", fuelReplenishNumber, buttons);
        engineComponent.setResourceBarColor(ENGINE_COLOR);
        engineComponent.setReplenishAction(ShipState.FUEL_REPLENISH);
        c.anchor = GridBagConstraints.CENTER;
        add(engineComponent, c);

        hullComponent = new ResourceComponent("Hull", "N/A", buttons);
        hullComponent.setResourceBarColor(HULL_COLOR);
        c.anchor = GridBagConstraints.SOUTH;
        add(hullComponent, c);

    }

    /**
     * A ResourceComponent is a constituent of the ResourceView. Each ResourceComponent shows the details about just
     * one type of resource
     */
    private class ResourceComponent extends JPanel {
        JProgressBar resourceProgressBar;
        JButton replenishButton;

        /**
         * Creates a ResourceComponent which displays info about a particular resource to the player.
         *
         * @param name The name of the resource.
         */
        ResourceComponent(String name, String replenishNumber, ArrayList<JButton> buttons) {
            super();

            this.setOpaque(false);

            this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

            this.resourceProgressBar = new JProgressBar();
            resourceProgressBar.setString(name);
            resourceProgressBar.setStringPainted(true);
            resourceProgressBar.setUI(new BasicProgressBarUI());
            resourceProgressBar.setIndeterminate(false);
            resourceProgressBar.setMaximum(10);
            resourceProgressBar.setFont(GameOptions.REGULAR_TEXT_FONT);

            this.replenishButton = new JButton("Replenish: " + replenishNumber);
            replenishButton.setFont(GameOptions.REGULAR_TEXT_FONT);
            replenishButton.setEnabled(false);
            replenishButton.setFocusable(false);
            replenishButton.setOpaque(true);
            replenishButton.setBorderPainted(false);
            replenishButton.setName(name);
            replenishButton.setBackground(Color.decode("#cccccc"));
            buttons.add(replenishButton);
            
            add(resourceProgressBar);
            add(replenishButton);
        }

        /**
         * Sets the resource bar to the appropriate amount
         *
         * @param currentLevel The level to set the resource bar to
         */
        void updateResourceLevel(float currentLevel) {
            resourceProgressBar.setValue((int) currentLevel);
        }

        void setResourceBarColor(Color c) {
            this.resourceProgressBar.setForeground(c);
        }

        void setMaximumResourceLevel(int maximumResourceLevel) {
            this.resourceProgressBar.setMaximum(maximumResourceLevel);
        }

        void setReplenishAction(ShipState state) {
            replenishButton.addActionListener(actionEvent -> parent.setState(state));
            replenishButton.setEnabled(true);
        }

        void setReplenishNumber(String number) {
            replenishButton.setText("Replenish: " + number);
        }
    }
}