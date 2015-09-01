package com.alee.laf.progressbar;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JProgressBar component painters.
 *
 * @author Alexandr Zernov
 */

public interface ProgressBarPainter<E extends JProgressBar, U extends WebProgressBarUI> extends Painter<E, U>, SpecificPainter
{
}