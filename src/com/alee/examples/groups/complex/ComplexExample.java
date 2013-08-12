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

package com.alee.examples.groups.complex;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.layout.ToolbarLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.painter.DefaultPainter;
import com.alee.extended.painter.TexturePainter;
import com.alee.extended.window.TestFrame;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.hotkey.HotkeyRunnable;
import com.alee.utils.*;
import com.alee.utils.ninepatch.NinePatchIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * User: mgarin Date: 13.12.12 Time: 20:20
 */

public class ComplexExample extends DefaultExample
{
    public String getTitle ()
    {
        return "Custom panel";
    }

    public String getDescription ()
    {
        return "Custom-styled panel";
    }

    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        WebPanel container = new WebPanel ( new TexturePainter ( loadIcon ( "bg1.png" ) ) );

        WebPanel panel = new WebPanel ( new ToolbarLayout ( 0, 0, ToolbarLayout.VERTICAL ) );
        panel.setPainter ( new ShadeBackgroundPainter ( ImageUtils.getBufferedImage ( loadIcon ( "bg2.png" ) ) ) );
        container.add ( panel );

        WebLabel titleLabel = new WebLabel ( "Custom-styled label" )
        {
            protected void paintComponent ( Graphics g )
            {
                super.paintComponent ( g );
                DebugUtils.paintBorderDebugInfo ( g, this );
            }
        };
        titleLabel.setForeground ( Color.WHITE );
        titleLabel.setHorizontalAlignment ( SwingConstants.CENTER );
        SwingUtils.setBoldFont ( titleLabel );
        panel.add ( titleLabel, ToolbarLayout.START );

        WebPanel innerPanel = new WebPanel ( new VerticalFlowLayout ( 15, 15 ) )
        {
            protected void paintComponent ( Graphics g )
            {
                super.paintComponent ( g );
                DebugUtils.paintBorderDebugInfo ( g, this );
            }
        };
        innerPanel.setPainter ( new ShadeBackgroundPainter ( null ) );
        panel.add ( innerPanel, ToolbarLayout.END );

        innerPanel.add ( new WebLabel ( "Card", loadIcon ( "type1.png" ) ) );
        innerPanel.add ( new WebLabel ( "Document", loadIcon ( "type2.png" ) ) );
        innerPanel.add ( new WebLabel ( "Folder", loadIcon ( "type3.png" ) ) );

        WebPanel ppp = new WebPanel ( new ToolbarLayout ( 0, 0, ToolbarLayout.VERTICAL ) );
        ppp.setPainter ( new ShadeBackgroundPainter ( null ) );
        ppp.setShadeWidth ( 20 );
        ppp.add ( new WebLabel ( "123" )
        {
            protected void paintComponent ( Graphics g )
            {
                super.paintComponent ( g );
                //                DebugUtils.paintBorderDebugInfo ( g, this );
            }
        } );
        ppp.add ( new WebLabel ( "345" )
        {
            {
                setPainter ( new ShadeBackgroundPainter ( null ) );
            }

            protected void paintComponent ( Graphics g )
            {
                super.paintComponent ( g );
                //                DebugUtils.paintBorderDebugInfo ( g, this );
            }
        }, ToolbarLayout.END );
        return ppp;
    }

    public static void main ( String[] args )
    {
        WebLookAndFeel.install ();
        final TestFrame tf = TestFrame.show ( new ComplexExample ().getPreview ( null ), 0 );
        HotkeyManager.registerHotkey ( Hotkey.SPACE, new HotkeyRunnable ()
        {
            public void run ( KeyEvent e )
            {
                tf.pack ();
            }
        } );
    }

    public class ShadeBackgroundPainter extends DefaultPainter<JComponent>
    {
        private BufferedImage bg;

        public ShadeBackgroundPainter ( BufferedImage bg )
        {
            super ();
            this.bg = bg;
            setMargin ( 25 );
        }

        public Dimension getPreferredSize ( JComponent c )
        {
            return getShadeIcon ().getPreferredSize ();
        }

        public void paint ( Graphics2D g2d, Rectangle bounds, JComponent c )
        {
            // Shade
            getShadeIcon ().paintIcon ( g2d, bounds );

            // Background
            LafUtils.setupAntialias ( g2d );
            g2d.setPaint ( bg != null ? new TexturePaint ( bg, new Rectangle ( 0, 0, bg.getWidth (), bg.getHeight () ) ) : Color.WHITE );
            g2d.fillRoundRect ( bounds.x + 20, bounds.y + 20, bounds.width - 40, bounds.height - 40, 20, 20 );
        }

        private NinePatchIcon getShadeIcon ()
        {
            return NinePatchUtils.getShadeIcon ( 20, 20, 1f );
        }
    }
}