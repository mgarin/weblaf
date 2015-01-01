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

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.SpecificPainter;

/**
 * Base interface for WebStyledLabel component painters.
 *
 * @author Mikle Garin
 */

public interface StyledLabelPainter<E extends WebStyledLabel> extends Painter<E>, SpecificPainter
{
    /**
     * Sets preferred row count.
     *
     * @param rows new preferred row count
     */
    public void setPreferredRowCount ( final int rows );

    /**
     * Sets whether color settings should be ignored or not.
     *
     * @param ignore whether color settings should be ignored or not
     */
    public void setIgnoreColorSettings ( final boolean ignore );

    /**
     * Sets subscript and superscript font ratio.
     *
     * @param ratio new subscript and superscript font ratio
     */
    public void setScriptFontRatio ( final float ratio );

    /**
     * Sets truncated text suffix.
     *
     * @param suffix new truncated text suffix
     */
    public void setTruncatedTextSuffix ( final String suffix );

    /**
     * Forces text ranges to be updated according to current style ranges and text.
     */
    public void updateTextRanges ();
}