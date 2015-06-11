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

package com.alee.laf.scroll;

import com.alee.extended.painter.Painter;
import com.alee.laf.StyleId;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.language.LanguageContainerMethods;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.log.Log;
import com.alee.managers.style.StyleManager;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SizeUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.SizeMethods;

import javax.swing.*;
import java.awt.*;

/**
 * This JScrollPane extension class provides a direct access to WebScrollPaneUI methods.
 * It also provides a few additional constructors and methods to setup the scrollpane.
 * <p/>
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application L&amp;F as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 */

public class WebScrollPane extends JScrollPane implements Styleable, ShapeProvider, SizeMethods<WebScrollPane>, LanguageContainerMethods
{
    /**
     * Constructs new empty scrollpane.
     */
    public WebScrollPane ()
    {
        super ();
    }

    /**
     * Construct new scrollpane with the specified view.
     *
     * @param view scrollpane view component
     */
    public WebScrollPane ( final Component view )
    {
        super ( view );
    }

    /**
     * Construct new empty scrollpane with the specified scroll policies.
     *
     * @param vsbPolicy vertical scroll bar policy
     * @param hsbPolicy horizontal scroll bar policy
     */
    public WebScrollPane ( final int vsbPolicy, final int hsbPolicy )
    {
        super ( vsbPolicy, hsbPolicy );
    }

    /**
     * Construct new scrollpane with the specified view and scroll policies.
     *
     * @param view      scrollpane view component
     * @param vsbPolicy vertical scroll bar policy
     * @param hsbPolicy horizontal scroll bar policy
     */
    public WebScrollPane ( final Component view, final int vsbPolicy, final int hsbPolicy )
    {
        super ( view, vsbPolicy, hsbPolicy );
    }

    /**
     * Constructs new empty scrollpane.
     *
     * @param id style ID
     */
    public WebScrollPane ( final StyleId id )
    {
        super ();
        setStyleId ( id );
    }

    /**
     * Construct new scrollpane with the specified view.
     *
     * @param id   style ID
     * @param view scrollpane view component
     */
    public WebScrollPane ( final StyleId id, final Component view )
    {
        super ( view );
        setStyleId ( id );
    }

    /**
     * Construct new empty scrollpane with the specified scroll policies.
     *
     * @param id        style ID
     * @param vsbPolicy vertical scroll bar policy
     * @param hsbPolicy horizontal scroll bar policy
     */
    public WebScrollPane ( final StyleId id, final int vsbPolicy, final int hsbPolicy )
    {
        super ( vsbPolicy, hsbPolicy );
        setStyleId ( id );
    }

    /**
     * Construct new scrollpane with the specified view and scroll policies.
     *
     * @param id        style ID
     * @param view      scrollpane view component
     * @param vsbPolicy vertical scroll bar policy
     * @param hsbPolicy horizontal scroll bar policy
     */
    public WebScrollPane ( final StyleId id, final Component view, final int vsbPolicy, final int hsbPolicy )
    {
        super ( view, vsbPolicy, hsbPolicy );
        setStyleId ( id );
    }

    /**
     * Returns scrollpane painter.
     *
     * @return scrollpane painter
     */
    public Painter getPainter ()
    {
        return StyleManager.getPainter ( this );
    }

    /**
     * Sets scrollpane painter.
     * Pass null to remove scrollpane painter.
     *
     * @param painter new scrollpane painter
     * @return this scrollpane
     */
    public WebScrollPane setPainter ( final Painter painter )
    {
        StyleManager.setCustomPainter ( this, painter );
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

    /**
     * {@inheritDoc}
     */
    @Override
    public WebScrollBar createHorizontalScrollBar ()
    {
        return new WebScrollPaneBar ( this, WebScrollBar.HORIZONTAL );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebScrollBar createVerticalScrollBar ()
    {
        return new WebScrollPaneBar ( this, WebScrollBar.VERTICAL );
    }

    /**
     * Returns horizontal {@link com.alee.laf.scroll.WebScrollBar} if it is installed in this scroll pane.
     *
     * @return {@link com.alee.laf.scroll.WebScrollBar} or null if it is not installed
     */
    public WebScrollBar getWebHorizontalScrollBar ()
    {
        return ( WebScrollBar ) super.getHorizontalScrollBar ();
    }

    /**
     * Returns vertical {@link com.alee.laf.scroll.WebScrollBar} if it is installed in this scroll pane.
     *
     * @return {@link com.alee.laf.scroll.WebScrollBar} or null if it is not installed
     */
    public WebScrollBar getWebVerticalScrollBar ()
    {
        return ( WebScrollBar ) super.getVerticalScrollBar ();
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
    private WebScrollPaneUI getWebUI ()
    {
        return ( WebScrollPaneUI ) getUI ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebScrollPaneUI ) )
        {
            try
            {
                setUI ( ( WebScrollPaneUI ) ReflectUtils.createInstance ( WebLookAndFeel.scrollPaneUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebScrollPaneUI () );
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
    public WebScrollPane setPreferredWidth ( final int preferredWidth )
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
    public WebScrollPane setPreferredHeight ( final int preferredHeight )
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
    public WebScrollPane setMinimumWidth ( final int minimumWidth )
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
    public WebScrollPane setMinimumHeight ( final int minimumHeight )
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
    public WebScrollPane setMaximumWidth ( final int maximumWidth )
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
    public WebScrollPane setMaximumHeight ( final int maximumHeight )
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
    public WebScrollPane setPreferredSize ( final int width, final int height )
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