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
import com.alee.extended.painter.AbstractPainter;
import com.alee.extended.painter.TexturePainter;
import com.alee.extended.window.TestFrame;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.hotkey.HotkeyRunnable;
import com.alee.utils.DebugUtils;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.ImageUtils;
import com.alee.utils.NinePatchUtils;
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
    @Override
    public String getTitle ()
    {
        return "Custom panel";
    }

    @Override
    public String getDescription ()
    {
        return "Custom-styled panel";
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        final WebPanel container = new WebPanel ( new TexturePainter ( loadIcon ( "bg1.png" ) ) );

        final WebPanel panel = new WebPanel ( new ToolbarLayout ( 0, 0, ToolbarLayout.VERTICAL ) );
        panel.setPainter ( new ShadeBackgroundPainter ( ImageUtils.getBufferedImage ( loadIcon ( "bg2.png" ) ) ) );
        container.add ( panel );

        final WebLabel titleLabel = new WebLabel ( "Custom-styled label" )
        {
            @Override
            protected void paintComponent ( final Graphics g )
            {
                super.paintComponent ( g );
                DebugUtils.paintBorderDebugInfo ( g, this );
            }
        };
        titleLabel.setBoldFont ();
        titleLabel.setForeground ( Color.WHITE );
        titleLabel.setHorizontalAlignment ( SwingConstants.CENTER );
        panel.add ( titleLabel, ToolbarLayout.START );

        final WebPanel innerPanel = new WebPanel ( new VerticalFlowLayout ( 15, 15 ) )
        {
            @Override
            protected void paintComponent ( final Graphics g )
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

        final WebPanel ppp = new WebPanel ( new ToolbarLayout ( 0, 0, ToolbarLayout.VERTICAL ) );
        ppp.setPainter ( new ShadeBackgroundPainter ( null ) );
        ppp.setShadeWidth ( 20 );
        ppp.add ( new WebLabel ( "123" ) );
        ppp.add ( new WebLabel ( "345" )
        {
            {
                setPainter ( new ShadeBackgroundPainter ( null ) );
            }
        }, ToolbarLayout.END );
        return ppp;
    }

    public static void main ( final String[] args )
    {
        WebLookAndFeel.install ();
        final TestFrame tf = TestFrame.show ( new ComplexExample ().getPreview ( null ), 0 );
        HotkeyManager.registerHotkey ( Hotkey.SPACE, new HotkeyRunnable ()
        {
            @Override
            public void run ( final KeyEvent e )
            {
                tf.pack ();
            }
        } );
    }

    /**
     * Custom shade background painter.
     */
    public class ShadeBackgroundPainter extends AbstractPainter<JComponent>
    {
        /**
         * Background image.
         */
        protected BufferedImage bg;

        /**
         * Constructs new shade background painter.
         *
         * @param bg background image
         */
        public ShadeBackgroundPainter ( final BufferedImage bg )
        {
            super ();
            this.bg = bg;
            setMargin ( 25 );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Dimension getPreferredSize ( final JComponent c )
        {
            return getShadeIcon ().getPreferredSize ();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void paint ( final Graphics2D g2d, final Rectangle bounds, final JComponent c )
        {
            // Shade
            getShadeIcon ().paintIcon ( g2d, bounds );

            // Background
            final Object aa = GraphicsUtils.setupAntialias ( g2d );
            g2d.setPaint ( bg != null ? new TexturePaint ( bg, new Rectangle ( 0, 0, bg.getWidth (), bg.getHeight () ) ) : Color.WHITE );
            g2d.fillRoundRect ( bounds.x + 20, bounds.y + 20, bounds.width - 40, bounds.height - 40, 20, 20 );
            GraphicsUtils.restoreAntialias ( g2d, aa );
        }

        /**
         * Returns shade icon.
         *
         * @return shade icon
         */
        protected NinePatchIcon getShadeIcon ()
        {
            return NinePatchUtils.getShadeIcon ( 20, 20, 1f );
        }
    }
}