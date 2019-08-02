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

package com.alee.utils.swing.extensions;

import java.awt.*;

/**
 * Common implementations for {@link FontMethods} interface methods.
 *
 * @author Mikle Garin
 * @see FontMethods
 */
public final class FontMethodsImpl
{
    /**
     * Returns whether component font is plain or not.
     *
     * @param component component to process
     * @return {@code true} if component font is plain, {@code false} otherwise
     */
    public static boolean isPlainFont ( final Component component )
    {
        return !( component != null && component.getFont () != null ) || component.getFont ().isPlain ();
    }

    /**
     * Changes font to plain for the specified component.
     *
     * @param component component to modify
     * @param <C>       component type
     * @return modified component
     */
    public static <C extends Component> C setPlainFont ( final Component component )
    {
        return setPlainFont ( component, true );
    }

    /**
     * Changes font to plain for the specified component.
     *
     * @param component component to modify
     * @param apply     whether to apply font changes or not
     * @param <C>       component type
     * @return modified component
     */
    public static <C extends Component> C setPlainFont ( final Component component, final boolean apply )
    {
        if ( apply && component != null && component.getFont () != null )
        {
            component.setFont ( component.getFont ().deriveFont ( Font.PLAIN ) );
        }
        return ( C ) component;
    }

    /**
     * Returns whether component font is bold or not.
     *
     * @param component component to process
     * @return {@code true} if component font is bold, {@code false} otherwise
     */
    public static boolean isBoldFont ( final Component component )
    {
        return component != null && component.getFont () != null && component.getFont ().isBold ();
    }

    /**
     * Changes font to bold for the specified component.
     *
     * @param component component to modify
     * @param <C>       component type
     * @return modified component
     */
    public static <C extends Component> C setBoldFont ( final Component component )
    {
        return setBoldFont ( component, true );
    }

    /**
     * Changes font to bold for the specified component.
     *
     * @param component component to modify
     * @param apply     whether to apply font changes or not
     * @param <C>       component type
     * @return modified component
     */
    public static <C extends Component> C setBoldFont ( final Component component, final boolean apply )
    {
        if ( apply && component != null && component.getFont () != null )
        {
            component.setFont ( component.getFont ().deriveFont ( Font.BOLD ) );
        }
        return ( C ) component;
    }

    /**
     * Returns whether component font is italic or not.
     *
     * @param component component to process
     * @return {@code true} if component font is italic, {@code false} otherwise
     */
    public static boolean isItalicFont ( final Component component )
    {
        return component != null && component.getFont () != null && component.getFont ().isItalic ();
    }

    /**
     * Changes font to italic for the specified component.
     *
     * @param component component to modify
     * @param <C>       component type
     * @return modified component
     */
    public static <C extends Component> C setItalicFont ( final Component component )
    {
        return setItalicFont ( component, true );
    }

    /**
     * Changes font to italic for the specified component.
     *
     * @param component component to modify
     * @param apply     whether to apply font changes or not
     * @param <C>       component type
     * @return modified component
     */
    public static <C extends Component> C setItalicFont ( final Component component, final boolean apply )
    {
        if ( apply && component != null && component.getFont () != null )
        {
            component.setFont ( component.getFont ().deriveFont ( Font.ITALIC ) );
        }
        return ( C ) component;
    }

    /**
     * Sets font  style for the specified component.
     *
     * @param component component to modify
     * @param bold      whether should set bold font or not
     * @param italic    whether should set italic font or not
     * @param <C>       component type
     * @return modified component
     */
    public static <C extends Component> C setFontStyle ( final Component component, final boolean bold, final boolean italic )
    {
        final int style = bold && italic ? Font.BOLD | Font.ITALIC : bold ? Font.BOLD : italic ? Font.ITALIC : Font.PLAIN;
        return setFontStyle ( component, style );
    }

    /**
     * Sets font  style for the specified component.
     *
     * @param component component to modify
     * @param style     new style
     * @param <C>       component type
     * @return modified component
     */
    public static <C extends Component> C setFontStyle ( final Component component, final int style )
    {
        if ( component != null && component.getFont () != null )
        {
            component.setFont ( component.getFont ().deriveFont ( style ) );
        }
        return ( C ) component;
    }

    /**
     * Returns font size of the specified component.
     *
     * @param component component to process
     * @return font size of the specified component
     */
    public static int getFontSize ( final Component component )
    {
        if ( component != null && component.getFont () != null )
        {
            return component.getFont ().getSize ();
        }
        return -1;
    }

    /**
     * Sets font size of the specified component.
     *
     * @param component component to modify
     * @param fontSize  new font size
     * @param <C>       component type
     * @return modified component
     */
    public static <C extends Component> C setFontSize ( final Component component, final int fontSize )
    {
        if ( component != null && component.getFont () != null )
        {
            component.setFont ( component.getFont ().deriveFont ( ( float ) fontSize ) );
        }
        return ( C ) component;
    }

    /**
     * Changes font size of the specified component.
     *
     * @param component component to modify
     * @param change    font size change amount
     * @param <C>       component type
     * @return modified component
     */
    public static <C extends Component> C changeFontSize ( final Component component, final int change )
    {
        if ( component != null && component.getFont () != null )
        {
            final Font font = component.getFont ();
            component.setFont ( font.deriveFont ( ( float ) font.getSize () + change ) );
        }
        return ( C ) component;
    }

    /**
     * Sets font size and style for the specified component.
     *
     * @param component component to modify
     * @param fontSize  new font size
     * @param bold      whether should set bold font or not
     * @param italic    whether should set italic font or not
     * @param <C>       component type
     * @return modified component
     */
    public static <C extends Component> C setFontSizeAndStyle ( final Component component, final int fontSize, final boolean bold,
                                                                final boolean italic )
    {
        final int style = bold && italic ? Font.BOLD | Font.ITALIC : bold ? Font.BOLD : italic ? Font.ITALIC : Font.PLAIN;
        return setFontSizeAndStyle ( component, fontSize, style );
    }

    /**
     * Sets font size and style for the specified component.
     *
     * @param component component to modify
     * @param fontSize  new font size
     * @param style     new style
     * @param <C>       component type
     * @return modified component
     */
    public static <C extends Component> C setFontSizeAndStyle ( final Component component, final int fontSize, final int style )
    {
        if ( component != null && component.getFont () != null )
        {
            component.setFont ( component.getFont ().deriveFont ( style, ( float ) fontSize ) );
        }
        return ( C ) component;
    }

    /**
     * Returns component font name.
     *
     * @param component component to process
     * @return component font name
     */
    public static String getFontName ( final Component component )
    {
        if ( component != null && component.getFont () != null )
        {
            return component.getFont ().getFontName ();
        }
        return null;
    }

    /**
     * Sets component font name.
     *
     * @param component component font name
     * @param fontName  new font name
     * @param <C>       component type
     * @return modified component
     */
    public static <C extends Component> C setFontName ( final Component component, final String fontName )
    {
        if ( component != null && component.getFont () != null )
        {
            final Font oldFont = component.getFont ();
            component.setFont ( new Font ( fontName, oldFont.getStyle (), oldFont.getSize () ) );
        }
        return ( C ) component;
    }
}