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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
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
     * Last displayed {@link WebCustomTooltip}.
     */
    @Nullable
    private WebCustomTooltip tooltip;

    /**
     * {@link WebTimer} used to display delayed tooltip display.
     */
    @Nullable
    private WebTimer delayTimer;

    @Override
    public long getDelay ()
    {
        return TooltipManager.getDefaultDelay ();
    }

    @Nullable
    @Override
    public Rectangle getSourceBounds ( @NotNull final C component, @NotNull final A area )
    {
        final Rectangle bounds = area.getBounds ( component );
        return bounds != null ? bounds.intersection ( component.getVisibleRect () ) : null;
    }

    @Nullable
    @Override
    public WebCustomTooltip getToolTip ( @NotNull final C component, @NotNull final A area )
    {
        final WebCustomTooltip tooltip;
        final String text = getToolTipText ( component, area );
        if ( text != null )
        {
            final TooltipWay direction = getDirection ( component, area );
            tooltip = new WebCustomTooltip ( component, text, direction );
        }
        else
        {
            tooltip = null;
        }
        return tooltip;
    }

    @Override
    public void hoverAreaChanged ( @NotNull final C component, @Nullable final A oldArea, @Nullable final A newArea )
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
     * @param component {@link JComponent} to display tooltip for
     * @param area      {@link ComponentArea}
     */
    protected void showTooltip ( @NotNull final C component, @NotNull final A area )
    {
        if ( isAvailable ( component, area ) )
        {
            final Rectangle sourceBounds = getSourceBounds ( component, area );
            if ( sourceBounds != null )
            {
                final WebCustomTooltip toolTip = getToolTip ( component, area );
                if ( toolTip != null )
                {
                    tooltip = toolTip;
                    tooltip.setRelativeToBounds ( sourceBounds );
                    TooltipManager.showOneTimeTooltip ( tooltip );
                }
            }
        }
    }

    /**
     * Returns whether or not this {@link ComponentArea} is still available.
     *
     * @param component {@link JComponent}
     * @param area      {@link ComponentArea}
     * @return {@code true} if this {@link ComponentArea} is still available, {@code false} otherwise
     */
    protected boolean isAvailable ( @NotNull final C component, @NotNull final A area )
    {
        return area.isAvailable ( component );
    }

    /**
     * Returns value for the specified {@link ComponentArea}.
     *
     * @param component {@link JComponent} to retrieve value from
     * @param area      {@link ComponentArea}
     * @return value for the specified {@link ComponentArea}
     */
    @Nullable
    protected V getValue ( @NotNull final C component, @NotNull final A area )
    {
        return area.getValue ( component );
    }

    /**
     * Returns tooltip direction based on value and {@link ComponentArea}.
     *
     * @param component {@link JComponent} to provide tooltip direction for
     * @param area      {@link ComponentArea}
     * @return tooltip direction based on value and {@link ComponentArea}
     */
    @NotNull
    protected TooltipWay getDirection ( @NotNull final C component, @NotNull final A area )
    {
        return TooltipWay.trailing;
    }

    /**
     * Returns tooltip text for the specified value and {@link ComponentArea}.
     *
     * @param component {@link JComponent} to provide tooltip for
     * @param area      {@link ComponentArea}
     * @return tooltip text for the specified value and {@link ComponentArea}
     */
    @Nullable
    protected abstract String getToolTipText ( @NotNull C component, @NotNull A area );
}