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

package com.alee.laf.combobox.behavior;

import com.alee.extended.behavior.Behavior;
import com.alee.utils.MathUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * Combobox behavior that changes selection on mouse wheel scroll when combobox is focused.
 *
 * @author Mikle Garin
 */

public class ComboBoxMouseWheelScrollBehavior implements MouseWheelListener, Behavior
{
    /**
     * Combobox using this behavior.
     */
    protected final JComboBox comboBox;

    /**
     * Constructs new combobox mouse wheel scroll behavior.
     *
     * @param comboBox combobox using this behavior
     */
    public ComboBoxMouseWheelScrollBehavior ( final JComboBox comboBox )
    {
        super ();
        this.comboBox = comboBox;
    }

    @Override
    public void mouseWheelMoved ( final MouseWheelEvent e )
    {
        if ( comboBox.isEnabled () && SwingUtils.hasFocusOwner ( comboBox ) )
        {
            // Changing selection in case index actually changed
            final int index = comboBox.getSelectedIndex ();
            final int newIndex = MathUtils.limit ( 0, index + e.getWheelRotation (), comboBox.getModel ().getSize () - 1 );
            if ( newIndex != index )
            {
                comboBox.setSelectedIndex ( newIndex );
            }
        }
    }

    /**
     * Installs behavior into combobox and ensures that it is the only one installed.
     *
     * @param comboBox combobox to modify
     * @return installed behavior
     */
    public static ComboBoxMouseWheelScrollBehavior install ( final JComboBox comboBox )
    {
        // Uninstalling old behavior first
        uninstall ( comboBox );

        // Installing new behavior
        final ComboBoxMouseWheelScrollBehavior behavior = new ComboBoxMouseWheelScrollBehavior ( comboBox );
        comboBox.addMouseWheelListener ( behavior );
        return behavior;
    }

    /**
     * Uninstalls all behaviors from the specified combobox.
     *
     * @param comboBox combobox to modify
     */
    public static void uninstall ( final JComboBox comboBox )
    {
        for ( final MouseWheelListener listener : comboBox.getMouseWheelListeners () )
        {
            if ( listener instanceof ComboBoxMouseWheelScrollBehavior )
            {
                comboBox.removeMouseWheelListener ( listener );
            }
        }
    }

    /**
     * Returns whether the specified combobox has any behaviors installed or not.
     *
     * @param comboBox combobox to process
     * @return true if the specified combobox has any behaviors installed, false otherwise
     */
    public static boolean isInstalled ( final JComboBox comboBox )
    {
        for ( final MouseWheelListener listener : comboBox.getMouseWheelListeners () )
        {
            if ( listener instanceof ComboBoxMouseWheelScrollBehavior )
            {
                return true;
            }
        }
        return false;
    }
}