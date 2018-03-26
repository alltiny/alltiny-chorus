package org.alltiny.chorus.render.element;

import org.alltiny.chorus.dom.DurationElement;
import org.junit.Assert;
import org.junit.Test;

/**
 * This test ensures that {@link NoteRender} is working correctly.
 */
public class NoteRenderTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNullAsNoteIsRejected() {
        new NoteRender(null);
    }

    @Test
    public void testQuarterNoteIsRenderedWithoutAnyFlag() {
        Assert.assertEquals("number of flags", 0, NoteRender.calcNumberOfFlags(new DurationElement(1, 4)));
    }

    @Test
    public void testEightsNoteIsRenderedWithOneFlag() {
        Assert.assertEquals("number of flags", 1, NoteRender.calcNumberOfFlags(new DurationElement(1, 8)));
    }

    @Test
    public void testSixteenthNoteIsRenderedWithTwoFlags() {
        Assert.assertEquals("number of flags", 2, NoteRender.calcNumberOfFlags(new DurationElement(1, 16)));
    }

    @Test
    public void testThirtySecondsNoteIsRenderedWithOneFlag() {
        Assert.assertEquals("number of flags", 3, NoteRender.calcNumberOfFlags(new DurationElement(1, 32)));
    }

    @Test
    public void testSixtyFourthNoteIsRenderedWithOneFlag() {
        Assert.assertEquals("number of flags", 4, NoteRender.calcNumberOfFlags(new DurationElement(1, 64)));
    }

    @Test
    public void testDottedQuarterNoteIsRenderedWithoutAnyFlag() {
        Assert.assertEquals("number of flags", 0, NoteRender.calcNumberOfFlags(new DurationElement(3, 8)));
    }

    @Test
    public void testDottedEightsNoteIsRenderedWithOneFlag() {
        Assert.assertEquals("number of flags", 1, NoteRender.calcNumberOfFlags(new DurationElement(3, 16)));
    }

    @Test
    public void testDottedSixteenthNoteIsRenderedWithTwoFlags() {
        Assert.assertEquals("number of flags", 2, NoteRender.calcNumberOfFlags(new DurationElement(3, 32)));
    }

    @Test
    public void testDottedThirtySecondsNoteIsRenderedWithOneFlag() {
        Assert.assertEquals("number of flags", 3, NoteRender.calcNumberOfFlags(new DurationElement(3, 64)));
    }

    @Test
    public void testDottedSixtyFourthNoteIsRenderedWithOneFlag() {
        Assert.assertEquals("number of flags", 4, NoteRender.calcNumberOfFlags(new DurationElement(3, 128)));
    }

    @Test
    public void testDoubleDottedQuarterNoteIsRenderedWithoutAnyFlag() {
        Assert.assertEquals("number of flags", 0, NoteRender.calcNumberOfFlags(new DurationElement(7, 16)));
    }

    @Test
    public void testDoubleDottedEightsNoteIsRenderedWithOneFlag() {
        Assert.assertEquals("number of flags", 1, NoteRender.calcNumberOfFlags(new DurationElement(7, 32)));
    }

    @Test
    public void testDoubleDottedSixteenthNoteIsRenderedWithTwoFlags() {
        Assert.assertEquals("number of flags", 2, NoteRender.calcNumberOfFlags(new DurationElement(7, 64)));
    }

    @Test
    public void testDoubleDottedThirtySecondsNoteIsRenderedWithOneFlag() {
        Assert.assertEquals("number of flags", 3, NoteRender.calcNumberOfFlags(new DurationElement(7, 128)));
    }

    @Test
    public void testDoubleDottedSixtyFourthNoteIsRenderedWithOneFlag() {
        Assert.assertEquals("number of flags", 4, NoteRender.calcNumberOfFlags(new DurationElement(7, 256)));
    }
}
