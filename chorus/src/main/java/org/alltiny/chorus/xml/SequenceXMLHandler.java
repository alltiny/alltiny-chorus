package org.alltiny.chorus.xml;

import org.alltiny.chorus.base.type.Key;
import org.alltiny.chorus.dom.*;
import org.alltiny.chorus.base.type.Clef;
import org.alltiny.xml.handler.XMLHandler;
import org.alltiny.xml.handler.AssignHandler;
import org.alltiny.xml.handler.AssignException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 08.11.2008 13:42:57
 */
public class SequenceXMLHandler extends XMLHandler<Sequence> {

    private final Sequence sequence;

    public SequenceXMLHandler(Attributes attributes, AssignHandler<Sequence> assignHandler) throws SAXException {
        super(assignHandler);
        String value = attributes.getValue("clef");
        Clef clef;
        if ("G".equals(value)) {
            clef = Clef.G;
        } else if ("G8basso".equals(value)) {
            clef = Clef.G8basso;
        } else if ("F".equals(value)) {
            clef = Clef.F;
        } else {
            throw new SAXException("Attribute 'clef' is not proper defined in sequence.");
        }

        sequence = new Sequence().setClef(clef);

        String key = attributes.getValue("key");
        // default the key to Mayor C
        sequence.setKey(Key.C);

        if ("C".equals(key)) {
            sequence.setKey(Key.C);
        } else if ("a".equals(key)) {
            sequence.setKey(Key.a);
        } else if ("F".equals(key)) {
            sequence.setKey(Key.F);
        } else if ("d".equals(key)) {
            sequence.setKey(Key.d);
        } else if ("B".equals(key)) {
            sequence.setKey(Key.B);
        } else if ("g".equals(key)) {
            sequence.setKey(Key.g);
        } else if ("Es".equals(key)) {
            sequence.setKey(Key.Es);
        } else if ("c".equals(key)) {
            sequence.setKey(Key.c);
        } else if ("As".equals(key)) {
            sequence.setKey(Key.As);
        } else if ("f".equals(key)) {
            sequence.setKey(Key.f);
        } else if ("Des".equals(key)) {
            sequence.setKey(Key.Des);
        } else if ("b".equals(key)) {
            sequence.setKey(Key.b);
        } else if ("Ges".equals(key)) {
            sequence.setKey(Key.Ges);
        } else if ("es".equals(key)) {
            sequence.setKey(Key.es);
        } else if ("G".equals(key)) {
            sequence.setKey(Key.G);
        } else if ("e".equals(key)) {
            sequence.setKey(Key.e);
        } else if ("D".equals(key)) {
            sequence.setKey(Key.D);
        } else if ("h".equals(key)) {
            sequence.setKey(Key.h);
        } else if ("A".equals(key)) {
            sequence.setKey(Key.A);
        } else if ("fis".equals(key)) {
            sequence.setKey(Key.fis);
        } else if ("E".equals(key)) {
            sequence.setKey(Key.E);
        } else if ("cis".equals(key)) {
            sequence.setKey(Key.cis);
        } else if ("H".equals(key)) {
            sequence.setKey(Key.H);
        } else if ("gis".equals(key)) {
            sequence.setKey(Key.gis);
        } else if ("Fis".equals(key)) {
            sequence.setKey(Key.Fis);
        } else if ("dis".equals(key)) {
            sequence.setKey(Key.dis);
        }
    }

    protected XMLHandler getHandlerInstance(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("note".equals(qName)) {
            return new NoteXMLHandler(attributes, new SequenceAssignHandler<Note>(sequence));
        }
        if ("rest".equals(qName)) {
            return new RestXMLHandler(attributes, new SequenceAssignHandler<Rest>(sequence));
        }
        if ("bar".equals(qName)) {
            return new BarXMLHandler(attributes, new SequenceAssignHandler<Bar>(sequence));
        }
        if ("dynamic".equals(qName)) {
            return new DynamicElementXMLHandler(attributes, new SequenceAssignHandler<DynamicElement>(sequence));
        }
        if ("anchor".equals(qName)) {
            return new AnchorXMLHandler(attributes, new SequenceAssignHandler<Anchor>(sequence));
        }
        if ("repeat-begin".equals(qName)) {
            return new SimpleElementXMLHandler<Element>(new RepeatBegin(), new SequenceAssignHandler<Element>(sequence));
        }
        if ("repeat-end".equals(qName)) {
            return new SimpleElementXMLHandler<Element>(new RepeatEnd(), new SequenceAssignHandler<Element>(sequence));
        }
        if ("relTempoChange".equals(qName)) {
            return new RelativeTempoChangeXMLHandler(new SequenceAssignHandler<RelativeTempoChange>(sequence));
        }
        if ("absTempoChange".equals(qName)) {
            return new AbsoluteTempoChangeXMLHandler(new SequenceAssignHandler<AbsoluteTempoChange>(sequence), attributes);
        }

        // throw an exception if this point is reached.
        throw new SAXException("Element \'" + qName + "\' could not be resolved as child in \'song\'");
    }

    public Sequence getObject() {
        return sequence;
    }

    private static class SequenceAssignHandler<Type extends Element> implements AssignHandler<Type> {

        private final Sequence sequence;

        public SequenceAssignHandler(Sequence sequence) {
            this.sequence = sequence;
        }

        public void assignNode(Type node) throws AssignException {
            sequence.addElement(node);
        }
    }
}
