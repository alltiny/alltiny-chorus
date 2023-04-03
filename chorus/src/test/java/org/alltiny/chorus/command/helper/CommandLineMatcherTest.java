package org.alltiny.chorus.command.helper;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * Tests for {@link CommandLineMatcher}.
 */
public class CommandLineMatcherTest {

    @Test
    public void testHelpFoo() {
        CommandLineMatcher matcher = new CommandLineMatcher(
            Collections.singletonList(new CommandWord("help")),
            new CommandLine("h foo")
        );

        Assert.assertTrue("should be matching", matcher.isMatching());
        Assert.assertEquals("arguments should be",
            Collections.singletonList(new CommandLineToken().setCharacters("foo").setStartPos(2).setEndPos(4)),
            matcher.getArguments());
    }

    @Test
    public void testAddVoice1Soprano() {
        CommandLineMatcher matcher = new CommandLineMatcher(
            Arrays.asList(
                new CommandWord("add"),
                new CommandWord("voice")
            ),
            new CommandLine("ad v 1 Soprano")
        );

        Assert.assertTrue("should be matching", matcher.isMatching());
        Assert.assertEquals("arguments should be",
            Arrays.asList(
                new CommandLineToken().setCharacters("1").setStartPos(5).setEndPos(5),
                new CommandLineToken().setCharacters("Soprano").setStartPos(7).setEndPos(13)
            ),
            matcher.getArguments());
    }

    @Test
    public void testAddVoice1SopranoCamelCase() {
        CommandLineMatcher matcher = new CommandLineMatcher(
            Arrays.asList(
                new CommandWord("add"),
                new CommandWord("voice")
            ),
            new CommandLine("aV 1 Soprano")
        );

        Assert.assertTrue("should be matching", matcher.isMatching());
        Assert.assertEquals("arguments should be",
            Arrays.asList(
                new CommandLineToken().setCharacters("1").setStartPos(3).setEndPos(3),
                new CommandLineToken().setCharacters("Soprano").setStartPos(5).setEndPos(11)
            ),
            matcher.getArguments());
    }

    @Test
    public void testOptionalCommand() {
        CommandLineMatcher matcher = new CommandLineMatcher(
            Arrays.asList(
                new CommandWord("open"),
                new CommandWord("file", false)
            ),
            new CommandLine("op")
        );

        Assert.assertTrue("should be matching", matcher.isMatching());
        Assert.assertTrue("arguments should be empty", matcher.getArguments().isEmpty());
    }
}
