package sequencer.warehouseSequencer;

import tools.channel.Channel;
import tools.channel.ChannelManager;
import tools.channel.Group;
import tools.message.AckMessage;
import tools.message.Action;
import tools.message.Message;
import tools.message.MessageProcesser;
import tools.message.Packet;

import tools.message.warehouse.WarehouseFEGetProductsByIDMessage;
import tools.message.warehouse.WarehouseFEGetProductsByRegisteredManufacturersMessage;
import tools.message.warehouse.WarehouseFEGetProductsByTypeMessage;
import tools.message.warehouse.WarehouseFEGetProductsMessage;
import tools.message.warehouse.WarehouseFEShippingGoodsMessage;
import tools.message.warehouse.WarehouseSequencerGetProductsByIDMessage;
import tools.message.warehouse.WarehouseSequencerGetProductsByRegisteredManufacturersMessage;
import tools.message.warehouse.WarehouseSequencerGetProductsByTypeMessage;
import tools.message.warehouse.WarehouseSequencerGetProductsMessage;
import tools.message.warehouse.WarehouseSequencerShippingGoodsMessage;

public class WarehouseSequencerMessageProcesser extends MessageProcesser {

	@Override
	public void processNewRequest(ChannelManager channelManager, Channel channel, Message msg) {
		if(msg.action == Action.ACK){
			channel.isWaitingForRespose = false;
		}else{
			channel.receivedMessage = msg;
			ackBack(channelManager, channel);

			switch(msg.action){
			case shippingGoods:
			case getProducts:
			case getProductsByRegisteredManufacturers:
			case getProductsByID:
			case getProductsByType:
				channelManager.sequencerID++;
				for(Channel replicaChannel: channelManager.channelMap.values()){
					if(replicaChannel.group == Group.REPLICA){
						replicaChannel.backupPacket = new Packet(replicaChannel.peerProcessName, replicaChannel.peerHost
								, replicaChannel.peerPort
								, generateWarehouseSequencerMessage(replicaChannel.localProcessName
										, ++replicaChannel.localSeq
										, replicaChannel.peerSeq
										, channelManager.sequencerID
										, msg));
						
						replicaChannel.isWaitingForRespose = true;
						synchronized(channelManager.outgoingPacketQueueLock) {
							channelManager.outgoingPacketQueue.add(replicaChannel.backupPacket);
							System.out.println("put this packet in outgoint queue:" + replicaChannel.backupPacket.toString());
						}
					}
				}
				break;
			default:
				System.out.println("Unrecognizable action");
				break;
			}
		}

		
		
	}

	
	
	private Message generateWarehouseSequencerMessage(String localProcessName
			, int localSeq
			, int peerSeq
			, int sequencerID
			, Message receivedMsg){
		System.out.println("generateWarehouseSequencerMessage() is called.");
		switch(receivedMsg.action){
		case shippingGoods:
					return new WarehouseSequencerShippingGoodsMessage(localProcessName
							, localSeq
							, peerSeq
							,((WarehouseFEShippingGoodsMessage)receivedMsg).itemList 
							, ((WarehouseFEShippingGoodsMessage)receivedMsg).retailerName
							, sequencerID); 
		case getProducts:
			return new WarehouseSequencerGetProductsMessage(localProcessName
							, localSeq
							, peerSeq 
							, ((WarehouseFEGetProductsMessage)receivedMsg).productID 
							, ((WarehouseFEGetProductsMessage)receivedMsg).manufacturerName
							, sequencerID);
		case getProductsByRegisteredManufacturers:
			return new WarehouseSequencerGetProductsByRegisteredManufacturersMessage(localProcessName
					, localSeq
					, peerSeq
					, ((WarehouseFEGetProductsByRegisteredManufacturersMessage)receivedMsg).manufacturerName
					, sequencerID);
	
		case getProductsByID:
			return new WarehouseSequencerGetProductsByIDMessage(localProcessName
							, localSeq
							, peerSeq
							, ((WarehouseFEGetProductsByIDMessage)receivedMsg).productID
							, sequencerID);
		case getProductsByType:
			return new WarehouseSequencerGetProductsByIDMessage(localProcessName
							, localSeq
							, peerSeq
							, ((WarehouseFEGetProductsByTypeMessage)receivedMsg).productType
							, sequencerID);
		}
		System.out.println("Bad action");
		return null;
	}


}
