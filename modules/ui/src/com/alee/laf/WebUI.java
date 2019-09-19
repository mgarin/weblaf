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

package com.alee.laf;

import com.alee.api.annotations.NotNull;

import javax.swing.*;

/**
 * Custom interface that should be implemented by all basic WebLaF UI implementations.
 * This interface must only be implemented by classes that extend {@link javax.swing.plaf.ComponentUI}.
 * Basic implementations are made to replace basic Swing UI implementations to avoid some nasty workarounds and provide better visuals.
 *
 * @param <C> component type
 * @author Mikle Garin
 */
@SuppressWarnings ( { "unused", "JavadocReference" } )
public interface WebUI<C extends JComponent>
{
    /**
     * Returns component property prefix.
     * This prefix is used by various properties provided within look and feel for the component this UI represents.
     * UI might use this property to unify retrieval of some basic properties (like font) for the component.
     *
     * @return component property prefix
     * @see WebLookAndFeel#initComponentDefaults(UIDefaults)
     */
    @NotNull
    public String getPropertyPrefix ();

    /**
     * Configures the specified component appropriate for the look and feel.
     * This method is invoked when the {@code ComponentUI} instance is being installed as the UI delegate on the specified component.
     * This method should completely configure the component for the look and feel, including the following:
     * <ol>
     * <li>Install any default property values for color, fonts, borders,
     * icons, opacity, etc. on the component.  Whenever possible,
     * property values initialized by the client program should <i>not</i>
     * be overridden.
     * <li>Install a {@code LayoutManager} on the component if necessary.
     * <li>Create/add any required sub-components to the component.
     * <li>Create/install event listeners on the component.
     * <li>Create/install a {@code PropertyChangeListener} on the component in order
     * to detect and respond to component property changes appropriately.
     * <li>Install keyboard UI (mnemonics, traversal, etc.) on the component.
     * <li>Initialize any appropriate instance data.
     * </ol>
     *
     * @param component the component where this UI delegate is being installed
     * @see javax.swing.plaf.ComponentUI#installUI(JComponent)
     * @see #uninstallUI
     * @see javax.swing.JComponent#setUI
     * @see javax.swing.JComponent#updateUI
     */
    public void installUI ( @NotNull JComponent component );

    /**
     * Reverses configuration which was done on the specified component during {@code installUI}.
     * This method is invoked when this {@code UIComponent} instance is being removed as the UI delegate for the specified component.
     * This method should undo the configuration performed in {@code installUI}, being careful to leave the {@code JComponent} instance
     * in a clean state (no extraneous listeners, look-and-feel-specific property objects, etc.).
     * This should include the following:
     * <ol>
     * <li>Remove any UI-set borders from the component.
     * <li>Remove any UI-set layout managers on the component.
     * <li>Remove any UI-added sub-components from the component.
     * <li>Remove any UI-added event/property listeners from the component.
     * <li>Remove any UI-installed keyboard UI from the component.
     * <li>Nullify any allocated instance data objects to allow for GC.
     * </ol>
     *
     * @param component the component from which this UI delegate is being removed;
     *                  this argument is often ignored, but might be used if the UI object is stateless and shared by multiple components
     * @see javax.swing.plaf.ComponentUI#uninstallUI(JComponent)
     * @see #installUI
     * @see javax.swing.JComponent#updateUI
     */
    public void uninstallUI ( @NotNull JComponent component );
}