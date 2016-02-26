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

package com.alee.extended.style;

import com.alee.extended.window.PopOverDirection;
import com.alee.extended.window.WebPopOver;
import com.alee.laf.colorchooser.WebColorChooserPanel;
import com.alee.laf.list.WebList;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.slider.WebSlider;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.log.Log;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleableComponent;
import com.alee.managers.style.data.ComponentStyleConverter;
import com.alee.utils.CompareUtils;
import com.alee.utils.MathUtils;
import com.alee.utils.xml.ColorConverter;
import com.alee.utils.xml.InsetsConverter;
import com.thoughtworks.xstream.converters.basic.FloatConverter;
import net.htmlparser.jericho.*;
import org.fife.ui.rsyntaxtextarea.LinkGenerator;
import org.fife.ui.rsyntaxtextarea.LinkGeneratorResult;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author Mikle Garin
 */

public class CodeLinkGenerator implements LinkGenerator
{
    /**
     * Code constants.
     */
    private static final List<String> propertyNodes =
            Arrays.asList ( ComponentStyleConverter.COMPONENT_NODE, ComponentStyleConverter.UI_NODE, ComponentStyleConverter.PAINTER_NODE );
    private static final String trueString = "true";
    private static final String falseString = "false";
    private static final List<String> booleanContent = Arrays.asList ( trueString, falseString );
    private static final List<String> colorContent = Arrays.asList ( "foreground", "fg", "background", "bg", "color" );
    private static final List<String> insetsContent = Arrays.asList ( "insets", "margin" );
    private static final List<String> opacityContent = Arrays.asList ( "opacity", "transparency" );

    /**
     * Data converters.
     */
    private static final ColorConverter colorConverter = new ColorConverter ();
    private static final InsetsConverter insetsConverter = new InsetsConverter ();
    private static final FloatConverter floatConverter = new FloatConverter ();

    /**
     * Parent component.
     */
    protected final Component parentComponent;

    /**
     * Runtime variables.
     */
    protected Source src = null;
    protected String text = null;

    /**
     * Constructs new code link generator.
     *
     * @param parentComponent parent component
     */
    public CodeLinkGenerator ( final Component parentComponent )
    {
        super ();
        this.parentComponent = parentComponent;
    }

    @Override
    public LinkGeneratorResult isLinkAtOffset ( final RSyntaxTextArea source, final int pos )
    {
        final String code = source.getText ();
        if ( src == null || !CompareUtils.equals ( src, code ) )
        {
            text = code;
            src = new Source ( code );
            src.setLogger ( null );
            src.fullSequentialParse ();
        }

        final Element element = src.getEnclosingElement ( pos );
        if ( element == null )
        {
            return null;
        }

        final Element parent = element.getParentElement ();
        if ( parent == null )
        {
            return null;
        }

        final String name = element.getName ();
        final String parentName = parent.getName ();
        if ( ComponentStyleConverter.STYLE_NODE.equals ( name ) )
        {
            final Attributes attributes = element.getAttributes ();
            for ( final Attribute attribute : attributes )
            {
                if ( attribute.getBegin () < pos && pos < attribute.getEnd () )
                {
                    final String attributeName = attribute.getName ();
                    if ( attributeName.equals ( ComponentStyleConverter.COMPONENT_TYPE_ATTRIBUTE ) )
                    {
                        final Segment content = attribute.getValueSegment ();
                        final String type = element.getAttributeValue ( ComponentStyleConverter.COMPONENT_TYPE_ATTRIBUTE );
                        final StyleableComponent selectedType = StyleableComponent.valueOf ( type );

                        return new LinkGeneratorResult ()
                        {
                            @Override
                            public HyperlinkEvent execute ()
                            {
                                try
                                {
                                    final WebPopOver typeChooser = new WebPopOver ( parentComponent );
                                    typeChooser.setCloseOnFocusLoss ( true );
                                    typeChooser.setPadding ( 5, 0, 5, 0 );

                                    final List<StyleableComponent> types = StyleableComponent.list ();
                                    final WebList typesList = new WebList ( types );
                                    typesList.setOpaque ( false );
                                    typesList.setVisibleRowCount ( Math.min ( 10, types.size () ) );
                                    typesList.setSelectOnHover ( true );
                                    typesList.setSelectedValue ( selectedType );
                                    final Runnable commitChanges = new Runnable ()
                                    {
                                        @Override
                                        public void run ()
                                        {
                                            final String typeString = typesList.getSelectedValue ().toString ();
                                            source.replaceRange ( typeString, content.getBegin (), content.getEnd () );
                                            typeChooser.dispose ();
                                        }
                                    };
                                    typesList.addMouseListener ( new MouseAdapter ()
                                    {
                                        @Override
                                        public void mouseReleased ( final MouseEvent e )
                                        {
                                            commitChanges.run ();
                                        }
                                    } );
                                    typesList.addKeyListener ( new KeyAdapter ()
                                    {
                                        @Override
                                        public void keyReleased ( final KeyEvent e )
                                        {
                                            if ( Hotkey.ENTER.isKeyTriggered ( e ) )
                                            {
                                                commitChanges.run ();
                                            }
                                        }
                                    } );

                                    final WebScrollPane scrollPane = new WebScrollPane ( StyleId.scrollpanePopup, typesList );
                                    typeChooser.add ( scrollPane );

                                    final Rectangle wb =
                                            source.getUI ().modelToView ( source, ( content.getBegin () + content.getEnd () ) / 2 );
                                    typeChooser.show ( source, wb.x, wb.y, wb.width, wb.height, PopOverDirection.down );

                                    return new HyperlinkEvent ( this, HyperlinkEvent.EventType.EXITED, null );
                                }
                                catch ( final BadLocationException e )
                                {
                                    Log.error ( this, e );
                                    return null;
                                }
                            }

                            @Override
                            public int getSourceOffset ()
                            {
                                return content.getBegin ();
                            }
                        };
                    }
                }
            }
        }
        else if ( propertyNodes.contains ( parentName ) )
        {
            final Segment content = element.getContent ();
            final String contentString = content.toString ();
            if ( booleanContent.contains ( contentString ) )
            {
                return new LinkGeneratorResult ()
                {
                    @Override
                    public HyperlinkEvent execute ()
                    {
                        source.replaceRange ( contentString.equals ( trueString ) ? falseString : trueString, content.getBegin (),
                                content.getEnd () );
                        return new HyperlinkEvent ( this, HyperlinkEvent.EventType.EXITED, null );
                    }

                    @Override
                    public int getSourceOffset ()
                    {
                        return content.getBegin ();
                    }
                };
            }
            else
            {
                if ( CompareUtils.contains ( name.toLowerCase ( Locale.ROOT ), colorContent ) )
                {
                    final Color color = ( Color ) colorConverter.fromString ( contentString );
                    if ( color != null || contentString.equals ( ColorConverter.NULL_COLOR ) )
                    {
                        return new LinkGeneratorResult ()
                        {
                            @Override
                            public HyperlinkEvent execute ()
                            {
                                try
                                {
                                    final WebPopOver colorChooser = new WebPopOver ( parentComponent );
                                    colorChooser.setCloseOnFocusLoss ( true );

                                    final WebColorChooserPanel colorChooserPanel = new WebColorChooserPanel ( false );
                                    colorChooserPanel.setColor ( color != null ? color : Color.WHITE );
                                    colorChooserPanel.addChangeListener ( new ChangeListener ()
                                    {
                                        private int length = content.getEnd () - content.getBegin ();

                                        @Override
                                        public void stateChanged ( final ChangeEvent e )
                                        {
                                            final Color newColor = colorChooserPanel.getColor ();
                                            if ( color == null || newColor != null && !newColor.equals ( color ) )
                                            {
                                                final String colorString = colorConverter.toString ( newColor );
                                                source.replaceRange ( colorString, content.getBegin (), content.getBegin () + length );
                                                length = colorString.length ();
                                            }
                                        }
                                    } );
                                    colorChooser.add ( colorChooserPanel );

                                    final Rectangle wb =
                                            source.getUI ().modelToView ( source, ( content.getBegin () + content.getEnd () ) / 2 );
                                    colorChooser.show ( source, wb.x, wb.y, wb.width, wb.height, PopOverDirection.down );

                                    return new HyperlinkEvent ( this, HyperlinkEvent.EventType.EXITED, null );
                                }
                                catch ( final BadLocationException e )
                                {
                                    Log.error ( this, e );
                                    return null;
                                }
                            }

                            @Override
                            public int getSourceOffset ()
                            {
                                return content.getBegin ();
                            }
                        };
                    }
                }
                else if ( CompareUtils.contains ( name.toLowerCase ( Locale.ROOT ), opacityContent ) )
                {
                    final Float f = ( Float ) floatConverter.fromString ( contentString );
                    if ( f != null )
                    {
                        return new LinkGeneratorResult ()
                        {
                            @Override
                            public HyperlinkEvent execute ()
                            {
                                try
                                {
                                    final WebPopOver opacityChooser = new WebPopOver ( parentComponent );
                                    opacityChooser.setCloseOnFocusLoss ( true );

                                    final int value = MathUtils.limit ( 0, Math.round ( 1000 * f ), 1000 );
                                    final WebSlider slider = new WebSlider ( WebSlider.HORIZONTAL, 0, 1000, value );
                                    slider.setPadding ( 10 );
                                    slider.setPaintTicks ( true );
                                    slider.setSnapToTicks ( true );
                                    slider.setPaintLabels ( false );
                                    slider.setMajorTickSpacing ( 50 );
                                    slider.setMinorTickSpacing ( 10 );
                                    slider.setPreferredWidth ( 500 );
                                    slider.addChangeListener ( new ChangeListener ()
                                    {
                                        private int length = content.getEnd () - content.getBegin ();

                                        @Override
                                        public void stateChanged ( final ChangeEvent e )
                                        {
                                            final String floatString = floatConverter.toString ( ( float ) slider.getValue () / 1000 );
                                            source.replaceRange ( floatString, content.getBegin (), content.getBegin () + length );
                                            length = floatString.length ();
                                        }
                                    } );
                                    opacityChooser.add ( slider );

                                    final int pos = ( content.getBegin () + content.getEnd () ) / 2;
                                    final Rectangle wb = source.getUI ().modelToView ( source, pos );
                                    opacityChooser.show ( source, wb.x, wb.y, wb.width, wb.height, PopOverDirection.down );

                                    return new HyperlinkEvent ( this, HyperlinkEvent.EventType.EXITED, null );
                                }
                                catch ( final BadLocationException e )
                                {
                                    Log.error ( this, e );
                                    return null;
                                }
                            }

                            @Override
                            public int getSourceOffset ()
                            {
                                return content.getBegin ();
                            }
                        };
                    }
                }
                else if ( CompareUtils.contains ( name.toLowerCase ( Locale.ROOT ), insetsContent ) )
                {
                    final Insets insets = ( Insets ) insetsConverter.fromString ( contentString );
                    if ( insets != null )
                    {
                        // todo Margin editor
                        return null;
                    }
                }
            }
        }

        // todo Parse property types properly later (so far types are "guessed" from property names)
        // todo This sounds like a small IDE already, but it is better than nothing
        //        if ( parentName.equals ( ComponentStyleConverter.COMPONENT_NODE ) )
        //        {
        //
        //        }
        //        else if ( parentName.equals ( ComponentStyleConverter.UI_NODE ) )
        //        {
        //
        //        }
        //        else if ( parentName.equals ( ComponentStyleConverter.PAINTER_NODE ) )
        //        {
        //
        //        }

        return null;
    }
}