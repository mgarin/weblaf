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

package com.alee.managers.style.skin.web.data.check;

import com.alee.api.Identifiable;
import com.alee.api.Mergeable;
import com.alee.managers.style.skin.web.data.decoration.IDecoration;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * @author Mikle Garin
 */

public interface ICheckStateIcon<E extends JComponent, D extends IDecoration<E, D>, I extends ICheckStateIcon<E, D, I>>
        extends Serializable, Cloneable, Mergeable<I>, Identifiable
{
    /**
     * Paints check state icon.
     *
     * @param g2d     graphics context
     * @param bounds  painting bounds
     * @param c       painted component
     * @param d       painted decoration state
     * @param content content bounds
     */
    public void paint ( Graphics2D g2d, Rectangle bounds, E c, D d, Rectangle content );
}