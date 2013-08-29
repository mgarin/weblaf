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

package com.alee.laf.spinner;

import com.alee.laf.StyleConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.text.WebTextFieldUI;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSpinnerUI;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * User: mgarin Date: 25.07.11 Time: 17:10
 */

public class WebSpinnerUI extends BasicSpinnerUI implements ShapeProvider
{
    private static final ImageIcon UP_ICON = new ImageIcon ( WebSpinnerUI.class.getResource ( "icons/up.png" ) );
    private static final ImageIcon DOWN_ICON = new ImageIcon ( WebSpinnerUI.class.getResource ( "icons/down.png" ) );

    private boolean drawBorder = StyleConstants.drawBorder;
    private boolean drawFocus = StyleConstants.drawFocus;
    private int round = StyleConstants.smallRound;
    private int shadeWidth = StyleConstants.shadeWidth;

    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( JComponent c )
    {
        return new WebSpinnerUI ();
    }

    @Override
    public void installUI ( JComponent c )
    {
        super.installUI ( c );

        // Default settings
        SwingUtils.setOrientation ( spinner );
        spinner.setBackground ( Color.WHITE );
        spinner.setOpaque ( false );

        // Updating border
        updateBorder ( drawBorder );
    }

    @Override
    public Shape provideShape ()
    {
        return LafUtils.getWebBorderShape ( spinner, getShadeWidth (), getRound () );
    }

    private void updateBorder ( boolean drawBorder )
    {
        if ( spinner != null )
        {
            if ( drawBorder )
            {
                spinner.setBorder ( BorderFactory.createEmptyBorder ( shadeWidth + 2, shadeWidth + 2, shadeWidth + 2, shadeWidth + 2 ) );
            }
            else
            {
                spinner.setBorder ( BorderFactory.createEmptyBorder ( 1, 1, 1, 1 ) );
            }
        }
    }

    public int getShadeWidth ()
    {
        return shadeWidth;
    }

    public void setShadeWidth ( int shadeWidth )
    {
        this.shadeWidth = shadeWidth;
        updateBorder ( drawBorder );
    }

    public int getRound ()
    {
        return round;
    }

    public void setRound ( int round )
    {
        this.round = round;
        updateBorder ( drawBorder );
    }

    public boolean isDrawBorder ()
    {
        return drawBorder;
    }

    public void setDrawBorder ( boolean drawBorder )
    {
        this.drawBorder = drawBorder;
        updateBorder ( drawBorder );
    }

    public boolean isDrawFocus ()
    {
        return drawFocus;
    }

    public void setDrawFocus ( boolean drawFocus )
    {
        this.drawFocus = drawFocus;
    }

    @Override
    public void paint ( Graphics g, JComponent c )
    {
        // Border, background and shade
        LafUtils.drawWebStyle ( ( Graphics2D ) g, c,
                drawFocus && SwingUtils.hasFocusOwner ( spinner ) ? StyleConstants.fieldFocusColor : StyleConstants.shadeColor, shadeWidth,
                round );

        super.paint ( g, c );
    }

    @Override
    protected Component createNextButton ()
    {
        WebButton nextButton = WebButton.createIconWebButton ( UP_ICON, StyleConstants.smallRound, 1, 2 );
        nextButton.setLeftRightSpacing ( 1 );
        nextButton.setDrawFocus ( false );
        nextButton.setFocusable ( false );

        nextButton.setName ( "Spinner.nextButton" );
        installNextButtonListeners ( nextButton );

        return nextButton;
    }

    @Override
    protected Component createPreviousButton ()
    {
        WebButton previousButton = WebButton.createIconWebButton ( DOWN_ICON, StyleConstants.smallRound, 1, 2 );
        previousButton.setLeftRightSpacing ( 1 );
        previousButton.setDrawFocus ( false );
        previousButton.setFocusable ( false );

        previousButton.setName ( "Spinner.previousButton" );
        installPreviousButtonListeners ( previousButton );

        return previousButton;
    }

    @Override
    protected JComponent createEditor ()
    {
        JComponent editor = super.createEditor ();
        installFieldUI ( ( ( JSpinner.DefaultEditor ) editor ).getTextField (), spinner );
        return editor;
    }

    public static void installFieldUI ( JFormattedTextField field, final JSpinner spinner )
    {
        field.setMargin ( new Insets ( 0, 0, 0, 0 ) );
        field.setBorder ( BorderFactory.createEmptyBorder ( 0, 0, 0, 0 ) );
        field.setUI ( new WebTextFieldUI ( field, false ) );
        field.setOpaque ( true );
        field.setBackground ( Color.WHITE );
        field.addFocusListener ( new FocusAdapter ()
        {
            @Override
            public void focusGained ( FocusEvent e )
            {
                spinner.repaint ();
            }

            @Override
            public void focusLost ( FocusEvent e )
            {
                spinner.repaint ();
            }
        } );
    }
}
