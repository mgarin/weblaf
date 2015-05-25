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
import com.alee.managers.style.StyleManager;
import com.alee.utils.CompareUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;
import java.awt.*;

/**
 * Custom UI for JScrollPane component.
 *
 * @author Mikle Garin
 */

public class WebScrollPaneUI extends BasicScrollPaneUI implements Styleable, ShapeProvider
{
    /**
     * Component painter.
     */
    protected ScrollPanePainter painter;

    /**
     * Runtime variables.
     */
    protected String styleId = null;

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