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

package com.alee.managers.effects;

import com.alee.extended.window.TestFrame;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.hotkey.HotkeyRunnable;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class ShapeTransition
{
    private static Shape shape = null;
    private static JComponent component;
    private static WebTimer timer;

    private static int counter = 0;

    public static void main ( final String[] args )
    {
        WebLookAndFeel.install ();

//        final Ellipse2D.Double circle = new Ellipse2D.Double ( 0, 0, 100, 100 );
        //        final GeneralPath circle = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        final Shape circle = new Arc2D.Double ( 0,0,800,800,0f, 270, Arc2D.PIE );

        final Line2D.Double line = new Line2D.Double ( 0, 0, 800, 0 );

        component = new JComponent ()
        {
            @Override
            protected void paintComponent ( final Graphics g )
            {
                super.paintComponent ( g );

                final Graphics2D g2d = ( Graphics2D ) g;
                GraphicsUtils.setupAntialias ( g2d );
                g2d.setPaint ( Color.BLACK );
                g2d.draw ( shape != null ? shape : circle );
            }

            @Override
            public Dimension getPreferredSize ()
            {
                return new Dimension ( 800, 800 );
            }
        };
        final MouseAdapter a = new MouseAdapter ()
        {
            public void mousePressed ( final MouseEvent e )
            {
                if ( timer != null && timer.isRunning () )
                {
                    reset ();
                }
                else
                {
                    addPoint ( e );
                }
            }

            public void mouseDragged ( final MouseEvent e )
            {
                if ( timer != null && timer.isRunning () )
                {
                    reset ();
                }
                else
                {
                    addPoint ( e );
                }
            }

            protected void reset ()
            {
                //                if ( timer != null )
                //                {
                //                    timer.stop ();
                //                }
                //                shape = null;
                //                counter = 0;
                //                circle.reset ();
                //                component.repaint ();
            }

            protected void addPoint ( final MouseEvent e )
            {
                //                if ( counter == 0 )
                //                {
                //                    circle.moveTo ( e.getPoint ().x, e.getPoint ().y );
                //                }
                //                else
                //                {
                //                    circle.lineTo ( e.getPoint ().x, e.getPoint ().y );
                //                }
                //                counter++;
                //                component.repaint ();
            }
        };
        component.addMouseListener ( a );
        component.addMouseMotionListener ( a );
        component.setFocusable ( true );
        TestFrame.show ( component );

        HotkeyManager.registerHotkey ( Hotkey.SPACE, new HotkeyRunnable ()
        {
            public void run ( final KeyEvent e )
            {
                System.out.println ( "STARTED!" );
                perform ( circle, line, 5000 );
            }
        } );

        component.requestFocusInWindow ();

    }

    public static void perform ( final Shape startShape, final Shape endShape, final long time )
    {
        final List<PointInfo> startInfo = gatherInfo ( startShape.getPathIterator ( null ) );
        final List<PointInfo> endInfo = gatherInfo ( endShape.getPathIterator ( null ) );

        final List<PointInfo> srcPoints = calculateSrc ( startInfo );
        final List<PointInfo> dstPoints = calculateDst ( startInfo, endInfo );

        if ( timer != null )
        {
            timer.stop ();
        }

        timer = WebTimer.repeat ( 1000 / 60, 0, true, new ActionListener ()
        {
            private float progress = 0f;

            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                progress += ( 1f - progress ) * 0.05f;


                final GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
                for ( int i = 0; i < srcPoints.size (); i++ )
                {
                    final PointInfo src = srcPoints.get ( i );
                    final PointInfo dst = dstPoints.get ( i );
                    switch ( src.command )
                    {
                        case PathIterator.SEG_MOVETO:
                            gp.moveTo ( progress ( src.points[ 0 ], dst.points[ 0 ], progress ),
                                    progress ( src.points[ 1 ], dst.points[ 1 ], progress ) );
                            break;

                        case PathIterator.SEG_LINETO:
                            gp.lineTo ( progress ( src.points[ 0 ], dst.points[ 0 ], progress ),
                                    progress ( src.points[ 1 ], dst.points[ 1 ], progress ) );
                            break;

                        case PathIterator.SEG_QUADTO:
                            gp.quadTo ( progress ( src.points[ 0 ], dst.points[ 0 ], progress ),
                                    progress ( src.points[ 1 ], dst.points[ 1 ], progress ),
                                    progress ( src.points[ 2 ], dst.points[ 2 ], progress ),
                                    progress ( src.points[ 3 ], dst.points[ 3 ], progress ) );
                            break;

                        case PathIterator.SEG_CUBICTO:
                            gp.curveTo ( progress ( src.points[ 0 ], dst.points[ 0 ], progress ),
                                    progress ( src.points[ 1 ], dst.points[ 1 ], progress ),
                                    progress ( src.points[ 2 ], dst.points[ 2 ], progress ),
                                    progress ( src.points[ 3 ], dst.points[ 3 ], progress ),
                                    progress ( src.points[ 4 ], dst.points[ 4 ], progress ),
                                    progress ( src.points[ 5 ], dst.points[ 5 ], progress ) );
                            break;

                        case PathIterator.SEG_CLOSE:
                            gp.closePath ();
                            break;
                    }
                }
                shape = gp;

                if ( progress >= 1f )
                {
                    progress = 1f;
                    timer.stop ();
                }
                else
                {
                    component.repaint ();
                }
            }
        } );
    }

    private static List<PointInfo> calculateSrc ( final List<PointInfo> startInfo )
    {
        return startInfo;
        //        List<PointInfo> srcInfo = new ArrayList<PointInfo> ( startInfo.size () );
        //        for ( int i = 0; i < startInfo.size (); i++ )
        //        {
        //            srcInfo.add ( new PointInfo ( i == 0 ? PathIterator.SEG_MOVETO : PathIterator.SEG_LINETO,
        //                    new float[]{ startInfo.get ( i ).points[ 0 ], startInfo.get ( i ).points[ 1 ] } ) );
        //        }
        //        return srcInfo;
    }

    private static List<PointInfo> calculateDst ( final List<PointInfo> startInfo, final List<PointInfo> endInfo )
    {
        final List<PointInfo> dstInfo = new ArrayList<PointInfo> ( startInfo.size () );
        final Point2D.Double p1 = new Point2D.Double ( endInfo.get ( 0 ).points[ 0 ], endInfo.get ( 0 ).points[ 1 ] );
        final Point2D.Double p2 = new Point2D.Double ( endInfo.get ( 1 ).points[ 0 ], endInfo.get ( 1 ).points[ 1 ] );
        for ( int i = 0; i < startInfo.size (); i++ )
        {
            final float x = ( float ) ( p1.x + p2.x ) * i / ( startInfo.size () - 1 );
            final float y = ( float ) ( p1.y + p2.y ) * i / ( startInfo.size () - 1 );
            final int command = startInfo.get ( i ).command;
            switch ( command )
            {
                case PathIterator.SEG_MOVETO:
                {
                    dstInfo.add ( new PointInfo ( command, new float[]{ x, y } ) );
                    break;
                }
                case PathIterator.SEG_LINETO:
                {
                    dstInfo.add ( new PointInfo ( command, new float[]{ x, y } ) );
                    break;
                }
                case PathIterator.SEG_QUADTO:
                {
                    final float[] pp = dstInfo.get ( i - 1 ).points;
                    final float px = pp[ pp.length - 2 ];
                    final float py = pp[ pp.length - 1 ];
                    dstInfo.add ( new PointInfo ( command, new float[]{ progress ( px, x, 0.5f ), progress ( py, y, 0.5f ), x, y } ) );
                    break;
                }
                case PathIterator.SEG_CUBICTO:
                {
                    final float[] pp = dstInfo.get ( i - 1 ).points;
                    final float px = pp[ pp.length - 2 ];
                    final float py = pp[ pp.length - 1 ];
                    dstInfo.add ( new PointInfo ( command,
                            new float[]{ progress ( px, x, 0.33f ), progress ( py, y, 0.33f ), progress ( px, x, 0.66f ),
                                    progress ( py, y, 0.66f ), x, y } ) );
                    break;
                }
                case PathIterator.SEG_CLOSE:
                {
                    dstInfo.add ( new PointInfo ( command, null ) );
                    break;
                }
            }
        }
        return dstInfo;
    }

    protected static List<PointInfo> gatherInfo ( final PathIterator iterator )
    {
        final List<PointInfo> info = new ArrayList<PointInfo> ();
        while ( !iterator.isDone () )
        {
            final float[] points = new float[ 6 ];
            final int type = iterator.currentSegment ( points );
            System.out.println ( Arrays.toString ( points ) );
            switch ( type )
            {
                case PathIterator.SEG_MOVETO:
                    System.out.println ( "SEG_MOVETO" );
                    break;

                case PathIterator.SEG_LINETO:
                    System.out.println ( "SEG_LINETO" );
                    break;

                case PathIterator.SEG_QUADTO:
                    System.out.println ( "SEG_QUADTO" );
                    break;

                case PathIterator.SEG_CUBICTO:
                    System.out.println ( "SEG_CUBICTO" );
                    break;

                case PathIterator.SEG_CLOSE:
                    System.out.println ( "SEG_CLOSE" );
                    break;
            }
            info.add ( new PointInfo ( type, points ) );
            iterator.next ();
        }
        return info;
    }

    public static class PointInfo
    {
        public final int command;
        public final float[] points;

        public PointInfo ( final int command, final float[] points )
        {
            super ();
            this.command = command;
            this.points = points;
        }
    }

    private static float progress ( final float x1, final float x2, final float progress )
    {
        return ( 1 - progress ) * x1 + progress * x2;
    }
}