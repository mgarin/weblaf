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

package com.alee.managers.style.skin.web;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.scroll.ScrollPanePainter;
import com.alee.laf.scroll.WebScrollPaneUI;
import com.alee.utils.LafUtils;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Web-style painter for JScrollPane component.
 * It is used as WebScrollPaneUI default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */

public class WebScrollPanePainter<E extends JScrollPane, U extends WebScrollPaneUI> extends WebContainerPainter<E, U>
        implements ScrollPanePainter<E, U>
{
    /**
     * Listeners.
     */
    protected PropertyChangeListener propertyChangeListener;

    /**
     * Runtime variables.
     */
    protected WebScrollPaneCorner lowerTrailing;
    protected WebScrollPaneCorner lowerLeading;
    protected WebScrollPaneCorner upperTrailing;

    /**
     * {@inheritDoc}
     */
    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Scroll bars styling
        LafUtils.setHorizontalScrollBarStyleId ( component, "scrollpane-horizontal" );
        LafUtils.setVerticalScrollBarStyleId ( component, "scrollpane-vertical" );

        // Updating scrollpane corner
        updateCorners ();

        // Listenin to orientation change to update corners
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateCorners ();
            }
        };
        component.addPropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, propertyChangeListener );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Resetting styling
        LafUtils.setHorizontalScrollBarStyleId ( component, null );
        LafUtils.setVerticalScrollBarStyleId ( component, null );

        // Removing listener and custom corners
        component.removePropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, propertyChangeListener );
        component.remove ( getLowerLeadingCorner () );
        component.remove ( getLowerTrailingCorner () );
        component.remove ( getUpperTrailing () );

        super.uninstall ( c, ui );
    }

    /**
     * Updates scrollpane corner.
     */
    protected void updateCorners ()
    {
        component.setCorner ( JScrollPane.LOWER_LEADING_CORNER, getLowerLeadingCorner () );
        component.setCorner ( JScrollPane.LOWER_TRAILING_CORNER, getLowerTrailingCorner () );
        component.setCorner ( JScrollPane.UPPER_TRAILING_CORNER, getUpperTrailing () );
    }

    /**
     * Returns lower leading corner.
     *
     * @return lower leading corner
     */
    protected WebScrollPaneCorner getLowerLeadingCorner ()
    {
        if ( lowerLeading == null )
        {
            lowerLeading = new WebScrollPaneCorner ( JScrollPane.LOWER_LEADING_CORNER );
        }
        return lowerLeading;
    }

    /**
     * Returns lower trailing corner.
     *
     * @return lower trailing corner
     */
    private WebScrollPaneCorner getLowerTrailingCorner ()
    {
        if ( lowerTrailing == null )
        {
            lowerTrailing = new WebScrollPaneCorner ( JScrollPane.LOWER_TRAILING_CORNER );
        }
        return lowerTrailing;
    }

    /**
     * Returns upper traiing corner.
     *
     * @return upper traiing corner
     */
    protected WebScrollPaneCorner getUpperTrailing ()
    {
        if ( upperTrailing == null )
        {
            upperTrailing = new WebScrollPaneCorner ( JScrollPane.UPPER_TRAILING_CORNER );
        }
        return upperTrailing;
    }
}