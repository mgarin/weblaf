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

package com.alee.laf.tree;

import com.alee.painter.SectionPainter;

import javax.swing.*;
import java.awt.*;

/**
 * Base interface for {@link JTree} drop location painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */

public interface ITreeDropLocationPainter<C extends JTree, U extends WTreeUI> extends SectionPainter<C, U>
{
    /**
     * Prepares painter to paint tree drop location.
     *
     * @param location drop location
     */
    public void prepareToPaint ( JTree.DropLocation location );

    /**
     * Returns drop view bounds.
     *
     * @param location drop location
     * @return drop view bounds
     */
    public Rectangle getDropViewBounds ( JTree.DropLocation location );
}