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
import com.alee.extended.colorchooser.DoubleColorFieldListener;
import com.alee.extended.layout.TableLayout;
import com.alee.extended.layout.ToolbarLayout;
import com.alee.global.StyleConstants;
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
import com.alee.utils.swing.DialogOptions;

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

public class WebColorChooserPanel extends WebPanel implements DialogOptions
{
    private final List<ChangeListener> changeListeners = new ArrayList<ChangeListener> ( 1 );
    private final List<ColorChooserListener> colorChooserListeners = new ArrayList<ColorChooserListener> ( 1 );

    private boolean showButtonsPanel = false;
    private Boolean webOnlyColors = false;

    private Color oldColor = Color.WHITE;
    private Color color = Color.WHITE;

    private int result = NONE_OPTION;

    private boolean adjustingText = false;

    private final PaletteColorChooser palette;
    private final LineColorChooser lineColorChooser;
    private final DoubleColorField doubleColorField;

    private final WebTextField hueField;
    private final WebTextField saturationField;
    private final WebTextField brightnessField;
    private final WebTextField redField;
    private final WebTextField greenField;
    private final WebTextField blueField;
    private final WebTextField hexColor;

    private WebPanel buttonsPanel;

    public WebColorChooserPanel ()
    {
        this ( false );
    }

    public WebColorChooserPanel ( final boolean showButtonsPanel )
    {
        super ();

        this.showButtonsPanel = showButtonsPanel;

        // Panel settings
        setOpaque ( false );
        setWebColoredBackground ( false );
        setMargin ( 2, 5, 2, 5 );
        setLayout ( new TableLayout ( new double[][]{ { TableLayout.FILL, 4, TableLayout.PREFERRED, 4, TableLayout.PREFERRED },
                { TableLayout.PREFERRED, TableLayout.PREFERRED } } ) );

        palette = new PaletteColorChooser ();
        palette.setOpaque ( false );
        palette.setWebOnlyColors ( isWebOnlyColors () );
        final ChangeListener paletteListener = new ChangeListener ()
        {
            @Override
            public void stateChanged ( final ChangeEvent e )
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
            @Override
            public void stateChanged ( final ChangeEvent e )
            {
                palette.setSideColor ( lineColorChooser.getColor () );
            }
        } );
        add ( lineColorChooser, "2,0" );


        final JPanel infoPanel = new JPanel ();
        infoPanel.setLayout ( new TableLayout ( new double[][]{ { TableLayout.PREFERRED, 4, TableLayout.FILL },
                { 3, TableLayout.FILL, 5, TableLayout.PREFERRED, 4, TableLayout.PREFERRED, 1, TableLayout.PREFERRED, 1,
                        TableLayout.PREFERRED, 1, TableLayout.PREFERRED, 3 } } ) );
        infoPanel.setOpaque ( false );
        add ( infoPanel, "4,0" );


        doubleColorField = new DoubleColorField ();
        doubleColorField.addDoubleColorFieldListener ( new DoubleColorFieldListener ()
        {
            @Override
            public void newColorPressed ( final Color newColor )
            {
                //
            }

            @Override
            public void oldColorPressed ( final Color currentColor )
            {
                setColor ( doubleColorField.getOldColor () );
            }
        } );
        updateDoubleColorField ( color );
        doubleColorField.setOldColor ( oldColor );
        infoPanel.add ( doubleColorField, "0,1,2,3" );


        infoPanel.add ( new WebSeparator ( WebSeparator.HORIZONTAL ), "0,5,2,5" );


        final JPanel colorsPanel = new JPanel ();
        colorsPanel.setLayout ( new TableLayout ( new double[][]{ { TableLayout.FILL, 5, TableLayout.PREFERRED, 4, TableLayout.PREFERRED },
                { TableLayout.PREFERRED, 0, TableLayout.PREFERRED, 0, TableLayout.PREFERRED, 1, TableLayout.PREFERRED, 1,
                        TableLayout.PREFERRED, 0, TableLayout.PREFERRED, 0, TableLayout.PREFERRED } } ) );
        colorsPanel.setOpaque ( false );
        infoPanel.add ( colorsPanel, "0,7,2,7" );

        // Hue
        final WebLabel hueButton = new WebLabel ( "H:" );
        hueButton.setDrawShade ( true );
        colorsPanel.add ( hueButton, "0,0" );
        hueField = new WebTextField ();
        colorsPanel.add ( hueField, "2,0" );
        final WebLabel hueSuffix = new WebLabel ( "°" );
        hueSuffix.setDrawShade ( true );
        colorsPanel.add ( hueSuffix, "4,0" );

        // Saturation
        final WebLabel saturationButton = new WebLabel ( "S:" );
        saturationButton.setDrawShade ( true );
        colorsPanel.add ( saturationButton, "0,2" );
        saturationField = new WebTextField ();
        colorsPanel.add ( saturationField, "2,2" );
        final WebLabel saturationSuffix = new WebLabel ( "%" );
        saturationSuffix.setDrawShade ( true );
        colorsPanel.add ( saturationSuffix, "4,2" );

        // Brightness
        final WebLabel brightnessButton = new WebLabel ( "B:" );
        brightnessButton.setDrawShade ( true );
        colorsPanel.add ( brightnessButton, "0,4" );
        brightnessField = new WebTextField ();
        colorsPanel.add ( brightnessField, "2,4" );
        final WebLabel brightnessSuffix = new WebLabel ( "%" );
        brightnessSuffix.setDrawShade ( true );
        colorsPanel.add ( brightnessSuffix, "4,4" );

        final CaretListener hsbListener = new CaretListener ()
        {
            @Override
            public void caretUpdate ( final CaretEvent e )
            {
                if ( !adjustingText )
                {
                    palette.removeChangeListener ( paletteListener );

                    try
                    {
                        final float h = ( float ) Integer.parseInt ( hueField.getText () ) / 360;
                        final float s = ( float ) Integer.parseInt ( saturationField.getText () ) / 100;
                        final float b = ( float ) Integer.parseInt ( brightnessField.getText () ) / 100;
                        color = new HSBColor ( h, s, b ).getColor ();
                        updateColors ( color, UpdateSource.hsbField );
                    }
                    catch ( final Throwable ex )
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


        final WebLabel redButton = new WebLabel ( "R:" );
        redButton.setDrawShade ( true );
        colorsPanel.add ( redButton, "0,8" );
        redField = new WebTextField ();
        redField.setColumns ( 3 );
        colorsPanel.add ( redField, "2,8" );

        final WebLabel greenButton = new WebLabel ( "G:" );
        greenButton.setDrawShade ( true );
        colorsPanel.add ( greenButton, "0,10" );
        greenField = new WebTextField ();
        greenField.setColumns ( 3 );
        colorsPanel.add ( greenField, "2,10" );

        final WebLabel blueButton = new WebLabel ( "B:" );
        blueButton.setDrawShade ( true );
        colorsPanel.add ( blueButton, "0,12" );
        blueField = new WebTextField ();
        blueField.setColumns ( 3 );
        colorsPanel.add ( blueField, "2,12" );

        final CaretListener rgbListener = new CaretListener ()
        {
            @Override
            public void caretUpdate ( final CaretEvent e )
            {
                if ( !adjustingText )
                {
                    palette.removeChangeListener ( paletteListener );

                    try
                    {
                        final int r = Integer.parseInt ( redField.getText () );
                        final int g = Integer.parseInt ( greenField.getText () );
                        final int b = Integer.parseInt ( blueField.getText () );
                        color = new Color ( r, g, b );
                        updateColors ( color, UpdateSource.rgbField );
                    }
                    catch ( final Throwable ex )
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


        final WebLabel hexLabel = new WebLabel ( "#" );
        hexLabel.setDrawShade ( true );
        infoPanel.add ( hexLabel, "0,11" );
        hexColor = new WebTextField ();
        updateHexField ( color );
        final CaretListener hexListener = new CaretListener ()
        {
            @Override
            public void caretUpdate ( final CaretEvent e )
            {
                if ( !adjustingText )
                {
                    palette.removeChangeListener ( paletteListener );

                    try
                    {
                        color = ColorUtils.parseHexColor ( hexColor.getText () );
                        updateColors ( color, UpdateSource.hexField );
                    }
                    catch ( final Throwable ex )
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
            @Override
            public void actionPerformed ( final ActionEvent e )
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
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                result = OK_OPTION;
                fireOkPressed ( e );
            }
        } );
        palette.getColorChooser ().addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mouseClicked ( final MouseEvent e )
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
            @Override
            public void actionPerformed ( final ActionEvent e )
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
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                result = CANCEL_OPTION;
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

    public void setShowButtonsPanel ( final boolean showButtonsPanel )
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

    public void setWebOnlyColors ( final boolean webOnlyColors )
    {
        this.webOnlyColors = webOnlyColors;
        palette.setWebOnlyColors ( webOnlyColors );
        lineColorChooser.setWebOnlyColors ( webOnlyColors );
    }

    private void updateColors ( final Color color, final UpdateSource updateSource )
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

    private void updateView ( final Color color )
    {
        lineColorChooser.setColor ( color );
        palette.setSideColor ( color );
        palette.setColor ( color );
    }

    private void updateDoubleColorField ( final Color color )
    {
        doubleColorField.setNewColor ( color );
    }

    private void updateHexField ( final Color color )
    {
        // Substring removes first # symbol
        hexColor.setText ( ColorUtils.getHexColor ( color ).substring ( 1 ) );
    }

    private void updateRGBFields ( final Color color )
    {
        redField.setText ( "" + color.getRed () );
        greenField.setText ( "" + color.getGreen () );
        blueField.setText ( "" + color.getBlue () );
    }

    private void updateHSBFields ( final Color color )
    {
        final float[] values = Color.RGBtoHSB ( color.getRed (), color.getGreen (), color.getBlue (), null );
        hueField.setText ( "" + lineColorChooser.getHue () );
        saturationField.setText ( "" + Math.round ( 100 * values[ 1 ] ) );
        brightnessField.setText ( "" + Math.round ( 100 * values[ 2 ] ) );
    }

    public Color getColor ()
    {
        return color;
    }

    public void setColor ( final Color color )
    {
        this.color = color;
        setOldColor ( color );
        updateColors ( color, UpdateSource.outer );
    }

    public Color getOldColor ()
    {
        return oldColor;
    }

    public void setOldColor ( final Color oldColor )
    {
        this.oldColor = oldColor;
        doubleColorField.setOldColor ( oldColor );
    }

    public void resetResult ()
    {
        setResult ( NONE_OPTION );
    }

    public void setResult ( final int result )
    {
        this.result = result;
    }

    public int getResult ()
    {
        return result;
    }

    public void addChangeListener ( final ChangeListener changeListener )
    {
        changeListeners.add ( changeListener );
    }

    public void removeChangeListener ( final ChangeListener changeListener )
    {
        changeListeners.remove ( changeListener );
    }

    private void fireStateChanged ()
    {
        final ChangeEvent changeEvent = new ChangeEvent ( WebColorChooserPanel.this );
        for ( final ChangeListener listener : CollectionUtils.copy ( changeListeners ) )
        {
            listener.stateChanged ( changeEvent );
        }
    }

    public void addColorChooserListener ( final ColorChooserListener colorChooserListener )
    {
        colorChooserListeners.add ( colorChooserListener );
    }

    public void removeColorChooserListener ( final ColorChooserListener colorChooserListener )
    {
        colorChooserListeners.remove ( colorChooserListener );
    }

    private void fireOkPressed ( final ActionEvent actionEvent )
    {
        for ( final ColorChooserListener listener : CollectionUtils.copy ( colorChooserListeners ) )
        {
            listener.okPressed ( actionEvent );
        }
    }

    private void fireResetPressed ( final ActionEvent actionEvent )
    {
        for ( final ColorChooserListener listener : CollectionUtils.copy ( colorChooserListeners ) )
        {
            listener.resetPressed ( actionEvent );
        }
    }

    private void fireCancelPressed ( final ActionEvent actionEvent )
    {
        for ( final ColorChooserListener listener : CollectionUtils.copy ( colorChooserListeners ) )
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
