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

package com.alee.laf.tabbedpane;

import com.alee.painter.decoration.AbstractSectionDecorationPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Simple tab content painter based on {@link AbstractSectionDecorationPainter}.
 * It is used within {@link TabbedPanePainter} to tab content background.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */
public class TabContentPainter<C extends JTabbedPane, U extends WTabbedPaneUI, D extends IDecoration<C, D>>
        extends AbstractSectionDecorationPainter<C, U, D> implements ITabContentPainter<C, U>
{
    /**
     * Painted tab content index.
     */
    protected transient Integer row;

    @Override
    public String getSectionId ()
    {
        return "tab-content";
    }

    @Override
    protected boolean isFocused ()
    {
        return false;
    }

    @Override
    public void prepareToPaint ( final int row )
    {
        this.row = row;
        updateDecorationState ();
    }
}