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

package com.alee.extended.label;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.PainterSupport;
import com.alee.laf.label.WebLabelUI;
import com.alee.managers.style.StyleManager;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;

public class WebStyledLabelUI extends WebLabelUI implements Styleable, SwingConstants
{
    /**
     * Component painter.
     */
    protected StyledLabelPainter painter;

    /**
     * Runtime variables.
     */
    protected String styleId = null;
    protected WebStyledLabel label;

    /**
     * Returns an instance of the WebStyledLabelUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebStyledLabelUI
     */
    @SuppressWarnings ( { "UnusedDeclaration" } )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebStyledLabelUI ();
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

        // Saving label reference
        label = ( WebStyledLabel ) c;

        // Applying skin
        StyleManager.applySkin ( label );
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
        StyleManager.removeSkin ( label );

        // Removing label reference
        label = null;

        super.uninstallUI ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void propertyChange ( final PropertyChangeEvent e )
    {
        super.propertyChange ( e );

        // Updating text ranges
        if ( WebStyledLabel.PROPERTY_STYLE_RANGE.equals ( e.getPropertyName () ) )
        {
            if ( painter != null )
            {
                painter.updateTextRanges ();
            }
        }
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
        this.styleId = id;
        StyleManager.applySkin ( label );
    }

    /**
     * Returns label painter.
     *
     * @return label painter
     */
    @Override
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets label painter.
     * Pass null to remove label painter.
     *
     * @param painter new label painter
     */
    @Override
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( label, new DataRunnable<StyledLabelPainter> ()
        {
            @Override
            public void run ( final StyledLabelPainter newPainter )
            {
                WebStyledLabelUI.this.painter = newPainter;
            }
        }, this.painter, painter, StyledLabelPainter.class, AdaptiveStyledLabelPainter.class );
    }

    /**
     * Paints label.
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