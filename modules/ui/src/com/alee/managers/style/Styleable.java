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
import com.alee.api.annotations.Nullable;
import com.alee.painter.Painter;

import java.awt.*;

/**
 * This interface is implemented by {@link javax.swing.JComponent}s which support styling through WebLaF skins.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.painter.PainterSupport
 * @see StyleManager
 * @see Skin
 */
public interface Styleable
{
    /**
     * Returns default component {@link StyleId}.
     * This method is asked when initial component {@link StyleId} have to be provided.
     *
     * @return default component {@link StyleId}
     */
    @NotNull
    public StyleId getDefaultStyleId ();

    /**
     * Returns component {@link StyleId}.
     *
     * @return component {@link StyleId}
     */
    @NotNull
    public StyleId getStyleId ();

    /**
     * Sets new component {@link StyleId}.
     * If style for the specified ID cannot be found in skin then its default style will be used instead.
     *
     * @param id custom component {@link StyleId}
     * @return previously used {@link StyleId}
     */
    @NotNull
    public StyleId setStyleId ( @NotNull StyleId id );

    /**
     * Resets {@link StyleId} to default value.
     *
     * @return previously used {@link StyleId}
     */
    @NotNull
    public StyleId resetStyleId ();

    /**
     * Returns skin currently applied to this component.
     *
     * @return skin currently applied to this component
     */
    @NotNull
    public Skin getSkin ();

    /**
     * Applies specified custom skin to the styleable component and all of its children linked via {@link StyleId}.
     * Actual linked children information is stored within {@link StyleData} data objects.
     * Custom skin provided using this method will not be replaced if application skin changes.
     *
     * @param skin skin to be applied
     * @return previously applied skin
     */
    @Nullable
    public Skin setSkin ( @NotNull Skin skin );

    /**
     * Applies specified custom skin to the styleable component and all of its children linked via {@link StyleId}.
     * Actual linked children information is stored within {@link StyleData} data objects.
     * Custom skin provided using this method will not be replaced if application skin changes.
     *
     * @param skin        skin to be applied
     * @param recursively whether or not should apply skin to child components
     * @return previously applied skin
     */
    @Nullable
    public Skin setSkin ( @NotNull Skin skin, boolean recursively );

    /**
     * Resets skin for this component and all of its children linked via {@link StyleId}.
     * Actual linked children information is stored within {@link StyleData} data objects.
     * Resetting component skin will also include it back into the skin update cycle in case global skin will be changed.
     *
     * @return skin applied to this component after reset
     */
    @Nullable
    public Skin resetSkin ();

    /**
     * Adds {@link StyleListener}.
     *
     * @param listener {@link StyleListener} to add
     */
    public void addStyleListener ( @NotNull StyleListener listener );

    /**
     * Removes {@link StyleListener}.
     *
     * @param listener {@link StyleListener} to remove
     */
    public void removeStyleListener ( @NotNull StyleListener listener );

    /**
     * Returns custom {@link Painter} used for implementing {@link javax.swing.JComponent}.
     *
     * @return custom {@link Painter} used for implementing {@link javax.swing.JComponent}
     */
    @Nullable
    public Painter getCustomPainter ();

    /**
     * Sets custom {@link Painter} for implementing {@link javax.swing.JComponent}.
     *
     * @param painter custom {@link Painter}
     * @return custom {@link Painter} previously used for implementing {@link javax.swing.JComponent}
     */
    @Nullable
    public Painter setCustomPainter ( @NotNull Painter painter );

    /**
     * Resets custom {@link Painter} for implementing {@link javax.swing.JComponent} to default one.
     *
     * @return {@code true} if custom {@link Painter} was successfully resetted, {@code false} otherwise
     */
    public boolean resetCustomPainter ();

    /**
     * Returns component's {@link Painter} {@link Shape}.
     * This method is not named {@code getShape ()} to avoid clashing with JDK7+ method in {@link Window} ancestors.
     *
     * @return component's {@link Painter} {@link Shape}
     */
    @NotNull
    public Shape getPainterShape ();

    /**
     * Returns whether or not component's custom {@link Shape} is used for better mouse events detection.
     * If it wasn't explicitly specified - {@link com.alee.laf.WebLookAndFeel#isShapeDetectionEnabled()} is used as result.
     *
     * @return {@code true} if component's custom {@link Shape} is used for better mouse events detection, {@code false} otherwise
     */
    public boolean isShapeDetectionEnabled ();

    /**
     * Sets whether or not component's custom {@link Shape} should be used for better mouse events detection.
     * It can be enabled globally through {@link com.alee.laf.WebLookAndFeel#setShapeDetectionEnabled(boolean)}.
     *
     * @param enabled whether or not component's custom {@link Shape} should be used for better mouse events detection
     */
    public void setShapeDetectionEnabled ( boolean enabled );

    /**
     * Returns current margin.
     * Might return {@code null} which is basically the same as an empty [0,0,0,0] margin.
     *
     * @return current margin
     */
    @Nullable
    public Insets getMargin ();

    /**
     * Sets new margin.
     *
     * @param margin new margin
     */
    public void setMargin ( int margin );

    /**
     * Sets new margin.
     *
     * @param top    new top margin
     * @param left   new left margin
     * @param bottom new bottom margin
     * @param right  new right margin
     */
    public void setMargin ( int top, int left, int bottom, int right );

    /**
     * Sets new margin.
     * {@code null} can be provided to set an empty [0,0,0,0] margin.
     *
     * @param margin new margin
     */
    public void setMargin ( @Nullable Insets margin );

    /**
     * Returns current padding.
     * Might return {@code null} which is basically the same as an empty [0,0,0,0] padding.
     *
     * @return current padding
     */
    @Nullable
    public Insets getPadding ();

    /**
     * Sets new padding.
     *
     * @param padding new padding
     */
    public void setPadding ( int padding );

    /**
     * Sets new padding.
     *
     * @param top    new top padding
     * @param left   new left padding
     * @param bottom new bottom padding
     * @param right  new right padding
     */
    public void setPadding ( int top, int left, int bottom, int right );

    /**
     * Sets new padding.
     * {@code null} can be provided to set an empty [0,0,0,0] padding.
     *
     * @param padding new padding
     */
    public void setPadding ( @Nullable Insets padding );
}