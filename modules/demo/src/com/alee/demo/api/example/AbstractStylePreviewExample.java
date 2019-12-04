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

package com.alee.demo.api.example;

import com.alee.api.annotations.NotNull;
import com.alee.api.resource.ClassResource;
import com.alee.api.resource.Resource;
import com.alee.managers.style.Skin;
import com.alee.utils.FileUtils;

/**
 * @author Mikle Garin
 */
public abstract class AbstractStylePreviewExample extends AbstractPreviewExample
{
    @NotNull
    @Override
    public String getStyleCode ( @NotNull final Skin skin )
    {
        return FileUtils.readToString ( getStyleResource ( skin ) );
    }

    /**
     * Returns style file representing styles for this example.
     * Styling system doesn't really force you to create separate files, but default style has them for convenience.
     * Demo application uses that fact to show separate examples for each specific component.
     *
     * @param skin skin to retrieve style file for
     * @return style file representing styles for this example
     */
    @NotNull
    protected Resource getStyleResource ( @NotNull final Skin skin )
    {
        return new ClassResource ( skin.getClass (), "resources/" + getStyleFileName () + ".xml" );
    }

    /**
     * Returns example style file name.
     *
     * @return example style file name
     */
    @NotNull
    protected String getStyleFileName ()
    {
        return getId ();
    }
}