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

package com.alee.laf.panel;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.PartialDecoration;
import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JPanel component painters.
 *
 * @author Mikle Garin
 */

public interface PanelPainter<E extends JPanel> extends Painter<E>, PartialDecoration, SpecificPainter
{
    /**
     * Sets whether panel decoration should be painted or not.
     *
     * @param undecorated whether panel decoration should be painted or not
     */
    public void setUndecorated ( boolean undecorated );

    /**
     * Sets whether focus should be painted or not.
     * Panel focus is displayed when either panel or one of its children are focused.
     *
     * @param paint whether focus should be painted or not
     */
    public void setPaintFocus ( final boolean paint );
}