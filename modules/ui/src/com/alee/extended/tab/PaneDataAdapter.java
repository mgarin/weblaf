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

import java.awt.*;

/**
 * DocumentDataListener extension for single PaneData.
 *
 * @author Mikle Garin
 */

public class PaneDataAdapter<T extends DocumentData> implements DocumentDataListener<T>
{
    /**
     * PaneData listening to DocumentData changes.
     */
    private final PaneData paneData;

    /**
     * Constructs new PaneDataAdapter for the specified PaneData.
     *
     * @param paneData PaneData listening to DocumentData changes
     */
    public PaneDataAdapter ( final PaneData paneData )
    {
        super ();
        this.paneData = paneData;
    }

    /**
     * Returns PaneData listening to DocumentData changes.
     *
     * @return PaneData listening to DocumentData changes
     */
    public PaneData getPaneData ()
    {
        return paneData;
    }

    @Override
    public void titleChanged ( final T document )
    {
        paneData.updateTabTitleComponent ( document );
    }

    @Override
    public void backgroundChanged ( final T document, final Color oldBackground, final Color newBackground )
    {
        paneData.updateTabBackground ( document );
    }

    @Override
    public void contentChanged ( final T document, final Component oldComponent, final Component newComponent )
    {
        paneData.updateTabComponent ( document );
    }
}