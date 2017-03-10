package ClientNetworking.GameHost;

import java.io.Serializable;

public class ChatMessage implements Serializable{

	public String nickname,message;

	public ChatMessage(String nickname,String message)
	{
		this.nickname=nickname;
		this.message=message;
	}
}
