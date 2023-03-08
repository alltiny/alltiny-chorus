package org.alltiny.chorus.model.converter;

import org.alltiny.chorus.dom.AbsoluteTempoChange;
import org.alltiny.chorus.dom.Anchor;
import org.alltiny.chorus.dom.Bar;
import org.alltiny.chorus.dom.DurationElement;
import org.alltiny.chorus.dom.Element;
import org.alltiny.chorus.dom.InlineSequence;
import org.alltiny.chorus.dom.Music;
import org.alltiny.chorus.dom.Note;
import org.alltiny.chorus.dom.RelativeTempoChange;
import org.alltiny.chorus.dom.RepeatBegin;
import org.alltiny.chorus.dom.RepeatEnd;
import org.alltiny.chorus.dom.Rest;
import org.alltiny.chorus.dom.Sequence;
import org.alltiny.chorus.dom.Song;
import org.alltiny.chorus.dom.Voice;
import org.alltiny.chorus.dom.decoration.Accidental;
import org.alltiny.chorus.dom.decoration.Beam;
import org.alltiny.chorus.dom.decoration.Bound;
import org.alltiny.chorus.dom.decoration.Decoration;
import org.alltiny.chorus.dom.decoration.Fermata;
import org.alltiny.chorus.dom.decoration.Triplet;
import org.alltiny.chorus.io.xmlv1.XMLAbsoluteTempoChangeV1;
import org.alltiny.chorus.io.xmlv1.XMLAccidentalV1;
import org.alltiny.chorus.io.xmlv1.XMLAnchorV1;
import org.alltiny.chorus.io.xmlv1.XMLBarV1;
import org.alltiny.chorus.io.xmlv1.XMLBeamV1;
import org.alltiny.chorus.io.xmlv1.XMLBoundV1;
import org.alltiny.chorus.io.xmlv1.XMLDecorationV1;
import org.alltiny.chorus.io.xmlv1.XMLDurationElementV1;
import org.alltiny.chorus.io.xmlv1.XMLElementV1;
import org.alltiny.chorus.io.xmlv1.XMLFermataV1;
import org.alltiny.chorus.io.xmlv1.XMLInlineSequenceV1;
import org.alltiny.chorus.io.xmlv1.XMLMusicV1;
import org.alltiny.chorus.io.xmlv1.XMLNoteV1;
import org.alltiny.chorus.io.xmlv1.XMLRelativeTempoChangeV1;
import org.alltiny.chorus.io.xmlv1.XMLRepeatBeginV1;
import org.alltiny.chorus.io.xmlv1.XMLRepeatEndV1;
import org.alltiny.chorus.io.xmlv1.XMLRestV1;
import org.alltiny.chorus.io.xmlv1.XMLSequenceV1;
import org.alltiny.chorus.io.xmlv1.XMLSongV1;
import org.alltiny.chorus.io.xmlv1.XMLTripletV1;
import org.alltiny.chorus.io.xmlv1.XMLVoiceV1;
import org.alltiny.chorus.model.generic.DOMMap;

import java.util.stream.Collectors;

public class FromXMLV1Converter {

    public Song convert(XMLSongV1 song) {
        return new Song()
            .setTitle(song.getTitle())
            .setAuthor(song.getAuthor())
            .setMeta(new DOMMap<DOMMap<?,String>,String>(song.getMeta()))
            .setTempo(song.getTempo())
            .setMusic(convert(song.getMusic()));
    }

    public Music convert(XMLMusicV1 musicV1) {
        return new Music().setVoices(
            musicV1.getVoices().stream()
                .map(this::convert)
                .collect(Collectors.toList()));
    }

    public Voice convert(XMLVoiceV1 voiceV1) {
        return new Voice()
            .setHead(voiceV1.getHead())
            .setName(voiceV1.getName())
            .setSequence(convert(voiceV1.getSequence()))
            .setInlineSequences(
                voiceV1.getInlineSequences().stream()
                    .map(this::convert)
                    .collect(Collectors.toList()));
    }

    public Sequence convert(XMLSequenceV1 sequenceV1) {
        return new Sequence()
            .setClef(sequenceV1.getClef())
            .setKey(sequenceV1.getKey())
            .setElements(
                sequenceV1.getElements()
                    .map(this::convert)
                    .collect(Collectors.toList()));
    }

    public InlineSequence convert(XMLInlineSequenceV1 inlineSequenceV1) {
        return new InlineSequence()
            .setAnchorRef(inlineSequenceV1.getAnchorRef())
            .setElements(inlineSequenceV1.getElements()
                .map(this::convert)
                .collect(Collectors.toList()));
    }

    public Element<?> convert(XMLElementV1 elementV1) {
        if (elementV1 instanceof XMLNoteV1) {
            return convert((XMLNoteV1)elementV1);
        } else if (elementV1 instanceof XMLRestV1) {
            return convert((XMLRestV1)elementV1);
        } else if (elementV1 instanceof XMLBarV1) {
            return convert((XMLBarV1)elementV1);
        } else if (elementV1 instanceof XMLAnchorV1) {
            return convert((XMLAnchorV1)elementV1);
        } else if (elementV1 instanceof XMLAbsoluteTempoChangeV1) {
            return convert((XMLAbsoluteTempoChangeV1)elementV1);
        } else if (elementV1 instanceof XMLRelativeTempoChangeV1) {
            return convert((XMLRelativeTempoChangeV1)elementV1);
        } else if (elementV1 instanceof XMLRepeatBeginV1) {
            return convert((XMLRepeatBeginV1)elementV1);
        } else if (elementV1 instanceof XMLRepeatEndV1) {
            return convert((XMLRepeatEndV1)elementV1);
        } else {
            throw new IllegalArgumentException("unknown element: " + elementV1);
        }
    }

    public Note convert(XMLNoteV1 noteV1) {
        return new Note()
            .setDuration(noteV1.getDuration())
            .setDivision(noteV1.getDivision())
            .setNoteValue(noteV1.getNoteValue())
            .setAccidental(noteV1.getAccidental() != null ? noteV1.getAccidental().getSign() : null)
            .setLyric(noteV1.getLyric())
            .setDecorations(noteV1.getDecorations()
                .map(this::convert)
                .collect(Collectors.toList()));
    }

    public Rest convert(XMLRestV1 restV1) {
        return new Rest()
            .setDuration(restV1.getDuration())
            .setDivision(restV1.getDivision());
    }

    public Bar convert(XMLBarV1 barV1) {
        return new Bar()
            .setDuration(barV1.getDuration())
            .setDivision(barV1.getDivision());
    }

    public Anchor convert(XMLAnchorV1 anchorV1) {
        return new Anchor()
            .setRef(anchorV1.getRef());
    }

    public AbsoluteTempoChange convert(XMLAbsoluteTempoChangeV1 absoluteTempoChangeV1) {
        return new AbsoluteTempoChange()
            .setNote(convert(absoluteTempoChangeV1.getNote()))
            .setNumberPerMinute(absoluteTempoChangeV1.getNumberPerMinute());
    }

    public RelativeTempoChange convert(XMLRelativeTempoChangeV1 relativeTempoChangeV1) {
        return new RelativeTempoChange()
            .setLeft(convert(relativeTempoChangeV1.getLeft()))
            .setRight(convert(relativeTempoChangeV1.getRight()));
    }

    public DurationElement<?> convert(XMLDurationElementV1 durationElementV1) {
        return new DurationElement<>()
            .setDuration(durationElementV1.getDuration())
            .setDivision(durationElementV1.getDivision());
    }

    public RepeatBegin convert(XMLRepeatBeginV1 repeatBeginV1) {
        return new RepeatBegin().setRef(repeatBeginV1.getRef());
    }

    public RepeatEnd convert(XMLRepeatEndV1 repeatEndV1) {
        return new RepeatEnd().setRef(repeatEndV1.getRef());
    }

    public Decoration<?> convert(XMLDecorationV1 decorationV1) {
        if (decorationV1 instanceof XMLAccidentalV1) {
            return convert((XMLAccidentalV1)decorationV1);
        } else if (decorationV1 instanceof XMLBeamV1) {
            return convert((XMLBeamV1)decorationV1);
        } else if (decorationV1 instanceof XMLBoundV1) {
            return convert((XMLBoundV1)decorationV1);
        } else if (decorationV1 instanceof XMLTripletV1) {
            return convert((XMLTripletV1)decorationV1);
        } else if (decorationV1 instanceof XMLFermataV1) {
            return convert((XMLFermataV1)decorationV1);
        } else {
            throw new IllegalArgumentException("unknown element: " + decorationV1);
        }
    }

    public Accidental convert(XMLAccidentalV1 accidentalV1) {
        return new Accidental().setSign(accidentalV1.getSign());
    }

    public Beam convert(XMLBeamV1 beamV1) {
        return new Beam().setRef(beamV1.getRef());
    }

    public Bound convert(XMLBoundV1 boundV1) {
        return new Bound().setRef(boundV1.getRef());
    }

    public Triplet convert(XMLTripletV1 tripletV1) {
        return new Triplet().setRef(tripletV1.getRef());
    }

    public Fermata convert(XMLFermataV1 fermataV1) {
        return new Fermata();
    }
}
