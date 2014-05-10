package org.alltiny.chorus.gui.layout;

import org.alltiny.chorus.render.Visual;

/**
 * This class manages a row in the grid.
 */
/* package-local */ class GridRow extends GridColumnRow {

    @Override
    protected void updateAll() {
        for (Visual visual : getAllVisuals()) {
            visual.setAbsMedianY(getMedianOffset());
            visual.setExtendTop(getLeadingExtend());
            visual.setExtendBottom(getTrailingExtend());
        }
    }
}
