package com.alee.laf;

/**
 * Class containing various custom style IDs.
 * <p/>
 * Most of these styles are used by various custom complex WebLaF component parts.
 * They are provided to allow restyling those parts without affecting default component style.
 * <p/>
 * You are free to override these variables to your liking.
 * Though you should do that before creating any components which use them.
 *
 * @author Mikle Garin
 */

public final class Styles
{
    /**
     * todo 1. Create style reference system on interfaces
     * todo 2. Provide them instead of strings to avoid confusion in constructors and style types
     */

    /**
     * Base components.
     */

    /**
     * {@link com.alee.laf.rootpane.WebRootPane} style IDs.
     */
    public static String windowTitlePanel = "window-title";
    public static String windowTitleLabel = "window-title";
    public static String windowButton = "window-button";
    public static String windowMinimizeButton = "window-minimize";
    public static String windowMaximizeButton = "window-maximize";
    public static String windowCloseButton = "window-close";

    /**
     * {@link com.alee.laf.colorchooser.WebColorChooser}
     */
    public static String colorchooser = "colorchooser";
    public static String colorchooserLabel = "colorchooser-label";
    public static String colorchooserButtonsPanel = "colorchooser-buttons";
    public static String colorchooserButton = "colorchooser-button";
    public static String colorchooserOkButton = "colorchooser-ok";
    public static String colorchooserResetButton = "colorchooser-reset";
    public static String colorchooserCancelButton = "colorchooser-cancel";

    /**
     * Custom WebLaF components.
     */

    /**
     * {@link com.alee.managers.tooltip.WebCustomTooltip} style IDs.
     */
    public static String customtooltipLabel = "customtooltip";
    public static String customtooltipHotkeyLabel = "customtooltip-hotkey";

    /**
     * {@link com.alee.extended.syntax.WebSyntaxPanel} style IDs.
     */
    public static String syntaxpanel = "syntaxpanel";

    /**
     * {@link com.alee.extended.filechooser.WebFileDrop} style IDs.
     */
    public static String filedrop = "filedrop";

    /**
     * {@link com.alee.extended.window.WebPopOver}
     */
    public static String popover = "popover";

    /**
     * {@link com.alee.extended.statusbar.WebMemoryBar} style IDs.
     */
    public static String memorybar = "memorybar";
    public static String memorybarLabel = "memorybar-label";
    public static String memorybarTooltipLabel = "memorybar-tip";

    /**
     * {@link com.alee.extended.filechooser.WebPathField} style IDs.
     */
    public static String pathfield = "pathfield";
    public static String pathfieldContentPanel = "pathfield-content";
    public static String pathfieldPathButton = "pathfield-path";
    public static String pathfieldChildrenButton = "pathfield-children";
    public static String pathfieldPopupScroll = "pathfield-popup";

    /**
     * {@link com.alee.extended.breadcrumb.WebBreadcrumb} style IDs.
     */
    public static String breadcrumb = "breadcrumb";
    public static String breadcrumbLabelElement = "breadcrumb-element";
    public static String breadcrumbPanelElement = "breadcrumb-element";

    /**
     * {@link com.alee.extended.syntax.WebSyntaxArea} and {@link com.alee.extended.syntax.WebSyntaxScrollPane} style IDs.
     */
    public static String syntaxareaScroll = "syntaxarea";
    public static String syntaxareaScrollGutter = "syntaxarea-gutter";

    /**
     * {@link com.alee.extended.panel.WebComponentPane} style IDs.
     */
    public static String componentpane = "componentpane";

    /**
     * {@link com.alee.extended.tab.WebDocumentPane} style IDs.
     */
    public static String documentpane = "documentpane";
    public static String documentpaneMenu = "documentpane";

    /**
     * {@link com.alee.extended.dock.WebDockablePane} and {@link com.alee.extended.dock.WebDockableFrame} style IDs.
     */
    public static String dockableFrame = "dockable-frame";
    public static String dockableFrameTitlePanel = "dockable-frame-title";
    public static String dockableFrameTitleLabel = "dockable-frame-title";
    public static String dockableFrameTitleButtons = "dockable-frame-buttons";
    public static String dockableFrameTitleButton = "dockable-frame-tool";
    public static String dockableFrameTitleIconButton = "dockable-frame-tool-icon";
}