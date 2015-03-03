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

package com.alee.examples.groups.colorchooser;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.examples.content.FeatureState;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.colorchooser.WebColorChooserDialog;
import com.alee.managers.settings.SettingsManager;
import com.alee.utils.ImageUtils;
import com.alee.utils.swing.DialogOptions;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Mikle Garin
 */

public class ColorChooserDialogExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Color chooser dialog";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled color chooser dialog";
    }

    @Override
    public FeatureState getFeatureState ()
    {
        return FeatureState.beta;
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Initial color
        final Color initialColor = SettingsManager.get ( "ColorChooserDialogExample.color", Color.WHITE );

        // Simple color chooser
        final WebButton colorChooserButton = new WebButton ( getColorText ( initialColor ), ImageUtils.createColorIcon ( initialColor ) );
        colorChooserButton.setLeftRightSpacing ( 0 );
        colorChooserButton.setMargin ( 0, 0, 0, 3 );
        colorChooserButton.addActionListener ( new ActionListener ()
        {
            private WebColorChooserDialog colorChooser = null;
            private Color lastColor = initialColor;

            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( colorChooser == null )
                {
                    colorChooser = new WebColorChooserDialog ( owner );
                }
                colorChooser.setColor ( lastColor );
                colorChooser.setVisible ( true );

                if ( colorChooser.getResult () == DialogOptions.OK_OPTION )
                {
                    final Color color = colorChooser.getColor ();
                    lastColor = color;

                    colorChooserButton.setIcon ( ImageUtils.createColorIcon ( color ) );
                    colorChooserButton.setText ( getColorText ( color ) );
                }
            }
        } );

        return new GroupPanel ( colorChooserButton );
    }

    private String getColorText ( final Color color )
    {
        return color.getRed () + ", " + color.getGreen () + ", " + color.getBlue ();
    }
}