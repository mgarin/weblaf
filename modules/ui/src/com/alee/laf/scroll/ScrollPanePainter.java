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

package com.alee.laf.scroll;

import com.alee.painter.decoration.AbstractContainerPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for JScrollPane component.
 * It is used as WebScrollPaneUI default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */

public class ScrollPanePainter<E extends JScrollPane, U extends WebScrollPaneUI, D extends IDecoration<E, D>>
        extends AbstractContainerPainter<E, U, D> implements IScrollPanePainter<E, U>
{
    @Override
    protected void orientationChange ()
    {
        // Updating scrollpane corners
        // todo updateCorners ();

        // Performing default actions
        super.orientationChange ();
    }
}