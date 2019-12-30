package com.quiz.lukebarratt;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser {

    //Map to store each Question. Integer acts as a key and Question holds
    //the values for each question inc questionID, answerID and text.
    private Map<Integer, Question> xmlParser;

    //Getter for XmlParser.
    public Map<Integer, Question> getXmlParser() {
        return xmlParser;
    }

    //Variable to store number or players.
    private int noOfPlayers;

    //getter to retrieve number of players.
    public int getNoOfPlayers() {
        return noOfPlayers;
    }

    //Method to call XML parser to read xml file and to pass questions and options to the client.
    public XMLParser(String file) {
        readQuestions(file);
    }

    //Method to read xml file.
    private void readQuestions(String file) {
        //Declare Tree map to store questionID and question.
        xmlParser = new TreeMap<>();

        try {
            //Declare file input.
            File inputFile = new File(file);
            //Build Database.
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newDefaultInstance();
            //Create new document based on the database.
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            //Parse the input file
            Document document = documentBuilder.parse(inputFile);
            document.getDocumentElement().normalize();
            //Get the first element in the input file.
            NodeList nodeList = document.getElementsByTagName("Question");

            //Retrieve integer for number of player from xml file with attribute players.
            noOfPlayers = Integer.parseInt(document.getDocumentElement().getAttribute("players"));

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    //variable to store questionID and answerID.
                    int questionID = Integer.parseInt(element.getAttribute("id"));
                    int answerID = Integer.parseInt(element.getAttribute("answerid"));

                    //Retrieve element from xml file with tag name text and get content.
                    String text = element.getElementsByTagName("text").item(0).getTextContent();
                    //Retrieve element from xml file with tag name answers.
                    NodeList answers = element.getElementsByTagName("answers").item(0).getChildNodes();
                    //Create new instance of Question class and store questionID, answerID and text for each question.
                    Question question = new Question(questionID, answerID, text);

                    for (int i1 = 0; i1 < answers.getLength(); i1++) {
                        Node node1 = answers.item(i1);

                        if (node1.getNodeType() == Node.ELEMENT_NODE) {
                            Element element1 = (Element) node1;

                            int optionID = Integer.parseInt(element1.getAttribute("id"));
                            String option = element1.getTextContent();

                            //call the addOption method declared in Question class and add the optionID and options to
                            //question object.
                            question.addOption(optionID, option);
                        }
                    }
                    //add questionID and question object to xmlParser Treemap.
                    xmlParser.put(questionID, question);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
