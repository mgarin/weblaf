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

package com.alee.extended.checkbox;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.PainterSupport;
import com.alee.laf.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.utils.CompareUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicCheckBoxUI;
import java.awt.*;

/**
 * Custom UI for WebTristateCheckBox component.
 *
 * @author Mikle Garin
 * @author Alexandr Zernov
 */

public class WebTristateCheckBoxUI extends BasicCheckBoxUI implements Styleable, ShapeProvider
{
    /**
     * Component painter.
     */
    protected TristateCheckBoxPainter painter;

    /**
     * Runtime variables.
     */
    protected StyleId styleId = null;
    protected JCheckBox checkBox = null;

    /**
     * Returns an instance of the WebTristateCheckBoxUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebTristateCheckBoxUI
     */
    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebTristateCheckBoxUI ();
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

        // Saving checkbox to local variable
        checkBox = ( JCheckBox ) c;

        // Applying skin
        StyleManager.applySkin ( checkBox );
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
        StyleManager.removeSkin ( checkBox );

        checkBox = null;

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StyleId getStyleId ()
    {
        return styleId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleId ( final StyleId id )
    {
        if ( !CompareUtils.equals ( this.styleId, id ) )
        {
            this.styleId = id;
            StyleManager.applySkin ( checkBox );
        }
    }

    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( checkBox, painter );
    }

    /**
     * Returns checkbox painter.
     *
     * @return checkbox painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets checkbox painter.
     * Pass null to remove checkbox painter.
     *
     * @param painter new checkbox painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( checkBox, new DataRunnable<TristateCheckBoxPainter> ()
        {
            @Override
            public void run ( final TristateCheckBoxPainter newPainter )
            {
                WebTristateCheckBoxUI.this.painter = newPainter;
            }
        }, this.painter, painter, TristateCheckBoxPainter.class, AdaptiveTristateCheckBoxPainter.class );
    }

    /**
     * Paints checkbox.
     *
     * @param g graphics context
     * @param c painted component
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
     * Returns icon bounds.
     *
     * @return icon bounds
     */
    public Rectangle getIconRect ()
    {
        if ( painter != null )
        {
            return painter.getIconRect ();
        }

        return null;
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