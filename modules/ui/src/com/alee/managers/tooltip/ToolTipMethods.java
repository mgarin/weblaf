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

package com.alee.managers.tooltip;

import com.alee.managers.language.data.TooltipWay;
import com.alee.utils.swing.SwingMethods;

import javax.swing.*;
import java.util.List;

/**
 * This interface provides a set of methods that should be added into components to support custom WebLaF tooltips.
 * Basically all these methods are already implemented in EventUtils but it is much easier to call them directly from popover.
 *
 * @author Mikle Garin
 * @see com.alee.utils.swing.SwingMethods
 * @see com.alee.managers.tooltip.TooltipManager
 */

public interface ToolTipMethods extends SwingMethods
{
    /**
     * Sets single custom WebLaF tooltip into the component.
     *
     * @param tooltip tooltip text or language key
     * @return created custom WebLaF tooltip
     */
    public WebCustomTooltip setToolTip ( final String tooltip );

    /**
     * Sets single custom WebLaF tooltip into the component.
     *
     * @param icon    tooltip icon
     * @param tooltip tooltip text or language key
     * @return created custom WebLaF tooltip
     */
    public WebCustomTooltip setToolTip ( final Icon icon, final String tooltip );

    /**
     * Sets single custom WebLaF tooltip into the component.
     *
     * @param tooltip    tooltip text or language key
     * @param tooltipWay tooltip display direction
     * @return created custom WebLaF tooltip
     */
    public WebCustomTooltip setToolTip ( final String tooltip, final TooltipWay tooltipWay );

    /**
     * Sets single custom WebLaF tooltip into the component.
     *
     * @param icon       tooltip icon
     * @param tooltip    tooltip text or language key
     * @param tooltipWay tooltip display direction
     * @return created custom WebLaF tooltip
     */
    public WebCustomTooltip setToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay );

    /**
     * Sets single custom WebLaF tooltip into the component.
     *
     * @param tooltip    tooltip text or language key
     * @param tooltipWay tooltip display direction
     * @param delay      tooltip display delay
     * @return created custom WebLaF tooltip
     */
    public WebCustomTooltip setToolTip ( final String tooltip, final TooltipWay tooltipWay, final int delay );

    /**
     * Sets single custom WebLaF tooltip into the component.
     *
     * @param icon       tooltip icon
     * @param tooltip    tooltip text or language key
     * @param tooltipWay tooltip display direction
     * @param delay      tooltip display delay
     * @return created custom WebLaF tooltip
     */
    public WebCustomTooltip setToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay, final int delay );

    /**
     * Sets single custom WebLaF tooltip into the component.
     *
     * @param tooltip custom tooltip component
     * @return created custom WebLaF tooltip
     */
    public WebCustomTooltip setToolTip ( final JComponent tooltip );

    /**
     * Sets single custom WebLaF tooltip into the component.
     *
     * @param tooltip custom tooltip component
     * @param delay   tooltip display delay
     * @return created custom WebLaF tooltip
     */
    public WebCustomTooltip setToolTip ( final JComponent tooltip, final int delay );

    /**
     * Sets single custom WebLaF tooltip into the component.
     *
     * @param tooltip    custom tooltip component
     * @param tooltipWay tooltip display direction
     * @return created custom WebLaF tooltip
     */
    public WebCustomTooltip setToolTip ( final JComponent tooltip, final TooltipWay tooltipWay );

    /**
     * Sets single custom WebLaF tooltip into the component.
     *
     * @param tooltip    custom tooltip component
     * @param tooltipWay tooltip display direction
     * @param delay      tooltip display delay
     * @return created custom WebLaF tooltip
     */
    public WebCustomTooltip setToolTip ( final JComponent tooltip, final TooltipWay tooltipWay, final int delay );

    /**
     * Adds custom WebLaF tooltip to the component.
     *
     * @param tooltip tooltip text or language key
     * @return created custom WebLaF tooltip
     */
    public WebCustomTooltip addToolTip ( final String tooltip );

    /**
     * Adds custom WebLaF tooltip to the component.
     *
     * @param icon    tooltip icon
     * @param tooltip tooltip text or language key
     * @return created custom WebLaF tooltip
     */
    public WebCustomTooltip addToolTip ( final Icon icon, final String tooltip );

    /**
     * Adds custom WebLaF tooltip to the component.
     *
     * @param tooltip    tooltip text or language key
     * @param tooltipWay tooltip display direction
     * @return created custom WebLaF tooltip
     */
    public WebCustomTooltip addToolTip ( final String tooltip, final TooltipWay tooltipWay );

    /**
     * Adds custom WebLaF tooltip to the component.
     *
     * @param icon       tooltip icon
     * @param tooltip    tooltip text or language key
     * @param tooltipWay tooltip display direction
     * @return created custom WebLaF tooltip
     */
    public WebCustomTooltip addToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay );

    /**
     * Adds custom WebLaF tooltip to the component.
     *
     * @param tooltip    tooltip text or language key
     * @param tooltipWay tooltip display direction
     * @param delay      tooltip display delay
     * @return created custom WebLaF tooltip
     */
    public WebCustomTooltip addToolTip ( final String tooltip, final TooltipWay tooltipWay, final int delay );

    /**
     * Adds custom WebLaF tooltip to the component.
     *
     * @param icon       tooltip icon
     * @param tooltip    tooltip text or language key
     * @param tooltipWay tooltip display direction
     * @param delay      tooltip display delay
     * @return created custom WebLaF tooltip
     */
    public WebCustomTooltip addToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay, final int delay );

    /**
     * Adds custom WebLaF tooltip to the component.
     *
     * @param tooltip custom tooltip component
     * @return created custom WebLaF tooltip
     */
    public WebCustomTooltip addToolTip ( final JComponent tooltip );

    /**
     * Adds custom WebLaF tooltip to the component.
     *
     * @param tooltip custom tooltip component
     * @param delay   tooltip display delay
     * @return created custom WebLaF tooltip
     */
    public WebCustomTooltip addToolTip ( final JComponent tooltip, final int delay );

    /**
     * Adds custom WebLaF tooltip to the component.
     *
     * @param tooltip    custom tooltip component
     * @param tooltipWay tooltip display direction
     * @return created custom WebLaF tooltip
     */
    public WebCustomTooltip addToolTip ( final JComponent tooltip, final TooltipWay tooltipWay );

    /**
     * Adds custom WebLaF tooltip to the component.
     *
     * @param tooltip    custom tooltip component
     * @param tooltipWay tooltip display direction
     * @param delay      tooltip display delay
     * @return created custom WebLaF tooltip
     */
    public WebCustomTooltip addToolTip ( final JComponent tooltip, final TooltipWay tooltipWay, final int delay );

    /**
     * Removes specified custom WebLaF tooltip from the component.
     *
     * @param tooltip custom WebLaF tooltip to remove
     */
    public void removeToolTip ( final WebCustomTooltip tooltip );

    /**
     * Removes all custom WebLaF tooltips from the component.
     */
    public void removeToolTips ();

    /**
     * Removes specified custom WebLaF tooltips from the component.
     *
     * @param tooltips custom WebLaF tooltips to remove
     */
    public void removeToolTips ( final WebCustomTooltip... tooltips );

    /**
     * Removes specified custom WebLaF tooltips from the component.
     *
     * @param tooltips custom WebLaF tooltips to remove
     */
    public void removeToolTips ( final List<WebCustomTooltip> tooltips );
}