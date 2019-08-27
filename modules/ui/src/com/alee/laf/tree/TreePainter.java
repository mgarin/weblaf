package com.alee.laf.tree;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.api.jdk.Predicate;
import com.alee.extended.tree.WebAsyncTree;
import com.alee.extended.tree.WebExTree;
import com.alee.managers.language.Language;
import com.alee.managers.language.LanguageListener;
import com.alee.managers.language.LanguageSensitive;
import com.alee.managers.language.UILanguageManager;
import com.alee.managers.style.BoundsType;
import com.alee.painter.DefaultPainter;
import com.alee.painter.PainterSupport;
import com.alee.painter.SectionPainter;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.IDecorationPainter;
import com.alee.utils.CollectionUtils;
import com.alee.utils.GeometryUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.TreeUI;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.*;

/**
 * Basic painter for {@link JTree} component.
 * It is used as {@link WTreeUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */
public class TreePainter<C extends JTree, U extends WTreeUI, D extends IDecoration<C, D>> extends AbstractDecorationPainter<C, U, D>
        implements ITreePainter<C, U>
{
    /**
     * Style settings.
     */
    protected boolean paintLines;
    protected boolean dashedLines;
    protected Color linesColor;

    /**
     * {@link SectionPainter} that can be used to customize tree rows background.
     * It is separated from {@link #nodePainter} as nodes not always take the whole row space.
     */
    @DefaultPainter ( TreeRowPainter.class )
    protected ITreeRowPainter rowPainter;

    /**
     * {@link SectionPainter} that can be used to customize tree nodes background.
     * It is separated from {@link #rowPainter} as nodes not always take the whole row space.
     */
    @DefaultPainter ( TreeNodePainter.class )
    protected ITreeNodePainter nodePainter;

    /**
     * {@link SectionPainter} that can be used to customize tree nodes selection background.
     * Separate painter is used because selection could include multiple nodes in some selection modes.
     * This painter will not know any information about the nodes, use {@link #nodePainter} for painting node-specific decorations.
     * You can also use {@link #rowPainter} for row-wide background customization that is aware of the row node contents.
     */
    @DefaultPainter ( TreeSelectionPainter.class )
    protected ITreeSelectionPainter selectionPainter;

    /**
     * Tree drop location painter.
     * Provides visual representation for drag-and-drop operation on tree nodes.
     */
    @DefaultPainter ( TreeDropLocationPainter.class )
    protected ITreeDropLocationPainter dropLocationPainter;

    /**
     * Tree nodes selector painter.
     * It can be provided to enable nodes multiselector.
     */
    @DefaultPainter ( TreeSelectorPainter.class )
    protected ITreeSelectorPainter selectorPainter;

    /**
     * Listeners.
     */
    protected transient TreeSelectionListener treeSelectionListener;
    protected transient TreeExpansionListener treeExpansionListener;
    protected transient MouseAdapter mouseAdapter;
    protected transient LanguageListener languageSensitive;

    /**
     * Runtime variables.
     */
    protected transient List<Integer> initialSelection = new ArrayList<Integer> ();
    protected transient Point selectionStart = null;
    protected transient Point selectionEnd = null;
    protected transient TreePath draggablePath = null;

    /**
     * Painting variables.
     */
    protected transient int totalChildIndent;
    protected transient int depthOffset;
    protected transient TreeModel treeModel;
    protected transient AbstractLayoutCache treeLayoutCache;
    protected transient Hashtable<TreePath, Boolean> paintingCache;
    protected transient CellRendererPane rendererPane;
    protected transient TreeCellRenderer currentCellRenderer;
    protected transient int editingRow = -1;
    protected transient int lastSelectionRow = -1;

    @Nullable
    @Override
    protected List<SectionPainter<C, U>> getSectionPainters ()
    {
        return asList ( rowPainter, nodePainter, selectionPainter, dropLocationPainter, selectorPainter );
    }

    @Override
    protected void installPropertiesAndListeners ()
    {
        super.installPropertiesAndListeners ();
        installTreeSelectionListeners ();
        installTreeExpansionListeners ();
        installTreeMouseListeners ();
        installLanguageListeners ();
    }

    @Override
    protected void uninstallPropertiesAndListeners ()
    {
        uninstallLanguageListeners ();
        uninstallTreeMouseListeners ();
        uninstallTreeExpansionListeners ();
        uninstallTreeSelectionListeners ();
        super.uninstallPropertiesAndListeners ();
    }

    /**
     * Installs custom {@link TreeSelectionListener} for complex selections update.
     */
    protected void installTreeSelectionListeners ()
    {
        treeSelectionListener = new TreeSelectionListener ()
        {
            @Override
            public void valueChanged ( @NotNull final TreeSelectionEvent e )
            {
                // Ensure component is still available
                // This might happen if painter is replaced from another TreeSelectionListener
                if ( component != null )
                {
                    // Optimized selection repaint
                    repaintSelection ();
                }
            }
        };
        component.addTreeSelectionListener ( treeSelectionListener );
    }

    /**
     * Uninstalls custom {@link TreeSelectionListener}.
     */
    protected void uninstallTreeSelectionListeners ()
    {
        component.removeTreeSelectionListener ( treeSelectionListener );
        treeSelectionListener = null;
    }

    /**
     * Installs custom {@link TreeExpansionListener} for complex selections update.
     */
    protected void installTreeExpansionListeners ()
    {
        treeExpansionListener = new TreeExpansionListener ()
        {
            @Override
            public void treeExpanded ( @NotNull final TreeExpansionEvent event )
            {
                // Ensure component is still available
                // This might happen if painter is replaced from another TreeExpansionListener
                if ( component != null )
                {
                    repaintSelection ();
                }
            }

            @Override
            public void treeCollapsed ( @NotNull final TreeExpansionEvent event )
            {
                // Ensure component is still available
                // This might happen if painter is replaced from another TreeExpansionListener
                if ( component != null )
                {
                    repaintSelection ();
                }
            }
        };
        component.addTreeExpansionListener ( treeExpansionListener );
    }

    /**
     * Uninstalls custom {@link TreeExpansionListener}.
     */
    protected void uninstallTreeExpansionListeners ()
    {
        component.removeTreeExpansionListener ( treeExpansionListener );
        treeExpansionListener = null;
    }

    /**
     * Installs custom {@link MouseAdapter}.
     */
    protected void installTreeMouseListeners ()
    {
        mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mousePressed ( @NotNull final MouseEvent e )
            {
                // Ensure component is still available
                // This might happen if painter is replaced from another MouseListener
                if ( component != null )
                {
                    // Only left mouse button events
                    if ( SwingUtilities.isLeftMouseButton ( e ) )
                    {
                        // CTRL/SHIFT modifiers are not active
                        final boolean ctrl = SwingUtils.isCtrl ( e );
                        final boolean shift = SwingUtils.isShift ( e );

                        // Drag is not available
                        final boolean noDrag = !component.getDragEnabled () || component.getTransferHandler () == null;

                        // Mouse did not hit actual tree node space
                        final boolean notInNodeSpace = ui.getExactRowForLocation ( e.getPoint (), false ) == -1;

                        // Handling additional events for full-line selection
                        if ( notInNodeSpace && isFullLineSelection () )
                        {
                            // Avoiding selection start when pressed on tree expand handle
                            // todo Checkbox condition should be in a different ui/painter and not in basic one
                            // todo Collapse node expansion on double-click if it is expanded and clicked on full line (on release)
                            final TreePath path = ui.getClosestPathForLocation ( component, e.getX (), e.getY () );
                            if ( path != null && !isLocationInExpandControl ( path, e.getX (), e.getY () ) &&
                                    !ui.isLocationInCheckBoxControl ( path, e.getX (), e.getY () ) )
                            {
                                // Updating selection
                                final int clickRow = ui.getExactRowForLocation ( e.getPoint (), true );
                                switch ( component.getSelectionModel ().getSelectionMode () )
                                {
                                    case TreeSelectionModel.SINGLE_TREE_SELECTION:
                                    {
                                        if ( ctrl && component.isRowSelected ( clickRow ) )
                                        {
                                            // Toggle selection
                                            component.clearSelection ();
                                        }
                                        else
                                        {
                                            // Change selection to one row
                                            component.setSelectionRow ( clickRow );
                                        }
                                        break;
                                    }
                                    case TreeSelectionModel.CONTIGUOUS_TREE_SELECTION:
                                    {
                                        if ( shift )
                                        {
                                            // Collecting new selection range
                                            final int anchorRow = component.getRowForPath ( component.getAnchorSelectionPath () );
                                            final List<Integer> selected = CollectionUtils.intRange ( anchorRow, clickRow );

                                            // Making sure we provide a proper lead selection row
                                            selected.remove ( 0 );
                                            selected.add ( anchorRow );

                                            // Updating selected rows
                                            component.setSelectionRows ( CollectionUtils.toIntArray ( selected ) );
                                        }
                                        else if ( ctrl )
                                        {
                                            // Toggle selection
                                            if ( component.isRowSelected ( clickRow ) )
                                            {
                                                component.removeSelectionRow ( clickRow );
                                            }
                                            else
                                            {
                                                component.addSelectionRow ( clickRow );
                                            }
                                        }
                                        else
                                        {
                                            // Change selection to one row
                                            component.setSelectionRow ( clickRow );
                                        }
                                        break;
                                    }
                                    case TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION:
                                    {
                                        if ( shift )
                                        {
                                            // Collecting new selection range
                                            final int anchorRow = component.getRowForPath ( component.getAnchorSelectionPath () );
                                            final List<Integer> selected = CollectionUtils.intRange ( anchorRow, clickRow );

                                            // Adding all previously selected rows whenever CTRL modifier is pressed
                                            if ( ctrl )
                                            {
                                                final int[] selectionRows = component.getSelectionRows ();
                                                if ( selectionRows != null )
                                                {
                                                    CollectionUtils.addUnique ( selected, selectionRows );
                                                }
                                            }

                                            // Making sure we provide a proper lead selection row
                                            selected.remove ( 0 );
                                            selected.add ( anchorRow );

                                            // Updating selected rows
                                            component.setSelectionRows ( CollectionUtils.toIntArray ( selected ) );
                                        }
                                        else if ( ctrl )
                                        {
                                            // Toggle selection
                                            if ( component.isRowSelected ( clickRow ) )
                                            {
                                                component.removeSelectionRow ( clickRow );
                                            }
                                            else
                                            {
                                                component.addSelectionRow ( clickRow );
                                            }
                                        }
                                        else
                                        {
                                            // Change selection to one row
                                            component.setSelectionRow ( clickRow );
                                        }
                                        break;
                                    }
                                }

                                // Marking row to be dragged
                                final int rowForPath = ui.getRowForPath ( component, path );
                                if ( isDragAvailable () && ui.getRowBounds ( rowForPath ).contains ( e.getX (), e.getY () ) &&
                                        component.isRowSelected ( rowForPath ) )
                                {
                                    draggablePath = path;
                                }
                            }
                        }

                        // Activating tree nodes selector
                        if ( !ctrl && !shift && ( noDrag || notInNodeSpace ) && isSelectorAvailable () )
                        {
                            // Avoiding selection start when pressed on tree expand handle
                            // todo This should occur on actual mouse drag, not press
                            final TreePath path = ui.getClosestPathForLocation ( component, e.getX (), e.getY () );
                            if ( path == null || !isLocationInExpandControl ( path, e.getX (), e.getY () ) &&
                                    !ui.isLocationInCheckBoxControl ( path, e.getX (), e.getY () ) )
                            {
                                // Avoid starting multiselection if row is selected and drag is possible
                                final int rowForPath = ui.getRowForPath ( component, path );
                                if ( isDragAvailable () && rowForPath != -1 &&
                                        ui.getRowBounds ( rowForPath ).contains ( e.getX (), e.getY () ) &&
                                        component.isRowSelected ( rowForPath ) )
                                {
                                    // Marking row to be dragged
                                    draggablePath = path;
                                }
                                else
                                {
                                    // Selection
                                    selectionStart = e.getPoint ();
                                    selectionEnd = selectionStart;

                                    // Initial tree selection
                                    initialSelection = getSelectedRows ();

                                    // Updating selection
                                    validateSelection ( e );

                                    // Repainting selection on the tree
                                    repaintSelector ();
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void mouseDragged ( @NotNull final MouseEvent e )
            {
                // Ensure component is still available
                // This might happen if painter is replaced from another MouseMotionListener
                if ( component != null )
                {
                    if ( draggablePath != null )
                    {
                        final TransferHandler transferHandler = component.getTransferHandler ();
                        transferHandler.exportAsDrag ( component, e, transferHandler.getSourceActions ( component ) );
                        draggablePath = null;
                    }
                    if ( isSelectorAvailable () && selectionStart != null )
                    {
                        // Selection
                        selectionEnd = e.getPoint ();

                        // Updating selection
                        validateSelection ( e );

                        // Repainting selection on the tree
                        repaintSelector ();

                        if ( !component.getVisibleRect ().contains ( e.getPoint () ) )
                        {
                            component.scrollRectToVisible ( new Rectangle ( e.getPoint (), new Dimension ( 0, 0 ) ) );
                        }
                    }
                }
            }

            @Override
            public void mouseReleased ( @NotNull final MouseEvent e )
            {
                // Ensure component is still available
                // This might happen if painter is replaced from another MouseListener
                if ( component != null )
                {
                    if ( draggablePath != null )
                    {
                        draggablePath = null;
                    }
                    if ( isSelectorAvailable () && selectionStart != null )
                    {
                        // Saving selection rect to repaint
                        // Rectangle fr = GeometryUtils.getContainingRect ( selectionStart, selectionEnd );

                        // Selection
                        selectionStart = null;
                        selectionEnd = null;

                        // Repainting selection on the tree
                        repaintSelector ( /*fr*/ );
                    }
                }
            }

            /**
             * Performs selection validation and updates.
             * todo Modify selection instead of overwriting each time?
             *
             * @param e mouse event
             */
            private void validateSelection ( @NotNull final MouseEvent e )
            {
                // Selection rect
                final Rectangle selection = GeometryUtils.getContainingRect ( selectionStart, selectionEnd );

                // Compute new selection
                final List<Integer> newSelection = new ArrayList<Integer> ();
                if ( SwingUtils.isShift ( e ) )
                {
                    for ( int row = 0; row < component.getRowCount (); row++ )
                    {
                        if ( ui.getRowBounds ( row ).intersects ( selection ) && !initialSelection.contains ( row ) )
                        {
                            newSelection.add ( row );
                        }
                    }
                    newSelection.addAll ( initialSelection );
                }
                else if ( SwingUtils.isCtrl ( e ) )
                {
                    final List<Integer> excludedRows = new ArrayList<Integer> ();
                    for ( int row = 0; row < component.getRowCount (); row++ )
                    {
                        if ( ui.getRowBounds ( row ).intersects ( selection ) )
                        {
                            if ( initialSelection.contains ( row ) )
                            {
                                excludedRows.add ( row );
                            }
                            else
                            {
                                newSelection.add ( row );
                            }
                        }
                    }
                    for ( final int row : initialSelection )
                    {
                        if ( !excludedRows.contains ( row ) )
                        {
                            newSelection.add ( row );
                        }
                    }
                }
                else
                {
                    for ( int row = 0; row < component.getRowCount (); row++ )
                    {
                        if ( ui.getRowBounds ( row ).intersects ( selection ) )
                        {
                            newSelection.add ( row );
                        }
                    }
                }

                // Change selection if it is not the same as before
                if ( !CollectionUtils.equals ( getSelectedRows (), newSelection, true ) )
                {
                    if ( newSelection.size () > 0 )
                    {
                        component.setSelectionRows ( CollectionUtils.toIntArray ( newSelection ) );
                    }
                    else
                    {
                        component.clearSelection ();
                    }
                }
            }

            /**
             * Returns selected rows list.
             *
             * @return selected rows list
             */
            @NotNull
            private List<Integer> getSelectedRows ()
            {
                final List<Integer> selection = new ArrayList<Integer> ();
                final int[] selectionRows = component.getSelectionRows ();
                if ( selectionRows != null )
                {
                    for ( final int row : selectionRows )
                    {
                        selection.add ( row );
                    }
                }
                return selection;
            }

            /**
             * Repaints tree selector.
             * Replaced with full repaint due to strange tree lines painting bug.
             */
            private void repaintSelector ()
            {
                component.repaint ( component.getVisibleRect () );
            }
        };
        component.addMouseListener ( mouseAdapter );
        component.addMouseMotionListener ( mouseAdapter );
    }

    /**
     * Uninstalls custom {@link MouseAdapter}.
     */
    protected void uninstallTreeMouseListeners ()
    {
        component.removeMouseMotionListener ( mouseAdapter );
        component.removeMouseListener ( mouseAdapter );
        mouseAdapter = null;
    }

    /**
     * Installs language listeners.
     */
    protected void installLanguageListeners ()
    {
        languageSensitive = new LanguageListener ()
        {
            @Override
            public void languageChanged ( @NotNull final Language oldLanguage, @NotNull final Language newLanguage )
            {
                if ( isLanguageSensitive () )
                {
                    if ( component.getRowCount () > 0 )
                    {
                        // Forcing node sizes update within tree
                        final TreeUI ui = component.getUI ();
                        if ( ui instanceof WTreeUI )
                        {
                            // Asking UI to update node sizes
                            ( ( WTreeUI ) ui ).updateNodeSizes ();
                        }
                        else
                        {
                            // Simply repainting tree when we don't have tools to update it properly
                            component.repaint ();
                        }
                    }
                }
            }
        };
        UILanguageManager.addLanguageListener ( component, languageSensitive );
    }

    /**
     * Returns whether or not tree is language-sensitive.
     * Tree is language-sensitive if either tree, its renderer, model, data provider or any of the nodes are language-sensitive.
     *
     * @return {@code true} if tree is language-sensitive, {@code false} otherwise
     */
    protected boolean isLanguageSensitive ()
    {
        return component instanceof LanguageSensitive ||
                component.getCellRenderer () instanceof LanguageSensitive ||
                component.getModel () instanceof LanguageSensitive ||
                component instanceof WebExTree && ( ( WebExTree ) component ).getDataProvider () instanceof LanguageSensitive ||
                component instanceof WebAsyncTree && ( ( WebAsyncTree ) component ).getDataProvider () instanceof LanguageSensitive ||
                TreeUtils.getTreeWalker ( component ).anyMatch ( new Predicate<TreeNode> ()
                {
                    @Override
                    public boolean test ( final TreeNode treeNode )
                    {
                        return treeNode instanceof LanguageSensitive;
                    }
                } );
    }

    /**
     * Uninstalls language listeners.
     */
    protected void uninstallLanguageListeners ()
    {
        UILanguageManager.removeLanguageListener ( component, languageSensitive );
        languageSensitive = null;
    }

    @Override
    protected void propertyChanged ( @NotNull final String property, @Nullable final Object oldValue, @Nullable final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChanged ( property, oldValue, newValue );

        // Update visual drop location
        if ( Objects.equals ( property, WebTree.DROP_LOCATION ) && dropLocationPainter != null )
        {
            // Repainting previous drop location
            final JTree.DropLocation oldLocation = ( JTree.DropLocation ) oldValue;
            if ( oldLocation != null && oldLocation.getPath () != null )
            {
                component.repaint ( dropLocationPainter.getDropViewBounds ( oldLocation ) );
            }

            // Repainting current drop location
            final JTree.DropLocation newLocation = ( JTree.DropLocation ) newValue;
            if ( newLocation != null && newLocation.getPath () != null )
            {
                component.repaint ( dropLocationPainter.getDropViewBounds ( newLocation ) );
            }
        }
    }

    @Override
    public boolean isRowHoverDecorationSupported ()
    {
        boolean supported = false;
        if ( component != null && component.isEnabled () )
        {
            if ( rowPainter != null && rowPainter instanceof IDecorationPainter )
            {
                supported = ( ( IDecorationPainter ) rowPainter ).usesState ( DecorationState.hover );
            }
            if ( !supported && nodePainter != null && nodePainter instanceof IDecorationPainter )
            {
                supported = ( ( IDecorationPainter ) nodePainter ).usesState ( DecorationState.hover );
            }
        }
        return supported;
    }

    @Override
    public void prepareToPaint ( final Hashtable<TreePath, Boolean> paintingCache, final TreeCellRenderer currentCellRenderer )
    {
        this.paintingCache = paintingCache;
        this.currentCellRenderer = currentCellRenderer;
    }

    @Override
    protected void paintContent ( @NotNull final Graphics2D g2d, @NotNull final C c, @NotNull final U ui, @NotNull final Rectangle bounds )
    {
        // Updating tree layout cache
        treeLayoutCache = ui.getTreeLayoutCache ();

        // Paint tree content only if cache exists
        if ( treeLayoutCache != null )
        {
            // Preparing to paint tree
            treeModel = component.getModel ();
            totalChildIndent = ui.getLeftChildIndent () + ui.getRightChildIndent ();
            rendererPane = ui.getCellRendererPane ();
            lastSelectionRow = component.getLeadSelectionRow ();
            final TreePath editingPath = component.getEditingPath ();
            editingRow = editingPath != null ? component.getRowForPath ( editingPath ) : -1;
            updateDepthOffset ();

            // Painting tree background
            paintBackground ( g2d );

            // Painting selected nodes background
            paintSelectedNodesBackground ( g2d );

            // Painting tree
            paintTree ( g2d );

            // Painting drop location
            paintDropLocation ( g2d );

            // Multiselector
            paintMultiselector ( g2d );
        }

        // Cleaning up
        treeModel = null;
        treeLayoutCache = null;
        paintingCache = null;
        rendererPane = null;
    }

    /**
     * Paints tree background.
     *
     * @param g2d graphics context
     */
    protected void paintBackground ( @NotNull final Graphics2D g2d )
    {
        // Painting row background if one is available
        if ( rowPainter != null || nodePainter != null )
        {
            final Rectangle paintBounds = g2d.getClipBounds ();
            final TreePath initialPath = ui.getClosestPathForLocation ( component, 0, paintBounds.y );
            final Enumeration paintingEnumerator = treeLayoutCache.getVisiblePathsFrom ( initialPath );

            if ( initialPath != null && paintingEnumerator != null )
            {
                final Insets insets = component.getInsets ();
                final int endY = paintBounds.y + paintBounds.height;
                final Rectangle boundsBuffer = new Rectangle ();

                Rectangle bounds;
                TreePath path;
                int row = treeLayoutCache.getRowForPath ( initialPath );
                while ( paintingEnumerator.hasMoreElements () )
                {
                    path = ( TreePath ) paintingEnumerator.nextElement ();
                    if ( path != null )
                    {
                        bounds = getPathBounds ( path, insets, boundsBuffer );
                        if ( bounds == null )
                        {
                            // Note from Swing devs:
                            // This will only happen if the model changes out from under us (usually in another thread)
                            // Swing isn't multi-threaded, but I'll put this check in anyway
                            break;
                        }

                        // Painting row background
                        if ( rowPainter != null )
                        {
                            // We have to ensure we can retrieve bounds for it first
                            final Rectangle rowBounds = ui.getRowBounds ( row, true );
                            if ( rowBounds != null )
                            {
                                // This is a workaround to make sure row painter takes the whole tree width
                                final Insets padding = PainterSupport.getPadding ( component );
                                if ( padding != null )
                                {
                                    // Increasing background by the padding sizes at left and right sides
                                    rowBounds.x -= padding.left;
                                    rowBounds.width += padding.left + padding.right;
                                }

                                // Painting row background
                                rowPainter.prepareToPaint ( row );
                                paintSection ( rowPainter, g2d, rowBounds );
                            }
                        }

                        // Painting node background
                        if ( nodePainter != null )
                        {
                            // We have to ensure we can retrieve bounds for it first
                            final Rectangle nodeBounds = ui.getRowBounds ( row );
                            if ( nodeBounds != null )
                            {
                                // Painting hover node background
                                nodePainter.prepareToPaint ( row );
                                paintSection ( nodePainter, g2d, nodeBounds );
                            }
                        }

                        if ( bounds.y + bounds.height >= endY )
                        {
                            break;
                        }
                    }
                    else
                    {
                        break;
                    }
                    row++;
                }
            }
        }
    }

    /**
     * Paints centered icon.
     *
     * @param c    component
     * @param g2d  graphics
     * @param icon icon
     * @param x    X coordinate
     * @param y    Y coordinate
     */
    protected void paintCentered ( @NotNull final Component c, @NotNull final Graphics2D g2d,
                                   @NotNull final Icon icon, final int x, final int y )
    {
        final int ix = findCenteredX ( x, icon.getIconWidth () );
        final int iy = y - icon.getIconHeight () / 2;
        icon.paintIcon ( c, g2d, ix, iy );
    }

    /**
     * Returns centered x coordinate for the icon.
     *
     * @param x         X coordinate
     * @param iconWidth icon width
     * @return centered x coordinate
     */
    protected int findCenteredX ( final int x, final int iconWidth )
    {
        return ltr ? x - ( int ) Math.ceil ( iconWidth / 2.0 ) : x - ( int ) Math.floor ( iconWidth / 2.0 );
    }

    /**
     * Paints special WebLaF tree nodes selection.
     * It is rendered separately from nodes allowing you to simplify your tree cell renderer component.
     *
     * @param g2d graphics context
     */
    protected void paintSelectedNodesBackground ( @NotNull final Graphics2D g2d )
    {
        if ( selectionPainter != null && component.getSelectionCount () > 0 && ui.getSelectionStyle () != TreeSelectionStyle.none )
        {
            // Painting selections
            final List<Rectangle> selections = getSelectionRects ();
            for ( final Rectangle rect : selections )
            {
                // Painting single selection
                paintSection ( selectionPainter, g2d, rect );
            }
        }
    }

    /**
     * Returns list of tree selections bounds.
     * This method takes selection style into account.
     *
     * @return list of tree selections bounds
     */
    @NotNull
    protected List<Rectangle> getSelectionRects ()
    {
        final List<Rectangle> selections;
        if ( ui.getSelectionStyle () != TreeSelectionStyle.none )
        {
            final int[] indices = component.getSelectionRows ();
            if ( indices != null && indices.length != 0 )
            {
                // Sorting selected rows
                Arrays.sort ( indices );

                // Calculating selection rects
                selections = new ArrayList<Rectangle> ( indices.length );
                final Insets insets = component.getInsets ();
                Rectangle maxRect = null;
                int lastRow = -1;
                for ( final int index : indices )
                {
                    if ( ui.getSelectionStyle () == TreeSelectionStyle.single )
                    {
                        // Required bounds
                        selections.add ( component.getRowBounds ( index ) );
                    }
                    else
                    {
                        if ( lastRow != -1 && lastRow + 1 != index )
                        {
                            // Save determined group
                            selections.add ( maxRect );

                            // Reset counting
                            maxRect = null;
                            lastRow = -1;
                        }
                        if ( lastRow == -1 || lastRow + 1 == index )
                        {
                            // Required bounds
                            final Rectangle b = component.getRowBounds ( index );

                            // Increasing bounds to cover whole line
                            if ( isFullLineSelection () )
                            {
                                b.x = insets.left;
                                b.width = component.getWidth () - insets.left - insets.right;
                            }

                            // Increase rect
                            maxRect = lastRow == -1 ? b : GeometryUtils.getContainingRect ( maxRect, b );

                            // Remember last row
                            lastRow = index;
                        }
                    }
                }
                if ( maxRect != null )
                {
                    selections.add ( maxRect );
                }
            }
            else
            {
                // Return empty selection when nothing is selected
                selections = Collections.emptyList ();
            }
        }
        else
        {
            // Return empty selection rects when custom selection painting is disabled
            selections = Collections.emptyList ();
        }
        return selections;
    }

    /**
     * Repaints all rectangles containing tree selections.
     * This method is optimized to repaint only those area which are actually have selection in them.
     */
    protected void repaintSelection ()
    {
        if ( component.getSelectionCount () > 0 )
        {
            for ( final Rectangle rect : getSelectionRects () )
            {
                component.repaint ( rect );
            }
        }
    }

    /**
     * Paints all base tree elements.
     * This is almost the same method as in BasicTreeUI but it doesn't paint drop line.
     *
     * @param g2d graphics context
     */
    protected void paintTree ( @NotNull final Graphics2D g2d )
    {
        final Rectangle paintBounds = g2d.getClipBounds ();
        final Insets insets = component.getInsets ();
        final TreePath initialPath = ui.getClosestPathForLocation ( component, 0, paintBounds.y );
        final Enumeration paintingEnumerator = treeLayoutCache.getVisiblePathsFrom ( initialPath );
        final int endY = paintBounds.y + paintBounds.height;
        int row = treeLayoutCache.getRowForPath ( initialPath );

        paintingCache.clear ();

        if ( initialPath != null && paintingEnumerator != null )
        {
            TreePath parentPath = initialPath;

            // Paint the lines, knobs, and rows

            // Find each parent and have them paint a line to their last child
            parentPath = parentPath.getParentPath ();
            while ( parentPath != null )
            {
                paintVerticalPartOfLeg ( g2d, paintBounds, insets, parentPath );
                paintingCache.put ( parentPath, Boolean.TRUE );
                parentPath = parentPath.getParentPath ();
            }

            // Information for the node being rendered.
            final Rectangle boundsBuffer = new Rectangle ();
            final boolean rootVisible = isRootVisible ();
            boolean isExpanded;
            boolean hasBeenExpanded;
            boolean isLeaf;
            Rectangle bounds;
            TreePath path;

            while ( paintingEnumerator.hasMoreElements () )
            {
                path = ( TreePath ) paintingEnumerator.nextElement ();
                if ( path != null )
                {
                    isLeaf = treeModel.isLeaf ( path.getLastPathComponent () );
                    if ( isLeaf )
                    {
                        isExpanded = hasBeenExpanded = false;
                    }
                    else
                    {
                        isExpanded = treeLayoutCache.getExpandedState ( path );
                        hasBeenExpanded = component.hasBeenExpanded ( path );
                    }

                    bounds = getPathBounds ( path, insets, boundsBuffer );
                    if ( bounds == null )
                    {
                        // Note from Swing devs:
                        // This will only happen if the model changes out from under us (usually in another thread).
                        // Swing isn't multi-threaded, but I'll put this check in anyway.
                        break;
                    }

                    // See if the vertical line to the parent has been painted
                    parentPath = path.getParentPath ();
                    if ( parentPath != null )
                    {
                        if ( paintingCache.get ( parentPath ) == null )
                        {
                            paintVerticalPartOfLeg ( g2d, paintBounds, insets, parentPath );
                            paintingCache.put ( parentPath, Boolean.TRUE );
                        }
                        paintHorizontalPartOfLeg ( g2d, paintBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf );
                    }
                    else if ( rootVisible && row == 0 )
                    {
                        paintHorizontalPartOfLeg ( g2d, paintBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf );
                    }
                    if ( shouldPaintExpandControl ( path, row, isExpanded, hasBeenExpanded, isLeaf ) )
                    {
                        paintExpandControl ( g2d, paintBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf );
                    }
                    paintRow ( g2d, paintBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf );
                    if ( bounds.y + bounds.height >= endY )
                    {
                        break;
                    }
                }
                else
                {
                    break;
                }
                row++;
            }
        }

        // Empty out the renderer pane, allowing renderers to be gc'ed.
        rendererPane.removeAll ();
    }

    /**
     * Returns whether or not {@code mouseX} and {@code mouseY} fall in the area of row that is used to expand/collapse the node and the
     * node at {@code row} does not represent a leaf.
     *
     * @param path   tree path
     * @param mouseX mouse X location
     * @param mouseY mouse Y location
     * @return {@code true} if {@code mouseX} and {@code mouseY} fall in the area of row that is used to expand/collapse the node and the
     * node at {@code row} does not represent a leaf, {@code false} otherwise
     */
    protected boolean isLocationInExpandControl ( @Nullable final TreePath path, final int mouseX, final int mouseY )
    {
        final boolean inExpandControl;
        if ( path != null && !component.getModel ().isLeaf ( path.getLastPathComponent () ) )
        {
            final int boxWidth = ui.getExpandedIcon () != null ? ui.getExpandedIcon ().getIconWidth () : 8;
            final Insets i = component.getInsets ();

            int boxLeftX = getRowX ( component.getRowForPath ( path ), path.getPathCount () - 1 );

            boxLeftX = ltr ? boxLeftX + i.left - ui.getRightChildIndent () + 1 :
                    component.getWidth () - boxLeftX - i.right + ui.getRightChildIndent () - 1;

            boxLeftX = findCenteredX ( boxLeftX, boxWidth );

            inExpandControl = mouseX >= boxLeftX && mouseX < boxLeftX + boxWidth;
        }
        else
        {
            inExpandControl = false;
        }
        return inExpandControl;
    }

    /**
     * Paints the expand (toggle) part of a row.
     * The receiver should NOT modify {@code clipBounds}, or {@code insets}.
     *
     * @param g2d             graphics context
     * @param clipBounds      clip bounds
     * @param insets          tree insets
     * @param bounds          tree path bounds
     * @param path            tree path
     * @param row             row index
     * @param isExpanded      whether row is expanded or not
     * @param hasBeenExpanded whether row has been expanded once before or not
     * @param isLeaf          whether node is leaf or not
     */
    protected void paintExpandControl ( @NotNull final Graphics2D g2d, @NotNull final Rectangle clipBounds, @NotNull final Insets insets,
                                        @NotNull final Rectangle bounds, @NotNull final TreePath path, final int row,
                                        final boolean isExpanded, final boolean hasBeenExpanded, final boolean isLeaf )
    {
        final Object value = path.getLastPathComponent ();

        // Paint icons if not a leaf and either hasn't been loaded,
        // or the model child count is > 0.
        if ( !isLeaf && ( !hasBeenExpanded || treeModel.getChildCount ( value ) > 0 ) )
        {
            final int middleXOfKnob;
            if ( ltr )
            {
                middleXOfKnob = bounds.x - ui.getRightChildIndent () + 1;
            }
            else
            {
                middleXOfKnob = bounds.x + bounds.width + ui.getRightChildIndent () - 1;
            }
            final int middleYOfKnob = bounds.y + bounds.height / 2;

            if ( isExpanded )
            {
                final Icon expandedIcon = ui.getExpandedIcon ();
                if ( expandedIcon != null )
                {
                    paintCentered ( component, g2d, expandedIcon, middleXOfKnob, middleYOfKnob );
                }
            }
            else
            {
                final Icon collapsedIcon = ui.getCollapsedIcon ();
                if ( collapsedIcon != null )
                {
                    paintCentered ( component, g2d, collapsedIcon, middleXOfKnob, middleYOfKnob );
                }
            }
        }
    }

    /**
     * Paints the renderer part of a row.
     * The receiver should NOT modify {@code clipBounds}, or {@code insets}.
     *
     * @param g2d             graphics context
     * @param clipBounds      clip bounds
     * @param insets          tree insets
     * @param bounds          tree path bounds
     * @param path            tree path
     * @param row             row index
     * @param isExpanded      whether row is expanded or not
     * @param hasBeenExpanded whether row has been expanded once before or not
     * @param isLeaf          whether node is leaf or not
     */
    protected void paintRow ( @NotNull final Graphics2D g2d, @NotNull final Rectangle clipBounds, @NotNull final Insets insets,
                              @NotNull final Rectangle bounds, @NotNull final TreePath path, final int row,
                              final boolean isExpanded, final boolean hasBeenExpanded, final boolean isLeaf )
    {
        // Don't paint the renderer if editing this row.
        if ( editingRow != row )
        {
            // Retrieving row cell renderer
            final Object value = path.getLastPathComponent ();
            final boolean hasFocus = ( component.hasFocus () ? lastSelectionRow : -1 ) == row;
            final boolean selected = component.isRowSelected ( row );
            final Component rowComponent =
                    currentCellRenderer.getTreeCellRendererComponent ( component, value, selected, isExpanded, isLeaf, row, hasFocus );

            // Painting cell renderer
            rendererPane.paintComponent ( g2d, rowComponent, component, bounds.x, bounds.y, bounds.width, bounds.height, true );
        }
    }

    /**
     * Returns whether or not the expand (toggle) control should be painted for the specified row.
     *
     * @param path            tree path
     * @param row             row index
     * @param isExpanded      whether row is expanded or not
     * @param hasBeenExpanded whether row has been expanded once before or not
     * @param isLeaf          whether node is leaf or not
     * @return {@code true} if the expand (toggle) control should be painted for the specified row, {@code false} otherwise
     */
    protected boolean shouldPaintExpandControl ( @NotNull final TreePath path, final int row, final boolean isExpanded,
                                                 final boolean hasBeenExpanded, final boolean isLeaf )
    {
        final boolean shouldPaint;
        if ( !isLeaf )
        {
            final int depth = path.getPathCount () - 1;
            shouldPaint = !( ( depth == 0 || depth == 1 && !isRootVisible () ) && !getShowsRootHandles () );
        }
        else
        {
            shouldPaint = false;
        }

        return shouldPaint;
    }

    /**
     * Returns whether or not root is visible.
     *
     * @return {@code true} if root is visible, {@code false} otherwise
     */
    protected boolean isRootVisible ()
    {
        return component != null && component.isRootVisible ();
    }

    /**
     * Returns whether or not root handles should be displayed.
     *
     * @return {@code true} if root handles should be displayed, {@code false} otherwise
     */
    protected boolean getShowsRootHandles ()
    {
        return component != null && component.getShowsRootHandles ();
    }

    /**
     * Paints the horizontal part of the leg.
     *
     * @param g2d             graphics
     * @param clipBounds      clip bounds
     * @param insets          tree insets
     * @param bounds          tree path bounds
     * @param path            tree path
     * @param row             row index
     * @param isExpanded      whether row is expanded or not
     * @param hasBeenExpanded whether row has been expanded once before or not
     * @param isLeaf          whether node is leaf or not
     */
    protected void paintHorizontalPartOfLeg ( @NotNull final Graphics2D g2d, @NotNull final Rectangle clipBounds,
                                              @NotNull final Insets insets, @NotNull final Rectangle bounds,
                                              @NotNull final TreePath path, final int row,
                                              final boolean isExpanded, final boolean hasBeenExpanded, final boolean isLeaf )
    {
        if ( paintLines )
        {
            // Don't paint the legs for the root node if the
            final int depth = path.getPathCount () - 1;
            if ( depth != 0 && ( depth != 1 || isRootVisible () ) || getShowsRootHandles () )
            {
                final int clipLeft = clipBounds.x;
                final int clipRight = clipBounds.x + clipBounds.width;
                final int clipTop = clipBounds.y;
                final int clipBottom = clipBounds.y + clipBounds.height;
                final int lineY = bounds.y + bounds.height / 2;

                if ( ltr )
                {
                    final int leftX = bounds.x - ui.getRightChildIndent ();
                    final int nodeX = bounds.x - getHorizontalLegIndent ();

                    if ( lineY >= clipTop && lineY < clipBottom && nodeX >= clipLeft && leftX < clipRight && leftX < nodeX )
                    {

                        g2d.setPaint ( linesColor );
                        paintHorizontalLine ( g2d, lineY, leftX, nodeX - 1 );
                    }
                }
                else
                {
                    final int nodeX = bounds.x + bounds.width + getHorizontalLegIndent ();
                    final int rightX = bounds.x + bounds.width + ui.getRightChildIndent ();

                    if ( lineY >= clipTop && lineY < clipBottom && rightX >= clipLeft && nodeX < clipRight && nodeX < rightX )
                    {

                        g2d.setPaint ( linesColor );
                        paintHorizontalLine ( g2d, lineY, nodeX, rightX - 1 );
                    }
                }
            }
        }
    }

    /**
     * Returns horizontal leg indent.
     *
     * @return horizontal leg indent
     */
    protected int getHorizontalLegIndent ()
    {
        return -2;
    }

    /**
     * Paints the vertical part of the leg.
     *
     * @param g2d        graphics
     * @param clipBounds clip bounds
     * @param insets     tree insets
     * @param path       tree path
     */
    protected void paintVerticalPartOfLeg ( @NotNull final Graphics2D g2d, @NotNull final Rectangle clipBounds,
                                            @NotNull final Insets insets, @NotNull final TreePath path )
    {
        if ( paintLines )
        {
            final int depth = path.getPathCount () - 1;
            if ( depth != 0 || getShowsRootHandles () || isRootVisible () )
            {
                int lineX = getRowX ( -1, depth + 1 );
                if ( ltr )
                {
                    lineX = lineX - ui.getRightChildIndent () + insets.left;
                }
                else
                {
                    lineX = component.getWidth () - lineX - insets.right + ui.getRightChildIndent () - 1;
                }
                final int clipLeft = clipBounds.x;
                final int clipRight = clipBounds.x + clipBounds.width - 1;

                if ( lineX >= clipLeft && lineX <= clipRight )
                {
                    final int clipTop = clipBounds.y;
                    final int clipBottom = clipBounds.y + clipBounds.height;
                    Rectangle parentBounds = getPathBounds ( path );
                    final Rectangle lastChildBounds = getPathBounds ( getLastChildPath ( path ) );

                    // Note from Swing devs:
                    // This shouldn't happen, but if the model is modified in another thread it is possible for this to happen.
                    // Swing isn't multi-threaded, but I'll add this check in anyway.
                    if ( lastChildBounds != null )
                    {
                        int top;

                        if ( parentBounds == null )
                        {
                            top = Math.max ( insets.top + getVerticalLegIndent (), clipTop );
                        }
                        else
                        {
                            top = Math.max ( parentBounds.y + parentBounds.height +
                                    getVerticalLegIndent (), clipTop );
                        }
                        if ( depth == 0 && !isRootVisible () )
                        {
                            if ( treeModel != null )
                            {
                                final Object root = treeModel.getRoot ();

                                if ( treeModel.getChildCount ( root ) > 0 )
                                {
                                    parentBounds = getPathBounds ( path.pathByAddingChild ( treeModel.getChild ( root, 0 ) ) );
                                    if ( parentBounds != null )
                                    {
                                        top = Math.max ( insets.top + getVerticalLegIndent (), parentBounds.y + parentBounds.height / 2 );
                                    }
                                }
                            }
                        }

                        final int bottom = Math.min ( lastChildBounds.y + lastChildBounds.height / 2, clipBottom );

                        if ( top <= bottom )
                        {
                            g2d.setPaint ( linesColor );
                            paintVerticalLine ( g2d, lineX, top, bottom );
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns a path to the last child of {@code parent}.
     *
     * @param parent parent tree path
     * @return path to the last child of {@code parent}
     */
    @Nullable
    protected TreePath getLastChildPath ( @NotNull final TreePath parent )
    {
        final TreePath lastChildPath;
        if ( treeModel != null )
        {
            final int childCount = treeModel.getChildCount ( parent.getLastPathComponent () );
            if ( childCount > 0 )
            {
                lastChildPath = parent.pathByAddingChild ( treeModel.getChild ( parent.getLastPathComponent (), childCount - 1 ) );
            }
            else
            {
                lastChildPath = null;
            }
        }
        else
        {
            lastChildPath = null;
        }
        return lastChildPath;
    }

    /**
     * Paints a vertical line.
     *
     * @param g2d graphics context
     * @param x   X coordinate
     * @param y1  start Y coordinate
     * @param y2  end Y coordinate
     */
    protected void paintVerticalLine ( @NotNull final Graphics2D g2d, final int x, final int y1, final int y2 )
    {
        if ( dashedLines )
        {
            paintDashedVerticalLine ( g2d, x, y1, y2 );
        }
        else
        {
            g2d.drawLine ( x, y1, x, y2 );
        }
    }

    /**
     * Paints dashed vertical line.
     * This method assumes that y1 &lt;= y2 always.
     * todo Change to proper stroke usage instead as this implementation is slow
     *
     * @param g2d graphics context
     * @param x   X coordinate
     * @param y1  start Y coordinate
     * @param y2  end Y coordinate
     */
    protected void paintDashedVerticalLine ( @NotNull final Graphics2D g2d, final int x, int y1, final int y2 )
    {
        // Painting only even coordinates helps join line segments so they appear as one line
        // This can be defeated by translating the Graphics2D by an odd amount
        y1 += y1 % 2;

        // Painting dashed line
        for ( int y = y1; y <= y2; y += 2 )
        {
            g2d.drawLine ( x, y, x, y );
        }
    }

    /**
     * Paints a horizontal line.
     *
     * @param g2d graphics context
     * @param y   Y coordinate
     * @param x1  start X coordinate
     * @param x2  end X coordinate
     */
    protected void paintHorizontalLine ( @NotNull final Graphics2D g2d, final int y, final int x1, final int x2 )
    {
        if ( dashedLines )
        {
            paintDashedHorizontalLine ( g2d, y, x1, x2 );
        }
        else
        {
            g2d.drawLine ( x1, y, x2, y );
        }
    }

    /**
     * Paints dashed horizontal line.
     * This method assumes that x1 &lt;= x2 always.
     * todo Change to proper stroke usage instead as this implementation is slow
     *
     * @param g2d graphics context
     * @param y   Y coordinate
     * @param x1  start X coordinate
     * @param x2  end X coordinate
     */
    protected void paintDashedHorizontalLine ( @NotNull final Graphics2D g2d, final int y, int x1, final int x2 )
    {
        // Painting only even coordinates helps join line segments so they appear as one line
        // This can be defeated by translating the Graphics2D by an odd amount
        x1 += x1 % 2 + ( ltr ? 0 : -1 );

        // Painting dashed line
        for ( int x = x1; x <= x2; x += 2 )
        {
            g2d.drawLine ( x, y, x, y );
        }
    }

    /**
     * Returns the location, along the x-axis, to render a particular row at. The return value does not include any Insets specified on the
     * JTree. This does not check for the validity of the row or depth, it is assumed to be correct and will not throw an Exception if the
     * row or depth doesn't match that of the tree.
     *
     * @param row   Row to return x location for
     * @param depth Depth of the row
     * @return amount to indent the given row.
     */
    protected int getRowX ( final int row, final int depth )
    {
        return totalChildIndent * ( depth + depthOffset );
    }

    /**
     * Updates how much each depth should be offset by.
     */
    protected void updateDepthOffset ()
    {
        if ( isRootVisible () )
        {
            if ( getShowsRootHandles () )
            {
                depthOffset = 1;
            }
            else
            {
                depthOffset = 0;
            }
        }
        else if ( !getShowsRootHandles () )
        {
            depthOffset = -1;
        }
        else
        {
            depthOffset = 0;
        }
    }

    /**
     * The vertical element of legs between nodes starts at the bottom of the parent node by default.
     * This method makes the leg start below that.
     *
     * @return vertical leg indent
     */
    protected int getVerticalLegIndent ()
    {
        return 0;
    }

    /**
     * Paints drop location if it is available.
     *
     * @param g2d graphics context
     */
    protected void paintDropLocation ( @NotNull final Graphics2D g2d )
    {
        // Checking drop location availability
        if ( dropLocationPainter != null )
        {
            final JTree.DropLocation dropLocation = component.getDropLocation ();
            if ( dropLocation != null && dropLocation.getPath () != null )
            {
                // Calculating drop location bounds
                final Rectangle dropViewBounds = dropLocationPainter.getDropViewBounds ( dropLocation );

                // Painting drop location view
                dropLocationPainter.prepareToPaint ( dropLocation );
                paintSection ( dropLocationPainter, g2d, dropViewBounds );
            }
        }
    }

    /**
     * Paints custom WebLaF multiselector.
     *
     * @param g2d graphics context
     */
    protected void paintMultiselector ( @NotNull final Graphics2D g2d )
    {
        if ( isSelectorAvailable () && selectionStart != null && selectionEnd != null )
        {
            // Calculating selector bounds
            final Rectangle rawBounds = GeometryUtils.getContainingRect ( selectionStart, selectionEnd );
            final Rectangle bounds = rawBounds.intersection ( BoundsType.component.bounds ( component ) );
            bounds.width -= 1;
            bounds.height -= 1;

            // Painting selector
            paintSection ( selectorPainter, g2d, bounds );
        }
    }

    /**
     * Returns whether selector is available for current tree or not.
     *
     * @return {@code true} if selector is available for current tree, {@code false} otherwise
     */
    protected boolean isSelectorAvailable ()
    {
        return selectorPainter != null && component != null && component.isEnabled () &&
                component.getSelectionModel ().getSelectionMode () != TreeSelectionModel.SINGLE_TREE_SELECTION;
    }

    /**
     * Returns the Rectangle enclosing the label portion that the last item in path will be painted into.
     * Will return null if any component in path is currently valid.
     *
     * @param path tree path
     * @return Rectangle enclosing the label portion that the last item in path will be painted into
     */
    @Nullable
    protected Rectangle getPathBounds ( @Nullable final TreePath path )
    {
        final Rectangle pathBounds;
        if ( component != null && treeLayoutCache != null )
        {
            pathBounds = getPathBounds ( path, component.getInsets (), new Rectangle () );
        }
        else
        {
            pathBounds = null;
        }
        return pathBounds;
    }

    /**
     * Returns path bounds used for painting.
     *
     * @param path   tree path
     * @param insets tree insets
     * @param bounds bounds buffer
     * @return path bounds
     */
    @Nullable
    protected Rectangle getPathBounds ( @Nullable final TreePath path, @NotNull final Insets insets, @NotNull Rectangle bounds )
    {
        bounds = treeLayoutCache.getBounds ( path, bounds );
        if ( bounds != null )
        {
            if ( ltr )
            {
                bounds.x += insets.left;
            }
            else
            {
                bounds.x = component.getWidth () - ( bounds.x + bounds.width ) - insets.right;
            }
            bounds.y += insets.top;
        }
        return bounds;
    }

    /**
     * Returns whether tree selection style points that the whole line is a single cell or not.
     *
     * @return {@code true} if tree selection style points that the whole line is a single cell, {@code false} otherwise
     */
    protected boolean isFullLineSelection ()
    {
        return ui.getSelectionStyle () == TreeSelectionStyle.line;
    }

    /**
     * Returns whether tree nodes drag available or not.
     *
     * @return {@code true} if tree nodes drag available, {@code false} otherwise
     */
    protected boolean isDragAvailable ()
    {
        return component != null && component.isEnabled () && component.getDragEnabled () &&
                component.getTransferHandler () != null && component.getTransferHandler ().getSourceActions ( component ) > 0;
    }
}