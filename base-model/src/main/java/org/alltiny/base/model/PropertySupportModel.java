package org.alltiny.base.model;

import java.beans.PropertyChangeListener;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 31.07.2009
 */
public interface PropertySupportModel {

    public void addPropertyChangeListener(PropertyChangeListener listener);
    public void addPropertyChangeListener(String propName, PropertyChangeListener listener);

    public void removePropertyChangeListener(PropertyChangeListener listener);
    public void removePropertyChangeListener(String propName, PropertyChangeListener listener);
}
