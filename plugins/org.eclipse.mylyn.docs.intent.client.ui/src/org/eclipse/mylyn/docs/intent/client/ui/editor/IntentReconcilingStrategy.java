/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.mylyn.docs.intent.client.ui.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.mylyn.docs.intent.client.ui.logger.IntentUiLogger;
import org.eclipse.swt.widgets.Display;

/**
 * This reconciling strategy will allow us to enable folding support in the Intent editor.
 * 
 * @author <a href="mailto:william.piers@obeo.fr">William Piers</a>
 */
public final class IntentReconcilingStrategy implements IReconcilingStrategy, IReconcilingStrategyExtension {

	/**
	 * This will hold the list of all annotations that have been added since the last reconciling.
	 */
	protected final Map<Annotation, Position> addedAnnotations = new HashMap<Annotation, Position>();

	/** Current known positions of foldable block. */
	protected final Map<Annotation, Position> currentAnnotations = new HashMap<Annotation, Position>();

	/**
	 * This will hold the list of all annotations that have been removed since the last reconciling.
	 */
	protected final List<Annotation> deletedAnnotations = new ArrayList<Annotation>();

	/** Editor this provides folding support to. */
	protected final IntentEditor editor;

	/**
	 * This will hold the list of all annotations that have been modified since the last reconciling.
	 */
	protected final Map<Annotation, Position> modifiedAnnotations = new HashMap<Annotation, Position>();

	/** The document we'll seek foldable blocks in. */
	private IDocument document;

	/** Current offset. */
	private int offset;

	/**
	 * Instantiates the reconciling strategy for a given editor.
	 * 
	 * @param editor
	 *            Editor which we seek to provide folding support for.
	 */
	public IntentReconcilingStrategy(IntentEditor editor) {
		this.editor = editor;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension#initialReconcile()
	 */
	public void initialReconcile() {
		offset = 0;
		computePositions();
		updateFoldingStructure();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.text.reconciler.IReconcilingStrategy#reconcile(org.eclipse.jface.text.reconciler.DirtyRegion,
	 *      org.eclipse.jface.text.IRegion)
	 */
	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		reconcile(subRegion);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.text.reconciler.IReconcilingStrategy#reconcile(org.eclipse.jface.text.IRegion)
	 */
	public void reconcile(IRegion partition) {
		offset = partition.getOffset();
		computePositions();
		updateFoldingStructure();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.text.reconciler.IReconcilingStrategy#setDocument(org.eclipse.jface.text.IDocument)
	 */
	public void setDocument(IDocument document) {
		this.document = document;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension#setProgressMonitor(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void setProgressMonitor(IProgressMonitor monitor) {
		// none
	}

	/**
	 * This will compute the current block positions. The offset at which computations start is determined by
	 * {@link #offset}.
	 */
	private void computePositions() {
		deletedAnnotations.clear();
		modifiedAnnotations.clear();
		addedAnnotations.clear();
		deletedAnnotations.addAll(currentAnnotations.keySet());
		for (Iterator<Entry<Annotation, Position>> iterator = currentAnnotations.entrySet().iterator(); iterator
				.hasNext();) {
			Entry<Annotation, Position> entry = iterator.next();
			final Position position = entry.getValue();
			if (position.getOffset() + position.getLength() < offset) {
				deletedAnnotations.remove(entry.getKey());
			}
		}

		IntentPartitioner partitioner = (IntentPartitioner)document.getDocumentPartitioner();
		try {
			for (ITypedRegion region : partitioner.getRegions()) {
				if (IntentDocumentProvider.INTENT_MODELINGUNIT.equals(region.getType())) {
					createOrUpdateAnnotation(region.getOffset(), region.getLength() + 1, false);
				} else if (IntentDocumentProvider.INTENT_STRUCTURAL_CONTENT.equals(region.getType())
						&& document.getChar(region.getOffset()) != '}') {
					final String beginning = document.get(region.getOffset(), region.getLength()).trim();
					if (beginning.endsWith("{")) {
						final int regionEnd = region.getOffset() + beginning.length();
						IRegion wholeRegion = editor.getBlockMatcher().match(document, regionEnd);
						if (wholeRegion != null) {
							createOrUpdateAnnotation(region.getOffset(),
									beginning.length() + wholeRegion.getLength(), false);
						}
					}
				}
			}
		} catch (BadLocationException e) {
			IntentUiLogger.logError(e);
		}
		for (Iterator<Annotation> iterator = deletedAnnotations.iterator(); iterator.hasNext();) {
			currentAnnotations.remove(iterator.next());
		}
	}

	/**
	 * This will update lists {@link #deletedAnnotations}, {@link #addedAnnotations} and
	 * {@link #modifiedAnnotations} according to the given values.
	 * 
	 * @param newOffset
	 *            Offset of the text we want the annotation updated of.
	 * @param newLength
	 *            Length of the text we want the annotation updated of.
	 * @param initiallyCollapsed
	 *            Indicates that the created annotation should be folded from start.
	 * @throws BadLocationException
	 *             Thrown if we try and get an out of range character. Should not happen.
	 */
	private void createOrUpdateAnnotation(final int newOffset, final int newLength, boolean initiallyCollapsed)
			throws BadLocationException {
		boolean createAnnotation = true;
		final Map<Annotation, Position> copy = new HashMap<Annotation, Position>(currentAnnotations);
		final String text = document.get(newOffset, newLength);
		for (Iterator<Entry<Annotation, Position>> iterator = copy.entrySet().iterator(); iterator.hasNext();) {
			Entry<Annotation, Position> entry = iterator.next();
			if (entry.getKey().getText().equals(text)) {
				createAnnotation = false;
				final Position oldPosition = entry.getValue();
				if (oldPosition.getOffset() != newOffset || oldPosition.getLength() != newLength) {
					final Position newPosition = new Position(newOffset, newLength);
					modifiedAnnotations.put(entry.getKey(), newPosition);
					currentAnnotations.put(entry.getKey(), newPosition);
				}
				deletedAnnotations.remove(entry.getKey());
				break;
			}
		}
		if (createAnnotation) {
			Annotation annotation = null;
			annotation = new ProjectionAnnotation(initiallyCollapsed);
			annotation.setText(text);
			final Position position = new Position(newOffset, newLength);
			currentAnnotations.put(annotation, position);
			addedAnnotations.put(annotation, position);
		}
	}

	/**
	 * Updates the editor's folding structure.
	 */
	private void updateFoldingStructure() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				editor.updateFoldingStructure(addedAnnotations, deletedAnnotations, modifiedAnnotations);
			}
		});
	}

}
