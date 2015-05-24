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

package com.alee.extended.button;

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
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

/**
 * Custom UI for WebSplitButton component.
 * This UI is based on WebButtonUI and simply adds a few features.
 *
 * @author Mikle Garin
 */

public class WebSplitButtonUI extends BasicButtonUI implements Styleable, ShapeProvider, SwingConstants
{
    /**
     * Component painter.
     */
    protected SplitButtonPainter painter;

    /**
     * Runtime variables.
     */
    protected String styleId = null;
    protected AbstractButton button = null;

    /**
     * Returns an instance of the WebSplitButtonUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebSplitButtonUI
     */
    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebSplitButtonUI ();
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Saving button reference
        button = ( AbstractButton ) c;

        // Applying skin
        StyleManager.applySkin ( button );
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
        StyleManager.removeSkin ( button );

        // Removing button reference
        button = null;

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
            StyleManager.applySkin ( button );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( button, painter );
    }

    /**
     * Returns whether or not mouse is currently over the split menu button.
     *
     * @return true if mouse is currently over the split menu button, false otherwise
     */
    public boolean isOnSplit ()
    {
        return painter != null && painter.isOnSplit ();
    }

    /**
     * Returns button painter.
     *
     * @return button painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets button painter.
     * Pass null to remove button painter.
     *
     * @param painter new button painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( button, new DataRunnable<SplitButtonPainter> ()
        {
            @Override
            public void run ( final SplitButtonPainter newPainter )
            {
                WebSplitButtonUI.this.painter = newPainter;
            }
        }, this.painter, painter, SplitButtonPainter.class, AdaptiveSplitButtonPainter.class );
    }

    /**
     * Paints button.
     *
     * @param g graphics
     * @param c component
     */
    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            // Painting button
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