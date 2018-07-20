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

package com.alee.laf.combobox;

import com.alee.utils.ReflectUtils;
import com.alee.utils.swing.EditabilityListener;
import com.alee.utils.swing.VisibilityListener;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;

/**
 * Pluggable look and feel interface for {@link JComboBox} component.
 *
 * @author Mikle Garin
 */
public abstract class WComboBoxUI extends BasicComboBoxUI
{
    /**
     * Returns whether or not wide popup is allowed.
     *
     * @return {@code true} if wide popup is allowed, {@code false} otherwise
     */
    public abstract boolean isWidePopup ();

    /**
     * Sets whether or not wide popup is allowed.
     *
     * @param wide whether or not wide popup is allowed
     */
    public abstract void setWidePopup ( boolean wide );

    /**
     * Returns combobox popup list.
     *
     * @return combobox popup list
     */
    public abstract JList getListBox ();

    /**
     * Returns the area that is reserved for drawing currently selected item.
     *
     * @return area that is reserved for drawing currently selected item
     */
    public abstract Rectangle getValueBounds ();

    /**
     * Adds combobox editability listener.
     *
     * @param listener combobox editability listener to add
     */
    public abstract void addEditabilityListener ( EditabilityListener listener );

    /**
     * Removes combobox editability listener.
     *
     * @param listener combobox editability listener to remove
     */
    public abstract void removeEditabilityListener ( EditabilityListener listener );

    /**
     * Adds combobox popup visibility listener.
     *
     * @param listener combobox popup visibility listener to add
     */
    public abstract void addPopupVisibilityListener ( VisibilityListener listener );

    /**
     * Removes combobox popup visibility listener.
     *
     * @param listener combobox popup visibility listener to remove
     */
    public abstract void removePopupVisibilityListener ( VisibilityListener listener );

    /**
     * Forces UI to update renderer size.
     * This will reset size cache and perform combobox visual update.
     */
    public void updateRendererSize ()
    {
        resetRendererSize ();
        comboBox.revalidate ();
        comboBox.repaint ();
    }

    /**
     * Resets cached renderer size.
     */
    protected void resetRendererSize ()
    {
        isMinimumSizeDirty = true;
        ReflectUtils.setFieldValueSafely ( this, "isDisplaySizeDirty", true );
    }
}