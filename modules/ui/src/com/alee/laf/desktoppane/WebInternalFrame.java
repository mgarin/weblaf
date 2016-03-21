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

package com.alee.laf.desktoppane;

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.managers.log.Log;
import com.alee.managers.style.*;
import com.alee.managers.style.Skin;
import com.alee.managers.style.Skinnable;
import com.alee.managers.style.StyleListener;
import com.alee.painter.Paintable;
import com.alee.painter.Painter;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.util.Map;

/**
 * This JInternalFrame extension class provides a direct access to WebInternalFrameUI methods.
 * There is also a set of additional methods to simplify some operations with internal frame.
 * <p>
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application L&amp;F as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 */

public class WebInternalFrame extends JInternalFrame
        implements Styleable, Skinnable, Paintable, ShapeProvider, MarginSupport, PaddingSupport, LanguageMethods
{
    /**
     * Event properties.
     */
    public static final String CLOSABLE_PROPERTY = "closable";
    public static final String MAXIMIZABLE_PROPERTY = "maximizable";
    public static final String ICONABLE_PROPERTY = "iconable";

    /**
     * Creates a non-resizable, non-closable, non-maximizable, non-iconifiable {@code JInternalFrame} with no title.
     */
    public WebInternalFrame ()
    {
        super ();
    }

    /**
     * Creates a non-resizable, non-closable, non-maximizable, non-iconifiable {@code WebInternalFrame} with the specified title.
     * Note that passing in a {@code null} {@code title} results in unspecified behavior and possibly an exception.
     *
     * @param title the non-{@code null} {@code String} to display in the title bar
     */
    public WebInternalFrame ( final String title )
    {
        super ( title );
    }

    /**
     * Creates a non-closable, non-maximizable, non-iconifiable {@code WebInternalFrame} with the specified title and resizability.
     *
     * @param title     the {@code String} to display in the title bar
     * @param resizable if {@code true}, the internal frame can be resized
     */
    public WebInternalFrame ( final String title, final boolean resizable )
    {
        super ( title, resizable );
    }

    /**
     * Creates a non-maximizable, non-iconifiable {@code WebInternalFrame} with the specified title, resizability, and closability.
     *
     * @param title     the {@code String} to display in the title bar
     * @param resizable if {@code true}, the internal frame can be resized
     * @param closable  if {@code true}, the internal frame can be closed
     */
    public WebInternalFrame ( final String title, final boolean resizable, final boolean closable )
    {
        super ( title, resizable, closable );
    }

    /**
     * Creates a non-iconifiable {@code WebInternalFrame} with the specified title, resizability, closability, and maximizability.
     *
     * @param title       the {@code String} to display in the title bar
     * @param resizable   if {@code true}, the internal frame can be resized
     * @param closable    if {@code true}, the internal frame can be closed
     * @param maximizable if {@code true}, the internal frame can be maximized
     */
    public WebInternalFrame ( final String title, final boolean resizable, final boolean closable, final boolean maximizable )
    {
        super ( title, resizable, closable, maximizable );
    }

    /**
     * Creates a {@code WebInternalFrame} with the specified title, resizability, closability, maximizability, and iconifiability.
     *
     * @param title       the {@code String} to display in the title bar
     * @param resizable   if {@code true}, the internal frame can be resized
     * @param closable    if {@code true}, the internal frame can be closed
     * @param maximizable if {@code true}, the internal frame can be maximized
     * @param iconifiable if {@code true}, the internal frame can be iconified
     */
    public WebInternalFrame ( final String title, final boolean resizable, final boolean closable, final boolean maximizable,
                              final boolean iconifiable )
    {
        super ( title, resizable, closable, maximizable, iconifiable );
    }

    /**
     * Creates a non-resizable, non-closable, non-maximizable, non-iconifiable {@code JInternalFrame} with no title.
     *
     * @param id style ID
     */
    public WebInternalFrame ( final StyleId id )
    {
        super ();
        setStyleId ( id );
    }

    /**
     * Creates a non-resizable, non-closable, non-maximizable, non-iconifiable {@code WebInternalFrame} with the specified title.
     * Note that passing in a {@code null} {@code title} results in unspecified behavior and possibly an exception.
     *
     * @param id    style ID
     * @param title the non-{@code null} {@code String} to display in the title bar
     */
    public WebInternalFrame ( final StyleId id, final String title )
    {
        super ( title );
        setStyleId ( id );
    }

    /**
     * Creates a non-closable, non-maximizable, non-iconifiable {@code WebInternalFrame} with the specified title and resizability.
     *
     * @param id        style ID
     * @param title     the {@code String} to display in the title bar
     * @param resizable if {@code true}, the internal frame can be resized
     */
    public WebInternalFrame ( final StyleId id, final String title, final boolean resizable )
    {
        super ( title, resizable );
        setStyleId ( id );
    }

    /**
     * Creates a non-maximizable, non-iconifiable {@code WebInternalFrame} with the specified title, resizability, and closability.
     *
     * @param id        style ID
     * @param title     the {@code String} to display in the title bar
     * @param resizable if {@code true}, the internal frame can be resized
     * @param closable  if {@code true}, the internal frame can be closed
     */
    public WebInternalFrame ( final StyleId id, final String title, final boolean resizable, final boolean closable )
    {
        super ( title, resizable, closable );
        setStyleId ( id );
    }

    /**
     * Creates a non-iconifiable {@code WebInternalFrame} with the specified title, resizability, closability, and maximizability.
     *
     * @param id          style ID
     * @param title       the {@code String} to display in the title bar
     * @param resizable   if {@code true}, the internal frame can be resized
     * @param closable    if {@code true}, the internal frame can be closed
     * @param maximizable if {@code true}, the internal frame can be maximized
     */
    public WebInternalFrame ( final StyleId id, final String title, final boolean resizable, final boolean closable,
                              final boolean maximizable )
    {
        super ( title, resizable, closable, maximizable );
        setStyleId ( id );
    }

    /**
     * Creates a {@code WebInternalFrame} with the specified title, resizability, closability, maximizability, and iconifiability.
     *
     * @param id          style ID
     * @param title       the {@code String} to display in the title bar
     * @param resizable   if {@code true}, the internal frame can be resized
     * @param closable    if {@code true}, the internal frame can be closed
     * @param maximizable if {@code true}, the internal frame can be maximized
     * @param iconifiable if {@code true}, the internal frame can be iconified
     */
    public WebInternalFrame ( final StyleId id, final String title, final boolean resizable, final boolean closable,
                              final boolean maximizable, final boolean iconifiable )
    {
        super ( title, resizable, closable, maximizable, iconifiable );
        setStyleId ( id );
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
    private WebInternalFrameUI getWebUI ()
    {
        return ( WebInternalFrameUI ) getUI ();
    }

    @Override
    public void setIcon ( final boolean b )
    {
        try
        {
            super.setIcon ( b );
        }
        catch ( final PropertyVetoException e )
        {
            Log.error ( this, e );
        }
    }

    /**
     * Safely hides internal frame.
     */
    public void close ()
    {
        try
        {
            setClosed ( true );
        }
        catch ( final PropertyVetoException e )
        {
            Log.error ( this, e );
        }
    }

    /**
     * Safely displays internal frame.
     */
    public void open ()
    {
        try
        {
            setClosed ( false );
            setVisible ( true );
        }
        catch ( final PropertyVetoException e )
        {
            Log.error ( this, e );
        }
    }

    /**
     * Installs a Web-UI into this component.
     */
    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebInternalFrameUI ) )
        {
            try
            {
                setUI ( ( WebInternalFrameUI ) ReflectUtils.createInstance ( WebLookAndFeel.internalFrameUI, this ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebInternalFrameUI ( this ) );
            }
        }
        else
        {
            setUI ( getUI () );
        }
        invalidate ();
    }

    /**
     * Language methods
     */

    @Override
    public void setLanguage ( final String key, final Object... data )
    {
        LanguageManager.registerComponent ( this, key, data );
    }

    @Override
    public void updateLanguage ( final Object... data )
    {
        LanguageManager.updateComponent ( this, data );
    }

    @Override
    public void updateLanguage ( final String key, final Object... data )
    {
        LanguageManager.updateComponent ( this, key, data );
    }

    @Override
    public void removeLanguage ()
    {
        LanguageManager.unregisterComponent ( this );
    }

    @Override
    public boolean isLanguageSet ()
    {
        return LanguageManager.isRegisteredComponent ( this );
    }

    @Override
    public void setLanguageUpdater ( final LanguageUpdater updater )
    {
        LanguageManager.registerLanguageUpdater ( this, updater );
    }

    @Override
    public void removeLanguageUpdater ()
    {
        LanguageManager.unregisterLanguageUpdater ( this );
    }
}