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

package com.alee.laf.progressbar;

import com.alee.api.jdk.Consumer;
import com.alee.api.jdk.Objects;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Custom UI for {@link JProgressBar} component.
 *
 * Basic UI usage have been removed due to:
 * 1. Multiple private things which do not work properly (like progress update on state change)
 * 2. Pointless animator which is not useful as we use our own ones
 * 3. Unnecessary settings initialization
 *
 * @param <C> component type
 * @author Mikle Garin
 */
public class WebProgressBarUI<C extends JProgressBar> extends WProgressBarUI<C> implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter ( ProgressBarPainter.class )
    protected IProgressBarPainter painter;

    /**
     * Runtime variables.
     */
    protected transient EventsHandler eventsHandler;

    /**
     * Returns an instance of the {@link WebProgressBarUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebProgressBarUI}
     */
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebProgressBarUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( progressBar );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( progressBar );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @Override
    protected void installListeners ()
    {
        // Installing default listeners
        super.installListeners ();

        // Installing custom listeners
        eventsHandler = new EventsHandler ();
        progressBar.addChangeListener ( eventsHandler );
        progressBar.addPropertyChangeListener ( eventsHandler );
    }

    @Override
    protected void uninstallListeners ()
    {
        // Uninstalling custom listeners
        progressBar.removeChangeListener ( eventsHandler );
        progressBar.removePropertyChangeListener ( eventsHandler );
        eventsHandler = null;

        // Uninstalling default listeners
        super.uninstallListeners ();
    }

    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( progressBar, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( progressBar, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( progressBar, painter, enabled );
    }

    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( progressBar );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        PainterSupport.setMargin ( progressBar, margin );
    }

    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( progressBar );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        PainterSupport.setPadding ( progressBar, padding );
    }

    /**
     * Returns progress bar painter.
     *
     * @return progress bar painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets progress bar painter.
     * Pass null to remove progress bar painter.
     *
     * @param painter new progress bar painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( progressBar, new Consumer<IProgressBarPainter> ()
        {
            @Override
            public void accept ( final IProgressBarPainter newPainter )
            {
                WebProgressBarUI.this.painter = newPainter;
            }
        }, this.painter, painter, IProgressBarPainter.class, AdaptiveProgressBarPainter.class );
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

    /**
     * Events handler replacing {@link javax.swing.plaf.basic.BasicProgressBarUI.Handler} one.
     */
    protected class EventsHandler implements ChangeListener, PropertyChangeListener
    {
        @Override
        public void stateChanged ( final ChangeEvent e )
        {
            progressBar.repaint ();
        }

        @Override
        public void propertyChange ( final PropertyChangeEvent e )
        {
            final String propertyName = e.getPropertyName ();
            if ( Objects.equals ( propertyName, WebLookAndFeel.INDETERMINATE_PROPERTY ) )
            {
                progressBar.repaint ();
            }
        }
    }
}