package com.alee.laf.tabbedpane;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JTabbedPane component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface ITabbedPanePainter<E extends JTabbedPane, U extends WebTabbedPaneUI> extends SpecificPainter<E, U>
{
}