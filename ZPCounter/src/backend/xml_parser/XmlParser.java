package backend.xml_parser;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

//Creates DOM object from XML document.
//Parameter of class TableNode contains all information from parsed document.

//*** Needs to do validation of parsed data. ***//

public class XmlParser
{
	private TableNode tabNode = null;

	public XmlParser ( File ffpars ) throws Exception
	{
            Document doc = getDocument(ffpars);
			parseDocument(doc);
	}

	//reads XML from file
	private Document getDocument(File fileForParsing) throws Exception{
		try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(fileForParsing);
        } catch (Exception exception) {
            Exception ex = new Exception("XML parsing error!");
            ex.setStackTrace(exception.getStackTrace());
        	throw ex;
        }
	}

	//creates example of TableNode class from XML document
	private void parseDocument(Document doc) throws Exception {
		Node node = doc.getChildNodes().item(0);
		tabNode = new TableNode(node);
		tabNode.parse();
	}

	public TableNode getTabNode() throws Exception
	{
		if (tabNode != null) return tabNode;
		throw new Exception( "TableNode doesn't exist");
	}
}
