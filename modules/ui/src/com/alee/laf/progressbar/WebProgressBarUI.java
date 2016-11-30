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

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.CompareUtils;
import com.alee.utils.swing.DataRunnable;

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
 * @author Mikle Garin
 */

public class WebProgressBarUI extends WProgressBarUI implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter ( ProgressBarPainter.class )
    protected IProgressBarPainter painter;

    /**
     * Runtime variables.
     */
    protected Insets margin = null;
    protected Insets padding = null;
    protected JProgressBar progressBar;
    protected EventsHandler eventsHandler;

    /**
     * Returns an instance of the WebProgressBarUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebProgressBarUI
     */
    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebProgressBarUI ();
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    @Override
    public void installUI ( final JComponent c )
    {
        // Saving progress bar reference
        progressBar = ( JProgressBar ) c;

        // Default settings
        installDefaults ();
        installListeners ();

        // Applying skin
        StyleManager.installSkin ( progressBar );
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
        StyleManager.uninstallSkin ( progressBar );

        // Default settings
        uninstallListeners ();
        uninstallDefaults ();

        // Removing progress bar reference
        progressBar = null;
    }

    /**
     * Installs default component settings.
     */
    protected void installDefaults ()
    {
        LookAndFeel.installColorsAndFont ( progressBar, "ProgressBar.background", "ProgressBar.foreground", "ProgressBar.font" );
    }

    /**
     * Uninstalls default component settings.
     */
    protected void uninstallDefaults ()
    {
        LookAndFeel.uninstallBorder ( progressBar );
    }

    /**
     * Installs component listeners.
     */
    protected void installListeners ()
    {
        eventsHandler = new EventsHandler ();
        progressBar.addChangeListener ( eventsHandler );
        progressBar.addPropertyChangeListener ( eventsHandler );
    }

    /**
     * Uninstalls component listeners.
     */
    protected void uninstallListeners ()
    {
        progressBar.removeChangeListener ( eventsHandler );
        progressBar.removePropertyChangeListener ( eventsHandler );
        eventsHandler = null;
    }

    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( progressBar, painter );
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
     * Returns progress bar painter.
     *
     * @return progress bar painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets progress bar painter.
     * Pass null to remove progress bar painter.
     *
     * @param painter new progress bar painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( progressBar, new DataRunnable<IProgressBarPainter> ()
        {
            @Override
            public void run ( final IProgressBarPainter newPainter )
            {
                WebProgressBarUI.this.painter = newPainter;
            }
        }, this.painter, painter, IProgressBarPainter.class, AdaptiveProgressBarPainter.class );
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
            painter.paint ( ( Graphics2D ) g, c, this, new Boundz ( c ) );
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
            if ( CompareUtils.equals ( propertyName, WebLookAndFeel.INDETERMINATE_PROPERTY ) )
            {
                progressBar.repaint ();
            }
        }
    }
}