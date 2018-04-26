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

import com.alee.api.clone.Clone;
import com.alee.extended.layout.TableLayout;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.background.AlphaLayerBackground;
import com.alee.utils.*;
import com.alee.utils.ninepatch.NinePatchIcon;
import com.alee.utils.ninepatch.NinePatchInterval;
import com.alee.utils.swing.MouseEventType;
import com.alee.utils.swing.extensions.SizeMethodsImpl;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This editor is not based on the Android dev kit editor - {@link NinePatchEditor} is more advanced and user-friendly.
 * This editor allows quick visual nine-patch editing, nine-patch information copying and also creation of new nine-patch files based on
 * any image file that could be loaded by WebLookAndFeel library.
 *
 * @author Mikle Garin
 * @see <a href="http://developer.android.com/guide/developing/tools/draw9patch.html">Android dev kit editor</a>
 * @see NinePatchEditorPanel
 * @see NinePatchEditorFrame
 */
public class NinePatchEditor extends WebPanel
{
    public static final Color STRETCH_GUIDELINES_COLOR = new Color ( 60, 150, 0 );
    public static final Color STRETCH_COLOR = new Color ( 80, 150, 0, 100 );
    public static final Color CONTENT_GUIDELINES_COLOR = new Color ( 80, 80, 80, 200 );
    public static final Color CONTENT_COLOR = new Color ( 90, 90, 255, 100 );
    public static final Color METRICS_TOP = Color.WHITE;
    public static final Color METRICS_BOTTOM = new Color ( 229, 233, 238 );
    public static final int RULER_LENGTH = 20;
    public static final int ADDITIONAL_SPACE = 80;
    public static final int SNAP = 3;
    public static final int MIN_ZOOM = 1;
    public static final int MAX_ZOOM = 32;
    public static final Stroke GUIDELINE_STROKE =
            new BasicStroke ( 1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1, new float[]{ 4, 4 }, 0 );

    /**
     * Alpha background.
     */
    private final AlphaLayerBackground ALPHA_LAYER_BACKGROUND = new AlphaLayerBackground ();

    private final List<ChangeListener> changeListeners = new ArrayList<ChangeListener> ( 1 );
    private final List<ZoomChangeListener> zoomChangeListeners = new ArrayList<ZoomChangeListener> ( 1 );

    private final List<NinePatchInfo> history = new ArrayList<NinePatchInfo> ();
    private int historyState = -1;

    private BufferedImage ninePatchImage;
    private NinePatchIcon ninePatchIcon;

    private boolean showGuideSpacing = true;
    private boolean showRuler = true;
    private boolean fillStretchAreas = true;
    private boolean fillContentArea = true;
    private boolean showRulerCursorPosition = true;
    private boolean showAreaCursorPosition = false;
    private int zoom = 5;

    private final WebScrollPane view;

    private boolean changed = false;
    private boolean someDragged = false;

    public NinePatchEditor ()
    {
        super ();

        ninePatchImage = null;
        ninePatchIcon = null;

        SwingUtils.setOrientation ( this );
        setOpaque ( false );
        setFocusable ( true );
        setLayout ( new TableLayout ( new double[][]{ { RULER_LENGTH, TableLayout.PREFERRED, TableLayout.FILL },
                { RULER_LENGTH, TableLayout.PREFERRED, TableLayout.FILL } } ) );

        setFont ( new JLabel ().getFont ().deriveFont ( 10f ) );

        view = new WebScrollPane ( StyleId.scrollpaneTransparentHovering, this );

        final NinePatchEditorMouseAdapter mouseAdapter = new NinePatchEditorMouseAdapter ();
        addMouseListener ( mouseAdapter );
        addMouseMotionListener ( mouseAdapter );
        addMouseWheelListener ( mouseAdapter );
    }

    public WebScrollPane getView ()
    {
        return view;
    }

    public boolean isSomeDragged ()
    {
        return someDragged;
    }

    public boolean isChanged ()
    {
        return changed;
    }

    public void setChanged ( final boolean changed )
    {
        this.changed = changed;
    }

    public int getZoom ()
    {
        return zoom;
    }

    public void setZoom ( final int zoom )
    {
        this.zoom = Math.max ( MIN_ZOOM, Math.min ( zoom, MAX_ZOOM ) );
        revalidate ();
        repaint ();
        fireZoomChanged ();
    }

    public boolean isFillStretchAreas ()
    {
        return fillStretchAreas;
    }

    public void setFillStretchAreas ( final boolean fillStretchAreas )
    {
        this.fillStretchAreas = fillStretchAreas;
        repaint ();
    }

    public boolean isFillContentArea ()
    {
        return fillContentArea;
    }

    public void setFillContentArea ( final boolean fillContentArea )
    {
        this.fillContentArea = fillContentArea;
        repaint ();
    }

    public boolean isShowRulerCursorPosition ()
    {
        return showRulerCursorPosition;
    }

    public void setShowRulerCursorPosition ( final boolean showRulerCursorPosition )
    {
        this.showRulerCursorPosition = showRulerCursorPosition;
        repaint ();
    }

    public boolean isShowAreaCursorPosition ()
    {
        return showAreaCursorPosition;
    }

    public void setShowAreaCursorPosition ( final boolean showAreaCursorPosition )
    {
        this.showAreaCursorPosition = showAreaCursorPosition;
        repaint ();
    }

    public boolean isShowGuideSpacing ()
    {
        return showGuideSpacing;
    }

    public void setShowGuideSpacing ( final boolean showGuideSpacing )
    {
        this.showGuideSpacing = showGuideSpacing;
        repaint ();
    }

    public boolean isShowRuler ()
    {
        return showRuler;
    }

    public void setShowRuler ( final boolean showRuler )
    {
        this.showRuler = showRuler;
        repaint ();
    }

    public BufferedImage getRawImage ()
    {
        return ninePatchIcon != null ? ninePatchIcon.getRawImage () : null;
    }

    public BufferedImage getNinePatchImage ()
    {
        if ( ninePatchIcon != null )
        {
            assembleImage ();
            return ninePatchImage;
        }
        else
        {
            return null;
        }
    }

    public void setNinePatchImage ( final BufferedImage ninePatchImage )
    {
        // Ignore action if something is being dragged
        if ( isSomeDragged () )
        {
            return;
        }

        // Create new NinePatchIcon from image file
        disassembleImage ( ninePatchImage );

        // Updates shown image
        validateIcon ();
        revalidate ();
        repaint ();

        // Add initial state to history
        clearHistory ();
        saveHistoryState ();

        // Reset any changes done
        this.changed = false;

        // Inform about changes
        fireStateChanged ();
    }

    public void setNinePatchIcon ( final NinePatchIcon ninePatchIcon )
    {
        // Ignore action if something is being dragged
        if ( isSomeDragged () )
        {
            return;
        }

        // Set new NinePatchIcon
        this.ninePatchIcon = ninePatchIcon;

        // Updates shown image
        validateIcon ();
        revalidate ();
        repaint ();

        // Add initial state to history
        clearHistory ();
        saveHistoryState ();

        // Reset any changes done
        this.changed = false;

        // Inform about changes
        fireStateChanged ();
    }

    public void setNinePatchInfo ( final NinePatchInfo ninePatchInfo )
    {
        // Some cases in which we ignore this action
        if ( this.ninePatchIcon == null || ninePatchInfo == null || isSomeDragged () ||
                getNinePatchInfo ().equals ( ninePatchInfo ) )
        {
            return;
        }

        // Revalidate NinePatchInfo data
        ninePatchIcon.setVerticalStretch ( ninePatchInfo.getVerticalStretch () );
        verifyVerticalStretchAreas ();
        ninePatchIcon.setHorizontalStretch ( ninePatchInfo.getHorizontalStretch () );
        verifyHorizontalStretchAreas ();
        ninePatchIcon.setMargin ( ninePatchInfo.getMargin () );
        verifyMargin ();

        // Updates shown image
        validateIcon ();
        revalidate ();
        repaint ();

        // Add state to history
        saveHistoryState ();
    }

    public NinePatchInfo getNinePatchInfo ()
    {
        if ( ninePatchIcon == null )
        {
            return null;
        }

        final NinePatchInfo ninePatchInfo = new NinePatchInfo ();
        ninePatchInfo.setImageSize ( ninePatchIcon.getRealImageSize () );
        ninePatchInfo.setHorizontalStretch ( ninePatchIcon.getHorizontalStretch () );
        ninePatchInfo.setVerticalStretch ( ninePatchIcon.getVerticalStretch () );
        ninePatchInfo.setMargin ( ninePatchIcon.getMargin () );
        return ninePatchInfo;
    }

    public NinePatchIcon getNinePatchIcon ()
    {
        return ninePatchIcon;
    }

    public void undo ()
    {
        if ( !isSomeDragged () && historyState > 0 )
        {
            historyState = historyState - 1;
            restoreHistoryState ( history.get ( historyState ) );
        }
    }

    public void redo ()
    {
        if ( !isSomeDragged () && historyState < history.size () - 1 )
        {
            historyState = historyState + 1;
            restoreHistoryState ( history.get ( historyState ) );
        }
    }

    private void restoreHistoryState ( final NinePatchInfo state )
    {
        // Ignore action if history state doesn't fit current image
        if ( !state.getImageSize ().equals ( ninePatchIcon.getRealImageSize () ) )
        {
            return;
        }

        // Update NinePatchIcon data
        ninePatchIcon.setMargin ( Clone.basic ().clone ( state.getMargin () ) );
        ninePatchIcon.setHorizontalStretch ( Clone.deep ().clone ( state.getHorizontalStretch () ) );
        ninePatchIcon.setVerticalStretch ( Clone.deep ().clone ( state.getVerticalStretch () ) );

        // Updates shown image
        validateIcon ();
        revalidate ();
        repaint ();
        fireStateChanged ();

        changed = historyState > 0;
    }

    private void saveHistoryState ()
    {
        if ( ninePatchIcon != null )
        {
            // Removing all undone states starting from end till the current state
            for ( int i = history.size () - 1; i > historyState; i-- )
            {
                history.remove ( i );
            }

            // Adding new state
            final NinePatchInfo info = new NinePatchInfo ();
            info.setImageSize ( ninePatchIcon.getRealImageSize () );
            info.setMargin ( Clone.basic ().clone ( ninePatchIcon.getMargin () ) );
            info.setHorizontalStretch ( Clone.deep ().clone ( ninePatchIcon.getHorizontalStretch () ) );
            info.setVerticalStretch ( Clone.deep ().clone ( ninePatchIcon.getVerticalStretch () ) );
            history.add ( info );
            historyState = history.size () - 1;

            // Updating changes state
            changed = true;
        }
    }

    private void clearHistory ()
    {
        history.clear ();
        historyState = -1;
    }

    private class NinePatchEditorMouseAdapter extends MouseAdapter
    {
        private boolean cameraDragged = false;

        private boolean hContentAreaDragged = false;
        private boolean hContentStartDragged = false;
        private boolean hContentEndDragged = false;
        private boolean vContentAreaDragged = false;
        private boolean vContentStartDragged = false;
        private boolean vContentEndDragged = false;
        private boolean contentAreaDragged = false;

        private boolean hStretchAreaDragged = false;
        private boolean hStretchStartDragged = false;
        private boolean hStretchEndDragged = false;
        private boolean vStretchAreaDragged = false;
        private boolean vStretchStartDragged = false;
        private boolean vStretchEndDragged = false;

        private boolean addingHorizontalStretch = false;
        private boolean addingVerticalStretch = false;
        private boolean removingHorizontalStretch = false;
        private boolean removingVerticalStretch = false;

        private int startX = -1;
        private int startY = -1;
        private Rectangle startRect = null;

        private Insets startMargin = null;

        private NinePatchInterval changedInterval = null;
        private NinePatchInterval removedInterval = null;

        @Override
        public void mouseWheelMoved ( final MouseWheelEvent e )
        {
            mouseEvent ( e, MouseEventType.mouseWheelMoved );
        }

        @Override
        public void mousePressed ( final MouseEvent e )
        {
            mouseEvent ( e, MouseEventType.mousePressed );
        }

        @Override
        public void mouseDragged ( final MouseEvent e )
        {
            mouseEvent ( e, MouseEventType.mouseDragged );
        }

        @Override
        public void mouseReleased ( final MouseEvent e )
        {
            mouseEvent ( e, MouseEventType.mouseReleased );
        }

        @Override
        public void mouseEntered ( final MouseEvent e )
        {
            mouseEvent ( e, MouseEventType.mouseEntered );
        }

        @Override
        public void mouseExited ( final MouseEvent e )
        {
            mouseEvent ( e, MouseEventType.mouseExited );
        }

        @Override
        public void mouseMoved ( final MouseEvent e )
        {
            mouseEvent ( e, MouseEventType.mouseMoved );
        }

        private void mouseEvent ( final MouseEvent e, final MouseEventType mouseEventType )
        {
            if ( processMouseEvent ( e, mouseEventType ) )
            {
                repaint ();
            }
        }

        private boolean processMouseEvent ( MouseEvent e, final MouseEventType mouseEventType )
        {
            // Checking whether image is set into the editor or not
            final BufferedImage image = getRawImage ();
            if ( image == null )
            {
                return false;
            }

            // Current cursor position
            final int x = e.getX ();
            final int y = e.getY ();

            // Constants
            final int cw = NinePatchEditor.this.getWidth ();
            final int ch = NinePatchEditor.this.getHeight ();
            final int iw = image.getWidth () * zoom;
            final int ih = image.getHeight () * zoom;
            final Insets margin = ninePatchIcon.getMargin ();
            final int imageStartX = ( cw + ( showRuler ? RULER_LENGTH : 0 ) ) / 2 - iw / 2;
            final int imageStartY = ( ch + ( showRuler ? RULER_LENGTH : 0 ) ) / 2 - ih / 2;

            // Variables
            boolean repaintRequired = false;

            // Content area coordinates
            final int contentStartX = getContentStartX ( imageStartX, margin );
            final int contentEndX = getContentEndX ( imageStartX, iw, margin );
            final int contentStartY = getContentStartY ( imageStartY, margin );
            final int contentEndY = getContentEndY ( imageStartY, ih, margin );

            // Request focus on any action inside
            if ( mouseEventType.equals ( MouseEventType.mousePressed ) )
            {
                requestFocusInWindow ();
            }

            // Mouse cursor position update
            if ( ( showRulerCursorPosition || showAreaCursorPosition ) && mouseEventType.equals ( MouseEventType.mouseMoved ) ||
                    mouseEventType.equals ( MouseEventType.mouseDragged ) || mouseEventType.equals ( MouseEventType.mouseEntered ) ||
                    mouseEventType.equals ( MouseEventType.mouseExited ) )
            {
                repaintRequired = true;
            }

            // Zoom change
            if ( !someDragged && mouseEventType.equals ( MouseEventType.mouseWheelMoved ) &&
                    ( SwingUtils.isCtrl ( e ) || SwingUtils.isAlt ( e ) ) )
            {
                final MouseWheelEvent mwe = ( MouseWheelEvent ) e;
                setZoom ( getZoom () - mwe.getWheelRotation () );
                setCursor ( Cursor.getDefaultCursor () );
                return repaintRequired;
            }

            // Visible area drag
            if ( SwingUtilities.isMiddleMouseButton ( e ) && ( cameraDragged || !someDragged ) )
            {
                if ( mouseEventType.equals ( MouseEventType.mousePressed ) )
                {
                    someDragged = true;
                    cameraDragged = true;
                    startRect = getVisibleRect ();
                    e = SwingUtilities.convertMouseEvent ( NinePatchEditor.this, e, view );
                    startX = e.getX ();
                    startY = e.getY ();
                    setCursor ( Cursor.getPredefinedCursor ( Cursor.HAND_CURSOR ) );
                }
                else if ( cameraDragged && mouseEventType.equals ( MouseEventType.mouseDragged ) )
                {
                    e = SwingUtilities.convertMouseEvent ( NinePatchEditor.this, e, view );
                    view.getHorizontalScrollBar ().setValue ( startRect.x - ( e.getX () - startX ) );
                    view.getVerticalScrollBar ().setValue ( startRect.y - ( e.getY () - startY ) );
                    setCursor ( Cursor.getPredefinedCursor ( Cursor.HAND_CURSOR ) );
                }
                else if ( cameraDragged && mouseEventType.equals ( MouseEventType.mouseReleased ) )
                {
                    someDragged = false;
                    cameraDragged = false;
                    setCursor ( Cursor.getDefaultCursor () );
                }
                return repaintRequired;
            }

            // Continue only if icon presented
            if ( ninePatchIcon == null || ninePatchImage == null )
            {
                return repaintRequired;
            }

            // Content area drag
            if ( hContentAreaDragged ||
                    !someDragged && new Rectangle ( contentStartX, imageStartY + ih, contentEndX - contentStartX, zoom ).contains ( x, y ) )
            {
                // Dragging horizontal content area
                if ( mouseEventType.equals ( MouseEventType.mousePressed ) )
                {
                    someDragged = true;
                    hContentAreaDragged = true;
                    startX = x;
                    startMargin = margin;
                }
                else if ( hContentAreaDragged && mouseEventType.equals ( MouseEventType.mouseDragged ) )
                {
                    final int changeX = ( x - startX ) / zoom;
                    int left = startMargin.left + changeX;
                    if ( left < 0 )
                    {
                        left = 0;
                    }
                    if ( left > image.getWidth () - 1 )
                    {
                        left = image.getWidth () - 1;
                    }
                    int right = startMargin.right - changeX;
                    if ( right < 0 )
                    {
                        right = 0;
                    }
                    if ( right > image.getWidth () - 1 )
                    {
                        right = image.getWidth () - 1;
                    }
                    ninePatchIcon.setMargin ( startMargin.top, left, startMargin.bottom, right );
                    repaintRequired = true;
                    fireStateChanged ();
                }
                else if ( hContentAreaDragged && mouseEventType.equals ( MouseEventType.mouseReleased ) )
                {
                    someDragged = false;
                    hContentAreaDragged = false;
                    saveHistoryState ();
                    fireStateChanged ();
                }
                setCursor ( Cursor.getPredefinedCursor ( Cursor.MOVE_CURSOR ) );
                return repaintRequired;
            }
            if ( vContentAreaDragged ||
                    !someDragged && new Rectangle ( imageStartX + iw, contentStartY, zoom, contentEndY - contentStartY ).contains ( x, y ) )
            {
                // Dragging vertical content area
                if ( mouseEventType.equals ( MouseEventType.mousePressed ) )
                {
                    someDragged = true;
                    vContentAreaDragged = true;
                    startY = y;
                    startMargin = margin;
                }
                else if ( vContentAreaDragged && mouseEventType.equals ( MouseEventType.mouseDragged ) )
                {
                    final int changeY = ( y - startY ) / zoom;
                    int top = startMargin.top + changeY;
                    if ( top < 0 )
                    {
                        top = 0;
                    }
                    if ( top > image.getHeight () - 1 )
                    {
                        top = image.getHeight () - 1;
                    }
                    int bottom = startMargin.bottom - changeY;
                    if ( bottom < 0 )
                    {
                        bottom = 0;
                    }
                    if ( bottom > image.getHeight () - 1 )
                    {
                        bottom = image.getHeight () - 1;
                    }
                    ninePatchIcon.setMargin ( top, startMargin.left, bottom, startMargin.right );
                    repaintRequired = true;
                    fireStateChanged ();
                }
                else if ( vContentAreaDragged && mouseEventType.equals ( MouseEventType.mouseReleased ) )
                {
                    someDragged = false;
                    vContentAreaDragged = false;
                    saveHistoryState ();
                    fireStateChanged ();
                }
                setCursor ( Cursor.getPredefinedCursor ( Cursor.MOVE_CURSOR ) );
                return repaintRequired;
            }

            // Content area resize
            if ( hContentStartDragged || !someDragged && y >= imageStartY && contentStartX + getSnap () >= x &&
                    contentStartX - getSnap () <= x )
            {
                // Dragging horizontal content area start
                if ( mouseEventType.equals ( MouseEventType.mousePressed ) )
                {
                    someDragged = true;
                    hContentStartDragged = true;
                    startX = x;
                    startMargin = margin;
                }
                else if ( hContentStartDragged && mouseEventType.equals ( MouseEventType.mouseDragged ) )
                {
                    final int changeX = ( x - startX ) / zoom;
                    int left = startMargin.left + changeX;
                    if ( left < 0 )
                    {
                        left = 0;
                    }
                    if ( left > image.getWidth () - startMargin.right - 1 )
                    {
                        left = image.getWidth () - startMargin.right - 1;
                    }
                    ninePatchIcon.setMargin ( startMargin.top, left, startMargin.bottom, startMargin.right );
                    repaintRequired = true;
                    fireStateChanged ();
                }
                else if ( hContentStartDragged && mouseEventType.equals ( MouseEventType.mouseReleased ) )
                {
                    someDragged = false;
                    hContentStartDragged = false;
                    saveHistoryState ();
                    fireStateChanged ();
                }
                setCursor ( Cursor.getPredefinedCursor ( Cursor.E_RESIZE_CURSOR ) );
                return repaintRequired;
            }
            if ( hContentEndDragged || !someDragged && y >= imageStartY && contentEndX + getSnap () >= x &&
                    contentEndX - getSnap () <= x )
            {
                // Dragging horizontal content area end
                if ( mouseEventType.equals ( MouseEventType.mousePressed ) )
                {
                    someDragged = true;
                    hContentEndDragged = true;
                    startX = x;
                    startMargin = margin;
                }
                else if ( hContentEndDragged && mouseEventType.equals ( MouseEventType.mouseDragged ) )
                {
                    final int changeX = ( x - startX ) / zoom;
                    int right = startMargin.right - changeX;
                    if ( right < 0 )
                    {
                        right = 0;
                    }
                    if ( right > image.getWidth () - startMargin.left - 1 )
                    {
                        right = image.getWidth () - startMargin.left - 1;
                    }
                    ninePatchIcon.setMargin ( startMargin.top, startMargin.left, startMargin.bottom, right );
                    repaintRequired = true;
                    fireStateChanged ();
                }
                else if ( hContentEndDragged && mouseEventType.equals ( MouseEventType.mouseReleased ) )
                {
                    someDragged = false;
                    hContentEndDragged = false;
                    saveHistoryState ();
                    fireStateChanged ();
                }
                setCursor ( Cursor.getPredefinedCursor ( Cursor.E_RESIZE_CURSOR ) );
                return repaintRequired;
            }
            if ( vContentStartDragged || !someDragged && x >= imageStartX && contentStartY + getSnap () >= y &&
                    contentStartY - getSnap () <= y )
            {
                // Dragging vertical content area start
                if ( mouseEventType.equals ( MouseEventType.mousePressed ) )
                {
                    someDragged = true;
                    vContentStartDragged = true;
                    startY = y;
                    startMargin = margin;
                }
                else if ( vContentStartDragged && mouseEventType.equals ( MouseEventType.mouseDragged ) )
                {
                    final int changeY = ( y - startY ) / zoom;
                    int top = startMargin.top + changeY;
                    if ( top < 0 )
                    {
                        top = 0;
                    }
                    if ( top > image.getHeight () - startMargin.bottom - 1 )
                    {
                        top = image.getHeight () - startMargin.bottom - 1;
                    }
                    ninePatchIcon.setMargin ( top, startMargin.left, startMargin.bottom, startMargin.right );
                    repaintRequired = true;
                    fireStateChanged ();
                }
                else if ( vContentStartDragged && mouseEventType.equals ( MouseEventType.mouseReleased ) )
                {
                    someDragged = false;
                    vContentStartDragged = false;
                    saveHistoryState ();
                    fireStateChanged ();
                }
                setCursor ( Cursor.getPredefinedCursor ( Cursor.S_RESIZE_CURSOR ) );
                return repaintRequired;
            }
            if ( vContentEndDragged || !someDragged && x >= imageStartX && contentEndY + getSnap () >= y &&
                    contentEndY - getSnap () <= y )
            {
                // Dragging vertical content area end
                if ( mouseEventType.equals ( MouseEventType.mousePressed ) )
                {
                    someDragged = true;
                    vContentEndDragged = true;
                    startY = y;
                    startMargin = margin;
                }
                else if ( vContentEndDragged && mouseEventType.equals ( MouseEventType.mouseDragged ) )
                {
                    final int changeY = ( y - startY ) / zoom;
                    int bottom = startMargin.bottom - changeY;
                    if ( bottom < 0 )
                    {
                        bottom = 0;
                    }
                    if ( bottom > image.getHeight () - startMargin.top - 1 )
                    {
                        bottom = image.getHeight () - startMargin.top - 1;
                    }
                    ninePatchIcon.setMargin ( startMargin.top, startMargin.left, bottom, startMargin.right );
                    repaintRequired = true;
                    fireStateChanged ();
                }
                else if ( vContentEndDragged && mouseEventType.equals ( MouseEventType.mouseReleased ) )
                {
                    someDragged = false;
                    vContentEndDragged = false;
                    saveHistoryState ();
                    fireStateChanged ();
                }
                setCursor ( Cursor.getPredefinedCursor ( Cursor.S_RESIZE_CURSOR ) );
                return repaintRequired;
            }

            // Stretch
            for ( final NinePatchInterval npi : ninePatchIcon.getHorizontalStretch () )
            {
                if ( !npi.isPixel () )
                {
                    // Stretch areas drag
                    if ( hStretchAreaDragged && changedInterval.getId ().equals ( npi.getId () ) || !someDragged &&
                            new Rectangle ( imageStartX + npi.getStart () * zoom, imageStartY - zoom,
                                    ( npi.getEnd () - npi.getStart () + 1 ) * zoom, zoom + ( fillStretchAreas ? ih : 0 ) )
                                    .contains ( x, y ) )
                    {
                        // Dragging horizontal stretch area
                        if ( mouseEventType.equals ( MouseEventType.mousePressed ) )
                        {
                            someDragged = true;
                            hStretchAreaDragged = true;
                            startX = x;
                            changedInterval = npi.clone ();
                        }
                        else if ( hStretchAreaDragged && mouseEventType.equals ( MouseEventType.mouseDragged ) )
                        {
                            final int changeX = ( x - startX ) / zoom;
                            int start = changedInterval.getStart () + changeX;
                            if ( start < 0 )
                            {
                                start = 0;
                            }
                            if ( start > image.getWidth () - 1 )
                            {
                                start = image.getWidth () - 1;
                            }
                            int end = changedInterval.getEnd () + changeX;
                            if ( end < 0 )
                            {
                                end = 0;
                            }
                            if ( end > image.getWidth () - 1 )
                            {
                                end = image.getWidth () - 1;
                            }
                            npi.setStart ( start );
                            npi.setEnd ( end );
                            repaintRequired = true;
                        }
                        else if ( hStretchAreaDragged && mouseEventType.equals ( MouseEventType.mouseReleased ) )
                        {
                            someDragged = false;
                            hStretchAreaDragged = false;
                            verifyHorizontalStretchAreas ();
                            repaintRequired = true;
                            saveHistoryState ();
                            fireStateChanged ();
                        }
                        setCursor ( Cursor.getPredefinedCursor ( Cursor.MOVE_CURSOR ) );
                        return repaintRequired;
                    }

                    // Stretch areas resize cursor  
                    if ( y <= imageStartY + ih )
                    {
                        if ( hStretchStartDragged && changedInterval.getId ().equals ( npi.getId () ) || !someDragged &&
                                imageStartX + npi.getStart () * zoom - getSnap () <= x &&
                                x <= imageStartX + npi.getStart () * zoom + getSnap () )
                        {
                            // Dragging horizontal stretch area start
                            if ( mouseEventType.equals ( MouseEventType.mousePressed ) )
                            {
                                someDragged = true;
                                hStretchStartDragged = true;
                                startX = x;
                                changedInterval = npi.clone ();
                            }
                            else if ( hStretchStartDragged && mouseEventType.equals ( MouseEventType.mouseDragged ) )
                            {
                                final int changeX = ( x - startX ) / zoom;
                                int start = changedInterval.getStart () + changeX;
                                if ( start < 0 )
                                {
                                    start = 0;
                                }
                                if ( start > changedInterval.getEnd () )
                                {
                                    start = changedInterval.getEnd ();
                                }
                                npi.setStart ( start );
                                repaintRequired = true;
                            }
                            else if ( hStretchStartDragged && mouseEventType.equals ( MouseEventType.mouseReleased ) )
                            {
                                someDragged = false;
                                hStretchStartDragged = false;
                                verifyHorizontalStretchAreas ();
                                repaintRequired = true;
                                saveHistoryState ();
                                fireStateChanged ();
                            }
                            setCursor ( Cursor.getPredefinedCursor ( Cursor.E_RESIZE_CURSOR ) );
                            return repaintRequired;
                        }
                        if ( hStretchEndDragged && changedInterval.getId ().equals ( npi.getId () ) || !someDragged &&
                                imageStartX + ( npi.getEnd () + 1 ) * zoom - getSnap () <= x &&
                                x <= imageStartX + ( npi.getEnd () + 1 ) * zoom + getSnap () )
                        {
                            // Dragging horizontal stretch area end
                            if ( mouseEventType.equals ( MouseEventType.mousePressed ) )
                            {
                                someDragged = true;
                                hStretchEndDragged = true;
                                startX = x;
                                changedInterval = npi.clone ();
                            }
                            else if ( hStretchEndDragged && mouseEventType.equals ( MouseEventType.mouseDragged ) )
                            {
                                final int changeX = ( x - startX ) / zoom;
                                int end = changedInterval.getEnd () + changeX;
                                if ( end < changedInterval.getStart () )
                                {
                                    end = changedInterval.getStart ();
                                }
                                if ( end > image.getWidth () - 1 )
                                {
                                    end = image.getWidth () - 1;
                                }
                                npi.setEnd ( end );
                                repaintRequired = true;
                            }
                            else if ( hStretchEndDragged && mouseEventType.equals ( MouseEventType.mouseReleased ) )
                            {
                                someDragged = false;
                                hStretchEndDragged = false;
                                verifyHorizontalStretchAreas ();
                                repaintRequired = true;
                                saveHistoryState ();
                                fireStateChanged ();
                            }
                            setCursor ( Cursor.getPredefinedCursor ( Cursor.E_RESIZE_CURSOR ) );
                            return repaintRequired;
                        }
                    }
                }
            }
            for ( final NinePatchInterval npi : ninePatchIcon.getVerticalStretch () )
            {
                if ( !npi.isPixel () )
                {
                    // Stretch areas drag
                    if ( vStretchAreaDragged && changedInterval.getId ().equals ( npi.getId () ) || !someDragged &&
                            new Rectangle ( imageStartX - zoom, imageStartY + npi.getStart () * zoom, zoom + ( fillStretchAreas ? iw : 0 ),
                                    ( npi.getEnd () - npi.getStart () + 1 ) * zoom ).contains ( x, y ) )
                    {
                        // Dragging vertical stretch area
                        if ( mouseEventType.equals ( MouseEventType.mousePressed ) )
                        {
                            someDragged = true;
                            vStretchAreaDragged = true;
                            startY = y;
                            changedInterval = npi.clone ();
                        }
                        else if ( vStretchAreaDragged && mouseEventType.equals ( MouseEventType.mouseDragged ) )
                        {
                            final int changeY = ( y - startY ) / zoom;
                            int start = changedInterval.getStart () + changeY;
                            if ( start < 0 )
                            {
                                start = 0;
                            }
                            if ( start > image.getHeight () - 1 )
                            {
                                start = image.getHeight () - 1;
                            }
                            int end = changedInterval.getEnd () + changeY;
                            if ( end < 0 )
                            {
                                end = 0;
                            }
                            if ( end > image.getHeight () - 1 )
                            {
                                end = image.getHeight () - 1;
                            }
                            npi.setStart ( start );
                            npi.setEnd ( end );
                            repaintRequired = true;
                        }
                        else if ( vStretchAreaDragged && mouseEventType.equals ( MouseEventType.mouseReleased ) )
                        {
                            someDragged = false;
                            vStretchAreaDragged = false;
                            verifyVerticalStretchAreas ();
                            repaintRequired = true;
                            saveHistoryState ();
                            fireStateChanged ();
                        }
                        setCursor ( Cursor.getPredefinedCursor ( Cursor.MOVE_CURSOR ) );
                        return repaintRequired;
                    }

                    // Stretch areas resize cursor  
                    if ( x <= imageStartX + iw )
                    {
                        if ( vStretchStartDragged && changedInterval.getId ().equals ( npi.getId () ) || !someDragged &&
                                imageStartY + npi.getStart () * zoom - getSnap () <= y &&
                                y <= imageStartY + npi.getStart () * zoom + getSnap () )
                        {
                            // Dragging vertical stretch area start
                            if ( mouseEventType.equals ( MouseEventType.mousePressed ) )
                            {
                                someDragged = true;
                                vStretchStartDragged = true;
                                startY = y;
                                changedInterval = npi.clone ();
                            }
                            else if ( vStretchStartDragged && mouseEventType.equals ( MouseEventType.mouseDragged ) )
                            {
                                final int changeY = ( y - startY ) / zoom;
                                int start = changedInterval.getStart () + changeY;
                                if ( start < 0 )
                                {
                                    start = 0;
                                }
                                if ( start > changedInterval.getEnd () )
                                {
                                    start = changedInterval.getEnd ();
                                }
                                npi.setStart ( start );
                                repaintRequired = true;
                            }
                            else if ( vStretchStartDragged && mouseEventType.equals ( MouseEventType.mouseReleased ) )
                            {
                                someDragged = false;
                                vStretchStartDragged = false;
                                verifyVerticalStretchAreas ();
                                repaintRequired = true;
                                saveHistoryState ();
                                fireStateChanged ();
                            }
                            setCursor ( Cursor.getPredefinedCursor ( Cursor.S_RESIZE_CURSOR ) );
                            return repaintRequired;
                        }
                        if ( vStretchEndDragged && changedInterval.getId ().equals ( npi.getId () ) || !someDragged &&
                                imageStartY + ( npi.getEnd () + 1 ) * zoom - getSnap () <= y &&
                                y <= imageStartY + ( npi.getEnd () + 1 ) * zoom + getSnap () )
                        {
                            // Dragging vertical stretch area end
                            if ( mouseEventType.equals ( MouseEventType.mousePressed ) )
                            {
                                someDragged = true;
                                vStretchEndDragged = true;
                                startY = y;
                                changedInterval = npi.clone ();
                            }
                            else if ( vStretchEndDragged && mouseEventType.equals ( MouseEventType.mouseDragged ) )
                            {
                                final int changeY = ( y - startY ) / zoom;
                                int end = changedInterval.getEnd () + changeY;
                                if ( end < changedInterval.getStart () )
                                {
                                    end = changedInterval.getStart ();
                                }
                                if ( end > image.getHeight () - 1 )
                                {
                                    end = image.getHeight () - 1;
                                }
                                npi.setEnd ( end );
                                repaintRequired = true;
                            }
                            else if ( vStretchEndDragged && mouseEventType.equals ( MouseEventType.mouseReleased ) )
                            {
                                someDragged = false;
                                vStretchEndDragged = false;
                                verifyVerticalStretchAreas ();
                                repaintRequired = true;
                                saveHistoryState ();
                                fireStateChanged ();
                            }
                            setCursor ( Cursor.getPredefinedCursor ( Cursor.S_RESIZE_CURSOR ) );
                            return repaintRequired;
                        }
                    }
                }
            }

            // Dragging content area
            if ( fillContentArea )
            {
                if ( contentAreaDragged || !someDragged &&
                        new Rectangle ( contentStartX, contentStartY, contentEndX - contentStartX, contentEndY - contentStartY )
                                .contains ( x, y ) )
                {
                    // Dragging vertical content area
                    if ( mouseEventType.equals ( MouseEventType.mousePressed ) )
                    {
                        someDragged = true;
                        contentAreaDragged = true;
                        startX = x;
                        startY = y;
                        startMargin = margin;
                    }
                    else if ( contentAreaDragged && mouseEventType.equals ( MouseEventType.mouseDragged ) )
                    {
                        final int changeY = ( y - startY ) / zoom;
                        int top = startMargin.top + changeY;
                        if ( top < 0 )
                        {
                            top = 0;
                        }
                        if ( top > image.getHeight () - 1 )
                        {
                            top = image.getHeight () - 1;
                        }
                        int bottom = startMargin.bottom - changeY;
                        if ( bottom < 0 )
                        {
                            bottom = 0;
                        }
                        if ( bottom > image.getHeight () - 1 )
                        {
                            bottom = image.getHeight () - 1;
                        }
                        final int changeX = ( x - startX ) / zoom;
                        int left = startMargin.left + changeX;
                        if ( left < 0 )
                        {
                            left = 0;
                        }
                        if ( left > image.getWidth () - 1 )
                        {
                            left = image.getWidth () - 1;
                        }
                        int right = startMargin.right - changeX;
                        if ( right < 0 )
                        {
                            right = 0;
                        }
                        if ( right > image.getWidth () - 1 )
                        {
                            right = image.getWidth () - 1;
                        }
                        ninePatchIcon.setMargin ( top, left, bottom, right );
                        repaintRequired = true;
                        fireStateChanged ();
                    }
                    else if ( contentAreaDragged && mouseEventType.equals ( MouseEventType.mouseReleased ) )
                    {
                        someDragged = false;
                        contentAreaDragged = false;
                        saveHistoryState ();
                        fireStateChanged ();
                    }
                    setCursor ( Cursor.getPredefinedCursor ( Cursor.MOVE_CURSOR ) );
                    return repaintRequired;
                }
            }

            // Adding new stretch areas
            if ( SwingUtilities.isLeftMouseButton ( e ) )
            {
                if ( addingHorizontalStretch || !someDragged )
                {
                    if ( mouseEventType.equals ( MouseEventType.mousePressed ) && x > imageStartX &&
                            x < imageStartX + iw && y < imageStartY )
                    {
                        someDragged = true;
                        addingHorizontalStretch = true;
                        startX = x;
                        changedInterval = new NinePatchInterval ( ( x - imageStartX ) / zoom, false );
                        ninePatchIcon.addHorizontalStretch ( changedInterval );
                        repaintRequired = true;
                    }
                    else if ( addingHorizontalStretch && mouseEventType.equals ( MouseEventType.mouseDragged ) )
                    {
                        final int start = ( startX - imageStartX ) / zoom;
                        final int end = ( x - imageStartX ) / zoom;
                        changedInterval.setStart ( Math.max ( 0, Math.min ( start, end ) ) );
                        changedInterval.setEnd ( Math.min ( image.getWidth () - 1, Math.max ( start, end ) ) );
                        repaintRequired = true;
                    }
                    else if ( addingHorizontalStretch && mouseEventType.equals ( MouseEventType.mouseReleased ) )
                    {
                        someDragged = false;
                        addingHorizontalStretch = false;
                        verifyHorizontalStretchAreas ();
                        repaintRequired = true;
                        saveHistoryState ();
                        fireStateChanged ();
                    }
                }
                if ( addingVerticalStretch || !someDragged )
                {
                    if ( mouseEventType.equals ( MouseEventType.mousePressed ) && y > imageStartY &&
                            y < imageStartY + ih && x < imageStartX )
                    {
                        someDragged = true;
                        addingVerticalStretch = true;
                        startY = y;
                        changedInterval = new NinePatchInterval ( ( y - imageStartY ) / zoom, false );
                        ninePatchIcon.addVerticalStretch ( changedInterval );
                        repaintRequired = true;
                    }
                    else if ( addingVerticalStretch && mouseEventType.equals ( MouseEventType.mouseDragged ) )
                    {
                        final int start = ( startY - imageStartY ) / zoom;
                        final int end = ( y - imageStartY ) / zoom;
                        changedInterval.setStart ( Math.max ( 0, Math.min ( start, end ) ) );
                        changedInterval.setEnd ( Math.min ( image.getHeight () - 1, Math.max ( start, end ) ) );
                        repaintRequired = true;
                    }
                    else if ( addingVerticalStretch && mouseEventType.equals ( MouseEventType.mouseReleased ) )
                    {
                        someDragged = false;
                        addingVerticalStretch = false;
                        verifyVerticalStretchAreas ();
                        repaintRequired = true;
                        saveHistoryState ();
                        fireStateChanged ();
                    }
                }
            }

            // Removing stretch areas   
            if ( SwingUtilities.isRightMouseButton ( e ) )
            {
                if ( removingHorizontalStretch || !someDragged )
                {
                    if ( mouseEventType.equals ( MouseEventType.mousePressed ) && x > imageStartX &&
                            x < imageStartX + iw && y < imageStartY )
                    {
                        someDragged = true;
                        removingHorizontalStretch = true;
                        startX = x;
                        removedInterval = new NinePatchInterval ( ( x - imageStartX ) / zoom, false );
                        finishHorizontalRemoval ();
                        repaintRequired = true;
                    }
                    else if ( removingHorizontalStretch && mouseEventType.equals ( MouseEventType.mouseDragged ) )
                    {
                        final int start = ( startX - imageStartX ) / zoom;
                        final int end = ( x - imageStartX ) / zoom;
                        removedInterval.setStart ( Math.max ( 0, Math.min ( start, end ) ) );
                        removedInterval.setEnd ( Math.min ( image.getWidth () - 1, Math.max ( start, end ) ) );
                        finishHorizontalRemoval ();
                        repaintRequired = true;
                    }
                    else if ( removingHorizontalStretch && mouseEventType.equals ( MouseEventType.mouseReleased ) )
                    {
                        someDragged = false;
                        removingHorizontalStretch = false;
                        repaintRequired = true;
                        saveHistoryState ();
                        fireStateChanged ();
                    }
                }
                if ( removingVerticalStretch || !someDragged )
                {
                    if ( mouseEventType.equals ( MouseEventType.mousePressed ) && y > imageStartY &&
                            y < imageStartY + ih && x < imageStartX )
                    {
                        someDragged = true;
                        removingVerticalStretch = true;
                        startY = y;
                        removedInterval = new NinePatchInterval ( ( y - imageStartY ) / zoom, false );
                        finishVerticalRemoval ();
                        repaintRequired = true;
                    }
                    else if ( removingVerticalStretch && mouseEventType.equals ( MouseEventType.mouseDragged ) )
                    {
                        final int start = ( startY - imageStartY ) / zoom;
                        final int end = ( y - imageStartY ) / zoom;
                        removedInterval.setStart ( Math.max ( 0, Math.min ( start, end ) ) );
                        removedInterval.setEnd ( Math.min ( image.getHeight () - 1, Math.max ( start, end ) ) );
                        finishVerticalRemoval ();
                        repaintRequired = true;
                    }
                    else if ( removingVerticalStretch && mouseEventType.equals ( MouseEventType.mouseReleased ) )
                    {
                        someDragged = false;
                        removingVerticalStretch = false;
                        repaintRequired = true;
                        saveHistoryState ();
                        fireStateChanged ();
                    }
                }
            }

            // Default cursor
            setCursor ( Cursor.getDefaultCursor () );

            return repaintRequired;
        }

        private void finishHorizontalRemoval ()
        {
            final boolean[] filled = getHorizontalFilledPixels ();
            for ( int i = removedInterval.getStart (); i <= removedInterval.getEnd (); i++ )
            {
                filled[ i ] = false;
            }
            ninePatchIcon.setHorizontalStretch ( NinePatchUtils.parseStretchIntervals ( filled ) );
            validateIcon ();
        }

        private void finishVerticalRemoval ()
        {
            final boolean[] filled = getVerticalFilledPixels ();
            for ( int i = removedInterval.getStart (); i <= removedInterval.getEnd (); i++ )
            {
                filled[ i ] = false;
            }
            ninePatchIcon.setVerticalStretch ( NinePatchUtils.parseStretchIntervals ( filled ) );
            validateIcon ();
        }
    }

    private void validateIcon ()
    {
        // todo
    }

    //    private boolean hasHorizontalStretch ()
    //    {
    //        for ( NinePatchInterval npi : ninePatchIcon.getHorizontalStretch () )
    //        {
    //            if ( !npi.isPixel () )
    //            {
    //                return true;
    //            }
    //        }
    //        return false;
    //    }

    private int getSnap ()
    {
        return Math.max ( SNAP, zoom / 2 );
    }

    private void verifyHorizontalStretchAreas ()
    {
        // Verifies that there is no excessive intervals by regrouping all pixels info
        final boolean[] filled = getHorizontalFilledPixels ();
        ninePatchIcon.setHorizontalStretch ( NinePatchUtils.parseStretchIntervals ( filled ) );
    }

    private boolean[] getHorizontalFilledPixels ()
    {
        final boolean[] filled = new boolean[ getRawImage ().getWidth () ];
        for ( final NinePatchInterval npi : ninePatchIcon.getHorizontalStretch () )
        {
            for ( int i = npi.getStart (); i <= npi.getEnd (); i++ )
            {
                if ( !npi.isPixel () && i < filled.length )
                {
                    filled[ i ] = true;
                }
            }
        }
        return filled;
    }

    private void verifyVerticalStretchAreas ()
    {
        // Verifies that there is no excessive intervals by regrouping all pixels info
        final boolean[] filled = getVerticalFilledPixels ();
        ninePatchIcon.setVerticalStretch ( NinePatchUtils.parseStretchIntervals ( filled ) );
    }

    private boolean[] getVerticalFilledPixels ()
    {
        final boolean[] filled = new boolean[ getRawImage ().getHeight () ];
        for ( final NinePatchInterval npi : ninePatchIcon.getVerticalStretch () )
        {
            for ( int i = npi.getStart (); i <= npi.getEnd (); i++ )
            {
                if ( !npi.isPixel () && i < filled.length )
                {
                    filled[ i ] = true;
                }
            }
        }
        return filled;
    }

    private void verifyMargin ()
    {
        // Verifies that margins fit the image size properly
        final Insets margin = ninePatchIcon.getMargin ();

        final int maxVerMargin = getRawImage ().getHeight () - 1;
        if ( margin.top > maxVerMargin )
        {
            margin.top = maxVerMargin;
        }
        if ( margin.bottom + margin.top > maxVerMargin )
        {
            margin.bottom = maxVerMargin - margin.top;
        }

        final int maxHorMargin = getRawImage ().getWidth () - 1;
        if ( margin.left > maxHorMargin )
        {
            margin.left = maxHorMargin;
        }
        if ( margin.right + margin.left > maxHorMargin )
        {
            margin.right = maxHorMargin - margin.left;
        }
    }

    private void disassembleImage ( final BufferedImage ninePatchImage )
    {
        // Disassembles image into editor data
        try
        {
            // If image was actually a nine-patch image
            this.ninePatchIcon = new NinePatchIcon ( ninePatchImage );
            this.ninePatchImage = ninePatchImage;
        }
        catch ( final IllegalArgumentException e )
        {
            // If image is not a nine-patch image we will fix it to be one
            final BufferedImage fixedImage = ImageUtils
                    .createCompatibleImage ( ninePatchImage.getWidth () + 2, ninePatchImage.getHeight () + 2, Transparency.TRANSLUCENT );
            final Graphics2D g2d = fixedImage.createGraphics ();
            g2d.drawImage ( ninePatchImage, 1, 1, null );
            g2d.setPaint ( Color.BLACK );
            g2d.drawLine ( 1, 0, fixedImage.getWidth () - 2, 0 );
            g2d.drawLine ( 0, 1, 0, fixedImage.getHeight () - 2 );
            g2d.dispose ();

            this.ninePatchIcon = new NinePatchIcon ( fixedImage );
            this.ninePatchImage = fixedImage;
        }
    }

    /**
     * Assembles editor data into valid Nine-patch image.
     */
    private void assembleImage ()
    {
        final BufferedImage rawImage = getRawImage ();

        // New image template
        ninePatchImage = ImageUtils.createCompatibleImage ( rawImage.getWidth () + 2, rawImage.getHeight () + 2, Transparency.TRANSLUCENT );
        final Graphics2D g2d = ninePatchImage.createGraphics ();
        g2d.drawImage ( rawImage, 1, 1, null );
        g2d.dispose ();

        // Color to fill with marks
        final int rgb = Color.BLACK.getRGB ();

        // todo Replace pixel filling with line painting to speedup assembling process in times

        // Stretch
        final boolean[] hf = getHorizontalFilledPixels ();
        for ( int i = 0; i < rawImage.getWidth (); i++ )
        {
            if ( hf[ i ] )
            {
                ninePatchImage.setRGB ( i + 1, 0, rgb );
            }
        }
        final boolean[] vf = getVerticalFilledPixels ();
        for ( int i = 0; i < rawImage.getHeight (); i++ )
        {
            if ( vf[ i ] )
            {
                ninePatchImage.setRGB ( 0, i + 1, rgb );
            }
        }

        // Content
        final Insets margin = ninePatchIcon.getMargin ();
        for ( int i = margin.left; i < rawImage.getWidth () - margin.right; i++ )
        {
            ninePatchImage.setRGB ( i + 1, ninePatchImage.getHeight () - 1, rgb );
        }
        for ( int i = margin.top; i < rawImage.getHeight () - margin.bottom; i++ )
        {
            ninePatchImage.setRGB ( ninePatchImage.getWidth () - 1, i + 1, rgb );
        }
    }

    @Override
    protected void paintComponent ( final Graphics g )
    {
        super.paintComponent ( g );

        // Editor image representation shown only when image is loaded
        if ( ninePatchIcon != null )
        {
            final Graphics2D g2d = ( Graphics2D ) g;
            final FontMetrics fm = g2d.getFontMetrics ();
            final BufferedImage image = getRawImage ();
            final int cw = getWidth ();
            final int ch = getHeight ();
            final int iw = image.getWidth () * zoom;
            final int ih = image.getHeight () * zoom;
            final Insets margin = ninePatchIcon.getMargin ();
            final int imageStartX = ( cw + ( showRuler ? RULER_LENGTH : 0 ) ) / 2 - iw / 2;
            final int imageStartY = ( ch + ( showRuler ? RULER_LENGTH : 0 ) ) / 2 - ih / 2;
            final Stroke stroke = g2d.getStroke ();

            // Native text anti-alias
            final Map taa = SwingUtils.setupTextAntialias ( g2d );

            // Alpha-background
            final Rectangle shape = new Rectangle ( imageStartX, imageStartY, iw, ih );
            ALPHA_LAYER_BACKGROUND.paint ( g2d, shape, this, null, shape );

            // Border
            g2d.setPaint ( Color.DARK_GRAY );
            g2d.drawRect ( imageStartX, imageStartY, iw, ih );

            // Background image
            g.drawImage ( image, imageStartX, imageStartY, iw, ih, null );

            // Editor stretch guidelines
            g2d.setStroke ( GUIDELINE_STROKE );
            for ( final NinePatchInterval npi : ninePatchIcon.getHorizontalStretch () )
            {
                if ( !npi.isPixel () )
                {
                    // Stretched area
                    if ( fillStretchAreas )
                    {
                        g2d.setPaint ( STRETCH_COLOR );
                        g2d.fillRect ( imageStartX + npi.getStart () * zoom, imageStartY, ( npi.getEnd () - npi.getStart () + 1 ) * zoom,
                                ih );
                    }

                    // Stretched area guidelines
                    g2d.setPaint ( STRETCH_GUIDELINES_COLOR );
                    g2d.drawLine ( imageStartX + npi.getStart () * zoom, 0, imageStartX + npi.getStart () * zoom, imageStartY + ih - 1 );
                    g2d.drawLine ( imageStartX + ( npi.getEnd () + 1 ) * zoom, 0, imageStartX + ( npi.getEnd () + 1 ) * zoom,
                            imageStartY + ih - 1 );

                    // Visual representation of nine-patch image information
                    g2d.setPaint ( Color.BLACK );
                    g2d.fillRect ( imageStartX + npi.getStart () * zoom + 1, imageStartY - zoom,
                            ( npi.getEnd () - npi.getStart () + 1 ) * zoom - 1, zoom );
                }
            }
            for ( final NinePatchInterval npi : ninePatchIcon.getVerticalStretch () )
            {
                if ( !npi.isPixel () )
                {
                    // Stretched area
                    if ( fillStretchAreas )
                    {
                        g2d.setPaint ( STRETCH_COLOR );
                        g2d.fillRect ( imageStartX, imageStartY + npi.getStart () * zoom, iw,
                                ( npi.getEnd () - npi.getStart () + 1 ) * zoom );
                    }

                    // Stretched area guidelines
                    g2d.setPaint ( STRETCH_GUIDELINES_COLOR );
                    g2d.drawLine ( 0, imageStartY + npi.getStart () * zoom, imageStartX + iw - 1, imageStartY + npi.getStart () * zoom );
                    g2d.drawLine ( 0, imageStartY + ( npi.getEnd () + 1 ) * zoom, imageStartX + iw - 1,
                            imageStartY + ( npi.getEnd () + 1 ) * zoom );

                    // Visual representation of nine-patch image information
                    g2d.setPaint ( Color.BLACK );
                    g2d.fillRect ( imageStartX - zoom, imageStartY + npi.getStart () * zoom + 1, zoom,
                            ( npi.getEnd () - npi.getStart () + 1 ) * zoom - 1 );
                }
            }

            // Editor content area
            final int csx = getContentStartX ( imageStartX, margin );
            final int cex = getContentEndX ( imageStartX, iw, margin );
            final int csy = getContentStartY ( imageStartY, margin );
            final int cey = getContentEndY ( imageStartY, ih, margin );
            if ( fillContentArea )
            {
                g2d.setPaint ( CONTENT_COLOR );
                g2d.fillRect ( csx + 1, csy + 1, iw - ( margin.left + margin.right ) * zoom - 1,
                        ih - ( margin.top + margin.bottom ) * zoom - 1 );
            }
            g2d.setPaint ( CONTENT_GUIDELINES_COLOR );
            g2d.drawLine ( csx, imageStartY, csx, ch );
            g2d.drawLine ( cex, imageStartY, cex, ch );
            g2d.drawLine ( imageStartX, csy, cw, csy );
            g2d.drawLine ( imageStartX, cey, cw, cey );

            // Visual representation of nine-patch image information
            g2d.setPaint ( Color.BLACK );
            g2d.fillRect ( imageStartX + iw + 1, csy + 1, zoom, cey - csy - 1 );
            g2d.fillRect ( csx + 1, imageStartY + ih + 1, cex - csx - 1, zoom );

            // Restoring old stroke
            g2d.setStroke ( stroke );

            // Spaces between all guides at top and left sides
            if ( showGuideSpacing )
            {
                g2d.setPaint ( Color.BLACK );
                final List<Integer> horizontalGuides = getHorizontalGuides ();
                for ( int i = 0; i < horizontalGuides.size () - 1; i++ )
                {
                    final Integer guide = horizontalGuides.get ( i );
                    final Integer nextGuide = horizontalGuides.get ( i + 1 );
                    final Integer x1 = imageStartX + guide * zoom;
                    final Integer x2 = imageStartX + nextGuide * zoom;
                    final int y1 = imageStartY - zoom - 10;
                    final int y2 = imageStartY - zoom - 7;
                    g2d.drawLine ( x1, y1, x2, y1 );
                    g2d.drawLine ( x1, y1, x1, y2 );
                    g2d.drawLine ( x2, y1, x2, y2 );

                    final String px = "" + ( nextGuide - guide );
                    g2d.drawString ( px, ( x1 + x2 ) / 2 + LafUtils.getTextCenterShiftX ( fm, px ), imageStartY - zoom - 15 );
                }
                final List<Integer> verticalGuides = getVerticalGuides ();
                for ( int i = 0; i < verticalGuides.size () - 1; i++ )
                {
                    final Integer guide = verticalGuides.get ( i );
                    final Integer nextGuide = verticalGuides.get ( i + 1 );
                    final Integer y1 = imageStartY + guide * zoom;
                    final Integer y2 = imageStartY + nextGuide * zoom;
                    final int x1 = imageStartX - zoom - 10;
                    final int x2 = imageStartX - zoom - 7;
                    g2d.drawLine ( x1, y1, x1, y2 );
                    g2d.drawLine ( x1, y1, x2, y1 );
                    g2d.drawLine ( x1, y2, x2, y2 );

                    final String px = "" + ( nextGuide - guide );
                    g2d.drawString ( px, x1 - fm.stringWidth ( px ) - 5, ( y1 + y2 ) / 2 + LafUtils.getTextCenterShiftY ( fm ) );
                }
            }

            // Ruler
            if ( showRuler )
            {
                drawRuler ( g2d, new Point ( imageStartX - 1, imageStartY - 1 ), imageStartX, imageStartY );
            }

            SwingUtils.restoreTextAntialias ( g2d, taa );
        }
    }

    private void drawRuler ( final Graphics2D g2d, final Point zp, final int imageStartX, final int imageStartY )
    {
        // Variables
        final Rectangle vr = NinePatchEditor.this.getVisibleRect ();
        final int minorTick = getMinorTicks ();
        final int majorTick = getMajorTicks ();
        final int ppu = zoom;

        // Ruler background
        g2d.setPaint ( new GradientPaint ( vr.x, vr.y, METRICS_TOP, vr.x, vr.y + RULER_LENGTH, METRICS_BOTTOM ) );
        g2d.fillRect ( vr.x, vr.y, vr.width, RULER_LENGTH );
        g2d.setPaint ( new GradientPaint ( vr.x, vr.y, METRICS_TOP, vr.x + RULER_LENGTH, vr.y, METRICS_BOTTOM ) );
        g2d.fillRect ( vr.x, vr.y, RULER_LENGTH, vr.height );

        // Mouse coordinates
        if ( showRulerCursorPosition || showAreaCursorPosition )
        {
            final Point mouse = CoreSwingUtils.getMouseLocation ( this );
            if ( mouse.x > vr.x + RULER_LENGTH && mouse.x < vr.x + vr.width &&
                    mouse.y > vr.y + RULER_LENGTH && mouse.y < vr.y + vr.height )
            {
                final int px = ( mouse.x - imageStartX ) / zoom - ( mouse.x >= imageStartX ? 0 : 1 );
                final int py = ( mouse.y - imageStartY ) / zoom - ( mouse.y >= imageStartY ? 0 : 1 );

                // Position marker transparency
                final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, 0.5f );

                // Ruler cursor position
                if ( showRulerCursorPosition )
                {
                    g2d.setPaint ( Color.RED );
                    g2d.fillRect ( imageStartX + px * zoom, vr.y, zoom, RULER_LENGTH );
                    g2d.fillRect ( vr.x, imageStartY + py * zoom, RULER_LENGTH, zoom );
                }

                // Area cursor position
                if ( showAreaCursorPosition )
                {
                    final Shape oldClip = GraphicsUtils.intersectClip ( g2d,
                            new Rectangle ( vr.x + RULER_LENGTH, vr.y + RULER_LENGTH, vr.width - RULER_LENGTH, vr.height - RULER_LENGTH ),
                            showAreaCursorPosition );
                    g2d.setPaint ( Color.RED );
                    g2d.fillRect ( imageStartX + px * zoom, vr.y, zoom, vr.height );
                    g2d.fillRect ( vr.x, imageStartY + py * zoom, vr.width, zoom );
                    GraphicsUtils.restoreClip ( g2d, oldClip );
                }

                GraphicsUtils.restoreComposite ( g2d, oc );
            }
        }

        g2d.setPaint ( Color.BLACK );
        String number;
        int i;

        // Horizontal marks
        i = vr.x + ( zp.x - vr.x ) % minorTick;
        for ( int j = i; j <= vr.x + vr.width; j += minorTick )
        {
            g2d.drawLine ( j, vr.y + RULER_LENGTH - 6, j, vr.y + RULER_LENGTH );
        }
        i = vr.x + ( zp.x - vr.x ) % majorTick;
        for ( int j = i; j <= vr.x + vr.width; j += majorTick )
        {
            g2d.drawLine ( j, vr.y + RULER_LENGTH - 15, j, vr.y + RULER_LENGTH );

            number = "" + ( ( j - zp.x ) / ppu );
            g2d.drawString ( number, j + 2, vr.y + RULER_LENGTH - 10 );
        }
        g2d.setPaint ( Color.BLACK );
        g2d.drawLine ( vr.x + RULER_LENGTH, vr.y + RULER_LENGTH, vr.x + vr.width, vr.y + RULER_LENGTH );

        // Vertical marks
        i = vr.y + ( zp.y - vr.y ) % minorTick;
        for ( int j = i; j <= vr.y + vr.height; j += minorTick )
        {
            g2d.drawLine ( vr.x + RULER_LENGTH - 6, j, vr.x + RULER_LENGTH, j );
        }
        i = vr.y + ( zp.y - vr.y ) % majorTick;
        for ( int j = i; j <= vr.y + vr.height; j += majorTick )
        {
            g2d.drawLine ( vr.x + RULER_LENGTH - 15, j, vr.x + RULER_LENGTH, j );

            number = "" + ( ( j - zp.y ) / ppu );
            g2d.rotate ( Math.toRadians ( -90 ) );
            g2d.drawString ( number, 1 - j - getFontMetrics ( getFont () ).stringWidth ( number ) - 2, vr.x + RULER_LENGTH - 10 );
            g2d.rotate ( Math.toRadians ( 90 ) );
        }
        g2d.setPaint ( Color.BLACK );
        g2d.drawLine ( vr.x + RULER_LENGTH, vr.y + RULER_LENGTH, vr.x + RULER_LENGTH, vr.y + vr.height );

        // Ruler corner
        g2d.setPaint ( Color.WHITE );
        g2d.fillRect ( vr.x, vr.y, RULER_LENGTH, RULER_LENGTH );
        final String unitsName = zoom + "x";
        final Point ts = LafUtils.getTextCenterShift ( g2d.getFontMetrics (), unitsName );
        g2d.setPaint ( Color.DARK_GRAY );
        g2d.drawString ( unitsName, vr.x + RULER_LENGTH / 2 + ts.x, vr.y + RULER_LENGTH / 2 + ts.y );
        g2d.setPaint ( Color.BLACK );
        g2d.drawLine ( vr.x + RULER_LENGTH, vr.y, vr.x + RULER_LENGTH, vr.y + RULER_LENGTH );
        g2d.drawLine ( vr.x, vr.y + RULER_LENGTH, vr.x + RULER_LENGTH, vr.y + RULER_LENGTH );
    }

    private int getMajorTicks ()
    {
        switch ( zoom )
        {
            case 1:
                return zoom * 50;
            case 2:
                return zoom * 20;
            case 3:
                return zoom * 20;
            case 4:
                return zoom * 10;
            case 5:
                return zoom * 10;
            case 6:
                return zoom * 10;
            default:
                return zoom * 5;
        }
    }

    private int getMinorTicks ()
    {
        switch ( zoom )
        {
            case 1:
                return zoom * 5;
            case 2:
                return zoom * 5;
            case 3:
                return zoom * 2;
            case 4:
                return zoom * 2;
            case 5:
                return zoom;
            case 6:
                return zoom;
            default:
                return zoom;
        }
    }

    private List<Integer> getHorizontalGuides ()
    {
        final List<Integer> horizontalGuides = new ArrayList<Integer> ();

        // Stretch guides
        for ( final NinePatchInterval npi : ninePatchIcon.getHorizontalStretch () )
        {
            if ( !horizontalGuides.contains ( npi.getStart () ) )
            {
                horizontalGuides.add ( npi.getStart () );
            }
            if ( !horizontalGuides.contains ( npi.getEnd () + 1 ) )
            {
                horizontalGuides.add ( npi.getEnd () + 1 );
            }
        }

        // Content guides
        final Insets margin = ninePatchIcon.getMargin ();
        if ( !horizontalGuides.contains ( margin.left ) )
        {
            horizontalGuides.add ( margin.left );
        }
        if ( !horizontalGuides.contains ( getRawImage ().getWidth () - margin.right ) )
        {
            horizontalGuides.add ( getRawImage ().getWidth () - margin.right );
        }

        // Sort ascending
        Collections.sort ( horizontalGuides );

        return horizontalGuides;
    }

    private List<Integer> getVerticalGuides ()
    {
        final List<Integer> verticalGuides = new ArrayList<Integer> ();

        // Stretch guides
        for ( final NinePatchInterval npi : ninePatchIcon.getVerticalStretch () )
        {
            if ( !verticalGuides.contains ( npi.getStart () ) )
            {
                verticalGuides.add ( npi.getStart () );
            }
            if ( !verticalGuides.contains ( npi.getEnd () + 1 ) )
            {
                verticalGuides.add ( npi.getEnd () + 1 );
            }
        }

        // Content guides
        final Insets margin = ninePatchIcon.getMargin ();
        if ( !verticalGuides.contains ( margin.top ) )
        {
            verticalGuides.add ( margin.top );
        }
        if ( !verticalGuides.contains ( getRawImage ().getHeight () - margin.bottom ) )
        {
            verticalGuides.add ( getRawImage ().getHeight () - margin.bottom );
        }

        // Sort ascending
        Collections.sort ( verticalGuides );

        return verticalGuides;
    }

    private int getContentStartX ( final int imageStartX, final Insets margin )
    {
        return imageStartX + margin.left * zoom;
    }

    private int getContentEndX ( final int imageStartX, final int iw, final Insets margin )
    {
        return imageStartX + iw - margin.right * zoom;
    }

    private int getContentStartY ( final int imageStartY, final Insets margin )
    {
        return imageStartY + margin.top * zoom;
    }

    private int getContentEndY ( final int imageStartY, final int ih, final Insets margin )
    {
        return imageStartY + ih - margin.bottom * zoom;
    }

    public List<ChangeListener> getChangeListeners ()
    {
        return changeListeners;
    }

    public void addChangeListener ( final ChangeListener changeListener )
    {
        changeListeners.add ( changeListener );
    }

    public void removeChangeListener ( final ChangeListener changeListener )
    {
        changeListeners.add ( changeListener );
    }

    private void fireStateChanged ()
    {
        final ChangeEvent changeEvent = new ChangeEvent ( NinePatchEditor.this );
        for ( final ChangeListener listener : CollectionUtils.copy ( changeListeners ) )
        {
            listener.stateChanged ( changeEvent );
        }
    }

    public List<ZoomChangeListener> getZoomChangeListeners ()
    {
        return zoomChangeListeners;
    }

    public void addZoomChangeListener ( final ZoomChangeListener zoomChangeListener )
    {
        zoomChangeListeners.add ( zoomChangeListener );
    }

    public void removeZoomChangeListener ( final ZoomChangeListener zoomChangeListener )
    {
        zoomChangeListeners.add ( zoomChangeListener );
    }

    private void fireZoomChanged ()
    {
        for ( final ZoomChangeListener listener : CollectionUtils.copy ( zoomChangeListeners ) )
        {
            listener.zoomChanged ();
        }
    }

    @Override
    public Dimension getPreferredSize ()
    {
        return SizeMethodsImpl.getPreferredSize ( this, getActualPreferredSize () );
    }

    @Override
    public Dimension getOriginalPreferredSize ()
    {
        return SizeMethodsImpl.getOriginalPreferredSize ( this, getActualPreferredSize () );
    }

    public Dimension getActualPreferredSize ()
    {
        // todo Should be in LaF for NinePatchEditor
        final boolean imageExists = ninePatchImage != null;
        final int iw = imageExists ? ( ninePatchImage.getWidth () + 2 ) * zoom : 400;
        final int ih = imageExists ? ( ninePatchImage.getHeight () + 2 ) * zoom : 400;
        return new Dimension ( RULER_LENGTH + iw + ADDITIONAL_SPACE, RULER_LENGTH + ih + ADDITIONAL_SPACE );
    }
}