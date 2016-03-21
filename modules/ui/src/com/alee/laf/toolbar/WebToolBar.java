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

package com.alee.laf.toolbar;

import com.alee.extended.layout.ToolbarLayout;
import com.alee.laf.WebLookAndFeel;
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
import java.util.List;
import java.util.Map;

/**
 * @author Mikle Garin
 */

public class WebToolBar extends JToolBar
        implements Styleable, Skinnable, Paintable, ShapeProvider, MarginSupport, PaddingSupport, SizeMethods<WebToolBar>,
        LanguageContainerMethods
{
    public WebToolBar ()
    {
        super ();
    }

    public WebToolBar ( final int orientation )
    {
        super ( orientation );
    }

    public WebToolBar ( final String name )
    {
        super ( name );
    }

    public WebToolBar ( final String name, final int orientation )
    {
        super ( name, orientation );
    }

    public WebToolBar ( final StyleId id )
    {
        super ();
        setStyleId ( id );
    }

    public WebToolBar ( final StyleId id, final int orientation )
    {
        super ( orientation );
        setStyleId ( id );
    }

    public WebToolBar ( final StyleId id, final String name )
    {
        super ( name );
        setStyleId ( id );
    }

    public WebToolBar ( final StyleId id, final String name, final int orientation )
    {
        super ( name, orientation );
        setStyleId ( id );
    }

    public void addToMiddle ( final Component component )
    {
        add ( component, ToolbarLayout.MIDDLE );
    }

    public void addFill ( final Component component )
    {
        add ( component, ToolbarLayout.FILL );
    }

    public void addToEnd ( final Component component )
    {
        add ( component, ToolbarLayout.END );
    }

    @Override
    public void addSeparator ()
    {
        addSeparator ( ToolbarLayout.START );
    }

    public WebToolBarSeparator addSeparatorToEnd ()
    {
        return addSeparator ( ToolbarLayout.END );
    }

    public WebToolBarSeparator addSeparator ( final String constrain )
    {
        return addSeparator ( constrain, StyleId.toolbarseparator );
    }

    public WebToolBarSeparator addSeparator ( final StyleId id )
    {
        return addSeparator ( ToolbarLayout.START, id );
    }

    public WebToolBarSeparator addSeparatorToEnd ( final StyleId id )
    {
        return addSeparator ( ToolbarLayout.END, id );
    }

    public WebToolBarSeparator addSeparator ( final String constrain, final StyleId id )
    {
        final WebToolBarSeparator separator = new WebToolBarSeparator ( id );
        add ( separator, constrain );
        return separator;
    }

    public void addSpacing ()
    {
        addSpacing ( 2 );
    }

    public void addSpacing ( final int spacing )
    {
        addSpacing ( spacing, ToolbarLayout.START );
    }

    public void addSpacingToEnd ()
    {
        addSpacingToEnd ( 2 );
    }

    public void addSpacingToEnd ( final int spacing )
    {
        addSpacing ( spacing, ToolbarLayout.END );
    }

    public void addSpacing ( final int spacing, final String constrain )
    {
        // todo Add layout implementation instead of wasted component
        add ( new WhiteSpace ( spacing ), constrain );
    }

    public void add ( final List<? extends Component> components, final int index )
    {
        if ( components != null )
        {
            for ( int i = 0; i < components.size (); i++ )
            {
                add ( components.get ( i ), index + i );
            }
        }
    }

    public void add ( final List<? extends Component> components, final String constraints )
    {
        if ( components != null )
        {
            for ( final Component component : components )
            {
                add ( component, constraints );
            }
        }
    }

    public void add ( final List<? extends Component> components )
    {
        if ( components != null )
        {
            for ( final Component component : components )
            {
                add ( component );
            }
        }
    }

    public void add ( final int index, final Component... components )
    {
        if ( components != null && components.length > 0 )
        {
            for ( int i = 0; i < components.length; i++ )
            {
                add ( components[ i ], index + i );
            }
        }
    }

    public void add ( final String constraints, final Component... components )
    {
        if ( components != null && components.length > 0 )
        {
            for ( final Component component : components )
            {
                add ( component, constraints );
            }
        }
    }

    public void add ( final Component... components )
    {
        if ( components != null && components.length > 0 )
        {
            for ( final Component component : components )
            {
                add ( component );
            }
        }
    }

    public Component getFirstComponent ()
    {
        if ( getComponentCount () > 0 )
        {
            return getComponent ( 0 );
        }
        else
        {
            return null;
        }
    }

    public Component getLastComponent ()
    {
        if ( getComponentCount () > 0 )
        {
            return getComponent ( getComponentCount () - 1 );
        }
        else
        {
            return null;
        }
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
    public WebToolBarUI getWebUI ()
    {
        return ( WebToolBarUI ) getUI ();
    }

    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebToolBarUI ) )
        {
            try
            {
                setUI ( ( WebToolBarUI ) ReflectUtils.createInstance ( WebLookAndFeel.toolBarUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebToolBarUI () );
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
        return SizeUtils.getPreferredWidth ( this );
    }

    @Override
    public WebToolBar setPreferredWidth ( final int preferredWidth )
    {
        return SizeUtils.setPreferredWidth ( this, preferredWidth );
    }

    @Override
    public int getPreferredHeight ()
    {
        return SizeUtils.getPreferredHeight ( this );
    }

    @Override
    public WebToolBar setPreferredHeight ( final int preferredHeight )
    {
        return SizeUtils.setPreferredHeight ( this, preferredHeight );
    }

    @Override
    public int getMinimumWidth ()
    {
        return SizeUtils.getMinimumWidth ( this );
    }

    @Override
    public WebToolBar setMinimumWidth ( final int minimumWidth )
    {
        return SizeUtils.setMinimumWidth ( this, minimumWidth );
    }

    @Override
    public int getMinimumHeight ()
    {
        return SizeUtils.getMinimumHeight ( this );
    }

    @Override
    public WebToolBar setMinimumHeight ( final int minimumHeight )
    {
        return SizeUtils.setMinimumHeight ( this, minimumHeight );
    }

    @Override
    public int getMaximumWidth ()
    {
        return SizeUtils.getMaximumWidth ( this );
    }

    @Override
    public WebToolBar setMaximumWidth ( final int maximumWidth )
    {
        return SizeUtils.setMaximumWidth ( this, maximumWidth );
    }

    @Override
    public int getMaximumHeight ()
    {
        return SizeUtils.getMaximumHeight ( this );
    }

    @Override
    public WebToolBar setMaximumHeight ( final int maximumHeight )
    {
        return SizeUtils.setMaximumHeight ( this, maximumHeight );
    }

    @Override
    public WebToolBar setPreferredSize ( final int width, final int height )
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