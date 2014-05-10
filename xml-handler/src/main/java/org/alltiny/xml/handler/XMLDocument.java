package org.alltiny.xml.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This is the xml handler of the document. It requires the class of the
 * expected root node handler. It invokes the constructor of the handler
 * via reflection and passes a reference of a root node assign handler to
 * it.
 * @param <Type> type of root node.
 */
public class XMLDocument<Type> extends XMLHandler<Type> {

    private XMLHandler<Type> rootHandler;
    private final RootAssignHandler<Type> rootAssignHandler = new RootAssignHandler<Type>();

    public XMLDocument(Class<? extends XMLHandler<Type>> rootHandlerClass) {
        this(new GenericXMLHandlerFactory<Type>(rootHandlerClass));
    }

    public XMLDocument(XMLHandlerFactory<Type> rootHandlerFactory) {
        /* pass a null implementation to the super class. note that this
         * document handler is never required to assign a node to a parent. */
        super(new NullAssignHandler<Type>());

        // use the given factory to create the root handler.
        rootHandler = rootHandlerFactory.createInstance(rootAssignHandler);
    }

    public Type getObject() {
        return rootAssignHandler.getRootNode();
    }

    protected XMLHandler getHandlerInstance(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        return rootHandler;
    }

    /**
     * This is an assign handler implementation which is only used internally
     * in this document handler to handle the reference onto the root node.
     */
    private static class RootAssignHandler<Type> implements AssignHandler<Type> {

        private Type rootNode = null;

        /**
         * This method is called by the child handler to assign the found root
         * node. Note that a xml document can only define one root node instance.
         */
        public void assignNode(Type node) throws AssignException {
            if (rootNode != null) {
                throw new AssignException(this, node, "This document was allready a root node assigned.");
            }

            rootNode = node;
        }

        public Type getRootNode() {
            return rootNode;
        }
    }

    /**
     * This generic factory was created to let {@link XMLDocument} support two different
     * constructors. One taking a {@link XMLHandler}-implementation and another taking a
     * Class which should be instantiated as handler. This generic factory uses reflection
     * the create a new instance.
     */
    private static class GenericXMLHandlerFactory<Type> implements XMLHandlerFactory<Type> {

        private final Class<? extends XMLHandler<Type>> rootHandlerClass;

        public GenericXMLHandlerFactory(Class<? extends XMLHandler<Type>> rootHandlerClass) {
            this.rootHandlerClass = rootHandlerClass;
        }

        public XMLHandler<Type> createInstance(AssignHandler<Type> assignHandler) {
            try { // instantiate the handler class.
                return rootHandlerClass.getConstructor(new Class[]{AssignHandler.class}).newInstance(new Object[]{assignHandler});
            } catch (Exception e) {
                throw new Error("Could not initialize given handler class: " + rootHandlerClass, e);
            }
        }
    }
}
