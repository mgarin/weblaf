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

import com.alee.demo.DemoApplication;
import com.alee.demo.skin.DemoIcons;
import com.alee.demo.skin.DemoStyles;
import com.alee.laf.combobox.ComboBoxCellParameters;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.combobox.WebComboBoxRenderer;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.style.Skin;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.utils.CoreSwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * {@link com.alee.demo.DemoApplication} skin chooser.
 *
 * @author Mikle Garin
 */
public final class SkinChooserTool extends WebPanel
{
    /**
     * Constructs new {@link SkinChooserTool}.
     */
    public SkinChooserTool ()
    {
        super ( StyleId.panelTransparent, new BorderLayout ( 0, 0 ) );

        // Skin chooser combobox
        final WebComboBox chooser = new WebComboBox ( DemoStyles.toolCombobox, DemoApplication.skins );
        chooser.setLanguage ( "demo.tool.skin" );
        chooser.setSelectedItem ( StyleManager.getSkin () );
        chooser.setRenderer ( new WebComboBoxRenderer<Skin, JList, ComboBoxCellParameters<Skin, JList>> ()
        {
            @Override
            protected Icon iconForValue ( final ComboBoxCellParameters<Skin, JList> parameters )
            {
                return parameters.index () == -1 ? DemoIcons.brush16 : parameters.value ().getIcon ();
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
                        StyleManager.setSkin ( ( Skin ) chooser.getSelectedItem () );
                    }
                } );
            }
        } );
        add ( chooser, BorderLayout.CENTER );
    }
}