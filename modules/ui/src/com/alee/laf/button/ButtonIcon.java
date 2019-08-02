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

package com.alee.laf.button;

import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.content.AbstractIconContent;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;

/**
 * Button icon content implementation.
 * It provides appropriate {@link AbstractButton} state icon.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */
@XStreamAlias ( "ButtonIcon" )
public class ButtonIcon<C extends AbstractButton, D extends IDecoration<C, D>, I extends ButtonIcon<C, D, I>>
        extends AbstractIconContent<C, D, I>
{
    @Override
    protected Icon getIcon ( final C c, final D d )
    {
        Icon icon = c.getIcon ();
        if ( icon != null )
        {
            final ButtonModel model = c.getModel ();
            Icon tmpIcon = null;
            Icon selectedIcon = null;
            if ( model.isSelected () )
            {
                selectedIcon = c.getSelectedIcon ();
                if ( selectedIcon != null )
                {
                    icon = selectedIcon;
                }
            }
            if ( !model.isEnabled () )
            {
                if ( model.isSelected () )
                {
                    tmpIcon = c.getDisabledSelectedIcon ();
                    if ( tmpIcon == null )
                    {
                        tmpIcon = selectedIcon;
                    }
                }

                if ( tmpIcon == null )
                {
                    tmpIcon = c.getDisabledIcon ();
                }
            }
            else if ( model.isPressed () && model.isArmed () )
            {
                tmpIcon = c.getPressedIcon ();
            }
            else if ( c.isRolloverEnabled () && model.isRollover () )
            {
                if ( model.isSelected () )
                {
                    tmpIcon = c.getRolloverSelectedIcon ();
                    if ( tmpIcon == null )
                    {
                        tmpIcon = selectedIcon;
                    }
                }
                if ( tmpIcon == null )
                {
                    tmpIcon = c.getRolloverIcon ();
                }
            }
            if ( tmpIcon != null )
            {
                icon = tmpIcon;
            }
        }
        return icon;
    }
}