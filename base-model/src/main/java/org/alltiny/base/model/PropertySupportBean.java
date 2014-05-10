package org.alltiny.base.model;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 31.07.2009
 */
public class PropertySupportBean implements PropertySupportModel {

    protected PropertyChangeSupport pcs;

    public PropertySupportBean() {
        pcs = new PropertyChangeSupport(this); // note that this points to the deriving class.
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String propName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propName, listener);
    }

    protected void firePropertyChange(String name, boolean oldValue, boolean newValue) {
        pcs.firePropertyChange(name, oldValue, newValue);
    }

    protected void firePropertyChange(String name, Object oldValue, Object newValue) {
        pcs.firePropertyChange(name, oldValue, newValue);
    }
}
