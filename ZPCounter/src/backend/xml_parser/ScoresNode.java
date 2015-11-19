package backend.xml_parser;

import org.w3c.dom.Node;

public class ScoresNode {

	private Node node;
	public String fio  = "";
	public Integer worksScores = 0;
	public Integer activityScores = 0;

	public ScoresNode(Node node) {
	    this.node = node;
	}

	public void parse(){
		fio = node.getAttributes().getNamedItem("fio").getNodeValue().trim();
		worksScores = Integer.parseInt(node.getAttributes().getNamedItem("worksScores").getNodeValue().trim());
		activityScores = Integer.parseInt(node.getAttributes().getNamedItem("activityScores").getNodeValue().trim());
	}

	public StringBuffer show(){
		 StringBuffer content = new StringBuffer();
		 content.append("	" + fio + "\n");
		 content.append("		Works Scores " + worksScores + "\n");
		 content.append("		Activity Scores " + activityScores + "\n");
		 return content;
	}
}
