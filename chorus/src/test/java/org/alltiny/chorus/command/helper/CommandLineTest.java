package org.alltiny.chorus.command.helper;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test for {@link CommandLine}.
 */
public class CommandLineTest {

    @Test
    public void testSplit() {
        Assert.assertEquals("tokens should be",
            Arrays.asList(
                new CommandLineToken().setCharacters("add").setStartPos(0).setEndPos(2),
                new CommandLineToken().setCharacters("voice").setStartPos(4).setEndPos(8),
                new CommandLineToken().setCharacters("foo").setStartPos(10).setEndPos(12)
            ),
            new CommandLine("add voice foo").getTokens());
    }
}
