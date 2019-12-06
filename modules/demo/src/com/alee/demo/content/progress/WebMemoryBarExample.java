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

package com.alee.demo.content.progress;

import com.alee.api.annotations.NotNull;
import com.alee.api.data.Orientation;
import com.alee.demo.api.example.*;
import com.alee.extended.memorybar.WebMemoryBar;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebMemoryBarExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "memorybar";
    }

    @NotNull
    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new BasicProgress ( "horizontal", StyleId.memorybar, Orientation.horizontal ),
                new BasicProgress ( "vertical", StyleId.memorybar, Orientation.vertical ),
                new BasicProgress ( "horizontal.undecorated", StyleId.memorybarUndecorated, Orientation.horizontal ),
                new BasicProgress ( "vertical.undecorated", StyleId.memorybarUndecorated, Orientation.vertical )
        );
    }

    /**
     * Memory bar preview.
     */
    protected class BasicProgress extends AbstractStylePreview
    {
        /**
         * Memory bar orientation.
         */
        private final Orientation orientation;

        /**
         * Constructs new style preview.
         *
         * @param id          preview identifier
         * @param styleId     preview {@link StyleId}
         * @param orientation memory bar orientation
         */
        public BasicProgress ( final String id, final StyleId styleId, final Orientation orientation )
        {
            super ( WebMemoryBarExample.this, id, FeatureState.updated, styleId );
            this.orientation = orientation;
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebMemoryBar allocatedBar = createMemoryBar ();
            allocatedBar.setDisplayMaximumMemory ( false );

            final WebMemoryBar maximumBar = createMemoryBar ();
            maximumBar.setDisplayMaximumMemory ( true );

            return CollectionUtils.asList ( allocatedBar, maximumBar );
        }

        /**
         * Returns new {@link WebMemoryBar} with preview settings.
         *
         * @return new {@link WebMemoryBar} with preview settings
         */
        @NotNull
        protected WebMemoryBar createMemoryBar ()
        {
            final WebMemoryBar memoryBar = new WebMemoryBar ( getStyleId () );
            memoryBar.setOrientation ( orientation );
            if ( orientation.isHorizontal () )
            {
                memoryBar.setPreferredWidth ( 150 );
            }
            else
            {
                memoryBar.setPreferredHeight ( 150 );
            }
            return memoryBar;
        }
    }
}