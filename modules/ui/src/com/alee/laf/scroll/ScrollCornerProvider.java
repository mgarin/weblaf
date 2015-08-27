package com.alee.laf.scroll;

import javax.swing.*;

/**
 * @author Alexandr Zernov
 */

public interface ScrollCornerProvider
{
    /**
     * Returns the component at the specified corner. The {@code key} value specifying the corner is one of:
     * <ul>
     * <li>ScrollPaneConstants.LOWER_LEFT_CORNER
     * <li>ScrollPaneConstants.LOWER_RIGHT_CORNER
     * <li>ScrollPaneConstants.UPPER_LEFT_CORNER
     * <li>ScrollPaneConstants.UPPER_RIGHT_CORNER
     * <li>ScrollPaneConstants.LOWER_LEADING_CORNER
     * <li>ScrollPaneConstants.LOWER_TRAILING_CORNER
     * <li>ScrollPaneConstants.UPPER_LEADING_CORNER
     * <li>ScrollPaneConstants.UPPER_TRAILING_CORNER
     * </ul>
     *
     * @param key one of the values as shown above
     * @return the corner component (which may be {@code null}) identified by the given key, or {@code null} if the key is invalid
     */
    public JComponent getCorner ( String key );
}
