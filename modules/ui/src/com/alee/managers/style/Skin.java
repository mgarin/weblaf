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

import com.alee.api.Identifiable;
import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.managers.icon.set.IconSet;
import com.alee.managers.style.data.ComponentStyle;

import javax.swing.*;
import java.util.List;

/**
 * Base interface single WebLaF skin.
 * Each skin combines a group of component painters and settings to provide an unique visual style.
 * If can also initialize WebLaF with your skin directly through one of {@link com.alee.laf.WebLookAndFeel} install methods.
 *
 * Here is a list of complete skin implementations:
 * - {@link com.alee.skin.web.WebSkin}
 * - {@link com.alee.skin.dark.DarkSkin}
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 */
public interface Skin extends Identifiable
{
    /**
     * Returns unique skin ID.
     * Used to collect and manage skins within StyleManager.
     *
     * @return unique skin ID
     */
    @Nullable
    @Override
    public String getId ();

    /**
     * Returns skin {@link Icon}.
     *
     * @return skin {@link Icon}
     */
    public Icon getIcon ();

    /**
     * Returns skin title.
     *
     * @return skin title
     */
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
    public String getSkinClass ();

    /**
     * Called upon this skin installation as default global skin.
     */
    public void install ();

    /**
     * Called upon this skin uninstallation from being default global skin.
     */
    public void uninstall ();

    /**
     * Applies specified extension to this skin.
     * Same extension might be applied multiple times in case application switches between styles multiple times.
     * This might happen because {@link com.alee.managers.style.StyleManager} can't track where exactly they were applied already.
     * You must ensure nothing goes wrong in case same extension attempts to get applied more than once.
     *
     * @param extension skin extension to apply
     * @return true if extension was applied successfully, false otherwise
     */
    public boolean applyExtension ( SkinExtension extension );

    /**
     * Returns skin icon sets.
     *
     * @return skin icon sets
     */
    public List<IconSet> getIconSets ();

    /**
     * Returns style for the specified supported component type.
     * Custom style ID can be specified in any Web-component or Web-UI to override default component style.
     * If style for such custom ID is not found in skin descriptor then default style for that component is used.
     *
     * @param component component instance
     * @return component style
     */
    public ComponentStyle getStyle ( JComponent component );

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