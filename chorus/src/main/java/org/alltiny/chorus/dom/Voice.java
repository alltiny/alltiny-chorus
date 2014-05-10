package org.alltiny.chorus.dom;

import java.util.List;
import java.util.ArrayList;

/**
 * This class represents a single voice which is a sequence of notes and rests.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 26.12.2007 20:31:08
 */
public class Voice {

    private String head;
    private String name;
    private Sequence sequence;
    private List<InlineSequence> inlineSequences = new ArrayList<InlineSequence>();

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }

    public Sequence getSequence() {
        return sequence;
    }

    public void addInlineSequence(InlineSequence sequence) {
        inlineSequences.add(sequence);
    }

    public List<InlineSequence> getInlineSequences() {
        return inlineSequences;
    }
}
