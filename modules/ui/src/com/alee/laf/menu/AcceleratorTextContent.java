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

package com.alee.laf.menu;

import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.content.AbstractTextContent;
import com.alee.utils.SwingUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import javax.swing.text.View;

/**
 * Menu item accelerator text content implementation.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */

@XStreamAlias ( "AcceleratorText" )
public class AcceleratorTextContent<E extends JMenuItem, D extends IDecoration<E, D>, I extends AcceleratorTextContent<E, D, I>>
        extends AbstractTextContent<E, D, I>
{
    @Override
    public String getId ()
    {
        return id != null ? id : "accelerator";
    }

    @Override
    protected boolean isHtmlText ( final E c, final D d )
    {
        return false;
    }

    @Override
    protected View getHtml ( final E c, final D d )
    {
        return null;
    }

    @Override
    protected String getText ( final E c, final D d )
    {
        final KeyStroke accelerator = c.getAccelerator ();
        return accelerator != null ? SwingUtils.hotkeyToString ( accelerator ) : null;
    }

    @Override
    protected int getMnemonicIndex ( final E c, final D d )
    {
        return -1;
    }
}