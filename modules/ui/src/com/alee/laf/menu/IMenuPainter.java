package com.alee.laf.menu;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JMenu component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IMenuPainter<E extends JMenu, U extends WebMenuUI> extends SpecificPainter<E, U>
{
}