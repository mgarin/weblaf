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

package com.alee.laf.tabbedpane;

import com.alee.api.annotations.NotNull;
import com.alee.managers.tooltip.AbstractToolTipProvider;
import com.alee.managers.tooltip.TooltipWay;

import javax.swing.*;

/**
 * Abstract WebLaF tooltip provider for {@link JTabbedPane} component.
 *
 * @param <V> tab value type
 * @author Mikle Garin
 */
public abstract class TabbedPaneToolTipProvider<V> extends AbstractToolTipProvider<V, JTabbedPane, TabbedPaneTabArea<V, JTabbedPane>>
{
    @NotNull
    @Override
    public TooltipWay getDirection ( @NotNull final JTabbedPane component, @NotNull final TabbedPaneTabArea<V, JTabbedPane> area )
    {
        final TooltipWay direction;
        switch ( component.getTabPlacement () )
        {
            default:
            case JTabbedPane.TOP:
                direction = TooltipWay.down;
                break;

            case JTabbedPane.BOTTOM:
                direction = TooltipWay.up;
                break;

            case JTabbedPane.LEFT:
                direction = TooltipWay.trailing;
                break;

            case JTabbedPane.RIGHT:
                direction = TooltipWay.leading;
                break;
        }
        return direction;
    }
}