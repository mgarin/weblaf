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

import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;

/**
 * Custom UI for WebStyledLabel component.
 *
 * @author Mikle Garin
 */

public class WebStyledLabelUI extends BasicLabelUI implements Styleable, ShapeProvider, MarginSupport, PaddingSupport, SwingConstants
{
    /**
     * Component painter.
     */
    @DefaultPainter ( StyledLabelPainter.class )
    protected IStyledLabelPainter painter;

    /**
     * Runtime variables.
     */
    protected WebStyledLabel label;
    protected Insets margin = null;
    protected Insets padding = null;

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
        StyleManager.installSkin ( label );
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
        StyleManager.uninstallSkin ( label );

        // Removing label reference
        label = null;

        super.uninstallUI ( c );
    }

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

    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( label );
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return StyleManager.setStyleId ( label, id );
    }

    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( label, painter );
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
     * Returns label painter.
     *
     * @return label painter
     */
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
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( label, new DataRunnable<IStyledLabelPainter> ()
        {
            @Override
            public void run ( final IStyledLabelPainter newPainter )
            {
                WebStyledLabelUI.this.painter = newPainter;
            }
        }, this.painter, painter, IStyledLabelPainter.class, AdaptiveStyledLabelPainter.class );
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