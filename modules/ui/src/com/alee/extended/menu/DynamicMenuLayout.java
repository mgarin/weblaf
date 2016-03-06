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

package com.alee.extended.menu;

import com.alee.extended.layout.AbstractLayoutManager;
import com.alee.utils.GeometryUtils;
import com.alee.utils.MathUtils;
import com.alee.utils.SwingUtils;

import java.awt.*;

/**
 * Custom dynamic menu layout.
 * This layout takes care of each menu item position including positioning during animation.
 *
 * @author Mikle Garin
 */

public class DynamicMenuLayout extends AbstractLayoutManager
{
    @Override
    public void layoutContainer ( final Container parent )
    {
        final WebDynamicMenu menu = ( WebDynamicMenu ) parent;
        final float displayProgress = MathUtils.sqr ( menu.getDisplayProgress () );
        final DynamicMenuType type = !menu.isHiding () ? menu.getType () : menu.getHideType ();

        if ( type.isRoundMenu () )
        {
            layoutRoundMenu ( menu, displayProgress, type );
        }
        else
        {
            layoutPlainMenu ( menu, displayProgress, type );
        }
    }

    protected void layoutPlainMenu ( final WebDynamicMenu menu, final float displayProgress, final DynamicMenuType type )
    {
        final Dimension max = SwingUtils.max ( menu.getComponents () );
        final int itemsCount = menu.getComponentCount ();

        switch ( type )
        {
            case list:
            {
                final int x = 0;
                int y = 0;
                for ( int i = 0; i < itemsCount; i++ )
                {
                    placePlainElement ( menu, i, x, y );
                    y += max.height * displayProgress;
                }
            }
            break;
        }
    }

    /**
     * Places single plain menu item into its current position.
     *
     * @param menu processed menu
     * @param i    menu item index
     * @param x    menu item X coordinate
     * @param y    menu item Y coordinate
     */
    protected void placePlainElement ( final WebDynamicMenu menu, final int i, final int x, final int y )
    {
        final Component menuItem = menu.getComponent ( i );
        final Dimension ps = menuItem.getPreferredSize ();
        menuItem.setBounds ( x, y, ps.width, ps.height );
    }

    protected void layoutRoundMenu ( final WebDynamicMenu menu, final float displayProgress, final DynamicMenuType type )
    {
        final Dimension max = SwingUtils.max ( menu.getComponents () );
        final Point center = new Point ( menu.getRadius (), menu.getRadius () );
        final int itemSide = Math.max ( max.width, max.height );

        final int hidingCause = menu.getHidingCause ();
        final int hidingCauseIndex = hidingCause != -1 ? hidingCause : 0;
        final int radius = menu.getRadius () - itemSide / 2;
        final double singleComponentSpacing = getSingleComponentSpacing ( menu );
        final double startingAngle = getStartingAngle ( menu );

        final int cwSign = menu.isClockwise () ? 1 : -1;
        final int itemsCount = menu.getComponentCount ();

        switch ( type )
        {
            case roll:
            {
                final double oneAngle = displayProgress * singleComponentSpacing;
                for ( int i = 0; i < itemsCount; i++ )
                {
                    final int ai = hidingCauseIndex + i;
                    final int ci = ai < itemsCount ? ai : ai - itemsCount;
                    final double angle = startingAngle + hidingCauseIndex * singleComponentSpacing + oneAngle * i * cwSign;
                    final int x = ( int ) Math.round ( center.x + radius * Math.cos ( angle ) );
                    final int y = ( int ) Math.round ( center.y + radius * Math.sin ( angle ) );
                    placeRoundElement ( menu, ci, x, y );
                }
            }
            break;

            case star:
            {
                final int cradius = Math.round ( radius * displayProgress );
                for ( int i = 0; i < itemsCount; i++ )
                {
                    final double angle = startingAngle + singleComponentSpacing * i * cwSign;
                    final int r = i == hidingCause ? radius : cradius;
                    final int x = ( int ) Math.round ( center.x + r * Math.cos ( angle ) );
                    final int y = ( int ) Math.round ( center.y + r * Math.sin ( angle ) );
                    placeRoundElement ( menu, i, x, y );
                }
            }
            break;

            case shutter:
            {
                // todo Catch angle if was half-opened and continue hiding from there
                final int cradius = Math.round ( radius * displayProgress );
                final int modSign = menu.isDisplaying () ? -1 : 1;
                final double angleMod = GeometryUtils.toRadians ( modSign * 90 * ( 1f - displayProgress ) );
                for ( int i = 0; i < itemsCount; i++ )
                {
                    final double mod = i == hidingCause ? 0 : angleMod;
                    final double angle = startingAngle + ( singleComponentSpacing * i + mod ) * cwSign;
                    final int r = i == hidingCause ? radius : cradius;
                    final int x = ( int ) Math.round ( center.x + r * Math.cos ( angle ) );
                    final int y = ( int ) Math.round ( center.y + r * Math.sin ( angle ) );
                    placeRoundElement ( menu, i, x, y );
                }
            }
            break;

            case fade:
            {
                for ( int i = 0; i < itemsCount; i++ )
                {
                    final double angle = startingAngle + singleComponentSpacing * i * cwSign;
                    final int x = ( int ) Math.round ( center.x + radius * Math.cos ( angle ) );
                    final int y = ( int ) Math.round ( center.y + radius * Math.sin ( angle ) );
                    placeRoundElement ( menu, i, x, y );
                }
            }
            break;
        }
    }

    /**
     * Returns spacing angle between two menu components.
     *
     * @param menu processed menu
     * @return spacing angle between two menu components
     */
    public double getSingleComponentSpacing ( final WebDynamicMenu menu )
    {
        final double fullCircle = GeometryUtils.toRadians ( Math.min ( menu.getAngleRange (), 360 ) );
        return fullCircle < Math.PI * 2 ? fullCircle / ( menu.getComponentCount () - 1 ) : fullCircle / menu.getComponentCount ();
    }

    /**
     * Returns menu items starting angle relative to vertical axis.
     *
     * @param menu processed menu
     * @return menu items starting angle relative to vertical axis
     */
    public double getStartingAngle ( final WebDynamicMenu menu )
    {
        return GeometryUtils.toRadians ( menu.getStartingAngle () ) - Math.PI / 2;
    }

    /**
     * Returns menu item angle relative to vertical axis.
     *
     * @param menu  menu to process
     * @param index menu item index
     * @return menu item angle relative to vertical axis
     */
    public double getItemAngle ( final WebDynamicMenu menu, final int index )
    {
        final int cwSign = menu.isClockwise () ? 1 : -1;
        switch ( menu.getType () )
        {
            case roll:
            case star:
            case shutter:
            case fade:
            default:
            {
                final double startingAngle = getStartingAngle ( menu );
                final double singleComponentSpacing = getSingleComponentSpacing ( menu );
                return startingAngle + singleComponentSpacing * index * cwSign;
            }
        }
    }

    /**
     * Places single round menu item into its current position.
     *
     * @param menu processed menu
     * @param i    menu item index
     * @param x    menu item center X coordinate
     * @param y    menu item center Y coordinate
     */
    protected void placeRoundElement ( final WebDynamicMenu menu, final int i, final int x, final int y )
    {
        final Component menuItem = menu.getComponent ( i );
        final Dimension ps = menuItem.getPreferredSize ();
        menuItem.setBounds ( x - ps.width / 2, y - ps.height / 2, ps.width, ps.height );
    }

    @Override
    public Dimension preferredLayoutSize ( final Container parent )
    {
        final WebDynamicMenu menu = ( WebDynamicMenu ) parent;
        switch ( menu.getType () )
        {
            case roll:
            case star:
            case shutter:
            case fade:
            {
                final int radius = menu.getRadius ();
                return new Dimension ( radius * 2, radius * 2 );
            }
            case list:
            {
                final Dimension max = SwingUtils.max ( menu.getComponents () );
                final int itemsCount = menu.getComponentCount ();
                return new Dimension ( max.width, max.height * itemsCount );
            }
            default:
            {
                return new Dimension ( 0, 0 );
            }
        }
    }

    public Point getDisplayPoint ( final WebDynamicMenu menu, final int x, final int y )
    {
        switch ( menu.getType () )
        {
            case roll:
            case star:
            case shutter:
            case fade:
            {
                final Dimension size = preferredLayoutSize ( menu );
                return new Point ( x - size.width / 2, y - size.height / 2 );
            }
            case list:
            default:
            {
                return new Point ( x, y );
            }
        }
    }
}