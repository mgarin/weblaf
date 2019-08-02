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

package com.alee.extended.colorchooser;

import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebColorPicker extends JComponent
{
    public static final ImageIcon DEFAULT_ICON = new ImageIcon ( WebColorPicker.class.getResource ( "icons/default.png" ) );
    public static final ImageIcon SWITCH_ICON = new ImageIcon ( WebColorPicker.class.getResource ( "icons/switch.png" ) );

    private List<ActionListener> actionListeners = new ArrayList<ActionListener> ( 1 );

    private static final int RECT_LENGTH = 28;

    private Color border1color = new Color ( 192, 192, 192 );
    private Color border2color = Color.WHITE;

    private Color color1 = Color.BLACK;
    private Color color2 = Color.WHITE;

    public WebColorPicker ()
    {
        super ();
        SwingUtils.setOrientation ( this );
        setPreferredSize ( new Dimension ( 44, 44 ) );
        initListeners ();
    }

    public Color getColor ()
    {
        return color1;
    }

    public Color getColor1 ()
    {
        return color1;
    }

    public void setColor1 ( final Color color1 )
    {
        this.color1 = color1;
        repaint ();
    }

    public Color getColor2 ()
    {
        return color2;
    }

    public void setColor2 ( final Color color2 )
    {
        this.color2 = color2;
        repaint ();
    }

    private void initListeners ()
    {
        final MouseAdapter adapter = new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                if ( isSwitchUnderPoint ( e ) )
                {
                    // Colors change
                    switchColors ();
                }
                else if ( isDefaultUnderPoint ( e ) )
                {
                    // Default colors
                    setDefaultColors ();
                }
                else if ( SwingUtilities.isLeftMouseButton ( e ) && isColor1UnderPoint ( e ) )
                {
                    // 1st color choose
                    final Color color = JColorChooser.showDialog ( null, "Primary color chooser", color1 );
                    if ( color != null )
                    {
                        color1 = color;
                        repaint ();
                        fireActionPerformed ();
                    }
                }
                else if ( SwingUtilities.isLeftMouseButton ( e ) && isColor2UnderPoint ( e ) )
                {
                    // 2nd color choose
                    final Color color = JColorChooser.showDialog ( null, "Secondary color chooser", color2 );
                    if ( color != null )
                    {
                        color2 = color;
                        repaint ();
                        fireActionPerformed ();
                    }
                }
            }

            @Override
            public void mouseMoved ( final MouseEvent e )
            {
                updateTT ( e );
            }

            @Override
            public void mouseDragged ( final MouseEvent e )
            {
                updateTT ( e );
            }

            private void updateTT ( final MouseEvent e )
            {
                if ( isSwitchUnderPoint ( e ) )
                {
                    updateToolTip ( PickerPart.colorSwitch );
                }
                else if ( isDefaultUnderPoint ( e ) )
                {
                    updateToolTip ( PickerPart.defaultColor );
                }
                else if ( isColor1UnderPoint ( e ) )
                {
                    updateToolTip ( PickerPart.color1 );
                }
                else if ( isColor2UnderPoint ( e ) )
                {
                    updateToolTip ( PickerPart.color2 );
                }
                else
                {
                    updateToolTip ( null );
                }
            }
        };
        addMouseListener ( adapter );
        addMouseMotionListener ( adapter );
    }

    private PickerPart lastPickerPart = PickerPart.colorSwitch;

    private void updateToolTip ( final PickerPart pickerPart )
    {
        if ( pickerPart != lastPickerPart )
        {
            setToolTip ( pickerPart );
            lastPickerPart = pickerPart;
        }
    }

    public void setToolTip ( final PickerPart pickerPart )
    {
        if ( pickerPart == null )
        {
            WebColorPicker.this.setToolTipText ( null );
        }
        else
        {
            WebColorPicker.this.setToolTipText ( "Switch primary and secondary colors" );
        }
    }

    public void switchColors ()
    {
        final Color tmp = color2;
        color2 = color1;
        color1 = tmp;
        repaint ();
        fireActionPerformed ();
    }

    public void setDefaultColors ()
    {
        color1 = Color.BLACK;
        color2 = Color.WHITE;
        repaint ();
        fireActionPerformed ();
    }

    public enum PickerPart
    {
        colorSwitch,
        defaultColor,
        color1,
        color2
    }

    private boolean isColor2UnderPoint ( final MouseEvent e )
    {
        return ( e.getX () > 2 + RECT_LENGTH || e.getY () > 2 + RECT_LENGTH ) &&
                e.getX () >= getWidth () - 1 - RECT_LENGTH && e.getX () <= getWidth () - 5 &&
                e.getY () >= getHeight () - 1 - RECT_LENGTH && e.getY () <= getHeight () - 5;
    }

    private boolean isColor1UnderPoint ( final MouseEvent e )
    {
        return e.getX () >= 4 && e.getX () <= RECT_LENGTH && e.getY () >= 4 &&
                e.getY () <= RECT_LENGTH;
    }

    private boolean isDefaultUnderPoint ( final MouseEvent e )
    {
        return e.getX () >= 1 && e.getX () <= 1 + DEFAULT_ICON.getIconWidth () &&
                e.getY () >= getHeight () - DEFAULT_ICON.getIconHeight () - 1 &&
                e.getY () <= getHeight () - 1;
    }

    private boolean isSwitchUnderPoint ( final MouseEvent e )
    {
        return e.getX () >= getWidth () - SWITCH_ICON.getIconWidth () - 1 &&
                e.getX () <= getWidth () - 1 && e.getY () >= 1 &&
                e.getY () <= 1 + SWITCH_ICON.getIconHeight ();
    }

    @Override
    public void paint ( final Graphics g )
    {
        super.paint ( g );

        if ( !isEnabled () )
        {
            g.setXORMode ( Color.GRAY );
        }

        // 2nd color
        g.setColor ( border1color );
        g.drawRect ( getWidth () - 3 - RECT_LENGTH, getHeight () - 3 - RECT_LENGTH, RECT_LENGTH, RECT_LENGTH );
        g.setColor ( border2color );
        g.drawRect ( getWidth () - 2 - RECT_LENGTH, getHeight () - 2 - RECT_LENGTH, RECT_LENGTH - 2, RECT_LENGTH - 2 );
        g.setColor ( color2 );
        g.fillRect ( getWidth () - 1 - RECT_LENGTH, getHeight () - 1 - RECT_LENGTH, RECT_LENGTH - 3, RECT_LENGTH - 3 );

        // 1st color
        g.setColor ( border1color );
        g.drawRect ( 2, 2, RECT_LENGTH, RECT_LENGTH );
        g.setColor ( border2color );
        g.drawRect ( 3, 3, RECT_LENGTH - 2, RECT_LENGTH - 2 );
        g.setColor ( color1 );
        g.fillRect ( 4, 4, RECT_LENGTH - 3, RECT_LENGTH - 3 );

        if ( !isEnabled () )
        {
            g.setPaintMode ();
        }

        // Controls
        g.drawImage ( SWITCH_ICON.getImage (), getWidth () - SWITCH_ICON.getIconWidth () - 1, 1, SWITCH_ICON.getImageObserver () );
        g.drawImage ( DEFAULT_ICON.getImage (), 1, getHeight () - DEFAULT_ICON.getIconHeight () - 1, DEFAULT_ICON.getImageObserver () );
    }

    @Override
    public void setEnabled ( final boolean enabled )
    {
        super.setEnabled ( enabled );
        repaint ();
    }

    public void addActionListener ( final ActionListener actionListener )
    {
        actionListeners.add ( actionListener );
    }

    public void removeActionListener ( final ActionListener actionListener )
    {
        actionListeners.remove ( actionListener );
    }

    private void fireActionPerformed ()
    {
        final ActionEvent actionEvent = new ActionEvent ( WebColorPicker.this, 0, "Color changed" );
        for ( final ActionListener actionListener : CollectionUtils.copy ( actionListeners ) )
        {
            actionListener.actionPerformed ( actionEvent );
        }
    }
}