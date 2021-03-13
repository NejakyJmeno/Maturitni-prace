package sample;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;

public class ScoreRecording {

    static Document dom;
    static Element e = null;
    static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    public static void recordScore (ArrayList scores) {

/*
        try {
            FileWriter myWriter = new FileWriter("scoredatabase.txt");
            myWriter.write(scores.get(0).toString());
            myWriter.write("\n");
            myWriter.write(scores.get(1).toString());
            myWriter.close();
            System.out.println("Yeet");
        } catch (IOException e) {
            System.out.println("FUCK!");
            e.printStackTrace();
        }

 */

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.newDocument();

            Element rootEle = dom.createElement("User" + Main.users);

            e = dom.createElement("name");
            e.appendChild(dom.createTextNode(scores.get(0).toString()));
            rootEle.appendChild(e);

            e = dom.createElement("score");
            e.appendChild(dom.createTextNode(scores.get(1).toString()));
            rootEle.appendChild(e);

            dom.appendChild(rootEle);

            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

                // send DOM to file
                tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream("scoredatabase.xml")));
                System.out.println("Yeet");

            } catch (TransformerException te) {
                System.out.println(te.getMessage());
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        } catch (ParserConfigurationException pce) {
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
        }

        Main.savedScore.clear();
        Main.users++;
    }

    private String getTextValue(String def, Element doc, String tag) {

        String value = def;
        NodeList nl;
        nl = doc.getElementsByTagName(tag);
        if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) {
            value = nl.item(0).getFirstChild().getNodeValue();
        }
        return value;
    }

}
