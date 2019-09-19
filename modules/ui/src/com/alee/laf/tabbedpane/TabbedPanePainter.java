package com.alee.laf.tabbedpane;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.painter.DefaultPainter;
import com.alee.painter.SectionPainter;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Basic painter for {@link JTabbedPane} component.
 * It is used as {@link WTabbedPaneUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */
public class TabbedPanePainter<C extends JTabbedPane, U extends WTabbedPaneUI<C>, D extends IDecoration<C, D>>
        extends AbstractDecorationPainter<C, U, D> implements ITabbedPanePainter<C, U>
{
    /**
     * {@link SectionPainter} that can be used to customize tab content background.
     */
    @DefaultPainter ( TabContentPainter.class )
    protected ITabContentPainter tabContentPainter;

    @Nullable
    @Override
    protected List<SectionPainter<C, U>> getSectionPainters ()
    {
        return asList ( tabContentPainter );
    }

    @Override
    protected void paintContent ( @NotNull final Graphics2D g2d, @NotNull final C c, @NotNull final U ui, @NotNull final Rectangle bounds )
    {
        paintTabContent ( g2d );
    }

    /**
     * Paints tree background.
     *
     * @param g2d graphics context
     */
    protected void paintTabContent ( @NotNull final Graphics2D g2d )
    {
        if ( tabContentPainter != null && component.getLayout () instanceof TabbedPaneLayout )
        {
            final TabbedPaneLayout layout = ( TabbedPaneLayout ) component.getLayout ();
            tabContentPainter.prepareToPaint ( component.getSelectedIndex () );
            paintSection ( tabContentPainter, g2d, layout.getContentBounds () );
        }
    }
}