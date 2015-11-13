package table_data;

import org.w3c.dom.Node;

public class ScoresNode {

	Node node;
	String fio  = "";
	Integer worksScores = 0;
	Integer activityScores = 0;

	public ScoresNode(Node node) {
	    this.node = node;
	}

	public void parse(){
		fio = node.getAttributes().getNamedItem("fio").getNodeValue();
		worksScores = Integer.parseInt(node.getAttributes().getNamedItem("worksScores").getNodeValue());
		activityScores = Integer.parseInt(node.getAttributes().getNamedItem("activityScores").getNodeValue());
	}

	public StringBuffer show(){
		 StringBuffer content = new StringBuffer();
		 content.append("	" + fio + "\n");
		 content.append("		Works Scores " + worksScores + "\n");
		 content.append("		Activity Scores " + activityScores + "\n");
		 return content;
	}
}
