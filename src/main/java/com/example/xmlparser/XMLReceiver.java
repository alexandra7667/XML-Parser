/**
 * This class listens for incoming XML documents.
 * When it receives a document, it is parsed, displayed on the console,
 * and a selection of values are sent to the Controller class for GUI display.
 *
 * @author Alexandra Härnström
 * @version 1
 */

package com.example.xmlparser;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class XMLReceiver implements Runnable{
    private boolean alive;
    private Controller controller;
    private Socket socket;
    private BufferedReader bufferedReader;
    private final String RECEIVED = "Received: ";

    public XMLReceiver(Controller controller, Socket socket) {
        alive = true;
        this.controller = controller;
        this.socket = socket;
    }

    /**
     * This method continuously listens for incoming XML documents.
     */
    @Override
    public void run() {

        createReader();

        while(alive) {
            try {
                String xmlMessage = bufferedReader.readLine();
                read(xmlMessage);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method parses a received XML document using SAX with DTD validation.
     * It prints the whole document to console, and sends a selection of values
     * to the Controller class for GUI display.
     * @param xmlMessage - The XML document to be parsed.
     */
    public void read(String xmlMessage) {

        Document document = null;

        try {
            SAXBuilder builder = new SAXBuilder(XMLReaders.DTDVALIDATING);
            document = builder.build(new StringReader(xmlMessage));

            Element root = document.getRootElement();
            List messageList = root.getChildren();

            Element header = (Element) messageList.get(0);
            Element body = (Element) messageList.get(1);

            List headerList = header.getChildren();

            Element protocol = (Element) headerList.get(0);
            Element id = (Element) headerList.get(1);

            List protocolList = protocol.getChildren();
            List idList = id.getChildren();

            Element type = (Element) protocolList.get(0);
            Element version = (Element) protocolList.get(1);
            Element command = (Element) protocolList.get(2);

            Element name = (Element) idList.get(0);
            Element email = (Element) idList.get(1);
            Element homepage = (Element) idList.get(2);
            Element host = (Element) idList.get(3);

            controller.showFilteredXml(RECEIVED, name.getText(), email.getText(), body.getText());

            showReceivedXmlOnConsole(document);
        }
        catch(Exception e) {
            //If the XML document does not adhere to the specified protocol
            System.out.println("KUNDE INTE PARSA ETT MEDDELANDE: " + xmlMessage);
        }
    }

    /**
     * This method displays the whole received XML on the console.
     * @param document - The XML document to be displayed.
     */
    private void showReceivedXmlOnConsole(Document document) {
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

        try {
            //Display document on console
            xmlOutputter.output(document, System.out);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method creates a BufferedReader of the socket's input stream
     */
    private void createReader() {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method stops the run method (terminates this class running as a Thread).
     */
    public void stopThread() {
        alive = false;
    }

    /**
     * This method closes the BufferedReader.
     */
    public void closeReader() {
        try {
            bufferedReader.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}