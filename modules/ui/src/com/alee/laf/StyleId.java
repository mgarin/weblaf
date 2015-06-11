package com.alee.laf;

import com.alee.managers.style.StyleException;
import com.alee.managers.style.SupportedComponent;
import com.alee.utils.CompareUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.laf.Styleable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.lang.ref.WeakReference;

/**
 * This class encapsulates style ID for single component.
 * <p/>
 * It also contains some style IDs for basic components.
 * Some of these styles are used by various custom complex WebLaF component parts.
 * They are provided to allow restyling those parts without affecting default component style.
 *
 * @author Mikle Garin
 */

public final class StyleId
{
    /**
     * Style IDs chain separator.
     */
    public static final String separator = ".";

    /**
     * Base components.
     */

    /**
     * {@link com.alee.laf.label.WebLabel} style IDs.
     */
    public static final StyleId labelShade = StyleId.of ( "shade" );

    /**
     * {@link com.alee.laf.panel.WebPanel} style IDs.
     */
    public static final StyleId panelTransparent = StyleId.of ( "transparent" );
    public static final StyleId panelWhite = StyleId.of ( "white" );
    public static final StyleId panelDecorated = StyleId.of ( "decorated" );

    /**
     * {@link com.alee.laf.scroll.WebScrollBar} style IDs.
     */
    public static final StyleId scrollbarUndecorated = StyleId.of ( "undecorated" );
    public static final StyleId scrollbarButtonless = StyleId.of ( "buttonless" );
    public static final StyleId scrollbarUndecoratedButtonless = StyleId.of ( "undecorated.buttonless" );

    /**
     * {@link com.alee.laf.scroll.WebScrollBar} style IDs.
     */
    public static final String scrollbarButton = "button";
    public static final String scrollbarDecreaseButton = "decrease";
    public static final String scrollbarIncreaseButton = "increase";

    /**
     * {@link com.alee.laf.scroll.WebScrollPane} style IDs.
     */
    public static final StyleId scrollpaneUndecorated = StyleId.of ( "undecorated" );
    public static final String scrollpaneBar = "scrollbar";
    public static final String scrollpaneVerticalBar = "vertical";
    public static final String scrollpaneHorizontalBar = "horizontal";

    /**
     * {@link com.alee.laf.splitpane.WebSplitPane} style IDs.
     */
    public static final String splitpaneOneTouchButton = "onetouch";
    public static final String splitpaneOneTouchLeftButton = "onetouch.left";
    public static final String splitpaneOneTouchRightButton = "onetouch.right";

    /**
     * {@link com.alee.laf.button.WebButton} style IDs.
     */
    public static final StyleId buttonIconOnly = StyleId.of ( "icon" );
    public static final StyleId buttonRolloverOnly = StyleId.of ( "rollover" );
    public static final StyleId buttonRolloverIconOnly = StyleId.of ( "rolloverIcon" );

    /**
     * {@link com.alee.laf.button.WebToggleButton} style IDs.
     */
    public static final StyleId togglebuttonIconOnly = StyleId.of ( "icon" );
    public static final StyleId togglebuttonRolloverOnly = StyleId.of ( "rollover" );
    public static final StyleId togglebuttonRolloverIconOnly = StyleId.of ( "rolloverIcon" );

    /**
     * {@link com.alee.laf.spinner.WebSpinner} style IDs.
     */
    public static final String spinnerButton = "button";
    public static final String spinnerNextButton = "next";
    public static final String spinnerPreviousButton = "previous";
    public static final String spinnerEditor = "editor";

    /**
     * {@link com.alee.laf.combobox.WebComboBox} style IDs.
     */
    public static final StyleId comboboxUndecorated = StyleId.of ( "undecorated" );
    public static final String comboboxEditor = "editor";
    public static final String comboboxArrowButton = "arrow";
    public static final String comboboxListScrollPane = "popup.scrollpane";
    public static final String comboboxListScrollBar = "popup.scrollbar";
    public static final String comboboxList = "popup.list";
    public static final String comboboxBoxRenderer = "box.renderer";
    public static final String comboboxListRenderer = "list.renderer";

    /**
     * {@link com.alee.laf.list.WebList} style IDs.
     */
    public static final String listCellRenderer = "renderer";
    public static final String listTextCellRenderer = "text.renderer";
    public static final String listIconCellRenderer = "icon.renderer";
    public static final String listCellEditor = "editor";

    /**
     * {@link com.alee.laf.table.WebTable} style IDs.
     */
    public static final String tableHeader = "header";
    public static final String tableHeaderCellRenderer = "renderer";
    public static final String tableCorner = "corner";
    public static final String tableCellRenderer = "cell";
    public static final String tableBooleanCellRenderer = "cell.boolean";
    public static final String tableCellEditor = "editor";
    public static final String tableBooleanCellEditor = "editor.boolean";
    public static final String tableDateCellEditor = "editor.date";

    /**
     * {@link com.alee.laf.tree.WebTree} style IDs.
     */
    public static final String treeCellRenderer = "renderer";
    public static final String treeCellEditor = "editor";

    /**
     * {@link com.alee.extended.tree.WebCheckBoxTree} style IDs.
     */
    public static final StyleId checkboxtree = StyleId.of ( "checkboxtree" );
    public static final String checkboxtreeCellRenderer = "renderer";

    /**
     * {@link com.alee.laf.toolbar.WebToolBar} style IDs.
     */
    public static final StyleId toolbarAttached = StyleId.of ( "attached" );

    /**
     * {@link com.alee.laf.optionpane.WebOptionPane} style IDs.
     */
    public static final String optionpaneButton = "button";
    public static final String optionpaneYesButton = "yes";
    public static final String optionpaneNoButton = "no";
    public static final String optionpaneOkButton = "ok";
    public static final String optionpaneCancelButton = "cancel";

    /**
     * {@link com.alee.laf.colorchooser.WebColorChooser} style IDs.
     */
    public static final StyleId colorchooserPanel = StyleId.of ( "colorchooser" );
    public static final String colorchooserLabel = "label";
    public static final String colorchooserControlsPanel = "controls";
    public static final String colorchooserWebonlyCheck = "webonly";
    public static final String colorchooserButton = "control.button";
    public static final String colorchooserOkButton = "ok";
    public static final String colorchooserResetButton = "reset";
    public static final String colorchooserCancelButton = "cancel";

    /**
     * {@link com.alee.laf.filechooser.WebFileChooser} style IDs.
     */
    public static final StyleId filechooserPanel = StyleId.of ( "filechooser" );
    public static final String filechooserToolbar = "decorated.bar";
    public static final String filechooserUndecoratedToolbar = "undecorated.bar";
    public static final String filechooserToolbarButton = "tool";
    public static final String filechooserHistoryScrollPane = "history";
    public static final String filechooserCenterPanel = "center";
    public static final String filechooserSouthPanel = "south";
    public static final String filechooserSelectedLabel = "selected";
    public static final String filechooserButton = "control.button";
    public static final String filechooserAcceptButton = "accept";
    public static final String filechooserCancelButton = "cancel";
    public static final String filechooserRemovalListPanel = "removal.list";
    public static final String filechooserPathField = "path";

    /**
     * {@link com.alee.laf.desktoppane.WebInternalFrame} style IDs.
     */
    public static final String internalframeTitleLabel = "title";
    public static final String internalframeButton = "decoration.button";
    public static final String internalframeMinimizeButton = "minimize";
    public static final String internalframeMaximizeButton = "maximize";
    public static final String internalframeCloseButton = "close";

    /**
     * {@link com.alee.laf.rootpane.WebRootPane} style IDs.
     */
    public static final String windowTitlePanel = "title";
    public static final String windowTitleLabel = "title";
    public static final String windowButton = "decoration.button";
    public static final String windowMinimizeButton = "minimize";
    public static final String windowMaximizeButton = "maximize";
    public static final String windowCloseButton = "close";

    /**
     * {@link com.alee.laf.tabbedpane.WebTabbedPane} style IDs.
     */
    public static final StyleId tabbedpaneAttached = StyleId.of ( "attached" );

    /**
     * Custom WebLaF components.
     */

    /**
     * {@link com.alee.utils.swing.WebHeavyWeightPopup} style IDs.
     */
    public static final StyleId heavyweightpopup = StyleId.of ( "heavyweightpopup" );

    /**
     * {@link com.alee.managers.notification.WebNotification} style IDs.
     */
    public static final StyleId notification = StyleId.of ( "notification" );
    public static final StyleId notificationWeb = StyleId.of ( "notification-web" );
    public static final StyleId notificationMac = StyleId.of ( "notification-mac" );
    public static final StyleId notificationDark = StyleId.of ( "notification-dark" );
    public static final StyleId notificationOptionButton = StyleId.of ( "notification-option" );

    /**
     * {@link com.alee.laf.grouping.GroupPane} style IDs.
     */
    public static final StyleId groupPane = StyleId.of ( "grouppane" );

    /**
     * {@link com.alee.extended.label.WebHotkeyLabel} style IDs.
     */
    public static final StyleId hotkeylabel = StyleId.of ( "hotkeylabel" );

    /**
     * {@link com.alee.managers.tooltip.WebCustomTooltip} style IDs.
     * todo Add proper parent (WebCustomTooltip should become a panel)
     */
    public static final StyleId customtooltipLabel = StyleId.of ( "customtooltip" );
    public static final StyleId customtooltipHotkeyLabel = StyleId.of ( "customtooltip.hotkey" );

    /**
     * {@link com.alee.extended.button.WebSwitch} style IDs.
     */
    public static final StyleId wswitch = StyleId.of ( "wswitch" );
    public static final String wswitchGripper = "gripper";
    public static final String wswitchLabel = "option";
    public static final String wswitchOffLabel = "on";
    public static final String wswitchOnLabel = "off";

    /**
     * {@link com.alee.extended.tree.WebTreeFilterField} style IDs.
     */
    public static final String treeFilterField = "treefilterfield";

    /**
     * {@link com.alee.extended.list.WebCheckBoxList} style IDs.
     * todo Create custom UI for this list and enclose these styles with it
     */
    public static final StyleId checkboxlist = StyleId.of ( "checkboxlist" );
    public static final String checkboxlistCellRenderer = "renderer";
    public static final String checkboxlistCellEditor = "editor";

    /**
     * {@link com.alee.extended.list.WebFileList} style IDs.
     */
    public static final StyleId filelist = StyleId.of ( "filelist" );
    public static final String filelistCellRenderer = "renderer";
    public static final String filelistTileCellRenderer = "renderer.tile";
    public static final String filelistIconCellRenderer = "renderer.icon";
    public static final String filelistCellEditor = "editor";

    /**
     * {@link com.alee.extended.filechooser.WebFileDrop} style IDs.
     */
    public static final StyleId filedrop = StyleId.of ( "filedrop" );
    public static final String filedropPlate = "plate";
    public static final String filedropPlateFileLabel = "file";
    public static final String filedropPlateRemoveButton = "remove";

    /**
     * {@link com.alee.extended.panel.WebCollapsiblePane} style IDs.
     */
    public static final StyleId collapsiblepane = StyleId.of ( "collapsiblepane" );
    public static final String collapsiblepaneHeaderPanel = "collapsiblepane-header";
    public static final String collapsiblepaneTitleLabel = "title";
    public static final String collapsiblepaneTopTitleLabel = "title.top";
    public static final String collapsiblepaneLeftTitleLabel = "title.left";
    public static final String collapsiblepaneBottomTitleLabel = "title.bottom";
    public static final String collapsiblepaneRightTitleLabel = "title.right";
    public static final String collapsiblepaneExpandButton = "expand";
    public static final String collapsiblepaneContentPanel = "content";

    /**
     * {@link com.alee.extended.panel.WebAccordion} style IDs.
     */
    public static final StyleId accordion = StyleId.of ( "accordion" );

    /**
     * {@link com.alee.managers.popup.WebPopup} style IDs.
     */
    public static final StyleId webpopup = StyleId.of ( "webpopup" );
    public static final StyleId webpopupBordered = StyleId.of ( "webpopup.bordered" );
    public static final StyleId webpopupDark = StyleId.of ( "webpopup.dark" );
    public static final StyleId webpopupLight = StyleId.of ( "webpopup.light" );
    public static final StyleId webpopupLightSmall = StyleId.of ( "webpopup.lightsmall" );
    public static final StyleId webpopupGreenLarge = StyleId.of ( "webpopup.greenlarge" );
    public static final StyleId webpopupBevel = StyleId.of ( "webpopup.bevel" );
    public static final StyleId webpopupGray = StyleId.of ( "webpopup.gray" );
    public static final StyleId webpopupGraySmall = StyleId.of ( "webpopup.graysmall" );
    public static final StyleId webpopupGrayEtched = StyleId.of ( "webpopup.grayetched" );
    public static final StyleId webpopupGrayDownTip = StyleId.of ( "webpopup.graydowntip" );

    /**
     * {@link com.alee.extended.window.WebPopOver}
     */
    public static final StyleId popover = StyleId.of ( "popover" );

    /**
     * {@link com.alee.extended.statusbar.WebMemoryBar} style IDs.
     */
    public static final StyleId memorybar = StyleId.of ( "memorybar" );
    public static final String memorybarLabel = "label";
    public static final String memorybarTooltipLabel = "tooltip";

    /**
     * {@link com.alee.extended.date.WebCalendar} style IDs.
     */
    public static final StyleId calendar = StyleId.of ( "calendar" );
    public static final String calendarHeaderPanel = "header";
    public static final String calendarButton = "control";
    public static final String calendarPrevYearButton = "prev.year";
    public static final String calendarPrevMonthButton = "prev.month";
    public static final String calendarNextMonthButton = "next.month";
    public static final String calendarNextYearButton = "next.year";
    public static final String calendarTitleLabel = "title";
    public static final String calendarWeekTitlesPanel = "week.titles";
    public static final String calendarWeekTitleLabel = "title";
    public static final String calendarMonthPanel = "month";
    public static final String calendarMonthDateToggleButton = "date";
    public static final String calendarPreviousMonthDateToggleButton = "previous.date";
    public static final String calendarCurrentMonthDateToggleButton = "current.date";
    public static final String calendarNextMonthDateToggleButton = "next.date";

    /**
     * {@link com.alee.extended.date.WebDateField} style IDs.
     */
    public static final StyleId datefield = StyleId.of ( "datefield" );
    public static final String datefieldChooseButton = "choose";
    public static final String datefieldCalendar = "popup.calendar";

    /**
     * {@link com.alee.extended.colorchooser.WebColorChooserField} style IDs.
     */
    public static final StyleId colorchooserfield = StyleId.of ( "colorchooserfield" );
    public static final String colorchooserfieldColorButton = "choose";

    /**
     * {@link com.alee.extended.filechooser.WebFileChooserField} style IDs.
     */
    public static final StyleId filechooserfield = StyleId.of ( "filechooserfield" );
    public static final String filechooserfieldContentPanel = "content";
    public static final String filechooserfieldContentScroll = "scroll";
    public static final String filechooserfieldFilePlate = "file";
    public static final String filechooserfieldFileNameLabel = "name";
    public static final String filechooserfieldFileRemoveButton = "remove";
    public static final String filechooserfieldChooseButton = "choose";

    /**
     * {@link com.alee.extended.filechooser.WebPathField} style IDs.
     */
    public static final StyleId pathfield = StyleId.of ( "pathfield" );
    public static final String pathfieldContentPanel = "content";
    public static final String pathfieldPathField = "path.field";
    public static final String pathfieldPopupScroll = "scroll";
    public static final String pathfieldRootButton = "path.root";
    public static final String pathfieldPathButton = "path.button";
    public static final String pathfieldChildrenButton = "path.children";

    /**
     * {@link com.alee.extended.breadcrumb.WebBreadcrumb} style IDs.
     */
    public static final StyleId breadcrumb = StyleId.of ( "breadcrumb" );
    public static final StyleId breadcrumbLabel = StyleId.of ( "breadcrumb.label" );
    public static final StyleId breadcrumbButton = StyleId.of ( "breadcrumb.button" );
    public static final StyleId breadcrumbToggleButton = StyleId.of ( "breadcrumb.togglebutton" );
    public static final StyleId breadcrumbPanel = StyleId.of ( "breadcrumb.panel" );

    /**
     * {@link com.alee.extended.syntax.WebSyntaxArea} and {@link com.alee.extended.syntax.WebSyntaxScrollPane} style IDs.
     */
    public static final StyleId syntaxareaScroll = StyleId.of ( "syntaxarea.scroll" );
    public static final String syntaxareaScrollGutter = "gutter";

    /**
     * {@link com.alee.extended.syntax.WebSyntaxPanel} style IDs.
     */
    public static final StyleId syntaxpanel = StyleId.of ( "syntaxpanel" );

    /**
     * {@link com.alee.extended.panel.WebComponentPane} style IDs.
     */
    public static final StyleId componentpane = StyleId.of ( "componentpane" );
    public static final String componentpanePanel = "panel";

    /**
     * {@link com.alee.extended.filechooser.WebDirectoryChooser} style IDs.
     */
    public static final StyleId directorychooser = StyleId.of ( "directorychooser" );
    public static final String directorychooserToolbar = "toolbar";
    public static final String directorychooserToolButton = "button";
    public static final String directorychooserFolderUpButton = "folderup";
    public static final String directorychooserHomeButton = "home";
    public static final String directorychooserDriveButton = "drive";
    public static final String directorychooserRefreshButton = "refresh";
    public static final String directorychooserNewFolderButton = "newfolder";
    public static final String directorychooserDeleteButton = "delete";
    public static final String directorychooserControlsPanel = "controls";
    public static final String directorychooserControlButton = "button";
    public static final String directorychooserAcceptButton = "accept";
    public static final String directorychooserCancelButton = "cancel";

    /**
     * {@link com.alee.extended.ninepatch.NinePatchEditor} style IDs.
     */
    public static final StyleId ninepatcheditor = StyleId.of ( "ninepatcheditor" );
    public static final String ninepatcheditorToolbar = "toolbar";
    public static final String ninepatcheditorZoomSlider = "zoom";
    public static final String ninepatcheditorFloatEditorSlider = "editor.float";
    public static final String ninepatcheditorPreviewField = "preview";

    /**
     * {@link com.alee.extended.tab.WebDocumentPane} style IDs.
     */
    public static final StyleId documentpane = StyleId.of ( "documentpane" );
    public static final String documentpaneTabbedPane = "tabbedpane";
    public static final String documentpaneCloseButton = "close";
    public static final String documentpaneMenu = "menu";

    /**
     * {@link com.alee.extended.dock.WebDockablePane} and {@link com.alee.extended.dock.WebDockableFrame} style IDs.
     */
    public static final StyleId dockablepane = StyleId.of ( "dockablepane" );
    public static final StyleId dockableframe = StyleId.of ( "dockableframe" );
    public static final String dockableframeTitlePanel = "title";
    public static final String dockableframeTitleLabel = "title";
    public static final String dockableframeTitleButtons = "buttons";
    public static final String dockableframeTitleButton = "tool.button";
    public static final String dockableframeTitleIconButton = "tool.icon.button";

    /**
     * Style ID.
     * Identifies some specific component style.
     */
    private final String id;

    /**
     * Related parent styleable element.
     * It is used to to build complete component ID based on {@link #id} and parent complete ID.
     * <p/>
     * For example: if you have button with ID "close" and a parent with ID "buttons" is specified - the final ID for your button will be
     * "buttons.close" and it should be provided within the installed skin to avoid styling issues.
     */
    private final WeakReference<Styleable> parent;

    /**
     * Constructs new style ID container.
     *
     * @param id style ID
     */
    private StyleId ( final String id )
    {
        this ( id, null );
    }

    /**
     * Constructs new style ID container.
     *
     * @param id     style ID
     * @param parent parent styleable element
     */
    private StyleId ( final String id, final Styleable parent )
    {
        super ();
        this.id = id;
        this.parent = parent != null ? new WeakReference<Styleable> ( parent ) : null;
    }

    /**
     * Returns style ID.
     *
     * @return style ID
     */
    public String getId ()
    {
        return id;
    }

    /**
     * Returns style ID container.
     *
     * @return style ID container
     */
    public Styleable getParent ()
    {
        return parent != null ? parent.get () : null;
    }

    /**
     * Returns complete style ID.
     *
     * @return complete style ID
     */
    public String getCompleteId ()
    {
        return getParent () != null ? get ( getParent () ).getCompleteId () + separator + getId () : getId ();
    }

    /**
     * Sets this style ID into the specified component.
     *
     * @param component component to set style ID for
     */
    public void set ( final Component component )
    {
        final Styleable styleable = getStyleable ( component );
        if ( styleable != null )
        {
            styleable.setStyleId ( this );
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals ( final Object obj )
    {
        if ( obj == null || !( obj instanceof StyleId ) )
        {
            return false;
        }
        final StyleId other = ( StyleId ) obj;
        return CompareUtils.equals ( getId (), other.getId () ) && getParent () == other.getParent ();
    }

    /**
     * Sets style ID into the specified component.
     *
     * @param component component to set style ID for
     */
    public static void set ( final Component component, final StyleId id )
    {
        final Styleable styleable = getStyleable ( component );
        if ( styleable != null )
        {
            styleable.setStyleId ( id );
        }
    }

    /**
     * Returns new style ID container.
     *
     * @param id style ID
     * @return new style ID container
     */
    public static StyleId of ( final String id )
    {
        return new StyleId ( id );
    }

    /**
     * Returns new style ID container.
     *
     * @param id     style ID
     * @param parent parent UI
     * @return new style ID container
     */
    public static StyleId of ( final String id, final ComponentUI parent )
    {
        final Styleable styleable = getStyleable ( parent );
        if ( parent == null || styleable == null )
        {
            throw new StyleException ( "Unable to determine Styleable element for parent: " + parent );
        }
        return new StyleId ( id, styleable );
    }

    /**
     * Returns new style ID container.
     *
     * @param id     style ID
     * @param parent parent component
     * @return new style ID container
     */
    public static StyleId of ( final String id, final Component parent )
    {
        final Styleable styleable = getStyleable ( parent );
        if ( parent == null || styleable == null )
        {
            throw new StyleException ( "Unable to determine Styleable element for parent: " + parent );
        }
        return new StyleId ( id, styleable );
    }

    /**
     * Returns complete style ID for the specified component.
     * This identifier might be customized in component to force StyleManager provide another style for that specific component.
     *
     * @param component component to retrieve complete style ID fore
     * @return component identifier used within style in skin descriptor
     */
    public static String getCompleteId ( final JComponent component )
    {
        return get ( component ).getCompleteId ();
    }

    /**
     * Returns style ID for the specified component.
     *
     * @param component component to retrieve style ID for
     * @return style ID for the specified component
     */
    public static StyleId get ( final JComponent component )
    {
        final Styleable styleable = LafUtils.getStyleable ( component );
        return styleable != null && styleable.getStyleId () != null ? styleable.getStyleId () : getDefault ( component );
    }

    /**
     * Returns style ID for the specified styleable element.
     *
     * @param styleable styleable element to retrieve style ID for
     * @return style ID for the specified styleable element
     */
    public static StyleId get ( final Styleable styleable )
    {
        return styleable != null && styleable.getStyleId () != null ? styleable.getStyleId () : getDefault ( styleable );
    }

    /**
     * Returns default style ID for the specified component.
     *
     * @param component component to retrieve default style ID for
     * @return default style ID for the specified component
     */
    public static StyleId getDefault ( final JComponent component )
    {
        final SupportedComponent type = SupportedComponent.getComponentTypeByUIClassID ( component.getUIClassID () );
        if ( type == null )
        {
            throw new StyleException ( "Unable to determine component type: " + component );
        }
        return type.getDefaultStyleId ();
    }

    /**
     * Returns default style ID for the specified component UI.
     *
     * @param component component to retrieve default style ID for
     * @return default style ID for the specified component UI
     */
    public static StyleId getDefault ( final ComponentUI ui )
    {
        final SupportedComponent type = SupportedComponent.getComponentTypeByUIClass ( ui.getClass () );
        if ( type == null )
        {
            throw new StyleException ( "Unable to determine component type for UI: " + ui );
        }
        return type.getDefaultStyleId ();
    }

    /**
     * Returns default style ID for the specified component.
     *
     * @param styleable to retrieve default style ID for
     * @return default style ID for the specified component
     */
    public static StyleId getDefault ( final Styleable styleable )
    {
        return styleable instanceof JComponent ? getDefault ( ( JComponent ) styleable ) : getDefault ( ( ComponentUI ) styleable );
    }

    /**
     * Returns styleable element for the specified component.
     *
     * @param component component to retrieve styleable element for
     * @return styleable element for the specified component
     */
    public static Styleable getStyleable ( final Component component )
    {
        if ( component != null )
        {
            if ( component instanceof Styleable )
            {
                return ( Styleable ) component;
            }
            return getStyleable ( LafUtils.getUI ( component ) );
        }
        return null;
    }

    /**
     * Returns styleable element for the specified ui.
     *
     * @param ui ui to retrieve styleable element for
     * @return styleable element for the specified ui
     */
    public static Styleable getStyleable ( final ComponentUI ui )
    {
        if ( ui != null && ui instanceof Styleable )
        {
            return ( Styleable ) ui;
        }
        return null;
    }
}