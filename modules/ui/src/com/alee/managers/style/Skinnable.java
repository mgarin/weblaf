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

/**
 * This interface is implemented by components which support styling through WebLaF skins.
 * It provides methods to modify component skin in runtime.
 *
 * @author Mikle Garin
 * @see com.alee.managers.style.StyleManager
 * @see Skin
 * @see com.alee.managers.style.StyleId
 * @see com.alee.managers.style.StyleData
 */

public interface Skinnable
{
    /**
     * Returns skin currently applied to this component.
     *
     * @return skin currently applied to this component
     */
    public Skin getSkin ();

    /**
     * Applies specified skin to the specified component and all of its children linked via {@link com.alee.managers.style.StyleId}.
     * Actual linked children information is stored within {@link com.alee.managers.style.StyleData} data objects.
     * Custom skin provided using this method will not be replaced if application skin changes.
     *
     * @param skin skin to be applied
     * @return previously applied skin
     */
    public Skin setSkin ( Skin skin );

    /**
     * Applies specified skin to the specified component and all of its children linked via {@link com.alee.managers.style.StyleId}.
     * Actual linked children information is stored within {@link com.alee.managers.style.StyleData} data objects.
     * Custom skin provided using this method will not be replaced if application skin changes.
     *
     * @param skin        skin to be applied
     * @param recursively whether or not should apply skin to child components
     * @return previously applied skin
     */
    public Skin setSkin ( Skin skin, boolean recursively );

    /**
     * Restores global skin for this component and all of its children linked via {@link com.alee.managers.style.StyleId}.
     * Actual linked children information is stored within {@link com.alee.managers.style.StyleData} data objects.
     * Restoring component skin will also include it back into the skin update cycle in case global skin will be changed.
     *
     * @return skin applied to this component after restoration
     */
    public Skin restoreSkin ();

    /**
     * Adds style change listener.
     *
     * @param listener style change listener to add
     */
    public void addStyleListener ( final StyleListener listener );

    /**
     * Removes style change listener.
     *
     * @param listener style change listener to remove
     */
    public void removeStyleListener ( final StyleListener listener );
}