/**
 */
package sew569_v2;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Connector</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link sew569_v2.Connector#getName <em>Name</em>}</li>
 *   <li>{@link sew569_v2.Connector#getConnectedTo <em>Connected To</em>}</li>
 *   <li>{@link sew569_v2.Connector#getParent <em>Parent</em>}</li>
 * </ul>
 *
 * @see sew569_v2.Sew569_v2Package#getConnector()
 * @model annotation="gmf.node label='name'"
 * @generated
 */
public interface Connector extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see sew569_v2.Sew569_v2Package#getConnector_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link sew569_v2.Connector#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Connected To</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Connected To</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Connected To</em>' reference.
	 * @see #setConnectedTo(Connector)
	 * @see sew569_v2.Sew569_v2Package#getConnector_ConnectedTo()
	 * @model annotation="gmf.link source.decoration='arrow' target.decoration='arrow'"
	 * @generated
	 */
	Connector getConnectedTo();

	/**
	 * Sets the value of the '{@link sew569_v2.Connector#getConnectedTo <em>Connected To</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Connected To</em>' reference.
	 * @see #getConnectedTo()
	 * @generated
	 */
	void setConnectedTo(Connector value);

	/**
	 * Returns the value of the '<em><b>Parent</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link sew569_v2.Part#getConnectors <em>Connectors</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parent</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent</em>' container reference.
	 * @see #setParent(Part)
	 * @see sew569_v2.Sew569_v2Package#getConnector_Parent()
	 * @see sew569_v2.Part#getConnectors
	 * @model opposite="connectors" transient="false"
	 * @generated
	 */
	Part getParent();

	/**
	 * Sets the value of the '{@link sew569_v2.Connector#getParent <em>Parent</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent</em>' container reference.
	 * @see #getParent()
	 * @generated
	 */
	void setParent(Part value);

} // Connector
