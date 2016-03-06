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

package com.alee.managers.language;

import com.alee.managers.language.data.Tooltip;
import com.alee.managers.language.data.TooltipType;
import com.alee.managers.language.data.Value;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.WebCustomTooltip;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Swing and WebLaF tooltips language support.
 *
 * @author Mikle Garin
 */

public class WeblafTooltipLanguageSupport implements TooltipLanguageSupport
{
    /**
     * Components custom WebLaF tooltips cache.
     * Used for proper tooltips disposal and update.
     */
    protected final Map<Component, List<WebCustomTooltip>> tooltipsCache = new WeakHashMap<Component, List<WebCustomTooltip>> ();

    @Override
    public void setupTooltip ( final Component component, final Value value )
    {
        // Removing old cached tooltips
        final boolean swingComponent = component instanceof JComponent;
        if ( tooltipsCache.containsKey ( component ) )
        {
            // Clearing Swing tooltip
            if ( swingComponent )
            {
                ( ( JComponent ) component ).setToolTipText ( null );
            }

            // Clearing WebLaF tooltips
            TooltipManager.removeTooltips ( component, tooltipsCache.get ( component ) );
            tooltipsCache.get ( component ).clear ();
        }
        // Adding new tooltips
        if ( value != null && value.getTooltips () != null && value.getTooltips ().size () > 0 )
        {
            for ( final Tooltip tooltip : value.getTooltips () )
            {
                if ( tooltip.getType ().equals ( TooltipType.swing ) )
                {
                    if ( swingComponent )
                    {
                        ( ( JComponent ) component ).setToolTipText ( tooltip.getText () );
                    }
                }
                else
                {
                    if ( tooltip.getDelay () != null )
                    {
                        cacheTip ( TooltipManager.setTooltip ( component, tooltip.getText (), tooltip.getWay (), tooltip.getDelay () ) );
                    }
                    else
                    {
                        cacheTip ( TooltipManager.setTooltip ( component, tooltip.getText () ) );
                    }
                }
            }
        }
    }

    /**
     * Caches created custom tooltip.
     *
     * @param tooltip tooltip to cache
     */
    protected void cacheTip ( final WebCustomTooltip tooltip )
    {
        final Component component = tooltip.getComponent ();

        // Creating array if it is needed
        if ( !tooltipsCache.containsKey ( component ) )
        {
            tooltipsCache.put ( component, new ArrayList<WebCustomTooltip> () );
        }

        // Updating cache
        tooltipsCache.get ( component ).add ( tooltip );
    }
}