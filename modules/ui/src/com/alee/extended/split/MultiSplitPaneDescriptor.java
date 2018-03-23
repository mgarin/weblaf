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

package com.alee.extended.split;

import com.alee.managers.style.AbstractComponentDescriptor;
import com.alee.managers.style.StyleId;

/**
 * Custom descriptor for {@link WebMultiSplitPane} component.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebMultiSplitPane">How to use WebMultiSplitPane</a>
 * @see WebMultiSplitPane
 */

public final class MultiSplitPaneDescriptor extends AbstractComponentDescriptor<WebMultiSplitPane>
{
    /**
     * Constructs new descriptor for {@link WebMultiSplitPane} component.
     */
    public MultiSplitPaneDescriptor ()
    {
        super ( "multisplitpane", WebMultiSplitPane.class, "MultiSplitPaneUI", WMultiSplitPaneUI.class, WebMultiSplitPaneUI.class,
                StyleId.multisplitpane );
    }
}