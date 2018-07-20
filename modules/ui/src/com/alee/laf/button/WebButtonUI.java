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

package com.alee.laf.button;

import com.alee.api.jdk.Consumer;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Custom UI for {@link JButton} component.
 *
 * @param <C> component type
 * @author Mikle Garin
 */
public class WebButtonUI<C extends JButton> extends WButtonUI<C> implements ShapeSupport, MarginSupport, PaddingSupport, SwingConstants
{
    /**
     * Component painter.
     */
    @DefaultPainter ( ButtonPainter.class )
    protected IButtonPainter painter;

    /**
     * Returns an instance of the {@link WebButtonUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebButtonUI}
     */
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebButtonUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( button );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( button );

        super.uninstallUI ( c );
    }

    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( button, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( button, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( button, painter, enabled );
    }

    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( button );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        PainterSupport.setMargin ( button, margin );
    }

    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( button );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        PainterSupport.setPadding ( button, padding );
    }

    /**
     * Returns button painter.
     *
     * @return button painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets button painter.
     * Pass null to remove button painter.
     *
     * @param painter new button painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( button, new Consumer<IButtonPainter> ()
        {
            @Override
            public void accept ( final IButtonPainter newPainter )
            {
                WebButtonUI.this.painter = newPainter;
            }
        }, this.painter, painter, IButtonPainter.class, AdaptiveButtonPainter.class );
    }

    @Override
    public boolean contains ( final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, painter, x, y );
    }

    @Override
    public int getBaseline ( final JComponent c, final int width, final int height )
    {
        return PainterSupport.getBaseline ( c, this, painter, width, height );
    }

    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( final JComponent c )
    {
        return PainterSupport.getBaselineResizeBehavior ( c, this, painter );
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, c, this, new Bounds ( c ) );
        }
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, painter );
    }
}