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
import com.alee.managers.language.LanguageContainerMethods;
import com.alee.managers.language.LanguageManager;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SizeUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.swing.SizeMethods;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * User: mgarin Date: 26.07.11 Time: 13:12
 */

public class WebPanel extends JPanel implements ShapeProvider, SizeMethods<WebPanel>, LanguageContainerMethods
{
    public WebPanel ()
    {
        super ( new BorderLayout () );
    }

    public WebPanel ( final boolean decorated )
    {
        super ( new BorderLayout () );
        setUndecorated ( !decorated );
    }

    public WebPanel ( final boolean decorated, final LayoutManager layout )
    {
        super ( layout );
        setUndecorated ( !decorated );
    }

    public WebPanel ( final boolean decorated, final Component component )
    {
        super ( new BorderLayout () );
        setUndecorated ( !decorated );
        add ( component, BorderLayout.CENTER );
    }

    public WebPanel ( final Component component )
    {
        super ( new BorderLayout () );
        add ( component, BorderLayout.CENTER );
    }

    public WebPanel ( final Painter painter )
    {
        super ( new BorderLayout () );
        setPainter ( painter );
    }

    public WebPanel ( final LayoutManager layout, final Painter painter )
    {
        super ( layout );
        setPainter ( painter );
    }

    public WebPanel ( final Painter painter, final Component component )
    {
        super ( new BorderLayout () );
        setPainter ( painter );
        add ( component, BorderLayout.CENTER );
    }

    public WebPanel ( final LayoutManager layout )
    {
        super ( layout );
    }

    public WebPanel ( final LayoutManager layout, final boolean isDoubleBuffered )
    {
        super ( layout, isDoubleBuffered );
    }

    public WebPanel ( final LayoutManager layout, final Component... components )
    {
        super ( layout );
        add ( components );
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

    public WebPanel setUndecorated ( final boolean undecorated )
    {
        getWebUI ().setUndecorated ( undecorated );
        return this;
    }

    public boolean isDrawFocus ()
    {
        return getWebUI ().isDrawFocus ();
    }

    public WebPanel setDrawFocus ( final boolean drawFocus )
    {
        getWebUI ().setDrawFocus ( drawFocus );
        return this;
    }

    public Painter getPainter ()
    {
        return getWebUI ().getPainter ();
    }

    public WebPanel setPainter ( final Painter painter )
    {
        getWebUI ().setPainter ( painter );
        return this;
    }

    public int getRound ()
    {
        return getWebUI ().getRound ();
    }

    public WebPanel setRound ( final int round )
    {
        getWebUI ().setRound ( round );
        return this;
    }

    public int getShadeWidth ()
    {
        return getWebUI ().getShadeWidth ();
    }

    public WebPanel setShadeWidth ( final int shadeWidth )
    {
        getWebUI ().setShadeWidth ( shadeWidth );
        return this;
    }

    public Insets getMargin ()
    {
        return getWebUI ().getMargin ();
    }

    public void setMargin ( final Insets margin )
    {
        getWebUI ().setMargin ( margin );
    }

    public WebPanel setMargin ( final int top, final int left, final int bottom, final int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
        return this;
    }

    public WebPanel setMargin ( final int spacing )
    {
        return setMargin ( spacing, spacing, spacing, spacing );
    }

    public Stroke getBorderStroke ()
    {
        return getWebUI ().getBorderStroke ();
    }

    public WebPanel setBorderStroke ( final Stroke stroke )
    {
        getWebUI ().setBorderStroke ( stroke );
        return this;
    }

    public boolean isDrawBackground ()
    {
        return getWebUI ().isDrawBackground ();
    }

    public WebPanel setDrawBackground ( final boolean drawBackground )
    {
        getWebUI ().setDrawBackground ( drawBackground );
        return this;
    }

    public boolean isWebColored ()
    {
        return getWebUI ().isWebColored ();
    }

    public WebPanel setWebColored ( final boolean webColored )
    {
        getWebUI ().setWebColored ( webColored );
        return this;
    }

    public boolean isDrawBottom ()
    {
        return getWebUI ().isDrawBottom ();
    }

    public WebPanel setDrawBottom ( final boolean drawBottom )
    {
        getWebUI ().setDrawBottom ( drawBottom );
        return this;
    }

    public boolean isDrawLeft ()
    {
        return getWebUI ().isDrawLeft ();
    }

    public WebPanel setDrawLeft ( final boolean drawLeft )
    {
        getWebUI ().setDrawLeft ( drawLeft );
        return this;
    }

    public boolean isDrawRight ()
    {
        return getWebUI ().isDrawRight ();
    }

    public WebPanel setDrawRight ( final boolean drawRight )
    {
        getWebUI ().setDrawRight ( drawRight );
        return this;
    }

    public boolean isDrawTop ()
    {
        return getWebUI ().isDrawTop ();
    }

    public WebPanel setDrawTop ( final boolean drawTop )
    {
        getWebUI ().setDrawTop ( drawTop );
        return this;
    }

    public WebPanel setDrawSides ( final boolean top, final boolean left, final boolean bottom, final boolean right )
    {
        getWebUI ().setDrawSides ( top, left, bottom, right );
        return this;
    }

    @Override
    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    public WebPanelUI getWebUI ()
    {
        return ( WebPanelUI ) getUI ();
    }

    @Override
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

    /**
     * Size methods.
     */

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
    public WebPanel setPreferredWidth ( final int preferredWidth )
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
    public WebPanel setPreferredHeight ( final int preferredHeight )
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
    public WebPanel setMinimumWidth ( final int minimumWidth )
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
    public WebPanel setMinimumHeight ( final int minimumHeight )
    {
        return SizeUtils.setMinimumHeight ( this, minimumHeight );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize ()
    {
        Dimension ps = SizeUtils.getPreferredSize ( this, super.getPreferredSize () );

        // Fix to take painter preferres size into account
        final Painter painter = getPainter ();
        if ( painter != null )
        {
            ps = SwingUtils.max ( ps, painter.getPreferredSize ( this ) );
        }

        return ps;
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