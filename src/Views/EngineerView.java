package Views;

import javax.swing.*;
import java.awt.*;
import Graphics.Screen;
import UI.ResourcesPresenter;

/**
 * Created by James on 01/02/17.
 */
public class EngineerView extends JPanel {

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

        resourcesView = new ResourcesView();
        resourcesView.setSize(new Dimension(1000, 200));
        resourcesView.setMaximumSize(new Dimension(1000, 200));
        resourcesView.setMinimumSize(new Dimension(1000, 200));
        resourcesView.setPreferredSize(new Dimension(1000, 200));
        this.add(resourcesView, BorderLayout.SOUTH);
    }

    public void makeUI() {
        resourcesView.makeUI();
    }

    public ResourcesView getResourcesView() {
        return this.resourcesView;
    }

    public void setPresenter(ResourcesPresenter presenter) {
        resourcesView.setPresenter(presenter);
    }
}
