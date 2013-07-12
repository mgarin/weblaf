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

import com.alee.extended.colorchooser.DoubleColorField;
import com.alee.extended.layout.ToolbarLayout;
import com.alee.laf.StyleConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.separator.WebSeparator;
import com.alee.laf.text.WebTextField;
import com.alee.managers.hotkey.Hotkey;
import com.alee.utils.CollectionUtils;
import com.alee.utils.ColorUtils;
import com.alee.utils.SwingUtils;
import info.clearthought.layout.TableLayout;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 10.03.11 Time: 16:46
 */

public class WebColorChooserPanel extends WebPanel
{
    private List<ChangeListener> changeListeners = new ArrayList<ChangeListener> ();
    private List<ColorChooserListener> colorChooserListeners = new ArrayList<ColorChooserListener> ();

    private boolean showButtonsPanel = false;
    private Boolean webOnlyColors = false;

    private Color oldColor = Color.WHITE;
    private Color color = Color.WHITE;

    private int result = StyleConstants.NONE_OPTION;

    private boolean adjustingText = false;

    private PaletteColorChooser palette;
    private LineColorChooser lineColorChooser;
    private DoubleColorField doubleColorField;

    private WebTextField hueField;
    private WebTextField saturationField;
    private WebTextField brightnessField;
    private WebTextField redField;
    private WebTextField greenField;
    private WebTextField blueField;
    private WebTextField hexColor;

    private WebPanel buttonsPanel;

    public WebColorChooserPanel ()
    {
        this ( false );
    }

    public WebColorChooserPanel ( boolean showButtonsPanel )
    {
        super ();

        this.showButtonsPanel = showButtonsPanel;

        // Panel settings
        setOpaque ( false );
        setWebColored ( false );
        setMargin ( 2, 5, 2, 5 );
        setLayout ( new TableLayout ( new double[][]{ { TableLayout.FILL, 4, TableLayout.PREFERRED, 4, TableLayout.PREFERRED },
                { TableLayout.PREFERRED, TableLayout.PREFERRED } } ) );

        palette = new PaletteColorChooser ();
        palette.setOpaque ( false );
        palette.setWebOnlyColors ( isWebOnlyColors () );
        final ChangeListener paletteListener = new ChangeListener ()
        {
            public void stateChanged ( ChangeEvent e )
            {
                color = palette.getColor ();
                updateColors ( color, UpdateSource.palette );
            }
        };
        palette.addChangeListener ( paletteListener );
        add ( palette, "0,0" );

        lineColorChooser = new LineColorChooser ();
        lineColorChooser.setOpaque ( false );
        lineColorChooser.setWebOnlyColors ( isWebOnlyColors () );
        lineColorChooser.addChangeListener ( new ChangeListener ()
        {
            public void stateChanged ( ChangeEvent e )
            {
                palette.setSideColor ( lineColorChooser.getColor () );
            }
        } );
        add ( lineColorChooser, "2,0" );


        JPanel infoPanel = new JPanel ();
        infoPanel.setLayout ( new TableLayout ( new double[][]{ { TableLayout.PREFERRED, 4, TableLayout.FILL },
                { 3, TableLayout.FILL, 5, TableLayout.PREFERRED, 4, TableLayout.PREFERRED, 1, TableLayout.PREFERRED, 1,
                        TableLayout.PREFERRED, 1, TableLayout.PREFERRED, 3 } } ) );
        infoPanel.setOpaque ( false );
        add ( infoPanel, "4,0" );


        doubleColorField = new DoubleColorField ()
        {
            protected void oldColorPressed ()
            {
                setColor ( doubleColorField.getOldColor () );
            }
        };
        updateDoubleColorField ( color );
        //        doubleColorField.setPreferredSize ( new Dimension ( 1, 50 ) );
        doubleColorField.setOldColor ( oldColor );
        infoPanel.add ( doubleColorField, "0,1,2,3" );


        infoPanel.add ( new WebSeparator ( WebSeparator.HORIZONTAL ), "0,5,2,5" );


        JPanel colorsPanel = new JPanel ();
        colorsPanel.setLayout ( new TableLayout ( new double[][]{ { TableLayout.FILL, 5, TableLayout.PREFERRED, 4, TableLayout.PREFERRED },
                { TableLayout.PREFERRED, 0, TableLayout.PREFERRED, 0, TableLayout.PREFERRED, 1, TableLayout.PREFERRED, 1,
                        TableLayout.PREFERRED, 0, TableLayout.PREFERRED, 0, TableLayout.PREFERRED } } ) );
        colorsPanel.setOpaque ( false );
        infoPanel.add ( colorsPanel, "0,7,2,7" );

        // Hue
        WebLabel hueButton = new WebLabel ( "H:" );
        hueButton.setDrawShade ( true );
        colorsPanel.add ( hueButton, "0,0" );
        hueField = new WebTextField ();
        colorsPanel.add ( hueField, "2,0" );
        WebLabel hueSuffix = new WebLabel ( "°" );
        hueSuffix.setDrawShade ( true );
        colorsPanel.add ( hueSuffix, "4,0" );

        // Saturation
        WebLabel saturationButton = new WebLabel ( "S:" );
        saturationButton.setDrawShade ( true );
        colorsPanel.add ( saturationButton, "0,2" );
        saturationField = new WebTextField ();
        colorsPanel.add ( saturationField, "2,2" );
        WebLabel saturationSuffix = new WebLabel ( "%" );
        saturationSuffix.setDrawShade ( true );
        colorsPanel.add ( saturationSuffix, "4,2" );

        // Brightness
        WebLabel brightnessButton = new WebLabel ( "B:" );
        brightnessButton.setDrawShade ( true );
        colorsPanel.add ( brightnessButton, "0,4" );
        brightnessField = new WebTextField ();
        colorsPanel.add ( brightnessField, "2,4" );
        WebLabel brightnessSuffix = new WebLabel ( "%" );
        brightnessSuffix.setDrawShade ( true );
        colorsPanel.add ( brightnessSuffix, "4,4" );

        final CaretListener hsbListener = new CaretListener ()
        {
            public void caretUpdate ( CaretEvent e )
            {
                if ( !adjustingText )
                {
                    palette.removeChangeListener ( paletteListener );

                    try
                    {
                        float h = ( float ) Integer.parseInt ( hueField.getText () ) / 360;
                        float s = ( float ) Integer.parseInt ( saturationField.getText () ) / 100;
                        float b = ( float ) Integer.parseInt ( brightnessField.getText () ) / 100;
                        color = new HSBColor ( h, s, b ).getColor ();
                        updateColors ( color, UpdateSource.hsbField );
                    }
                    catch ( Throwable ex )
                    {
                        //
                    }

                    palette.addChangeListener ( paletteListener );
                }
            }
        };
        hueField.addCaretListener ( hsbListener );
        saturationField.addCaretListener ( hsbListener );
        brightnessField.addCaretListener ( hsbListener );


        colorsPanel.add ( new WebSeparator ( WebSeparator.HORIZONTAL ), "0,6,4,6" );


        WebLabel redButton = new WebLabel ( "R:" );
        redButton.setDrawShade ( true );
        colorsPanel.add ( redButton, "0,8" );
        redField = new WebTextField ();
        redField.setColumns ( 3 );
        colorsPanel.add ( redField, "2,8" );

        WebLabel greenButton = new WebLabel ( "G:" );
        greenButton.setDrawShade ( true );
        colorsPanel.add ( greenButton, "0,10" );
        greenField = new WebTextField ();
        greenField.setColumns ( 3 );
        colorsPanel.add ( greenField, "2,10" );

        WebLabel blueButton = new WebLabel ( "B:" );
        blueButton.setDrawShade ( true );
        colorsPanel.add ( blueButton, "0,12" );
        blueField = new WebTextField ();
        blueField.setColumns ( 3 );
        colorsPanel.add ( blueField, "2,12" );

        final CaretListener rgbListener = new CaretListener ()
        {
            public void caretUpdate ( CaretEvent e )
            {
                if ( !adjustingText )
                {
                    palette.removeChangeListener ( paletteListener );

                    try
                    {
                        int r = Integer.parseInt ( redField.getText () );
                        int g = Integer.parseInt ( greenField.getText () );
                        int b = Integer.parseInt ( blueField.getText () );
                        color = new Color ( r, g, b );
                        updateColors ( color, UpdateSource.rgbField );
                    }
                    catch ( Throwable ex )
                    {
                        //
                    }

                    palette.addChangeListener ( paletteListener );
                }
            }
        };
        redField.addCaretListener ( rgbListener );
        greenField.addCaretListener ( rgbListener );
        blueField.addCaretListener ( rgbListener );


        infoPanel.add ( new WebSeparator ( WebSeparator.HORIZONTAL ), "0,9,2,9" );


        WebLabel hexLabel = new WebLabel ( "#" );
        hexLabel.setDrawShade ( true );
        infoPanel.add ( hexLabel, "0,11" );
        hexColor = new WebTextField ();
        updateHexField ( color );
        final CaretListener hexListener = new CaretListener ()
        {
            public void caretUpdate ( CaretEvent e )
            {
                if ( !adjustingText )
                {
                    palette.removeChangeListener ( paletteListener );

                    try
                    {
                        color = ColorUtils.parseHexColor ( hexColor.getText () );
                        updateColors ( color, UpdateSource.hexField );
                    }
                    catch ( Throwable ex )
                    {
                        //
                    }

                    palette.addChangeListener ( paletteListener );
                }
            }
        };
        hexColor.addCaretListener ( hexListener );
        //        hexColor.addActionListener ( new ActionListener()
        //        {
        //            public void actionPerformed ( ActionEvent e )
        //            {
        //                updateColorFields ( DesignerUtils.hexToColor ( hexColor.getText () ) );
        //            }
        //        } );
        infoPanel.add ( hexColor, "2,11" );


        // Buttons
        if ( showButtonsPanel )
        {
            add ( getButtonsPanel (), "0,1,4,1" );
        }


        setOldColor ( oldColor );
        setColor ( color );
    }

    private WebPanel getButtonsPanel ()
    {
        if ( buttonsPanel == null )
        {
            buttonsPanel = createButtonsPanel ();
        }
        return buttonsPanel;
    }

    private WebPanel createButtonsPanel ()
    {
        final WebPanel buttonsPanel = new WebPanel ( new ToolbarLayout ( 2, ToolbarLayout.HORIZONTAL ) );
        buttonsPanel.setOpaque ( false );
        buttonsPanel.setMargin ( 5, 0, 3, 0 );

        final WebCheckBox webOnly = new WebCheckBox ();
        webOnly.setLanguage ( "weblaf.colorchooser.webonly" );
        webOnly.setSelected ( isWebOnlyColors () );
        webOnly.setMargin ( 0, 5, 0, 5 );
        webOnly.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                setWebOnlyColors ( webOnly.isSelected () );
            }
        } );
        buttonsPanel.add ( webOnly );

        final WebButton ok = new WebButton ();
        ok.setLanguage ( "weblaf.colorchooser.choose" );
        ok.addHotkey ( WebColorChooserPanel.this, Hotkey.ENTER );
        if ( StyleConstants.highlightControlButtons )
        {
            ok.setShineColor ( StyleConstants.greenHighlight );
        }
        ok.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                result = StyleConstants.OK_OPTION;
                fireOkPressed ( e );
            }
        } );
        palette.getColorChooser ().addMouseListener ( new MouseAdapter ()
        {
            public void mouseClicked ( MouseEvent e )
            {
                if ( e.getClickCount () == 2 )
                {
                    ok.doClick ( 0 );
                }
            }
        } );
        buttonsPanel.add ( ok, ToolbarLayout.END );

        final WebButton reset = new WebButton ();
        reset.setLanguage ( "weblaf.colorchooser.reset" );
        reset.addHotkey ( WebColorChooserPanel.this, Hotkey.ALT_R );
        if ( StyleConstants.highlightControlButtons )
        {
            reset.setShineColor ( StyleConstants.blueHighlight );
        }
        reset.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                setColor ( getOldColor () );
                fireResetPressed ( e );
            }
        } );
        buttonsPanel.add ( reset, ToolbarLayout.END );

        final WebButton cancel = new WebButton ();
        cancel.setLanguage ( "weblaf.colorchooser.cancel" );
        cancel.addHotkey ( WebColorChooserPanel.this, Hotkey.ESCAPE );
        if ( StyleConstants.highlightControlButtons )
        {
            cancel.setShineColor ( StyleConstants.redHighlight );
        }
        cancel.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                result = StyleConstants.CANCEL_OPTION;
                setColor ( getOldColor () );
                fireCancelPressed ( e );
            }
        } );
        buttonsPanel.add ( cancel, ToolbarLayout.END );

        SwingUtils.equalizeComponentsSize ( ok, reset, cancel );
        return buttonsPanel;
    }

    public boolean isShowButtonsPanel ()
    {
        return showButtonsPanel;
    }

    public void setShowButtonsPanel ( boolean showButtonsPanel )
    {
        if ( this.showButtonsPanel != showButtonsPanel )
        {
            this.showButtonsPanel = showButtonsPanel;
            if ( showButtonsPanel )
            {
                add ( getButtonsPanel (), "0,1,4,1" );
            }
            else
            {
                remove ( getButtonsPanel () );
            }
            revalidate ();
        }
    }

    public boolean isWebOnlyColors ()
    {
        return webOnlyColors;
    }

    public void setWebOnlyColors ( boolean webOnlyColors )
    {
        this.webOnlyColors = webOnlyColors;
        palette.setWebOnlyColors ( webOnlyColors );
        lineColorChooser.setWebOnlyColors ( webOnlyColors );
    }

    private void updateColors ( Color color, UpdateSource updateSource )
    {
        adjustingText = true;

        // Updating view
        if ( !updateSource.equals ( UpdateSource.palette ) )
        {
            updateView ( color );
        }

        // Updating color field
        if ( !updateSource.equals ( UpdateSource.doubleField ) )
        {
            updateDoubleColorField ( color );
        }

        // Updating HSB fields
        if ( !updateSource.equals ( UpdateSource.hsbField ) )
        {
            updateHSBFields ( color );
        }

        // Updating RGB fields
        if ( !updateSource.equals ( UpdateSource.rgbField ) )
        {
            updateRGBFields ( color );
        }

        // Updating HEX fields
        if ( !updateSource.equals ( UpdateSource.hexField ) )
        {
            updateHexField ( color );
        }

        adjustingText = false;

        // Informing about state change
        fireStateChanged ();
    }

    private void updateView ( Color color )
    {
        lineColorChooser.setColor ( color );
        palette.setColor ( color );
    }

    private void updateDoubleColorField ( Color color )
    {
        doubleColorField.setNewColor ( color );
    }

    private void updateHexField ( Color color )
    {
        hexColor.setText ( ColorUtils.getHexColor ( color ) );
    }

    private void updateRGBFields ( Color color )
    {
        redField.setText ( "" + color.getRed () );
        greenField.setText ( "" + color.getGreen () );
        blueField.setText ( "" + color.getBlue () );
    }

    private void updateHSBFields ( Color color )
    {
        float[] values = Color.RGBtoHSB ( color.getRed (), color.getGreen (), color.getBlue (), null );
        hueField.setText ( "" + lineColorChooser.getHue () );
        saturationField.setText ( "" + Math.round ( 100 * values[ 1 ] ) );
        brightnessField.setText ( "" + Math.round ( 100 * values[ 2 ] ) );
    }

    public Color getColor ()
    {
        return color;
    }

    public void setColor ( Color color )
    {
        this.color = color;
        setOldColor ( color );
        updateColors ( color, UpdateSource.outer );
    }

    public Color getOldColor ()
    {
        return oldColor;
    }

    public void setOldColor ( Color oldColor )
    {
        this.oldColor = oldColor;
        doubleColorField.setOldColor ( oldColor );
    }

    public void resetResult ()
    {
        setResult ( StyleConstants.NONE_OPTION );
    }

    public void setResult ( int result )
    {
        this.result = result;
    }

    public int getResult ()
    {
        return result;
    }

    public void addChangeListener ( ChangeListener changeListener )
    {
        changeListeners.add ( changeListener );
    }

    public void removeChangeListener ( ChangeListener changeListener )
    {
        changeListeners.remove ( changeListener );
    }

    private void fireStateChanged ()
    {
        ChangeEvent changeEvent = new ChangeEvent ( WebColorChooserPanel.this );
        for ( ChangeListener listener : CollectionUtils.copy ( changeListeners ) )
        {
            listener.stateChanged ( changeEvent );
        }
    }

    public void addColorChooserListener ( ColorChooserListener colorChooserListener )
    {
        colorChooserListeners.add ( colorChooserListener );
    }

    public void removeColorChooserListener ( ColorChooserListener colorChooserListener )
    {
        colorChooserListeners.remove ( colorChooserListener );
    }

    private void fireOkPressed ( ActionEvent actionEvent )
    {
        for ( ColorChooserListener listener : CollectionUtils.copy ( colorChooserListeners ) )
        {
            listener.okPressed ( actionEvent );
        }
    }

    private void fireResetPressed ( ActionEvent actionEvent )
    {
        for ( ColorChooserListener listener : CollectionUtils.copy ( colorChooserListeners ) )
        {
            listener.resetPressed ( actionEvent );
        }
    }

    private void fireCancelPressed ( ActionEvent actionEvent )
    {
        for ( ColorChooserListener listener : CollectionUtils.copy ( colorChooserListeners ) )
        {
            listener.cancelPressed ( actionEvent );
        }
    }

    private enum UpdateSource
    {
        outer,
        palette,
        doubleField,
        hsbField,
        rgbField,
        hexField
    }
}
