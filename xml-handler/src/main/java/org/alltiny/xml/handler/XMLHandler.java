package org.alltiny.xml.handler;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ContentHandler;

/**
 * This class is an abstract xml handler for SAXParsers. It allows to build
 * up a hierarchical structure of xml handlers. To us it, just derive your
 * handler implementation from this class.
 * @param <Type> type of node this handler is for.
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 13.11.2008
 */
public abstract class XMLHandler<Type> extends DefaultHandler {

    /** This is the reference onto the parent's assign handler. */
    private final AssignHandler<Type> assignHandler;

    /** This reference stores the current reader. */
    private XMLReader reader;

    /** This reference is used to store and restore the parent content handler. */
    private ContentHandler parentHandler;

    /**
     * This constructor creates a new handler instance. It requires
     * an reference onto an {@link AssignHandler} which shall be used
     * to assign the finally created object to a parent node.
     *
     * @param assignHandler which shall be used to assign the created
     * item to a parent. <code>null</code> is not allowed.
     */
    public XMLHandler(final AssignHandler<Type> assignHandler) {
        this.assignHandler = assignHandler;
    }

    protected final void setReader(XMLReader reader) {
        this.reader = reader;
    }

    /**
     * This method is used internally by XMLHandler to manage the hierarchical
     * parsing control.
     */
    private void setParentHandler(ContentHandler parentHandler) {
        this.parentHandler = parentHandler;
    }

    public final void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // resolve the given element by your own.
        XMLHandler childHandler = getHandlerInstance(uri, localName, qName, attributes);
        // check whether the handler was resolved.
        if (childHandler == null) {
            throw new SAXException("Element '" + qName + "' could not be resolved in handler " + this);
        }
        // pass the current reader instance to the child handler.
        childHandler.setReader(reader);
        // pass your reference to child handler to enable the child handler to give back control.
        childHandler.setParentHandler(this);
        // attach the child handler as current content handler to the reader.
        reader.setContentHandler(childHandler);
    }

    /**
     * This method is called by the SAXParser when the end of element was detected.
     */
    public final void endElement(String uri, String localName, String qName) throws SAXException {
        try { // let the parent attach the created object.
            assignHandler.assignNode(getObject());
        } catch (AssignException ex) {
            throw new SAXException("The created element could not be assigned to parent node", ex);
        }
        // give the parent content handler back control by attaching it to the reader.
        reader.setContentHandler(parentHandler);
    }

    /**
     * This method returns the reference onto the created object.
     */
    public abstract Type getObject();

    /**
     * This method should create and return an instance of {@link XMLHandler},
     * which is able to handle SAXEvents for the requested element. This XML-
     * handler-framework will give the delivered intance the control over all
     * following SAXEvents. If <code>null</code> is returned, then an
     * SAXException will be created to telling that "element XYZ was not resolved
     * by handler ABC". That allows a better error analysis.
     *
     * @return an handler instance which signs responsible for the given node.
     *   Please return <code>null</code> if your implementation could not
     *   resolve the requested element.
     *
     * @throws SAXException if an error in attributed was detected.
     */
    protected abstract XMLHandler getHandlerInstance(String uri, String localName, String qName, Attributes attributes) throws SAXException;
}
