package org.alltiny.chorus.command;

import jakarta.xml.bind.JAXBContext;
import org.alltiny.chorus.command.generic.Command;
import org.alltiny.chorus.command.generic.ExecutedCommand;
import org.alltiny.chorus.dom.Song;
import org.alltiny.chorus.io.xmlv1.XMLSongV1;
import org.alltiny.chorus.model.app.AppMessage;
import org.alltiny.chorus.model.app.ApplicationModel;
import org.alltiny.chorus.model.converter.FromXMLV1Converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class OpenFileCommand extends Command<OpenFileCommand> {

    private static final Pattern pattern = Pattern.compile("open (?<filename>.+)");

    public OpenFileCommand(ApplicationModel appModel) {
        super(appModel);
    }

    @Override
    public boolean feelsResponsible() {
        return pattern.matcher(getAppModel().getCommandLine()).matches();
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
            Matcher matcher = pattern.matcher(getAppModel().getCommandLine());
            if (!matcher.matches()) {
                return null;
            }
            final String filename = matcher.group("filename");
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
            if (start[0] == 0x1f) { // probably a GZIP-File
                try (InputStream in = new GZIPInputStream(new FileInputStream(filename))) {
                    song = (XMLSongV1) JAXBContext.newInstance(XMLSongV1.class)
                        .createUnmarshaller()
                        .unmarshal(in);
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
}
