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

import com.alee.demo.skin.DemoStyles;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.combobox.WebComboBoxRenderer;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.style.StyleId;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Demo application language chooser.
 *
 * @author Mikle Garin
 */

public final class LanguageChooserTool extends WebPanel
{
    /**
     * Constructs new orientation chooser tool.
     */
    public LanguageChooserTool ()
    {
        super ( StyleId.panelTransparent, new BorderLayout ( 0, 0 ) );

        // Orientation chooser combobox
        final WebComboBox language = new WebComboBox ( DemoStyles.toolCombobox, LanguageManager.getSupportedLanguages () );
        language.setToolTip ( "demo.tool.language" );
        language.setSelectedItem ( LanguageManager.getLanguage () );
        language.setRenderer ( new WebComboBoxRenderer ()
        {
            @Override
            protected Icon iconForValue ( final JList list, final Object value, final int index, final boolean isSelected,
                                          final boolean hasFocus )
            {
                return LanguageManager.getLanguageIcon ( ( String ) value );
            }

            @Override
            protected String textForValue ( final JList list, final Object value, final int index, final boolean isSelected,
                                            final boolean hasFocus )
            {
                return LanguageManager.getLanguageTitle ( ( String ) value );
            }
        } );
        language.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                // Executing later to avoid any possible interferences
                SwingUtils.invokeLater ( new Runnable ()
                {
                    @Override
                    public void run ()
                    {
                        LanguageManager.setLanguage ( ( String ) language.getSelectedItem () );
                    }
                } );
            }
        } );
        add ( language, BorderLayout.CENTER );
    }
}