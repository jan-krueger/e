package de.SweetCode.e.resources.dialogue.parser;

import com.sun.istack.internal.Nullable;
import de.SweetCode.e.E;
import de.SweetCode.e.resources.dialogue.Dialogue;
import de.SweetCode.e.resources.dialogue.DialogueNode;
import de.SweetCode.e.resources.dialogue.DialogueOptionPointer;
import de.SweetCode.e.resources.dialogue.DialogueParser;
import de.SweetCode.e.resources.dialogue.condition.DialogueConditionWrapper;
import de.SweetCode.e.resources.dialogue.condition.DialogueConditions;
import de.SweetCode.e.utils.Assert;
import de.SweetCode.e.utils.log.LogEntry;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class YarnXmlParser implements DialogueParser<File, String> {

    private final static Pattern pattern = Pattern.compile("\\[{2}(.*?)\\|(.*?)(\\|(@.*))?\\]{2}");

    private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder documentBuilder = this.factory.newDocumentBuilder();


    private Map<DialogueNode<String>, List<DialogueOptionPointer>> dialogueNodes = new HashMap<>();

    public YarnXmlParser() throws ParserConfigurationException {
    }

    @Override
    public Dialogue parse(String startIdentifier, File input, @Nullable DialogueConditions conditions) {

        Assert.assertNotNull("The input cannot be null.", input);
        Assert.assertNotNull("The start identifier cannot be null.", startIdentifier);

        // prepare to parse again
        this.dialogueNodes.clear();

        Document document = null;

        try {
            document = this.documentBuilder.parse(input);
        } catch (SAXException | IOException e) {
            E.getE().getLog().log(
                LogEntry.Builder.create()
                    .message("Failed to parse the provided file (%s). Error: %s", input, e.getMessage())
                .build()
            );
        }

        Map<String, DialogueConditionWrapper> conditionWrapper = DialogueConditionWrapper.getConditionWrappers(conditions);

        //http://stackoverflow.com/a/13787629
        document.normalize();

        // validate first node name
        String nodeName = document.getDocumentElement().getNodeName();

        if(!(nodeName.equalsIgnoreCase("nodes"))) {
            //@TODO handle - invalid file
        }

        NodeList nodeList = document.getElementsByTagName("node");

        for (int i = 0; i < nodeList.getLength(); i++) {

            Node node = nodeList.item(i);

        }

        return new Dialogue<>(null);
    }

    private boolean isValidNode(Node node) {

        if(!(node.getNodeType() == Node.ELEMENT_NODE)) {
            return false;
        }

        return false;
    }

}
