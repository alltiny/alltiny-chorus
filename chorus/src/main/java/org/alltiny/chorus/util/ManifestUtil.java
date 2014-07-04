package org.alltiny.chorus.util;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Manifest;

/**
 * This class help dealing with Manifest attributes.
 */
public class ManifestUtil {

    private Manifest manifest = null;

    /**
     * This constructor will search the Manifest file containing the given implementation title.
     */
    public ManifestUtil(String implementationTitle) {
        try {
            Enumeration<URL> resources = getClass().getClassLoader().getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                Manifest manifest = new Manifest(resources.nextElement().openStream());
                String manifestImplTitle = manifest.getMainAttributes().getValue("Implementation-Title");
                if (implementationTitle.equals(manifestImplTitle)) {
                    this.manifest = manifest;
                    break;
                }
            }
        } catch (IOException e) { /* nothing to do. */ }
    }

    /**
     * @return the value for the attribute with the given name or the default value if none could be found.
     */
    public String getMainAttribute(String name, String defaultValue) {
        String value = manifest != null ? manifest.getMainAttributes().getValue(name) : null;
        return value != null ? String.valueOf(value) : defaultValue;
    }
}
