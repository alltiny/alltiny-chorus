package org.alltiny.xml.handler;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;

/**
 * This test ensures the {@link XMLParser} is working.
 */
public class XMLParsingTest {

    @Test
    public void testParsing() throws Exception {
        String xmlData = "<a><b><c/><c/></b></a>";

        XMLDocument<A> doc = new XMLDocument<A>(AHandler.class);
        XMLParser.parseXML(new ByteArrayInputStream(xmlData.getBytes()), doc);

        // start evaluation.
        Assert.assertNotNull("Root element must not be null.", doc.getObject());
        A aElement = doc.getObject();
        Assert.assertNotNull("Child of 'A' must not be null.", aElement.getChild());
        B bElement = aElement.getChild();
        Assert.assertNotNull("Child-List of 'B' must not be null.", bElement.getChildren());
        Assert.assertEquals("Wrong number of children in node 'B'.", 2, bElement.getChildren().size());
    }

    @Test(expected = SAXException.class)
    public void testInvalidDocumentParsing() throws Exception {
        String xmlData = "<a></a>"; // invalid xml stream with two root nodes.

        XMLDocument<A> doc = new XMLDocument<A>(AHandler.class);
        XMLParser.parseXML(new ByteArrayInputStream(xmlData.getBytes()), doc);

        // provoke a second assignment of the root node by parsing a second time with the same document.
        XMLParser.parseXML(new ByteArrayInputStream(xmlData.getBytes()), doc);
    }
}
