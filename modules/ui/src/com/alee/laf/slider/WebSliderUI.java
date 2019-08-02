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

package com.alee.laf.slider;

import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.api.jdk.Consumer;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;

/**
 * Custom UI for {@link JSlider} component.
 *
 * @author Mikle Garin
 * @author Michka Popoff
 * @author Alexandr Zernov
 */
public class WebSliderUI extends BasicSliderUI implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter ( SliderPainter.class )
    protected ISliderPainter painter;

    /**
     * Returns an instance of the {@link WebSliderUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebSliderUI}
     */
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebSliderUI ( ( JSlider ) c );
    }

    /**
     * Constructs new slider UI.
     *
     * @param b slider
     */
    public WebSliderUI ( final JSlider b )
    {
        super ( b );
    }

    @Override
    public void installUI ( final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( slider );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( slider );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( slider, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( slider, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( slider, painter, enabled );
    }

    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( slider );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        PainterSupport.setMargin ( slider, margin );
    }

    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( slider );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        PainterSupport.setPadding ( slider, padding );
    }

    /**
     * Returns slider painter.
     *
     * @return slider painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets slider painter.
     * Pass null to remove slider painter.
     *
     * @param painter new slider painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( slider, new Consumer<ISliderPainter> ()
        {
            @Override
            public void accept ( final ISliderPainter newPainter )
            {
                WebSliderUI.this.painter = newPainter;
            }
        }, this.painter, painter, ISliderPainter.class, AdaptiveSliderPainter.class );
    }

    @Override
    public boolean contains ( final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, painter, x, y );
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.setDragging ( isDragging () );
            painter.paint ( ( Graphics2D ) g, c, this, new Bounds ( c ) );
        }
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ), painter );
    }
}