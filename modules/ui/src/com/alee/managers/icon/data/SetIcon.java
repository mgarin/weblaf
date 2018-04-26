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

package com.alee.managers.icon.data;

import com.alee.managers.icon.IconManager;
import com.alee.painter.decoration.DecorationException;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.content.AbstractIconContent;
import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;

/**
 * {@link IconManager} set icon content implementation.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-IconManager">How to use IconManager</a>
 * @see IconManager
 */
@XStreamAlias ( "SetIcon" )
public class SetIcon<C extends JComponent, D extends IDecoration<C, D>, I extends SetIcon<C, D, I>> extends AbstractIconContent<C, D, I>
{
    /**
     * todo 1. Rename to something more convenient
     */

    /**
     * Unique set icon ID.
     */
    @XStreamAsAttribute
    protected String icon;

    /**
     * Returns set icon ID.
     *
     * @return set icon ID
     */
    protected String getIconId ()
    {
        if ( !TextUtils.isEmpty ( icon ) )
        {
            return icon;
        }
        else
        {
            throw new DecorationException ( "Set icon ID must be specified" );
        }
    }

    @Override
    protected Icon getIcon ( final C c, final D d )
    {
        return IconManager.getIcon ( getIconId () );
    }
}