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
import com.alee.managers.style.data.PainterStyle;
import com.alee.utils.LafUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.BorderMethods;
import com.alee.utils.swing.PainterMethods;

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

public class WebScrollBarUI extends BasicScrollBarUI implements BorderMethods, PainterMethods
{
    /**
     * Component style settings.
     */
    protected Insets margin = WebScrollBarStyle.margin;
    protected int scrollBarWidth = WebScrollBarStyle.scrollBarWidth;

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
     * Returns an instance of the WebScrollBarUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebScrollBarUI
     */
    @SuppressWarnings ( "UnusedParameters" )
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

        // Proper enabled state handling
        scrollbar.putClientProperty ( SwingUtils.HANDLES_ENABLE_STATE, true );

        // Default settings
        SwingUtils.setOrientation ( scrollbar );

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

        super.uninstallUI ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBorder ()
    {
        if ( scrollbar != null )
        {
            // Preserve old borders
            if ( SwingUtils.isPreserveBorders ( scrollbar ) )
            {
                return;
            }

            // Actual margin
            final boolean ltr = scrollbar.getComponentOrientation ().isLeftToRight ();
            final Insets m = new Insets ( margin.top, ltr ? margin.left : margin.right, margin.bottom, ltr ? margin.right : margin.left );

            // Calculating additional borders
            if ( painter != null )
            {
                // Painter borders
                final Insets pi = painter.getMargin ( scrollbar );
                m.top += pi.top;
                m.left += ltr ? pi.left : pi.right;
                m.bottom += pi.bottom;
                m.right += ltr ? pi.right : pi.left;
            }

            // Installing border
            scrollbar.setBorder ( LafUtils.createWebBorder ( m ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Painter createPainter ( final PainterStyle style )
    {
        final String styleId = style.getId ();
        if ( styleId.equals ( "painter" ) )
        {
            return ReflectUtils.createInstanceSafely ( style.getPainterClass () );
        }
        else if ( styleId.equals ( "decrease-button-painter" ) )
        {
            return ReflectUtils.createInstanceSafely ( style.getPainterClass (), scrollbar, ScrollBarButtonType.decrease );
        }
        else if ( styleId.equals ( "increase-button-painter" ) )
        {
            return ReflectUtils.createInstanceSafely ( style.getPainterClass (), scrollbar, ScrollBarButtonType.increase );
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void installPainter ( final Painter painter, final PainterStyle style )
    {
        final String styleId = style.getId ();
        if ( styleId.equals ( "painter" ) )
        {
            setPainter ( ( ScrollBarPainter ) painter );
        }
        else if ( styleId.equals ( "decrease-button-painter" ) )
        {
            setDecreaseButtonPainter ( ( ScrollBarButtonPainter ) painter );
        }
        else if ( styleId.equals ( "increase-button-painter" ) )
        {
            setIncreaseButtonPainter ( ( ScrollBarButtonPainter ) painter );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uninstallPainters ()
    {
        setPainter ( null );
        setDecreaseButtonPainter ( null );
        setIncreaseButtonPainter ( null );
    }

    /**
     * Returns whether scroll bar arrow buttons should be displayed or not.
     *
     * @return true if scroll bar arrow buttons should be displayed, false otherwise
     */
    public boolean isButtonsVisible ()
    {
        return painter.isButtonsVisible ();
    }

    /**
     * Sets whether scroll bar arrow buttons should be displayed or not.
     *
     * @param visible whether scroll bar arrow buttons should be displayed or not
     */
    public void setButtonsVisible ( final boolean visible )
    {
        painter.setButtonsVisible ( visible );
    }

    /**
     * Returns whether scroll bar track should be displayed or not.
     *
     * @return true if scroll bar track should be displayed, false otherwise
     */
    public boolean isDrawTrack ()
    {
        return painter.isDrawTrack ();
    }

    /**
     * Sets whether scroll bar track should be displayed or not.
     *
     * @param draw whether scroll bar track should be displayed or not
     */
    public void setDrawTrack ( final boolean draw )
    {
        painter.setDrawTrack ( draw );
    }

    /**
     * Returns scroll bar track border color.
     *
     * @return scroll bar track border color
     */
    public Color getTrackBorderColor ()
    {
        return painter.getTrackBorderColor ();
    }

    /**
     * Sets scroll bar track border color.
     *
     * @param color new scroll bar track border color
     */
    public void setTrackBorderColor ( final Color color )
    {
        painter.setTrackBorderColor ( color );
    }

    /**
     * Returns scroll bar track background color.
     *
     * @return scroll bar track background color
     */
    public Color getTrackBackgroundColor ()
    {
        return painter.getTrackBackgroundColor ();
    }

    /**
     * Sets scroll bar track background color.
     *
     * @param color new scroll bar track background color
     */
    public void setTrackBackgroundColor ( final Color color )
    {
        painter.setTrackBackgroundColor ( color );
    }

    /**
     * Returns scroll bar thumb border color.
     *
     * @return scroll bar thumb border color
     */
    public Color getThumbBorderColor ()
    {
        return painter.getThumbBorderColor ();
    }

    /**
     * Sets scroll bar thumb border color.
     *
     * @param color new scroll bar thumb border color
     */
    public void setThumbBorderColor ( final Color color )
    {
        painter.setThumbBorderColor ( color );
    }

    /**
     * Returns scroll bar thumb background color.
     *
     * @return scroll bar thumb background color
     */
    public Color getThumbBackgroundColor ()
    {
        return painter.getThumbBackgroundColor ();
    }

    /**
     * Sets scroll bar thumb background color.
     *
     * @param color new scroll bar thumb background color
     */
    public void setThumbBackgroundColor ( final Color color )
    {
        painter.setThumbBackgroundColor ( color );
    }

    /**
     * Returns scroll bar thumb disabled border color.
     *
     * @return scroll bar thumb disabled border color
     */
    public Color getThumbDisabledBorderColor ()
    {
        return painter.getThumbDisabledBorderColor ();
    }

    /**
     * Sets scroll bar thumb disabled border color.
     *
     * @param color new scroll bar thumb disabled border color
     */
    public void setThumbDisabledBorderColor ( final Color color )
    {
        painter.setThumbDisabledBorderColor ( color );
    }

    /**
     * Returns scroll bar thumb disabled background color.
     *
     * @return scroll bar thumb disabled background color
     */
    public Color getThumbDisabledBackgroundColor ()
    {
        return painter.getThumbDisabledBackgroundColor ();
    }

    /**
     * Sets scroll bar thumb disabled background color.
     *
     * @param color new scroll bar thumb disabled background color
     */
    public void setThumbDisabledBackgroundColor ( final Color color )
    {
        painter.setThumbDisabledBackgroundColor ( color );
    }

    /**
     * Returns scroll bar thumb rollover border color.
     *
     * @return scroll bar thumb rollover border color
     */
    public Color getThumbRolloverBorderColor ()
    {
        return painter.getThumbRolloverBorderColor ();
    }

    /**
     * Sets scroll bar thumb rollover border color.
     *
     * @param color new scroll bar thumb rollover border color
     */
    public void setThumbRolloverBorderColor ( final Color color )
    {
        painter.setThumbRolloverBorderColor ( color );
    }

    /**
     * Returns scroll bar thumb rollover background color.
     *
     * @return scroll bar thumb rollover background color
     */
    public Color getThumbRolloverBackgroundColor ()
    {
        return painter.getThumbRolloverBackgroundColor ();
    }

    /**
     * Sets scroll bar thumb rollover background color.
     *
     * @param color new scroll bar thumb rollover background color
     */
    public void setThumbRolloverBackgroundColor ( final Color color )
    {
        painter.setThumbRolloverBackgroundColor ( color );
    }

    /**
     * Returns scroll bar thumb pressed/dragged border color.
     *
     * @return scroll bar thumb pressed/dragged border color
     */
    public Color getThumbPressedBorderColor ()
    {
        return painter.getThumbPressedBorderColor ();
    }

    /**
     * Sets scroll bar thumb pressed/dragged border color.
     *
     * @param color new scroll bar thumb pressed/dragged border color1
     */
    public void setThumbPressedBorderColor ( final Color color )
    {
        painter.setThumbPressedBorderColor ( color );
    }

    /**
     * Returns scroll bar thumb pressed/dragged background color.
     *
     * @return scroll bar thumb pressed/dragged background color
     */
    public Color getThumbPressedBackgroundColor ()
    {
        return painter.getThumbPressedBackgroundColor ();
    }

    /**
     * Sets scroll bar thumb pressed/dragged background color.
     *
     * @param color new scroll bar thumb pressed/dragged background color
     */
    public void setThumbPressedBackgroundColor ( final Color color )
    {
        painter.setThumbPressedBackgroundColor ( color );
    }

    /**
     * Returns scroll bar thumb corners rounding.
     *
     * @return scroll bar thumb corners rounding
     */
    public int getThumbRound ()
    {
        return painter.getThumbRound ();
    }

    /**
     * Sets scroll bar thumb corners rounding.
     *
     * @param round new scroll bar thumb corners rounding
     */
    public void setThumbRound ( final int round )
    {
        painter.setThumbRound ( round );
    }

    /**
     * Returns scroll bar thumb margin.
     *
     * @return scroll bar thumb margin
     */
    public Insets getThumbMargin ()
    {
        return painter.getThumbMargin ();
    }

    /**
     * Sets scroll bar thumb margin.
     *
     * @param margin new scroll bar thumb margin
     */
    public void setThumbMargin ( final Insets margin )
    {
        painter.setThumbMargin ( margin );
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
    public ScrollBarPainter getPainter ()
    {
        return painter;
    }

    /**
     * Sets scroll bar painter.
     * Pass null to remove scroll bar painter.
     *
     * @param painter new scroll bar painter
     */
    public void setPainter ( final ScrollBarPainter painter )
    {
        PainterSupport.uninstallPainter ( scrollbar, this.painter );
        this.painter = painter;
        PainterSupport.installPainter ( scrollbar, this.painter );
    }

    /**
     * Returns decrease button painter.
     * This the button displayed at top or left side of the scroll bar.
     *
     * @return decrease button painter
     */
    public ScrollBarButtonPainter getDecreaseButtonPainter ()
    {
        return decreaseButtonPainter;
    }

    /**
     * Sets decrease button painter.
     * This the button displayed at top or left side of the scroll bar.
     *
     * @param painter new decrease button painter
     */
    public void setDecreaseButtonPainter ( final ScrollBarButtonPainter painter )
    {
        this.decreaseButtonPainter = painter;
        if ( decrButton != null )
        {
            ( ( WebButton ) decrButton ).setPainter ( painter );
        }
    }

    /**
     * Returns increase button painter.
     * This the button displayed at bottom or right side of the scroll bar.
     *
     * @return increase button painter
     */
    public ScrollBarButtonPainter getIncreaseButtonPainter ()
    {
        return increaseButtonPainter;
    }

    /**
     * Sets increase button painter.
     * This the button displayed at bottom or right side of the scroll bar.
     *
     * @param painter new increase button painter
     */
    public void setIncreaseButtonPainter ( final ScrollBarButtonPainter painter )
    {
        this.increaseButtonPainter = painter;
        if ( incrButton != null )
        {
            ( ( WebButton ) incrButton ).setPainter ( painter );
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
            painter.paint ( ( Graphics2D ) g, SwingUtils.size ( scrollbar ), scrollbar );
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
                return painter != null && painter.buttonsVisible ? super.getPreferredSize () : new Dimension ( 0, 0 );
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
                return painter != null && painter.buttonsVisible ? super.getPreferredSize () : new Dimension ( 0, 0 );
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
        if ( painter != null && painter.buttonsVisible && decrButton != null && incrButton != null )
        {
            final Dimension dps = decrButton.getPreferredSize ();
            final Dimension ips = decrButton.getPreferredSize ();
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