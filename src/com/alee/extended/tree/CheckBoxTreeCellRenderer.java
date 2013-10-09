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

package com.alee.extended.tree;

import com.alee.extended.checkbox.WebTristateCheckBox;

import javax.swing.tree.TreeCellRenderer;

/**
 * Base for any checkbox tree cell renderer.
 *
 * @author Mikle Garin
 */

public interface CheckBoxTreeCellRenderer extends TreeCellRenderer
{
    /**
     * Returns gap between checkbox and actual cell renderer.
     *
     * @return gap between checkbox and actual cell renderer
     */
    public int getCheckBoxRendererGap ();

    /**
     * Sets gap between checkbox and actual cell renderer.
     *
     * @param checkBoxRendererGap new gap between checkbox and actual cell renderer
     */
    public void setCheckBoxRendererGap ( int checkBoxRendererGap );

    /**
     * Returns checkbox part width in this cell renderer.
     *
     * @return checkbox part width in this cell renderer
     */
    public int getCheckBoxWidth ();

    /**
     * Returns checkbox used for rendering.
     *
     * @return checkbox used for rendering
     */
    public WebTristateCheckBox getCheckBox ();
}