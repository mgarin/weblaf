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

package com.alee.extended.label;

import com.alee.api.jdk.Objects;
import com.alee.api.merge.MergeBehavior;
import com.alee.api.merge.RecursiveMerge;
import com.alee.api.merge.behavior.PreserveOnMerge;
import com.alee.utils.CollectionUtils;
import com.alee.utils.ReflectUtils;

import java.awt.*;
import java.util.List;

/**
 * This class represents custom text style for WebStyledLabel component.
 * It contains various style settings supported by the styled label UI.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebStyledLabel">How to use WebStyledLabel</a>
 * @see com.alee.extended.label.WebStyledLabel
 */
public class StyleRange implements MergeBehavior<StyleRange>, Cloneable
{
    /**
     * Text style start index.
     */
    protected final int startIndex;

    /**
     * Text style length.
     */
    protected final int length;

    /**
     * Text foreground.
     */
    protected final Color foreground;

    /**
     * Text background.
     */
    protected final Color background;

    /**
     * Basic text style.
     * Either {@link Font#ITALIC} or {@link Font#BOLD} or their combination.
     */
    @PreserveOnMerge
    protected final int style;

    /**
     * Custom text styles.
     *
     * @see com.alee.extended.label.CustomStyle
     */
    protected final List<CustomStyle> customStyles;

    /**
     * Constructs new StyleRange based on another StyleRange settings.
     *
     * @param styleRange style range
     */
    public StyleRange ( final StyleRange styleRange )
    {
        this ( styleRange.getStartIndex (), styleRange.getLength (), styleRange );
    }

    /**
     * Constructs new StyleRange based on another StyleRange settings but with new start index and length.
     *
     * @param startIndex text style start index
     * @param length     text style length
     * @param styleRange style range
     */
    public StyleRange ( final int startIndex, final int length, final StyleRange styleRange )
    {
        this ( startIndex, length, styleRange.getStyle (), styleRange.getForeground (),
                styleRange.getBackground (), CollectionUtils.copy ( styleRange.getCustomStyle () ) );
    }

    /**
     * Constructs new empty StyleRange.
     *
     * @param startIndex   text style start index
     * @param length       text style length
     * @param customStyles custom text styles
     */
    public StyleRange ( final int startIndex, final int length, final CustomStyle... customStyles )
    {
        this ( startIndex, length, -1, null, null, customStyles );
    }

    /**
     * Constructs new StyleRange with the specified settings
     *
     * @param startIndex   text style start index
     * @param length       text style length
     * @param style        basic text style
     * @param customStyles custom text styles
     */
    public StyleRange ( final int startIndex, final int length, final int style, final CustomStyle... customStyles )
    {
        this ( startIndex, length, style, null, null, customStyles );
    }

    /**
     * Constructs new StyleRange with the specified settings
     *
     * @param startIndex   text style start index
     * @param length       text style length
     * @param foreground   text foreground color
     * @param customStyles custom text styles
     */
    public StyleRange ( final int startIndex, final int length, final Color foreground, final CustomStyle... customStyles )
    {
        this ( startIndex, length, -1, foreground, null, customStyles );
    }

    /**
     * Constructs new StyleRange with the specified settings
     *
     * @param startIndex   text style start index
     * @param length       text style length
     * @param foreground   text foreground color
     * @param background   text background color
     * @param customStyles custom text styles
     */
    public StyleRange ( final int startIndex, final int length, final Color foreground, final Color background,
                        final CustomStyle... customStyles )
    {
        this ( startIndex, length, -1, foreground, background, customStyles );
    }

    /**
     * Constructs new StyleRange with the specified settings
     *
     * @param startIndex   text style start index
     * @param length       text style length
     * @param style        basic text style
     * @param foreground   text foreground color
     * @param customStyles custom text styles
     */
    public StyleRange ( final int startIndex, final int length, final int style, final Color foreground, final CustomStyle... customStyles )
    {
        this ( startIndex, length, style, foreground, null, customStyles );
    }

    /**
     * Constructs new StyleRange with the specified settings
     *
     * @param startIndex   text style start index
     * @param length       text style length
     * @param style        basic text style
     * @param foreground   text foreground color
     * @param background   text background color
     * @param customStyles custom text styles
     */
    public StyleRange ( final int startIndex, final int length, final int style, final Color foreground, final Color background,
                        final CustomStyle... customStyles )
    {
        this ( startIndex, length, style, foreground, background, CollectionUtils.asList ( customStyles ) );
    }

    /**
     * Constructs new StyleRange with the specified settings
     *
     * @param startIndex   text style start index
     * @param length       text style length
     * @param style        basic text style
     * @param foreground   text foreground color
     * @param background   text background color
     * @param customStyles custom text styles
     */
    public StyleRange ( final int startIndex, final int length, final int style, final Color foreground, final Color background,
                        final List<CustomStyle> customStyles )
    {
        if ( startIndex < 0 )
        {
            throw new IllegalArgumentException ( "Style start index cannot be less than zero" );
        }
        if ( length <= 0 )
        {
            throw new IllegalArgumentException ( "Style length cannot be zero or less than zero" );
        }
        if ( Objects.notEquals ( style, -1, Font.PLAIN, Font.BOLD, Font.ITALIC, Font.BOLD | Font.ITALIC ) )
        {
            throw new IllegalArgumentException ( "Unknown font style: " + style );
        }
        this.startIndex = startIndex;
        this.length = length;
        this.foreground = foreground;
        this.background = background;
        this.style = style;
        this.customStyles = customStyles;
    }

    /**
     * Returns text style start index.
     *
     * @return text style start index
     */
    public int getStartIndex ()
    {
        return startIndex;
    }

    /**
     * Returns text style length.
     *
     * @return text style length
     */
    public int getLength ()
    {
        return length;
    }

    /**
     * Returns basic text style.
     *
     * @return basic text style
     */
    public int getStyle ()
    {
        return style;
    }

    /**
     * Returns foreground color.
     *
     * @return foreground color.
     */
    public Color getForeground ()
    {
        return foreground;
    }

    /**
     * Returns background color.
     *
     * @return background color.
     */
    public Color getBackground ()
    {
        return background;
    }

    /**
     * Returns whether the text is strike through or not.
     *
     * @return true if the text is strike through, false otherwise
     */
    public boolean isStrikeThrough ()
    {
        return customStyles.contains ( CustomStyle.strikeThrough );
    }

    /**
     * Returns whether the text is double strike through or not.
     *
     * @return true if the text is double strike through, false otherwise
     */
    public boolean isDoubleStrikeThrough ()
    {
        return customStyles.contains ( CustomStyle.doubleStrikeThrough );
    }

    /**
     * Returns whether the line is waved or not.
     *
     * @return true if the line is waved, false otherwise
     */
    public boolean isWaved ()
    {
        return customStyles.contains ( CustomStyle.waved );
    }

    /**
     * Returns whether the text is underlined or not.
     *
     * @return true if the text is underlined, false otherwise
     */
    public boolean isUnderlined ()
    {
        return customStyles.contains ( CustomStyle.underlined );
    }

    /**
     * Returns whether the text is superscript or not.
     *
     * @return true if the text is superscript, false otherwise
     */
    public boolean isSuperscript ()
    {
        return customStyles.contains ( CustomStyle.superscript );
    }

    /**
     * Returns whether the text is subscript or not.
     *
     * @return true if the text is subscript, false otherwise
     */
    public boolean isSubscript ()
    {
        return customStyles.contains ( CustomStyle.subscript );
    }

    /**
     * Returns custom styles applied to the text.
     *
     * @return custom styles applied to the text
     */
    public List<CustomStyle> getCustomStyle ()
    {
        return customStyles;
    }

    @Override
    public StyleRange merge ( final RecursiveMerge merge, final Class type, final StyleRange object, final int depth )
    {
        final StyleRange result = merge.mergeFields ( type, this, object, depth );

        /**
         * Special merge algorithm for {@link Font} style field.
         * It is important to avoid simple overwriting of the {@link #style} field in this class.
         */
        final int fontStyle;
        if ( object.style != -1 )
        {
            if ( this.style != -1 )
            {
                fontStyle = this.style | object.style;
            }
            else
            {
                fontStyle = object.style;
            }
        }
        else
        {
            fontStyle = this.style;
        }
        ReflectUtils.setFieldValueSafely ( result, "style", fontStyle );

        return result;
    }
}