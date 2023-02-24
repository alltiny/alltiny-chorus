package org.alltiny.chorus.io.xmlv1;

import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mapper for {@link Map<String,String>}
 *
 * @since 4.0
 */
public class MapAdapter extends XmlAdapter<MapAdapter.MapWrapper, Map<String,String>> {

    @Override
    public Map<String, String> unmarshal(MapWrapper wrapper) {
        return wrapper.elements.stream()
            .collect(Collectors.toMap(
                Node::getNodeName,
                Node::getTextContent)
            );
    }

    @Override
    public MapWrapper marshal(Map<String,String> map) throws Exception {
        final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

        final MapWrapper wrapper = new MapWrapper();
        wrapper.elements = map.entrySet()
            .stream()
            .map(e -> {
                final Element element = document.createElement(e.getKey());
                element.appendChild(document.createTextNode(e.getValue()));
                return element;
            })
            .collect(Collectors.toList());
        return wrapper;
    }

    static class MapWrapper {
        @XmlAnyElement
        List<Element> elements;
    }
}
