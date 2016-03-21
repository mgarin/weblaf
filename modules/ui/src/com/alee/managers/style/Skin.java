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

package com.alee.managers.style;

import com.alee.api.IconSupport;
import com.alee.api.TitleSupport;
import com.alee.managers.style.data.ComponentStyle;

import javax.swing.*;

/**
 * @author Mikle Garin
 */

public interface Skin extends IconSupport, TitleSupport
{
    /**
     * Returns unique skin ID.
     * Used to collect and manage skins within StyleManager.
     *
     * @return unique skin ID
     */
    public String getId ();

    /**
     * Returns skin icon.
     *
     * @return skin icon
     */
    @Override
    public Icon getIcon ();

    /**
     * Returns skin title.
     *
     * @return skin title
     */
    @Override
    public String getTitle ();

    /**
     * Returns skin description.
     *
     * @return skin description
     */
    public String getDescription ();

    /**
     * Returns skin author.
     *
     * @return skin author
     */
    public String getAuthor ();

    /**
     * Returns whether this skin is supported or not.
     * This method reflects the default mechanism of checking skin support.
     * You can override it in your own skin to provide any custom checks.
     *
     * @return true if this skin is supported, false otherwise
     */
    public boolean isSupported ();

    /**
     * Returns skin base class name.
     *
     * @return skin base class name
     */
    public abstract String getSkinClass ();

    /**
     * Returns style for the specified supported component type.
     * Custom style ID can be specified in any Web-component or Web-UI to override default component style.
     * If style for such custom ID is not found in skin descriptor then default style for that component is used.
     *
     * @param component component instance
     * @return component style
     */
    public abstract ComponentStyle getComponentStyle ( JComponent component );

    /**
     * Applies this skin to the specified component.
     * Returns whether skin was successfully applied or not.
     *
     * @param component component to apply skin to
     * @return true if skin was applied, false otherwise
     */
    public boolean applySkin ( JComponent component );

    /**
     * Updates this skin on the specified component.
     * This is required to provide optimized update sequence in some cases.
     *
     * @param component component to update skin for
     */
    public void updateSkin ( JComponent component );

    /**
     * Removes this skin from the specified component.
     *
     * @param component component to remove skin from
     * @return true if skin was successfully removed, false otherwise
     */
    public boolean removeSkin ( JComponent component );
}