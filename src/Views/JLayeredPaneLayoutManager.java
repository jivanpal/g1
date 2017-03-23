package Views;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by James on 21/02/17.
 * @author James Brown
 */
public class JLayeredPaneLayoutManager implements LayoutManager {
    private Map<Component, Rectangle> bounds = new LinkedHashMap<Component, Rectangle>();

    @Override
    public void addLayoutComponent(String s, Component component) {
        bounds.put(component, new Rectangle(component.getPreferredSize()));
    }

    @Override
    public void removeLayoutComponent(Component component) {
        bounds.remove(component);
    }

    @Override
    public Dimension preferredLayoutSize(Container container) {
        Rectangle rect = new Rectangle();
        for (Rectangle r : bounds.values()) {
            rect = rect.union(r);
        }

        return rect.getSize();
    }

    @Override
    public Dimension minimumLayoutSize(Container container) {
        return preferredLayoutSize(container);
    }

    @Override
    public void layoutContainer(Container container) {
        for(Map.Entry<Component, Rectangle> entry : bounds.entrySet()) {
            entry.getKey().setBounds(entry.getValue());
        }
    }

    public void setBounds(Component c, Rectangle bounds) {
        this.bounds.put(c, bounds);
    }
}
