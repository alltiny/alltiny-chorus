package org.alltiny.chorus.gui.layout;

import org.alltiny.chorus.render.Visual;

/**
 * This class manages a row in the grid.
 */
/* package-local */ class GridColumn extends GridColumnRow {

    @Override
    protected void updateAll() {
        for (Visual visual : getAllVisuals()) {
            visual.setAbsMedianX(getMedianOffset());
            visual.setExtendLeft(getLeadingExtend());
            visual.setExtendRight(getTrailingExtend());
        }
    }
}