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

package com.alee.extended.tab;

/**
 * Extension for WebDocumentPane documents state listener.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDocumentPane">How to use WebDocumentPane</a>
 * @see com.alee.extended.tab.WebDocumentPane
 */

public abstract class DocumentAdapter<T extends DocumentData> implements DocumentListener<T>
{
    @Override
    public void opened ( final T document, final PaneData<T> pane, final int index )
    {
        // Do nothing by default
    }

    @Override
    public void selected ( final T document, final PaneData<T> pane, final int index )
    {
        // Do nothing by default
    }

    @Override
    public boolean closing ( final T document, final PaneData<T> pane, final int index )
    {
        // Return true by default
        return true;
    }

    @Override
    public void closed ( final T document, final PaneData<T> pane, final int index )
    {
        // Do nothing by default
    }
}