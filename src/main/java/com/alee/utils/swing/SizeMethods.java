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

package com.alee.utils.swing;

import com.alee.laf.panel.WebPanel;

/**
 * This interface provides a set of methods that should be added into components that support custom size methods.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public interface SizeMethods extends SwingMethods
{
    public int getPreferredWidth ();

    public WebPanel setPreferredWidth ( int preferredWidth );

    public int getPreferredHeight ();

    public WebPanel setPreferredHeight ( int preferredHeight );

    public int getMinimumWidth ();

    public WebPanel setMinimumWidth ( int minimumWidth );

    public int getMinimumHeight ();

    public WebPanel setMinimumHeight ( int minimumHeight );
}