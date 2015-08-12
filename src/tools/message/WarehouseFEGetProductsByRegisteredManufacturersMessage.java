package tools.message;

public class WarehouseFEGetProductsByRegisteredManufacturersMessage extends Message{
	
	String manufacturerName;
	
	public WarehouseFEGetProductsByRegisteredManufacturersMessage(String sender, int senderSeq, int receiverSeq, String manufacturerName){
		super(sender, senderSeq, receiverSeq, Action.getProductsByRegisteredManufacturers);
		this.manufacturerName = manufacturerName;
	}

}
