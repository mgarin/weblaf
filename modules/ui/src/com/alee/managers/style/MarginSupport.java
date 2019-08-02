package com.alee.managers.style;

import java.awt.*;

/**
 * This interface is implemented by classes which provide customizable margin.
 * Margin is a spacing between element bounds and its view.
 *
 * Margin is supported through custom borders in WebLaF-decorated components.
 * Be aware that if you specify your own border into those components this option will have no effect.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see MarginMethods
 * @see StyleManager
 */
public interface MarginSupport
{
    /**
     * Empty margin.
     */
    public static Insets EMPTY = null;

    /**
     * Returns current margin.
     * Might return {@code null} which is basically the same as an empty [0,0,0,0] margin.
     *
     * @return current margin
     */
    public Insets getMargin ();

    /**
     * Sets new margin.
     * {@code null} can be provided to set an empty [0,0,0,0] margin.
     *
     * @param margin new margin
     */
    public void setMargin ( Insets margin );
}