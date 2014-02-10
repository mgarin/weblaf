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

package com.alee.managers.style.editor;

import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.painter.DashedBorderPainter;
import com.alee.extended.painter.InnerShadePainter;
import com.alee.extended.panel.BorderPanel;
import com.alee.extended.panel.CenterPanel;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.WebButtonGroup;
import com.alee.extended.statusbar.WebMemoryBar;
import com.alee.extended.statusbar.WebStatusBar;
import com.alee.extended.window.PopOverDirection;
import com.alee.extended.window.WebPopOver;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.colorchooser.WebColorChooserPanel;
import com.alee.laf.label.WebLabel;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.scroll.WebScrollBar;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.splitpane.WebSplitPane;
import com.alee.laf.text.WebTextField;
import com.alee.laf.toolbar.ToolbarStyle;
import com.alee.laf.toolbar.WebToolBar;
import com.alee.managers.glasspane.GlassPaneManager;
import com.alee.managers.glasspane.WebGlassPane;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.style.SupportedComponent;
import com.alee.managers.style.data.SkinInfo;
import com.alee.managers.style.skin.CustomSkin;
import com.alee.managers.style.skin.web.WebSkin;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.utils.*;
import com.alee.utils.swing.DocumentChangeListener;
import com.alee.utils.swing.IntTextDocument;
import com.alee.utils.swing.WebTimer;
import com.alee.utils.xml.ColorConverter;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.RUndoManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class StyleEditor extends WebFrame
{
    /**
     * todo 1. Translate editor
     */

    private static final ImageIcon info = new ImageIcon ( StyleEditor.class.getResource ( "icons/status/info.png" ) );
    private static final ImageIcon ok = new ImageIcon ( StyleEditor.class.getResource ( "icons/status/ok.png" ) );
    private static final ImageIcon warn = new ImageIcon ( StyleEditor.class.getResource ( "icons/status/warn.png" ) );
    private static final ImageIcon error = new ImageIcon ( StyleEditor.class.getResource ( "icons/status/error.png" ) );

    private static final BufferedImage magnifier =
            ImageUtils.getBufferedImage ( new ImageIcon ( StyleEditor.class.getResource ( "icons/editor/magnifierImage.png" ) ) );

    private final DashedBorderPainter boundsPainter;
    private final InnerShadePainter viewPainter;

    private WebToolBar toolBar;
    private WebPanel container;
    private WebSplitPane split;
    private WebPanel componentViewer;
    private RSyntaxTextArea xmlEditor;
    private RUndoManager xmlEditorHistory;
    private RTextScrollPane xmlEditorScroll;
    private WebStatusBar statusbar;

    private WebLabel statusMessage;
    private final List<JComponent> previewComponents = new ArrayList<JComponent> ();

    private String lastAppliedXml = null;
    private int updateDelay = 50;
    private int zoomFactor = 4;
    private ComponentOrientation orientation = WebLookAndFeel.getOrientation ();
    private boolean enabled = true;

    public StyleEditor ()
    {
        super ( "WebLaF skin editor" );
        setIconImages ( WebLookAndFeel.getImages () );

        boundsPainter = new DashedBorderPainter ( new float[]{ 2f, 3f } );
        boundsPainter.setWidth ( 1 );
        boundsPainter.setRound ( 0 );
        boundsPainter.setColor ( null );
        viewPainter = new InnerShadePainter ( 10, 5, 0.75f );

        initializeContainer ();
        initializeToolBar ();
        initializeStatusBar ();
        initializeViewer ();
        initializeEditor ();

        setDefaultCloseOperation ( WindowConstants.EXIT_ON_CLOSE );
        setSize ( 1000, 650 );
        setLocationRelativeTo ( null );
    }

    private void initializeToolBar ()
    {
        toolBar = new WebToolBar ( WebToolBar.HORIZONTAL );
        toolBar.setToolbarStyle ( ToolbarStyle.attached );
        toolBar.setMargin ( 4 );
        toolBar.setSpacing ( 4 );
        toolBar.setFloatable ( false );

        final ImageIcon magnifierIcon = new ImageIcon ( StyleEditor.class.getResource ( "icons/editor/magnifier.png" ) );
        final WebToggleButton magnifierButton = new WebToggleButton ( "Magnifier", magnifierIcon );
        TooltipManager.setTooltip ( magnifierButton, magnifierIcon, "Show/hide magnifier tool" );
        magnifierButton.addHotkey ( Hotkey.ALT_Q );
        magnifierButton.setRound ( 0 );
        magnifierButton.setFocusable ( false );
        initializeMagnifier ( magnifierButton );
        final WebButton zoomFactorButton = new WebButton ( "4x" );
        zoomFactorButton.setRound ( 0 );
        zoomFactorButton.setFocusable ( false );
        zoomFactorButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final WebPopupMenu menu = new WebPopupMenu ();
                for ( int i = 2; i <= 10; i++ )
                {
                    final int factor = i;
                    final JMenuItem menuItem = new WebMenuItem ( i + "x zoom" );
                    menuItem.addActionListener ( new ActionListener ()
                    {
                        @Override
                        public void actionPerformed ( final ActionEvent e )
                        {
                            zoomFactor = factor;
                            zoomFactorButton.setText ( factor + "x" );
                        }
                    } );
                    menu.add ( menuItem );
                }
                menu.showBelowMiddle ( zoomFactorButton );
            }
        } );
        toolBar.add ( new WebButtonGroup ( magnifierButton, zoomFactorButton ) );

        final ImageIcon boundsIcon = new ImageIcon ( StyleEditor.class.getResource ( "icons/editor/bounds.png" ) );
        final WebToggleButton boundsButton = new WebToggleButton ( "Bounds", boundsIcon );
        TooltipManager.setTooltip ( boundsButton, boundsIcon, "Show/hide component bounds" );
        boundsButton.addHotkey ( Hotkey.ALT_W );
        boundsButton.setRound ( 0 );
        boundsButton.setFocusable ( false );
        boundsButton.setSelected ( boundsPainter.getColor () != null );
        boundsButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                boundsPainter.setColor ( boundsButton.isSelected () ? Color.GRAY : null );
            }
        } );
        toolBar.add ( boundsButton );

        final ImageIcon disabledIcon = new ImageIcon ( StyleEditor.class.getResource ( "icons/editor/disabled.png" ) );
        final WebToggleButton disabledButton = new WebToggleButton ( "Disabled", disabledIcon );
        TooltipManager.setTooltip ( disabledButton, disabledIcon, "Disable/enable components" );
        disabledButton.addHotkey ( Hotkey.ALT_D );
        disabledButton.setRound ( 0 );
        disabledButton.setFocusable ( false );
        disabledButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                enabled = !enabled;

                // Applying enabled state to separate components as they might not be visible on panel
                for ( final JComponent component : previewComponents )
                {
                    SwingUtils.setEnabledRecursively ( component, enabled );
                }
            }
        } );
        toolBar.add ( disabledButton );

        final ImageIcon orientationIcon = new ImageIcon ( StyleEditor.class.getResource ( "icons/editor/orientation.png" ) );
        final WebToggleButton orientationButton = new WebToggleButton ( "RTL orientation", orientationIcon );
        TooltipManager.setTooltip ( orientationButton, orientationIcon, "Change components orientation" );
        orientationButton.addHotkey ( Hotkey.ALT_R );
        orientationButton.setRound ( 0 );
        orientationButton.setFocusable ( false );
        orientationButton.setSelected ( !orientation.isLeftToRight () );
        orientationButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                orientation = orientation.isLeftToRight () ? ComponentOrientation.RIGHT_TO_LEFT : ComponentOrientation.LEFT_TO_RIGHT;

                // Applying orientation to whole panel first
                componentViewer.applyComponentOrientation ( orientation );

                // Applying orientation to separate components as they might not be visible on panel
                for ( final JComponent component : previewComponents )
                {
                    component.applyComponentOrientation ( orientation );
                }
            }
        } );
        toolBar.add ( orientationButton );

        //

        final ImageIcon updateIcon = new ImageIcon ( StyleEditor.class.getResource ( "icons/editor/update.png" ) );
        final WebLabel delayLabel = new WebLabel ( "Skin update delay:", updateIcon ).setMargin ( 4 );
        final WebTextField delayField = new WebTextField ( new IntTextDocument (), "" + updateDelay, 3 );
        delayField.setHorizontalAlignment ( WebTextField.CENTER );
        delayField.getDocument ().addDocumentListener ( new DocumentChangeListener ()
        {
            @Override
            public void documentChanged ( final DocumentEvent e )
            {
                try
                {
                    updateDelay = Integer.parseInt ( delayField.getText () );
                    if ( updateDelay < 0 )
                    {
                        updateDelay = 0;
                    }
                }
                catch ( final Throwable ex )
                {
                    // Ignore exceptions
                }
            }
        } );
        final WebLabel msLabel = new WebLabel ( "ms" ).setMargin ( 4 );
        toolBar.addToEnd ( new GroupPanel ( 4, delayLabel, delayField, msLabel ) );

        container.add ( toolBar, BorderLayout.NORTH );
    }

    private void initializeContainer ()
    {
        container = new WebPanel ();
        getContentPane ().add ( container, BorderLayout.CENTER );

        split = new WebSplitPane ( WebSplitPane.HORIZONTAL_SPLIT, true );
        split.setDividerLocation ( 300 );
        container.add ( new BorderPanel ( split, 7 ), BorderLayout.CENTER );
    }

    private void initializeStatusBar ()
    {
        statusbar = new WebStatusBar ();

        statusMessage = new WebLabel ( "Edit XML at the right side and see UI changes at the left side!", info ).setMargin ( 4 );
        statusbar.add ( statusMessage );
        statusbar.addToEnd ( new WebMemoryBar ().setPreferredWidth ( 200 ) );

        container.add ( statusbar, BorderLayout.SOUTH );
    }

    private void initializeViewer ()
    {
        componentViewer = new WebPanel ( new VerticalFlowLayout ( VerticalFlowLayout.TOP, 0, 15, true, false ) );
        componentViewer.setMargin ( 5 );
        split.setLeftComponent ( new WebScrollPane ( componentViewer, false, false ) );

        //

        //        final WebLabel label = new WebLabel ( "Simple Swing label" );
        //        addViewComponent ( "JLabel", label, label, true );

        //

        final WebScrollBar hsb = new WebScrollBar ( WebScrollBar.HORIZONTAL, 45, 10, 0, 100 );
        addViewComponent ( "JSCrollBar (horizontal)", hsb, hsb, false );

        //

        final WebScrollBar vsb = new WebScrollBar ( WebScrollBar.VERTICAL, 45, 10, 0, 100 ).setPreferredHeight ( 100 );
        addViewComponent ( "JSCrollBar (vertical)", vsb, vsb, true );

        //

        final WebPopupMenu popupMenu = new WebPopupMenu ();
        popupMenu.add ( new WebMenuItem ( "Item 1" ) );
        popupMenu.add ( new WebMenuItem ( "Item 2" ) );
        popupMenu.add ( new WebMenuItem ( "Item 3" ) );
        popupMenu.addSeparator ();
        popupMenu.add ( new WebMenuItem ( "Item 4", Hotkey.ALT_F4 ) );

        final WebButton popupButton = new WebButton ( "Show popup menu", new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                popupMenu.showBelowMiddle ( ( WebButton ) e.getSource () );
            }
        } );

        addViewComponent ( "JPopupMenu", popupButton, popupMenu, false );
    }

    private void addViewComponent ( final String title, final JComponent displayedView, final JComponent view, final boolean center )
    {
        final SupportedComponent type = SupportedComponent.getComponentTypeByUIClassID ( view.getUIClassID () );
        final WebLabel titleLabel = new WebLabel ( title, type.getIcon () ).setMargin ( 0, 7, 3, 0 );
        final WebPanel boundsPanel = new WebPanel ( boundsPainter, displayedView );
        final WebPanel viewPanel = new WebPanel ( viewPainter, center ? new CenterPanel ( boundsPanel ) : boundsPanel );
        final WebPanel container = new WebPanel ( new BorderLayout ( 0, 0 ) );
        container.add ( titleLabel, BorderLayout.NORTH );
        container.add ( viewPanel, BorderLayout.CENTER );
        componentViewer.add ( container );

        titleLabel.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                viewPanel.setVisible ( !viewPanel.isVisible () );
                componentViewer.revalidate ();
                componentViewer.repaint ();
            }
        } );

        previewComponents.add ( view );
    }

    private void initializeEditor ()
    {
        xmlEditor = new RSyntaxTextArea ()
        {
            @Override
            protected RUndoManager createUndoManager ()
            {
                xmlEditorHistory = super.createUndoManager ();
                xmlEditorHistory.setLimit ( 5 );
                return xmlEditorHistory;
            }
        };
        xmlEditor.setSyntaxEditingStyle ( SyntaxConstants.SYNTAX_STYLE_XML );
        xmlEditor.setMargin ( new Insets ( 0, 5, 0, 0 ) );
        xmlEditor.setAntiAliasingEnabled ( true );
        xmlEditor.setUseFocusableTips ( true );
        xmlEditor.setTabSize ( 4 );
        xmlEditor.setCodeFoldingEnabled ( true );
        xmlEditor.setPaintTabLines ( false );
        xmlEditor.setWhitespaceVisible ( false );
        xmlEditor.setEOLMarkersVisible ( false );

        xmlEditor.setText ( FileUtils.readToString ( WebSkin.class, "resources/WebSkin.xml" ) );
        xmlEditor.setCaretPosition ( 0 );
        xmlEditorHistory.discardAllEdits ();

        xmlEditor.setHyperlinksEnabled ( true );
        xmlEditor.setLinkGenerator ( new CodeLinkGenerator () );

        xmlEditor.getDocument ().addDocumentListener ( new DocumentChangeListener ()
        {
            private final WebTimer updateTimer = new WebTimer ( updateDelay, new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    final String newXml = xmlEditor.getText ();
                    if ( lastAppliedXml == null || !lastAppliedXml.equals ( newXml ) )
                    {
                        try
                        {
                            final SkinInfo skinInfo = XmlUtils.fromXML ( newXml );
                            final CustomSkin customSkin = new CustomSkin ( skinInfo );
                            applySkin ( customSkin );
                            lastAppliedXml = newXml;
                        }
                        catch ( final Throwable ex )
                        {
                            System.err.println ( "Unable to update skin: " + ex.getMessage () );
                        }
                    }
                }
            } ).setRepeats ( false );

            @Override
            public void documentChanged ( final DocumentEvent e )
            {
                updateTimer.restart ( updateDelay );
            }
        } );

        xmlEditorScroll = new RTextScrollPane ( xmlEditor );
        xmlEditorScroll.setVerticalScrollBarPolicy ( WebScrollPane.VERTICAL_SCROLLBAR_ALWAYS );

        try
        {
            Theme.load ( StyleEditor.class.getResourceAsStream ( "resources/editorTheme.xml" ) ).apply ( xmlEditor );
        }
        catch ( final IOException e )
        {
            e.printStackTrace ();
        }

        split.setRightComponent ( xmlEditorScroll );
    }

    private void applySkin ( final CustomSkin customSkin )
    {
        // Applying updated skin
        int updated = 0;
        int total = 0;
        for ( final JComponent component : previewComponents )
        {
            final boolean applied = customSkin.apply ( component );
            component.revalidate ();
            component.repaint ();
            updated += applied ? 1 : 0;
            total++;
        }

        // Updating preview components layout
        componentViewer.revalidate ();

        // Updating status bar text
        if ( updated == total )
        {
            statusMessage.setIcon ( ok );
            statusMessage.setText ( "All components updated properly" );
        }
        else if ( updated == 0 )
        {
            statusMessage.setIcon ( error );
            statusMessage.setText ( "Failed to update all components" );
        }
        else
        {
            statusMessage.setIcon ( warn );
            statusMessage.setText ( "Failed to update some components" );
        }
    }

    private class CodeLinkGenerator implements LinkGenerator
    {
        private final String contentStartTag = ">";
        private final String contentEndTag = "<";
        private final ColorConverter colorConverter = new ColorConverter ();

        public CodeLinkGenerator ()
        {
            super ();
        }

        @Override
        public LinkGeneratorResult isLinkAtOffset ( final RSyntaxTextArea source, final int pos )
        {
            final String code = source.getText ();
            final int wordStart = getContentStart ( code, pos );
            final int wordEnd = getContentEnd ( code, pos );
            final String word = code.substring ( wordStart, wordEnd );

            try
            {
                final Color color = ( Color ) colorConverter.fromString ( word );
                return color != null ? new LinkGeneratorResult ()
                {
                    @Override
                    public HyperlinkEvent execute ()
                    {
                        try
                        {
                            final WebPopOver colorChooser = new WebPopOver ( StyleEditor.this );
                            colorChooser.setCloseOnFocusLoss ( true );
                            colorChooser.setBorderColor ( Color.GRAY );

                            final WebColorChooserPanel colorChooserPanel = new WebColorChooserPanel ( false );
                            colorChooserPanel.setColor ( color );
                            colorChooserPanel.addChangeListener ( new ChangeListener ()
                            {
                                private int length = wordEnd - wordStart;

                                @Override
                                public void stateChanged ( final ChangeEvent e )
                                {
                                    final Color newColor = colorChooserPanel.getColor ();
                                    if ( newColor != null && !newColor.equals ( color ) )
                                    {
                                        final String colorString = colorConverter.toString ( newColor );
                                        source.replaceRange ( colorString, wordStart, wordStart + length );
                                        length = colorString.length ();
                                    }
                                }
                            } );
                            colorChooser.add ( colorChooserPanel );

                            final Rectangle wb = source.getUI ().modelToView ( source, ( wordStart + wordEnd ) / 2 );
                            colorChooser.show ( source, wb.x, wb.y, wb.width, wb.height, PopOverDirection.down );

                            return new HyperlinkEvent ( this, HyperlinkEvent.EventType.EXITED, null );
                        }
                        catch ( final BadLocationException e )
                        {
                            e.printStackTrace ();
                            return null;
                        }
                    }

                    @Override
                    public int getSourceOffset ()
                    {
                        return wordStart;
                    }
                } : null;
            }
            catch ( final Throwable e )
            {
                return null;
            }
        }

        public int getContentStart ( final String text, final int location )
        {
            int wordStart = location;
            while ( wordStart > 0 && !text.substring ( wordStart - 1, wordStart ).equals ( contentStartTag ) )
            {
                wordStart--;
            }
            return wordStart;
        }

        public int getContentEnd ( final String text, final int location )
        {
            int wordEnd = location;
            while ( wordEnd < text.length () - 1 && !text.substring ( wordEnd, wordEnd + 1 ).equals ( contentEndTag ) )
            {
                wordEnd++;
            }
            return wordEnd;
        }
    }

    private void initializeMagnifier ( final WebToggleButton magnifierButton )
    {
        final WebGlassPane glassPane = GlassPaneManager.getGlassPane ( StyleEditor.this );
        final JComponent zoomProvider = SwingUtils.getRootPane ( StyleEditor.this ).getLayeredPane ();
        magnifierButton.addActionListener ( new ActionListener ()
        {
            private boolean visible = false;
            private AWTEventListener listener;
            private WebTimer forceUpdater;

            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                performAction ();
            }

            protected void performAction ()
            {
                if ( !visible )
                {
                    visible = true;

                    if ( forceUpdater == null || listener == null )
                    {
                        forceUpdater = new WebTimer ( 200, new ActionListener ()
                        {
                            @Override
                            public void actionPerformed ( final ActionEvent e )
                            {
                                updateMagnifier ();
                            }
                        } );
                        listener = new AWTEventListener ()
                        {
                            @Override
                            public void eventDispatched ( final AWTEvent event )
                            {
                                SwingUtilities.invokeLater ( new Runnable ()
                                {
                                    @Override
                                    public void run ()
                                    {
                                        if ( visible )
                                        {
                                            forceUpdater.restart ();
                                            updateMagnifier ();
                                        }
                                    }
                                } );
                            }
                        };
                    }
                    Toolkit.getDefaultToolkit ().addAWTEventListener ( listener, AWTEvent.MOUSE_MOTION_EVENT_MASK );
                    Toolkit.getDefaultToolkit ().addAWTEventListener ( listener, AWTEvent.MOUSE_WHEEL_EVENT_MASK );
                    Toolkit.getDefaultToolkit ().addAWTEventListener ( listener, AWTEvent.MOUSE_EVENT_MASK );
                    updateMagnifier ();

                    setCursor ( SystemUtils.getTransparentCursor () );
                }
                else
                {
                    visible = false;

                    Toolkit.getDefaultToolkit ().removeAWTEventListener ( listener );
                    forceUpdater.stop ();
                    hideMagnifier ();

                    setCursor ( Cursor.getDefaultCursor () );
                }
            }

            protected void updateMagnifier ()
            {
                final Point mp = MouseInfo.getPointerInfo ().getLocation ();
                final Rectangle gb = SwingUtils.getBoundsOnScreen ( glassPane );
                if ( gb.contains ( mp ) )
                {
                    final Point gp = gb.getLocation ();
                    final int mx = mp.x - gp.x - magnifier.getWidth () / 2;
                    final int my = mp.y - gp.y - magnifier.getHeight () / 2;

                    final int w = 162 / zoomFactor;
                    final BufferedImage image = ImageUtils.createCompatibleImage ( w, w, Transparency.TRANSLUCENT );
                    final Graphics2D g2d = image.createGraphics ();
                    g2d.translate ( -( mp.x - gp.x - w / 2 ), -( mp.y - gp.y - w / 2 ) );
                    zoomProvider.paintAll ( g2d );
                    g2d.dispose ();

                    final BufferedImage finalImage = ImageUtils.createCompatibleImage ( 220, 220, Transparency.TRANSLUCENT );
                    final Graphics2D g = finalImage.createGraphics ();
                    g.setClip ( new Ellipse2D.Double ( 29, 29, 162, 162 ) );
                    g.drawImage ( image, 29, 29, 162, 162, null );
                    g.setClip ( null );
                    g.drawImage ( magnifier, 0, 0, null );
                    g.dispose ();

                    glassPane.setPaintedImage ( finalImage, new Point ( mx, my ) );
                }
                else
                {
                    hideMagnifier ();
                }
            }

            protected void hideMagnifier ()
            {
                glassPane.setPaintedImage ( null, null );
            }
        } );
    }

    public static void main ( final String[] args )
    {
        WebLookAndFeel.install ();
        final StyleEditor styleEditor = new StyleEditor ();
        styleEditor.setVisible ( true );
    }
}