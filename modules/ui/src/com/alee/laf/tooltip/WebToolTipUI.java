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

package com.alee.laf.tooltip;

import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Custom UI for {@link JToolTip} component.
 *
 * @author Mikle Garin
 */

public class WebToolTipUI extends WToolTipUI implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter ( ToolTipPainter.class )
    protected IToolTipPainter painter;

    /**
     * Runtime variables.
     */
    protected JComponent tooltip = null;
    protected Insets margin = null;
    protected Insets padding = null;

    /**
     * Returns an instance of the WebToolTipUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebToolTipUI
     */
    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebToolTipUI ();
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    @Override
    public void installUI ( final JComponent c )
    {
        // Saving tooltip to local variable
        tooltip = c;

        // Installing default component settings
        installDefaults ( c );

        // Applying skin
        StyleManager.installSkin ( tooltip );
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
        StyleManager.uninstallSkin ( tooltip );

        // Uninstalling default component settings
        uninstallDefaults ( c );

        // Cleaning up reference
        this.tooltip = null;
    }

    /**
     * Installs default component settings.
     *
     * @param c component for this UI
     */
    protected void installDefaults ( final JComponent c )
    {
        if ( SwingUtils.isUIResource ( c.getFont () ) )
        {
            c.setFont ( UIManager.getFont ( "ToolTip.font" ) );
        }
    }

    /**
     * Uninstalls default component settings.
     *
     * @param c component for this UI
     */
    protected void uninstallDefaults ( final JComponent c )
    {
        LookAndFeel.uninstallBorder ( c );
    }

    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( tooltip, painter );
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
     * Returns tooltip painter.
     *
     * @return tooltip painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets tooltip painter.
     * Pass null to remove tooltip painter.
     *
     * @param painter new tooltip painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( tooltip, new DataRunnable<IToolTipPainter> ()
        {
            @Override
            public void run ( final IToolTipPainter newPainter )
            {
                WebToolTipUI.this.painter = newPainter;
            }
        }, this.painter, painter, IToolTipPainter.class, AdaptiveToolTipPainter.class );
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
        return PainterSupport.getPreferredSize ( c, painter );
    }
}