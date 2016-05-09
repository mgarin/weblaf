package com.alee.laf.toolbar;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JToolBar component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IToolBarPainter<E extends JToolBar, U extends WebToolBarUI> extends SpecificPainter<E, U>
{
}