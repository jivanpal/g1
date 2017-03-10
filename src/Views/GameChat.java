package Views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ClientNetworking.GameClient.GameClient;
import ClientNetworking.GameHost.ChatMessage;

/**
 * 
 * @author Svetlin
 * A JPanel for the in-game chat
 */
public class GameChat extends JPanel implements Observer{
	private final JPanel parent;

	private JLabel chat = new JLabel("");
	private String messages = "";

	private JTextField input;
	private String nickname;
	private String text;
	private GameClient client;


	public GameChat(JPanel parent, GameClient client,String nickname) {
		this.parent = parent;
		this.nickname = nickname;
		this.client = client;

		client.addChatObserver(this);

		JPanel southPanel = new JPanel();

		input = new JTextField();
		input.setActionCommand("enter");
		input.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if (actionEvent.getActionCommand().equals("enter")) {
					ChatMessage message = new ChatMessage(nickname, input.getText());
					input.setText("");
					client.send(message);

					sendFocusBackToFrame();
				}
			}
		});


		JScrollPane scroller = new JScrollPane(chat, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroller.setOpaque(false);
		scroller.getViewport().setOpaque(false);

		input.setEditable(true);
		input.setEnabled(true);

		setLayout(new BorderLayout());
		this.add(scroller, BorderLayout.CENTER);
		this.add(input, BorderLayout.SOUTH);

	}

	public void enterPressed() {

	}

	public void sendFocusBackToFrame() {
		if(parent instanceof EngineerView) {
			EngineerView p = (EngineerView) parent;
			p.parentFrame.requestFocusInWindow();
		} else if (parent instanceof PilotView) {
			PilotView p = (PilotView) parent;
			p.parentFrame.requestFocusInWindow();
		} else {
			// This is bad.
			// I guess we just don't do anything?
		}
	}

	@Override
	public void update(Observable o, Object arg) {
			if(client.getMessage() == null) {
				System.err.print("Oh no message is null");
			} else {
				// TODO: Replace once transparency works.
				// messages += "<br><font color=\"rgb(255,255,255)\">" + client.getMessage()+"</font>";

				messages += "<br>" + client.getMessage();
				chat.setText("<html>" + messages + "</html>");
				this.revalidate();
				this.repaint();
			}
	}
	
}
