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
import com.alee.extended.painter.Painter;
import com.alee.global.StyleConstants;
import com.alee.managers.style.StyleId;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.separator.WebSeparator;
import com.alee.managers.language.LanguageContainerMethods;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.log.Log;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SizeUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.SizeMethods;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class WebToolBar extends JToolBar implements Styleable, ShapeProvider, SizeMethods<WebToolBar>, LanguageContainerMethods
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

    /**
     * Additional toolbar element methods
     */

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

    public WebSeparator addSeparatorToEnd ()
    {
        return addSeparator ( ToolbarLayout.END );
    }

    public WebSeparator addSeparator ( final String constrain )
    {
        final WebSeparator separator = new WebSeparator ( getOrientation () == HORIZONTAL ? VERTICAL : HORIZONTAL );
        add ( separator, constrain );
        return separator;
    }

    public WebSeparator addSeparator ( final int spacing )
    {
        return addSeparator ( ToolbarLayout.START, spacing );
    }

    public WebSeparator addSeparatorToEnd ( final int spacing )
    {
        return addSeparator ( ToolbarLayout.END, spacing );
    }

    public WebSeparator addSeparator ( final String constrain, final int spacing )
    {
        final boolean hor = getOrientation () == HORIZONTAL;
        final WebSeparator separator = new WebSeparator ( hor ? VERTICAL : HORIZONTAL );
        // todo style id for toolbar separator
        // separator.setStyleId ();
        add ( separator, constrain );
        return separator;
    }

    public void addSpacing ()
    {
        addSpacing ( StyleConstants.contentSpacing );
    }

    public void addSpacing ( final int spacing )
    {
        addSpacing ( spacing, ToolbarLayout.START );
    }

    public void addSpacingToEnd ()
    {
        addSpacingToEnd ( StyleConstants.contentSpacing );
    }

    public void addSpacingToEnd ( final int spacing )
    {
        addSpacing ( spacing, ToolbarLayout.END );
    }

    public void addSpacing ( final int spacing, final String constrain )
    {
        add ( new WhiteSpace ( spacing ), constrain );
    }

    /**
     * Additional children interaction methods
     */

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

    /**
     * UI methods
     */
    /**
     * Returns toolbar painter.
     *
     * @return toolbar painter
     */
    public Painter getPainter ()
    {
        return getWebUI ().getPainter ();
    }

    /**
     * Sets toolbar painter.
     * Pass null to remove toolbar painter.
     *
     * @param painter new toolbar painter
     * @return this toolbar
     */
    public WebToolBar setPainter ( final Painter painter )
    {
        getWebUI ().setPainter ( painter );
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StyleId getStyleId ()
    {
        return getWebUI ().getStyleId ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleId ( final StyleId id )
    {
        getWebUI ().setStyleId ( id );
    }

    @Override
    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

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
    public WebToolBar setPreferredWidth ( final int preferredWidth )
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
    public WebToolBar setPreferredHeight ( final int preferredHeight )
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
    public WebToolBar setMinimumWidth ( final int minimumWidth )
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
    public WebToolBar setMinimumHeight ( final int minimumHeight )
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
    public WebToolBar setMaximumWidth ( final int maximumWidth )
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
    public WebToolBar setMaximumHeight ( final int maximumHeight )
    {
        return SizeUtils.setMaximumHeight ( this, maximumHeight );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebToolBar setPreferredSize ( final int width, final int height )
    {
        return SizeUtils.setPreferredSize ( this, width, height );
    }

    /**
     * Language container methods
     */

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
