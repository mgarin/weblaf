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

import java.awt.event.ActionEvent;
import java.util.EventListener;

/**
 * This listener allows you to easilly handle color chooser button events.
 *
 * @author Mikle Garin
 */
public interface ColorChooserListener extends EventListener
{
    /**
     * Called when "ok" button is pressed in color chooser.
     *
     * @param e ok button event
     */
    public void okPressed ( ActionEvent e );

    /**
     * Called when "reset" button is pressed in color chooser.
     *
     * @param e reset button event
     */
    public void resetPressed ( ActionEvent e );

    /**
     * Called when "cancel" button is pressed in color chooser.
     *
     * @param e cancel button event
     */
    public void cancelPressed ( ActionEvent e );
}
