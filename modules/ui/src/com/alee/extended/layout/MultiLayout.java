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
    public void setLayoutManagers ( List<LayoutManager> layoutManagers )
    {
        this.layoutManagers = layoutManagers;
    }

    /**
     * Adds applied layout manager.
     *
     * @param layoutManager applied layout manager to add
     */
    public void addLayoutManager ( LayoutManager layoutManager )
    {
        this.layoutManagers.add ( layoutManager );
    }

    /**
     * Removes applied layout manager.
     *
     * @param layoutManager applied layout manager to remove
     */
    public void removeLayoutManager ( LayoutManager layoutManager )
    {
        this.layoutManagers.remove ( layoutManager );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addLayoutComponent ( Component comp, Object constraints )
    {
        for ( LayoutManager layoutManager : layoutManagers )
        {
            if ( layoutManager instanceof LayoutManager2 )
            {
                ( ( LayoutManager2 ) layoutManager ).addLayoutComponent ( comp, constraints );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addLayoutComponent ( String name, Component comp )
    {
        for ( LayoutManager layoutManager : layoutManagers )
        {
            layoutManager.addLayoutComponent ( name, comp );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLayoutComponent ( Component comp )
    {
        for ( LayoutManager layoutManager : layoutManagers )
        {
            layoutManager.removeLayoutComponent ( comp );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addComponent ( Component component, Object constraints )
    {
        for ( LayoutManager layoutManager : layoutManagers )
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeComponent ( Component component )
    {
        for ( LayoutManager layoutManager : layoutManagers )
        {
            layoutManager.removeLayoutComponent ( component );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension preferredLayoutSize ( Container parent )
    {
        Dimension ps = new Dimension ( 0, 0 );
        for ( LayoutManager layoutManager : layoutManagers )
        {
            ps = SwingUtils.max ( ps, layoutManager.preferredLayoutSize ( parent ) );
        }
        return ps;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension minimumLayoutSize ( Container parent )
    {
        Dimension ms = new Dimension ( 0, 0 );
        for ( LayoutManager layoutManager : layoutManagers )
        {
            ms = SwingUtils.max ( ms, layoutManager.minimumLayoutSize ( parent ) );
        }
        return ms;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension maximumLayoutSize ( Container parent )
    {
        Dimension ms = new Dimension ( Integer.MAX_VALUE, Integer.MAX_VALUE );
        for ( LayoutManager layoutManager : layoutManagers )
        {
            if ( layoutManager instanceof LayoutManager2 )
            {
                ms = SwingUtils.min ( ms, ( ( LayoutManager2 ) layoutManager ).maximumLayoutSize ( parent ) );
            }
        }
        return ms;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void invalidateLayout ( Container parent )
    {
        for ( LayoutManager layoutManager : layoutManagers )
        {
            if ( layoutManager instanceof LayoutManager2 )
            {
                ( ( LayoutManager2 ) layoutManager ).invalidateLayout ( parent );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutContainer ( Container parent )
    {
        for ( LayoutManager layoutManager : layoutManagers )
        {
            layoutManager.layoutContainer ( parent );
        }
    }
}