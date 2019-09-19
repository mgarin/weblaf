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

import com.alee.utils.ImageUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.xml.InsetsConverter;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract component information provider which provides style guidelines and some basic methods.
 *
 * @param <C> component type
 * @author Mikle Garin
 */
public abstract class AbstractComponentPreview<C extends Component> implements ComponentPreview<C>
{
    /**
     * Opaque icon.
     */
    public static final ImageIcon opaque = new ImageIcon ( AbstractComponentPreview.class.getResource ( "icons/opaque.png" ) );

    /**
     * Additional type icons.
     */
    public static final ImageIcon frameType = new ImageIcon ( AbstractComponentPreview.class.getResource ( "icons/frame.png" ) );
    public static final ImageIcon dialogType = new ImageIcon ( AbstractComponentPreview.class.getResource ( "icons/dialog.png" ) );
    public static final ImageIcon windowType = new ImageIcon ( AbstractComponentPreview.class.getResource ( "icons/window.png" ) );
    public static final ImageIcon glassPaneType = new ImageIcon ( AbstractComponentPreview.class.getResource ( "icons/glasspane.png" ) );
    public static final ImageIcon unknownType = new ImageIcon ( AbstractComponentPreview.class.getResource ( "icons/unknown.png" ) );

    /**
     * Merged icons cache.
     */
    protected static final Map<String, Icon> mergedCache = new HashMap<String, Icon> ( 30 );

    /**
     * Basic style guidelines.
     */
    protected static final String visibleColor = "black";
    protected static final String visibleAwtColor = "165,145,70";
    protected static final String hiddenColor = "180,180,180";
    protected static final String styleIdColor = "30,110,30";
    protected static final String marginColor = "190,190,0";
    protected static final String paddingColor = "0,150,70";

    @Override
    public Icon getIcon ( final C component )
    {
        // Retrieving component icon
        Icon icon = getIconImpl ( component );

        // Adding component opacity state icon
        if ( SwingUtils.isOpaque ( component ) )
        {
            final String key = icon.hashCode () + "," + opaque.hashCode ();
            if ( mergedCache.containsKey ( key ) )
            {
                icon = mergedCache.get ( key );
            }
            else
            {
                icon = ImageUtils.mergeIcons ( icon, opaque );
                mergedCache.put ( key, icon );
            }
        }

        return icon;
    }

    /**
     * Returns actual icon for the specified component.
     *
     * @param component component to provide icon for
     * @return actual icon for the specified component
     */
    protected abstract Icon getIconImpl ( C component );

    /**
     * Returns main title foreground color.
     *
     * @param component inspected component
     * @return main title foreground color
     */
    protected String getTitleColor ( final C component )
    {
        return component.isShowing () ? component instanceof JComponent ? visibleColor : visibleAwtColor : hiddenColor;
    }

    /**
     * Creates and returns {@link LayoutManager} text.
     *
     * @param layout {@link LayoutManager}
     * @return {@link LayoutManager} text
     */
    protected String renderLayout ( final LayoutManager layout )
    {
        return layout != null ? " {[" + ReflectUtils.getCompleteClassName ( layout ) + "]:b}" : "";
    }

    /**
     * Creates and returns {@link Insets} text.
     *
     * @param insets {@link Insets} to render
     * @param color  text color
     * @return {@link Insets} text
     */
    protected String renderInsets ( final Insets insets, final String color )
    {
        final String text;
        if ( !SwingUtils.isEmpty ( insets ) )
        {
            text = " {[ " + InsetsConverter.insetsToString ( insets ) + " ]:b;c(" + color + ")}";
        }
        else
        {
            text = "";
        }
        return text;
    }
}