package org.alltiny.chorus.model.generic;

import java.util.function.BiConsumer;

public class DOMHierarchicalListener<Model,Value,Identifier> implements DOMEventListener<Model> {

    private final Locator<Model,Value,Identifier> locator;
    private final Callback<Value,Identifier> callback;
    private final DOMHierarchicalListener<Value,?,?> delegate;

    private String name; // name of this listener to help with debugging

    public DOMHierarchicalListener(Locator<Model,Value,Identifier> locator, Callback<Value,Identifier> callback) {
        this.locator = locator;
        this.callback = callback;
        this.delegate = null;
    }

    public DOMHierarchicalListener(Locator<Model,Value,Identifier> locator, DOMHierarchicalListener<Value,?,?> delegate) {
        this.locator = locator;
        this.callback = null;
        this.delegate = delegate;
    }

    public String getName() {
        return name;
    }

    public DOMHierarchicalListener<Model,Value,Identifier> setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public void initialize(Model model, Context<?> context) {
        if (delegate != null) {
            locator.delegate(model, delegate::initialize, context);
        } else {
            locator.initialize(model, callback, context);
        }
    }

    @Override
    public void shutdown(Model model, Context<?> context) {
        if (delegate != null) {
            locator.delegate(model, delegate::shutdown, context);
        } else {
            locator.shutdown(model, callback, context);
        }
    }

    @Override
    public void accept(DOMEvent<Model> event, Context<?> context) {
        if (event == null || !locator.isMatching(event)) {
            return;
        }
        if (delegate == null) { // no delegator present - use locator to resolve this event
            locator.resolve(event, callback, delegate != null, context);
        } else { // delegate
            if (event instanceof DOMCausedEvent && ((DOMCausedEvent)event).getCause() != null) {
                // resolve event-based
                locator.delegate(event, delegate::accept, context);
            } else { // no cause - resolve model based
                if (event instanceof DOMIndexedItemInsertedEvent ||
                    event instanceof DOMPropertyAddedEvent) {
                    locator.delegate(event.getSource(), delegate::initialize, new Context<>(null, null, event.getOperation(), context));
                } else if (event instanceof DOMIndexedItemRemovedEvent) {
                    DOMIndexedItemRemovedEvent<Model,Value> iire = (DOMIndexedItemRemovedEvent<Model,Value>)event;
                    delegate.shutdown(iire.getItem(), new Context<>(null, null, event.getOperation(), context));
                } else if (event instanceof DOMPropertyRemovedEvent) {
                    DOMPropertyRemovedEvent<Model,Value> re = (DOMPropertyRemovedEvent<Model,Value>)event;
                    delegate.shutdown(re.getOldValue(), new Context<>(null, null, event.getOperation(), context));
                } else if (event instanceof DOMPropertyChangedEvent) {
                    DOMPropertyChangedEvent<Model,Value> pce = (DOMPropertyChangedEvent<Model,Value>)event;
                    delegate.shutdown(pce.getOldValue(), new Context<>(null, null, event.getOperation(), context));
                    delegate.initialize(pce.getNewValue(), new Context<>(null, null, event.getOperation(), context));
                }
            }
        }
    }

    @Override
    public String toString() {
        return "DOMHierarchicalListener{" +
            "name='" + name + '\'' +
            '}';
    }

    public interface Callback<Value,Identifier> {
        void added(Value value, Identifier identifier, Context<?> context);
        void changed(Value value, Identifier identifier, Context<?> context);
        void removed(Value value, Identifier identifier, Context<?> context);
    }

    public interface Locator<Model,Value,Identifier> {
        boolean isMatching(DOMEvent<?> event);
        void resolve(DOMEvent<?> event, Callback<Value,Identifier> callback, boolean hasDelegate, Context<?> context);
        void delegate(Model model, BiConsumer<Value, Context<Identifier>> delegateFunction, Context<?> context);
        void delegate(DOMEvent<Model> event, BiConsumer<DOMEvent<Value>, Context<Identifier>> delegateFunction, Context<?> context);
        void initialize(Model model, Callback<Value,Identifier> callback, Context<?> context);
        void shutdown(Model model, Callback<Value,Identifier> callback, Context<?> context);
    }

    public static class PropertyOnMap<Model extends DOMMap<Model,?>,Value> implements Locator<Model,Value,String> {

        private final Class<Model> modelClass;
        private final String propertyName;

        public PropertyOnMap(Class<Model> modelClass, String propertyName) {
            this.modelClass = modelClass;
            this.propertyName = propertyName;
        }

        @Override
        public boolean isMatching(DOMEvent<?> event) {
            return event.getSource().getClass().isAssignableFrom(modelClass) &&
                event instanceof DOMPropertyChangedEvent &&
                ((DOMPropertyChangedEvent<?,?>)event).getPropertyName().equals(propertyName);
        }

        @Override
        public void resolve(DOMEvent<?> event, Callback<Value,String> callback, boolean hasDelegate, Context<?> context) {
            if (event instanceof DOMPropertyRemovedEvent) {
                Value value = ((DOMPropertyRemovedEvent<Model,Value>)event).getOldValue();
                callback.removed(value, propertyName, new Context<>(propertyName, (DOMNode<?>)event.getSource(), event.getOperation(), context));
            } else if (event instanceof DOMPropertyAddedEvent && !hasDelegate) {
                Value value = ((DOMPropertyAddedEvent<Model,Value>)event).getNewValue();
                callback.added(value, propertyName, new Context<>(propertyName, (DOMNode<?>)event.getSource(), event.getOperation(), context));
            } else if (event instanceof DOMPropertyChangedEvent && !hasDelegate) {
                Value value = ((DOMPropertyChangedEvent<Model,Value>)event).getNewValue();
                callback.changed(value, propertyName, new Context<>(propertyName, (DOMNode<?>)event.getSource(), event.getOperation(), context));
            }
        }

        @Override
        public void delegate(Model model, BiConsumer<Value, Context<String>> delegateFunction, Context<?> context) {
            if (model == null) {
                return;
            }
            delegateFunction.accept(
                (Value)model.get(propertyName),
                new Context<>(propertyName, model, context)
            );
        }

        @Override
        public void delegate(DOMEvent<Model> event, BiConsumer<DOMEvent<Value>,Context<String>> delegateFunction, Context<?> context) {
            if (event == null) {
                return;
            }
            if (isMatching(event)) {
                delegateFunction.accept(
                    ((DOMPropertyChangedEvent)event).getCause(),
                    new Context<>(propertyName, event.getSource(), event.getOperation(), context)
                );
            }
        }

        @Override
        public void initialize(Model model, Callback<Value,String> callback, Context<?> context) {
            callback.added(
                (Value)model.get(propertyName),
                propertyName,
                new Context<>(propertyName, model, context)
            );
        }

        @Override
        public void shutdown(Model model, Callback<Value,String> callback, Context<?> context) {
            callback.removed(
                (Value)model.get(propertyName),
                propertyName,
                new Context<>(propertyName, model, context)
            );
        }

        @Override
        public String toString() {
            return "PropertyOnMap{" + modelClass + "[" + propertyName + "]}";
        }
    }

    public static class AnyItemInList<Model extends DOMList<?,Value>,Value> implements Locator<Model,Value,Integer> {

        @Override
        public boolean isMatching(DOMEvent<?> event) {
            return
                event instanceof DOMIndexedItemChangedEvent &&
                event.getSource() instanceof DOMList;
        }

        @Override
        public void resolve(DOMEvent<?> event, Callback<Value,Integer> callback, boolean hasDelegate, Context<?> context) {
            if (event instanceof DOMIndexedItemChangedEvent) {
                DOMIndexedItemChangedEvent<Model,Value> ce = (DOMIndexedItemChangedEvent<Model,Value>)event;
                final int i = ce.getIndex();
                if (event instanceof DOMIndexedItemRemovedEvent) {
                    callback.removed(ce.getItem(), i, new Context<>(i, ce.getSource(), event.getOperation(), context));
                } else if (event instanceof DOMIndexedItemInsertedEvent && !hasDelegate) {
                    callback.added(ce.getItem(), i, new Context<>(i, ce.getSource(), event.getOperation(), context));
                } else if (event instanceof DOMIndexedItemChangedEvent && !hasDelegate) {
                    callback.changed(ce.getItem(), i, new Context<>(i, ce.getSource(), event.getOperation(), context));
                }
            }
        }

        @Override
        public void delegate(Model model, BiConsumer<Value,Context<Integer>> delegateFunction, Context<?> context) {
            for (int i = 0; i < model.size(); i++) {
                delegateFunction.accept(
                    model.get(i),
                    new Context<>(i, model, context)
                );
            }
        }

        @Override
        public void delegate(DOMEvent<Model> event, BiConsumer<DOMEvent<Value>,Context<Integer>> delegateFunction, Context<?> context) {
            if (isMatching(event)) {
                delegateFunction.accept(
                    ((DOMIndexedItemChangedEvent)event).getCause(),
                    new Context<>(((DOMIndexedItemChangedEvent)event).getIndex(), event.getSource(), event.getOperation(), context)
                );
            }
        }

        @Override
        public void initialize(Model model, Callback<Value,Integer> callback, Context<?> context) {
            for (int i = 0; i < model.size(); i++) {
                callback.added(model.get(i), i, new Context<>(i, model, context));
            }
        }

        @Override
        public void shutdown(Model model, Callback<Value,Integer> callback, Context<?> context) {
            for (int i = 0; i < model.size(); i++) {
                callback.removed(model.get(i), i, new Context<>(i, model, context));
            }
        }

        @Override
        public String toString() {
            return "AnyItemInList{}";
        }
    }
}
