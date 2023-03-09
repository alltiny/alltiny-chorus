package org.alltiny.chorus.gui;

import org.alltiny.chorus.dom.Music;
import org.alltiny.chorus.dom.Song;
import org.alltiny.chorus.dom.Voice;
import org.alltiny.chorus.model.app.ApplicationModel;
import org.junit.Assert;
import org.junit.Test;

public class MuteVoiceToolbarTest {

    @Test
    public void testOrderOfComponents() {
        final ApplicationModel model = new ApplicationModel();
        final MuteVoiceToolbar toolbar = new MuteVoiceToolbar(model);

        model.setCurrentSong(new Song());
        model.getCurrentSong().setMusic(new Music());
        model.getCurrentSong().getMusic().addVoice(new Voice().setName("a"), 0);
        // b inserted on index 0 should move a to index 1
        model.getCurrentSong().getMusic().addVoice(new Voice().setName("b"), 0);

        Assert.assertEquals("toolbar should have 2 buttons", 2, toolbar.getComponents().length);
        Assert.assertEquals("first button should be labeled", "b", ((MuteVoiceToggleButton)toolbar.getComponents()[0]).getText());
        Assert.assertEquals("second button should be labeled", "a", ((MuteVoiceToggleButton)toolbar.getComponents()[1]).getText());
    }

    @Test
    public void testCreationWithoutIndex() {
        final ApplicationModel model = new ApplicationModel();
        final MuteVoiceToolbar toolbar = new MuteVoiceToolbar(model);

        model.setCurrentSong(new Song());
        model.getCurrentSong().setMusic(new Music());
        model.getCurrentSong().getMusic().addVoice(new Voice().setName("a"));

        Assert.assertEquals("toolbar should have 1 buttons", 1, toolbar.getComponents().length);
        Assert.assertEquals("first button should be labeled", "a", ((MuteVoiceToggleButton)toolbar.getComponents()[0]).getText());
    }
}
