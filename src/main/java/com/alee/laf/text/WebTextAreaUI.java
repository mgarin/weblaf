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

package com.alee.laf.text;

import com.alee.extended.painter.Painter;
import com.alee.laf.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextAreaUI;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

/**
 * User: mgarin Date: 17.08.11 Time: 23:01
 */

public class WebTextAreaUI extends BasicTextAreaUI
{
    private Painter painter = WebTextAreaStyle.painter;

    private FocusListener focusListener;
    private PropertyChangeListener marginChangeListener;

    public static ComponentUI createUI ( JComponent c )
    {
        return new WebTextAreaUI ();
    }

    public void installUI ( JComponent c )
    {
        super.installUI ( c );

        final JTextComponent textComponent = getComponent ();

        // Default settings
        SwingUtils.setOrientation ( textComponent );
        textComponent.setOpaque ( true );
        textComponent.setBackground ( Color.WHITE );
        textComponent.setSelectionColor ( StyleConstants.textSelectionColor );
        textComponent.setForeground ( Color.BLACK );
        textComponent.setSelectedTextColor ( Color.BLACK );
        textComponent.setCaretColor ( Color.GRAY );
        textComponent.setMargin ( WebTextAreaStyle.margin );

        // Updating border
        updateBorder ();

        focusListener = new FocusListener ()
        {
            public void focusLost ( FocusEvent e )
            {
                textComponent.repaint ();
            }

            public void focusGained ( FocusEvent e )
            {
                textComponent.repaint ();
            }
        };
        textComponent.addFocusListener ( focusListener );

        marginChangeListener = new PropertyChangeListener ()
        {
            public void propertyChange ( PropertyChangeEvent evt )
            {
                updateBorder ();
            }
        };
        textComponent.addPropertyChangeListener ( WebLookAndFeel.COMPONENT_MARGIN_PROPERTY, marginChangeListener );
    }

    public void uninstallUI ( JComponent c )
    {
        JTextComponent component = getComponent ();
        component.removeFocusListener ( focusListener );
        component.removePropertyChangeListener ( WebLookAndFeel.COMPONENT_MARGIN_PROPERTY, marginChangeListener );

        super.uninstallUI ( c );
    }

    public Painter getPainter ()
    {
        return painter;
    }

    public void setPainter ( Painter painter )
    {
        this.painter = painter;
        getComponent ().setOpaque ( painter == null || painter.isOpaque ( getComponent () ) );
        updateBorder ();
    }

    private void updateBorder ()
    {
        JTextComponent component = getComponent ();
        if ( component != null )
        {
            // Actual margin
            Insets margin = component.getMargin ();
            boolean ltr = component.getComponentOrientation ().isLeftToRight ();
            Insets m = margin == null ? new Insets ( 0, 0, 0, 0 ) :
                    new Insets ( margin.top, ( ltr ? margin.left : margin.right ), margin.bottom, ( ltr ? margin.right : margin.left ) );

            // Applying border
            if ( painter != null )
            {
                // Painter borders
                Insets pi = painter.getMargin ( component );
                m.top += pi.top;
                m.bottom += pi.bottom;
                m.left += ltr ? pi.left : pi.right;
                m.right += ltr ? pi.right : pi.left;
            }

            // Installing border
            component.setBorder ( LafUtils.createWebBorder ( m ) );
        }
    }

    protected void paintBackground ( Graphics g )
    {
        //
    }

    protected void paintSafely ( Graphics g )
    {
        Graphics2D g2d = ( Graphics2D ) g;
        JTextComponent c = getComponent ();

        if ( c.isOpaque () && ( painter == null || !painter.isOpaque ( c ) ) )
        {
            // Paint default background
            g2d.setPaint ( c.getBackground () );
            g2d.fillRect ( 0, 0, c.getWidth (), c.getHeight () );
        }

        if ( painter != null )
        {
            // Use background painter instead of default UI graphics
            painter.paint ( g2d, SwingUtils.size ( c ), c );
        }

        Map hints = SwingUtils.setupTextAntialias ( g2d, c );
        super.paintSafely ( g );
        SwingUtils.restoreTextAntialias ( g2d, hints );
    }

    public Dimension getPreferredSize ( JComponent c )
    {
        Dimension ps = super.getPreferredSize ( c );
        if ( painter != null )
        {
            if ( c.getLayout () != null )
            {
                ps = SwingUtils.max ( ps, c.getLayout ().preferredLayoutSize ( c ) );
            }
            ps = SwingUtils.max ( ps, painter.getPreferredSize ( c ) );
        }
        return ps;
    }
}