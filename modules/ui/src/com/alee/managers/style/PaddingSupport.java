package com.alee.managers.style;

import java.awt.*;

/**
 * This interface is implemented by components and UIs which support customizable padding.
 * Padding is a spacing between component visible decoration and its content.
 * <p>
 * It might have different implementation depending on the component.
 * For example for {@link javax.swing.JButton} it is a spacing between its decoration border and its content.
 * For {@link javax.swing.JTabbedPane} it is a spacing between tab content area borders and content inside.
 * <p>
 * Padding might be supported through either custom borders in WebLaF-decorated components or component layout.
 * Be aware that if you specify your own border into those components this option might have no effect.
 *
 * @author Mikle Garin
 */

public interface PaddingSupport
{
    /**
     * Empty padding.
     */
    public static Insets EMPTY = null;

    /**
     * Returns current padding.
     * Might return null which is basically the same as an empty [0,0,0,0] padding.
     *
     * @return current padding
     */
    public Insets getPadding ();

    /**
     * Sets new padding.
     * {@code null} can be provided to set an empty [0,0,0,0] padding.
     *
     * @param padding new padding
     */
    public void setPadding ( Insets padding );
}