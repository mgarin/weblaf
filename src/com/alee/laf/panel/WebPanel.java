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

package com.alee.laf.panel;

import com.alee.extended.painter.Painter;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.language.LanguageContainer;
import com.alee.managers.language.LanguageManager;
import com.alee.utils.ReflectUtils;
import com.alee.utils.laf.ShapeProvider;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * User: mgarin Date: 26.07.11 Time: 13:12
 */

public class WebPanel extends JPanel implements ShapeProvider, LanguageContainer
{
    private int preferredWidth = -1;
    private int minimumWidth = -1;
    private int preferredHeight = -1;
    private int minimumHeight = -1;

    public WebPanel ()
    {
        super ( new BorderLayout () );
    }

    public WebPanel ( boolean decorated )
    {
        super ( new BorderLayout () );
        setUndecorated ( !decorated );
    }

    public WebPanel ( boolean decorated, LayoutManager layout )
    {
        super ( layout );
        setUndecorated ( !decorated );
    }

    public WebPanel ( boolean decorated, Component component )
    {
        super ( new BorderLayout () );
        setUndecorated ( !decorated );
        add ( component, BorderLayout.CENTER );
    }

    public WebPanel ( Component component )
    {
        super ( new BorderLayout () );
        add ( component, BorderLayout.CENTER );
    }

    public WebPanel ( Painter painter )
    {
        super ( new BorderLayout () );
        setPainter ( painter );
    }

    public WebPanel ( LayoutManager layout, Painter painter )
    {
        super ( layout );
        setPainter ( painter );
    }

    public WebPanel ( Painter painter, Component component )
    {
        super ( new BorderLayout () );
        setPainter ( painter );
        add ( component, BorderLayout.CENTER );
    }

    public WebPanel ( LayoutManager layout )
    {
        super ( layout );
    }

    public WebPanel ( LayoutManager layout, boolean isDoubleBuffered )
    {
        super ( layout, isDoubleBuffered );
    }

    public WebPanel ( LayoutManager layout, Component... components )
    {
        super ( layout );
        add ( components );
    }

    /**
     * Additional childs interaction methods
     */

    public void add ( List<? extends Component> components, int index )
    {
        if ( components != null )
        {
            for ( int i = 0; i < components.size (); i++ )
            {
                add ( components.get ( i ), index + i );
            }
        }
    }

    public void add ( List<? extends Component> components, String constraints )
    {
        if ( components != null )
        {
            for ( Component component : components )
            {
                add ( component, constraints );
            }
        }
    }

    public void add ( List<? extends Component> components )
    {
        if ( components != null )
        {
            for ( Component component : components )
            {
                add ( component );
            }
        }
    }

    public void add ( int index, Component... components )
    {
        if ( components != null && components.length > 0 )
        {
            for ( int i = 0; i < components.length; i++ )
            {
                add ( components[ i ], index + i );
            }
        }
    }

    public void add ( String constraints, Component... components )
    {
        if ( components != null && components.length > 0 )
        {
            for ( Component component : components )
            {
                add ( component, constraints );
            }
        }
    }

    public void add ( Component... components )
    {
        if ( components != null && components.length > 0 )
        {
            for ( Component component : components )
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

    public WebPanel setUndecorated ( boolean undecorated )
    {
        getWebUI ().setUndecorated ( undecorated );
        return this;
    }

    public boolean isDrawFocus ()
    {
        return getWebUI ().isDrawFocus ();
    }

    public WebPanel setDrawFocus ( boolean drawFocus )
    {
        getWebUI ().setDrawFocus ( drawFocus );
        return this;
    }

    public Painter getPainter ()
    {
        return getWebUI ().getPainter ();
    }

    public WebPanel setPainter ( Painter painter )
    {
        getWebUI ().setPainter ( painter );
        return this;
    }

    public ShapeProvider getClipProvider ()
    {
        return getWebUI ().getClipProvider ();
    }

    public WebPanel setClipProvider ( ShapeProvider clipProvider )
    {
        getWebUI ().setClipProvider ( clipProvider );
        return this;
    }

    public int getRound ()
    {
        return getWebUI ().getRound ();
    }

    public WebPanel setRound ( int round )
    {
        getWebUI ().setRound ( round );
        return this;
    }

    public int getShadeWidth ()
    {
        return getWebUI ().getShadeWidth ();
    }

    public WebPanel setShadeWidth ( int shadeWidth )
    {
        getWebUI ().setShadeWidth ( shadeWidth );
        return this;
    }

    public Insets getMargin ()
    {
        return getWebUI ().getMargin ();
    }

    public void setMargin ( Insets margin )
    {
        getWebUI ().setMargin ( margin );
    }

    public WebPanel setMargin ( int top, int left, int bottom, int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
        return this;
    }

    public WebPanel setMargin ( int spacing )
    {
        return setMargin ( spacing, spacing, spacing, spacing );
    }

    public Stroke getBorderStroke ()
    {
        return getWebUI ().getBorderStroke ();
    }

    public WebPanel setBorderStroke ( Stroke stroke )
    {
        getWebUI ().setBorderStroke ( stroke );
        return this;
    }

    public boolean isDrawBackground ()
    {
        return getWebUI ().isDrawBackground ();
    }

    public WebPanel setDrawBackground ( boolean drawBackground )
    {
        getWebUI ().setDrawBackground ( drawBackground );
        return this;
    }

    public boolean isWebColored ()
    {
        return getWebUI ().isWebColored ();
    }

    public WebPanel setWebColored ( boolean webColored )
    {
        getWebUI ().setWebColored ( webColored );
        return this;
    }

    public boolean isDrawBottom ()
    {
        return getWebUI ().isDrawBottom ();
    }

    public WebPanel setDrawBottom ( boolean drawBottom )
    {
        getWebUI ().setDrawBottom ( drawBottom );
        return this;
    }

    public boolean isDrawLeft ()
    {
        return getWebUI ().isDrawLeft ();
    }

    public WebPanel setDrawLeft ( boolean drawLeft )
    {
        getWebUI ().setDrawLeft ( drawLeft );
        return this;
    }

    public boolean isDrawRight ()
    {
        return getWebUI ().isDrawRight ();
    }

    public WebPanel setDrawRight ( boolean drawRight )
    {
        getWebUI ().setDrawRight ( drawRight );
        return this;
    }

    public boolean isDrawTop ()
    {
        return getWebUI ().isDrawTop ();
    }

    public WebPanel setDrawTop ( boolean drawTop )
    {
        getWebUI ().setDrawTop ( drawTop );
        return this;
    }

    public WebPanel setDrawSides ( boolean top, boolean left, boolean bottom, boolean right )
    {
        getWebUI ().setDrawSides ( top, left, bottom, right );
        return this;
    }

    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    public WebPanelUI getWebUI ()
    {
        return ( WebPanelUI ) getUI ();
    }

    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebPanelUI ) )
        {
            try
            {
                setUI ( ( WebPanelUI ) ReflectUtils.createInstance ( WebLookAndFeel.panelUI ) );
            }
            catch ( Throwable e )
            {
                e.printStackTrace ();
                setUI ( new WebPanelUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    public int getPreferredWidth ()
    {
        return preferredWidth;
    }

    public WebPanel setPreferredWidth ( int preferredWidth )
    {
        this.preferredWidth = preferredWidth;
        return this;
    }

    public int getMinimumWidth ()
    {
        return minimumWidth;
    }

    public WebPanel setMinimumWidth ( int minimumWidth )
    {
        this.minimumWidth = minimumWidth;
        return this;
    }

    public int getPreferredHeight ()
    {
        return preferredHeight;
    }

    public WebPanel setPreferredHeight ( int preferredHeight )
    {
        this.preferredHeight = preferredHeight;
        return this;
    }

    public int getMinimumHeight ()
    {
        return minimumHeight;
    }

    public WebPanel setMinimumHeight ( int minimumHeight )
    {
        this.minimumHeight = minimumHeight;
        return this;
    }

    public Dimension getPreferredSize ()
    {
        Dimension ps = super.getPreferredSize ();
        if ( preferredWidth != -1 )
        {
            ps.width = preferredWidth;
        }
        else if ( minimumWidth != -1 )
        {
            ps.width = Math.max ( minimumWidth, ps.width );
        }
        if ( preferredHeight != -1 )
        {
            ps.height = preferredHeight;
        }
        else if ( minimumHeight != -1 )
        {
            ps.height = Math.max ( minimumHeight, ps.height );
        }
        return ps;
    }

    /**
     * Language container methods
     */

    /**
     * {@inheritDoc}
     */
    public void setLanguageContainerKey ( String key )
    {
        LanguageManager.registerLanguageContainer ( this, key );
    }

    /**
     * {@inheritDoc}
     */
    public void removeLanguageContainerKey ()
    {
        LanguageManager.unregisterLanguageContainer ( this );
    }

    /**
     * {@inheritDoc}
     */
    public String getLanguageContainerKey ()
    {
        return LanguageManager.getLanguageContainerKey ( this );
    }
}