package com.quiz.lukebarratt;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class DOMParser {

    static void parseXMLFile() {
        Scanner scanner = new Scanner(System.in);
        try {
            File inputFile = new File("questionaire.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputFile);
            document.getDocumentElement().normalize();
            NodeList questionList = document.getElementsByTagName("Question");

            for (int i = 0; i < questionList.getLength(); i++) {
                Node q = questionList.item(i);

                if (q.getNodeType() == Node.ELEMENT_NODE) {
                    Element question = (Element) q;
                    String questionNumber = question.getAttribute("id");
                    System.out.println("Question " + questionNumber + ": ");
                    System.out.println(question.getElementsByTagName("text").item(0).getTextContent());
                    int answer = scanner.nextInt();
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
