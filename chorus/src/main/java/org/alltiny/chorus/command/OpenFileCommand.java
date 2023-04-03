package org.alltiny.chorus.command;

import jakarta.xml.bind.JAXBContext;
import org.alltiny.chorus.command.generic.Command;
import org.alltiny.chorus.command.generic.ExecutedCommand;
import org.alltiny.chorus.command.helper.CommandLineMatcher;
import org.alltiny.chorus.command.helper.CommandLineToken;
import org.alltiny.chorus.command.helper.CommandWord;
import org.alltiny.chorus.dom.Song;
import org.alltiny.chorus.io.xmlv1.XMLSongV1;
import org.alltiny.chorus.model.app.AppMessage;
import org.alltiny.chorus.model.app.ApplicationModel;
import org.alltiny.chorus.model.app.FileType;
import org.alltiny.chorus.model.converter.FromXMLV1Converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

public class OpenFileCommand extends Command<OpenFileCommand> {

    private static final List<CommandWord> commandWords = Arrays.asList(
        new CommandWord("open"), new CommandWord("file", false)
    );

    public OpenFileCommand(ApplicationModel appModel) {
        super(appModel);
    }

    @Override
    public boolean feelsResponsible() {
        return new CommandLineMatcher(commandWords, getAppModel().getCommandLine()).isMatching();
    }

    @Override
    public String getUsageOneLine() {
        return "open <file>";
    }

    @Override
    public String getDescriptionOneLine() {
        return "opens the given file";
    }

    @Override
    public Function<Void, ExecutedCommand<OpenFileCommand>> getExecutableFunction() {
        return unused -> {
            CommandLineMatcher matcher = new CommandLineMatcher(commandWords, getAppModel().getCommandLine());
            if (!matcher.isMatching()) {
                return null;
            }
            final String filename = getFileName(matcher.getArguments());
            final byte[] start;
            try (InputStream in = new FileInputStream(filename)) {
                start = in.readNBytes(1);
            } catch (IOException e) {
                getAppModel().getApplicationMessageQueue().add(
                    new AppMessage(AppMessage.Type.ERROR, "cannot read file '" + filename + "': " + e.getMessage())
                );
                return null;
            }

            final XMLSongV1 song;
            final FileType fileType;
            if (start[0] == 0x1f) { // probably a GZIP-File
                try (InputStream in = new GZIPInputStream(new FileInputStream(filename))) {
                    song = (XMLSongV1) JAXBContext.newInstance(XMLSongV1.class)
                        .createUnmarshaller()
                        .unmarshal(in);
                    fileType = FileType.GZ_XML;
                } catch (Exception e) {
                    getAppModel().getApplicationMessageQueue().add(
                        new AppMessage(AppMessage.Type.ERROR, "cannot read file '" + filename + "': " + e.getMessage())
                    );
                    return null;
                }
            } else if (start[0] == '<') { // XML Preamble
                try (InputStream in = new FileInputStream(filename)) {
                    song = (XMLSongV1) JAXBContext.newInstance(XMLSongV1.class)
                        .createUnmarshaller()
                        .unmarshal(in);
                    fileType = FileType.XML;
                } catch (Exception e) {
                    getAppModel().getApplicationMessageQueue().add(
                        new AppMessage(AppMessage.Type.ERROR, "cannot read file '" + filename + "': " + e.getMessage())
                    );
                    return null;
                }
            } else {
                getAppModel().getApplicationMessageQueue().add(
                    new AppMessage(AppMessage.Type.ERROR, "unknown file type '" + filename + "'")
                );
                return null;
            }

            // map the song to the internal model.
            Song songModel = new FromXMLV1Converter().convert(song);
            songModel.setFilename(filename);
            getAppModel().setCurrentSong(songModel);
            getAppModel().setFileType(fileType);

            getAppModel().getApplicationMessageQueue()
                .add(new AppMessage(AppMessage.Type.SUCCESS, "opened file '" + filename + "'"));

            return new ExecutedCommand<OpenFileCommand>()
                .setResolvedCommand("open " + filename)
                .setSuccessful(true);
        };
    }

    public static String commandFor(File file) {
        return "open " + file.getAbsolutePath();
    }

    public static String getFileName(List<CommandLineToken> tokens) {
        return tokens.stream()
            .map(CommandLineToken::getCharacters)
            .collect(Collectors.joining(" "));
    }
}
