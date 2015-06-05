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
import com.alee.extended.painter.PainterSupport;
import com.alee.laf.Styles;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.StyleManager;
import com.alee.managers.style.skin.web.WebScrollPaneCorner;
import com.alee.utils.CompareUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.MarginSupport;
import com.alee.utils.laf.PaddingSupport;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom UI for JScrollPane component.
 *
 * @author Mikle Garin
 */

public class WebScrollPaneUI extends BasicScrollPaneUI implements Styleable, ShapeProvider, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    protected ScrollPanePainter painter;

    /**
     * Listeners.
     */
    protected PropertyChangeListener propertyChangeListener;

    /**
     * Runtime variables.
     */
    protected String styleId = null;
    protected Insets margin = null;
    protected Insets padding = null;
    protected Map<String, JComponent> cornersCache = new HashMap<String, JComponent> ( 4 );

    /**
     * Returns an instance of the WebScrollPaneUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebScrollPaneUI
     */
    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebScrollPaneUI ();
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    @Override
    public void installUI ( final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Scroll bars styling
        LafUtils.setVerticalScrollBarStyleId ( scrollpane, Styles.scrollpaneVerticalBar );
        LafUtils.setHorizontalScrollBarStyleId ( scrollpane, Styles.scrollpaneHorizontalBar );

        // Updating scrollpane corner
        updateCorners ();

        // Orientation change listener
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateCorners ();
            }
        };
        c.addPropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, propertyChangeListener );

        // Applying skin
        StyleManager.applySkin ( scrollpane );
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.removeSkin ( scrollpane );

        // Removing listener and custom corners
        removeCorners ();

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleId ()
    {
        return styleId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleId ( final String id )
    {
        if ( !CompareUtils.equals ( this.styleId, id ) )
        {
            this.styleId = id;
            StyleManager.applySkin ( scrollpane );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( scrollpane, painter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getMargin ()
    {
        return margin;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        PainterSupport.updateBorder ( getPainter () );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getPadding ()
    {
        return padding;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPadding ( final Insets padding )
    {
        this.padding = padding;
        PainterSupport.updateBorder ( getPainter () );
    }

    /**
     * Returns panel painter.
     *
     * @return panel painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets scroll pane painter.
     * Pass null to remove scroll pane painter.
     *
     * @param painter new scroll pane painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( scrollpane, new DataRunnable<ScrollPanePainter> ()
        {
            @Override
            public void run ( final ScrollPanePainter newPainter )
            {
                WebScrollPaneUI.this.painter = newPainter;
            }
        }, this.painter, painter, ScrollPanePainter.class, AdaptiveScrollPanePainter.class );
    }

    /**
     * Updates custom scrollpane corners.
     */
    protected void updateCorners ()
    {
        final ScrollCornerProvider scp = getScrollCornerProvider ();
        updateCorner ( LOWER_LEADING_CORNER, scp );
        updateCorner ( LOWER_TRAILING_CORNER, scp );
        updateCorner ( UPPER_TRAILING_CORNER, scp );
    }

    /**
     * Removes custom scrollpane corners.
     */
    protected void removeCorners ()
    {
        for ( final Component corner : cornersCache.values () )
        {
            if ( corner != null )
            {
                scrollpane.remove ( corner );
            }
        }

        cornersCache.clear ();
    }

    protected ScrollCornerProvider getScrollCornerProvider ()
    {
        ScrollCornerProvider scp = null;

        final JViewport vp = scrollpane.getViewport ();
        if ( vp != null )
        {
            final Component comp = vp.getView ();

            // Check if component provide corners
            if ( comp != null )
            {
                if ( comp instanceof ScrollCornerProvider )
                {
                    scp = ( ScrollCornerProvider ) comp;
                }
                else
                {
                    final ComponentUI ui = LafUtils.getUI ( comp );
                    if ( ui != null && ui instanceof ScrollCornerProvider )
                    {
                        scp = ( ScrollCornerProvider ) ui;
                    }
                }
            }
        }

        return scp;
    }

    /**
     * Returns corner for key.
     */
    protected void updateCorner ( final String key, final ScrollCornerProvider provider )
    {
        JComponent corner = cornersCache.get ( key );
        if ( corner == null )
        {
            if ( provider != null )
            {
                corner = provider.getCorner ( key );
            }
            else
            {
                corner = new WebScrollPaneCorner ( key );
            }
            cornersCache.put ( key, corner );
        }

        if ( corner != null )
        {
            scrollpane.setCorner ( key, corner );
        }
    }

    /**
     * Paints scrollpane.
     *
     * @param g graphics
     * @param c component
     */
    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, SwingUtils.size ( c ), c, this );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ), painter );
    }
}