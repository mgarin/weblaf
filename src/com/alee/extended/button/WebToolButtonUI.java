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

package com.alee.extended.button;

import com.alee.laf.StyleConstants;
import com.alee.utils.ImageUtils;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;

/**
 * User: mgarin Date: 16.09.11 Time: 16:18
 */

public class WebToolButtonUI extends BasicButtonUI
{
    private int iconSize = 16;
    private int selectedIconSize = 24;

    private WebTimer animator;

    public void installUI ( JComponent c )
    {
        super.installUI ( c );

        if ( c instanceof JToggleButton )
        {
            final JToggleButton toggleButton = ( JToggleButton ) c;
            toggleButton.addItemListener ( new ItemListener ()
            {
                public void itemStateChanged ( ItemEvent e )
                {
                    if ( animator != null && animator.isRunning () )
                    {
                        animator.stop ();
                    }
                    if ( toggleButton.isSelected () && toggleButton.getIcon () != null )
                    {
                        final FloatSpring fs = new FloatSpring ( 16 );
                        fs.setPosition ( 16 );
                        animator = new WebTimer ( "WebToolButtonUI.animator", StyleConstants.animationDelay, new ActionListener ()
                        {
                            private int timeLeft = 0;

                            public void actionPerformed ( ActionEvent e )
                            {
                                timeLeft += StyleConstants.animationDelay;
                                fs.update ( 32, ( float ) timeLeft / 1000 );

                                iconSize = Math.round ( fs.getPosition () );
                                toggleButton.repaint ();

                                if ( Math.round ( fs.getPosition () ) == 32 )
                                {
                                    animator.stop ();
                                }
                            }
                            //                            private int waveCount = 0;
                            //
                            //                            public void actionPerformed ( ActionEvent e )
                            //                            {
                            //                                if ( waveCount < 4 )
                            //                                {
                            //                                    if ( waveCount == 0 )
                            //                                    {
                            //                                        iconSize += Math.max ( 1,
                            //                                                ( selectedIconSize + 10 - iconSize ) / 3 );
                            //                                        iconSize = Math.min ( selectedIconSize + 10, iconSize );
                            //                                        if ( iconSize >= selectedIconSize + 10 )
                            //                                        {
                            //                                            waveCount++;
                            //                                        }
                            //                                    }
                            //                                    else if ( waveCount == 2 )
                            //                                    {
                            //                                        iconSize += Math.max ( 1,
                            //                                                ( selectedIconSize + 3 - iconSize ) / 2 );
                            //                                        iconSize = Math.min ( selectedIconSize + 3, iconSize );
                            //                                        if ( iconSize >= selectedIconSize + 3 )
                            //                                        {
                            //                                            waveCount++;
                            //                                        }
                            //                                    }
                            //                                    else if ( waveCount == 1 )
                            //                                    {
                            //                                        iconSize -= Math.max ( 1,
                            //                                                ( iconSize - ( selectedIconSize - 2 ) ) / 2 );
                            //                                        if ( iconSize <= selectedIconSize - 2 )
                            //                                        {
                            //                                            waveCount++;
                            //                                        }
                            //                                    }
                            //                                    else if ( waveCount == 3 )
                            //                                    {
                            //                                        iconSize -= 1;
                            //                                        if ( iconSize <= selectedIconSize )
                            //                                        {
                            //                                            waveCount++;
                            //                                        }
                            //                                    }
                            //                                    toggleButton.repaint ();
                            //                                }
                            //                                else
                            //                                {
                            //                                    animator.stop ();
                            //                                }
                            //                            }
                        } );
                        animator.start ();
                    }
                    else
                    {
                        iconSize = 16;
                    }
                }
            } );
        }
    }

    public void paint ( Graphics g, JComponent c )
    {
        AbstractButton b = ( AbstractButton ) c;
        ButtonModel model = b.getModel ();

        Graphics2D g2d = ( Graphics2D ) g;
        g2d.setPaint ( Color.LIGHT_GRAY );
        g2d.drawRect ( 0, 0, c.getWidth () - 1, c.getHeight () - 1 );
        if ( model.isSelected () )
        {
            g2d.fillRect ( 0, 0, c.getWidth (), c.getHeight () );
        }

        super.paint ( g, c );
    }

    protected void paintIcon ( Graphics g, JComponent c, Rectangle iconRect )
    {
        AbstractButton b = ( AbstractButton ) c;
        ButtonModel model = b.getModel ();
        if ( model.isSelected () )
        {
            Icon icon = b.getSelectedIcon () != null ? b.getSelectedIcon () : b.getIcon ();
            if ( icon instanceof ImageIcon )
            {
                ImageIcon imageIcon = ( ImageIcon ) icon;
                BufferedImage bi = ImageUtils.getBufferedImage ( imageIcon.getImage () );
                bi = ImageUtils.createPreviewImage ( bi, iconSize );
                Graphics2D g2d = ( Graphics2D ) g;
                g2d.drawImage ( bi, iconRect.x + iconRect.width / 2 - iconSize / 2, iconRect.y + iconRect.height / 2 - iconSize / 2,
                        iconSize, iconSize, null );
            }
            else
            {
                super.paintIcon ( g, c, iconRect );
            }
        }
        else
        {
            super.paintIcon ( g, c, iconRect );
        }
    }
}
