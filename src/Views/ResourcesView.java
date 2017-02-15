package Views;

import UI.ResourcesModel;
import UI.ResourcesPresenter;

import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;

/**
 * Created by James on 27/01/17.
 * A view which shows all relevant information about the ships resources (health, shields and fuel levels).
 */
public class ResourcesView extends JPanel {
    private ResourcesPresenter presenter;

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
    }

    /**
     * Updates the resource level shown to the player for a given resource type.
     * @param type Integer corresponding to the type of resource - see the constants defined in ResourceModel.java
     * @param level The new resource level
     */
    public void updateResourceLevels(int type, float level) {
        switch (type) {
            case ResourcesModel.SHIELDS:
                shieldsComponent.updateResourceLevel(level);
                break;

            case ResourcesModel.HULL:
                hullComponent.updateResourceLevel(level);
                break;

            case ResourcesModel.ENGINE:
                engineComponent.updateResourceLevel(level);
                break;
        }
    }

    public void setPresenter(ResourcesPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Instantiates all of the components of the UI and adds them to the JPanel.
     * Must be called before displaying, otherwise nothing will showup!
     */
    public void makeUI() {
        shieldsComponent = new ResourceComponent("Shields", ResourcesModel.SHIELDS);
        shieldsComponent.setResourceBarColor(Color.blue);
        add(shieldsComponent);

        hullComponent = new ResourceComponent("Hull", ResourcesModel.HULL);
        hullComponent.setResourceBarColor(Color.red);
        add(hullComponent);

        engineComponent = new ResourceComponent("Engines", ResourcesModel.ENGINE);
        engineComponent.setResourceBarColor(Color.yellow);
        add(engineComponent);
    }

    /**
     * A ResourceComponent is a constituent of the ResourceView. Each ResourceComponent shows the details about just
     * one type of resource
     */
    private class ResourceComponent extends JPanel {
        private String resourceName;
        private JProgressBar resourceProgressBar;

        /**
         * Creates a ResourceComponent which displays info about a particular resource to the player.
         * @param name The name of the resource.
         * @param resourceType Integer corresponding to the type of resource - see the constants defined in ResourceModel.java
         */
        public ResourceComponent(String name, int resourceType) {
            super();

            this.resourceName = name;
            this.resourceProgressBar = new JProgressBar();
            resourceProgressBar.setString(name);
            resourceProgressBar.setStringPainted(true);
            resourceProgressBar.setUI(new BasicProgressBarUI());
            resourceProgressBar.setIndeterminate(false);

            // Find the maximum value for this kind of resource
            float maxValue = presenter.getMaximumResourceValue(resourceType);
            resourceProgressBar.setMaximum((int) maxValue);

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
