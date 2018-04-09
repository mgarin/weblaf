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

package com.alee.managers.tooltip;

import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Abstract WebLaF tooltip provider which defines base methods used across all components.
 *
 * @param <V> value type
 * @param <C> component type
 * @param <A> component area type
 * @author Mikle Garin
 */
public abstract class AbstractToolTipProvider<V, C extends JComponent, A extends ComponentArea<V, C>> implements ToolTipProvider<V, C, A>
{
    /**
     * Last displayed tooltip.
     */
    private WebCustomTooltip tooltip;

    /**
     * Delayed tooltip display timer.
     */
    private WebTimer delayTimer;

    @Override
    public long getDelay ()
    {
        return TooltipManager.getDefaultDelay ();
    }

    @Override
    public Rectangle getSourceBounds ( final C component, final V value, final A area )
    {
        final Rectangle bounds = area.getBounds ( component );
        return bounds.intersection ( component.getVisibleRect () );
    }

    @Override
    public WebCustomTooltip getToolTip ( final C component, final V value, final A area )
    {
        final TooltipWay direction = getDirection ( component, value, area );
        final String text = getToolTipText ( component, value, area );
        return new WebCustomTooltip ( component, text, direction );
    }

    @Override
    public void hoverAreaChanged ( final C component, final A oldArea, final A newArea )
    {
        // Close previously displayed tooltip
        if ( delayTimer != null )
        {
            delayTimer.stop ();
        }
        if ( tooltip != null )
        {
            tooltip.closeTooltip ();
        }

        // Display or delay new tooltip if needed
        if ( newArea != null )
        {
            final long delay = getDelay ();
            if ( delay <= 0 )
            {
                showTooltip ( component, newArea );
            }
            else
            {
                delayTimer = WebTimer.delay ( delay, new ActionListener ()
                {
                    @Override
                    public void actionPerformed ( final ActionEvent e )
                    {
                        showTooltip ( component, newArea );
                    }
                } );
            }
        }
    }

    /**
     * Displays custom tooltip for the specified {@link ComponentArea}.
     *
     * @param component component to display tooltip for
     * @param area      {@link ComponentArea}
     */
    protected void showTooltip ( final C component, final A area )
    {
        // Retrieving value for specified component area
        final V value = getValue ( component, area );

        // Retrieving tooltip component
        tooltip = getToolTip ( component, value, area );

        // Updating tooltip bounds
        tooltip.setRelativeToBounds ( getSourceBounds ( component, value, area ) );

        // Displaying one-time tooltip
        TooltipManager.showOneTimeTooltip ( tooltip );
    }

    /**
     * Returns value for the specified {@link ComponentArea}.
     *
     * @param component component to retrieve value from
     * @param area      {@link ComponentArea}
     * @return value for the specified {@link ComponentArea}
     */
    protected V getValue ( final C component, final A area )
    {
        return area.getValue ( component );
    }

    /**
     * Returns tooltip direction based on value and {@link ComponentArea}.
     *
     * @param component component to provide tooltip direction for
     * @param value     value
     * @param area      {@link ComponentArea}
     * @return tooltip direction based on value and {@link ComponentArea}
     */
    protected TooltipWay getDirection ( final C component, final V value, final A area )
    {
        return TooltipWay.trailing;
    }

    /**
     * Returns tooltip text for the specified value and {@link ComponentArea}.
     *
     * @param component component to provide tooltip for
     * @param value     value
     * @param area      {@link ComponentArea}
     * @return tooltip text for the specified value and {@link ComponentArea}
     */
    protected abstract String getToolTipText ( C component, V value, A area );
}