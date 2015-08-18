package tools.message.replica;

import java.util.ArrayList;

import tools.Customer;
import tools.message.Action;
import tools.message.Message;

public class DoSyncMessage extends Message {
	public ArrayList<Customer> customerList;
	public DoSyncMessage(String sender, int senderSeq, int receiverSeq,
			ArrayList<Customer> customerList) {
		super(sender, senderSeq, receiverSeq, Action.doSync);
		this.customerList = customerList;
	}
	public String toString(){
		String retVal = new String();
		for(Customer customer: customerList){
			retVal += customer.toString() + "\n";
		}
		return retVal;
	}
}
