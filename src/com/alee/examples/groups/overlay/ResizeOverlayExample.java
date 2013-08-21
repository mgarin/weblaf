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

package com.alee.examples.groups.overlay;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.WebOverlay;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextArea;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * User: mgarin Date: 15.06.12 Time: 12:18
 */

public class ResizeOverlayExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Resize overlay";
    }

    @Override
    public String getDescription ()
    {
        return "Overlay with resize component";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        WebOverlay overlayPanel = new WebOverlay ();

        // Overlayed area
        String text = "Try resizing this area...";
        WebTextArea textArea = new WebTextArea ( text, 3, 20 );
        textArea.setLineWrap ( true );
        textArea.setWrapStyleWord ( true );

        // Scroll pane that will be sized by resize corner
        final WebScrollPane scrollPane = new WebScrollPane ( textArea );
        overlayPanel.setComponent ( scrollPane );

        // Resize corner displayed as overlay
        final JComponent resizer = new JComponent ()
        {
            @Override
            protected void paintComponent ( Graphics g )
            {
                Graphics2D g2d = ( Graphics2D ) g;
                LafUtils.setupAntialias ( g2d );

                g2d.setPaint ( isEnabled () ? Color.GRAY : Color.LIGHT_GRAY );
                if ( getComponentOrientation ().isLeftToRight () )
                {
                    g2d.drawLine ( 0, 10, 10, 0 );
                    g2d.drawLine ( 4, 10, 10, 4 );
                    g2d.drawLine ( 8, 10, 10, 8 );
                }
                else
                {
                    g2d.drawLine ( 11, 10, 1, 0 );
                    g2d.drawLine ( 7, 10, 1, 4 );
                    g2d.drawLine ( 3, 10, 1, 8 );
                }
            }

            @Override
            public Dimension getPreferredSize ()
            {
                return new Dimension ( 12, 12 );
            }
        };
        resizer.setCursor ( Cursor.getPredefinedCursor ( Cursor.SE_RESIZE_CURSOR ) );
        resizer.addPropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( PropertyChangeEvent evt )
            {
                resizer.setCursor ( Cursor.getPredefinedCursor (
                        resizer.getComponentOrientation ().isLeftToRight () ? Cursor.SE_RESIZE_CURSOR : Cursor.SW_RESIZE_CURSOR ) );
            }
        } );

        // Resize adapter
        MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            private Dimension startDim = null;
            private Point start = null;

            @Override
            public void mousePressed ( MouseEvent e )
            {
                if ( resizer.isEnabled () && SwingUtilities.isLeftMouseButton ( e ) )
                {
                    startDim = scrollPane.getSize ();
                    start = MouseInfo.getPointerInfo ().getLocation ();
                }
            }

            @Override
            public void mouseDragged ( MouseEvent e )
            {
                if ( start != null )
                {
                    boolean ltr = resizer.getComponentOrientation ().isLeftToRight ();

                    Point p = MouseInfo.getPointerInfo ().getLocation ();
                    Dimension ps = scrollPane.getLayout ().preferredLayoutSize ( scrollPane );
                    Dimension newPs = new Dimension ( startDim.width + ( ltr ? p.x - start.x : start.x - p.x ),
                            startDim.height + ( p.y - start.y ) * 2 );

                    scrollPane.setPreferredSize ( SwingUtils.max ( ps, newPs ) );
                    scrollPane.revalidate ();
                    scrollPane.repaint ();
                }
            }

            @Override
            public void mouseReleased ( MouseEvent e )
            {
                if ( start != null )
                {
                    startDim = null;
                    start = null;
                }
            }
        };
        resizer.addMouseListener ( mouseAdapter );
        resizer.addMouseMotionListener ( mouseAdapter );

        // Adding overlay
        overlayPanel.addOverlay ( resizer, SwingConstants.TRAILING, SwingConstants.BOTTOM );
        overlayPanel.setOverlayMargin ( 3 );

        return new GroupPanel ( overlayPanel );
    }
}