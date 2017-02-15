package UI;

import Views.EngineerView;
import Views.PilotView;

import javax.swing.JFrame;

public class TestGame {

	public static void main(String[] args) {
		PilotView view = new PilotView();

		/*ResourcesModel model = new ResourcesModel(10, 10, 10);
		ResourcesPresenter presenter = new ResourcesPresenter();
		EngineerView view = new EngineerView();
		presenter.setModel(model);
		presenter.setView(view.getResourcesView());
		view.setPresenter(presenter);

		view.makeUI();*/

		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(1900, 1080);
		f.add(view);
		f.setVisible(true);
	}
}
