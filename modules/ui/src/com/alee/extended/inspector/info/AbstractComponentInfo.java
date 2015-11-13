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

import com.alee.utils.SwingUtils;
import com.alee.utils.xml.InsetsConverter;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract component information provider which provides style guidelines and some basic methods.
 *
 * @author Mikle Garin
 */

public abstract class AbstractComponentInfo<T extends Component> implements ComponentInfo<T>
{
    /**
     * Additional type icons.
     */
    public static final ImageIcon frameType = new ImageIcon ( AbstractComponentInfo.class.getResource ( "icons/frame.png" ) );
    public static final ImageIcon dialogType = new ImageIcon ( AbstractComponentInfo.class.getResource ( "icons/dialog.png" ) );
    public static final ImageIcon windowType = new ImageIcon ( AbstractComponentInfo.class.getResource ( "icons/window.png" ) );
    public static final ImageIcon layeredPaneType = new ImageIcon ( AbstractComponentInfo.class.getResource ( "icons/layeredpane.png" ) );
    public static final ImageIcon glassPaneType = new ImageIcon ( AbstractComponentInfo.class.getResource ( "icons/glasspane.png" ) );
    public static final ImageIcon unknownType = new ImageIcon ( AbstractComponentInfo.class.getResource ( "icons/unknown.png" ) );

    /**
     * Basic style guidelines.
     */
    protected static final String visibleColor = "black";
    protected static final String visibleAwtColor = "165,145,70";
    protected static final String hiddenColor = "180,180,180";
    protected static final String styleIdColor = "30,110,30";
    protected static final String marginColor = "190,190,0";
    protected static final String paddingColor = "0,150,70";

    /**
     * Returns main title foreground color.
     *
     * @param component inspected component
     * @return main title foreground color
     */
    protected String getTitleColor ( final T component )
    {
        return component.isShowing () ? component instanceof JComponent ? visibleColor : visibleAwtColor : hiddenColor;
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