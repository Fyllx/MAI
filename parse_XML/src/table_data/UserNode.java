package table_data;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UserNode {
	
	Node userNode;
	String fio  = "";
	ArrayList<DayNode> days = null;

	public UserNode(Node node) {
	    this.userNode = node;
	}
	
	public void parse() throws Exception{
		NodeList userChildNodes = userNode.getChildNodes();
		for (int i = 0; i < userChildNodes.getLength(); i++) {
			Node node = userChildNodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName() == "fio") { 
					fio = node.getTextContent();
				}
				else if (node.getNodeName() == "dates"){
					NodeList dayNodes = node.getChildNodes();
					days = new ArrayList<DayNode>(dayNodes.getLength());
					for (int j = 0; j < dayNodes.getLength(); j++) {
						Node dnode = dayNodes.item(j);
						if (dnode.getNodeType() == Node.ELEMENT_NODE) {
							DayNode day = new DayNode(dnode);
							day.parse();
							days.add(day);
						}
					}
				}
				else {
					 String message = "XML parsing error! Some excrescent node find in 'User' child nodes.";
		             throw new Exception(message);
				}
			}
		}
		
	}

	public StringBuffer show(){
		StringBuffer content = new StringBuffer();
		String spase = "	";
		content.append(spase + fio + "\n");
		for (int i=0; i<days.size(); i++){
			content.append(days.get(i).show());
		}
		return content;
	}
}
