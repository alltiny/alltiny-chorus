package org.alltiny.chorus.base.type;

import static org.alltiny.chorus.base.type.AccidentalSign.FLAT;
import static org.alltiny.chorus.base.type.AccidentalSign.NONE;
import static org.alltiny.chorus.base.type.AccidentalSign.SHARP;

/**
 * This class defines the existing keys (Tonarten).
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 19.12.2007 22:47:12
 */
public enum Key {
    C  (NONE,NONE,NONE,NONE,NONE,NONE,NONE), // C-Dur, C-Major
    a  (NONE,NONE,NONE,NONE,NONE,NONE,NONE), // a-Moll, a-Minor
    F  (NONE,NONE,NONE,NONE,NONE,NONE,FLAT), // F-Dur, F-Major
    d  (NONE,NONE,NONE,NONE,NONE,NONE,FLAT), // d-Moll, d-Minor
    B  (NONE,NONE,FLAT,NONE,NONE,NONE,FLAT), // B-Dur, B-Major
    g  (NONE,NONE,FLAT,NONE,NONE,NONE,FLAT), // g-Moll, g-Minor
    Es (NONE,NONE,FLAT,NONE,NONE,FLAT,FLAT), // Es-Dur, Es-Major
    c  (NONE,NONE,FLAT,NONE,NONE,FLAT,FLAT), // c-Moll, c-Minor
    As (NONE,FLAT,FLAT,NONE,NONE,FLAT,FLAT), // As-Dur, As-Major
    f  (NONE,FLAT,FLAT,NONE,NONE,FLAT,FLAT), // f-Moll, f-Minor
    Des(NONE,FLAT,FLAT,NONE,FLAT,FLAT,FLAT), // Des-Dur, Des-Major
    b  (NONE,FLAT,FLAT,NONE,FLAT,FLAT,FLAT), // b-Moll, b-Minor
    Ges(FLAT,FLAT,FLAT,NONE,FLAT,FLAT,FLAT), // Ges-Dur, Ges-Major
    es (FLAT,FLAT,FLAT,NONE,FLAT,FLAT,FLAT), // es-Moll, es-Minor

    G  (NONE,NONE,NONE,SHARP,NONE,NONE,NONE), // F-Dur, F-Major
    e  (NONE,NONE,NONE,SHARP,NONE,NONE,NONE), // d-Moll, d-Minor
    D  (SHARP,NONE,NONE,SHARP,NONE,NONE,NONE), // B-Dur, B-Major
    h  (SHARP,NONE,NONE,SHARP,NONE,NONE,NONE), // g-Moll, g-Minor
    A  (SHARP,NONE,NONE,SHARP,SHARP,NONE,NONE), // Es-Dur, Es-Major
    fis(SHARP,NONE,NONE,SHARP,SHARP,NONE,NONE), // c-Moll, c-Minor
    E  (SHARP,SHARP,NONE,SHARP,SHARP,NONE,NONE), // As-Dur, As-Major
    cis(SHARP,SHARP,NONE,SHARP,SHARP,NONE,NONE), // f-Moll, f-Minor
    H  (SHARP,SHARP,NONE,SHARP,SHARP,SHARP,NONE), // Des-Dur, Des-Major
    gis(SHARP,SHARP,NONE,SHARP,SHARP,SHARP,NONE), // b-Moll, b-Minor
    Fis(SHARP,SHARP,SHARP,SHARP,SHARP,SHARP,NONE), // Ges-Dur, Ges-Major
    dis(SHARP,SHARP,SHARP,SHARP,SHARP,SHARP,NONE); // es-Moll, es-Minor

    private final AccidentalSign[] mods = new AccidentalSign[BaseNote.values().length];

    Key(AccidentalSign cModifier,
        AccidentalSign dModifier,
        AccidentalSign eModifier,
        AccidentalSign fModifier,
        AccidentalSign gModifier,
        AccidentalSign aModifier,
        AccidentalSign bModifier) {
        mods[BaseNote.C.ordinal()] = cModifier;
        mods[BaseNote.D.ordinal()] = dModifier;
        mods[BaseNote.E.ordinal()] = eModifier;
        mods[BaseNote.F.ordinal()] = fModifier;
        mods[BaseNote.G.ordinal()] = gModifier;
        mods[BaseNote.A.ordinal()] = aModifier;
        mods[BaseNote.B.ordinal()] = bModifier;
    }

    public AccidentalSign getCMod() { return mods[BaseNote.C.ordinal()]; }
    public AccidentalSign getDMod() { return mods[BaseNote.D.ordinal()]; }
    public AccidentalSign getEMod() { return mods[BaseNote.E.ordinal()]; }
    public AccidentalSign getFMod() { return mods[BaseNote.F.ordinal()]; }
    public AccidentalSign getGMod() { return mods[BaseNote.G.ordinal()]; }
    public AccidentalSign getAMod() { return mods[BaseNote.A.ordinal()]; }
    public AccidentalSign getBMod() { return mods[BaseNote.B.ordinal()]; }

    public AccidentalSign getMod(BaseNote note) {
        return mods[note.ordinal()];
    }
}
