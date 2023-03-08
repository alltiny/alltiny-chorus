package org.alltiny.chorus.dom;

import org.junit.Assert;
import org.junit.Test;

/**
 * This class ensures that {@link RelativeTempoChange} works correctly.
 */
public class TempoChangeTest {

    @Test
    public void testDurationFactor() {
        Assert.assertEquals(0.666d, 0.001d, new RelativeTempoChange()
            .setLeft(new DurationElement(3,4))
            .setRight(new DurationElement(1,2)).getDurationFactor());
    }
}
