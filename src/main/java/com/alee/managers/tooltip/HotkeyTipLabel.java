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

package com.alee.managers.tooltip;

import com.alee.laf.label.WebLabel;
import com.alee.utils.SwingUtils;

/**
 * User: mgarin Date: 24.09.12 Time: 16:11
 */

public class HotkeyTipLabel extends WebLabel
{
    public HotkeyTipLabel ()
    {
        super ();

        setForeground ( WebCustomTooltipStyle.hotkeyColor );
        SwingUtils.setBoldFont ( this );
        SwingUtils.changeFontSize ( this, -1 );

        setHorizontalAlignment ( WebLabel.CENTER );
        setVerticalAlignment ( WebLabel.CENTER );

        setPainter ( new HotkeyTipPainter () );
    }
}