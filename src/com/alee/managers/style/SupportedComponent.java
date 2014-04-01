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

import com.alee.extended.button.WebSplitButton;
import com.alee.extended.checkbox.WebTristateCheckBox;
import com.alee.extended.label.WebMultiLineLabel;
import com.alee.extended.label.WebVerticalLabel;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.data.ComponentStyleConverter;
import com.alee.utils.LafUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.laf.Styleable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.table.JTableHeader;
import java.util.*;

/**
 * This enumeration represents list of supported components.
 * It also contains some default references and useful settings.
 *
 * @author Mikle Garin
 * @see com.alee.laf.WebLookAndFeel
 */

public enum SupportedComponent
{
    /**
     * Label-related components.
     */
    label
            {
                @Override
                public boolean supportsPainters ()
                {
                    return true;
                }

                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JLabel.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "LabelUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.labelUI;
                }
            },
    verticalLabel
            {
                @Override
                public boolean supportsPainters ()
                {
                    return true;
                }

                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return WebVerticalLabel.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "VerticalLabelUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.verticalLabelUI;
                }
            },
    multiLineLabel
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return WebMultiLineLabel.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "MultiLineLabelUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.multiLineLabelUI;
                }
            },
    toolTip
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JToolTip.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "ToolTipUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.toolTipUI;
                }
            },

    /**
     * Button-related components.
     */
    button
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JButton.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "ButtonUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.buttonUI;
                }
            },
    splitButton
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return WebSplitButton.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "SplitButtonUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.splitButtonUI;
                }
            },
    toggleButton
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JToggleButton.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "ToggleButtonUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.toggleButtonUI;
                }
            },
    checkBox
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JCheckBox.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "CheckBoxUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.checkBoxUI;
                }
            },
    tristateCheckBox
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return WebTristateCheckBox.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "TristateCheckBoxUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.tristateCheckBoxUI;
                }
            },
    radioButton
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JRadioButton.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "RadioButtonUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.radioButtonUI;
                }
            },

    /**
     * Menu-related components.
     */
    menuBar
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JMenuBar.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "MenuBarUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.menuBarUI;
                }
            },
    menu
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JMenu.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "MenuUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.menuUI;
                }
            },
    popupMenu
            {
                @Override
                public boolean supportsPainters ()
                {
                    return true;
                }

                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JPopupMenu.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "PopupMenuUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.popupMenuUI;
                }
            },
    menuItem
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JMenuItem.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "MenuItemUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.menuItemUI;
                }
            },
    checkBoxMenuItem
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JCheckBoxMenuItem.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "CheckBoxMenuItemUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.checkBoxMenuItemUI;
                }
            },
    radioButtonMenuItem
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JRadioButtonMenuItem.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "RadioButtonMenuItemUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.radioButtonMenuItemUI;
                }
            },
    popupMenuSeparator
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JPopupMenu.Separator.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "PopupMenuSeparatorUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.popupMenuSeparatorUI;
                }
            },

    /**
     * Separator component.
     */
    separator
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JSeparator.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "SeparatorUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.separatorUI;
                }
            },

    /**
     * Scroll-related components.
     */
    scrollBar
            {
                @Override
                public boolean supportsPainters ()
                {
                    return true;
                }

                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JScrollBar.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "ScrollBarUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.scrollBarUI;
                }
            },
    scrollPane
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JScrollPane.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "ScrollPaneUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.scrollPaneUI;
                }
            },
    viewport
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JViewport.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "ViewportUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.viewportUI;
                }
            },

    /**
     * Text-related components.
     */
    textField
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JTextField.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "TextFieldUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.textFieldUI;
                }
            },
    passwordField
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JPasswordField.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "PasswordFieldUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.passwordFieldUI;
                }
            },
    formattedTextField
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JFormattedTextField.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "FormattedTextFieldUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.formattedTextFieldUI;
                }
            },
    textArea
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JTextArea.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "TextAreaUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.textAreaUI;
                }
            },
    editorPane
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JEditorPane.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "EditorPaneUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.editorPaneUI;
                }
            },
    textPane
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JTextPane.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "TextPaneUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.textPaneUI;
                }
            },

    /**
     * Toolbar-related components.
     */
    toolBar
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JToolBar.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "ToolBarUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.toolBarUI;
                }
            },
    toolBarSeparator
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JToolBar.Separator.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "ToolBarSeparatorUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.toolBarSeparatorUI;
                }
            },

    /**
     * Table-related components.
     */
    table
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JTable.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "TableUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.tableUI;
                }
            },
    tableHeader
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JTableHeader.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "TableHeaderUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.tableHeaderUI;
                }
            },

    /**
     * Chooser components.
     */
    colorChooser
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JColorChooser.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "ColorChooserUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.colorChooserUI;
                }
            },
    fileChooser
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JFileChooser.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "FileChooserUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.fileChooserUI;
                }
            },

    /**
     * Container-related components.
     */
    panel
            {
                @Override
                public boolean supportsPainters ()
                {
                    return true;
                }

                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JPanel.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "PanelUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.panelUI;
                }
            },
    rootPane
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JRootPane.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "RootPaneUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.rootPaneUI;
                }
            },
    tabbedPane
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JTabbedPane.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "TabbedPaneUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.tabbedPaneUI;
                }
            },
    splitPane
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JSplitPane.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "SplitPaneUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.splitPaneUI;
                }
            },

    /**
     * Other data-related components.
     */
    progressBar
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JProgressBar.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "ProgressBarUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.progressBarUI;
                }
            },
    slider
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JSlider.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "SliderUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.sliderUI;
                }
            },
    spinner
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JSpinner.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "SpinnerUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.spinnerUI;
                }
            },
    tree
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JTree.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "TreeUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.treeUI;
                }
            },
    list
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JList.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "ListUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.listUI;
                }
            },
    comboBox
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JComboBox.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "ComboBoxUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.comboBoxUI;
                }
            },

    /**
     * Desktop-pane-related components.
     */
    desktopPane
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JDesktopPane.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "DesktopPaneUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.desktopPaneUI;
                }
            },
    desktopIcon
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JInternalFrame.JDesktopIcon.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "DesktopIconUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.desktopIconUI;
                }
            },
    internalFrame
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JInternalFrame.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "InternalFrameUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.internalFrameUI;
                }
            },

    /**
     * Option pane component.
     */
    optionPane
            {
                @Override
                public Class<? extends JComponent> getComponentClass ()
                {
                    return JOptionPane.class;
                }

                @Override
                public String getUIClassID ()
                {
                    return "OptionPaneUI";
                }

                @Override
                public String getDefaultUIClass ()
                {
                    return WebLookAndFeel.optionPaneUI;
                }
            };

    /**
     * Returns whether this component type supports painters or not.
     *
     * @return true if this component type supports painters, false otherwise
     */
    public boolean supportsPainters ()
    {
        return false;
    }

    /**
     * Returns component class for this component type.
     *
     * @return component class for this component type
     */
    public abstract Class<? extends JComponent> getComponentClass ();

    /**
     * Returns UI class ID used by LookAndFeel to store various settings.
     *
     * @return UI class ID
     */
    public abstract String getUIClassID ();

    /**
     * Returns default UI class canonical name.
     * This value is used in WebLookAndFeel to provide default UI classes.
     * However they can be reassigned before WebLookAndFeel installation.
     *
     * @return default UI class canonical name
     */
    public abstract String getDefaultUIClass ();

    /**
     * Returns style identifier for the specified component.
     * This identifier might be customized in component to force StyleManager provide another style for that specific component.
     *
     * @return component identifier used within style in skin descriptor
     */
    public String getComponentStyleId ( final JComponent component )
    {
        final Styleable styleable = LafUtils.getStyleable ( component );
        final String styleId = styleable != null ? styleable.getStyleId () : ComponentStyleConverter.DEFAULT_STYLE_ID;
        return styleId != null ? styleId : ComponentStyleConverter.DEFAULT_STYLE_ID;
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
     * Component type icons cache.
     */
    private static final Map<SupportedComponent, ImageIcon> componentIcons =
            new EnumMap<SupportedComponent, ImageIcon> ( SupportedComponent.class );

    /**
     * Returns component type icon.
     *
     * @return component type icon
     */
    public ImageIcon getIcon ()
    {
        ImageIcon icon = componentIcons.get ( this );
        if ( icon == null )
        {
            icon = new ImageIcon ( SupportedComponent.class.getResource ( "icons/component/" + this + ".png" ) );
            componentIcons.put ( this, icon );
        }
        return icon;
    }

    /**
     * Lazily initialized component types map by their UI class IDs.
     */
    private static final Map<String, SupportedComponent> componentByUIClassID =
            new HashMap<String, SupportedComponent> ( values ().length );

    /**
     * Returns supported component type by UI class ID.
     *
     * @param uiClassID UI class ID
     * @return supported component type by UI class ID
     */
    public static SupportedComponent getComponentTypeByUIClassID ( final String uiClassID )
    {
        if ( componentByUIClassID.size () == 0 )
        {
            for ( final SupportedComponent supportedComponent : values () )
            {
                componentByUIClassID.put ( supportedComponent.getUIClassID (), supportedComponent );
            }
        }
        return componentByUIClassID.get ( uiClassID );
    }

    /**
     * Returns list of component types which supports painters.
     *
     * @return list of component types which supports painters
     */
    public static List<SupportedComponent> getPainterSupportedComponents ()
    {
        final List<SupportedComponent> supportedComponents = new ArrayList<SupportedComponent> ();
        for ( final SupportedComponent sc : SupportedComponent.values () )
        {
            if ( sc.supportsPainters () )
            {
                supportedComponents.add ( sc );
            }
        }
        return supportedComponents;
    }
}