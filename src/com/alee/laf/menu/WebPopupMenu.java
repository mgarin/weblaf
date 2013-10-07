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

package com.alee.laf.menu;

import com.alee.extended.painter.Painter;
import com.alee.laf.WebLookAndFeel;
import com.alee.utils.ReflectUtils;
import com.alee.utils.laf.ShapeProvider;

import javax.swing.*;
import java.awt.*;

/**
 * This JPopupMenu extension class provides a direct access to WebPopupMenuUI methods.
 * It also has a few additional methods to simplify popup window positioning.
 *
 * @author Mikle Garin
 */

public class WebPopupMenu extends JPopupMenu implements ShapeProvider
{
    /**
     * Constructs new popup menu.
     */
    public WebPopupMenu ()
    {
        super ();
    }

    /**
     * Constructs new popup menu with the specified label.
     *
     * @param label popup menu label
     */
    public WebPopupMenu ( final String label )
    {
        super ( label );
    }

    /**
     * Adds separator into menu.
     */
    @Override
    public void addSeparator ()
    {
        add ( new WebPopupMenu.Separator () );
    }

    /**
     * Adds separator into menu at the specified Z-index.
     *
     * @param index separator Z-index
     */
    public void addSeparator ( int index )
    {
        add ( new WebPopupMenu.Separator (), index );
    }

    /**
     * Displays popup menu above the invoker component starting at its leading side.
     * This method takes into account component orientation.
     *
     * @param invoker invoker component
     */
    public void showAbove ( final Component invoker )
    {
        showAboveStart ( invoker );
    }

    /**
     * Displays popup menu above the invoker component starting at its leading side.
     * This method takes into account component orientation.
     *
     * @param invoker invoker component
     */
    public void showAboveStart ( Component invoker )
    {
        setPopupMenuWay ( PopupMenuWay.aboveStart );
        show ( invoker, 0, 0 );
    }

    /**
     * Displays popup menu above the invoker component at its middle.
     *
     * @param invoker invoker component
     */
    public void showAboveMiddle ( Component invoker )
    {
        setPopupMenuWay ( PopupMenuWay.aboveMiddle );
        show ( invoker, 0, 0 );
    }

    /**
     * Displays popup menu above the invoker component starting at its trailing side.
     * This method takes into account component orientation.
     *
     * @param invoker invoker component
     */
    public void showAboveEnd ( Component invoker )
    {
        setPopupMenuWay ( PopupMenuWay.aboveEnd );
        show ( invoker, 0, 0 );
    }

    /**
     * Displays popup menu under the invoker component starting at its leading side.
     * This method also takes into account component orientation.
     *
     * @param invoker invoker component
     */
    public void showBelow ( final Component invoker )
    {
        showBelowStart ( invoker );
    }

    /**
     * Displays popup menu under the invoker component starting at its leading side.
     * This method takes into account component orientation.
     *
     * @param invoker invoker component
     */
    public void showBelowStart ( final Component invoker )
    {
        setPopupMenuWay ( PopupMenuWay.belowStart );
        show ( invoker, 0, 0 );
    }

    /**
     * Displays popup menu under the invoker component at its middle.
     *
     * @param invoker invoker component
     */
    public void showBelowMiddle ( final Component invoker )
    {
        setPopupMenuWay ( PopupMenuWay.belowMiddle );
        show ( invoker, 0, 0 );
    }

    /**
     * Displays popup menu under the invoker component starting at its trailing side.
     * This method takes into account component orientation.
     *
     * @param invoker invoker component
     */
    public void showBelowEnd ( final Component invoker )
    {
        setPopupMenuWay ( PopupMenuWay.belowEnd );
        show ( invoker, 0, 0 );
    }

    /**
     * Returns popup menu style.
     *
     * @return popup menu style
     */
    public PopupMenuStyle getPopupMenuStyle ()
    {
        return getWebUI ().getPopupMenuStyle ();
    }

    /**
     * Sets popup menu style.
     *
     * @param style new popup menu style
     */
    public void setPopupMenuStyle ( final PopupMenuStyle style )
    {
        getWebUI ().setPopupMenuStyle ( style );
    }

    /**
     * Returns popup menu border color.
     *
     * @return popup menu border color
     */
    public Color getBorderColor ()
    {
        return getWebUI ().getBorderColor ();
    }

    /**
     * Sets popup menu border color.
     *
     * @param color new popup menu border color
     */
    public void setBorderColor ( final Color color )
    {
        getWebUI ().setBorderColor ( color );
    }

    /**
     * Returns popup menu border corners rounding.
     *
     * @return popup menu border corners rounding
     */
    public int getRound ()
    {
        return getWebUI ().getRound ();
    }

    /**
     * Sets popup menu border corners rounding.
     *
     * @param round new popup menu border corners rounding
     */
    public void setRound ( final int round )
    {
        getWebUI ().setRound ( round );
    }

    /**
     * Returns popup menu shade width.
     *
     * @return popup menu shade width
     */
    public int getShadeWidth ()
    {
        return getWebUI ().getShadeWidth ();
    }

    /**
     * Sets popup menu shade width.
     *
     * @param width new popup menu shade width
     */
    public void setShadeWidth ( final int width )
    {
        getWebUI ().setShadeWidth ( width );
    }

    /**
     * Returns popup menu shade opacity.
     *
     * @return popup menu shade opacity
     */
    public float getShadeOpacity ()
    {
        return getWebUI ().getShadeOpacity ();
    }

    /**
     * Sets popup menu shade opacity.
     *
     * @param opacity new popup menu shade opacity
     */
    public void setShadeOpacity ( final float opacity )
    {
        getWebUI ().setShadeOpacity ( opacity );
    }

    /**
     * Returns popup menu dropdown style corner width.
     *
     * @return popup menu dropdown style corner width
     */
    public int getCornerWidth ()
    {
        return getWebUI ().getCornerWidth ();
    }

    /**
     * Sets popup menu dropdown style corner width.
     *
     * @param width popup menu dropdown style corner width
     */
    public void setCornerWidth ( final int width )
    {
        getWebUI ().setCornerWidth ( width );
    }

    /**
     * Returns popup menu content margin.
     *
     * @return popup menu content margin
     */
    @Override
    public Insets getMargin ()
    {
        return getWebUI ().getMargin ();
    }

    /**
     * Sets popup menu content margin.
     *
     * @param margin new popup menu content margin
     */
    public void setMargin ( final Insets margin )
    {
        getWebUI ().setMargin ( margin );
    }

    /**
     * Returns popup menu painter.
     *
     * @return popup menu painter
     */
    public Painter getPainter ()
    {
        return getWebUI ().getPainter ();
    }

    /**
     * Sets popup menu painter.
     *
     * @param painter new popup menu painter
     */
    public void setPainter ( final Painter painter )
    {
        getWebUI ().setPainter ( painter );
    }

    /**
     * Returns spacing between menubar popup menus.
     *
     * @return spacing between menubar popup menus
     */
    public int getMenuSpacing ()
    {
        return getWebUI ().getMenuSpacing ();
    }

    /**
     * Sets spacing between menubar popup menus.
     *
     * @param spacing new spacing between menubar popup menus
     */
    public void setMenuSpacing ( final int spacing )
    {
        getWebUI ().setMenuSpacing ( spacing );
    }

    /**
     * Returns whether popup menu should try to fix its initial location when displayed or not.
     *
     * @return true if popup menu should try to fix its initial location when displayed, false otherwise
     */
    public boolean isFixLocation ()
    {
        return getWebUI ().isFixLocation ();
    }

    /**
     * Sets whether popup menu should try to fix its initial location when displayed or not.
     *
     * @param fixLocation whether popup menu should try to fix its initial location when displayed or not
     */
    public void setFixLocation ( final boolean fixLocation )
    {
        getWebUI ().setFixLocation ( fixLocation );
    }

    /**
     * Assists popup menu to allow it choose the best position relative to invoker.
     * Its value nullified right after first usage to avoid popup menu display issues in future.
     *
     * @param way approximate popup menu display way
     */
    public void setPopupMenuWay ( final PopupMenuWay way )
    {
        getWebUI ().setPopupMenuWay ( way );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    /**
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    public WebPopupMenuUI getWebUI ()
    {
        return ( WebPopupMenuUI ) getUI ();
    }

    /**
     * Installs a Web-UI into this component.
     */
    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebPopupMenuUI ) )
        {
            try
            {
                setUI ( ( WebPopupMenuUI ) ReflectUtils.createInstance ( WebLookAndFeel.popupMenuUI ) );
            }
            catch ( Throwable e )
            {
                e.printStackTrace ();
                setUI ( new WebPopupMenuUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }
}