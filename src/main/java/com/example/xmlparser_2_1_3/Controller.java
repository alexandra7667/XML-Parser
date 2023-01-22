/**
 * This is the Controller class. It instantiates objects of XMLSender and XMLReceiver,
 * and starts the XMLReceiver as a new Thread.
 * It handles interaction between the GUI and the logic of the program.
 *
 * @author Alexandra Härnström
 * @version 1
 */

package com.example.xmlparser_2_1_3;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.Socket;

public class Controller {
    private Socket socket;
    private XMLSender xmlSender;
    private XMLReceiver xmlReceiver;
    private final String SENT = "Sent: ";
    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldHomepage;
    @FXML
    private TextField textFieldBody;
    @FXML
    private TextArea textArea;

    public Controller() {
        createSocketConnection();
        xmlSender = new XMLSender(socket);
        xmlReceiver = new XMLReceiver(this, socket);
        Thread receiverThread = new Thread(xmlReceiver);
        receiverThread.start();
    }

    /**
     * This method creates a Socket object for sending and
     * receiving XML documents.
     */
    private void createSocketConnection() {
        String host = "atlas.dsv.su.se";
        int port = 9494;

        try {
            socket = new Socket(host, port);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method retrieves user input from the GUI form and
     * sends the values to the MessageSender class.
     */
    @FXML
    protected void getTextFieldValues() {
        String name = textFieldName.getText();
        String email = textFieldEmail.getText();
        String homepage = textFieldHomepage.getText();
        String body = textFieldBody.getText();

        xmlSender.createXML(name, email, homepage, body);

        showFilteredXml(SENT, name, email, body);

        textFieldName.clear();
        textFieldEmail.clear();
        textFieldHomepage.clear();
        textFieldBody.clear();
    }

    /**
     * This method displays selected values (of an XML) in the GUI's TextArea.
     * @param sentOrReceived - "Sent: " or "Received: " depending on whether the XML was sent or received
     * @param name - The 'name' value (Element) of the XML
     * @param email - The 'email' value (Element) of the XML
     * @param body - The 'body' value (Element) of the XML
     */
    protected void showFilteredXml(String sentOrReceived, String name, String email, String body) {
        textArea.setText(textArea.getText() + "\n" + sentOrReceived + name + " (" + email + ") : " + body);
    }

    public void exitProgram() {
        xmlReceiver.stopThread();

        xmlReceiver.closeReader();
        xmlSender.closeWriter();

        /**
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

*/
    }
}