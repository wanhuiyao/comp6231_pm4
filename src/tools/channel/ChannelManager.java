package tools.channel;

import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import sequencer.retailerSequencer.RetailerSequencerMessageProcesser;
import tools.ConfigureManager;
import tools.LoggerClient;
import tools.message.Message;
import tools.message.MessageProcesser;
import tools.message.Packet;

public class ChannelManager{
	public LoggerClient loggerClient;
	public HashMap<String, Channel> channelMap;
	public Object outgoingPacketQueueLock;
	public Queue<Packet> outgoingPacketQueue;
	public MessageProcesser messageProcesser;
	public int sequencerID;
	NetworkIO networkIO;
	ReadThread readThread;
	WriteThread writeThread;
	public ChannelManager(LoggerClient loggerClient, MessageProcesser messageProcesser) throws SocketException, Exception {
		this.loggerClient = loggerClient;
		this.messageProcesser = messageProcesser;
		channelMap = new HashMap<String, Channel>();
		outgoingPacketQueueLock = new Object();
		outgoingPacketQueue = new LinkedList<Packet>();
		sequencerID = 0;
		networkIO = new NetworkIO(ConfigureManager.getInstance().getInt("RetailerSequencerPort"));
		readThread = new ReadThread(this, networkIO);
		writeThread = new WriteThread(this, networkIO);
	}
	
	public void addChannel(Channel channel){
		if(channelMap.containsKey(channel.peerProcessName)){
			System.out.println(channel.peerProcessName + " already exists in channelMap!");
		}else{
			channelMap.put(channel.peerHost, channel);
			System.out.println("Udp channel to " + channel.peerProcessName + ":" + channel.peerHost + ":" + channel.peerPort);
		}
	}
	
	public void processMessage(Message message) {
		if(channelMap.containsKey(message.sender)){
			messageProcesser.processMessage(this, message);
		}else{
			System.out.println("channelMap does not contian " + message.sender);
		}
	}
	
	public void collectLostPacket(){
		for(Channel channel: channelMap.values()){
			if(channel.hasCachedMsg){
				outgoingPacketQueue.add(new Packet(channel.peerHost, channel.peerPort, channel.cachedMsg));
			}
		}
	}
	
	public void start(){
		readThread.start();
		writeThread.start();
	}
}
