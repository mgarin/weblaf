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

package com.alee.managers.style;

import com.alee.api.jdk.BiConsumer;
import com.alee.api.jdk.Function;
import com.alee.extended.breadcrumb.BreadcrumbDescriptor;
import com.alee.extended.breadcrumb.BreadcrumbElementShape;
import com.alee.extended.breadcrumb.BreadcrumbLayout;
import com.alee.extended.breadcrumb.BreadcrumbProgressBackground;
import com.alee.extended.breadcrumb.element.*;
import com.alee.extended.button.SplitButtonDescriptor;
import com.alee.extended.canvas.CanvasDescriptor;
import com.alee.extended.canvas.Gripper;
import com.alee.extended.checkbox.MixedIcon;
import com.alee.extended.checkbox.TristateCheckBoxDescriptor;
import com.alee.extended.date.DateFieldDescriptor;
import com.alee.extended.dock.DockableFrameDescriptor;
import com.alee.extended.dock.DockablePaneDescriptor;
import com.alee.extended.image.ImageDescriptor;
import com.alee.extended.label.AbstractStyledTextContent;
import com.alee.extended.label.HotkeyLabelBackground;
import com.alee.extended.label.StyledLabelDescriptor;
import com.alee.extended.label.StyledLabelText;
import com.alee.extended.link.LinkDescriptor;
import com.alee.extended.panel.SelectablePanelPainter;
import com.alee.extended.split.MultiSplitPaneDescriptor;
import com.alee.extended.split.MultiSplitPaneDividerDescriptor;
import com.alee.extended.statusbar.MemoryBarBackground;
import com.alee.extended.statusbar.StatusBarDescriptor;
import com.alee.extended.syntax.SyntaxPanelPainter;
import com.alee.extended.window.PopOverPainter;
import com.alee.extended.window.PopupDescriptor;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.*;
import com.alee.laf.checkbox.CheckBoxDescriptor;
import com.alee.laf.checkbox.CheckIcon;
import com.alee.laf.colorchooser.ColorChooserDescriptor;
import com.alee.laf.combobox.ComboBoxDescriptor;
import com.alee.laf.desktoppane.DesktopIconDescriptor;
import com.alee.laf.desktoppane.DesktopPaneDescriptor;
import com.alee.laf.desktoppane.InternalFrameDescriptor;
import com.alee.laf.filechooser.FileChooserDescriptor;
import com.alee.laf.label.LabelDescriptor;
import com.alee.laf.label.LabelIcon;
import com.alee.laf.label.LabelLayout;
import com.alee.laf.label.LabelText;
import com.alee.laf.list.ListDescriptor;
import com.alee.laf.menu.*;
import com.alee.laf.optionpane.OptionPaneDescriptor;
import com.alee.laf.panel.PanelDescriptor;
import com.alee.laf.progressbar.ProgressBarDescriptor;
import com.alee.laf.progressbar.ProgressBarText;
import com.alee.laf.radiobutton.RadioButtonDescriptor;
import com.alee.laf.radiobutton.RadioIcon;
import com.alee.laf.rootpane.RootPaneDescriptor;
import com.alee.laf.scroll.ScrollBarDescriptor;
import com.alee.laf.scroll.ScrollPaneDescriptor;
import com.alee.laf.scroll.layout.WebScrollPaneLayout;
import com.alee.laf.separator.SeparatorDescriptor;
import com.alee.laf.separator.SeparatorStripes;
import com.alee.laf.slider.SliderDescriptor;
import com.alee.laf.spinner.SpinnerDescriptor;
import com.alee.laf.splitpane.SplitPaneDescriptor;
import com.alee.laf.splitpane.SplitPaneDividerDescriptor;
import com.alee.laf.tabbedpane.TabbedPaneDescriptor;
import com.alee.laf.table.TableDescriptor;
import com.alee.laf.table.TableHeaderDescriptor;
import com.alee.laf.text.*;
import com.alee.laf.toolbar.ToolBarDescriptor;
import com.alee.laf.toolbar.ToolBarSeparatorDescriptor;
import com.alee.laf.tooltip.StyledToolTipText;
import com.alee.laf.tooltip.ToolTipDescriptor;
import com.alee.laf.tooltip.ToolTipText;
import com.alee.laf.tree.TreeDescriptor;
import com.alee.laf.viewport.ViewportDescriptor;
import com.alee.laf.viewport.WebViewportLayout;
import com.alee.managers.style.data.ComponentStyle;
import com.alee.managers.style.data.SkinInfo;
import com.alee.painter.Painter;
import com.alee.painter.decoration.AbstractDecoration;
import com.alee.painter.decoration.Decorations;
import com.alee.painter.decoration.NinePatchDecoration;
import com.alee.painter.decoration.WebDecoration;
import com.alee.painter.decoration.background.*;
import com.alee.painter.decoration.border.AbstractBorder;
import com.alee.painter.decoration.border.LineBorder;
import com.alee.painter.decoration.content.*;
import com.alee.painter.decoration.layout.*;
import com.alee.painter.decoration.shadow.AbstractShadow;
import com.alee.painter.decoration.shadow.ExpandingShadow;
import com.alee.painter.decoration.shadow.WebShadow;
import com.alee.painter.decoration.shape.ArrowShape;
import com.alee.painter.decoration.shape.BoundsShape;
import com.alee.painter.decoration.shape.EllipseShape;
import com.alee.painter.decoration.shape.WebShape;
import com.alee.skin.web.WebSkin;
import com.alee.utils.LafUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.TextUtils;
import com.alee.utils.XmlUtils;
import com.alee.utils.collection.ImmutableList;
import com.alee.utils.ninepatch.NinePatchIcon;
import com.alee.utils.reflection.LazyInstance;
import com.alee.utils.swing.WeakComponentData;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.plaf.ComponentUI;
import java.util.*;

/**
 * This manager handles all default Swing and custom WebLaF component styles.
 * It provides API for {@link ComponentDescriptor}s usage for adjusting default component settings.
 * It also manages global and per-component {@link Skin}s and custom component {@link Painter}s.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see Skin
 * @see SkinExtension
 * @see StyleId
 * @see StyleData
 */
public final class StyleManager
{
    /**
     * List of listeners for various style events.
     * todo Might cause memory leaks in components, replace with {@link com.alee.utils.swing.WeakComponentDataList}?
     */
    private static final EventListenerList listenerList = new EventListenerList ();

    /**
     * List of registered {@link ComponentDescriptor}s.
     * These are widely used across WebLaF to provide various common information about existing components.
     * {@link ComponentDescriptor}s were introduced to allow easy customization of component base supported by WebLaF.
     * You can introduce an absolutely new {@link JComponent} implementation by simply registering {@link ComponentDescriptor} for it.
     */
    private static final List<ComponentDescriptor> descriptors = new ArrayList<ComponentDescriptor> ( 60 );

    /**
     * {@link ComponentDescriptor}s cached by their identifiers.
     * This cache contains only {@link ComponentDescriptor}s which exist in {@link #descriptors} list.
     * Whenever some {@link ComponentDescriptor} is unregistered or replaced by another one it is also cleared from this cache.
     */
    private static final Map<String, ComponentDescriptor> descriptorsByIdentifier = new HashMap<String, ComponentDescriptor> ( 60 );

    /**
     * {@link ComponentDescriptor}s cached by {@link JComponent} class.
     * This cache contains only {@link ComponentDescriptor}s which exist in {@link #descriptors} list.
     * Whenever some {@link ComponentDescriptor} is unregistered or replaced by another one it is also cleared from this cache.
     */
    private static final Map<Class<? extends JComponent>, ComponentDescriptor> descriptorsByClass =
            new HashMap<Class<? extends JComponent>, ComponentDescriptor> ( 60 );

    /**
     * Various component style related data which includes:
     *
     * 1. Skins applied for each specific skinnable component
     * Used to determine skinnable components, update them properly and detect their current skin.
     *
     * 2. Style IDs set for each specific component
     * They are all collected and stored in StyleManager to determine their changes correctly.
     *
     * 3. Style children each styled component has
     * Those children are generally collected here for convenient changes tracking.
     */
    private static final WeakComponentData<JComponent, StyleData> styleData =
            new WeakComponentData<JComponent, StyleData> ( "StyleManager.StyleData", 200 );

    /**
     * Installed skin extensions.
     * They provide additional styles for active skin.
     * Whether or not each extension will be applied to currently active skin is decided based on the extension description.
     *
     * Note that there are a few important limitations:
     * 1. Extensions can be attached to skins only, not to other extensions
     * 2. You cannot put any style overrides into extensions
     */
    private static final List<SkinExtension> extensions = new ArrayList<SkinExtension> ();

    /**
     * Synchronization lock object for skin operations.
     * todo This lock is redundant if all actions must be performed on EDT anyway
     */
    private static final Object skinLock = new Object ();

    /**
     * {@link LazyInstance} for default WebLaF {@link Skin}.
     * It can be specified before WebLaF is installed or any managers are initialized.
     * It can be used to avoid extra loading time due to default skin or unnecessary UI updates later on.
     */
    private static LazyInstance<? extends Skin> defaultSkin = null;

    /**
     * Currently used skin.
     * This skin is applied to all newly created components styled by WebLaF except customized ones.
     */
    private static Skin currentSkin = null;

    /**
     * Whether strict style checks are enabled or not.
     * In case strict checks are enabled any incorrect properties or painters getter and setter calls will cause exceptions.
     * These exceptions will not cause UI to halt but they will properly inform about missing styles, incorrect settings etc.
     * It is highly recommended to keep this property enabled to see and fix all problems right away.
     */
    private static boolean strictStyleChecks = true;

    /**
     * Whether {@link StyleManager} is initialized or not.
     */
    private static boolean initialized = false;

    /**
     * Initializes {@link StyleManager} settings.
     */
    public static synchronized void initialize ()
    {
        if ( !initialized )
        {
            // Registering common annotations
            initializeAnnotations ();

            // Registering common component descriptors
            initializeDescriptors ();

            // Updating initialization mark
            initialized = true;

            // Applying default skin as current skin
            setSkin ( getDefaultSkin () );
        }
    }

    /**
     * Initializes common XStream annotations.
     */
    private static void initializeAnnotations ()
    {
        // Class aliases
        XmlUtils.processAnnotations ( SkinInfo.class );
        XmlUtils.processAnnotations ( ComponentStyle.class );
        XmlUtils.processAnnotations ( NinePatchIcon.class );
        XmlUtils.processAnnotations ( AbstractDecoration.class );
        XmlUtils.processAnnotations ( Decorations.class );
        XmlUtils.processAnnotations ( WebDecoration.class );
        XmlUtils.processAnnotations ( NinePatchDecoration.class );
        XmlUtils.processAnnotations ( AbstractShadow.class );
        XmlUtils.processAnnotations ( BoundsShape.class );
        XmlUtils.processAnnotations ( WebShape.class );
        XmlUtils.processAnnotations ( EllipseShape.class );
        XmlUtils.processAnnotations ( ArrowShape.class );
        XmlUtils.processAnnotations ( BreadcrumbElementShape.class );
        XmlUtils.processAnnotations ( WebShadow.class );
        XmlUtils.processAnnotations ( ExpandingShadow.class );
        XmlUtils.processAnnotations ( AbstractBorder.class );
        XmlUtils.processAnnotations ( LineBorder.class );
        XmlUtils.processAnnotations ( AbstractBackground.class );
        XmlUtils.processAnnotations ( AbstractClipBackground.class );
        XmlUtils.processAnnotations ( ColorBackground.class );
        XmlUtils.processAnnotations ( GradientBackground.class );
        XmlUtils.processAnnotations ( PresetTextureBackground.class );
        XmlUtils.processAnnotations ( AlphaLayerBackground.class );
        XmlUtils.processAnnotations ( MovingHighlightBackground.class );
        XmlUtils.processAnnotations ( BreadcrumbProgressBackground.class );
        XmlUtils.processAnnotations ( TextureType.class );
        XmlUtils.processAnnotations ( GradientType.class );
        XmlUtils.processAnnotations ( GradientColor.class );
        XmlUtils.processAnnotations ( Stripes.class );
        XmlUtils.processAnnotations ( Stripe.class );
        XmlUtils.processAnnotations ( Gripper.class );
        XmlUtils.processAnnotations ( BorderLayout.class );
        XmlUtils.processAnnotations ( OverflowLineLayout.class );
        XmlUtils.processAnnotations ( AlignLayout.class );
        XmlUtils.processAnnotations ( AbstractContentLayout.class );
        XmlUtils.processAnnotations ( IconTextLayout.class );
        XmlUtils.processAnnotations ( AbstractContent.class );
        XmlUtils.processAnnotations ( CheckIcon.class );
        XmlUtils.processAnnotations ( RadioIcon.class );
        XmlUtils.processAnnotations ( MixedIcon.class );
        XmlUtils.processAnnotations ( SeparatorStripes.class );
        XmlUtils.processAnnotations ( RoundRectangle.class );
        XmlUtils.processAnnotations ( DashFocus.class );
        XmlUtils.processAnnotations ( AbstractIconContent.class );
        XmlUtils.processAnnotations ( AbstractTextContent.class );
        XmlUtils.processAnnotations ( AbstractStyledTextContent.class );
        XmlUtils.processAnnotations ( LocaleTextContent.class );
        XmlUtils.processAnnotations ( ToolTipText.class );
        XmlUtils.processAnnotations ( StyledToolTipText.class );
        XmlUtils.processAnnotations ( ButtonLayout.class );
        XmlUtils.processAnnotations ( ButtonIcon.class );
        XmlUtils.processAnnotations ( ButtonText.class );
        XmlUtils.processAnnotations ( SimpleButtonIcon.class );
        XmlUtils.processAnnotations ( StyledButtonText.class );
        XmlUtils.processAnnotations ( LabelLayout.class );
        XmlUtils.processAnnotations ( LabelIcon.class );
        XmlUtils.processAnnotations ( LabelText.class );
        XmlUtils.processAnnotations ( StyledLabelText.class );
        XmlUtils.processAnnotations ( MenuItemLayout.class );
        XmlUtils.processAnnotations ( SimpleMenuItemLayout.class );
        XmlUtils.processAnnotations ( AcceleratorText.class );
        XmlUtils.processAnnotations ( ProgressBarText.class );
        XmlUtils.processAnnotations ( HotkeyLabelBackground.class );
        XmlUtils.processAnnotations ( MemoryBarBackground.class );

        // Painter aliases
        XmlUtils.processAnnotations ( PopOverPainter.class );
        XmlUtils.processAnnotations ( SelectablePanelPainter.class );
        XmlUtils.processAnnotations ( SyntaxPanelPainter.class );
        XmlUtils.processAnnotations ( BreadcrumbPanelPainter.class );
        XmlUtils.processAnnotations ( BreadcrumbLabelPainter.class );
        XmlUtils.processAnnotations ( BreadcrumbStyledLabelPainter.class );
        XmlUtils.processAnnotations ( BreadcrumbLinkPainter.class );
        XmlUtils.processAnnotations ( BreadcrumbButtonPainter.class );
        XmlUtils.processAnnotations ( BreadcrumbToggleButtonPainter.class );
        XmlUtils.processAnnotations ( BreadcrumbSplitButtonPainter.class );
        XmlUtils.processAnnotations ( BreadcrumbComboBoxPainter.class );
        XmlUtils.processAnnotations ( BreadcrumbDateFieldPainter.class );
        XmlUtils.processAnnotations ( BreadcrumbCheckBoxPainter.class );
        XmlUtils.processAnnotations ( BreadcrumbTristateCheckBoxPainter.class );
        XmlUtils.processAnnotations ( BreadcrumbRadioButtonPainter.class );
        XmlUtils.processAnnotations ( BreadcrumbTextFieldPainter.class );
        XmlUtils.processAnnotations ( BreadcrumbFormattedTextFieldPainter.class );
        XmlUtils.processAnnotations ( BreadcrumbPasswordFieldPainter.class );

        // Layout aliases
        XmlUtils.processAnnotations ( WebScrollPaneLayout.class );
        XmlUtils.processAnnotations ( WebScrollPaneLayout.UIResource.class );
        XmlUtils.processAnnotations ( WebViewportLayout.class );
        XmlUtils.processAnnotations ( WebViewportLayout.UIResource.class );
        XmlUtils.processAnnotations ( BreadcrumbLayout.class );
        XmlUtils.processAnnotations ( BreadcrumbLayout.UIResource.class );

        // Workaround for ScrollPaneLayout due to neccessity of its usage
        XmlUtils.omitField ( ScrollPaneLayout.class, "viewport" );
        XmlUtils.omitField ( ScrollPaneLayout.class, "vsb" );
        XmlUtils.omitField ( ScrollPaneLayout.class, "hsb" );
        XmlUtils.omitField ( ScrollPaneLayout.class, "rowHead" );
        XmlUtils.omitField ( ScrollPaneLayout.class, "colHead" );
        XmlUtils.omitField ( ScrollPaneLayout.class, "lowerLeft" );
        XmlUtils.omitField ( ScrollPaneLayout.class, "lowerRight" );
        XmlUtils.omitField ( ScrollPaneLayout.class, "upperLeft" );
        XmlUtils.omitField ( ScrollPaneLayout.class, "upperRight" );
        XmlUtils.omitField ( ScrollPaneLayout.class, "vsbPolicy" );
        XmlUtils.omitField ( ScrollPaneLayout.class, "hsbPolicy" );
    }

    /**
     * Initializes common {@link ComponentDescriptor}s.
     */
    private static void initializeDescriptors ()
    {
        /**
         * Basic components.
         */
        registerComponentDescriptor ( new CanvasDescriptor () );
        registerComponentDescriptor ( new ImageDescriptor () );

        /**
         * Label-related components.
         */
        registerComponentDescriptor ( new LabelDescriptor () );
        registerComponentDescriptor ( new StyledLabelDescriptor () );
        registerComponentDescriptor ( new ToolTipDescriptor () );
        registerComponentDescriptor ( new LinkDescriptor () );

        /**
         * Button-related components.
         */
        registerComponentDescriptor ( new ButtonDescriptor () );
        registerComponentDescriptor ( new SplitButtonDescriptor () );
        registerComponentDescriptor ( new ToggleButtonDescriptor () );
        registerComponentDescriptor ( new CheckBoxDescriptor () );
        registerComponentDescriptor ( new TristateCheckBoxDescriptor () );
        registerComponentDescriptor ( new RadioButtonDescriptor () );

        /**
         * Separator component.
         */
        registerComponentDescriptor ( new SeparatorDescriptor () );

        /**
         * Menu-related components.
         */
        registerComponentDescriptor ( new MenuBarDescriptor () );
        registerComponentDescriptor ( new MenuDescriptor () );
        registerComponentDescriptor ( new PopupMenuDescriptor () );
        registerComponentDescriptor ( new MenuItemDescriptor () );
        registerComponentDescriptor ( new CheckBoxMenuItemDescriptor () );
        registerComponentDescriptor ( new RadioButtonMenuItemDescriptor () );
        registerComponentDescriptor ( new PopupMenuSeparatorDescriptor () );

        /**
         * Container-related components.
         */
        registerComponentDescriptor ( new PanelDescriptor () );
        registerComponentDescriptor ( new BreadcrumbDescriptor () );
        registerComponentDescriptor ( new TabbedPaneDescriptor () );
        registerComponentDescriptor ( new SplitPaneDividerDescriptor () );
        registerComponentDescriptor ( new SplitPaneDescriptor () );
        registerComponentDescriptor ( new MultiSplitPaneDividerDescriptor () );
        registerComponentDescriptor ( new MultiSplitPaneDescriptor () );
        registerComponentDescriptor ( new PopupDescriptor () );

        /**
         * Rootpane-related components.
         */
        registerComponentDescriptor ( new RootPaneDescriptor () );

        /**
         * Toolbar-related components.
         */
        registerComponentDescriptor ( new ToolBarDescriptor () );
        registerComponentDescriptor ( new ToolBarSeparatorDescriptor () );

        /**
         * Statusbar-related components.
         */
        registerComponentDescriptor ( new StatusBarDescriptor () );

        /**
         * Scroll-related components.
         */
        registerComponentDescriptor ( new ScrollBarDescriptor () );
        registerComponentDescriptor ( new ViewportDescriptor () );
        registerComponentDescriptor ( new ScrollPaneDescriptor () );

        /**
         * Text-related components.
         */
        registerComponentDescriptor ( new TextFieldDescriptor () );
        registerComponentDescriptor ( new PasswordFieldDescriptor () );
        registerComponentDescriptor ( new FormattedTextFieldDescriptor () );
        registerComponentDescriptor ( new TextAreaDescriptor () );
        registerComponentDescriptor ( new EditorPaneDescriptor () );
        registerComponentDescriptor ( new TextPaneDescriptor () );

        /**
         * Table-related components.
         */
        registerComponentDescriptor ( new TableHeaderDescriptor () );
        registerComponentDescriptor ( new TableDescriptor () );

        /**
         * Custom data-related components.
         */
        registerComponentDescriptor ( new ProgressBarDescriptor () );
        registerComponentDescriptor ( new SliderDescriptor () );
        registerComponentDescriptor ( new SpinnerDescriptor () );
        registerComponentDescriptor ( new ComboBoxDescriptor () );
        registerComponentDescriptor ( new ListDescriptor () );
        registerComponentDescriptor ( new TreeDescriptor () );

        /**
         * Chooser components.
         */
        registerComponentDescriptor ( new ColorChooserDescriptor () );
        registerComponentDescriptor ( new FileChooserDescriptor () );

        /**
         * Desktop-pane-related components.
         */
        registerComponentDescriptor ( new DesktopPaneDescriptor () );
        registerComponentDescriptor ( new DesktopIconDescriptor () );
        registerComponentDescriptor ( new InternalFrameDescriptor () );

        /**
         * Dockable-pane-related components.
         */
        registerComponentDescriptor ( new DockablePaneDescriptor () );
        registerComponentDescriptor ( new DockableFrameDescriptor () );

        /**
         * Option pane component.
         */
        registerComponentDescriptor ( new OptionPaneDescriptor () );

        /**
         * Chooser components.
         */
        registerComponentDescriptor ( new DateFieldDescriptor () );
    }

    /**
     * Throws {@link StyleException} if manager is not yet initialized.
     *
     * @throws StyleException if manager is not yet initialized
     */
    private static void mustBeInitialized () throws StyleException
    {
        if ( !initialized )
        {
            throw new StyleException ( "StyleManager must be initialized first" );
        }
    }

    /**
     * Returns whether strict style checks are enabled or not.
     *
     * @return true if strict style checks are enabled, false otherwise
     */
    public static boolean isStrictStyleChecks ()
    {
        return strictStyleChecks;
    }

    /**
     * Sets whether strict style checks are enabled or not.
     *
     * @param strict whether strict style checks are enabled or not
     */
    public static void setStrictStyleChecks ( final boolean strict )
    {
        StyleManager.strictStyleChecks = strict;
    }

    /**
     * Returns all registered {@link ComponentDescriptor}s count.
     *
     * @return all registered {@link ComponentDescriptor}s count
     */
    public static int getDescriptorsCount ()
    {
        // Checking manager initialization
        mustBeInitialized ();

        // Synchronized by descriptors
        synchronized ( descriptors )
        {
            // Return descriptors count
            return descriptors.size ();
        }
    }

    /**
     * Returns immutable list of all registered {@link ComponentDescriptor}s.
     *
     * @return immutable list of all registered {@link ComponentDescriptor}s
     */
    public static List<ComponentDescriptor> getDescriptors ()
    {
        // Checking manager initialization
        mustBeInitialized ();

        // Synchronized by descriptors
        synchronized ( descriptors )
        {
            // Return an immutable copy of the source list
            return new ImmutableList<ComponentDescriptor> ( descriptors );
        }
    }

    /**
     * Returns {@link ComponentDescriptor} with the specified identifier.
     *
     * @param id  {@link ComponentDescriptor} identifier
     * @param <C> {@link JComponent} type
     * @param <U> {@link ComponentUI} type
     * @return {@link ComponentDescriptor} with the specified identifier
     */
    public static <C extends JComponent, U extends ComponentUI> ComponentDescriptor<C, U> getDescriptor ( final String id )
    {
        // Checking manager initialization
        mustBeInitialized ();

        // Synchronized by descriptors
        synchronized ( descriptors )
        {
            // Looking for descriptor
            final ComponentDescriptor<C, U> descriptor = getDescriptorImpl ( id );

            // Ensure we found descriptor
            if ( descriptor == null )
            {
                throw new StyleException ( "There is no descriptor registered with identifier: " + id );
            }

            return descriptor;
        }
    }

    /**
     * Returns {@link ComponentDescriptor} for the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to find {@link ComponentDescriptor} for
     * @param <C>       {@link JComponent} type
     * @param <U>       {@link ComponentUI} type
     * @return {@link ComponentDescriptor} for the specified {@link JComponent}
     */
    public static <C extends JComponent, U extends ComponentUI> ComponentDescriptor<C, U> getDescriptor ( final C component )
    {
        return getDescriptor ( component.getClass () );
    }

    /**
     * Returns {@link ComponentDescriptor} for the specified {@link JComponent} class.
     * This method will also ensure that returned {@link ComponentDescriptor} is never {@code null}.
     *
     * @param componentClass {@link JComponent} class to find {@link ComponentDescriptor} for
     * @param <C>            {@link JComponent} type
     * @param <U>            {@link ComponentUI} type
     * @return {@link ComponentDescriptor} for the specified {@link JComponent} class
     */
    public static <C extends JComponent, U extends ComponentUI> ComponentDescriptor<C, U> getDescriptor (
            final Class<? extends JComponent> componentClass )
    {
        // Checking manager initialization
        mustBeInitialized ();

        // Synchronized by descriptors
        synchronized ( descriptors )
        {
            // Looking for descriptor
            final ComponentDescriptor<C, U> descriptor = getDescriptorImpl ( componentClass );

            // Ensure we found descriptor
            if ( descriptor == null )
            {
                throw new StyleException ( "There is no descriptor registered for: " + componentClass );
            }

            return descriptor;
        }
    }

    /**
     * Returns {@link ComponentDescriptor} for the specified {@link JComponent} identifier.
     *
     * @param id  {@link JComponent} identifier
     * @param <C> {@link JComponent} type
     * @param <U> {@link ComponentUI} type
     * @return {@link ComponentDescriptor} for the specified {@link JComponent} identifier
     */
    private static <C extends JComponent, U extends ComponentUI> ComponentDescriptor<C, U> getDescriptorImpl ( final String id )
    {
        return descriptorsByIdentifier.get ( id );
    }

    /**
     * Returns {@link ComponentDescriptor} for the specified {@link JComponent} class.
     *
     * @param componentClass {@link JComponent} class to find {@link ComponentDescriptor} for
     * @param <C>            {@link JComponent} type
     * @param <U>            {@link ComponentUI} type
     * @return {@link ComponentDescriptor} for the specified {@link JComponent} class
     */
    private static <C extends JComponent, U extends ComponentUI> ComponentDescriptor<C, U> getDescriptorImpl (
            final Class<? extends JComponent> componentClass )
    {
        // Looking for superclass descriptors
        final ComponentDescriptor<C, U> descriptor;
        if ( descriptorsByClass.containsKey ( componentClass ) )
        {
            // Looking for registered descriptor
            descriptor = descriptorsByClass.get ( componentClass );
        }
        else
        {
            // Only check descriptors for JComponent superclass types
            final Class<?> superclass = componentClass.getSuperclass ();
            if ( JComponent.class.isAssignableFrom ( superclass ) )
            {
                final Class<? extends JComponent> componentSuperClass = ( Class<? extends JComponent> ) superclass;

                // Looking for component superclass descriptor
                descriptor = getDescriptorImpl ( componentSuperClass );

                // Caching descriptor
                descriptorsByClass.put ( componentClass, descriptor );
            }
            else
            {
                // No descriptor
                descriptor = null;
            }
        }
        return descriptor;
    }

    /**
     * Returns whether or not styling is supported for the component with the specified identifier.
     *
     * @param id identifier of the component to check styling support for
     * @return {@code true} if styling is supported for the component with the specified identifier, {@code false} otherwise
     */
    public static boolean isSupported ( final String id )
    {
        return !TextUtils.isEmpty ( id ) && getDescriptorImpl ( id ) != null;
    }

    /**
     * Returns whether or not styling of the specified {@link JComponent} is supported.
     *
     * @param component component to check styling support for
     * @return {@code true} if styling of the specified {@link JComponent} is supported, {@code false} otherwise
     */
    public static boolean isSupported ( final JComponent component )
    {
        return getDescriptorImpl ( component.getClass () ) != null;
    }

    /**
     * Registers new {@link ComponentDescriptor}.
     * It will replace any other {@link ComponentDescriptor} registered for the same {@link JComponent} type.
     * Manager initialization is not required to register {@link ComponentDescriptor}.
     *
     * @param descriptor {@link ComponentDescriptor} to register
     */
    public static void registerComponentDescriptor ( final ComponentDescriptor descriptor )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Synchronized by descriptors
        synchronized ( descriptors )
        {
            final Class componentClass = descriptor.getComponentClass ();

            // Removing existing descriptor with same class
            final ComponentDescriptor toRemove = descriptorsByClass.get ( componentClass );
            if ( toRemove != null )
            {
                unregisterComponentDescriptor ( toRemove );
            }

            // Saving new descriptor
            descriptors.add ( descriptor );

            // Caching descriptor
            descriptorsByIdentifier.put ( descriptor.getId (), descriptor );
            descriptorsByClass.put ( componentClass, descriptor );

            // Updating UIDefaults
            if ( WebLookAndFeel.isInstalled () )
            {
                descriptor.updateDefaults ( UIManager.getLookAndFeelDefaults () );
            }

            // todo Listeners for these changes?
        }
    }

    /**
     * Unregisters existing {@link ComponentDescriptor}.
     * Manager initialization is not required to unregister {@link ComponentDescriptor}.
     *
     * @param descriptor {@link ComponentDescriptor} to register
     */
    public static void unregisterComponentDescriptor ( final ComponentDescriptor descriptor )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Synchronized by descriptors
        synchronized ( descriptors )
        {
            // Removing descriptor
            descriptors.remove ( descriptor );

            // Removing descriptor cache by identifier
            descriptorsByIdentifier.remove ( descriptor.getId () );

            // Removing descriptor cache by class
            final Iterator<Map.Entry<Class<? extends JComponent>, ComponentDescriptor>> byClassIterator =
                    descriptorsByClass.entrySet ().iterator ();
            while ( byClassIterator.hasNext () )
            {
                if ( byClassIterator.next ().getValue () == descriptor )
                {
                    byClassIterator.remove ();
                }
            }

            // todo A way to restore default descriptors upon custom ones removal?
        }
    }

    /**
     * Returns {@link LazyInstance} for default {@link Skin}.
     *
     * @return {@link LazyInstance} for default {@link Skin}
     */
    public static LazyInstance<? extends Skin> getDefaultSkin ()
    {
        return defaultSkin != null ? defaultSkin : new LazyInstance<WebSkin> ( WebSkin.class );
    }

    /**
     * Changes default {@link Skin} used upon {@link StyleManager} initialization.
     * {@link StyleManager} doesn't need to be initialized to change it.
     *
     * @param skin      default {@link Skin} class name
     * @param arguments default {@link Skin} constructor arguments
     */
    public static void setDefaultSkin ( final String skin, final Object... arguments )
    {
        try
        {
            final Class<? extends Skin> skinClass = ReflectUtils.getClass ( skin );
            setDefaultSkin ( new LazyInstance<Skin> ( skinClass, arguments ) );
        }
        catch ( final ClassNotFoundException e )
        {
            final String msg = "Unable to load skin class for name: %s";
            throw new StyleException ( String.format ( msg, skin ), e );
        }
    }

    /**
     * Changes default {@link Skin} used upon {@link StyleManager} initialization.
     * {@link StyleManager} doesn't need to be initialized to change it.
     *
     * @param skin      default {@link Skin} class
     * @param arguments default {@link Skin} constructor arguments
     */
    public static void setDefaultSkin ( final Class<? extends Skin> skin, final Object... arguments )
    {
        setDefaultSkin ( new LazyInstance<Skin> ( skin, arguments ) );
    }

    /**
     * Changes default {@link Skin} used upon {@link StyleManager} initialization.
     * Manager initialization is not required to change default {@link Skin}.
     *
     * @param skin default skin class
     */
    public static void setDefaultSkin ( final LazyInstance<? extends Skin> skin )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Updating default skin class
        StyleManager.defaultSkin = skin;
    }

    /**
     * Returns currently applied {@link Skin}.
     *
     * @return currently applied {@link Skin}
     */
    public static Skin getSkin ()
    {
        // Synchronized by skin lock
        synchronized ( skinLock )
        {
            return currentSkin;
        }
    }

    /**
     * Applies {@link Skin} with the specified {@link Class} name to all {@link Styleable} components.
     * That {@link Skin} will also be applied to all {@link Styleable} components created afterwards.
     *
     * @param skin      {@link Skin} class name
     * @param arguments {@link Skin} constructor arguments
     * @return previously applied {@link Skin}
     */
    public static Skin setSkin ( final String skin, final Object... arguments )
    {
        try
        {
            final Class<? extends Skin> skinClass = ReflectUtils.getClass ( skin );
            return setSkin ( skinClass, arguments );
        }
        catch ( final ClassNotFoundException e )
        {
            final String msg = "Unable to load skin class for name: %s";
            throw new StyleException ( String.format ( msg, skin ), e );
        }
    }

    /**
     * Applies {@link Skin} of the specified {@link Class} to all {@link Styleable} components.
     * That {@link Skin} will also be applied to all {@link Styleable} components created afterwards.
     *
     * @param skin      {@link Skin} class
     * @param arguments {@link Skin} constructor arguments
     * @return previously applied {@link Skin}
     */
    public static Skin setSkin ( final Class<? extends Skin> skin, final Object... arguments )
    {
        return setSkin ( new LazyInstance<Skin> ( skin, arguments ) );
    }

    /**
     * Applies {@link Skin} based on the {@link LazyInstance} to all {@link Styleable} components.
     * That {@link Skin} will also be applied to all {@link Styleable} components created afterwards.
     *
     * @param skin {@link LazyInstance} for {@link Skin}
     * @return previously applied {@link Skin}
     */
    public static Skin setSkin ( final LazyInstance<? extends Skin> skin )
    {
        return setSkin ( skin.create () );
    }

    /**
     * Applies specified {@link Skin} to all {@link Styleable} components.
     * That {@link Skin} will also be applied to all {@link Styleable} components created afterwards.
     *
     * @param skin {@link Skin} to apply
     * @return previously applied {@link Skin}
     */
    public static Skin setSkin ( final Skin skin )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Checking manager initialization
        mustBeInitialized ();

        // Synchronized by skin lock
        synchronized ( skinLock )
        {
            // Checking skin reference
            if ( skin == null )
            {
                throw new StyleException ( "Null skin provided" );
            }

            // Checking skin support
            if ( !skin.isSupported () )
            {
                final String msg = "Skin is not supported in this system: %s";
                throw new StyleException ( String.format ( msg, skin ) );
            }

            // Saving previously applied skin
            final Skin previousSkin = currentSkin;

            // Uninstalling previous skin
            if ( previousSkin != null )
            {
                previousSkin.uninstall ();
            }

            // Updating currently applied skin
            currentSkin = skin;

            // Installing new skin
            if ( skin != null )
            {
                skin.install ();
            }

            // Applying new skin to all existing skinnable components
            styleData.forEach ( new BiConsumer<JComponent, StyleData> ()
            {
                @Override
                public void accept ( final JComponent component, final StyleData styleData )
                {
                    if ( !styleData.isPinnedSkin () && styleData.getSkin () == previousSkin )
                    {
                        // There is no need to update child style components here as we will reach them anyway
                        // So we simply update each single component skin separately
                        styleData.applySkin ( skin, false );
                    }
                }
            } );

            // Informing about skin change
            fireSkinChanged ( previousSkin, skin );

            return previousSkin;
        }
    }

    /**
     * Adds new {@link SkinExtension}s which will either be loaded right away if manager is initialized or later on if its not.
     * Manager initialization is not required to provide additional {@link SkinExtension}s.
     *
     * @param extensions {@link SkinExtension}s to add
     */
    public static void addExtensions ( final SkinExtension... extensions )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Synchronized by skin lock
        synchronized ( skinLock )
        {
            // Iterating through added extensions
            for ( final SkinExtension extension : extensions )
            {
                // Checking extension reference
                if ( extension == null )
                {
                    throw new StyleException ( "Null extension provided" );
                }

                // Saving extension
                StyleManager.extensions.add ( extension );

                // Performing additional actions if manager was already initialized
                // It is allowed to add extensions before LaF initialization to speedup startup
                if ( initialized )
                {
                    // Installing extension onto the current skin
                    // Components are not updated when extension is added because extension styles should not be used at this point yet
                    // If they are used it is an issue of components/extension initialization order and it should be fixed in application
                    getSkin ().applyExtension ( extension );
                }
            }
        }
    }

    /**
     * Returns list of installed {@link SkinExtension}s.
     *
     * @return list of installed {@link SkinExtension}s
     */
    public static List<SkinExtension> getExtensions ()
    {
        // Synchronized by skin lock
        synchronized ( skinLock )
        {
            return new ImmutableList<SkinExtension> ( extensions );
        }
    }

    /**
     * Returns component style ID.
     *
     * @param component component to retrieve style ID for
     * @return component style ID
     */
    public static StyleId getStyleId ( final JComponent component )
    {
        return getData ( component ).getStyleId ();
    }

    /**
     * Sets new component style ID.
     *
     * @param component component to set style ID for
     * @param id        new style ID
     * @return previously used style ID
     */
    public static StyleId setStyleId ( final JComponent component, final StyleId id )
    {
        return getData ( component ).setStyleId ( id );
    }

    /**
     * Resets style ID to default value.
     * This method forces component to instantly apply style with the specified ID to itself.
     *
     * @param component component to reset style ID for
     * @return previously used style ID
     */
    public static StyleId resetStyleId ( final JComponent component )
    {
        return getData ( component ).resetStyleId ( true );
    }

    /**
     * Applies current skin to the skinnable component.
     * This method is used only to setup style data into UI on install.
     * It is not recommended to use it outside of that install behavior.
     *
     * @param component component to apply skin to
     */
    public static void installSkin ( final JComponent component )
    {
        final StyleData data = getData ( component );
        data.install ();
        data.applySkin ( getSkin (), false );
    }

    /**
     * Updates current skin in the skinnable component.
     * This method is used only to properly update skin on various changes.
     * It is not recommended to use it outside of style manager behavior.
     *
     * @param component component to update skin for
     */
    public static void updateSkin ( final JComponent component )
    {
        getData ( component ).updateSkin ( true );
    }

    /**
     * Removes skin applied to the specified component and returns it.
     * This method is used only to cleanup style data from UI on uninstall.
     * It is not recommended to use it outside of that uninstall behavior.
     *
     * @param component component to remove skin from
     */
    public static void uninstallSkin ( final JComponent component )
    {
        final StyleData data = getData ( component );
        data.removeSkin ();
        data.uninstall ();
    }

    /**
     * Returns skin currently applied to the specified component.
     *
     * @param component component to retrieve applied skin from
     * @return skin currently applied to the specified component
     */
    public static Skin getSkin ( final JComponent component )
    {
        return getData ( component ).getSkin ();
    }

    /**
     * Applies specified custom skin to the skinnable component and all of its children linked via {@link com.alee.managers.style.StyleId}.
     * Actual linked children information is stored within {@link com.alee.managers.style.StyleData} data objects.
     * Custom skin provided using this method will not be replaced if application skin changes.
     *
     * @param component component to apply skin to
     * @param skin      skin to be applied
     * @return previously applied skin
     */
    public static Skin setSkin ( final JComponent component, final Skin skin )
    {
        return setSkin ( component, skin, false );
    }

    /**
     * Applies specified custom skin to the skinnable component and all of its children linked via {@link com.alee.managers.style.StyleId}.
     * Actual linked children information is stored within {@link com.alee.managers.style.StyleData} data objects.
     * Custom skin provided using this method will not be replaced if application skin changes.
     *
     * @param component   component to apply skin to
     * @param skin        skin to be applied
     * @param recursively whether or not should apply skin to child components
     * @return previously applied skin
     */
    public static Skin setSkin ( final JComponent component, final Skin skin, final boolean recursively )
    {
        return getData ( component ).applyCustomSkin ( skin, recursively );
    }

    /**
     * Resets skin for the specified component and all of its children linked via {@link com.alee.managers.style.StyleId}.
     * Actual linked children information is stored within {@link com.alee.managers.style.StyleData} data objects.
     * Resetting component skin will also include it back into the skin update cycle in case global skin will be changed.
     *
     * @param component component to reset skin for
     * @return skin applied to the specified component after reset
     */
    public static Skin resetSkin ( final JComponent component )
    {
        return getData ( component ).resetSkin ();
    }

    /**
     * Adds skin change listener.
     *
     * @param component component to listen skin changes on
     * @param listener  skin change listener to add
     */
    public static void addStyleListener ( final JComponent component, final StyleListener listener )
    {
        getData ( component ).addStyleListener ( listener );
    }

    /**
     * Removes skin change listener.
     *
     * @param component component to listen skin changes on
     * @param listener  skin change listener to remove
     */
    public static void removeStyleListener ( final JComponent component, final StyleListener listener )
    {
        getData ( component ).removeStyleListener ( listener );
    }

    /**
     * Returns custom painter for the specified component.
     *
     * @param component component to retrieve custom painter for
     * @return custom painter for the specified component
     */
    public static Painter getCustomPainter ( final JComponent component )
    {
        return getData ( component ).getCustomPainter ();
    }

    /**
     * Sets new custom {@link Painter} for the specified {@link JComponent}.
     * You should call this method when setting painter outside of the UI.
     *
     * @param component component to set painter for
     * @param painter   {@link Painter}
     * @return previously used custom {@link Painter}
     */
    public static Painter setCustomPainter ( final JComponent component, final Painter painter )
    {
        return getData ( component ).setCustomPainter ( painter );
    }

    /**
     * Resets custom {@link Painter} for the specified {@link JComponent} to default one.
     *
     * @param component {@link JComponent} to reset custom {@link Painter} for
     * @return {@code true} if custom {@link Painter} was successfully resetted, {@code false} otherwise
     */
    public static boolean resetCustomPainter ( final JComponent component )
    {
        return getData ( component ).resetCustomPainter ();
    }

    /**
     * Returns component style data.
     *
     * @param component component to retrieve style data for
     * @return component style data
     */
    protected static StyleData getData ( final JComponent component )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Checking manager initialization
        mustBeInitialized ();

        // Checking component support
        if ( !isSupported ( component ) )
        {
            final String msg = "Component styling is not supported: %s";
            throw new StyleException ( String.format ( msg, component ) );
        }

        // Ensure that component has correct UI first, fix for #376
        // This should never happen if WebLaF is installed before creating any Swing components
        // Component might be missing UI here because it's style identifier was applied from upper level component
        if ( !LafUtils.hasWebLafUI ( component ) )
        {
            // Trying to make component use WebLaF UI by forcefully updating it, but we do not force any specific UI
            // Also note that calling this might create new StyleData instance, so we have to be careful with that
            component.updateUI ();

            // Checking that proper UI was installed
            if ( !LafUtils.hasWebLafUI ( component ) )
            {
                // Our attempt to apply WebLaF UI has failed, throwing appropriate exception
                final String msg = "Component '%s' doesn't use WebLaF UI";
                throw new StyleException ( String.format ( msg, component.getClass () ) );
            }
        }

        // Retrieving component style data
        return styleData.get ( component, new Function<JComponent, StyleData> ()
        {
            @Override
            public StyleData apply ( final JComponent component )
            {
                return new StyleData ( component );
            }
        } );
    }

    /**
     * Adds skin change listener.
     *
     * @param listener skin change listener to add
     */
    public static void addSkinListener ( final SkinListener listener )
    {
        listenerList.add ( SkinListener.class, listener );
    }

    /**
     * Removes skin change listener.
     *
     * @param listener skin change listener to remove
     */
    public static void removeSkinListener ( final SkinListener listener )
    {
        listenerList.remove ( SkinListener.class, listener );
    }

    /**
     * Informs listeners about skin change.
     *
     * @param previous previously used skin
     * @param current  currently used skin
     */
    public static void fireSkinChanged ( final Skin previous, final Skin current )
    {
        for ( final SkinListener listener : listenerList.getListeners ( SkinListener.class ) )
        {
            listener.skinChanged ( previous, current );
        }
    }
}