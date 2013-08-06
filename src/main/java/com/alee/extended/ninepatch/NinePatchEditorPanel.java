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

package com.alee.extended.ninepatch;

import com.alee.extended.drag.FileDropHandler;
import com.alee.extended.painter.AlphaLayerPainter;
import com.alee.extended.painter.ColorPainter;
import com.alee.extended.painter.NinePatchIconPainter;
import com.alee.extended.panel.ResizablePanel;
import com.alee.extended.panel.WebButtonGroup;
import com.alee.extended.statusbar.WebStatusBar;
import com.alee.extended.tree.WebFileTree;
import com.alee.laf.GlobalConstants;
import com.alee.laf.StyleConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.colorchooser.WebColorChooserDialog;
import com.alee.laf.filechooser.WebFileChooser;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.slider.WebSlider;
import com.alee.laf.splitpane.WebSplitPane;
import com.alee.laf.text.WebTextField;
import com.alee.laf.toolbar.ToolbarStyle;
import com.alee.laf.toolbar.WebToolBar;
import com.alee.laf.toolbar.WebToolBarStyle;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.settings.SettingsManager;
import com.alee.utils.*;
import com.alee.utils.ninepatch.NinePatchInterval;
import com.alee.utils.ninepatch.NinePatchIntervalType;
import info.clearthought.layout.TableLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 22.12.11 Time: 13:31
 */

public class NinePatchEditorPanel extends WebPanel
{
    public static final ImageIcon OPEN_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/open.png" ) );
    public static final ImageIcon SAVE_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/save.png" ) );
    public static final ImageIcon SAVE_AS_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/saveas.png" ) );
    public static final ImageIcon COPY_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/copy.png" ) );
    public static final ImageIcon PASTE_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/paste.png" ) );
    public static final ImageIcon UNDO_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/undo.png" ) );
    public static final ImageIcon REDO_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/redo.png" ) );

    public static final ImageIcon GUIDES_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/guides.png" ) );
    public static final ImageIcon RULER_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/ruler.png" ) );
    public static final ImageIcon STRETCH_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/stretch.png" ) );
    public static final ImageIcon CONTENT_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/content.png" ) );
    public static final ImageIcon RULER_CURSOR_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/ruler_cursor.png" ) );
    public static final ImageIcon AREA_CURSOR_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/area_cursor.png" ) );
    public static final ImageIcon MIN_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/min.png" ) );
    public static final ImageIcon MAX_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/max.png" ) );

    public static final ImageIcon ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/icon.png" ) );
    public static final ImageIcon SHOW_ICON_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/show_icon.png" ) );
    public static final ImageIcon SHOW_TEXT_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/show_text.png" ) );
    public static final ImageIcon FOREGROUND_COLOR_ICON =
            new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/foreground_color.png" ) );
    public static final ImageIcon TRANSPARENT_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/transparent.png" ) );
    public static final ImageIcon BACKGROUND_COLOR_ICON =
            new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/background_color.png" ) );

    private List<ChangeListener> changeListeners = new ArrayList<ChangeListener> ();

    private String imageSrc = null;

    private WebFileTree fileTree;
    private NinePatchEditor ninePatchEditor;

    private WebToolBar toolBar;
    private WebButton save;
    private WebButton saveAs;

    private ChangeListener changeListener;
    private ZoomChangeListener zoomChangeListener;

    private AlphaLayerPainter abp;
    private ColorPainter cbp;

    private WebPanel previewPanel;
    private WebLabel preview;

    private boolean openFromTreeEnabled = true;

    public NinePatchEditorPanel ()
    {
        super ();

        initializeAliases ();

        setLayout ( new BorderLayout () );

        ninePatchEditor = new NinePatchEditor ();
        ninePatchEditor.setTransferHandler ( new FileDropHandler ()
        {
            protected boolean filesImported ( List<File> files )
            {
                if ( files != null )
                {
                    for ( File file : files )
                    {
                        if ( ImageUtils.isImageLoadable ( file.getName () ) )
                        {
                            openImage ( file );
                            return true;
                        }
                    }
                }
                return false;
            }
        } );

        fileTree = new WebFileTree ();
        fileTree.setSelectionMode ( TreeSelectionModel.SINGLE_TREE_SELECTION );
        fileTree.setFileFilter ( GlobalConstants.IMAGES_AND_FOLDERS_FILTER );
        fileTree.addTreeSelectionListener ( new TreeSelectionListener ()
        {
            public void valueChanged ( TreeSelectionEvent e )
            {
                if ( openFromTreeEnabled && fileTree.getSelectionCount () > 0 )
                {
                    openImage ( fileTree.getSelectedFile () );
                }
            }
        } );
        fileTree.addMouseListener ( new MouseAdapter ()
        {
            public void mouseClicked ( MouseEvent e )
            {
                if ( e.getClickCount () == 2 && openFromTreeEnabled &&
                        fileTree.getSelectionCount () > 0 )
                {
                    openImage ( fileTree.getSelectedFile () );
                }
            }
        } );
        fileTree.setTransferHandler ( new TransferHandler ()
        {
            public int getSourceActions ( JComponent c )
            {
                return TransferHandler.COPY;
            }

            protected Transferable createTransferable ( JComponent c )
            {
                return fileTree.getSelectionCount () > 0 ? new StringSelection ( fileTree.getSelectedFile ().getAbsolutePath () ) : null;
            }
        } );

        WebScrollPane filesView = new WebScrollPane ( fileTree, false );
        filesView.setMinimumWidth ( 200 );
        filesView.setPreferredHeight ( 0 );
        filesView.setBorder ( BorderFactory.createMatteBorder ( 0, 0, 0, 1, WebToolBarStyle.borderColor ) );

        WebScrollPane editorView = ninePatchEditor.getView ();
        editorView.setBorder ( BorderFactory.createMatteBorder ( 0, 1, 0, 1, WebToolBarStyle.borderColor ) );

        WebPanel previewView = createPreviewPanel ();
        previewView.setBorder ( BorderFactory.createMatteBorder ( 0, 1, 0, 0, WebToolBarStyle.borderColor ) );

        final WebSplitPane previewSplit = new WebSplitPane ( WebSplitPane.HORIZONTAL_SPLIT );
        previewSplit.setLeftComponent ( editorView );
        previewSplit.setRightComponent ( previewView );
        previewSplit.setOneTouchExpandable ( true );
        previewSplit.setContinuousLayout ( true );
        previewSplit.setResizeWeight ( 1 );

        final WebSplitPane filesSplit = new WebSplitPane ( WebSplitPane.HORIZONTAL_SPLIT );
        filesSplit.setLeftComponent ( filesView );
        filesSplit.setRightComponent ( previewSplit );
        filesSplit.setOneTouchExpandable ( true );
        filesSplit.setContinuousLayout ( true );
        filesSplit.setResizeWeight ( 0 );
        add ( filesSplit, BorderLayout.CENTER );

        Integer fsl = SettingsManager.get ( "NinePatchEditor", "filesSplitLocation", ( Integer ) null );
        if ( fsl != null )
        {
            filesSplit.setDividerLocation ( fsl );
        }
        else
        {
            filesSplit.setDividerLocation ( 230 );
        }
        filesSplit.addPropertyChangeListener ( WebSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener ()
        {
            public void propertyChange ( PropertyChangeEvent pce )
            {
                SettingsManager.set ( "NinePatchEditor", "filesSplitLocation", filesSplit.getDividerLocation () );
            }
        } );

        Integer psl = SettingsManager.get ( "NinePatchEditor", "splitLocation", ( Integer ) null );
        if ( psl != null )
        {
            previewSplit.setDividerLocation ( psl );
        }
        previewSplit.addPropertyChangeListener ( WebSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener ()
        {
            public void propertyChange ( PropertyChangeEvent pce )
            {
                SettingsManager.set ( "NinePatchEditor", "splitLocation", previewSplit.getDividerLocation () );
            }
        } );

        ///////////////////////////////////////////////////////////////////////////////////

        toolBar = new WebToolBar ( WebToolBar.HORIZONTAL );
        toolBar.setToolbarStyle ( ToolbarStyle.attached );
        toolBar.setFloatable ( false );
        add ( toolBar, BorderLayout.NORTH );

        WebButton open = new WebButton ( OPEN_ICON );
        open.setLanguage ( "weblaf.ex.npeditor.openImage" );
        open.addHotkey ( NinePatchEditorPanel.this, Hotkey.CTRL_O );
        open.setRolloverDecoratedOnly ( true );
        open.addActionListener ( new ActionListener ()
        {
            private WebFileChooser wfc = null;

            public void actionPerformed ( ActionEvent e )
            {
                if ( wfc == null )
                {
                    wfc = new WebFileChooser ( imageSrc );
                    wfc.setDialogTitleKey ( "weblaf.ex.npeditor.openImage.title" );
                    wfc.setMultiSelectionEnabled ( false );
                    wfc.addChoosableFileFilter ( GlobalConstants.IMAGES_FILTER );
                    wfc.setFileFilter ( GlobalConstants.IMAGES_FILTER );
                }
                if ( imageSrc != null )
                {
                    wfc.setSelectedFile ( imageSrc );
                }
                if ( wfc.showOpenDialog ( SwingUtils.getWindowAncestor ( NinePatchEditorPanel.this ) ) == WebFileChooser.APPROVE_OPTION )
                {
                    openImage ( wfc.getSelectedFile () );
                }
            }
        } );
        toolBar.add ( open );

        toolBar.addSeparator ();

        save = new WebButton ( SAVE_ICON );
        save.setLanguage ( "weblaf.ex.npeditor.saveImage" );
        save.addHotkey ( NinePatchEditorPanel.this, Hotkey.CTRL_S );
        save.setRolloverDecoratedOnly ( true );
        save.setEnabled ( false );
        save.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                if ( imageSrc != null )
                {
                    try
                    {
                        saveImage ( new File ( imageSrc ) );
                    }
                    catch ( IOException e1 )
                    {
                        e1.printStackTrace ();
                    }
                }
            }
        } );
        toolBar.add ( save );

        saveAs = new WebButton ( SAVE_AS_ICON );
        saveAs.setLanguage ( "weblaf.ex.npeditor.saveImageAs" );
        saveAs.addHotkey ( NinePatchEditorPanel.this, Hotkey.CTRL_SHIFT_S );
        saveAs.setRolloverDecoratedOnly ( true );
        saveAs.addActionListener ( new ActionListener ()
        {
            private WebFileChooser wfc = null;

            public void actionPerformed ( ActionEvent e )
            {
                if ( wfc == null )
                {
                    wfc = new WebFileChooser ( imageSrc );
                    wfc.setDialogTitleKey ( "weblaf.ex.npeditor.saveImageAs.title" );
                    wfc.setMultiSelectionEnabled ( false );
                }
                if ( imageSrc != null )
                {
                    wfc.setSelectedFile ( imageSrc );
                }
                if ( wfc.showSaveDialog ( SwingUtils.getWindowAncestor ( NinePatchEditorPanel.this ) ) == WebFileChooser.APPROVE_OPTION )
                {
                    try
                    {
                        saveImage ( wfc.getSelectedFile () );
                        save.setEnabled ( true );
                    }
                    catch ( IOException e1 )
                    {
                        e1.printStackTrace ();
                    }
                }
            }
        } );
        toolBar.add ( saveAs );

        toolBar.addSeparator ();

        WebButton copy = new WebButton ( COPY_ICON );
        copy.setLanguage ( "weblaf.ex.npeditor.copyInfo" );
        copy.addHotkey ( NinePatchEditorPanel.this, Hotkey.CTRL_C );
        copy.setRolloverDecoratedOnly ( true );
        copy.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                if ( !ninePatchEditor.isSomeDragged () )
                {
                    SystemUtils.copyToClipboard ( XmlUtils.toXML ( ninePatchEditor.getNinePatchInfo () ) );
                }
            }
        } );
        toolBar.add ( copy );

        WebButton paste = new WebButton ( PASTE_ICON );
        paste.setLanguage ( "weblaf.ex.npeditor.pasteInfo" );
        paste.addHotkey ( NinePatchEditorPanel.this, Hotkey.CTRL_V );
        paste.setRolloverDecoratedOnly ( true );
        paste.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                if ( !ninePatchEditor.isSomeDragged () )
                {
                    String xml = SystemUtils.getStringFromClipboard ();
                    if ( xml != null )
                    {
                        // Retrieving data from xml
                        NinePatchInfo info = XmlUtils.fromXML ( xml );

                        // Restoring data if it fits size
                        ninePatchEditor.setNinePatchInfo ( info );
                    }
                }
            }
        } );
        toolBar.add ( paste );

        toolBar.addSeparator ();

        WebButton undo = new WebButton ( UNDO_ICON );
        undo.setLanguage ( "weblaf.ex.npeditor.undo" );
        undo.addHotkey ( NinePatchEditorPanel.this, Hotkey.CTRL_Z );
        undo.setRolloverDecoratedOnly ( true );
        undo.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                ninePatchEditor.undo ();
            }
        } );
        toolBar.add ( undo );

        WebButton redo = new WebButton ( REDO_ICON );
        redo.setLanguage ( "weblaf.ex.npeditor.redo" );
        redo.addHotkey ( NinePatchEditorPanel.this, Hotkey.CTRL_R );
        redo.addHotkey ( NinePatchEditorPanel.this, Hotkey.CTRL_SHIFT_Z );
        redo.setRolloverDecoratedOnly ( true );
        redo.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                ninePatchEditor.redo ();
            }
        } );
        toolBar.add ( redo );


        boolean si = SettingsManager.get ( "NinePatchEditor", "preview.showIcon", false );
        preview.setIcon ( si ? ICON : null );

        final WebToggleButton showIcon = new WebToggleButton ( SHOW_ICON_ICON );
        showIcon.setLanguage ( "weblaf.ex.npeditor.preview.showIcon" );
        showIcon.setRolloverDecoratedOnly ( true );
        showIcon.setSelected ( si );
        showIcon.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                boolean show = showIcon.isSelected ();
                SettingsManager.set ( "NinePatchEditor", "preview.showIcon", show );
                preview.setIcon ( show ? ICON : null );
            }
        } );
        toolBar.addToEnd ( showIcon );

        boolean st = SettingsManager.get ( "NinePatchEditor", "preview.showText", true );
        final WebToggleButton showText = new WebToggleButton ( SHOW_TEXT_ICON );
        showText.setLanguage ( "weblaf.ex.npeditor.preview.showText" );
        showText.setCursor ( Cursor.getDefaultCursor () );
        showText.setDrawFocus ( false );
        showText.setRolloverDecoratedOnly ( true );
        showText.setRolloverDarkBorderOnly ( false );
        showText.setDrawRight ( false );
        showText.setDrawRightLine ( true );
        showText.setShadeWidth ( 0 );
        showText.setSelected ( st );

        String pText = SettingsManager.get ( "NinePatchEditor", "preview.text", LanguageManager.get ( "weblaf.ex.npeditor.preview.text" ) );
        preview.setText ( st ? parseToMultilineHtml ( pText ) : "" );

        final WebTextField textField = new WebTextField ( 8 );
        textField.setText ( pText );
        textField.setHorizontalAlignment ( WebTextField.CENTER );
        textField.setDrawFocus ( false );
        textField.setEditable ( st );
        textField.setMargin ( -1 );
        textField.addCaretListener ( new CaretListener ()
        {
            public void caretUpdate ( CaretEvent e )
            {
                SettingsManager.set ( "NinePatchEditor", "preview.text", textField.getText () );
                preview.setText ( showText.isSelected () ? parseToMultilineHtml ( textField.getText () ) : "" );
            }
        } );
        showText.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                boolean show = showText.isSelected ();
                SettingsManager.set ( "NinePatchEditor", "preview.showText", show );
                textField.setEditable ( show );
                preview.setText ( show ? parseToMultilineHtml ( textField.getText () ) : "" );
            }
        } );
        textField.setLeadingComponent ( showText );
        toolBar.addToEnd ( textField );


        final WebButton chooseColor = new WebButton ( FOREGROUND_COLOR_ICON );
        chooseColor.setLanguage ( "weblaf.ex.npeditor.preview.foregroundColor" );
        preview.setForeground ( SettingsManager.get ( "NinePatchEditor", "preview.foregroundColor", Color.WHITE ) );
        chooseColor.setCursor ( Cursor.getDefaultCursor () );
        chooseColor.setDrawFocus ( false );
        chooseColor.setRolloverDecoratedOnly ( true );
        chooseColor.setRolloverDarkBorderOnly ( false );
        chooseColor.setDrawLeft ( false );
        chooseColor.setDrawLeftLine ( true );
        chooseColor.setShadeWidth ( 0 );
        chooseColor.addActionListener ( new ActionListener ()
        {
            private WebColorChooserDialog webColorChooser = null;

            public void actionPerformed ( ActionEvent e )
            {
                if ( webColorChooser == null )
                {
                    webColorChooser = new WebColorChooserDialog ( SwingUtils.getWindowAncestor ( previewPanel ) );
                    webColorChooser.setColor ( preview.getForeground () );
                }
                if ( webColorChooser.showDialog () == StyleConstants.OK_OPTION )
                {
                    Color nfg = webColorChooser.getColor ();
                    SettingsManager.set ( "NinePatchEditor", "preview.foregroundColor", nfg );
                    preview.setForeground ( nfg );
                }
            }
        } );
        textField.setTrailingComponent ( chooseColor );

        abp = new AlphaLayerPainter ();

        boolean da = SettingsManager.get ( "NinePatchEditor", "preview.transparentBackground", true );
        final WebToggleButton drawAlphaBackground = new WebToggleButton ( TRANSPARENT_ICON );
        drawAlphaBackground.setLanguage ( "weblaf.ex.npeditor.preview.transparentBackground" );
        drawAlphaBackground.setSelected ( da );
        drawAlphaBackground.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                SettingsManager.set ( "NinePatchEditor", "preview.transparentBackground", true );
                previewPanel.setPainter ( abp );
            }
        } );

        cbp = new ColorPainter ( SettingsManager.get ( "NinePatchEditor", "preview.backgroundColor", Color.WHITE ) );

        final WebToggleButton drawColoredBackground = new WebToggleButton ( ImageUtils.createColorIcon ( cbp.getColor () ) );
        drawColoredBackground.setLanguage ( "weblaf.ex.npeditor.preview.coloredBackground" );
        drawColoredBackground.setSelected ( !da );
        drawColoredBackground.addActionListener ( new ActionListener ()
        {
            private WebColorChooserDialog wcc = null;

            public void actionPerformed ( ActionEvent e )
            {
                SettingsManager.set ( "NinePatchEditor", "preview.transparentBackground", false );

                if ( wcc == null )
                {
                    wcc = new WebColorChooserDialog ( SwingUtils.getWindowAncestor ( previewPanel ), "" );
                    wcc.setLanguage ( "preview.backgroundColor.title" );
                    wcc.setColor ( cbp.getColor () );
                }
                wcc.setVisible ( true );

                if ( wcc.getResult () == StyleConstants.OK_OPTION )
                {
                    Color color = wcc.getColor ();
                    SettingsManager.set ( "NinePatchEditor", "preview.backgroundColor", color );
                    drawColoredBackground.setIcon ( ImageUtils.createColorIcon ( color ) );
                    cbp.setColor ( color );
                }

                previewPanel.setPainter ( cbp );
            }
        } );

        previewPanel.setPainter ( da ? abp : cbp );

        WebButtonGroup settings = new WebButtonGroup ( true, drawAlphaBackground, drawColoredBackground );
        //        settings.setButtonsDrawFocus ( false );
        toolBar.addToEnd ( settings );

        ///////////////////////////////////////////////////////////////////////////////////

        WebStatusBar bottomToolBar = new WebStatusBar ();
        add ( bottomToolBar, BorderLayout.SOUTH );

        boolean sgs = SettingsManager.get ( "NinePatchEditor", "showSpacing", true );
        final WebToggleButton showGuidesSpacing = new WebToggleButton ( GUIDES_ICON );
        showGuidesSpacing.setLanguage ( "weblaf.ex.npeditor.showSpacing" );
        showGuidesSpacing.setRolloverDecoratedOnly ( true );
        showGuidesSpacing.setSelected ( sgs );
        ninePatchEditor.setShowGuideSpacing ( sgs );
        showGuidesSpacing.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                boolean sgs = showGuidesSpacing.isSelected ();
                SettingsManager.set ( "NinePatchEditor", "showSpacing", sgs );
                ninePatchEditor.setShowGuideSpacing ( sgs );
            }
        } );

        boolean sr = SettingsManager.get ( "NinePatchEditor", "showRuler", true );
        final WebToggleButton showRuler = new WebToggleButton ( RULER_ICON );
        showRuler.setLanguage ( "weblaf.ex.npeditor.showRuler" );
        showRuler.setRolloverDecoratedOnly ( true );
        showRuler.setSelected ( sr );
        ninePatchEditor.setShowRuler ( sr );
        showRuler.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                boolean sr = showRuler.isSelected ();
                SettingsManager.set ( "NinePatchEditor", "showRuler", sr );
                ninePatchEditor.setShowRuler ( sr );
            }
        } );

        final boolean fc = SettingsManager.get ( "NinePatchEditor", "fillContent", true );
        final WebToggleButton fillContent = new WebToggleButton ( CONTENT_ICON );
        fillContent.setLanguage ( "weblaf.ex.npeditor.fillContent" );
        fillContent.setRolloverDecoratedOnly ( true );
        fillContent.setSelected ( fc );
        ninePatchEditor.setFillContentArea ( fc );
        fillContent.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                boolean fc = fillContent.isSelected ();
                SettingsManager.set ( "NinePatchEditor", "fillContent", fc );
                ninePatchEditor.setFillContentArea ( fc );
            }
        } );

        final boolean fs = SettingsManager.get ( "NinePatchEditor", "fillStretch", true );
        final WebToggleButton fillStretch = new WebToggleButton ( STRETCH_ICON );
        fillStretch.setLanguage ( "weblaf.ex.npeditor.fillStretch" );
        fillStretch.setRolloverDecoratedOnly ( true );
        fillStretch.setSelected ( fs );
        ninePatchEditor.setFillStretchAreas ( fs );
        fillStretch.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                boolean fs = fillStretch.isSelected ();
                SettingsManager.set ( "NinePatchEditor", "fillStretch", fs );
                ninePatchEditor.setFillStretchAreas ( fs );
            }
        } );

        final boolean rc = SettingsManager.get ( "NinePatchEditor", "rulerCursor", true );
        final WebToggleButton rulerCursor = new WebToggleButton ( RULER_CURSOR_ICON );
        rulerCursor.setLanguage ( "weblaf.ex.npeditor.rulerCursor" );
        rulerCursor.setRolloverDecoratedOnly ( true );
        rulerCursor.setSelected ( rc );
        ninePatchEditor.setShowRulerCursorPosition ( rc );
        rulerCursor.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                boolean rc = rulerCursor.isSelected ();
                SettingsManager.set ( "NinePatchEditor", "rulerCursor", rc );
                ninePatchEditor.setShowRulerCursorPosition ( rc );
            }
        } );

        final boolean ac = SettingsManager.get ( "NinePatchEditor", "areaCursor", false );
        final WebToggleButton areaCursor = new WebToggleButton ( AREA_CURSOR_ICON );
        areaCursor.setLanguage ( "weblaf.ex.npeditor.areaCursor" );
        areaCursor.setRolloverDecoratedOnly ( true );
        areaCursor.setSelected ( ac );
        ninePatchEditor.setShowAreaCursorPosition ( ac );
        areaCursor.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                boolean ac = areaCursor.isSelected ();
                SettingsManager.set ( "NinePatchEditor", "areaCursor", ac );
                ninePatchEditor.setShowAreaCursorPosition ( ac );
            }
        } );

        bottomToolBar.add ( showGuidesSpacing );
        bottomToolBar.add ( showRuler );
        bottomToolBar.addSeparator ();
        bottomToolBar.add ( fillContent );
        bottomToolBar.add ( fillStretch );
        bottomToolBar.addSeparator ();
        bottomToolBar.add ( rulerCursor );
        bottomToolBar.add ( areaCursor );

        //

        final WebSlider zoomSlider = new WebSlider ( NinePatchEditor.MIN_ZOOM, NinePatchEditor.MAX_ZOOM, ninePatchEditor.getZoom () )
        {
            public Dimension getPreferredSize ()
            {
                Dimension ps = super.getPreferredSize ();
                ps.width = 170;
                return ps;
            }
        };
        zoomSlider.setAngledThumb ( false );
        zoomSlider.setPaintTicks ( false );
        zoomSlider.setPaintLabels ( false );
        changeListener = new ChangeListener ()
        {
            public void stateChanged ( ChangeEvent e )
            {
                ninePatchEditor.removeZoomChangeListener ( zoomChangeListener );
                ninePatchEditor.setZoom ( zoomSlider.getValue () );
                ninePatchEditor.addZoomChangeListener ( zoomChangeListener );
            }
        };
        zoomSlider.addChangeListener ( changeListener );
        zoomChangeListener = new ZoomChangeListener ()
        {
            public void zoomChanged ()
            {
                zoomSlider.removeChangeListener ( changeListener );
                zoomSlider.setValue ( ninePatchEditor.getZoom () );
                zoomSlider.addChangeListener ( changeListener );
            }
        };
        ninePatchEditor.addZoomChangeListener ( zoomChangeListener );

        WebButton minZoom = new WebButton ( MIN_ICON );
        minZoom.setRolloverDecoratedOnly ( true );
        minZoom.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                zoomSlider.setValue ( NinePatchEditor.MIN_ZOOM );
            }
        } );

        WebButton maxZoom = new WebButton ( MAX_ICON );
        maxZoom.setRolloverDecoratedOnly ( true );
        maxZoom.addActionListener ( new ActionListener ()
        {
            public void actionPerformed ( ActionEvent e )
            {
                zoomSlider.setValue ( NinePatchEditor.MAX_ZOOM );
            }
        } );

        bottomToolBar.addToEnd ( minZoom );
        bottomToolBar.addToEnd ( zoomSlider );
        bottomToolBar.addToEnd ( maxZoom );
    }

    private static boolean aliasesInitialized = false;

    private static void initializeAliases ()
    {
        if ( !aliasesInitialized )
        {
            aliasesInitialized = true;
            XmlUtils.processAnnotations ( NinePatchInfo.class );
            XmlUtils.processAnnotations ( NinePatchInterval.class );
            XmlUtils.processAnnotations ( NinePatchIntervalType.class );
        }
    }

    public void openImage ( File file )
    {
        try
        {
            // Ignore same file opening
            if ( imageSrc != null && ( file.getAbsolutePath ().equals ( imageSrc ) ) )
            {
                return;
            }

            // Ignore unloadable images
            if ( file.isDirectory () || !ImageUtils.isImageLoadable ( file.getName () ) )
            {
                return;
            }

            // Load image (avoiding Toolkit cache)
            Image image = Toolkit.getDefaultToolkit ().createImage ( file.getAbsolutePath () );
            if ( image == null )
            {
                return;
            }

            // Check if changes save needed
            if ( !continueAfterSave () )
            {
                return;
            }

            // Loading image fully through ImageIcon MediaTracker
            BufferedImage bi = ImageUtils.getBufferedImage ( new ImageIcon ( image ) );

            // Open image file
            ninePatchEditor.setNinePatchImage ( bi );
            imageSrc = file.getAbsolutePath ();
            save.setEnabled ( true );

            // Show location in tree
            openFromTreeEnabled = false;
            fileTree.setSelectedFile ( file );
            openFromTreeEnabled = true;

            // Inform about changes
            fireStateChanged ();
        }
        catch ( Throwable e )
        {
            //
        }
    }

    public boolean continueAfterSave ()
    {
        if ( ninePatchEditor.isChanged () )
        {
            final String message = LanguageManager.get ( "weblaf.ex.npeditor.saveChanges.text" );
            final String title = LanguageManager.get ( "weblaf.ex.npeditor.saveChanges.title" );
            int confirm = WebOptionPane.showConfirmDialog ( NinePatchEditorPanel.this, message, title, WebOptionPane.YES_NO_CANCEL_OPTION,
                    WebOptionPane.QUESTION_MESSAGE );

            if ( confirm == WebOptionPane.YES_OPTION )
            {
                // Save changes before open
                if ( save.isEnabled () )
                {
                    save.doClick ();
                }
                else
                {
                    saveAs.doClick ();
                }

                // Save operation cancelled or failed
                if ( ninePatchEditor.isChanged () )
                {
                    return false;
                }
            }
            else if ( confirm == WebOptionPane.CANCEL_OPTION )
            {
                // Cancel open
                return false;
            }
        }
        return true;
    }

    private WebPanel createPreviewPanel ()
    {
        previewPanel = new WebPanel ()
        {
            public Dimension getPreferredSize ()
            {
                Dimension ps = super.getPreferredSize ();
                ps.width = Math.max ( 230, ps.width );
                ps.height = 400;
                return ps;
            }
        };
        previewPanel.setLayout ( new TableLayout ( new double[][]{ { TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL },
                { TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL } } ) );
        previewPanel.setPainter ( abp );

        preview = new WebLabel ( "", WebLabel.CENTER );
        final ResizablePanel resizablePanel = new ResizablePanel ( preview );
        previewPanel.add ( resizablePanel, "1,1" );

        updatePreviews ();
        getNinePatchEditor ().addChangeListener ( new ChangeListener ()
        {
            public void stateChanged ( ChangeEvent e )
            {
                updatePreviews ();
            }
        } );

        return new WebPanel ( previewPanel );
    }

    private String parseToMultilineHtml ( String text )
    {
        return "<html><center>" + text.replaceAll ( ";", "<br>" ).replaceAll ( "\\\\n", "<br>" ) +
                "</center></html>";
    }

    private void updatePreviews ()
    {
        NinePatchIconPainter bp = new NinePatchIconPainter ( getNinePatchEditor ().getNinePatchIcon () );
        preview.setPainter ( bp );
        previewPanel.revalidate ();
    }

    private void saveImage ( File imageFile ) throws IOException
    {
        // Checking file name
        String fullName = getSaveFileName ( imageFile );
        imageFile = new File ( imageFile.getParent (), fullName );
        imageSrc = imageFile.getAbsolutePath ();

        // Saving image
        BufferedImage image = ninePatchEditor.getNinePatchImage ();
        ImageIO.write ( image, "png", imageFile );

        // Nullify changes
        ninePatchEditor.setChanged ( false );

        // Inform about changes
        fireStateChanged ();
    }

    private String getSaveFileName ( File imageFile )
    {
        // Adding .9 to file name if it doesn't exist
        String format = ".png";
        String subFormat = ".9";
        String fullName = imageFile.getName ();
        if ( fullName.endsWith ( format ) )
        {
            String name = fullName.substring ( 0, fullName.lastIndexOf ( format ) );
            if ( !name.endsWith ( subFormat ) )
            {
                fullName = name + subFormat + format;
            }
        }
        else if ( fullName.endsWith ( subFormat ) )
        {
            fullName = fullName + format;
        }
        else
        {
            int dot = fullName.lastIndexOf ( "." );
            if ( dot != -1 )
            {
                fullName = fullName.substring ( 0, dot );
            }
            fullName = fullName + subFormat + format;
        }
        return fullName;
    }

    public NinePatchEditor getNinePatchEditor ()
    {
        return ninePatchEditor;
    }

    public String getImageSrc ()
    {
        return imageSrc;
    }

    public void setNinePatchImage ( ImageIcon imageIcon )
    {
        setNinePatchImage ( imageIcon.getImage () );
    }

    public void setNinePatchImage ( Image image )
    {
        setNinePatchImage ( ImageUtils.getBufferedImage ( image ) );
    }

    public void setNinePatchImage ( BufferedImage ninePatchImage )
    {
        // Check if changes save needed
        if ( !continueAfterSave () )
        {
            return;
        }

        // Open image file
        ninePatchEditor.setNinePatchImage ( ninePatchImage );
        imageSrc = null;
        save.setEnabled ( true );

        // Inform about changes
        fireStateChanged ();
    }

    public BufferedImage getNinePatchImage ()
    {
        return ninePatchEditor.getNinePatchImage ();
    }

    public List<ChangeListener> getChangeListeners ()
    {
        return changeListeners;
    }

    public void addChangeListener ( ChangeListener changeListener )
    {
        changeListeners.add ( changeListener );
    }

    public void removeChangeListener ( ChangeListener changeListener )
    {
        changeListeners.add ( changeListener );
    }

    private void fireStateChanged ()
    {
        ChangeEvent changeEvent = new ChangeEvent ( NinePatchEditorPanel.this );
        for ( ChangeListener listener : CollectionUtils.copy ( changeListeners ) )
        {
            listener.stateChanged ( changeEvent );
        }
    }
}