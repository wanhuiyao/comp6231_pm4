package retailer;

import tools.channel.Channel;
import tools.channel.ChannelManager;
import tools.channel.Group;
import tools.message.AckMessage;
import tools.message.Action;
import tools.message.Message;
import tools.message.MessageProcesser;
import tools.message.Packet;

public class RetailerFEMessageProcesser extends MessageProcesser {

	@Override
	public void processNewRequest(ChannelManager channelManager, Channel channel, Message msg) {
		if(msg.action == Action.ACK){
			channel.isWaitingForRespose = false;
		}else{
			channel.receivedMessage = msg;

			switch(msg.action){
			case getCatelog:
			case signIn:
			case signUp:
			case submitOrder:
				System.out.println(channel.peerProcessName + " message is saved in receivedMessage.");
				break;
			case INIT:
				channel.localSeq = 0;
				channel.peerSeq = msg.senderSeq;
				break;
			default:
				System.out.println("Unrecognizable action");
				break;
			}
			
			ackBack(channelManager, channel);
		}
	}

}
