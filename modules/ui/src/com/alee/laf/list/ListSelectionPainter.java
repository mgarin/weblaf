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

package com.alee.laf.list;

import com.alee.painter.decoration.AbstractSectionDecorationPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * List selection painter based on {@link AbstractSectionDecorationPainter}.
 * It is used within {@link ListPainter} to paint selection.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */

public class ListSelectionPainter<C extends JList, U extends WebListUI, D extends IDecoration<C, D>>
        extends AbstractSectionDecorationPainter<C, U, D> implements IListSelectionPainter<C, U>
{
    @Override
    public String getSectionId ()
    {
        return "selection";
    }

    @Override
    protected boolean isFocused ()
    {
        return false;
    }
}