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

package com.alee.extended.dock;

import com.alee.extended.window.TestFrame;
import com.alee.laf.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButtonStyle;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.tabbedpane.TabbedPaneStyle;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.laf.text.WebTextArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * User: mgarin Date: 28.06.12 Time: 13:31
 */

public class WebDockablePane extends WebPanel
{
    private static final Color buttonsPaneBackground = new Color ( 233, 236, 240 );

    public WebDockablePane ()
    {
        super ( new DockingPaneLayout () );
    }

    public DockingPaneLayout getActualLayout ()
    {
        return ( DockingPaneLayout ) getLayout ();
    }

    public void addDockableFrame ( WebDockableFrame dockableFrame, String position )
    {
        // todo
    }

    @Override
    protected void paintComponent ( Graphics g )
    {
        super.paintComponent ( g );

        Graphics2D g2d = ( Graphics2D ) g;
        DockingPaneLayout layout = getActualLayout ();
        DockingPaneInfo info = layout.getDockingPaneInfo ();

        int x = info.hasLeftButtons ? info.leftButtonsPaneBounds.x + info.leftButtonsPaneBounds.width : info.rect.x;
        int y = info.hasTopButtons ? info.topButtonsPaneBounds.y + info.topButtonsPaneBounds.height : info.rect.y;
        int x2 = info.hasRightButtons ? info.rightButtonsPaneBounds.x - 1 : info.rect.x + info.rect.width - 1;
        int y2 = info.hasBottomButtons ? info.bottomButtonsPaneBounds.y - 1 : info.rect.y + info.rect.height - 1;

        if ( info.hasTopButtons )
        {
            g2d.setPaint ( buttonsPaneBackground );
            g2d.fill ( info.topButtonsPaneBounds );
            g2d.setPaint ( StyleConstants.darkBorderColor );
            g2d.drawLine ( x, y, x2, y );
        }
        if ( info.hasLeftButtons )
        {
            g2d.setPaint ( buttonsPaneBackground );
            g2d.fill ( info.leftButtonsPaneBounds );
            g2d.setPaint ( StyleConstants.darkBorderColor );
            g2d.drawLine ( x, y, x, y2 );
        }
        if ( info.hasRightButtons )
        {
            g2d.setPaint ( buttonsPaneBackground );
            g2d.fill ( info.rightButtonsPaneBounds );
            g2d.setPaint ( StyleConstants.darkBorderColor );
            g2d.drawLine ( x2, y, x2, y2 );
        }
        if ( info.hasBottomButtons )
        {
            g2d.setPaint ( buttonsPaneBackground );
            g2d.fill ( info.bottomButtonsPaneBounds );
            g2d.setPaint ( StyleConstants.darkBorderColor );
            g2d.drawLine ( x, y2, x2, y2 );
        }
    }

    private static ImageIcon top = new ImageIcon ( WebDockablePane.class.getResource ( "icons/dock_top_.png" ) );
    private static ImageIcon left = new ImageIcon ( WebDockablePane.class.getResource ( "icons/dock_left_.png" ) );
    private static ImageIcon right = new ImageIcon ( WebDockablePane.class.getResource ( "icons/dock_right_.png" ) );
    private static ImageIcon bottom = new ImageIcon ( WebDockablePane.class.getResource ( "icons/dock_bottom_.png" ) );

    public static void main ( String[] args )
    {
        WebButtonStyle.round = 0;
        WebButtonStyle.drawFocus = false;
        WebButtonStyle.rolloverDecoratedOnly = true;
        //        WebButtonStyle.shadeWidth = 0;
        WebLookAndFeel.install ();


        final WebDockablePane pane = new WebDockablePane ();
        //        pane.getActualLayout ().setFramesMargin ( new Insets ( 2, 1, 1, 1 ) );

        pane.add ( new WebToggleButton ( "Top frame", top )
        {
            {
                setSelected ( true );
            }
        }, DockingPaneLayout.TOP_LEFT );
        pane.add ( new WebToggleButton ( "Test 2", top ), DockingPaneLayout.TOP_LEFT );
        pane.add ( new WebToggleButton ( "Test 3", top ), DockingPaneLayout.TOP_RIGHT );
        //
        pane.add ( new WebToggleButton ( left )
        {
            {
                setSelected ( true );
            }
        }, DockingPaneLayout.LEFT_TOP );
        pane.add ( new WebToggleButton ( left ), DockingPaneLayout.LEFT_BOTTOM );
        pane.add ( new WebToggleButton ( left ), DockingPaneLayout.LEFT_BOTTOM );
        //
        pane.add ( new WebToggleButton ( right ), DockingPaneLayout.RIGHT_TOP );
        pane.add ( new WebToggleButton ( right ), DockingPaneLayout.RIGHT_TOP );
        pane.add ( new WebToggleButton ( right ), DockingPaneLayout.RIGHT_TOP );
        pane.add ( new WebToggleButton ( right ), DockingPaneLayout.RIGHT_TOP );
        pane.add ( new WebToggleButton ( right ), DockingPaneLayout.RIGHT_BOTTOM );
        pane.add ( new WebToggleButton ( right ), DockingPaneLayout.RIGHT_BOTTOM );
        //
        //        pane.add ( new WebToggleButton ( "Test 4", bottom ), DockingPaneLayout.BOTTOM_LEFT );
        //        pane.add ( new WebToggleButton ( "Test 5", bottom ), DockingPaneLayout.BOTTOM_RIGHT );
        //        pane.add ( new WebToggleButton ( "Test 6", bottom ), DockingPaneLayout.BOTTOM_RIGHT );

        pane.add ( createTopFrame (), DockingPaneLayout.TOP_FRAME );
        pane.add ( createLeftFrame (), DockingPaneLayout.LEFT_FRAME );
        //        pane.add ( new WebToggleButton ( "RIGHT" ), DockingPaneLayout.RIGHT_FRAME );
        //        pane.add ( new WebToggleButton ( "BOTTOM" ), DockingPaneLayout.BOTTOM_FRAME );

        pane.add ( createContent (), DockingPaneLayout.CONTENT );

        for ( final Component c : pane.getComponents () )
        {
            if ( c instanceof AbstractButton )
            {
                c.addMouseListener ( new MouseAdapter ()
                {
                    @Override
                    public void mousePressed ( MouseEvent e )
                    {
                        if ( SwingUtilities.isMiddleMouseButton ( e ) )
                        {
                            pane.remove ( c );
                            pane.revalidate ();
                            pane.repaint ();
                        }
                    }
                } );
            }
        }

        TestFrame.show ( pane ).center ( 600, 500 );
    }

    private static Component createLeftFrame ()
    {
        WebDockableFrame frame = new WebDockableFrame ( left, "Left frame" );
        frame.setFrameType ( FrameType.left );
        frame.add ( new WebTextArea ( "123" ) );
        //        frame.setPainter ( PopupManager.getPopupPainter ( PopupStyle.lightSmall ) );
        return frame;
    }

    private static Component createTopFrame ()
    {
        WebDockableFrame frame = new WebDockableFrame ( top, "Top frame" );
        frame.setFrameType ( FrameType.top );
        frame.add ( new WebTextArea ( "123", 3, 0 ) );
        //        frame.setPainter ( PopupManager.getPopupPainter ( PopupStyle.lightSmall ) );
        return frame;
    }

    private static Component createContent ()
    {
        WebTabbedPane content = new WebTabbedPane ( WebTabbedPane.TOP );
        content.setTabbedPaneStyle ( TabbedPaneStyle.attached );
        for ( int i = 0; i < 20; i++ )
        {
            content.addTab ( "Tab " + i, new WebLabel () );
        }
        return new WebPanel ( true, content ).setShadeWidth ( 3 );
        //        return new WebPanel ( PopupManager.getPopupPainter ( PopupStyle.lightSmall ), content );
    }
}