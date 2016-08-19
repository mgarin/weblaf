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

package com.alee.extended.link;

import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Custom UI for {@link WebLink} component.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebLink">How to use WebLink</a>
 * @see WebLink
 */

public class WebLinkUI extends WLinkUI implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter ( LinkPainter.class )
    protected ILinkPainter painter;

    /**
     * Listeners.
     */
    protected MouseAdapter linkExecutionListener;

    /**
     * Runtime variables.
     */
    protected WebLink link;
    protected Insets margin = null;
    protected Insets padding = null;

    /**
     * Returns an instance of the {@link WebLinkUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebLinkUI}
     */
    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebLinkUI ();
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

        // Saving link reference
        link = ( WebLink ) c;

        // Installing default settings
        installDefaults ();

        // Installing actions
        installLinkListeners ();

        // Applying skin
        StyleManager.installSkin ( link );
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
        StyleManager.uninstallSkin ( link );

        // Installing actions
        uninstallLinkListeners ();

        // Removing link reference
        link = null;

        super.uninstallUI ( c );
    }

    /**
     * Installs default component settings.
     */
    protected void installDefaults ()
    {
        link.setFocusable ( false );
        link.setVisitable ( true );
        link.setVisited ( false );
    }

    /**
     * Installs basic link listeners.
     */
    protected void installLinkListeners ()
    {
        linkExecutionListener = new MouseAdapter ()
        {
            private boolean pressed;

            @Override
            public void mousePressed ( final MouseEvent e )
            {
                if ( link.isEnabled () && SwingUtils.isLeftMouseButton ( e ) && Bounds.margin.of ( link ).contains ( e.getPoint () ) )
                {
                    pressed = true;
                    if ( link.isFocusable () )
                    {
                        link.requestFocusInWindow ();
                    }
                }
            }

            @Override
            public void mouseReleased ( final MouseEvent e )
            {
                if ( SwingUtils.isLeftMouseButton ( e ) )
                {
                    if ( link.isEnabled () && pressed && Bounds.margin.of ( link ).contains ( e.getPoint () ) )
                    {
                        link.fireLinkExecuted ();
                    }
                    pressed = false;
                }
            }
        };
        link.addMouseListener ( linkExecutionListener );
    }

    /**
     * Uninstalls basic link listeners.
     */
    protected void uninstallLinkListeners ()
    {
        link.removeMouseListener ( linkExecutionListener );
        linkExecutionListener = null;
    }

    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( link, painter );
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
     * Returns link painter.
     *
     * @return link painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets link painter.
     * Pass null to remove link painter.
     *
     * @param painter new link painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( link, new DataRunnable<ILinkPainter> ()
        {
            @Override
            public void run ( final ILinkPainter newPainter )
            {
                WebLinkUI.this.painter = newPainter;
            }
        }, this.painter, painter, ILinkPainter.class, AdaptiveLinkPainter.class );
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