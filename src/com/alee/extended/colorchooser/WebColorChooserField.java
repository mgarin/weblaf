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

import com.alee.extended.image.WebImage;
import com.alee.extended.painter.AbstractPainter;
import com.alee.laf.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.colorchooser.ColorChooserListener;
import com.alee.laf.colorchooser.WebColorChooserPanel;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.rootpane.WebWindow;
import com.alee.laf.text.WebTextField;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyManager;
import com.alee.managers.hotkey.HotkeyRunnable;
import com.alee.utils.ColorUtils;
import com.alee.utils.ImageUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * User: mgarin Date: 21.12.12 Time: 15:38
 */

public class WebColorChooserField extends WebTextField
{
    public static final ImageIcon pipetteIcon = new ImageIcon ( WebColorChooserField.class.getResource ( "icons/eyedropper.png" ) );

    private Color color;
    private ColorChooserFieldType fieldType;
    private boolean pipetteEnabled = true;
    private int pipettePixels = 9;
    private int pixelSize = 11;

    private String last = "";

    private Robot robot;
    private WebImage pipettePicker;
    private WebButton colorButton;
    private WebWindow popup;
    private WebColorChooserPanel colorChooserPanel;

    // todo Make possible color's alpha channel selection

    public WebColorChooserField ()
    {
        this ( Color.WHITE );
    }

    public WebColorChooserField ( Color color )
    {
        super ();

        // Pipette color picker
        updatePipette ();

        // Trailing color choose button
        colorButton = new WebButton ( ImageUtils.createColorChooserIcon ( color ) );
        colorButton.setFocusable ( false );
        colorButton.setShadeWidth ( 0 );
        colorButton.setMoveIconOnPress ( false );
        colorButton.setRolloverDecoratedOnly ( true );
        colorButton.setCursor ( Cursor.getDefaultCursor () );
        colorButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                showColorChooserPopup ();
            }
        } );
        setTrailingComponent ( colorButton );

        // Actions
        addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                updateColorFromField ();
            }
        } );
        addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( MouseEvent e )
            {
                if ( isEnabled () && SwingUtilities.isRightMouseButton ( e ) )
                {
                    showColorChooserPopup ();
                }
            }
        } );
        addFocusListener ( new FocusAdapter ()
        {
            @Override
            public void focusLost ( FocusEvent e )
            {
                updateColorFromField ();
            }
        } );
        addKeyListener ( new KeyAdapter ()
        {
            @Override
            public void keyReleased ( KeyEvent e )
            {
                if ( isEnabled () )
                {
                    if ( Hotkey.ESCAPE.isTriggered ( e ) )
                    {
                        updateViewFromColor ();
                    }
                    else if ( Hotkey.DOWN.isTriggered ( e ) )
                    {
                        showColorChooserPopup ();
                    }
                }
            }
        } );

        // Initial data
        setFieldType ( ColorChooserFieldType.rgb );
        setColor ( color );

        // Initial styling settings
        updateMargin ();
        setHorizontalAlignment ( CENTER );
    }

    public boolean isPipetteEnabled ()
    {
        return pipetteEnabled;
    }

    public void setPipetteEnabled ( boolean pipetteEnabled )
    {
        this.pipetteEnabled = pipetteEnabled;
        updatePipette ();
    }

    public Color getColor ()
    {
        return color;
    }

    public void setColor ( Color color )
    {
        this.color = color;
        updateViewFromColor ();
    }

    public ColorChooserFieldType getFieldType ()
    {
        return fieldType;
    }

    public void setFieldType ( ColorChooserFieldType fieldType )
    {
        this.fieldType = fieldType;
        updateFieldType ();
    }

    @Override
    public void setDrawBorder ( boolean drawBorder )
    {
        super.setDrawBorder ( drawBorder );
        updateMargin ();
    }

    /**
     * Private update methods
     */

    private void updateViewFromColor ()
    {
        colorButton.setIcon ( ImageUtils.createColorChooserIcon ( color ) );
        updateText ();
    }

    private void updateColorFromField ()
    {
        String current = getText ();
        if ( !current.equals ( last ) )
        {
            try
            {
                boolean hex = fieldType.equals ( ColorChooserFieldType.hex );
                Color newColor = hex ? ColorUtils.parseHexColor ( current ) : ColorUtils.parseRgbColor ( current );
                if ( newColor != null )
                {
                    // todo Ignoring alpha for now
                    newColor = ColorUtils.removeAlpha ( newColor );

                    // Apply new value
                    setColor ( newColor );
                }
                else
                {
                    // Restore old value
                    updateViewFromColor ();
                }
            }
            catch ( Throwable e )
            {
                // Restore old value
                updateViewFromColor ();
            }
        }
    }

    private void updateFieldType ()
    {
        if ( fieldType != null )
        {
            boolean hex = fieldType.equals ( ColorChooserFieldType.hex );
            setColumns ( hex ? 6 : 9 );
            updateText ();
        }
    }

    private void updateText ()
    {
        if ( color != null )
        {
            String text = getColorText ( color );
            setText ( text );
            last = text;
        }
    }

    private String getColorText ( Color color )
    {
        boolean hex = fieldType.equals ( ColorChooserFieldType.hex );
        return hex ? ColorUtils.getHexColor ( color ) : color.getRed () + "," + color.getGreen () + "," + color.getBlue ();
    }

    private void updateMargin ()
    {
        setMargin ( isDrawBorder () ? new Insets ( -1, 0, -1, -1 ) : new Insets ( 0, 0, 0, 0 ) );
    }

    /**
     * Pipette chooser
     */

    private void updatePipette ()
    {
        if ( pipetteEnabled )
        {
            installPipette ();
        }
        else
        {
            uninstallPipette ();
        }
        revalidate ();
    }

    private void installPipette ()
    {
        if ( pipettePicker == null )
        {
            // Pipette picker icon
            pipettePicker = new WebImage ( pipetteIcon );
            //            pipettePicker.setMargin ( 0, 2, 0, 2 );

            // Pipette picker actions
            try
            {
                robot = new Robot ();
            }
            catch ( AWTException e )
            {
                e.printStackTrace ();
            }
            if ( robot != null )
            {
                MouseAdapter mouseAdapter = new MouseAdapter ()
                {
                    private boolean shouldUpdateColor;

                    private WebDialog window;
                    private WebPanel screen;
                    private WebLabel info;

                    private boolean updating = false;
                    private BufferedImage screenshot;
                    private Color color;

                    @Override
                    public void mousePressed ( MouseEvent e )
                    {
                        if ( pipetteEnabled && SwingUtils.isLeftMouseButton ( e ) )
                        {
                            // Resetting color update mark
                            shouldUpdateColor = true;

                            // Creating preview window
                            createPreviewWindow ();
                            updateWindowLocation ();

                            // Displaying preview window
                            window.pack ();
                            window.setVisible ( true );

                            // Transferring focus to preview panel
                            screen.requestFocus ();

                            // Updating preview screenshot
                            updateScreenshot ();
                        }
                    }

                    @Override
                    public void mouseDragged ( MouseEvent e )
                    {
                        if ( pipetteEnabled && SwingUtils.isLeftMouseButton ( e ) && window != null )
                        {
                            // Updating preview window location
                            updateWindowLocation ();

                            // Updating preview screenshot
                            updateScreenshot ();
                        }
                    }

                    @Override
                    public void mouseReleased ( MouseEvent e )
                    {
                        if ( pipetteEnabled && SwingUtils.isLeftMouseButton ( e ) && window != null )
                        {
                            // Closing preview window
                            window.dispose ();
                        }
                    }

                    private void updateScreenshot ()
                    {
                        // Simply ignore update if an old one is still running
                        if ( !updating )
                        {
                            // Updating image in a separate thread to avoid UI freezing
                            updating = true;
                            new Thread ( new Runnable ()
                            {
                                @Override
                                public void run ()
                                {
                                    if ( screen != null )
                                    {
                                        Point p = MouseInfo.getPointerInfo ().getLocation ();
                                        screenshot = robot.createScreenCapture (
                                                new Rectangle ( p.x - pipettePixels / 2, p.y - pipettePixels / 2, pipettePixels,
                                                        pipettePixels ) );
                                        color = new Color ( screenshot.getRGB ( pipettePixels / 2, pipettePixels / 2 ) );
                                        if ( screen != null )
                                        {
                                            screen.repaint ();
                                            info.setText ( getColorText ( color ) );
                                        }
                                        else
                                        {
                                            screenshot.flush ();
                                            screenshot = null;
                                            color = null;
                                        }
                                    }
                                    updating = false;
                                }
                            } ).start ();
                        }
                    }

                    private void createPreviewWindow ()
                    {
                        window = new WebDialog ( pipettePicker );
                        window.setLayout ( new BorderLayout () );
                        window.setUndecorated ( true );
                        window.setAlwaysOnTop ( true );

                        window.addWindowListener ( new WindowAdapter ()
                        {
                            @Override
                            public void windowClosed ( WindowEvent e )
                            {
                                if ( screenshot != null )
                                {
                                    if ( shouldUpdateColor )
                                    {
                                        setColor ( color );
                                    }
                                    screenshot.flush ();
                                    screenshot = null;
                                }
                                HotkeyManager.unregisterHotkeys ( screen );
                                window = null;
                                screen = null;
                            }
                        } );

                        final AbstractPainter<WebPanel> screenPainter = new AbstractPainter<WebPanel> ()
                        {
                            /**
                             * {@inheritDoc}
                             */
                            @Override
                            public void paint ( final Graphics2D g2d, final Rectangle bounds, final WebPanel c )
                            {
                                if ( window.isShowing () && robot != null )
                                {
                                    // Screen
                                    g2d.drawImage ( screenshot, bounds.x + 2, bounds.y + 2, bounds.width - 4, bounds.height - 4, null );

                                    // Border
                                    g2d.setPaint ( Color.BLACK );
                                    g2d.drawRect ( 0, 0, bounds.width - 1, bounds.height - 1 );
                                    g2d.setPaint ( Color.WHITE );
                                    g2d.drawRect ( 1, 1, bounds.width - 3, bounds.height - 3 );

                                    // Cursor
                                    int mx = bounds.x + bounds.width / 2;
                                    int my = bounds.y + bounds.height / 2;
                                    g2d.setPaint ( Color.WHITE );
                                    g2d.drawLine ( mx - 1, my - 7, mx - 1, my + 7 );
                                    g2d.drawLine ( mx + 1, my - 7, mx + 1, my + 7 );
                                    g2d.drawLine ( mx - 7, my - 1, mx + 7, my - 1 );
                                    g2d.drawLine ( mx - 7, my + 1, mx + 7, my + 1 );
                                    g2d.setPaint ( Color.BLACK );
                                    g2d.drawLine ( mx, my - 7, mx, my + 7 );
                                    g2d.drawLine ( mx - 7, my, mx + 7, my );
                                }
                            }
                        };

                        screen = new WebPanel ( screenPainter );
                        screen.setFocusable ( true );
                        screen.setPreferredSize ( new Dimension ( pipettePixels * pixelSize + 4, pipettePixels * pixelSize + 4 ) );
                        window.add ( screen, BorderLayout.CENTER );

                        info = new WebLabel ( WebLabel.LEADING );
                        info.setMargin ( 4 );
                        info.setIcon ( new Icon ()
                        {
                            @Override
                            public void paintIcon ( Component c, Graphics g, int x, int y )
                            {
                                if ( color != null )
                                {
                                    Graphics2D g2d = ( Graphics2D ) g;
                                    g2d.setPaint ( Color.BLACK );
                                    g2d.drawRect ( x, y, 15, 15 );
                                    g2d.setPaint ( Color.WHITE );
                                    g2d.drawRect ( x + 1, y + 1, 13, 13 );
                                    g2d.setPaint ( color );
                                    g2d.fillRect ( x + 2, y + 2, 12, 12 );
                                }
                            }

                            @Override
                            public int getIconWidth ()
                            {
                                return 16;
                            }

                            @Override
                            public int getIconHeight ()
                            {
                                return 16;
                            }
                        } );
                        info.setPainter ( new AbstractPainter<WebLabel> ()
                        {
                            /**
                             * {@inheritDoc}
                             */
                            @Override
                            public Insets getMargin ( final WebLabel c )
                            {
                                return new Insets ( 0, 2, 2, 2 );
                            }

                            /**
                             * {@inheritDoc}
                             */
                            @Override
                            public void paint ( final Graphics2D g2d, final Rectangle bounds, final WebLabel c )
                            {
                                g2d.setPaint ( Color.BLACK );
                                g2d.drawRect ( bounds.x, bounds.y - 1, bounds.width - 1, bounds.height );
                            }
                        } );
                        window.add ( info, BorderLayout.SOUTH );

                        HotkeyManager.registerHotkey ( screen, Hotkey.ESCAPE, new HotkeyRunnable ()
                        {
                            @Override
                            public void run ( KeyEvent e )
                            {
                                if ( window != null )
                                {
                                    shouldUpdateColor = false;
                                    window.dispose ();
                                }
                            }
                        } );
                    }

                    private void updateWindowLocation ()
                    {
                        Point p = MouseInfo.getPointerInfo ().getLocation ();
                        Rectangle b = window.getGraphicsConfiguration ().getDevice ().getDefaultConfiguration ().getBounds ();
                        int ww = window.getWidth ();
                        int wh = window.getHeight ();
                        int x = p.x + 20 + ww < b.x + b.width ? p.x + 20 : p.x - 20 - ww;
                        int y = p.y + 20 + wh < b.y + b.height ? p.y + 20 : p.y - 20 - wh;
                        window.setLocation ( x, y );
                    }
                };
                pipettePicker.addMouseListener ( mouseAdapter );
                pipettePicker.addMouseMotionListener ( mouseAdapter );
                pipettePicker.setCursor ( Cursor.getDefaultCursor () );
            }
        }

        // Adding field leading component
        setLeadingComponent ( pipettePicker );
    }

    private void uninstallPipette ()
    {
        // Removing field leading component
        setLeadingComponent ( null );
    }

    /**
     * Color chooser popup
     */

    private void showColorChooserPopup ()
    {
        // Checking that component is eligable for focus request
        if ( !requestFocusInWindow () && !isFocusOwner () )
        {
            // Cancel operation if component is not eligable for focus yet
            // This might occur if some other component input verifier holds the focus or in some other rare cases
            return;
        }

        // Update date from field if it was changed
        updateColorFromField ();

        // Create popup if it doesn't exist
        if ( popup == null || colorChooserPanel == null )
        {
            Window ancestor = SwingUtils.getWindowAncestor ( this );

            // Color chooser
            colorChooserPanel = new WebColorChooserPanel ( true );
            colorChooserPanel.setColor ( color );
            colorChooserPanel.setUndecorated ( false );
            colorChooserPanel.setDrawFocus ( false );
            colorChooserPanel.setRound ( StyleConstants.smallRound );
            colorChooserPanel.setShadeWidth ( 0 );

            // Popup window
            popup = new WebWindow ( ancestor );
            popup.setLayout ( new BorderLayout () );
            popup.setCloseOnFocusLoss ( true );
            popup.setWindowOpaque ( false );
            popup.add ( colorChooserPanel );
            popup.pack ();

            // Correct popup positioning
            updatePopupLocation ();
            ancestor.addComponentListener ( new ComponentAdapter ()
            {
                @Override
                public void componentMoved ( ComponentEvent e )
                {
                    if ( popup.isShowing () )
                    {
                        updatePopupLocation ();
                    }
                }

                @Override
                public void componentResized ( ComponentEvent e )
                {
                    if ( popup.isShowing () )
                    {
                        updatePopupLocation ();
                    }
                }
            } );
            ancestor.addPropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, new PropertyChangeListener ()
            {
                @Override
                public void propertyChange ( PropertyChangeEvent evt )
                {
                    if ( popup.isShowing () )
                    {
                        updatePopupLocation ();
                    }
                }
            } );

            colorChooserPanel.addColorChooserListener ( new ColorChooserListener ()
            {
                @Override
                public void okPressed ( ActionEvent e )
                {
                    setColor ( colorChooserPanel.getColor () );
                    popup.setVisible ( false );
                }

                @Override
                public void resetPressed ( ActionEvent e )
                {

                }

                @Override
                public void cancelPressed ( ActionEvent e )
                {
                    popup.setVisible ( false );
                }
            } );
        }
        else
        {
            // Updating window location
            updatePopupLocation ();

            // Updating color
            colorChooserPanel.setColor ( color );
        }

        // Applying orientation to popup
        SwingUtils.copyOrientation ( WebColorChooserField.this, popup );

        // Showing popup and changing focus
        popup.setVisible ( true );
        colorChooserPanel.requestFocusInWindow ();
    }

    private void updatePopupLocation ()
    {
        final Point los = WebColorChooserField.this.getLocationOnScreen ();
        final Rectangle gb = popup.getGraphicsConfiguration ().getBounds ();
        final int shadeWidth = isDrawBorder () ? getShadeWidth () : 0;
        final boolean ltr = WebColorChooserField.this.getComponentOrientation ().isLeftToRight ();
        final int w = WebColorChooserField.this.getWidth ();
        final int h = WebColorChooserField.this.getHeight ();

        final int x;
        if ( ltr )
        {
            if ( los.x + shadeWidth + popup.getWidth () <= gb.x + gb.width )
            {
                x = los.x + shadeWidth;
            }
            else
            {
                x = los.x + w - shadeWidth - popup.getWidth ();
            }
        }
        else
        {
            if ( los.x + w - shadeWidth - popup.getWidth () >= gb.x )
            {
                x = los.x + w - shadeWidth - popup.getWidth ();
            }
            else
            {
                x = los.x + shadeWidth;
            }
        }

        final int y;
        if ( los.y + h + popup.getHeight () <= gb.y + gb.height )
        {
            y = los.y + h + ( isDrawBorder () ? 0 : 1 );
        }
        else
        {
            y = los.y - popup.getHeight () - ( isDrawBorder () ? 0 : 1 );
        }

        popup.setLocation ( x, y );
    }
}
