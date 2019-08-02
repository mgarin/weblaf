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

package com.alee.laf.colorchooser;

import com.alee.extended.layout.TableLayout;
import com.alee.laf.panel.WebPanel;
import com.alee.utils.CollectionUtils;
import com.alee.utils.ImageUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class PaletteColorChooser extends WebPanel
{
    public static final ImageIcon LOOP_ICON = new ImageIcon ( PaletteColorChooser.class.getResource ( "icons/loop.png" ) );

    private List<ChangeListener> changeListeners = new ArrayList<ChangeListener> ( 1 );

    private boolean adjusting = false;

    private boolean webOnlyColors = false;

    private Color sideColor = Color.RED;
    private Color color = Color.WHITE;

    private PaletteColorChooserPaint paletteColorChooserPaint;
    private BufferedImage image;
    private Point coordinate;

    private JComponent colorChooser;

    public PaletteColorChooser ()
    {
        super ();

        paletteColorChooserPaint = new PaletteColorChooserPaint ( 0, 0, 256, 256, sideColor );
        image = ImageUtils.createCompatibleImage ( 256, 256, Transparency.TRANSLUCENT );
        coordinate = new Point ( 2, 2 );
        repaintImage ();

        setLayout ( new TableLayout ( new double[][]{ { TableLayout.PREFERRED }, { 3, TableLayout.PREFERRED, 3 } } ) );

        colorChooser = new JComponent ()
        {
            @Override
            protected void paintComponent ( final Graphics g )
            {
                super.paintComponent ( g );

                final Graphics2D g2d = ( Graphics2D ) g;

                final Shape old = g2d.getClip ();
                final Area clip = new Area ( new Rectangle2D.Double ( 2, 2, getWidth () - 4, getHeight () - 4 ) );
                clip.intersect ( new Area ( old ) );
                g2d.setClip ( clip );

                g2d.drawImage ( image, 2, 2, null );
                g2d.drawImage ( LOOP_ICON.getImage (), coordinate.x - LOOP_ICON.getIconWidth () / 2,
                        coordinate.y - LOOP_ICON.getIconHeight () / 2, LOOP_ICON.getImageObserver () );

                g2d.setClip ( old );
            }
        };
        colorChooser.setBorder ( BorderFactory.createCompoundBorder ( BorderFactory.createLineBorder ( Color.GRAY, 1 ),
                BorderFactory.createLineBorder ( Color.WHITE, 1 ) ) );
        //        colorChooser.setBorder ( BorderFactory
        //                .createBevelBorder ( BevelBorder.LOWERED, Color.WHITE, Color.WHITE,
        //                        new Color ( 160, 160, 160 ), new Color ( 178, 178, 178 ) ) );
        colorChooser.setPreferredSize ( new Dimension ( 260, 260 ) );

        final ColorChooserMouseAdapter adapter = new ColorChooserMouseAdapter ();
        colorChooser.addMouseListener ( adapter );
        colorChooser.addMouseMotionListener ( adapter );

        add ( colorChooser, "0,1" );
    }

    public JComponent getColorChooser ()
    {
        return colorChooser;
    }

    private void repaintImage ()
    {
        final Graphics2D g2d = image.createGraphics ();
        g2d.setPaint ( paletteColorChooserPaint );
        g2d.fillRect ( 0, 0, 256, 256 );
        g2d.dispose ();
    }

    private class ColorChooserMouseAdapter extends MouseAdapter
    {
        private boolean dragging = false;

        @Override
        public void mousePressed ( final MouseEvent e )
        {
            dragging = true;
            adjusting = true;
            updateCoordinate ( e.getPoint () );
        }

        @Override
        public void mouseDragged ( final MouseEvent e )
        {
            updateCoordinate ( e.getPoint () );
        }

        @Override
        public void mouseReleased ( final MouseEvent e )
        {
            dragging = false;
            adjusting = false;
            if ( !colorChooser.getVisibleRect ().contains ( e.getPoint () ) )
            {
                setCursor ( Cursor.getDefaultCursor () );
            }
        }

        private void updateCoordinate ( final Point point )
        {
            coordinate = point;
            if ( coordinate.x < 2 )
            {
                coordinate.x = 2;
            }
            else if ( coordinate.x > 256 + 2 )
            {
                coordinate.x = 256 + 2;
            }
            if ( coordinate.y < 2 )
            {
                coordinate.y = 2;
            }
            else if ( coordinate.y > 256 + 2 )
            {
                coordinate.y = 256 + 2;
            }
            setColor ( paletteColorChooserPaint.getColor ( coordinate.x, coordinate.y ) );
        }

        @Override
        public void mouseEntered ( final MouseEvent e )
        {
            setCursor ( createLoopCursor () );
        }

        @Override
        public void mouseExited ( final MouseEvent e )
        {
            if ( !dragging )
            {
                setCursor ( Cursor.getDefaultCursor () );
            }
        }
    }

    private Cursor createLoopCursor ()
    {
        final Dimension dimension = Toolkit.getDefaultToolkit ().getBestCursorSize ( 14, 14 );

        final BufferedImage bufferedImage =
                ImageUtils.createCompatibleImage ( dimension.width, dimension.height, Transparency.TRANSLUCENT );

        final Graphics2D g2d = bufferedImage.createGraphics ();
        g2d.drawImage ( LOOP_ICON.getImage (), 0, 0, LOOP_ICON.getImageObserver () );
        g2d.dispose ();

        return Toolkit.getDefaultToolkit ().createCustomCursor ( bufferedImage, new Point ( 7, 7 ), "Loop Cursor" );
    }

    public Color getSideColor ()
    {
        return sideColor;
    }

    public void setSideColor ( final Color sideColor )
    {
        adjusting = true;

        // Settings corner color
        this.sideColor = sideColor;
        paletteColorChooserPaint = new PaletteColorChooserPaint ( 1, 1, 256, 256, sideColor );
        paletteColorChooserPaint.setWebSafe ( webOnlyColors );

        // Updating cached image
        repaintImage ();

        // Settings selected color
        setColor ( paletteColorChooserPaint.getColor ( coordinate.x, coordinate.y ) );

        adjusting = false;
    }

    public Color getColor ()
    {
        return color;
    }

    public void setColor ( final Color color )
    {
        this.color = color;

        if ( !adjusting )
        {
            // Updating cursor position
            updateSelectionByColor ();
        }

        // Informing about color change
        firePropertyChanged ();

        // Updating area
        colorChooser.repaint ();
    }

    public boolean isWebOnlyColors ()
    {
        return webOnlyColors;
    }

    public void setWebOnlyColors ( final boolean webOnlyColors )
    {
        this.webOnlyColors = webOnlyColors;
        paletteColorChooserPaint.setWebSafe ( webOnlyColors );
        repaintImage ();
        colorChooser.repaint ();
        firePropertyChanged ();
    }

    private void updateSelectionByColor ()
    {
        final HSBColor hsbColor = new HSBColor ( color );
        coordinate.x = 2 + Math.round ( 256 * hsbColor.getSaturation () );
        coordinate.y = 2 + Math.round ( 256 - 256 * hsbColor.getBrightness () );
    }

    public void addChangeListener ( final ChangeListener listener )
    {
        changeListeners.add ( listener );
    }

    public void removeChangeListener ( final ChangeListener listener )
    {
        changeListeners.remove ( listener );
    }

    private void firePropertyChanged ()
    {
        final ChangeEvent changeEvent = new ChangeEvent ( PaletteColorChooser.this );
        for ( final ChangeListener changeListener : CollectionUtils.copy ( changeListeners ) )
        {
            changeListener.stateChanged ( changeEvent );
        }
    }
}