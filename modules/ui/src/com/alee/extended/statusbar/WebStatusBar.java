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

package com.alee.extended.statusbar;

import com.alee.extended.layout.ToolbarLayout;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.separator.WebSeparator;
import com.alee.managers.language.LanguageContainerMethods;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.log.Log;
import com.alee.managers.style.*;
import com.alee.managers.style.Skin;
import com.alee.managers.style.Skinnable;
import com.alee.managers.style.StyleListener;
import com.alee.painter.Paintable;
import com.alee.painter.Painter;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SizeUtils;
import com.alee.utils.swing.SizeMethods;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Implementation of status bar panel.
 * It is a container that is usually used at the bottom side of the application UI and contains some status information.
 *
 * @author Mikle Garin
 */

public class WebStatusBar extends JComponent
        implements Styleable, Skinnable, Paintable, ShapeProvider, MarginSupport, PaddingSupport, SizeMethods<WebStatusBar>,
        LanguageContainerMethods
{
    /**
     * Constructs new status bar.
     */
    public WebStatusBar ()
    {
        this ( StyleId.statusbar );
    }

    /**
     * Constructs new status bar.
     *
     * @param id style ID
     */
    public WebStatusBar ( final StyleId id )
    {
        super ();
        setLayout ( new ToolbarLayout () );
        updateUI ();
        setStyleId ( id );
    }

    /**
     * Adds component to the middle status bar area.
     *
     * @param component component to add
     */
    public void addToMiddle ( final Component component )
    {
        add ( component, ToolbarLayout.MIDDLE );
    }

    /**
     * Adds component to the fill status bar area.
     *
     * @param component component to add
     */
    public void addFill ( final Component component )
    {
        add ( component, ToolbarLayout.FILL );
    }

    /**
     * Adds component to the end of the status bar.
     *
     * @param component component to add
     */
    public void addToEnd ( final Component component )
    {
        add ( component, ToolbarLayout.END );
    }

    /**
     * Adds separator to the start of the status bar.
     */
    public void addSeparator ()
    {
        addSeparator ( ToolbarLayout.START );
    }

    /**
     * Adds separator to the end of the status bar.
     */
    public void addSeparatorToEnd ()
    {
        addSeparator ( ToolbarLayout.END );
    }

    /**
     * Adds separator under the specified constaints.
     *
     * @param constraints layout constraints
     */
    public void addSeparator ( final String constraints )
    {
        add ( createSeparator (), constraints );
    }

    /**
     * Returns new status bar separator.
     *
     * @return new status bar separator
     */
    protected WebSeparator createSeparator ()
    {
        return new WebSeparator ( WebSeparator.VERTICAL );
    }

    @Override
    public StyleId getStyleId ()
    {
        return getWebUI ().getStyleId ();
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return getWebUI ().setStyleId ( id );
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
    public Skin restoreSkin ()
    {
        return StyleManager.restoreSkin ( this );
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
    public boolean restoreDefaultPainters ()
    {
        return StyleManager.restoreDefaultPainters ( this );
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
    public WebStatusBarUI getWebUI ()
    {
        return ( WebStatusBarUI ) getUI ();
    }

    /**
     * Returns the look and feel (L&amp;F) object that renders this component.
     *
     * @return the StatusBarUI object that renders this component
     */
    public StatusBarUI getUI ()
    {
        return ( StatusBarUI ) ui;
    }

    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebStatusBarUI ) )
        {
            try
            {
                setUI ( ( WebStatusBarUI ) ReflectUtils.createInstance ( WebLookAndFeel.statusBarUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebStatusBarUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    @Override
    public String getUIClassID ()
    {
        return StyleableComponent.statusbar.getUIClassID ();
    }

    @Override
    public int getPreferredWidth ()
    {
        return SizeUtils.getPreferredWidth ( this );
    }

    @Override
    public WebStatusBar setPreferredWidth ( final int preferredWidth )
    {
        return SizeUtils.setPreferredWidth ( this, preferredWidth );
    }

    @Override
    public int getPreferredHeight ()
    {
        return SizeUtils.getPreferredHeight ( this );
    }

    @Override
    public WebStatusBar setPreferredHeight ( final int preferredHeight )
    {
        return SizeUtils.setPreferredHeight ( this, preferredHeight );
    }

    @Override
    public int getMinimumWidth ()
    {
        return SizeUtils.getMinimumWidth ( this );
    }

    @Override
    public WebStatusBar setMinimumWidth ( final int minimumWidth )
    {
        return SizeUtils.setMinimumWidth ( this, minimumWidth );
    }

    @Override
    public int getMinimumHeight ()
    {
        return SizeUtils.getMinimumHeight ( this );
    }

    @Override
    public WebStatusBar setMinimumHeight ( final int minimumHeight )
    {
        return SizeUtils.setMinimumHeight ( this, minimumHeight );
    }

    @Override
    public int getMaximumWidth ()
    {
        return SizeUtils.getMaximumWidth ( this );
    }

    @Override
    public WebStatusBar setMaximumWidth ( final int maximumWidth )
    {
        return SizeUtils.setMaximumWidth ( this, maximumWidth );
    }

    @Override
    public int getMaximumHeight ()
    {
        return SizeUtils.getMaximumHeight ( this );
    }

    @Override
    public WebStatusBar setMaximumHeight ( final int maximumHeight )
    {
        return SizeUtils.setMaximumHeight ( this, maximumHeight );
    }

    @Override
    public WebStatusBar setPreferredSize ( final int width, final int height )
    {
        return SizeUtils.setPreferredSize ( this, width, height );
    }

    @Override
    public void setLanguageContainerKey ( final String key )
    {
        LanguageManager.registerLanguageContainer ( this, key );
    }

    @Override
    public void removeLanguageContainerKey ()
    {
        LanguageManager.unregisterLanguageContainer ( this );
    }

    @Override
    public String getLanguageContainerKey ()
    {
        return LanguageManager.getLanguageContainerKey ( this );
    }
}