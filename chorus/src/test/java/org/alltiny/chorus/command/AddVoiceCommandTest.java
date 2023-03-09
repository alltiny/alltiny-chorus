package org.alltiny.chorus.command;

import org.alltiny.chorus.model.app.ApplicationModel;
import org.junit.Assert;
import org.junit.Test;

public class AddVoiceCommandTest {

    @Test
    public void testOrderOfVoices() {
        final ApplicationModel model = new ApplicationModel();
        final AddVoiceCommand command = new AddVoiceCommand(model);

        model.setCommandLine("add voice 1 a");
        command.getExecutableFunction().apply(null);
        model.setCommandLine("add voice 1 b");
        command.getExecutableFunction().apply(null);

        Assert.assertNotNull("model should have a song", model.getCurrentSong());
        Assert.assertNotNull("song should have music", model.getCurrentSong().getMusic());
        Assert.assertEquals("number of voice should be", 2, model.getCurrentSong().getMusic().getVoices().size());
        Assert.assertEquals("name of first voice is", "b", model.getCurrentSong().getMusic().getVoices().get(0).getName());
        Assert.assertEquals("name of second voice is", "a", model.getCurrentSong().getMusic().getVoices().get(1).getName());
    }
}
