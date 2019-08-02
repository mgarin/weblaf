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

import javax.swing.plaf.basic.BasicColorChooserUI;
import java.awt.*;

/**
 * Pluggable look and feel interface for {@link WebColorChooser} component.
 *
 * @author Mikle Garin
 */
public abstract class WColorChooserUI extends BasicColorChooserUI
{
    /**
     * Returns whether or not buttons panel should be displayed.
     *
     * @return {@code true} if buttons panel should be displayed, {@code false} otherwise
     */
    public abstract boolean isShowButtonsPanel ();

    /**
     * Sets whether or not buttons panel should be displayed.
     *
     * @param display whether or not buttons panel should be displayed
     */
    public abstract void setShowButtonsPanel ( boolean display );

    /**
     * Returns whether or not web-only colors should be displayed.
     *
     * @return {@code true} if web-only colors should be displayed, {@code false} otherwise
     */
    public abstract boolean isWebOnlyColors ();

    /**
     * Sets whether or not web-only colors should be displayed.
     *
     * @param webOnly whether or not web-only colors should be displayed
     */
    public abstract void setWebOnlyColors ( boolean webOnly );

    /**
     * Returns previously selected color.
     *
     * @return previously selected color
     */
    public abstract Color getPreviousColor ();

    /**
     * Sets previously selected color.
     *
     * @param previous previously selected color
     */
    public abstract void setPreviousColor ( Color previous );

    /**
     * Resets color chooser result.
     */
    public abstract void resetResult ();

    /**
     * Sets color chooser result.
     *
     * @param result color chooser result
     */
    public abstract void setResult ( int result );

    /**
     * Returns color chooser result.
     *
     * @return color chooser result
     */
    public abstract int getResult ();

    /**
     * Adds color chooser listener.
     *
     * @param listener color chooser listener to add
     */
    public abstract void addColorChooserListener ( ColorChooserListener listener );

    /**
     * Removes color chooser listener.
     *
     * @param listener color chooser listener to remove
     */
    public abstract void removeColorChooserListener ( ColorChooserListener listener );
}