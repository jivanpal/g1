package UI;

/**
 * Created by James on 28/01/17.
 */
public class ResourcesPresenter {
    private ResourcesModel mModel;
    private ResourcesView mView;

    public ResourcesPresenter() {}

    public void setModel(ResourcesModel model) {
        this.mModel = model;
    }

    public void setView(ResourcesView view) {
        this.mView = view;
    }

    public void resourceIncrementButtonPress(int resourceType) {
        mModel.updateResource(resourceType, 1f);
        mView.updateResourceLevels(resourceType, mModel.getCurrentResourceLevel(resourceType));
    }

    public void resourceDecrementButtonPress(int resourceType) {
        mModel.updateResource(resourceType, -1f);
        mView.updateResourceLevels(resourceType, mModel.getCurrentResourceLevel(resourceType));
    }

    /**
     * Returns the maximum possible value of a resource type
     * @param resourceType Integer corresponding to the type of resource - see the constants defined in ResourceModel.java
     * @return The maximum possible value
     */
    public float getMaximumResourceValue(int resourceType) {
        return mModel.getMaximumResourceValue(resourceType);
    }
}
