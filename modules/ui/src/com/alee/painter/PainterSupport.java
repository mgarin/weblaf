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

package com.alee.painter;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.*;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.WeakComponentData;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import java.awt.*;

/**
 * This class provides utilities for linking {@link Painter}s with component UIs.
 * It was added to simplify {@link Painter}s usage within UI classes tied to specific {@link ComponentUI} implementations.
 * Without this utility class a lot of code copy-paste would be required between all different UI implementations.
 *
 * {@link Painter}s do not suffer from that issue since they are implemented differently - each specific Painters has its own interface
 * unlike {@link ComponentUI}s which are not based on interfaces but always abstract classes, like {@link javax.swing.plaf.ButtonUI} one.
 *
 * @author Mikle Garin
 */
public final class PainterSupport
{
    /**
     * Installed painters.
     * todo These should be moved into {@link StyleManager} and preserver in {@link com.alee.managers.style.StyleData}
     */
    @NotNull
    private static final WeakComponentData<JComponent, Painter> installedPainters =
            new WeakComponentData<JComponent, Painter> ( "PainterSupport.painter", 200 );

    /**
     * Margins saved for each {@link JComponent} instance.
     * todo These settings should be completely moved into {@link AbstractPainter} upon multiple painters elimination
     *
     * @see #getMargin(JComponent)
     * @see #setMargin(JComponent, Insets)
     */
    @NotNull
    private static final WeakComponentData<JComponent, Insets> margins =
            new WeakComponentData<JComponent, Insets> ( "PainterSupport.margin", 200 );

    /**
     * Paddings saved for each {@link JComponent} instance.
     * todo These settings should be completely moved into {@link AbstractPainter} upon multiple painters elimination
     *
     * @see #getPadding(JComponent)
     * @see #setPadding(JComponent, Insets)
     */
    @NotNull
    private static final WeakComponentData<JComponent, Insets> paddings =
            new WeakComponentData<JComponent, Insets> ( "PainterSupport.padding", 200 );

    /**
     * Shape detection settings saved for each {@link JComponent} instance.
     * todo These settings should be completely moved into {@link AbstractPainter} upon multiple painters elimination
     *
     * @see #isShapeDetectionEnabled(JComponent)
     * @see #setShapeDetectionEnabled(JComponent, boolean)
     */
    @NotNull
    private static final WeakComponentData<JComponent, Boolean> shapeDetectionEnabled =
            new WeakComponentData<JComponent, Boolean> ( "PainterSupport.shapeDetectionEnabled", 200 );


    /**
     * Returns {@link Painter} currently installed on the specified {@link Component}.
     * Note that {@link Painter}s can only be installed on {@link JComponent}s, but this method accepts {@link Component} for convenience.
     *
     * @param component {@link Component} to retreive {@link Painter} for
     * @return {@link Painter} currently installed on the specified {@link Component}
     */
    @Nullable
    public static Painter getPainter ( @Nullable final Component component )
    {
        return component instanceof JComponent ? installedPainters.get ( ( JComponent ) component ) : null;
    }

    /**
     * Sets {@link Painter} for the specified {@link JComponent}.
     * Provided {@link Painter} can be {@code null} in which case current {@link Painter} will simply be uninstalled.
     *
     * @param component   {@link JComponent}
     * @param componentUI {@link ComponentUI}
     * @param painter     {@link Painter} to install
     */
    public static void setPainter ( @NotNull final JComponent component, @NotNull final ComponentUI componentUI,
                                    @Nullable final Painter painter )
    {
        final ComponentDescriptor descriptor = StyleManager.getDescriptor ( component );

        // Creating adaptive painter if required
        final SpecificPainter newPainter = getApplicablePainter (
                painter,
                descriptor.getPainterInterface (),
                descriptor.getPainterAdapterClass ()
        );

        // Uninstalling old painter
        final Painter oldPainter = installedPainters.get ( component );
        if ( oldPainter != null )
        {
            oldPainter.uninstall ( component, componentUI );
            installedPainters.clear ( component );
        }

        // Installing new painter
        if ( newPainter != null )
        {
            // Installing painter
            newPainter.install ( component, componentUI );
            installedPainters.set ( component, newPainter );

            // Applying initial component settings
            final Boolean opaque = newPainter.isOpaque ();
            if ( opaque != null )
            {
                LookAndFeel.installProperty ( component, WebLookAndFeel.OPAQUE_PROPERTY, opaque ? Boolean.TRUE : Boolean.FALSE );
            }
        }

        // Firing painter change event
        SwingUtils.firePropertyChanged ( component, WebLookAndFeel.PAINTER_PROPERTY, oldPainter, newPainter );
    }

    /**
     * Returns specified {@link Painter} if it can be assigned to requested {@link Painter} type, otherwise returns newly created
     * {@link AdaptivePainter} that acts as a decorator for the specified {@link Painter}.
     * Used by {@link ComponentUI}s to adapt non-specific {@link Painter}s for their specific needs.
     *
     * @param painter       {@link Painter}
     * @param requested     requested {@link SpecificPainter} class
     * @param adaptiveClass {@link AdaptivePainter} class
     * @param <P>           requested {@link SpecificPainter} type
     * @return specified {@link Painter} if it can be assigned to requested {@link Painter} type, otherwise returns newly created
     * {@link AdaptivePainter} that acts as a decorator for the specified {@link Painter}
     */
    private static <P extends SpecificPainter> P getApplicablePainter ( @Nullable final Painter painter, @NotNull final Class<P> requested,
                                                                        @NotNull final Class<? extends P> adaptiveClass )
    {
        final P result;
        if ( painter != null )
        {
            if ( ReflectUtils.isAssignable ( requested, painter.getClass () ) )
            {
                result = ( P ) painter;
            }
            else
            {
                result = ReflectUtils.createInstanceSafely ( adaptiveClass, painter );
            }
        }
        else
        {
            result = null;
        }
        return result;
    }

    /**
     * Returns {@link Component} border insets or {@code null} if {@link Component} doesn't have borders.
     * {@code null} is basically the same as an empty [0,0,0,0] border insets.
     *
     * @param component component to retrieve border insets from
     * @return component border insets or {@code null} if component doesn't have borders
     */
    @Nullable
    public static Insets getInsets ( @Nullable final Component component )
    {
        final Insets insets;
        if ( component instanceof JComponent )
        {
            insets = ( ( JComponent ) component ).getInsets ();
        }
        else
        {
            insets = null;
        }
        return insets;
    }

    /**
     * Returns current {@link JComponent} margin.
     * Might return {@code null} which is basically the same as an empty [0,0,0,0] margin.
     *
     * @param component {@link JComponent} to retrieve margin from
     * @return current {@link JComponent} margin
     */
    @Nullable
    public static Insets getMargin ( @NotNull final JComponent component )
    {
        return getMargin ( component, false );
    }

    /**
     * Returns current {@link JComponent} margin.
     * Might return {@code null} which is basically the same as an empty [0,0,0,0] margin.
     *
     * @param component        {@link JComponent} to retrieve margin from
     * @param applyOrientation whether or not {@link ComponentOrientation} of the specified {@link JComponent} should be applied to margin
     * @return current {@link JComponent} margin
     */
    @Nullable
    public static Insets getMargin ( @NotNull final JComponent component, final boolean applyOrientation )
    {
        final Insets result;
        final Insets margin = margins.get ( component );
        if ( margin != null )
        {
            if ( applyOrientation )
            {
                final boolean ltr = component.getComponentOrientation ().isLeftToRight ();
                result = new Insets (
                        margin.top,
                        ltr ? margin.left : margin.right,
                        margin.bottom,
                        ltr ? margin.right : margin.left
                );
            }
            else
            {
                result = new Insets ( margin.top, margin.left, margin.bottom, margin.right );
            }
        }
        else
        {
            result = null;
        }
        return result;
    }

    /**
     * Sets {@link JComponent} margin.
     *
     * @param component {@link JComponent} to set margin for
     * @param margin    new margin
     */
    public static void setMargin ( @NotNull final JComponent component, final int margin )
    {
        setMargin ( component, margin, margin, margin, margin );
    }

    /**
     * Sets {@link JComponent} margin.
     *
     * @param component {@link JComponent} to set margin for
     * @param top       new top margin
     * @param left      new left margin
     * @param bottom    new bottom margin
     * @param right     new right margin
     */
    public static void setMargin ( @NotNull final JComponent component, final int top, final int left, final int bottom, final int right )
    {
        setMargin ( component, new Insets ( top, left, bottom, right ) );
    }

    /**
     * Sets {@link JComponent} margin.
     * {@code null} can be provided to set an empty [0,0,0,0] margin.
     *
     * @param component {@link JComponent} to set margin for
     * @param margin    new margin
     */
    public static void setMargin ( @NotNull final JComponent component, @Nullable final Insets margin )
    {
        final Insets oldMargin = margins.get ( component );
        if ( oldMargin == null || oldMargin instanceof UIResource || !( margin instanceof UIResource ) )
        {
            // Updating margin cache
            margins.set ( component, margin );

            // Notifying everyone about component margin changes
            SwingUtils.firePropertyChanged ( component, WebLookAndFeel.LAF_MARGIN_PROPERTY, oldMargin, margin );
        }
    }

    /**
     * Returns current {@link JComponent} padding.
     * Might return {@code null} which is basically the same as an empty [0,0,0,0] padding.
     *
     * @param component {@link JComponent} to retrieve padding from
     * @return current {@link JComponent} padding
     */
    @Nullable
    public static Insets getPadding ( @NotNull final JComponent component )
    {
        return getPadding ( component, false );
    }

    /**
     * Returns current {@link JComponent} padding.
     * Might return {@code null} which is basically the same as an empty [0,0,0,0] padding.
     *
     * @param component        {@link JComponent} to retrieve padding from
     * @param applyOrientation whether or not {@link ComponentOrientation} of the specified {@link JComponent} should be applied to padding
     * @return current {@link JComponent} padding
     */
    @Nullable
    public static Insets getPadding ( @NotNull final JComponent component, final boolean applyOrientation )
    {
        final Insets result;
        final Insets padding = paddings.get ( component );
        if ( padding != null )
        {
            if ( applyOrientation )
            {
                final boolean ltr = component.getComponentOrientation ().isLeftToRight ();
                result = new Insets (
                        padding.top,
                        ltr ? padding.left : padding.right,
                        padding.bottom,
                        ltr ? padding.right : padding.left
                );
            }
            else
            {
                result = new Insets ( padding.top, padding.left, padding.bottom, padding.right );
            }
        }
        else
        {
            result = null;
        }
        return result;
    }

    /**
     * Sets {@link JComponent} padding.
     *
     * @param component {@link JComponent} to set padding for
     * @param padding    new padding
     */
    public static void setPadding ( @NotNull final JComponent component, final int padding )
    {
        setPadding ( component, padding, padding, padding, padding );
    }

    /**
     * Sets {@link JComponent} padding.
     *
     * @param component {@link JComponent} to set padding for
     * @param top       new top padding
     * @param left      new left padding
     * @param bottom    new bottom padding
     * @param right     new right padding
     */
    public static void setPadding ( @NotNull final JComponent component, final int top, final int left, final int bottom, final int right )
    {
        setPadding ( component, new Insets ( top, left, bottom, right ) );
    }

    /**
     * Sets {@link JComponent} padding.
     * {@code null} can be provided to set an empty [0,0,0,0] padding.
     *
     * @param component {@link JComponent} to set padding for
     * @param padding   new padding
     */
    public static void setPadding ( @NotNull final JComponent component, @Nullable final Insets padding )
    {
        final Insets oldPadding = paddings.get ( component );
        if ( oldPadding == null || oldPadding instanceof UIResource || !( padding instanceof UIResource ) )
        {
            // Updating padding cache
            paddings.set ( component, padding );

            // Notifying everyone about component padding changes
            SwingUtils.firePropertyChanged ( component, WebLookAndFeel.LAF_PADDING_PROPERTY, oldPadding, padding );
        }
    }

    /**
     * Returns {@link Shape} of the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to return {@link Shape} for
     * @return {@link Shape} of the specified {@link JComponent}
     */
    @NotNull
    public static Shape getShape ( @NotNull final JComponent component )
    {
        final Shape shape;
        final Painter painter = getPainter ( component );
        if ( painter instanceof PainterShapeProvider )
        {
            shape = ( ( PainterShapeProvider ) painter ).provideShape (
                    component,
                    BoundsType.margin.bounds ( component )
            );
        }
        else
        {
            shape = BoundsType.margin.bounds ( component );
        }
        return shape;
    }

    /**
     * Returns whether or not {@link JComponent}'s custom {@link Shape} is used for better mouse events detection.
     * If it wasn't explicitly specified - {@link WebLookAndFeel#isShapeDetectionEnabled()} is used as result.
     *
     * @param component {@link JComponent} to return {@link Shape} for
     * @return {@code true} if {@link JComponent}'s custom {@link Shape} is used for better mouse events detection, {@code false} otherwise
     */
    public static boolean isShapeDetectionEnabled ( @NotNull final JComponent component )
    {
        final Boolean enabled = shapeDetectionEnabled.get ( component );
        return enabled != null ? enabled : WebLookAndFeel.isShapeDetectionEnabled ();
    }

    /**
     * Sets whether or not {@link JComponent}'s custom {@link Shape} should be used for better mouse events detection.
     * It can be enabled globally through {@link com.alee.laf.WebLookAndFeel#setShapeDetectionEnabled(boolean)}.
     *
     * @param component {@link JComponent} to return {@link Shape} for
     * @param enabled   whether or not {@link JComponent}'s custom {@link Shape} should be used for better mouse events detection
     */
    public static void setShapeDetectionEnabled ( @NotNull final JComponent component, final boolean enabled )
    {
        shapeDetectionEnabled.set ( component, enabled );
    }

    /**
     * Returns whether or not specified (x,y) location is contained within the shape of the {@link JComponent}.
     *
     * @param component   {@link JComponent}
     * @param componentUI {@link ComponentUI}
     * @param x           X coordinate
     * @param y           Y coordinate
     * @return {@code true} if specified (x,y) location is contained within the shape of the component, {@code false} otherwise
     */
    public static boolean contains ( @NotNull final JComponent component, @NotNull final ComponentUI componentUI, final int x, final int y )
    {
        final boolean contains;
        final Painter painter = getPainter ( component );
        if ( painter != null && isShapeDetectionEnabled ( component ) )
        {
            contains = painter.contains ( component, componentUI, new Bounds ( component ), x, y );
        }
        else
        {
            contains = 0 <= x && x < component.getWidth () && 0 <= y && y < component.getHeight ();
        }
        return contains;
    }

    /**
     * Returns {@link JComponent} baseline measured from the top of the component bounds for the specified {@link JComponent} size.
     * A return value less than {@code 0} indicates this component does not have a reasonable baseline.
     * This method is primarily meant for {@link LayoutManager}s to align components along their baseline.
     *
     * @param component   {@link JComponent}
     * @param componentUI {@link ComponentUI}
     * @param width       offered component width
     * @param height      offered component height
     * @return {@link JComponent} baseline measured from the top of the component bounds for the specified {@link JComponent} size
     */
    public static int getBaseline ( @NotNull final JComponent component, @NotNull final ComponentUI componentUI,
                                    final int width, final int height )
    {
        // Default baseline
        int baseline = -1;

        // Painter baseline support
        final Painter painter = getPainter ( component );
        if ( painter != null )
        {
            // Creating appropriate bounds for painter
            final Bounds componentBounds = new Bounds ( new Dimension ( width, height ) );

            // Retrieving baseline provided by painter
            baseline = painter.getBaseline ( component, componentUI, componentBounds );
        }

        // Border baseline support
        // Taken from JPanel baseline implementation
        if ( baseline == -1 )
        {
            final Border border = component.getBorder ();
            if ( border instanceof AbstractBorder )
            {
                baseline = ( ( AbstractBorder ) border ).getBaseline ( component, width, height );
            }
        }

        return baseline;
    }

    /**
     * Returns {@link java.awt.Component.BaselineResizeBehavior} indicating how baseline of a {@link Component} changes when resized.
     *
     * @param component   {@link JComponent}
     * @param componentUI {@link ComponentUI}
     * @return {@link java.awt.Component.BaselineResizeBehavior} indicating how baseline of a {@link Component} changes when resized
     */
    @NotNull
    public static Component.BaselineResizeBehavior getBaselineResizeBehavior ( @NotNull final JComponent component,
                                                                               @NotNull final ComponentUI componentUI )
    {
        final Component.BaselineResizeBehavior behavior;
        final Painter painter = getPainter ( component );
        if ( painter != null )
        {
            behavior = painter.getBaselineResizeBehavior ( component, componentUI );
        }
        else
        {
            final Border border = component.getBorder ();
            if ( border instanceof AbstractBorder )
            {
                final AbstractBorder abstractBorder = ( AbstractBorder ) border;
                behavior = abstractBorder.getBaselineResizeBehavior ( component );
            }
            else
            {
                behavior = Component.BaselineResizeBehavior.OTHER;
            }
        }
        return behavior;
    }

    /**
     * Paints {@link JComponent} on the specified {@link Graphics}.
     *
     * @param g         {@link Graphics} to paint on
     * @param component {@link JComponent} to paint
     * @param ui        {@link JComponent}'s {@link ComponentUI}
     */
    public static void paint ( @NotNull final Graphics g, @NotNull final JComponent component, @NotNull final ComponentUI ui )
    {
        final Painter painter = PainterSupport.getPainter ( component );
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, component, ui, new Bounds ( component ) );
        }
    }

    /**
     * Paints {@link JComponent} on the specified {@link Graphics}.
     *
     * @param g          {@link Graphics} to paint on
     * @param component  {@link JComponent} to paint
     * @param ui         {@link JComponent}'s {@link ComponentUI}
     * @param parameters {@link PaintParameters}
     */
    public static void paint ( @NotNull final Graphics g, @NotNull final JComponent component, @NotNull final ComponentUI ui,
                               @NotNull final PaintParameters parameters )
    {
        final Painter painter = PainterSupport.getPainter ( component );
        if ( painter != null )
        {
            if ( painter instanceof ParameterizedPaint )
            {
                final ParameterizedPaint parameterizedPaint = ( ParameterizedPaint ) painter;
                parameterizedPaint.prepareToPaint ( parameters );
                painter.paint ( ( Graphics2D ) g, component, ui, new Bounds ( component ) );
            }
            else
            {
                throw new PainterException ( "Painter doesn't support parameters: " + painter );
            }
        }
    }

    /**
     * Returns {@link JComponent} preferred size or {@code null} if there is no preferred size.
     *
     * @param component {@link JComponent}
     * @return {@link JComponent} preferred size or {@code null} if there is no preferred size
     */
    @Nullable
    public static Dimension getPreferredSize ( @NotNull final JComponent component )
    {
        return getPreferredSize ( component, null, false );
    }

    /**
     * Returns component preferred size or {@code null} if there is no preferred size.
     * todo Probably get rid of this method and force painters to determine full preferred size?
     *
     * @param component component painter is applied to
     * @param preferred component preferred size
     * @return component preferred size or {@code null} if there is no preferred size
     */
    @Nullable
    public static Dimension getPreferredSize ( @NotNull final JComponent component, @Nullable final Dimension preferred )
    {
        return getPreferredSize ( component, preferred, false );
    }

    /**
     * Returns component preferred size or {@code null} if there is no preferred size.
     *
     * @param component        component painter is applied to
     * @param preferred        component preferred size
     * @param ignoreLayoutSize whether or not layout preferred size should be ignored
     * @return component preferred size or {@code null} if there is no preferred size
     */
    @Nullable
    public static Dimension getPreferredSize ( @NotNull final JComponent component, @Nullable final Dimension preferred,
                                               final boolean ignoreLayoutSize )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Painter's preferred size
        final Painter painter = getPainter ( component );
        Dimension ps = SwingUtils.max ( preferred, painter != null ? painter.getPreferredSize () : null );

        // Layout preferred size
        if ( !ignoreLayoutSize )
        {
            final LayoutManager layout = component.getLayout ();
            if ( layout != null )
            {
                ps = SwingUtils.max ( ps, layout.preferredLayoutSize ( component ) );
            }
        }

        return ps;
    }

    /**
     * Returns whether or not component uses decoratable painter.
     *
     * @param component component to process
     * @return true if component uses decoratable painter, false otherwise
     */
    public static boolean isDecoratable ( @Nullable final Component component )
    {
        // todo Add additional decoration conditions for: && ((AbstractDecorationPainter)painter)... ?
        return getPainter ( component ) instanceof AbstractDecorationPainter;
    }
}