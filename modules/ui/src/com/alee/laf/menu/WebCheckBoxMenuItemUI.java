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

package com.alee.laf.menu;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Consumer;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicCheckBoxMenuItemUI;
import java.awt.*;

/**
 * Custom UI for {@link JCheckBoxMenuItem} component.
 *
 * @author Mikle Garin
 */
public class WebCheckBoxMenuItemUI extends BasicCheckBoxMenuItemUI implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter ( CheckBoxMenuItemPainter.class )
    protected ICheckBoxMenuItemPainter painter;

    /**
     * Returns an instance of the {@link WebCheckBoxMenuItemUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebCheckBoxMenuItemUI}
     */
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebCheckBoxMenuItemUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( menuItem );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( menuItem );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @NotNull
    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( menuItem, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( menuItem, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( menuItem, painter, enabled );
    }

    @Nullable
    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( menuItem );
    }

    @Override
    public void setMargin ( @Nullable final Insets margin )
    {
        PainterSupport.setMargin ( menuItem, margin );
    }

    @Nullable
    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( menuItem );
    }

    @Override
    public void setPadding ( @Nullable final Insets padding )
    {
        PainterSupport.setPadding ( menuItem, padding );
    }

    /**
     * Returns menu item painter.
     *
     * @return menu item painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets menu item painter.
     * Pass null to remove menu item painter.
     *
     * @param painter new menu item painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( menuItem, this, new Consumer<ICheckBoxMenuItemPainter> ()
        {
            @Override
            public void accept ( final ICheckBoxMenuItemPainter newPainter )
            {
                WebCheckBoxMenuItemUI.this.painter = newPainter;
            }
        }, this.painter, painter, ICheckBoxMenuItemPainter.class, AdaptiveCheckBoxMenuItemPainter.class );
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