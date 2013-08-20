package com.alee.extended.panel;

import java.util.EventListener;

/**
 * This is a special WebCollapsiblePane events listener that fires four kinds of events.
 * Two of them are fired before the collapsible pane finished either collapsing or expanding and two other fired after.
 *
 * @author Mikle Garin
 */

public interface AccordionListener extends EventListener
{
    /**
     * Notifies when accordion selection changes.
     */
    public void selectionChanged ();
}