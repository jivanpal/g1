package UI;

import Views.EngineerView;

import javax.swing.JFrame;

public class Game {

	public static void main(String[] args) {
		ResourcesModel model = new ResourcesModel(10, 10, 10);
		ResourcesPresenter presenter = new ResourcesPresenter();
		EngineerView view = new EngineerView();
		presenter.setModel(model);
		presenter.setView(view.getResourcesView());
		view.setPresenter(presenter);

		view.makeUI();

		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(600, 600);
		f.add(view);
		f.setVisible(true);
	}
}