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

package com.alee.managers.style;

import com.alee.utils.CompareUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.xml.InsetsConverter;

import javax.swing.*;
import java.awt.*;

/**
 * Default component information provider.
 *
 * @author Mikle Garin
 */

public class DefaultComponentInfo<T extends JComponent> implements ComponentInfo<T>
{
    @Override
    public ImageIcon getIcon ( final StyleableComponent type, final T component )
    {
        return null;
    }

    @Override
    public String getText ( final StyleableComponent type, final T component )
    {
        final String black = "black";
        final String gray = "180,180,180";
        final String green = "30,110,30";

        final String titleColor = component.isShowing () ? black : gray;
        final String title = "{" + ReflectUtils.getClassName ( component.getClass () ) + ":c(" + titleColor + ")}";

        final String style = " [ {" + StyleId.get ( component ).getCompleteId () + ":b;c(" + green + ")} ]";

        final Insets i = component.getInsets ();
        final boolean ei = i.top == 0 && i.left == 0 && i.bottom == 0 && i.right == 0;
        final String insets = !ei ? " b[" + InsetsConverter.insetsToString ( i ) + "]" : "";

        final Dimension s = component.isShowing () ? component.getSize () : null;
        final Dimension ps = component.getPreferredSize ();
        final String size;
        if ( CompareUtils.equals ( s, ps ) )
        {
            size = " s&p[" + ps.width + "x" + ps.height + "]";
        }
        else
        {
            final String currentSize = s != null ? " s[" + s.width + "x" + s.height + "]" : "";
            final String preferredSize = " p[" + ps.width + "x" + ps.height + "]";
            size = currentSize + preferredSize;
        }

        return title + style + insets + size;
    }
}