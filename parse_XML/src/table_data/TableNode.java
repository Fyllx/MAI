package table_data;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class TableNode {
	
	Node firstNode    = null;
	String tableName  = "";
	String comment    = "";
	String createDate = "";
	String beginDate  = "";
	String finishDate = "";
	ArrayList<UserNode> users = null;
	ArrayList<ScoresNode> scores = null;

	public TableNode() {
	}
	
	public TableNode(Node node) {
	    this.firstNode = node;
	}

	public void parse() throws Exception {
		tableName = firstNode.getAttributes().getNamedItem("type").getNodeValue();
		comment = firstNode.getAttributes().getNamedItem("comment").getNodeValue();
		createDate = firstNode.getAttributes().getNamedItem("dtCreate").getNodeValue();
		beginDate = firstNode.getAttributes().getNamedItem("dtBegin").getNodeValue();
		finishDate = firstNode.getAttributes().getNamedItem("dtFinish").getNodeValue();

		NodeList userNodes = firstNode.getChildNodes();
		if (tableName.contains("schedule")){
			users = new ArrayList<UserNode>(userNodes.getLength());
			for (int i = 0; i < userNodes.getLength(); i++) {
				Node node = userNodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					UserNode userNode = new UserNode(node);
					userNode.parse();
					users.add(userNode);
				}
			}
		}
		else if (tableName.contains("scores")) {
			scores = new ArrayList<ScoresNode>(userNodes.getLength());
			for (int i = 0; i < userNodes.getLength(); i++) {
				Node node = userNodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					ScoresNode scNode = new ScoresNode(node);
					scNode.parse();
					scores.add(scNode);
				}
			}
		}
		else {
			String message = "Uncnown table name found. Parsing can't be finished.";
            throw new Exception(message);
        }
    }

	public void show(){
		System.out.println("Table name: " + tableName);
		System.out.println("Comment: " + comment);
		System.out.println("Date of create: " + createDate);
		System.out.println("Begin date: " + beginDate);
		System.out.println("End date: " + finishDate);
		System.out.println("Users: ");		
		if (users != null){
			for (int i=0; i<users.size(); i++){
				System.out.println(users.get(i).show());
			}
		}
		if (scores != null){
			for (int i=0; i<scores.size(); i++){
				System.out.println(scores.get(i).show());
			}
		}
	}
	
}
