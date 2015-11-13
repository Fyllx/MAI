package parse_XML;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import table_data.TableNode;

// Creates DOM object from XML document.
// Parameter of class TableNode contains all information from parsed document.

//*** Needs to do something with Errors representation. ***//
//*** Needs to do validation of parsed data. ***//

public class Main {
	
	public static String fileName = "timeAndDistanceSheet2015-10-08.xml";//"scoresSheet2015-10-08.xml";

	public static void main(String[] args) {
		try {
            Document doc = getDocument(fileName);
            
			TableNode tableNode = parseDocument(doc);
            
			showDocument(tableNode);
			System.out.println("Finish");
        } catch (Exception ex) {
            System.out.println("!!!!!!!!" + ex.getMessage() + "!!!!!!!!!");
        }
	}

	//reads XML from file
	public static Document getDocument(String fileName) throws Exception{
		try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = f.newDocumentBuilder();
            return builder.parse(new File(fileName));
        } catch (Exception exception) {
            //String message = "XML parsing error!";
            //throw new Exception(message);
        	throw exception;
        }
	}
	
	//creates example of TableNode class from XML document
	public static TableNode parseDocument(Document doc) throws Exception {
		Node node = doc.getChildNodes().item(0);
		TableNode tabNode = new TableNode(node);
		tabNode.parse();
		return tabNode;
	}
	
	//shows example of TableNode class in console
	public static void showDocument(TableNode tableNode){
		tableNode.show();		
	}

}
