package tools.message;

public class WarehouseSequencerGetProductsByRegisteredManufacturersMessage extends WarehouseFEGetProductsByRegisteredManufacturersMessage{
	
	public int sequencerID;
	public WarehouseSequencerGetProductsByRegisteredManufacturersMessage(String sender, int senderSeq, int receiverSeq,
			String manufacturerName, int sequencerID) {
		super(sender, senderSeq, receiverSeq,manufacturerName);
		
		this.sequencerID = sequencerID;
	}

}
