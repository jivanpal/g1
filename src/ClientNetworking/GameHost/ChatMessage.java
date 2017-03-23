package ClientNetworking.GameHost;

import java.io.Serializable;
/**
 * A message containing the name of the sender and the String message
 * @author Svetlin
 */
public class ChatMessage implements Serializable{

	private static final long serialVersionUID = 1L;


	public String nickname,message;
	/**
	 * Constructor
	 * @param nickname the sender's name
	 * @param message the message
	 */
	public ChatMessage(String nickname,String message)
	{
		this.nickname=nickname;
		this.message=message;
	}
}
