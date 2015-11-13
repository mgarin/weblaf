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

import com.alee.managers.glasspane.WebGlassPane;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleableComponent;
import com.alee.utils.LafUtils;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Default Styleable JComponent information provider.
 *
 * @author Mikle Garin
 */

public class StyleableInfo<T extends JComponent> extends AbstractComponentInfo<T>
{
    @Override
    public ImageIcon getIcon ( final StyleableComponent type, final T component )
    {
        if ( component instanceof WebGlassPane )
        {
            return glassPaneType;
        }
        else
        {
            return type.getIcon ();
        }
    }

    @Override
    public String getText ( final StyleableComponent type, final T component )
    {
        final String title = "{" + ReflectUtils.getClassName ( component.getClass () ) + ":c(" + getTitleColor ( component ) + ")}";

        final String style = " [ {" + StyleId.get ( component ).getCompleteId () + ":b;c(" + styleIdColor + ")} ]";

        final Insets margin = LafUtils.getMargin ( component );
        final String mtext = renderInsets ( margin, marginColor );

        final Insets padding = LafUtils.getPadding ( component );
        final String ptext = renderInsets ( padding, paddingColor );

        return title + style + mtext + ptext;
    }
}