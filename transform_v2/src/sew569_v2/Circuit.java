/**
 */
package sew569_v2;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Circuit</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link sew569_v2.Circuit#getParts <em>Parts</em>}</li>
 * </ul>
 *
 * @see sew569_v2.Sew569_v2Package#getCircuit()
 * @model
 * @generated
 */
public interface Circuit extends EObject {
	/**
	 * Returns the value of the '<em><b>Parts</b></em>' containment reference list.
	 * The list contents are of type {@link sew569_v2.Part}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parts</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parts</em>' containment reference list.
	 * @see sew569_v2.Sew569_v2Package#getCircuit_Parts()
	 * @model containment="true"
	 * @generated
	 */
	EList<Part> getParts();

} // Circuit
