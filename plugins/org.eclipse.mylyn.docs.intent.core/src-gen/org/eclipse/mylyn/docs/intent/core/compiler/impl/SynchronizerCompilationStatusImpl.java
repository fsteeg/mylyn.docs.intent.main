/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.mylyn.docs.intent.core.compiler.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.mylyn.docs.intent.core.compiler.CompilerPackage;
import org.eclipse.mylyn.docs.intent.core.compiler.SynchronizerCompilationStatus;
import org.eclipse.mylyn.docs.intent.core.compiler.SynchronizerResourceState;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Synchronizer Compilation Status</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.mylyn.docs.intent.core.compiler.impl.SynchronizerCompilationStatusImpl#getWorkingCopyResourceURI <em>Working Copy Resource URI</em>}</li>
 *   <li>{@link org.eclipse.mylyn.docs.intent.core.compiler.impl.SynchronizerCompilationStatusImpl#getCompiledResourceURI <em>Compiled Resource URI</em>}</li>
 *   <li>{@link org.eclipse.mylyn.docs.intent.core.compiler.impl.SynchronizerCompilationStatusImpl#getWorkingCopyResourceState <em>Working Copy Resource State</em>}</li>
 *   <li>{@link org.eclipse.mylyn.docs.intent.core.compiler.impl.SynchronizerCompilationStatusImpl#getCompiledResourceState <em>Compiled Resource State</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SynchronizerCompilationStatusImpl extends CompilationStatusImpl implements SynchronizerCompilationStatus {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SynchronizerCompilationStatusImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CompilerPackage.Literals.SYNCHRONIZER_COMPILATION_STATUS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getWorkingCopyResourceURI() {
		return (String)eGet(
				CompilerPackage.Literals.SYNCHRONIZER_COMPILATION_STATUS__WORKING_COPY_RESOURCE_URI, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWorkingCopyResourceURI(String newWorkingCopyResourceURI) {
		eSet(CompilerPackage.Literals.SYNCHRONIZER_COMPILATION_STATUS__WORKING_COPY_RESOURCE_URI,
				newWorkingCopyResourceURI);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCompiledResourceURI() {
		return (String)eGet(CompilerPackage.Literals.SYNCHRONIZER_COMPILATION_STATUS__COMPILED_RESOURCE_URI,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCompiledResourceURI(String newCompiledResourceURI) {
		eSet(CompilerPackage.Literals.SYNCHRONIZER_COMPILATION_STATUS__COMPILED_RESOURCE_URI,
				newCompiledResourceURI);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SynchronizerResourceState getWorkingCopyResourceState() {
		return (SynchronizerResourceState)eGet(
				CompilerPackage.Literals.SYNCHRONIZER_COMPILATION_STATUS__WORKING_COPY_RESOURCE_STATE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWorkingCopyResourceState(SynchronizerResourceState newWorkingCopyResourceState) {
		eSet(CompilerPackage.Literals.SYNCHRONIZER_COMPILATION_STATUS__WORKING_COPY_RESOURCE_STATE,
				newWorkingCopyResourceState);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SynchronizerResourceState getCompiledResourceState() {
		return (SynchronizerResourceState)eGet(
				CompilerPackage.Literals.SYNCHRONIZER_COMPILATION_STATUS__COMPILED_RESOURCE_STATE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCompiledResourceState(SynchronizerResourceState newCompiledResourceState) {
		eSet(CompilerPackage.Literals.SYNCHRONIZER_COMPILATION_STATUS__COMPILED_RESOURCE_STATE,
				newCompiledResourceState);
	}

} //SynchronizerCompilationStatusImpl
