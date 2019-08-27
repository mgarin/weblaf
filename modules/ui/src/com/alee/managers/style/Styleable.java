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

import com.alee.api.annotations.NotNull;

/**
 * This interface is implemented by components and UIs which support styling through WebLaF skins.
 * It provides only two methods to allow default component style ID modifications.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see StyleManager
 * @see Skin
 */
public interface Styleable
{
    /**
     * Returns default component {@link com.alee.managers.style.StyleId}.
     * This method is asked when initial component style ID have to be provided.
     *
     * @return default component {@link com.alee.managers.style.StyleId}
     */
    @NotNull
    public StyleId getDefaultStyleId ();

    /**
     * Returns component style ID.
     *
     * @return component style ID
     */
    @NotNull
    public StyleId getStyleId ();

    /**
     * Sets new component style ID.
     * If style for the specified ID cannot be found in skin then its default style will be used instead.
     *
     * @param id custom component style ID
     * @return previously used style ID
     */
    public StyleId setStyleId ( StyleId id );

    /**
     * Resets style ID to default value.
     *
     * @return previously used style ID
     */
    public StyleId resetStyleId ();

    /**
     * Returns skin currently applied to this component.
     *
     * @return skin currently applied to this component
     */
    public Skin getSkin ();

    /**
     * Applies specified custom skin to the styleable component and all of its children linked via {@link com.alee.managers.style.StyleId}.
     * Actual linked children information is stored within {@link com.alee.managers.style.StyleData} data objects.
     * Custom skin provided using this method will not be replaced if application skin changes.
     *
     * @param skin skin to be applied
     * @return previously applied skin
     */
    public Skin setSkin ( Skin skin );

    /**
     * Applies specified custom skin to the styleable component and all of its children linked via {@link com.alee.managers.style.StyleId}.
     * Actual linked children information is stored within {@link com.alee.managers.style.StyleData} data objects.
     * Custom skin provided using this method will not be replaced if application skin changes.
     *
     * @param skin        skin to be applied
     * @param recursively whether or not should apply skin to child components
     * @return previously applied skin
     */
    public Skin setSkin ( Skin skin, boolean recursively );

    /**
     * Resets skin for this component and all of its children linked via {@link com.alee.managers.style.StyleId}.
     * Actual linked children information is stored within {@link com.alee.managers.style.StyleData} data objects.
     * Resetting component skin will also include it back into the skin update cycle in case global skin will be changed.
     *
     * @return skin applied to this component after reset
     */
    public Skin resetSkin ();

    /**
     * Adds style change listener.
     *
     * @param listener style change listener to add
     */
    public void addStyleListener ( StyleListener listener );

    /**
     * Removes style change listener.
     *
     * @param listener style change listener to remove
     */
    public void removeStyleListener ( StyleListener listener );
}