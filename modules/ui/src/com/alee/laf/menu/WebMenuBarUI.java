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

import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.api.jdk.Consumer;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuBarUI;
import java.awt.*;

/**
 * Custom UI for {@link JMenuBar} component.
 *
 * @author Mikle Garin
 * @author Alexandr Zernov
 */
public class WebMenuBarUI extends BasicMenuBarUI implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter ( MenuBarPainter.class )
    protected IMenuBarPainter painter;

    /**
     * Preserved old layout.
     */
    protected transient LayoutManager oldLayout;

    /**
     * Returns an instance of the {@link WebMenuBarUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebMenuBarUI}
     */
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebMenuBarUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Installing custom layout
        installLayout ();

        // Applying skin
        StyleManager.installSkin ( menuBar );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( menuBar );

        // Uninstalling custom layout
        uninstallLayout ();

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    /**
     * Installs custom {@link LayoutManager} into {@link JMenuBar}.
     */
    protected void installLayout ()
    {
        oldLayout = menuBar.getLayout ();
        menuBar.setLayout ( createLayout () );
    }

    /**
     * Uninstalls custom {@link LayoutManager} from {@link JMenuBar}.
     */
    protected void uninstallLayout ()
    {
        menuBar.setLayout ( oldLayout );
        oldLayout = null;
    }

    /**
     * Returns custom {@link LayoutManager} for the menubar.
     *
     * @return custom {@link LayoutManager} for the menubar
     */
    protected LayoutManager createLayout ()
    {
        return new MenuBarLayout ();
    }

    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( menuBar, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( menuBar, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( menuBar, painter, enabled );
    }

    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( menuBar );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        PainterSupport.setMargin ( menuBar, margin );
    }

    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( menuBar );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        PainterSupport.setPadding ( menuBar, padding );
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
        PainterSupport.setPainter ( menuBar, new Consumer<IMenuBarPainter> ()
        {
            @Override
            public void accept ( final IMenuBarPainter newPainter )
            {
                WebMenuBarUI.this.painter = newPainter;
            }
        }, this.painter, painter, IMenuBarPainter.class, AdaptiveMenuBarPainter.class );
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

    /**
     * Returns menu bar preferred size.
     *
     * @param c menu bar component
     * @return menu bar preferred size
     */
    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        // return PainterSupport.getPreferredSize ( c, painter );
        return null;
    }
}