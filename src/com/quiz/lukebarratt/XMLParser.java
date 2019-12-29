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

    private Map<Integer, Question> xmlParser;

    public Map<Integer, Question> getXmlParser() {
        return xmlParser;
    }

    private int noOfPlayers;

    public int getNoOfPlayers() {
        return noOfPlayers;
    }

    public XMLParser(String file) {
        readQuestions(file);
    }

    private void readQuestions(String file) {
        xmlParser = new TreeMap<>();

        try {
            File inputFile = new File(file);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newDefaultInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputFile);
            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("Question");

            noOfPlayers = Integer.parseInt(document.getDocumentElement().getAttribute("players"));

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    int questionID = Integer.parseInt(element.getAttribute("id"));
                    int answerID = Integer.parseInt(element.getAttribute("answerid"));

                    String text = element.getElementsByTagName("text").item(0).getTextContent();
                    NodeList answers = element.getElementsByTagName("answers").item(0).getChildNodes();
                    Question question = new Question(questionID, answerID, text);

                    for (int i1 = 0; i1 < answers.getLength(); i1++) {
                        Node node1 = answers.item(i1);

                        if (node1.getNodeType() == Node.ELEMENT_NODE) {
                            Element element1 = (Element) node1;

                            int optionID = Integer.parseInt(element1.getAttribute("id"));
                            String option = element1.getTextContent();

                            question.addOption(optionID, option);
                        }
                    }
                    xmlParser.put(questionID, question);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
