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

@SuppressWarnings ("SpellCheckingInspection")
public final class Styles
{
    /**
     * Default style ID for all components.
     */
    public static final String defaultStyle = "default";

    /**
     * Base components.
     */

    /**
     * {@link com.alee.laf.label.WebLabel} style IDs.
     */
    public static final String labelShade = "shade";

    /**
     * {@link com.alee.extended.label.WebVerticalLabel} style IDs.
     */
    public static final String verticallabelShade = "shade";

    /**
     * {@link com.alee.laf.panel.WebPanel} style IDs.
     */
    public static final String panelTransparent = "transparent";
    public static final String panelWhite = "white";
    public static final String panelDecorated = "decorated";

    /**
     * {@link com.alee.laf.scroll.WebScrollBar} style IDs.
     */
    public static final String scrollbarUndecorated = "undecorated";
    public static final String scrollbarButtonless = "buttonless";
    public static final String scrollbarUndecoratedButtonless = "undecorated-buttonless";

    /**
     * {@link com.alee.laf.scroll.WebScrollPane} style IDs.
     */
    public static final String scrollpaneBar = "scrollpane";
    public static final String scrollpaneVerticalBar = "scrollpane-vertical";
    public static final String scrollpaneHorizontalBar = "scrollpane-horizontal";
    public static final String scrollpaneUndecorated = "scrollpane-undecorated";

    /**
     * {@link com.alee.laf.button.WebButton} style IDs.
     */
    public static final String buttonIconOnly = "icon";
    public static final String buttonRolloverOnly = "rollover";
    public static final String buttonRolloverIconOnly = "rolloverIcon";

    /**
     * {@link com.alee.laf.button.WebToggleButton} style IDs.
     */
    public static final String togglebuttonIconOnly = "icon";
    public static final String togglebuttonRolloverOnly = "rollover";
    public static final String togglebuttonRolloverIconOnly = "rolloverIcon";

    /**
     * {@link com.alee.laf.spinner.WebSpinner} style IDs.
     */
    public static final String spinnerButton = "spinner";
    public static final String spinnerNextButton = "spinner-next";
    public static final String spinnerPrevButton = "spinner-prev";
    public static final String spinnerField = "spinner-field";

    /**
     * {@link com.alee.laf.combobox.WebComboBox} style IDs.
     */
    public static final String comboboxUndecorated = "combobox-undecorated";
    public static final String comboboxSelectedLabel = "combobox-selected";
    public static final String comboboxArrowButton = "combobox-arrow";
    public static final String comboboxListScrollPane = "combobox-list";
    public static final String comboboxListScrollBar = "combobox-list";
    public static final String comboboxListComponent = "combobox-list-component";
    public static final String comboboxListLabel = "combobox-list-cell";
    public static final String comboboxEditor = "combobox-editor";

    /**
     * {@link com.alee.laf.list.WebList} style IDs.
     */
    public static final String listCellRenderer = "list-cell";
    public static final String listTextCellRenderer = "list-text-cell";
    public static final String listIconCellRenderer = "list-icon-cell";
    public static final String listCellEditor = "list-cell-editor";

    /**
     * {@link com.alee.laf.table.WebTable} style IDs.
     */
    public static final String tableHeaderCellRenderer = "table-header-cell";
    public static final String tableCellRenderer = "table-cell";
    public static final String tableBooleanCellRenderer = "table-cell-boolean";
    public static final String tableBooleanCellEditor = "table-editor-boolean";
    public static final String tableDateCellEditor = "table-editor-date";

    /**
     * {@link com.alee.laf.tree.WebTree} style IDs.
     */
    public static final String treeCellRenderer = "tree-cell";
    public static final String treeCellEditor = "tree-cell-editor";
    public static final String treeFilterField = "tree-filter-field";

    /**
     * {@link com.alee.extended.tree.WebCheckBoxTree} style IDs.
     */
    public static final String checkboxTreeCellRenderer = "tree-cell";

    /**
     * {@link com.alee.laf.toolbar.WebToolBar} style IDs.
     */
    public static final String toolbarAttached = "attached";

    /**
     * {@link com.alee.laf.optionpane.WebOptionPane} style IDs.
     */
    public static final String optionpaneButton = "optionpane";
    public static final String optionpaneYesButton = "optionpane-yes";
    public static final String optionpaneNoButton = "optionpane-no";
    public static final String optionpaneOkButton = "optionpane-ok";
    public static final String optionpaneCancelButton = "optionpane-cancel";

    /**
     * {@link com.alee.laf.colorchooser.WebColorChooser} style IDs.
     */
    public static final String colorchooser = "colorchooser";
    public static final String colorchooserLabel = "colorchooser-label";
    public static final String colorchooserButtonsPanel = "colorchooser-buttons";
    public static final String colorchooserWebonlyCheck = "colorchooser-webonly";
    public static final String colorchooserButton = "colorchooser-button";
    public static final String colorchooserOkButton = "colorchooser-ok";
    public static final String colorchooserResetButton = "colorchooser-reset";
    public static final String colorchooserCancelButton = "colorchooser-cancel";

    /**
     * {@link com.alee.laf.filechooser.WebFileChooser} style IDs.
     */
    public static final String filechooserToolbar = "filechooser";
    public static final String filechooserUndecoratedToolbar = "filechooser-undecorated";
    public static final String filechooserToolbarButton = "filechooser-tool";
    public static final String filechooserHistoryScrollPane = "filechooser-history";
    public static final String filechooserHistoryScrollBar = "filechooser-history";
    public static final String filechooserCenterPanel = "filechooser-center";
    public static final String filechooserSouthPanel = "filechooser-south";
    public static final String filechooserSelectedLabel = "filechooser-selected";
    public static final String filechooserButton = "filechooser";
    public static final String filechooserAcceptButton = "filechooser-accept";
    public static final String filechooserCancelButton = "filechooser-cancel";
    public static final String filechooserRemovalListPanel = "filechooser-removal-list";
    public static final String filechooserPathField = "filechooser-path-field";
    public static final String filechooserTableEditor = "filechooser-table-editor";

    /**
     * {@link com.alee.laf.desktoppane.WebInternalFrame} style IDs.
     */
    public static final String internalframeButton = "internalframe";
    public static final String internalframeMinimizeButton = "internalframe-minimize";
    public static final String internalframeMaximizeButton = "internalframe-maximize";
    public static final String internalframeCloseButton = "internalframe-close";

    /**
     * {@link com.alee.laf.rootpane.WebRootPane} style IDs.
     */
    public static final String windowTitlePanel = "window-title";
    public static final String windowTitleLabel = "window-title";
    public static final String windowButton = "window";
    public static final String windowMinimizeButton = "window-minimize";
    public static final String windowMaximizeButton = "window-maximize";
    public static final String windowCloseButton = "window-close";

    /**
     * Custom WebLaF components.
     */

    /**
     * {@link com.alee.extended.label.WebHotkeyLabel} style IDs.
     */
    public static final String hotkeylabel = "hotkeylabel";

    /**
     * {@link com.alee.managers.tooltip.WebCustomTooltip} style IDs.
     */
    public static final String customtooltipLabel = "customtooltip";
    public static final String customtooltipHotkeyLabel = "customtooltip-hotkey";

    /**
     * {@link com.alee.extended.button.WebSwitch} style IDs.
     */
    public static final String wswitch = "wswitch";
    public static final String wswitchGripper = "wswitch-gripper";
    public static final String wswitchLabel = "wswitch";
    public static final String wswitchOffLabel = "wswitch-on";
    public static final String wswitchOnLabel = "wswitch-off";

    /**
     * {@link com.alee.extended.syntax.WebSyntaxPanel} style IDs.
     */
    public static final String syntaxpanel = "syntaxpanel";

    /**
     * {@link com.alee.extended.list.WebCheckBoxList} style IDs.
     */
    public static final String checkboxlistCellRenderer = "checkboxlist-cell";
    public static final String checkboxlistCellEditor = "checkboxlist-cell-editor";

    /**
     * {@link com.alee.extended.list.WebFileList} style IDs.
     */
    public static final String filelistCellRenderer = "filelist-cell";
    public static final String filelistTileCellRenderer = "filelist-tile-cell";
    public static final String filelistIconCellRenderer = "filelist-icon-cell";
    public static final String filelistCellEditor = "filelist-cell-editor";

    /**
     * {@link com.alee.extended.filechooser.WebFileDrop} style IDs.
     */
    public static final String filedrop = "filedrop";
    public static final String filedropPlate = "filedrop-plate";
    public static final String filedropPlateFileLabel = "filedrop-plate-file";
    public static final String filedropPlateRemoveButton = "filedrop-plate-remove";

    /**
     * {@link com.alee.extended.panel.WebCollapsiblePane} style IDs.
     */
    public static final String collapsiblepane = "collapsiblepane";
    public static final String collapsiblepaneHeaderPanel = "collapsiblepane-header";
    public static final String collapsiblepaneExpandButton = "collapsiblepane-expand";
    public static final String collapsiblepaneContentPanel = "collapsiblepane-content";

    /**
     * {@link com.alee.extended.panel.WebAccordion} style IDs.
     */
    public static final String accordion = "accordion";

    /**
     * {@link com.alee.extended.window.WebPopOver}
     */
    public static final String popover = "popover";

    /**
     * {@link com.alee.extended.statusbar.WebMemoryBar} style IDs.
     */
    public static final String memorybar = "memorybar";
    public static final String memorybarLabel = "memorybar-label";
    public static final String memorybarTooltipLabel = "memorybar-tip";

    /**
     * {@link com.alee.extended.date.WebCalendar} style IDs.
     */
    public static final String calendar = "calendar";
    public static final String calendarButton = "calendar-button";
    public static final String calendarPrevYearButton = "calendar-prev-year";
    public static final String calendarPrevMonthButton = "calendar-prev-month";
    public static final String calendarTitleLabel = "calendar-title";
    public static final String calendarNextMonthButton = "calendar-next-month";
    public static final String calendarNextYearButton = "calendar-next-year";
    public static final String calendarWeekTitlesPanel = "calendar-week-titles";
    public static final String calendarWeekTitleLabel = "calendar-week-title";
    public static final String calendarMonthPanel = "calendar-month";
    public static final String calendarMonthDateToggleButton = "calendar-date";
    public static final String calendarPrevMonthDateToggleButton = "calendar-prev-date";
    public static final String calendarCurrentMonthDateToggleButton = "calendar-current-date";
    public static final String calendarNextMonthDateToggleButton = "calendar-next-date";

    /**
     * {@link com.alee.extended.date.WebDateField} style IDs.
     */
    public static final String datefieldChooseButton = "datefield-choose";
    public static final String datefieldField = "datefield-field";
    public static final String datefieldCalendar = "datefield";

    /**
     * {@link com.alee.extended.colorchooser.WebColorChooserField} style IDs.
     */
    public static final String colorchooserfieldColorButton = "colorchooserfield-color";

    /**
     * {@link com.alee.extended.filechooser.WebFileChooserField} style IDs.
     */
    public static final String filechooserfield = "filechooserfield";
    public static final String filechooserfieldContentScroll = "filechooserfield-content";
    public static final String filechooserfieldContentScrollBar = "filechooserfield-content";
    public static final String filechooserfieldContentPanel = "filechooserfield-content";
    public static final String filechooserfieldFilePlate = "filechooserfield-file";
    public static final String filechooserfieldFileNameLabel = "filechooserfield-file";
    public static final String filechooserfieldFileRemoveButton = "filechooserfield-file-remove";
    public static final String filechooserfieldChooseButton = "filechooserfield-choose";

    /**
     * {@link com.alee.extended.filechooser.WebPathField} style IDs.
     */
    public static final String pathfield = "pathfield";
    public static final String pathfieldContentPanel = "pathfield-content";
    public static final String pathfieldRootButton = "pathfield-root";
    public static final String pathfieldPathButton = "pathfield-path";
    public static final String pathfieldChildrenButton = "pathfield-children";
    public static final String pathfieldPopupScroll = "pathfield-popup";

    /**
     * {@link com.alee.extended.breadcrumb.WebBreadcrumb} style IDs.
     */
    public static final String breadcrumb = "breadcrumb";
    public static final String breadcrumbLabel = "breadcrumb-element";
    public static final String breadcrumbButton = "breadcrumb-element";
    public static final String breadcrumbToggleButton = "breadcrumb-element";
    public static final String breadcrumbPanel = "breadcrumb-element";

    /**
     * {@link com.alee.extended.syntax.WebSyntaxArea} and {@link com.alee.extended.syntax.WebSyntaxScrollPane} style IDs.
     */
    public static final String syntaxareaScroll = "syntaxarea";
    public static final String syntaxareaScrollGutter = "syntaxarea-gutter";

    /**
     * {@link com.alee.extended.panel.WebComponentPane} style IDs.
     */
    public static final String componentpane = "componentpane";

    /**
     * {@link com.alee.extended.filechooser.WebDirectoryChooser} style IDs.
     */
    public static final String directorychooserToolbar = "directorychooser";
    public static final String directorychooserToolButton = "directorychooser-tool";
    public static final String directorychooserFolderUpButton = "directorychooser-tool-folderup";
    public static final String directorychooserHomeButton = "directorychooser-tool-home";
    public static final String directorychooserDriveButton = "directorychooser-tool-drive";
    public static final String directorychooserRefreshButton = "directorychooser-tool-refresh";
    public static final String directorychooserNewFolderButton = "directorychooser-tool-newfolder";
    public static final String directorychooserDeleteButton = "directorychooser-tool-delete";
    public static final String directorychooserButton = "directorychooser";
    public static final String directorychooserAcceptButton = "directorychooser-accept";
    public static final String directorychooserCancelButton = "directorychooser-cancel";

    /**
     * {@link com.alee.extended.ninepatch.NinePatchEditor} style IDs.
     */
    public static final String ninepatcheditorZoomSlider = "ninepatcheditor-zoom";
    public static final String ninepatcheditorFloatEditorSlider = "ninepatcheditor-editor-float";
    public static final String ninepatcheditorPreviewField = "ninepatcheditor-preview-field";

    /**
     * {@link com.alee.extended.tab.WebDocumentPane} style IDs.
     */
    public static final String documentpane = "documentpane";
    public static final String documentpaneCloseButton = "documentpane-close";
    public static final String documentpaneMenu = "documentpane";

    /**
     * {@link com.alee.extended.dock.WebDockablePane} and {@link com.alee.extended.dock.WebDockableFrame} style IDs.
     */
    public static final String dockableFrame = "dockable-frame";
    public static final String dockableFrameTitlePanel = "dockable-frame-title";
    public static final String dockableFrameTitleLabel = "dockable-frame-title";
    public static final String dockableFrameTitleButtons = "dockable-frame-buttons";
    public static final String dockableFrameTitleButton = "dockable-frame-tool";
    public static final String dockableFrameTitleIconButton = "dockable-frame-tool-icon";
}