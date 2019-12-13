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
import com.alee.painter.SpecificPainter;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

/**
 * Interface for various information and behavior providers for {@link JComponent} implementations.
 *
 * @param <C> {@link JComponent} type
 * @param <U> base {@link ComponentUI} type
 * @param <P> {@link SpecificPainter} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see StyleManager
 * @see StyleManager#registerComponentDescriptor(ComponentDescriptor)
 * @see StyleManager#unregisterComponentDescriptor(ComponentDescriptor)
 * @see AbstractComponentDescriptor
 */
public interface ComponentDescriptor<C extends JComponent, U extends ComponentUI, P extends SpecificPainter> extends Identifiable
{
    /**
     * Returns {@link JComponent} identifier.
     * It is used as component style type and in most cases as default style identifier.
     *
     * @return {@link JComponent} identifier
     */
    @NotNull
    @Override
    public String getId ();

    /**
     * Returns {@link JComponent} {@link Class}.
     *
     * @return {@link JComponent} {@link Class}
     */
    @NotNull
    public Class<C> getComponentClass ();

    /**
     * Returns {@link ComponentUI} {@link Class} identifier.
     * This identifier is used by LookAndFeel to store various settings.
     *
     * @return {@link ComponentUI} {@link Class} identifier
     */
    @NotNull
    public String getUIClassId ();

    /**
     * Returns base {@link ComponentUI} {@link Class} applicable to {@link JComponent}.
     * This class is required to limit and properly check UIs that can be applied to specific {@link JComponent} implementations.
     *
     * @return base {@link ComponentUI} {@link Class} applicable to {@link JComponent}
     */
    @NotNull
    public Class<U> getBaseUIClass ();

    /**
     * Returns {@link ComponentUI} {@link Class} used for {@link JComponent} by default.
     * This value is used in {@link com.alee.laf.WebLookAndFeel} to provide default UI classes.
     *
     * @return {@link ComponentUI} {@link Class} used for {@link JComponent} by default
     */
    @NotNull
    public Class<? extends U> getUIClass ();

    /**
     * Returns {@link SpecificPainter} interface {@link Class}.
     *
     * @return {@link SpecificPainter} interface {@link Class}
     */
    @NotNull
    public Class<P> getPainterInterface ();

    /**
     * Returns default {@link SpecificPainter} implementation {@link Class}.
     *
     * @return default {@link SpecificPainter} implementation {@link Class}
     */
    @NotNull
    public Class<? extends P> getPainterClass ();

    /**
     * Returns adapter for {@link SpecificPainter}.
     *
     * @return adapter for {@link SpecificPainter}
     */
    @NotNull
    public Class<? extends P> getPainterAdapterClass ();

    /**
     * Returns {@link JComponent} default {@link StyleId}.
     *
     * @return {@link JComponent} default {@link StyleId}
     */
    @NotNull
    public StyleId getDefaultStyleId ();

    /**
     * Returns {@link JComponent} instance-specific default {@link StyleId}.
     *
     * @param component {@link JComponent} instance to retrieve default {@link StyleId} for
     * @return {@link JComponent} instance-specific default {@link StyleId}
     */
    @NotNull
    public StyleId getDefaultStyleId ( @NotNull JComponent component );

    /**
     * Returns {@link JComponent} {@link Icon}.
     *
     * @return {@link JComponent} {@link Icon}
     */
    @NotNull
    public Icon getIcon ();

    /**
     * Returns {@link JComponent} name.
     *
     * @return {@link JComponent} name
     */
    @NotNull
    public String getTitle ();

    /**
     * Performs {@link UIDefaults} table updates.
     * Normally you should put {@link ComponentUI} class mapping here, but other settings can also be updated if needed.
     *
     * @param table {@link UIDefaults}
     */
    public void updateDefaults ( @NotNull UIDefaults table );

    /**
     * Asks descriptor to update {@link ComponentUI} instance for the specified {@link JComponent}.
     * It is up to implementation whether {@link ComponentUI} instance will be preserved or changed to the one currently provided by LaF.
     * Various {@link ComponentDescriptor} implementations might customize this behavior to update used sub-component UIs.
     *
     * @param component {@link JComponent} instance
     */
    public void updateUI ( @NotNull C component );
}