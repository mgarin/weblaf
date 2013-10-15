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

package com.alee.utils.swing;

import java.awt.*;

/**
 * This interface provides a set of methods that should be added into components that support font and uses that font to render.
 * Basically all these methods are already implemented in SwingUtils but it is much easier to call them directly from component.
 *
 * @param <C> component type
 * @author Mikle Garin
 * @see SwingMethods
 * @see com.alee.utils.SwingUtils
 */

public interface FontMethods<C extends Component> extends SwingMethods
{
    /**
     * Sets plain font for the component.
     *
     * @return modified component
     */
    public C setPlainFont ();

    /**
     * Sets plain font for the component.
     *
     * @param apply whether to apply font changes or not
     * @return modified component
     */
    public C setPlainFont ( boolean apply );

    /**
     * Returns whether component font is plain or not.
     *
     * @return true if component font is plain, false otherwise
     */
    public boolean isPlainFont ();

    /**
     * Sets bold font for the component.
     *
     * @return modified component
     */
    public C setBoldFont ();

    /**
     * Sets bold font for the component.
     *
     * @param apply whether to apply font changes or not
     * @return modified component
     */
    public C setBoldFont ( boolean apply );

    /**
     * Returns whether component font is bold or not.
     *
     * @return true if component font is bold, false otherwise
     */
    public boolean isBoldFont ();

    /**
     * Sets italic or plain font for the component.
     *
     * @return modified component
     */
    public C setItalicFont ();

    /**
     * Sets italic or plain font for the component.
     *
     * @param apply whether to apply font changes or not
     * @return modified component
     */
    public C setItalicFont ( boolean apply );

    /**
     * Returns whether component font is italic or not.
     *
     * @return true if component font is italic, false otherwise
     */
    public boolean isItalicFont ();

    /**
     * Sets component font style.
     *
     * @param bold   whether should set bold font or not
     * @param italic whether should set italic font or not
     * @return modified component
     */
    public C setFontStyle ( boolean bold, boolean italic );

    /**
     * Sets component font style.
     *
     * @param style new style
     * @return modified component
     */
    public C setFontStyle ( int style );

    /**
     * Sets component font size.
     *
     * @param fontSize font size
     * @return modified component
     */
    public C setFontSize ( int fontSize );

    /**
     * Changes font size of the specified component.
     *
     * @param change font size change amount
     * @return modified component
     */
    public C changeFontSize ( int change );

    /**
     * Returns component font size.
     *
     * @return component font size
     */
    public int getFontSize ();

    /**
     * Sets font size and style for the specified component.
     *
     * @param fontSize new font size
     * @param bold     whether should set bold font or not
     * @param italic   whether should set italic font or not
     * @return modified component
     */
    public C setFontSizeAndStyle ( int fontSize, boolean bold, boolean italic );

    /**
     * Sets font size and style for the specified component.
     *
     * @param fontSize new font size
     * @param style    new style
     * @return modified component
     */
    public C setFontSizeAndStyle ( int fontSize, int style );

    /**
     * Sets component font name.
     *
     * @param fontName new font name
     * @return modified component
     */
    public C setFontName ( String fontName );

    /**
     * Returns component font name.
     *
     * @return component font name
     */
    public String getFontName ();
}