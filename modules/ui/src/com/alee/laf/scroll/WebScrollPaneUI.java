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

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.LafUtils;
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
     * Style settings.
     */
    protected Color cornerBackground;
    protected Color cornerLineColor;

    public Color getCornerBackground ()
    {
        return cornerBackground;
    }

    public void setCornerBackground ( Color cornerBackground )
    {
        this.cornerBackground = cornerBackground;
        updateCornerColors ();
    }

    public Color getCornerLineColor ()
    {
        return cornerLineColor;
    }

    public void setCornerLineColor ( Color cornerLineColor )
    {
        this.cornerLineColor = cornerLineColor;
        updateCornerColors ();
    }

    /**
     * Component painter.
     */
    @DefaultPainter ( ScrollPanePainter.class )
    protected IScrollPanePainter painter;

    /**
     * Listeners.
     */
    protected PropertyChangeListener propertyChangeListener;

    /**
     * Runtime variables.
     */
    protected Insets margin = null;
    protected Insets padding = null;
    protected Component view = null;
    protected Map<String, JComponent> cornersCache = new HashMap<String, JComponent> ( 4 );

    /**
     * Returns an instance of the WebScrollPaneUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebScrollPaneUI
     */
    @SuppressWarnings ("UnusedParameters")
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

        // Link to view
        final JViewport viewport = scrollpane.getViewport ();
        if ( viewport != null )
        {
            view = viewport.getView ();
        }

        // Scroll bars styling
        StyleId.scrollpaneViewport.at ( scrollpane ).set ( scrollpane.getViewport () );
        StyleId.scrollpaneVerticalBar.at ( scrollpane ).set ( scrollpane.getVerticalScrollBar () );
        StyleId.scrollpaneHorizontalBar.at ( scrollpane ).set ( scrollpane.getHorizontalScrollBar () );

        // Updating scrollpane corner
        updateCorners ();

        // Property change listener
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                if ( evt.getPropertyName ().equals ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY ) )
                {
                    updateCorners ();
                }
                else if ( evt.getPropertyName ().equals ( WebLookAndFeel.VIEWPORT_PROPERTY ) )
                {
                    StyleId.scrollpaneViewport.at ( scrollpane ).set ( scrollpane.getViewport () );
                }
                else if ( evt.getPropertyName ().equals ( WebLookAndFeel.VERTICAL_SCROLLBAR_PROPERTY ) )
                {
                    final JScrollBar vsb = scrollpane.getVerticalScrollBar ();
                    if ( vsb != null )
                    {
                        StyleId.scrollpaneVerticalBar.at ( scrollpane ).set ( vsb );
                    }
                }
                else if ( evt.getPropertyName ().equals ( WebLookAndFeel.HORIZONTAL_SCROLLBAR_PROPERTY ) )
                {
                    final JScrollBar hsb = scrollpane.getHorizontalScrollBar ();
                    if ( hsb != null )
                    {
                        StyleId.scrollpaneHorizontalBar.at ( scrollpane ).set ( hsb );
                    }
                }
            }
        };
        scrollpane.addPropertyChangeListener ( propertyChangeListener );

        // Applying skin
        StyleManager.installSkin ( scrollpane );
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
        StyleManager.uninstallSkin ( scrollpane );

        // Cleaning up listeners
        scrollpane.removePropertyChangeListener ( propertyChangeListener );

        // Removing listener and custom corners
        removeCorners ();

        // Removing link to view
        view = null;

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( scrollpane );
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return StyleManager.setStyleId ( scrollpane, id );
    }

    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( scrollpane, painter );
    }

    @Override
    public Insets getMargin ()
    {
        return margin;
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        PainterSupport.updateBorder ( getPainter () );
    }

    @Override
    public Insets getPadding ()
    {
        return padding;
    }

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
        PainterSupport.setPainter ( scrollpane, new DataRunnable<IScrollPanePainter> ()
        {
            @Override
            public void run ( final IScrollPanePainter newPainter )
            {
                WebScrollPaneUI.this.painter = newPainter;
            }
        }, this.painter, painter, IScrollPanePainter.class, AdaptiveScrollPanePainter.class );
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
     * Updates corner colors.
     */
    protected void updateCornerColors ()
    {
        updateCornerColor ( LOWER_LEADING_CORNER );
        updateCornerColor ( LOWER_TRAILING_CORNER );
        updateCornerColor ( UPPER_TRAILING_CORNER );
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

    /**
     * Returns scroll corner provider.
     *
     * @return scroll corner provider
     */
    protected ScrollCornerProvider getScrollCornerProvider ()
    {
        // Check if component provide corners
        ScrollCornerProvider scp = null;
        if ( view != null )
        {
            if ( view instanceof ScrollCornerProvider )
            {
                scp = ( ScrollCornerProvider ) view;
            }
            else
            {
                final ComponentUI ui = LafUtils.getUI ( view );
                if ( ui != null && ui instanceof ScrollCornerProvider )
                {
                    scp = ( ScrollCornerProvider ) ui;
                }
            }
        }
        return scp;
    }

    /**
     * Updates corner for the specified key.
     *
     * @param key      corner key
     * @param provider scroll corner provider
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
            if ( corner == null )
            {
                final WebScrollPaneCorner wc = new WebScrollPaneCorner ( key );
                if ( cornerBackground != null ) wc.setBackground ( cornerBackground );
                if ( cornerLineColor != null ) wc.setBorderColor ( cornerLineColor );
                corner = wc;
            }
            cornersCache.put ( key, corner );
        }

        scrollpane.setCorner ( key, corner );
    }

    /**
     * Updates corner colors for the specified key.
     *
     * @param key      corner key
     */
    protected void updateCornerColor ( final String key )
    {
        JComponent corner = cornersCache.get ( key );
        if ( corner != null && corner instanceof WebScrollPaneCorner )
        {
            final WebScrollPaneCorner wc = ( WebScrollPaneCorner ) corner;
            if ( cornerBackground != null ) wc.setBackground ( cornerBackground );
            if ( cornerLineColor != null ) wc.setBorderColor ( cornerLineColor );
        }
    }

    @Override
    protected void syncScrollPaneWithViewport ()
    {
        super.syncScrollPaneWithViewport ();

        final JViewport viewport = scrollpane.getViewport ();

        if ( viewport != null )
        {
            final Component newView = viewport.getView ();
            if ( newView != null && !newView.equals ( view ) )
            {
                view = newView;
                updateCorners ();
            }
        }
    }

    @Override
    protected void updateViewport ( final PropertyChangeEvent e )
    {
        super.updateViewport ( e );

        if ( e.getOldValue () != e.getNewValue () )
        {
            syncScrollPaneWithViewport ();
        }
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, Bounds.component.of ( c ), c, this );
        }
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        // return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ), painter );
        return null;
    }
}