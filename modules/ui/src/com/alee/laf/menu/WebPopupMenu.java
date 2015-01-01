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

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.language.LanguageContainerMethods;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.log.Log;
import com.alee.managers.style.StyleManager;
import com.alee.managers.style.skin.web.PopupStyle;
import com.alee.managers.style.skin.web.WebPopupMenuPainter;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SizeUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.SizeMethods;

import javax.swing.*;
import java.awt.*;

/**
 * This JPopupMenu extension class provides a direct access to WebPopupMenuUI methods.
 * It also has a few additional methods to simplify popup window positioning.
 *
 * @author Mikle Garin
 */

public class WebPopupMenu extends JPopupMenu implements Styleable, ShapeProvider, SizeMethods<WebPopupMenu>, LanguageContainerMethods
{
    /**
     * Constructs new popup menu.
     */
    public WebPopupMenu ()
    {
        super ();
    }

    /**
     * Constructs new popup menu with the specified style ID.
     *
     * @param styleId popup menu style ID
     */
    public WebPopupMenu ( final String styleId )
    {
        super ();
        setStyleId ( styleId );
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
    public void addSeparator ( final int index )
    {
        add ( new WebPopupMenu.Separator (), index );
    }

    /**
     * Displays popup menu above the invoker component starting at its leading side.
     * This method takes into account component orientation.
     *
     * @param invoker invoker component
     * @return this popup menu
     */
    public WebPopupMenu showAbove ( final Component invoker )
    {
        return showAboveStart ( invoker );
    }

    /**
     * Displays popup menu above the invoker component starting at its leading side.
     * This method takes into account component orientation.
     *
     * @param invoker invoker component
     * @return this popup menu
     */
    public WebPopupMenu showAboveStart ( final Component invoker )
    {
        setPopupMenuWay ( PopupMenuWay.aboveStart );
        show ( invoker, 0, 0 );
        return this;
    }

    /**
     * Displays popup menu above the invoker component at its middle.
     *
     * @param invoker invoker component
     * @return this popup menu
     */
    public WebPopupMenu showAboveMiddle ( final Component invoker )
    {
        setPopupMenuWay ( PopupMenuWay.aboveMiddle );
        show ( invoker, 0, 0 );
        return this;
    }

    /**
     * Displays popup menu above the invoker component starting at its trailing side.
     * This method takes into account component orientation.
     *
     * @param invoker invoker component
     * @return this popup menu
     */
    public WebPopupMenu showAboveEnd ( final Component invoker )
    {
        setPopupMenuWay ( PopupMenuWay.aboveEnd );
        show ( invoker, 0, 0 );
        return this;
    }

    /**
     * Displays popup menu under the invoker component starting at its leading side.
     * This method also takes into account component orientation.
     *
     * @param invoker invoker component
     * @return this popup menu
     */
    public WebPopupMenu showBelow ( final Component invoker )
    {
        return showBelowStart ( invoker );
    }

    /**
     * Displays popup menu under the invoker component starting at its leading side.
     * This method takes into account component orientation.
     *
     * @param invoker invoker component
     * @return this popup menu
     */
    public WebPopupMenu showBelowStart ( final Component invoker )
    {
        setPopupMenuWay ( PopupMenuWay.belowStart );
        show ( invoker, 0, 0 );
        return this;
    }

    /**
     * Displays popup menu under the invoker component at its middle.
     *
     * @param invoker invoker component
     * @return this popup menu
     */
    public WebPopupMenu showBelowMiddle ( final Component invoker )
    {
        setPopupMenuWay ( PopupMenuWay.belowMiddle );
        show ( invoker, 0, 0 );
        return this;
    }

    /**
     * Displays popup menu under the invoker component starting at its trailing side.
     * This method takes into account component orientation.
     *
     * @param invoker invoker component
     * @return this popup menu
     */
    public WebPopupMenu showBelowEnd ( final Component invoker )
    {
        setPopupMenuWay ( PopupMenuWay.belowEnd );
        show ( invoker, 0, 0 );
        return this;
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
     * @param margin popup menu content margin
     * @return this popup menu
     */
    public WebPopupMenu setMargin ( final Insets margin )
    {
        getWebUI ().setMargin ( margin );
        return this;
    }

    /**
     * Sets popup menu content margin.
     *
     * @param top    top popup menu content margin
     * @param left   left popup menu content margin
     * @param bottom bottom popup menu content margin
     * @param right  right popup menu content margin
     * @return this popup menu
     */
    public WebPopupMenu setMargin ( final int top, final int left, final int bottom, final int right )
    {
        return setMargin ( new Insets ( top, left, bottom, right ) );
    }

    /**
     * Sets popup menu content margin.
     *
     * @param spacing popup menu content margin
     * @return this popup menu
     */
    public WebPopupMenu setMargin ( final int spacing )
    {
        return setMargin ( spacing, spacing, spacing, spacing );
    }

    /**
     * Returns popup menu painter.
     *
     * @return popup menu painter
     */
    public PopupMenuPainter getPainter ()
    {
        return StyleManager.getPainter ( this );
    }

    /**
     * Sets popup menu painter.
     *
     * @param painter new popup menu painter
     * @return this popup menu
     */
    public WebPopupMenu setPainter ( final WebPopupMenuPainter painter )
    {
        StyleManager.setCustomPainter ( this, painter );
        return this;
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
     * @return this popup menu
     */
    public WebPopupMenu setMenuSpacing ( final int spacing )
    {
        getWebUI ().setMenuSpacing ( spacing );
        return this;
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
     * @return this popup menu
     */
    public WebPopupMenu setFixLocation ( final boolean fixLocation )
    {
        getWebUI ().setFixLocation ( fixLocation );
        return this;
    }

    /**
     * Assists popup menu to allow it choose the best position relative to invoker.
     * Its value nullified right after first usage to avoid popup menu display issues in future.
     *
     * @param way approximate popup menu display way
     * @return this popup menu
     */
    public WebPopupMenu setPopupMenuWay ( final PopupMenuWay way )
    {
        getWebUI ().setPopupMenuWay ( way );
        return this;
    }

    /**
     * Returns popup style.
     *
     * @return popup style
     */
    public PopupStyle getPopupStyle ()
    {
        return getWebUI ().getPopupStyle ();
    }

    /**
     * Sets popup style.
     *
     * @param style new popup style
     * @return this popup menu
     */
    public WebPopupMenu setPopupStyle ( final PopupStyle style )
    {
        getWebUI ().setPopupStyle ( style );
        return this;
    }

    /**
     * Returns popup border color.
     *
     * @return popup border color
     */
    public Color getBorderColor ()
    {
        return getWebUI ().getBorderColor ();
    }

    /**
     * Sets popup border color.
     *
     * @param color new popup border color
     * @return this popup menu
     */
    public WebPopupMenu setBorderColor ( final Color color )
    {
        getWebUI ().setBorderColor ( color );
        return this;
    }

    /**
     * Returns decoration corners rounding.
     *
     * @return decoration corners rounding
     */
    public int getRound ()
    {
        return getWebUI ().getRound ();
    }

    /**
     * Sets decoration corners rounding.
     *
     * @param round decoration corners rounding
     * @return this popup menu
     */
    public WebPopupMenu setRound ( final int round )
    {
        getWebUI ().setRound ( round );
        return this;
    }

    /**
     * Returns decoration shade width.
     *
     * @return decoration shade width
     */
    public int getShadeWidth ()
    {
        return getWebUI ().getShadeWidth ();
    }

    /**
     * Sets decoration shade width.
     *
     * @param shadeWidth decoration shade width
     * @return this popup menu
     */
    public WebPopupMenu setShadeWidth ( final int shadeWidth )
    {
        getWebUI ().setShadeWidth ( shadeWidth );
        return this;
    }

    /**
     * Returns popup shade transparency.
     *
     * @return popup shade transparency
     */
    public float getShadeTransparency ()
    {
        return getWebUI ().getShadeTransparency ();
    }

    /**
     * Sets popup shade transparency.
     *
     * @param opacity new popup shade transparency
     * @return this popup menu
     */
    public WebPopupMenu setShadeTransparency ( final float opacity )
    {
        getWebUI ().setShadeTransparency ( opacity );
        return this;
    }

    /**
     * Returns popup dropdown style corner width.
     *
     * @return popup dropdown style corner width
     */
    public int getCornerWidth ()
    {
        return getWebUI ().getCornerWidth ();
    }

    /**
     * Sets popup dropdown style corner width.
     *
     * @param width popup dropdown style corner width
     * @return this popup menu
     */
    public WebPopupMenu setCornerWidth ( final int width )
    {
        getWebUI ().setCornerWidth ( width );
        return this;
    }

    /**
     * Returns dropdown corner alignment.
     *
     * @return dropdown corner alignment
     */
    public int getCornerAlignment ()
    {
        return getWebUI ().getCornerAlignment ();
    }

    /**
     * Sets dropdown corner alignment.
     *
     * @param cornerAlignment dropdown corner alignment
     */
    public void setCornerAlignment ( final int cornerAlignment )
    {
        StyleManager.setCustomPainterProperty ( this, "cornerAlignment", cornerAlignment );
    }

    /**
     * Returns popup background transparency.
     *
     * @return popup background transparency
     */
    public float getTransparency ()
    {
        return getWebUI ().getTransparency ();
    }

    /**
     * Sets popup background transparency.
     *
     * @param transparency popup background transparency
     * @return this popup menu
     */
    public WebPopupMenu setTransparency ( final float transparency )
    {
        getWebUI ().setTransparency ( transparency );
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleId ()
    {
        return getWebUI ().getStyleId ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleId ( final String id )
    {
        getWebUI ().setStyleId ( id );
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
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebPopupMenuUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPreferredWidth ()
    {
        return SizeUtils.getPreferredWidth ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebPopupMenu setPreferredWidth ( final int preferredWidth )
    {
        return SizeUtils.setPreferredWidth ( this, preferredWidth );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPreferredHeight ()
    {
        return SizeUtils.getPreferredHeight ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebPopupMenu setPreferredHeight ( final int preferredHeight )
    {
        return SizeUtils.setPreferredHeight ( this, preferredHeight );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinimumWidth ()
    {
        return SizeUtils.getMinimumWidth ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebPopupMenu setMinimumWidth ( final int minimumWidth )
    {
        return SizeUtils.setMinimumWidth ( this, minimumWidth );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinimumHeight ()
    {
        return SizeUtils.getMinimumHeight ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebPopupMenu setMinimumHeight ( final int minimumHeight )
    {
        return SizeUtils.setMinimumHeight ( this, minimumHeight );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaximumWidth ()
    {
        return SizeUtils.getMaximumWidth ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebPopupMenu setMaximumWidth ( final int maximumWidth )
    {
        return SizeUtils.setMaximumWidth ( this, maximumWidth );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaximumHeight ()
    {
        return SizeUtils.getMaximumHeight ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebPopupMenu setMaximumHeight ( final int maximumHeight )
    {
        return SizeUtils.setMaximumHeight ( this, maximumHeight );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize ()
    {
        return SizeUtils.getPreferredSize ( this, super.getPreferredSize () );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebPopupMenu setPreferredSize ( final int width, final int height )
    {
        return SizeUtils.setPreferredSize ( this, width, height );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLanguageContainerKey ( final String key )
    {
        LanguageManager.registerLanguageContainer ( this, key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLanguageContainerKey ()
    {
        LanguageManager.unregisterLanguageContainer ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLanguageContainerKey ()
    {
        return LanguageManager.getLanguageContainerKey ( this );
    }
}