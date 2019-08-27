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

import com.alee.api.annotations.NotNull;
import com.alee.laf.label.WLabelUI;

import javax.swing.*;

/**
 * Pluggable look and feel interface for {@link WebStyledLabel} component.
 *
 * @param <C> component type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebStyledLabel">How to use WebStyledLabel</a>
 * @see WebStyledLabel
 */
public abstract class WStyledLabelUI<C extends WebStyledLabel> extends WLabelUI<C> implements SwingConstants
{
    @NotNull
    @Override
    public String getPropertyPrefix ()
    {
        return "StyledLabel.";
    }

    @Override
    protected void installDefaults ()
    {
        // Installing label defaults
        super.installDefaults ();

        // Installing styled label defaults
        label.setWrap ( TextWrap.mixed );
        label.setHorizontalTextAlignment ( -1 );
        label.setVerticalTextAlignment ( CENTER );
        label.setRows ( 0 );
        label.setMinimumRows ( 0 );
        label.setMaximumRows ( 0 );
    }
}