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

package com.alee.extended.inspector.info;

import com.alee.utils.ReflectUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Default AWT Component information provider.
 *
 * @param <C> component type
 * @author Mikle Garin
 */
public class AWTComponentPreview<C extends Component> extends AbstractComponentPreview<C>
{
    @Override
    public Icon getIconImpl ( final C component )
    {
        final Icon icon;
        if ( component instanceof Frame )
        {
            icon = frameType;
        }
        else if ( component instanceof Dialog )
        {
            icon = dialogType;
        }
        else if ( component instanceof Window )
        {
            icon = windowType;
        }
        else
        {
            icon = unknownType;
        }
        return icon;
    }

    @Override
    public String getText ( final C component )
    {
        return "{" + ReflectUtils.getCompleteClassName ( component.getClass () ) + ":c(" + getTitleColor ( component ) + ")}";
    }
}