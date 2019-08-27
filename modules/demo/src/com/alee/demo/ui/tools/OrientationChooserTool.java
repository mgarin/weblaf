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

package com.alee.demo.ui.tools;

import com.alee.api.annotations.NotNull;
import com.alee.demo.skin.DemoIcons;
import com.alee.demo.skin.DemoStyles;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.combobox.ComboBoxCellParameters;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.combobox.WebComboBoxRenderer;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.language.LM;
import com.alee.managers.language.Language;
import com.alee.managers.language.LanguageListener;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CoreSwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.ComponentOrientation.LEFT_TO_RIGHT;
import static java.awt.ComponentOrientation.RIGHT_TO_LEFT;

/**
 * Demo application orientation chooser.
 *
 * @author Mikle Garin
 */
public final class OrientationChooserTool extends WebPanel
{
    /**
     * Constructs new {@link OrientationChooserTool}.
     */
    public OrientationChooserTool ()
    {
        super ( StyleId.panelTransparent, new BorderLayout ( 0, 0 ) );

        // Orientation chooser combobox
        final WebComboBox chooser = new WebComboBox ( DemoStyles.toolCombobox, new OrientationModel () );
        chooser.setLanguage ( "demo.tool.orientation" );
        chooser.setSelectedItem ( WebLookAndFeel.getOrientation () );
        chooser.setRenderer ( new WebComboBoxRenderer<ComponentOrientation, JList, ComboBoxCellParameters<ComponentOrientation, JList>> ()
        {
            @Override
            protected Icon iconForValue ( final ComboBoxCellParameters<ComponentOrientation, JList> parameters )
            {
                return parameters.value () == LEFT_TO_RIGHT ? DemoIcons.ltr16 : DemoIcons.rtl16;
            }

            @Override
            protected String textForValue ( final ComboBoxCellParameters<ComponentOrientation, JList> parameters )
            {
                return LM.get ( "demo.tool.orientation." + ( parameters.value () == LEFT_TO_RIGHT ? "ltr" : "rtl" ) );
            }
        } );
        chooser.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                // Executing later to avoid any possible interferences
                CoreSwingUtils.invokeLater ( new Runnable ()
                {
                    @Override
                    public void run ()
                    {
                        WebLookAndFeel.setOrientation ( ( ComponentOrientation ) chooser.getSelectedItem () );
                    }
                } );
            }
        } );
        add ( chooser, BorderLayout.CENTER );
    }

    /**
     * Custom orientation combobox model.
     * It is created to properly update combobox size on language changes.
     */
    public class OrientationModel extends DefaultComboBoxModel implements LanguageListener
    {
        /**
         * Constructs new orientation combobox model.
         */
        public OrientationModel ()
        {
            super ( CollectionUtils.asVector ( LEFT_TO_RIGHT, RIGHT_TO_LEFT ) );
            OrientationChooserTool.this.addLanguageListener ( this );
        }

        @Override
        public void languageChanged ( @NotNull final Language oldLanguage, @NotNull final Language newLanguage )
        {
            super.fireContentsChanged ( this, 0, getSize () );
        }
    }
}