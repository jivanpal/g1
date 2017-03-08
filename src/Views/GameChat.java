package Views;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
	public GameChat(GameClient client,String nickname)
	{
		client.addChatObserver(this);
		
		
		JPanel southPanel = new JPanel();
		

		JTextArea input = new JTextArea();
		input.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				String text = input.getDocument().toString();
				if(text.contains("\r") || text.contains("\n"))
				{
					ChatMessage message = new ChatMessage(nickname,text);
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
	
	@Override
	public void update(Observable o, Object arg) {
			messages += 	"<br>" + gameClient.getMessage();
			chat.setText("<html>+" +  messages + "</html>");
			this.revalidate();
			this.repaint();
	}
	
}
