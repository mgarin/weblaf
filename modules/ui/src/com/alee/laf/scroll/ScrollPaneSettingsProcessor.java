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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * {@link SettingsProcessor} implementation that handles {@link JScrollPane} settings.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see SettingsProcessor
 */
public class ScrollPaneSettingsProcessor extends SettingsProcessor<JScrollPane, ScrollPaneState, Configuration<ScrollPaneState>>
{
    /**
     * {@link AdjustmentListener} for tracking {@link JScrollPane} value changes.
     */
    protected transient AdjustmentListener adjustmentListener;

    /**
     * {@link PropertyChangeListener} for tracking {@link JScrollBar}s changes.
     */
    protected transient PropertyChangeListener propertyChangeListener;

    /**
     * Constructs new {@link ScrollPaneSettingsProcessor}.
     *
     * @param scrollPane    {@link JScrollPane} which settings are being managed
     * @param configuration {@link Configuration}
     */
    public ScrollPaneSettingsProcessor ( final JScrollPane scrollPane, final Configuration configuration )
    {
        super ( scrollPane, configuration );
    }

    @Override
    protected void register ( final JScrollPane scrollPane )
    {
        adjustmentListener = new AdjustmentListener ()
        {
            @Override
            public void adjustmentValueChanged ( final AdjustmentEvent e )
            {
                save ();
            }
        };
        scrollPane.getHorizontalScrollBar ().addAdjustmentListener ( adjustmentListener );
        scrollPane.getVerticalScrollBar ().addAdjustmentListener ( adjustmentListener );

        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                final JScrollBar oldScrollBar = ( JScrollBar ) evt.getOldValue ();
                if ( oldScrollBar != null )
                {
                    oldScrollBar.removeAdjustmentListener ( adjustmentListener );
                }
                final JScrollBar newScrollBar = ( JScrollBar ) evt.getNewValue ();
                if ( newScrollBar != null )
                {
                    newScrollBar.addAdjustmentListener ( adjustmentListener );
                }
            }
        };
        scrollPane.addPropertyChangeListener ( WebScrollBar.HORIZONTAL_SCROLL_BAR_PROPERTY, propertyChangeListener );
        scrollPane.addPropertyChangeListener ( WebScrollBar.VERTICAL_SCROLL_BAR_PROPERTY, propertyChangeListener );
    }

    @Override
    protected void unregister ( final JScrollPane scrollPane )
    {
        scrollPane.removePropertyChangeListener ( WebScrollBar.VERTICAL_SCROLL_BAR_PROPERTY, propertyChangeListener );
        scrollPane.removePropertyChangeListener ( WebScrollBar.HORIZONTAL_SCROLL_BAR_PROPERTY, propertyChangeListener );
        propertyChangeListener = null;

        scrollPane.getVerticalScrollBar ().removeAdjustmentListener ( adjustmentListener );
        scrollPane.getHorizontalScrollBar ().removeAdjustmentListener ( adjustmentListener );
        adjustmentListener = null;
    }

    @Override
    protected ScrollPaneState createDefaultValue ()
    {
        return new ScrollPaneState ( component () );
    }

    @Override
    protected void loadSettings ( final JScrollPane scrollPane )
    {
        loadSettings ().apply ( scrollPane );
    }

    @Override
    protected void saveSettings ( final JScrollPane scrollPane )
    {
        saveSettings ( new ScrollPaneState ( scrollPane ) );
    }
}