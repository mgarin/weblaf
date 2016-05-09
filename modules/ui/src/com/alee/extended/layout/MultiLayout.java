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

package com.alee.extended.layout;

import com.alee.utils.SwingUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom layout that allows multiply layout strategies to be applied to single container.
 * Be aware that standard Swing layouts aren't developed to be used together so they aren't suitable for this layout.
 * You will need custom layouts which work only with specific components and won't affect the same components.
 *
 * @author Mikle Garin
 */

public class MultiLayout extends AbstractLayoutManager
{
    /**
     * Applied layout managers list.
     */
    protected List<LayoutManager> layoutManagers;

    /**
     * Constructs new MultiLayout.
     */
    public MultiLayout ()
    {
        super ();
        layoutManagers = new ArrayList<LayoutManager> ( 2 );
    }

    /**
     * Returns applied layout managers.
     *
     * @return applied layout managers
     */
    public List<LayoutManager> getLayoutManagers ()
    {
        return layoutManagers;
    }

    /**
     * Sets applied layout managers.
     *
     * @param layoutManagers new applied layout managers
     */
    public void setLayoutManagers ( final List<LayoutManager> layoutManagers )
    {
        this.layoutManagers = layoutManagers;
    }

    /**
     * Adds applied layout manager.
     *
     * @param layoutManager applied layout manager to add
     */
    public void addLayoutManager ( final LayoutManager layoutManager )
    {
        this.layoutManagers.add ( layoutManager );
    }

    /**
     * Removes applied layout manager.
     *
     * @param layoutManager applied layout manager to remove
     */
    public void removeLayoutManager ( final LayoutManager layoutManager )
    {
        this.layoutManagers.remove ( layoutManager );
    }

    @Override
    public void addLayoutComponent ( final Component comp, final Object constraints )
    {
        for ( final LayoutManager layoutManager : layoutManagers )
        {
            if ( layoutManager instanceof LayoutManager2 )
            {
                ( ( LayoutManager2 ) layoutManager ).addLayoutComponent ( comp, constraints );
            }
        }
    }

    @Override
    public void addLayoutComponent ( final String name, final Component comp )
    {
        for ( final LayoutManager layoutManager : layoutManagers )
        {
            layoutManager.addLayoutComponent ( name, comp );
        }
    }

    @Override
    public void removeLayoutComponent ( final Component comp )
    {
        for ( final LayoutManager layoutManager : layoutManagers )
        {
            layoutManager.removeLayoutComponent ( comp );
        }
    }

    @Override
    public void addComponent ( final Component component, final Object constraints )
    {
        for ( final LayoutManager layoutManager : layoutManagers )
        {
            if ( layoutManager instanceof LayoutManager2 )
            {
                ( ( LayoutManager2 ) layoutManager ).addLayoutComponent ( component, constraints );
            }
            else
            {
                layoutManager.addLayoutComponent ( constraints == null ? null : constraints.toString (), component );
            }
        }
    }

    @Override
    public void removeComponent ( final Component component )
    {
        for ( final LayoutManager layoutManager : layoutManagers )
        {
            layoutManager.removeLayoutComponent ( component );
        }
    }

    @Override
    public Dimension preferredLayoutSize ( final Container parent )
    {
        Dimension ps = new Dimension ( 0, 0 );
        for ( final LayoutManager layoutManager : layoutManagers )
        {
            ps = SwingUtils.max ( ps, layoutManager.preferredLayoutSize ( parent ) );
        }
        return ps;
    }

    @Override
    public Dimension minimumLayoutSize ( final Container parent )
    {
        Dimension ms = new Dimension ( 0, 0 );
        for ( final LayoutManager layoutManager : layoutManagers )
        {
            ms = SwingUtils.max ( ms, layoutManager.minimumLayoutSize ( parent ) );
        }
        return ms;
    }

    @Override
    public Dimension maximumLayoutSize ( final Container parent )
    {
        Dimension ms = new Dimension ( Integer.MAX_VALUE, Integer.MAX_VALUE );
        for ( final LayoutManager layoutManager : layoutManagers )
        {
            if ( layoutManager instanceof LayoutManager2 )
            {
                ms = SwingUtils.min ( ms, ( ( LayoutManager2 ) layoutManager ).maximumLayoutSize ( parent ) );
            }
        }
        return ms;
    }

    @Override
    public void invalidateLayout ( final Container parent )
    {
        for ( final LayoutManager layoutManager : layoutManagers )
        {
            if ( layoutManager instanceof LayoutManager2 )
            {
                ( ( LayoutManager2 ) layoutManager ).invalidateLayout ( parent );
            }
        }
    }

    @Override
    public void layoutContainer ( final Container parent )
    {
        for ( final LayoutManager layoutManager : layoutManagers )
        {
            layoutManager.layoutContainer ( parent );
        }
    }
}