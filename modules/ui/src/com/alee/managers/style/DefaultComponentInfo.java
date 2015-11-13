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

import com.alee.utils.LafUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
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
    protected static final String visibleColor = "black";
    protected static final String hiddenColor = "180,180,180";
    protected static final String styleIdColor = "30,110,30";
    protected static final String marginColor = "190,190,0";
    protected static final String paddingColor = "0,150,70";

    @Override
    public ImageIcon getIcon ( final StyleableComponent type, final T component )
    {
        return null;
    }

    @Override
    public String getText ( final StyleableComponent type, final T component )
    {
        final String titleColor = component.isShowing () ? visibleColor : hiddenColor;
        final String title = "{" + ReflectUtils.getClassName ( component.getClass () ) + ":c(" + titleColor + ")}";

        final String style = " [ {" + StyleId.get ( component ).getCompleteId () + ":b;c(" + styleIdColor + ")} ]";

        final Insets margin = LafUtils.getMargin ( component );
        final String mtext = renderInsets ( margin, marginColor );

        final Insets padding = LafUtils.getPadding ( component );
        final String ptext = renderInsets ( padding, paddingColor );

        return title + style + mtext + ptext;
    }

    /**
     * Creates and returns insets text.
     *
     * @param insets insets to render
     * @param color  text color
     * @return insets text
     */
    protected String renderInsets ( final Insets insets, final String color )
    {
        if ( !SwingUtils.isEmpty ( insets ) )
        {
            return " [ {" + InsetsConverter.insetsToString ( insets ) + ":b;c(" + color + ")} ]";
        }
        else
        {
            return "";
        }
    }
}