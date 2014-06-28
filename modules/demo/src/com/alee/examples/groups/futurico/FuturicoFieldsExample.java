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

package com.alee.examples.groups.futurico;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.painter.NinePatchStatePainter;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.text.WebPasswordField;
import com.alee.laf.text.WebTextField;
import com.alee.utils.NinePatchUtils;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 14.03.12 Time: 14:37
 */

public class FuturicoFieldsExample extends DefaultExample
{
    public static final ImageIcon searchIcon = new ImageIcon ( FuturicoFieldsExample.class.getResource ( "icons/field/search.png" ) );
    public static final ImageIcon pSearchIcon = new ImageIcon ( FuturicoFieldsExample.class.getResource ( "icons/field/psearch.png" ) );

    @Override
    public String getTitle ()
    {
        return "Futurico text fields";
    }

    @Override
    public String getDescription ()
    {
        return "Futurico-styled text fields";
    }

    @Override
    public boolean isFillWidth ()
    {
        return true;
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Painters used for example fields
        final NinePatchStatePainter painter1 = NinePatchUtils.loadNinePatchStatePainter ( getResource ( "field.xml" ) );

        // Text field
        final WebTextField textField = new WebTextField ( "Styled text field" );
        textField.setPainter ( painter1 );
        textField.setForeground ( Color.WHITE );

        // Password field
        final WebPasswordField passwordField = new WebPasswordField ( "Styled password field" );
        passwordField.setPainter ( painter1 );
        passwordField.setForeground ( Color.WHITE );

        // Field with trailing component
        final WebTextField componentField = new WebTextField ( "Styled text field with leading component" );
        componentField.setFieldMargin ( 0, 0, 0, 6 );
        componentField.setPainter ( NinePatchUtils.loadNinePatchStatePainter ( getResource ( "sfield.xml" ) ) );
        componentField.setForeground ( Color.WHITE );
        final WebButton sButton = new WebButton ();
        sButton.setFocusable ( false );
        sButton.setUndecorated ( true );
        sButton.setLeftRightSpacing ( 0 );
        sButton.setMoveIconOnPress ( false );
        sButton.setCursor ( Cursor.getDefaultCursor () );
        sButton.setIcon ( searchIcon );
        sButton.setPressedIcon ( pSearchIcon );
        componentField.setTrailingComponent ( sButton );

        return new GroupPanel ( false, textField, passwordField, componentField );
    }
}