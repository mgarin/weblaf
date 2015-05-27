package com.alee.extended.checkbox;

import com.alee.extended.painter.SpecificPainter;
import com.alee.laf.radiobutton.BasicStateButtonPainter;

import javax.swing.*;

/**
 * Base interface for JCheckBox component painters.
 *
 * @author Alexandr Zernov
 */

public interface TrisateCheckBoxPainter<E extends JCheckBox, U extends WebTristateCheckBoxUI>
        extends BasicStateButtonPainter<E, U>, SpecificPainter
{
}
