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

import com.alee.api.annotations.NotNull;

import javax.swing.*;
import java.awt.event.MouseAdapter;

/**
 * Default {@link WebDocumentPane} tab title {@link JComponent} provider.
 *
 * @param <T> {@link DocumentData} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDocumentPane">How to use WebDocumentPane</a>
 * @see TabTitleComponent
 * @see TabTitleComponentProvider
 * @see WebDocumentPane
 */
public class DefaultTabTitleComponentProvider<T extends DocumentData> implements TabTitleComponentProvider<T>
{
    @NotNull
    @Override
    public JComponent createTabTitleComponent ( @NotNull final PaneData<T> paneData, @NotNull final T document,
                                                @NotNull final MouseAdapter mouseAdapter )
    {
        return new TabTitleComponent<T> ( paneData, document, mouseAdapter );
    }
}