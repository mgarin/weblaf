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

package com.alee.demo.content.button;

import com.alee.demo.api.AbstractExample;
import com.alee.managers.style.skin.web.WebSkin;
import com.alee.utils.xml.ResourceFile;
import com.alee.utils.xml.ResourceLocation;

/**
 * @author Mikle Garin
 */

public class ButtonExample extends AbstractExample
{
    @Override
    public String getId ()
    {
        return "button";
    }

    @Override
    protected ResourceFile getStyleFile ()
    {
        return new ResourceFile ( ResourceLocation.nearClass, "resources/button.xml", WebSkin.class );
    }
}