package org.alltiny.chorus.model.generic;

public class DOMListListenerAdapter<Value> implements DOMEventListener<DOMList<DOMList<?,Value>,Value>> {

    private final DOMList<DOMList<?,Value>,Value> source;

    public DOMListListenerAdapter(DOMList<DOMList<?, Value>, Value> source) {
        this.source = source;
    }

    @Override
    public void accept(DOMEvent<DOMList<DOMList<?, Value>, Value>> domListDOMEvent, Context<?> context) {
        if (domListDOMEvent.getSource() != source) {
            return;
        }
        if (domListDOMEvent instanceof DOMIndexedItemInsertedEvent) {
            inserted((DOMIndexedItemInsertedEvent<DOMList<DOMList<?,Value>,Value>,Value>)domListDOMEvent);
        } else if (domListDOMEvent instanceof DOMIndexedItemRemovedEvent) {
            removed((DOMIndexedItemRemovedEvent<DOMList<DOMList<?,Value>,Value>,Value>)domListDOMEvent);
        } else if (domListDOMEvent instanceof DOMIndexedItemChangedEvent) {
            changed((DOMIndexedItemChangedEvent<DOMList<DOMList<?,Value>,Value>,Value>)domListDOMEvent);
        }
    }

    protected void inserted(DOMIndexedItemInsertedEvent<DOMList<DOMList<?,Value>,Value>,Value> event) {}
    protected void removed(DOMIndexedItemRemovedEvent<DOMList<DOMList<?,Value>,Value>,Value> event) {}
    protected void changed(DOMIndexedItemChangedEvent<DOMList<DOMList<?,Value>,Value>,Value> event) {}
}
