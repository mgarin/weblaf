/*
 * This file is part of WebLookAndFeel library.
 *
 * WebLookAndFeel library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WebLookAndFeel library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WebLookAndFeel library.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.alee.demo.api;

import com.alee.demo.skin.DemoStyles;
import com.alee.laf.panel.WebPanel;

import java.awt.*;

/**
 * @author Mikle Garin
 */

public final class PreviewPanel extends WebPanel
{
    /**
     * Preview feature state.
     */
    protected FeatureState state;

    /**
     * Constructs new preview panel with the specified style ID.
     *
     * @param state      feature state
     * @param layout     panel layout
     * @param components components to add into panel
     */
    public PreviewPanel ( final FeatureState state, final LayoutManager layout, final Component... components )
    {
        super ( DemoStyles.previewLightPanel, layout, components );
        this.state = state;
    }

    /**
     * Returns preview feature state.
     *
     * @return preview feature state
     */
    public FeatureState getState ()
    {
        return state;
    }
}