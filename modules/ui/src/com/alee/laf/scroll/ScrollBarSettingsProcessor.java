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

package com.alee.laf.scroll;

import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsProcessor;

import javax.swing.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 * {@link SettingsProcessor} implementation that handles {@link JScrollBar} settings.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see SettingsProcessor
 */
public class ScrollBarSettingsProcessor extends SettingsProcessor<JScrollBar, ScrollBarState, Configuration<ScrollBarState>>
{
    /**
     * {@link AdjustmentListener} for tracking {@link JScrollBar} value changes.
     */
    protected transient AdjustmentListener adjustmentListener;

    /**
     * Constructs new {@link ScrollBarSettingsProcessor}.
     *
     * @param scrollBar     {@link JScrollBar} which settings are being managed
     * @param configuration {@link Configuration}
     */
    public ScrollBarSettingsProcessor ( final JScrollBar scrollBar, final Configuration configuration )
    {
        super ( scrollBar, configuration );
    }

    @Override
    protected void register ( final JScrollBar scrollBar )
    {
        adjustmentListener = new AdjustmentListener ()
        {
            @Override
            public void adjustmentValueChanged ( final AdjustmentEvent e )
            {
                save ();
            }
        };
        scrollBar.addAdjustmentListener ( adjustmentListener );
    }

    @Override
    protected void unregister ( final JScrollBar scrollBar )
    {
        scrollBar.removeAdjustmentListener ( adjustmentListener );
        adjustmentListener = null;
    }

    @Override
    protected ScrollBarState createDefaultValue ()
    {
        return new ScrollBarState ( component () );
    }

    @Override
    protected void loadSettings ( final JScrollBar scrollBar )
    {
        loadSettings ().apply ( scrollBar );
    }

    @Override
    protected void saveSettings ( final JScrollBar scrollBar )
    {
        saveSettings ( new ScrollBarState ( scrollBar ) );
    }
}