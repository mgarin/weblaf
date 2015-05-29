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

package com.alee.laf.scroll;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.PainterSupport;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.managers.style.StyleManager;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.BorderMethods;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Custom UI for JScrollBar component.
 *
 * @author Mikle Garin
 */

public class WebScrollBarUI extends BasicScrollBarUI implements Styleable, BorderMethods
{
    /**
     * todo 1. Return painters taken directly from the buttons to avoid inconsistance
     * todo 2. Probably fire additional button-painter change events? Or just leave it for button UIs?
     */

    /**
     * UI style settings.
     */
    protected Insets margin = WebScrollBarStyle.margin;
    protected int scrollBarWidth = WebScrollBarStyle.scrollBarWidth;
    protected boolean paintButtons = WebScrollBarStyle.paintButtons;
    protected boolean paintTrack = WebScrollBarStyle.paintTrack;

    /**
     * Component painters.
     */
    protected ScrollBarPainter painter;
    protected ScrollBarButtonPainter decreaseButtonPainter;
    protected ScrollBarButtonPainter increaseButtonPainter;

    /**
     * Scroll bar listeners.
     */
    protected PropertyChangeListener orientationChangeListener;

    /**
     * Runtime variables.
     */
    protected String styleId = null;

    /**
     * Returns an instance of the WebScrollBarUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebScrollBarUI
     */
    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebScrollBarUI ();
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

        // Default settings
        SwingUtils.setOrientation ( scrollbar );
        SwingUtils.setHandlesEnableStateMark ( scrollbar );

        // Applying skin
        StyleManager.applySkin ( scrollbar );

        // Orientation change listener
        orientationChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateBorder ();
            }
        };
        scrollbar.addPropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, orientationChangeListener );
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Removing listeners
        scrollbar.removePropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, orientationChangeListener );

        // Uninstalling applied skin
        StyleManager.removeSkin ( scrollbar );

        // Uninstalling UI
        super.uninstallUI ( c );
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
        StyleManager.applySkin ( scrollbar );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBorder ()
    {
        LafUtils.updateBorder ( scrollbar, margin, painter );
    }

    /**
     * Returns whether scroll bar arrow buttons should be displayed or not.
     *
     * @return true if scroll bar arrow buttons should be displayed, false otherwise
     */
    public boolean isPaintButtons ()
    {
        return paintButtons;
    }

    /**
     * Sets whether scroll bar arrow buttons should be displayed or not.
     *
     * @param paintButtons whether scroll bar arrow buttons should be displayed or not
     */
    public void setPaintButtons ( final boolean paintButtons )
    {
        this.paintButtons = paintButtons;
        if ( painter != null )
        {
            painter.setPaintButtons ( paintButtons );
        }
    }

    /**
     * Returns whether scroll bar track should be displayed or not.
     *
     * @return true if scroll bar track should be displayed, false otherwise
     */
    public boolean isPaintTrack ()
    {
        return paintTrack;
    }

    /**
     * Sets whether scroll bar track should be displayed or not.
     *
     * @param paintTrack whether scroll bar track should be displayed or not
     */
    public void setPaintTrack ( final boolean paintTrack )
    {
        this.paintTrack = paintTrack;
        if ( painter != null )
        {
            painter.setPaintTrack ( paintTrack );
        }
    }

    /**
     * Returns scroll bar content margin.
     *
     * @return scroll bar content margin
     */
    public Insets getMargin ()
    {
        return margin;
    }

    /**
     * Sets scroll bar content margin.
     *
     * @param margin new scroll bar content margin
     */
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        updateBorder ();
    }

    /**
     * Returns scroll bar width.
     *
     * @return scroll bar width
     */
    public int getScrollBarWidth ()
    {
        return scrollBarWidth;
    }

    /**
     * Sets scroll bar width.
     *
     * @param scrollBarWidth new scroll bar width
     */
    public void setScrollBarWidth ( final int scrollBarWidth )
    {
        this.scrollBarWidth = scrollBarWidth;
        updateBorder ();
    }

    /**
     * Returns scroll bar painter.
     *
     * @return scroll bar painter
     */
    public Painter getPainter ()
    {
        return LafUtils.getAdaptedPainter ( painter );
    }

    /**
     * Sets scroll bar painter.
     * Pass null to remove scroll bar painter.
     *
     * @param painter new scroll bar painter
     */
    public void setPainter ( final Painter painter )
    {
        // Creating adaptive painter if required
        final ScrollBarPainter properPainter =
                LafUtils.getProperPainter ( painter, ScrollBarPainter.class, AdaptiveScrollBarPainter.class );

        // Properly updating painter
        PainterSupport.uninstallPainter ( scrollbar, this.painter );
        final Painter oldPainter = this.painter;
        this.painter = properPainter;
        applyPainterSettings ( properPainter );
        PainterSupport.installPainter ( scrollbar, properPainter );

        // Firing painter change event
        // This is made using reflection because required method is protected within Component class
        LafUtils.firePainterChanged ( scrollbar, oldPainter, properPainter );
    }

    /**
     * Applies UI settings to this specific painter.
     *
     * @param painter scroll bar painter
     */
    private void applyPainterSettings ( final ScrollBarPainter painter )
    {
        if ( painter != null )
        {
            // UI settings
            painter.setPaintButtons ( paintButtons );
            painter.setPaintTrack ( paintTrack );

            // Runtime variables
            painter.setDragged ( isDragging );
            painter.setTrackBounds ( getTrackBounds () );
            painter.setThumbBounds ( getThumbBounds () );
        }
    }

    /**
     * Returns decrease button painter.
     * This the button displayed at top or left side of the scroll bar.
     *
     * @return decrease button painter
     */
    public Painter getDecreaseButtonPainter ()
    {
        return LafUtils.getAdaptedPainter ( decreaseButtonPainter );
    }

    /**
     * Sets decrease button painter.
     * This the button displayed at top or left side of the scroll bar.
     *
     * @param painter new decrease button painter
     */
    public void setDecreaseButtonPainter ( final Painter painter )
    {
        // Creating adaptive painter if required
        final ScrollBarButtonPainter properPainter =
                LafUtils.getProperPainter ( painter, ScrollBarButtonPainter.class, AdaptiveScrollBarButtonPainter.class );

        // Properly updating painter
        this.decreaseButtonPainter = properPainter;
        if ( decrButton != null )
        {
            if ( properPainter != null )
            {
                properPainter.setButtonType ( ScrollBarButtonType.decrease );
                properPainter.setScrollbar ( scrollbar );
            }
            ( ( WebButton ) decrButton ).setPainter ( properPainter );
        }
    }

    /**
     * Returns increase button painter.
     * This the button displayed at bottom or right side of the scroll bar.
     *
     * @return increase button painter
     */
    public Painter getIncreaseButtonPainter ()
    {
        return LafUtils.getAdaptedPainter ( increaseButtonPainter );
    }

    /**
     * Sets increase button painter.
     * This the button displayed at bottom or right side of the scroll bar.
     *
     * @param painter new increase button painter
     */
    public void setIncreaseButtonPainter ( final Painter painter )
    {
        // Creating adaptive painter if required
        final ScrollBarButtonPainter properPainter =
                LafUtils.getProperPainter ( painter, ScrollBarButtonPainter.class, AdaptiveScrollBarButtonPainter.class );

        // Properly updating painter
        this.increaseButtonPainter = properPainter;
        if ( incrButton != null )
        {
            if ( properPainter != null )
            {
                properPainter.setButtonType ( ScrollBarButtonType.increase );
                properPainter.setScrollbar ( scrollbar );
            }
            ( ( WebButton ) incrButton ).setPainter ( properPainter );
        }
    }

    /**
     * Paints scroll bar decorations.
     * The whole painting process is delegated to installed painter class.
     *
     * @param g graphics context
     * @param c scroll bar component
     */
    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.setDragged ( isDragging );
            painter.setTrackBounds ( getTrackBounds () );
            painter.setThumbBounds ( getThumbBounds () );
            painter.paint ( ( Graphics2D ) g, SwingUtils.size ( c ), c );
        }
    }

    /**
     * Installs additional scroll bar components.
     */
    @Override
    protected void installComponents ()
    {
        final WebButton db = new WebButton ( decreaseButtonPainter )
        {
            @Override
            public Dimension getPreferredSize ()
            {
                // The best way (so far) to hide buttons without causing a serious mess in the code
                return painter != null && paintButtons ? super.getPreferredSize () : new Dimension ( 0, 0 );
            }
        };
        db.setFocusable ( false );
        db.setLeftRightSpacing ( 0 );

        decrButton = db;
        scrollbar.add ( decrButton );

        final WebButton ib = new WebButton ( increaseButtonPainter )
        {
            @Override
            public Dimension getPreferredSize ()
            {
                // The best way (so far) to hide buttons without causing a serious mess in the code
                return painter != null && paintButtons ? super.getPreferredSize () : new Dimension ( 0, 0 );
            }
        };
        ib.setFocusable ( false );
        ib.setLeftRightSpacing ( 0 );

        incrButton = ib;
        scrollbar.add ( incrButton );

        // Force the children's enabled state to be updated.
        scrollbar.setEnabled ( scrollbar.isEnabled () );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        final boolean ver = scrollbar.getOrientation () == JScrollBar.VERTICAL;

        // Simple scroll bar preferred size
        Dimension ps = ver ? new Dimension ( scrollBarWidth, 48 ) : new Dimension ( 48, scrollBarWidth );

        // Arrow button preferred sizes
        if ( painter != null && paintButtons && decrButton != null && incrButton != null )
        {
            final Dimension dps = decrButton.getPreferredSize ();
            final Dimension ips = incrButton.getPreferredSize ();
            if ( ver )
            {
                ps.width = Math.max ( ps.width, Math.max ( dps.width, ips.width ) );
            }
            else
            {
                ps.height = Math.max ( ps.height, Math.max ( dps.height, ips.height ) );
            }
        }

        // Insets
        final Insets i = c.getInsets ();
        ps.width += i.left + i.right;
        ps.height += i.top + i.bottom;

        // Background painter preferred size
        if ( painter != null )
        {
            ps = SwingUtils.max ( ps, painter.getPreferredSize ( c ) );
        }

        return ps;
    }
}