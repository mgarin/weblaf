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

package com.alee.examples.content.presentation;

import com.alee.examples.content.Example;
import com.alee.managers.tooltip.TooltipAdapter;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.WebCustomTooltip;
import com.alee.utils.swing.DataProvider;

import java.awt.*;

/**
 * User: mgarin Date: 21.12.12 Time: 16:28
 */

public class OneTimeTooltipStep extends PresentationStep
{
    private DataProvider<Point> pointProvider;
    private TooltipAdapter forcefulDisposal;

    public OneTimeTooltipStep ( String shortName, int duration, final WebCustomTooltip tooltip, Example example )
    {
        this ( shortName, duration, tooltip, example, null );
    }

    public OneTimeTooltipStep ( String shortName, int duration, final WebCustomTooltip tooltip, Example example,
                                DataProvider<Point> pointProvider )
    {
        super ( shortName, duration );

        tooltip.setDefaultCloseBehavior ( false );
        setPointProvider ( pointProvider );

        setOnStart ( getOnStart ( tooltip, example ) );
        setOnEnd ( getOnEnd ( tooltip ) );
    }

    public DataProvider<Point> getPointProvider ()
    {
        return pointProvider;
    }

    public void setPointProvider ( DataProvider<Point> pointProvider )
    {
        this.pointProvider = pointProvider;
    }

    private Runnable getOnStart ( final WebCustomTooltip tooltip, final Example example )
    {
        return new Runnable ()
        {
            @Override
            public void run ()
            {
                // Updating display location
                if ( pointProvider != null )
                {
                    tooltip.setDisplayLocation ( pointProvider.provide () );
                }

                // Displaying tooltip
                TooltipManager.showOneTimeTooltip ( tooltip );

                // Listening for forceful tooltip disposal
                forcefulDisposal = new TooltipAdapter ()
                {
                    @Override
                    public void tooltipHidden ()
                    {
                        example.nextPresentationStep ();
                    }
                };
                tooltip.addTooltipListener ( forcefulDisposal );
            }
        };
    }

    private Runnable getOnEnd ( final WebCustomTooltip tooltip )
    {
        return new Runnable ()
        {
            @Override
            public void run ()
            {
                // No need to listen for force disposal anymore
                tooltip.removeTooltipListener ( forcefulDisposal );

                // Close tooltip when time ends
                tooltip.closeTooltip ();
            }
        };
    }
}
