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

package com.alee.examples.groups.tooltip;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.panel.GroupPanel;
import com.alee.global.StyleConstants;
import com.alee.laf.label.WebLabel;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.ComponentUpdater;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: mgarin Date: 30.11.12 Time: 17:38
 */

public class CustomContentTooltipExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Custom content tooltip";
    }

    @Override
    public String getDescription ()
    {
        return "Custom Web-styled tooltip with custom content";
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // You can show any kind of content inside the tooltip
        final WebLabel tip = new WebLabel ( "Custom content tooltip", loadIcon ( "web.png" ) );
        TooltipManager.setTooltip ( tip, new WheelImage (), TooltipWay.trailing, 0 ).setLeftRightSpacing ( 0 );
        tip.setMargin ( 4 );

        return new GroupPanel ( tip );
    }

    /**
     * Component with custom animation
     */

    private class WheelImage extends JComponent
    {
        private final ImageIcon wheelIcon;
        private int angle = 0;

        public WheelImage ()
        {
            super ();
            SwingUtils.setOrientation ( this );
            setOpaque ( false );

            // Loading icon
            wheelIcon = loadIcon ( "wheel.png" );

            // Wheel rotation updater
            ComponentUpdater.install ( this, "WheelImage.animator", StyleConstants.animationDelay, new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    angle += 1;
                    repaint ();
                }
            } );
        }

        @Override
        protected void paintComponent ( final Graphics g )
        {
            super.paintComponent ( g );

            final Graphics2D g2d = ( Graphics2D ) g;
            final Object iq = GraphicsUtils.setupImageQuality ( g2d );

            g2d.translate ( wheelIcon.getIconWidth () / 2, wheelIcon.getIconHeight () / 2 );
            if ( angle != 0 )
            {
                g2d.rotate ( angle * Math.PI / 180 );
            }
            g2d.drawImage ( wheelIcon.getImage (), -wheelIcon.getIconWidth () / 2, -wheelIcon.getIconHeight () / 2,
                    wheelIcon.getImageObserver () );

            GraphicsUtils.restoreImageQuality ( g2d, iq );
        }

        @Override
        public Dimension getPreferredSize ()
        {
            return new Dimension ( wheelIcon.getIconWidth (), wheelIcon.getIconHeight () );
        }
    }
}