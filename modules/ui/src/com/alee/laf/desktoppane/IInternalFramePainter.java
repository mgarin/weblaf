package com.alee.laf.desktoppane;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JInternalFrame component painters.
 *
 * @author Alexandr Zernov
 */

public interface IInternalFramePainter<E extends JInternalFrame, U extends WebInternalFrameUI> extends SpecificPainter<E, U>
{
}