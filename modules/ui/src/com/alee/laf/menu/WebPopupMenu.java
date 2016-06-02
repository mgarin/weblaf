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

import com.alee.managers.log.Log;
import com.alee.managers.style.*;
import com.alee.painter.Paintable;
import com.alee.painter.Painter;
import com.alee.utils.swing.extensions.SizeMethods;
import com.alee.utils.swing.extensions.SizeMethodsImpl;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * This JPopupMenu extension class provides a direct access to WebPopupMenuUI methods.
 * It also has a few additional methods to simplify popup window positioning.
 *
 * @author Mikle Garin
 */

public class WebPopupMenu extends JPopupMenu
        implements Styleable, Paintable, ShapeProvider, MarginSupport, PaddingSupport, SizeMethods<WebPopupMenu>
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
     * @param id style ID
     */
    public WebPopupMenu ( final StyleId id )
    {
        super ();
        setStyleId ( id );
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

    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( this );
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return StyleManager.setStyleId ( this, id );
    }

    @Override
    public StyleId resetStyleId ()
    {
        return StyleManager.resetStyleId ( this );
    }

    @Override
    public Skin getSkin ()
    {
        return StyleManager.getSkin ( this );
    }

    @Override
    public Skin setSkin ( final Skin skin )
    {
        return StyleManager.setSkin ( this, skin );
    }

    @Override
    public Skin setSkin ( final Skin skin, final boolean recursively )
    {
        return StyleManager.setSkin ( this, skin, recursively );
    }

    @Override
    public Skin resetSkin ()
    {
        return StyleManager.resetSkin ( this );
    }

    @Override
    public void addStyleListener ( final StyleListener listener )
    {
        StyleManager.addStyleListener ( this, listener );
    }

    @Override
    public void removeStyleListener ( final StyleListener listener )
    {
        StyleManager.removeStyleListener ( this, listener );
    }

    @Override
    public Map<String, Painter> getCustomPainters ()
    {
        return StyleManager.getCustomPainters ( this );
    }

    @Override
    public Painter getCustomPainter ()
    {
        return StyleManager.getCustomPainter ( this );
    }

    @Override
    public Painter getCustomPainter ( final String id )
    {
        return StyleManager.getCustomPainter ( this, id );
    }

    @Override
    public Painter setCustomPainter ( final Painter painter )
    {
        return StyleManager.setCustomPainter ( this, painter );
    }

    @Override
    public Painter setCustomPainter ( final String id, final Painter painter )
    {
        return StyleManager.setCustomPainter ( this, id, painter );
    }

    @Override
    public boolean resetPainter ()
    {
        return StyleManager.resetPainter ( this );
    }

    @Override
    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    @Override
    public Insets getMargin ()
    {
        return getWebUI ().getMargin ();
    }

    /**
     * Sets new margin.
     *
     * @param margin new margin
     */
    public void setMargin ( final int margin )
    {
        setMargin ( margin, margin, margin, margin );
    }

    /**
     * Sets new margin.
     *
     * @param top    new top margin
     * @param left   new left margin
     * @param bottom new bottom margin
     * @param right  new right margin
     */
    public void setMargin ( final int top, final int left, final int bottom, final int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        getWebUI ().setMargin ( margin );
    }

    @Override
    public Insets getPadding ()
    {
        return getWebUI ().getPadding ();
    }

    /**
     * Sets new padding.
     *
     * @param padding new padding
     */
    public void setPadding ( final int padding )
    {
        setPadding ( padding, padding, padding, padding );
    }

    /**
     * Sets new padding.
     *
     * @param top    new top padding
     * @param left   new left padding
     * @param bottom new bottom padding
     * @param right  new right padding
     */
    public void setPadding ( final int top, final int left, final int bottom, final int right )
    {
        setPadding ( new Insets ( top, left, bottom, right ) );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        getWebUI ().setPadding ( padding );
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
                setUI ( ( WebPopupMenuUI ) UIManager.getUI ( this ) );
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

    @Override
    public int getPreferredWidth ()
    {
        return SizeMethodsImpl.getPreferredWidth ( this );
    }

    @Override
    public WebPopupMenu setPreferredWidth ( final int preferredWidth )
    {
        return SizeMethodsImpl.setPreferredWidth ( this, preferredWidth );
    }

    @Override
    public int getPreferredHeight ()
    {
        return SizeMethodsImpl.getPreferredHeight ( this );
    }

    @Override
    public WebPopupMenu setPreferredHeight ( final int preferredHeight )
    {
        return SizeMethodsImpl.setPreferredHeight ( this, preferredHeight );
    }

    @Override
    public int getMinimumWidth ()
    {
        return SizeMethodsImpl.getMinimumWidth ( this );
    }

    @Override
    public WebPopupMenu setMinimumWidth ( final int minimumWidth )
    {
        return SizeMethodsImpl.setMinimumWidth ( this, minimumWidth );
    }

    @Override
    public int getMinimumHeight ()
    {
        return SizeMethodsImpl.getMinimumHeight ( this );
    }

    @Override
    public WebPopupMenu setMinimumHeight ( final int minimumHeight )
    {
        return SizeMethodsImpl.setMinimumHeight ( this, minimumHeight );
    }

    @Override
    public int getMaximumWidth ()
    {
        return SizeMethodsImpl.getMaximumWidth ( this );
    }

    @Override
    public WebPopupMenu setMaximumWidth ( final int maximumWidth )
    {
        return SizeMethodsImpl.setMaximumWidth ( this, maximumWidth );
    }

    @Override
    public int getMaximumHeight ()
    {
        return SizeMethodsImpl.getMaximumHeight ( this );
    }

    @Override
    public WebPopupMenu setMaximumHeight ( final int maximumHeight )
    {
        return SizeMethodsImpl.setMaximumHeight ( this, maximumHeight );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        return SizeMethodsImpl.getPreferredSize ( this, super.getPreferredSize () );
    }

    @Override
    public Dimension getOriginalPreferredSize ()
    {
        return SizeMethodsImpl.getOriginalPreferredSize ( this, super.getPreferredSize () );
    }

    @Override
    public WebPopupMenu setPreferredSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setPreferredSize ( this, width, height );
    }
}