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
import com.alee.laf.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.separator.WebSeparator;
import com.alee.managers.language.LanguageContainerMethods;
import com.alee.managers.language.LanguageManager;
import com.alee.utils.ReflectUtils;
import com.alee.utils.laf.ShapeProvider;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * User: mgarin Date: 23.08.11 Time: 16:15
 */

public class WebToolBar extends JToolBar implements ShapeProvider, LanguageContainerMethods
{
    public WebToolBar ()
    {
        super ();
    }

    public WebToolBar ( final String name )
    {
        super ( name );
    }

    public WebToolBar ( final String name, final int orientation )
    {
        super ( name, orientation );
    }

    public WebToolBar ( final int orientation )
    {
        super ( orientation );
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
        final WebSeparator separator = new WebSeparator ( getOrientation () );
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
        add ( separator.setMargin ( hor ? 0 : spacing, hor ? spacing : 0, hor ? 0 : spacing, hor ? spacing : 0 ), constrain );
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
     * Additional childs interaction methods
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

    public boolean isUndecorated ()
    {
        return getWebUI ().isUndecorated ();
    }

    public void setUndecorated ( final boolean undecorated )
    {
        getWebUI ().setUndecorated ( undecorated );
    }

    public Painter getPainter ()
    {
        return getWebUI ().getPainter ();
    }

    public void setPainter ( final Painter painter )
    {
        getWebUI ().setPainter ( painter );
    }

    public int getRound ()
    {
        return getWebUI ().getRound ();
    }

    public void setRound ( final int round )
    {
        getWebUI ().setRound ( round );
    }

    public int getShadeWidth ()
    {
        return getWebUI ().getShadeWidth ();
    }

    public void setShadeWidth ( final int shadeWidth )
    {
        getWebUI ().setShadeWidth ( shadeWidth );
    }

    @Override
    public Insets getMargin ()
    {
        return getWebUI ().getMargin ();
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        getWebUI ().setMargin ( margin );
    }

    public void setMargin ( final int top, final int left, final int bottom, final int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
    }

    public void setMargin ( final int spacing )
    {
        setMargin ( spacing, spacing, spacing, spacing );
    }

    public ToolbarStyle getToolbarStyle ()
    {
        return getWebUI ().getToolbarStyle ();
    }

    public void setToolbarStyle ( final ToolbarStyle toolbarStyle )
    {
        getWebUI ().setToolbarStyle ( toolbarStyle );
    }

    public int getSpacing ()
    {
        return getWebUI ().getSpacing ();
    }

    public void setSpacing ( final int spacing )
    {
        getWebUI ().setSpacing ( spacing );
    }

    public Color getTopBgColor ()
    {
        return getWebUI ().getTopBgColor ();
    }

    public void setTopBgColor ( final Color topBgColor )
    {
        getWebUI ().setTopBgColor ( topBgColor );
    }

    public Color getBottomBgColor ()
    {
        return getWebUI ().getBottomBgColor ();
    }

    public void setBottomBgColor ( final Color bottomBgColor )
    {
        getWebUI ().setBottomBgColor ( bottomBgColor );
    }

    public Color getBorderColor ()
    {
        return getWebUI ().getBorderColor ();
    }

    public void setBorderColor ( final Color lowerBorderColor )
    {
        getWebUI ().setBorderColor ( lowerBorderColor );
    }

    public ToolbarLayout getToolbarLayout ()
    {
        return ( ToolbarLayout ) getLayout ();
    }

    public boolean isFloating ()
    {
        return getWebUI ().isFloating ();
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
            catch ( Throwable e )
            {
                e.printStackTrace ();
                setUI ( new WebToolBarUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
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
