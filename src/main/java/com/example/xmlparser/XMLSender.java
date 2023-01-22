/**
 * This class creates and sends XML documents via a Socket connection,
 * and displays the XML on the console.
 *
 * @author Alexandra Härnström
 * @version 1
 */

package com.example.xmlparser;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.*;
import java.net.Socket;

public class XMLSender {
    private Socket socket;
    private PrintWriter printWriter;
    public XMLSender(Socket socket) {
        this.socket = socket;
        createWriter();
    }

    /**
     * This method creates an XML document from user input values,
     * retrieved from the GUI.
     * @param name - The 'name' value for the XML
     * @param email - The 'email' value for the XML
     * @param homepage - The 'homepage' value for the XML
     * @param body - The 'body' value for the XML
     */
    public void createXML(String name, String email, String homepage, String body) {

        Document document = new Document();
        Element messageElement = new Element("message");
        document.setRootElement(messageElement);

        DocType docType = new DocType(
                "message",
                "xxx",
                "https://xxx.dtd");

        document.setDocType(docType);

        //Create header
        Element headerElement = new Element("header");
        messageElement.addContent(headerElement);

        //Create protocol
        Element protocolElement = new Element("protocol");
        headerElement.addContent(protocolElement);

        //Create id
        Element idElement = new Element("id");
        headerElement.addContent(idElement);

        //Create protocol contents
        Element typeElement = new Element("type");
        typeElement.setText("CTTP");
        protocolElement.addContent(typeElement);

        Element versionElement = new Element("version");
        versionElement.addContent("1.0");
        protocolElement.addContent(versionElement);

        Element commandElement = new Element("command");
        commandElement.addContent("MESS");
        protocolElement.addContent(commandElement);

        //Create id contents
        Element nameElement = new Element("name");
        nameElement.addContent(name);
        idElement.addContent(nameElement);

        Element emailElement = new Element("email");
        emailElement.addContent(email);
        idElement.addContent(emailElement);

        Element homepageElement = new Element("homepage");
        homepageElement.addContent(homepage);
        idElement.addContent(homepageElement);

        Element hostElement = new Element("host");
        hostElement.addContent("unknown");
        idElement.addContent(hostElement);

        //Create body
        Element bodyElement = new Element("body");
        bodyElement.addContent(body);
        messageElement.addContent(bodyElement);

        sendXML(document);
    }

    /**
     * This method sends an XML document
     * @param document - The XML Document to be sent
     */
    private void sendXML(Document document) {
        //Convert Document object to clean text without new lines (to be able to send whole XML)
        Format format = Format.getCompactFormat();
        format.setLineSeparator("");

        //XMLOutputter outputs a JDOM document as a stream of bytes
        XMLOutputter xmlOutputter = new XMLOutputter(format);

        try {
            //Send document by socket
            xmlOutputter.output(document, printWriter);

            //Display document on console
            xmlOutputter.setFormat(Format.getPrettyFormat());
            xmlOutputter.output(document, System.out);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method creates a PrintWriter from the socket's output stream.
     * Auto flush after a new line is set to true.
     */
    private void createWriter() {
        try {
            printWriter = new PrintWriter(socket.getOutputStream(), true);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void closeWriter() {
        printWriter.close();
    }
}
