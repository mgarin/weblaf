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

package com.alee.demo.api.example;

import com.alee.api.annotations.NotNull;
import com.alee.demo.skin.DemoStyles;
import com.alee.laf.panel.WebPanel;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.Stateful;
import com.alee.utils.CollectionUtils;

import java.awt.*;
import java.util.List;

/**
 * Custom panel for a single preview.
 *
 * @author Mikle Garin
 */
public final class PreviewPanel extends WebPanel implements Stateful
{
    /**
     * Preview {@link FeatureState}.
     */
    private final FeatureState state;

    /**
     * Constructs new {@link PreviewPanel} with the specified settings.
     *
     * @param state      {@link FeatureState}
     * @param layout     panel {@link LayoutManager}
     * @param components {@link Component}s to add into panel
     */
    public PreviewPanel ( @NotNull final FeatureState state, final LayoutManager layout, final Component... components )
    {
        super ( DemoStyles.previewPanel, layout, components );
        this.state = state;
        DecorationUtils.fireStatesChanged ( this );
    }

    /**
     * Returns preview {@link FeatureState}.
     *
     * @return preview {@link FeatureState}
     */
    @NotNull
    public FeatureState getState ()
    {
        return state;
    }

    @NotNull
    @Override
    public List<String> getStates ()
    {
        return CollectionUtils.asList ( state != null ? state.name () : FeatureState.common.name () );
    }
}