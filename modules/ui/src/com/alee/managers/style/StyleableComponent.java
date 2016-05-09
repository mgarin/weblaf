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

import com.alee.api.IconSupport;
import com.alee.api.TitleSupport;
import com.alee.extended.button.WebSplitButton;
import com.alee.extended.checkbox.WebTristateCheckBox;
import com.alee.extended.date.WebDateField;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.statusbar.WebStatusBar;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.log.Log;
import com.alee.utils.CollectionUtils;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This enumeration represents list of Swing and WebLaF components which support WebLaF styling.
 * It also contains some references and useful settings for each component type.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.laf.WebLookAndFeel
 */

public enum StyleableComponent implements IconSupport, TitleSupport
{
    /**
     * Label-related components.
     */
    label ( JLabel.class, "LabelUI", WebLookAndFeel.labelUI, StyleId.label ),
    styledlabel ( WebStyledLabel.class, "StyledLabelUI", WebLookAndFeel.styledLabelUI, StyleId.styledlabel ),
    tooltip ( JToolTip.class, "ToolTipUI", WebLookAndFeel.toolTipUI, StyleId.tooltip ),

    /**
     * Button-related components.
     */
    button ( JButton.class, "ButtonUI", WebLookAndFeel.buttonUI, StyleId.button ),
    splitbutton ( WebSplitButton.class, "SplitButtonUI", WebLookAndFeel.splitButtonUI, StyleId.splitbutton ),
    togglebutton ( JToggleButton.class, "ToggleButtonUI", WebLookAndFeel.toggleButtonUI, StyleId.togglebutton ),
    checkbox ( JCheckBox.class, "CheckBoxUI", WebLookAndFeel.checkBoxUI, StyleId.checkbox ),
    tristatecheckbox ( WebTristateCheckBox.class, "TristateCheckBoxUI", WebLookAndFeel.tristateCheckBoxUI, StyleId.tristatecheckbox ),
    radiobutton ( JRadioButton.class, "RadioButtonUI", WebLookAndFeel.radioButtonUI, StyleId.radiobutton ),

    /**
     * Separator component.
     */
    separator ( JSeparator.class, "SeparatorUI", WebLookAndFeel.separatorUI, StyleId.separator ),

    /**
     * Menu-related components.
     */
    menubar ( JMenuBar.class, "MenuBarUI", WebLookAndFeel.menuBarUI, StyleId.menubar ),
    menu ( JMenu.class, "MenuUI", WebLookAndFeel.menuUI, StyleId.menu ),
    popupmenu ( JPopupMenu.class, "PopupMenuUI", WebLookAndFeel.popupMenuUI, StyleId.popupmenu ),
    menuitem ( JMenuItem.class, "MenuItemUI", WebLookAndFeel.menuItemUI, StyleId.menuitem ),
    checkboxmenuitem ( JCheckBoxMenuItem.class, "CheckBoxMenuItemUI", WebLookAndFeel.checkBoxMenuItemUI, StyleId.checkboxmenuitem ),
    radiobuttonmenuitem ( JRadioButtonMenuItem.class, "RadioButtonMenuItemUI", WebLookAndFeel.radioButtonMenuItemUI,
            StyleId.radiobuttonmenuitem ),
    popupmenuseparator ( JPopupMenu.Separator.class, "PopupMenuSeparatorUI", WebLookAndFeel.popupMenuSeparatorUI,
            StyleId.popupmenuseparator ),

    /**
     * Container-related components.
     */
    panel ( JPanel.class, "PanelUI", WebLookAndFeel.panelUI, StyleId.panel ),
    rootpane ( JRootPane.class, "RootPaneUI", WebLookAndFeel.rootPaneUI, StyleId.rootpane ),
    tabbedpane ( JTabbedPane.class, "TabbedPaneUI", WebLookAndFeel.tabbedPaneUI, StyleId.tabbedpane ),
    splitpane ( JSplitPane.class, "SplitPaneUI", WebLookAndFeel.splitPaneUI, StyleId.splitpane ),

    /**
     * Toolbar-related components.
     */
    toolbar ( JToolBar.class, "ToolBarUI", WebLookAndFeel.toolBarUI, StyleId.toolbar ),
    toolbarseparator ( JToolBar.Separator.class, "ToolBarSeparatorUI", WebLookAndFeel.toolBarSeparatorUI, StyleId.toolbarseparator ),

    /**
     * Statusbar-related components.
     */
    statusbar ( WebStatusBar.class, "StatusBarUI", WebLookAndFeel.statusBarUI, StyleId.statusbar ),

    /**
     * Scroll-related components.
     */
    scrollbar ( JScrollBar.class, "ScrollBarUI", WebLookAndFeel.scrollBarUI, StyleId.scrollbar ),
    scrollpane ( JScrollPane.class, "ScrollPaneUI", WebLookAndFeel.scrollPaneUI, StyleId.scrollpane ),
    viewport ( JViewport.class, "ViewportUI", WebLookAndFeel.viewportUI, StyleId.viewport ),

    /**
     * Text-related components.
     */
    textfield ( JTextField.class, "TextFieldUI", WebLookAndFeel.textFieldUI, StyleId.textfield ),
    passwordfield ( JPasswordField.class, "PasswordFieldUI", WebLookAndFeel.passwordFieldUI, StyleId.passwordfield ),
    formattedtextfield ( JFormattedTextField.class, "FormattedTextFieldUI", WebLookAndFeel.formattedTextFieldUI,
            StyleId.formattedtextfield ),
    textarea ( JTextArea.class, "TextAreaUI", WebLookAndFeel.textAreaUI, StyleId.textarea ),
    editorpane ( JEditorPane.class, "EditorPaneUI", WebLookAndFeel.editorPaneUI, StyleId.editorpane ),
    textpane ( JTextPane.class, "TextPaneUI", WebLookAndFeel.textPaneUI, StyleId.textpane ),

    /**
     * Table-related components.
     */
    tableheader ( JTableHeader.class, "TableHeaderUI", WebLookAndFeel.tableHeaderUI, StyleId.tableheader ),
    table ( JTable.class, "TableUI", WebLookAndFeel.tableUI, StyleId.table ),

    /**
     * Custom data-related components.
     */
    progressbar ( JProgressBar.class, "ProgressBarUI", WebLookAndFeel.progressBarUI, StyleId.progressbar ),
    slider ( JSlider.class, "SliderUI", WebLookAndFeel.sliderUI, StyleId.slider ),
    spinner ( JSpinner.class, "SpinnerUI", WebLookAndFeel.spinnerUI, StyleId.spinner ),
    combobox ( JComboBox.class, "ComboBoxUI", WebLookAndFeel.comboBoxUI, StyleId.combobox ),
    list ( JList.class, "ListUI", WebLookAndFeel.listUI, StyleId.list ),
    tree ( JTree.class, "TreeUI", WebLookAndFeel.treeUI, StyleId.tree ),

    /**
     * Chooser components.
     */
    colorchooser ( JColorChooser.class, "ColorChooserUI", WebLookAndFeel.colorChooserUI, StyleId.colorchooser ),
    filechooser ( JFileChooser.class, "FileChooserUI", WebLookAndFeel.fileChooserUI, StyleId.filechooser ),

    /**
     * Desktop-pane-related components.
     */
    desktoppane ( JDesktopPane.class, "DesktopPaneUI", WebLookAndFeel.desktopPaneUI, StyleId.desktoppane ),
    desktopicon ( JInternalFrame.JDesktopIcon.class, "DesktopIconUI", WebLookAndFeel.desktopIconUI, StyleId.desktopicon ),
    internalframe ( JInternalFrame.class, "InternalFrameUI", WebLookAndFeel.internalFrameUI, StyleId.internalframe ),

    /**
     * Option pane component.
     */
    optionpane ( JOptionPane.class, "OptionPaneUI", WebLookAndFeel.optionPaneUI, StyleId.optionpane ),

    /**
     * Chooser components.
     */
    datefield ( WebDateField.class, "DateFieldUI", WebLookAndFeel.dateFieldUI, StyleId.datefield );

    /**
     * Component type icons cache.
     */
    private static final Map<StyleableComponent, ImageIcon> componentIcons =
            new EnumMap<StyleableComponent, ImageIcon> ( StyleableComponent.class );

    /**
     * Lazily initialized component types map by their UI class IDs.
     */
    private static final Map<String, StyleableComponent> componentByUIClassID =
            new HashMap<String, StyleableComponent> ( values ().length );

    /**
     * Lazily initialized component types map by their UI classes.
     */
    private static final Map<Class<? extends ComponentUI>, StyleableComponent> componentByUIClass =
            new HashMap<Class<? extends ComponentUI>, StyleableComponent> ( values ().length );

    /**
     * Styleable component class.
     * It is provided directly in this enum.
     */
    protected final Class<? extends JComponent> componentClass;

    /**
     * Styleable component UI class ID.
     * It is provided directly in this enum.
     */
    protected final String uiClassID;

    /**
     * Styleable component default UI class canonical name.
     * It is taken from {@link com.alee.laf.WebLookAndFeel} class constants.
     */
    protected final String defaultUIClass;

    /**
     * Styleable component default style ID.
     * It is taken from {@link com.alee.managers.style.StyleId} class constants.
     */
    protected final StyleId defaultStyleId;

    /**
     * Constructs a reference to component with specified settings.
     *
     * @param componentClass component class for this component type
     * @param uiClassID      UI class ID used by LookAndFeel to store various settings
     * @param defaultUIClass default UI class canonical name
     * @param styleId        default style ID
     */
    private StyleableComponent ( final Class<? extends JComponent> componentClass, final String uiClassID, final String defaultUIClass,
                                 final StyleId styleId )
    {
        this.componentClass = componentClass;
        this.uiClassID = uiClassID;
        this.defaultUIClass = defaultUIClass;
        this.defaultStyleId = styleId;
    }

    /**
     * Returns component class for this component type.
     *
     * @return component class for this component type
     */
    public Class<? extends JComponent> getComponentClass ()
    {
        return componentClass;
    }

    /**
     * Returns UI class ID used by LookAndFeel to store various settings.
     *
     * @return UI class ID
     */
    public String getUIClassID ()
    {
        return uiClassID;
    }

    /**
     * Returns default UI class canonical name.
     * This value is used in WebLookAndFeel to provide default UI classes.
     * However they can be reassigned before WebLookAndFeel installation.
     *
     * @return default UI class canonical name
     */
    public String getDefaultUIClass ()
    {
        return defaultUIClass;
    }

    /**
     * Returns UI class for this component type.
     * Result of this method is not cached because UI classes might be changed in runtime.
     *
     * @return UI class for this component type
     */
    public Class<? extends ComponentUI> getUIClass ()
    {
        final Class type = ReflectUtils.getClassSafely ( UIManager.getString ( getUIClassID () ) );
        final Class defaultType = ReflectUtils.getClassSafely ( getDefaultUIClass () );
        return ReflectUtils.isAssignable ( defaultType, type ) ? type : defaultType;
    }

    /**
     * Returns default style ID for this component type.
     *
     * @return default style ID for this component type
     */
    public StyleId getDefaultStyleId ()
    {
        return defaultStyleId;
    }

    /**
     * Returns component type icon.
     *
     * @return component type icon
     */
    @Override
    public ImageIcon getIcon ()
    {
        if ( componentIcons.containsKey ( this ) )
        {
            return componentIcons.get ( this );
        }
        else
        {
            try
            {
                final ImageIcon icon = new ImageIcon ( StyleableComponent.class.getResource ( "icons/styleable/" + this + ".png" ) );
                componentIcons.put ( this, icon );
                return icon;
            }
            catch ( final Throwable e )
            {
                Log.get ().error ( "Unable to find component icon: " + this, e );
                componentIcons.put ( this, null );
                return null;
            }
        }
    }

    /**
     * Returns component title.
     *
     * @return component title
     */
    @Override
    public String getTitle ()
    {
        return ReflectUtils.getClassName ( getComponentClass () );
    }

    /**
     * Returns whether or not specified component is supported.
     *
     * @param component component to determine type for
     * @return true if specified component is supported, false otherwise
     */
    public static boolean isSupported ( final Component component )
    {
        return component instanceof JComponent && isSupported ( ( ( JComponent ) component ).getUIClassID () );
    }

    /**
     * Returns whether or not component with the specified UI class ID is supported.
     *
     * @param uiClassID component UI class ID
     * @return true if component with the specified UI class ID is supported, false otherwise
     */
    public static boolean isSupported ( final String uiClassID )
    {
        return getImpl ( uiClassID ) != null;
    }

    /**
     * Returns whether or not component with the specified UI is supported.
     *
     * @param ui component UI
     * @return true if component with the specified UI is supported, false otherwise
     */
    public static boolean isSupported ( final ComponentUI ui )
    {
        return isSupported ( ui.getClass () );
    }

    /**
     * Returns whether or not component with the specified UI class is supported.
     *
     * @param uiClass UI class
     * @return true if component with the specified UI class is supported, false otherwise
     */
    public static boolean isSupported ( final Class<? extends ComponentUI> uiClass )
    {
        return getImpl ( uiClass ) != null;
    }

    /**
     * Returns supported component type by component.
     *
     * @param component component to determine type for
     * @return supported component type by component
     * @throws com.alee.managers.style.StyleException in case component is not supported
     */
    public static StyleableComponent get ( final JComponent component )
    {
        return get ( component.getUIClassID () );
    }

    /**
     * Returns supported component type by UI class ID.
     *
     * @param uiClassID component UI class ID
     * @return supported component type by UI class ID
     * @throws com.alee.managers.style.StyleException in case component is not supported
     */
    public static StyleableComponent get ( final String uiClassID )
    {
        final StyleableComponent type = getImpl ( uiClassID );
        if ( type == null )
        {
            throw new StyleException ( "Unsupported component UI class ID: " + uiClassID );
        }
        return type;
    }

    /**
     * Returns supported component type by component UI.
     *
     * @param ui component UI
     * @return supported component type by component UI
     */
    public static StyleableComponent get ( final ComponentUI ui )
    {
        return get ( ui.getClass () );
    }

    /**
     * Returns supported component type by component UI class.
     *
     * @param uiClass UI class
     * @return supported component type by component UI class
     * @throws com.alee.managers.style.StyleException in case component is not supported
     */
    public static StyleableComponent get ( final Class<? extends ComponentUI> uiClass )
    {
        final StyleableComponent type = getImpl ( uiClass );
        if ( type == null )
        {
            throw new StyleException ( "Unsupported component UI class: " + uiClass );
        }
        return type;
    }

    /**
     * Returns supported component type by UI class ID.
     *
     * @param uiClassID UI class ID
     * @return supported component type by UI class ID
     */
    private static StyleableComponent getImpl ( final String uiClassID )
    {
        if ( componentByUIClassID.size () == 0 )
        {
            for ( final StyleableComponent supportedComponent : values () )
            {
                componentByUIClassID.put ( supportedComponent.getUIClassID (), supportedComponent );
            }
        }
        return componentByUIClassID.get ( uiClassID );
    }

    /**
     * Returns supported component type by component UI class.
     *
     * @param uiClass UI class
     * @return supported component type by component UI class
     */
    private static StyleableComponent getImpl ( final Class<? extends ComponentUI> uiClass )
    {
        StyleableComponent type = componentByUIClass.get ( uiClass );
        if ( type == null )
        {
            // This method uses a different cache due to possibility of overriden UI classes usage
            // Generally it caches types returned by provided UI classes instead of default ones
            for ( final StyleableComponent supportedComponent : values () )
            {
                final Class<? extends ComponentUI> typeClass = supportedComponent.getUIClass ();
                if ( ReflectUtils.isAssignable ( typeClass, uiClass ) )
                {
                    type = supportedComponent;
                    componentByUIClass.put ( uiClass, supportedComponent );
                    break;
                }
            }
        }
        return type;
    }

    /**
     * Returns list of component types which supports painters.
     *
     * @return list of component types which supports painters
     */
    public static List<StyleableComponent> list ()
    {
        return CollectionUtils.asList ( values () );
    }
}