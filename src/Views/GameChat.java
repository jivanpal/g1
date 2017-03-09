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

	private GameClient gameClient;
	private JLabel chat = new JLabel("");
	private String messages = "";

	private JTextField input;
	private String nickname;
	private String text;
	private GameClient client;


	public GameChat(GameClient client,String nickname) {
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
					input.setText("");
					ChatMessage message = new ChatMessage(nickname, input.getText());
					client.send(message);
				}
			}
		});


		JScrollPane scroller = new JScrollPane(chat, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		input.setEditable(true);
		input.setEnabled(true);

		setLayout(new BorderLayout());
		this.add(scroller, BorderLayout.CENTER);
		this.add(input, BorderLayout.SOUTH);

	}

	public void enterPressed() {

	}

	@Override
	public void update(Observable o, Object arg) {
			if(client.getMessage() == null) {
				System.err.print("Oh no message is null");
			} else {
				messages += "<br>" + client.getMessage();
				chat.setText("<html>+" + messages + "</html>");
				this.revalidate();
				this.repaint();
			}
	}
	
}
