/**
 */
package EAM_Metamodel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Landscape</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link EAM_Metamodel.EAM_Landscape#getNode <em>Node</em>}</li>
 *   <li>{@link EAM_Metamodel.EAM_Landscape#getName <em>Name</em>}</li>
 *   <li>{@link EAM_Metamodel.EAM_Landscape#getNsURI <em>Ns URI</em>}</li>
 *   <li>{@link EAM_Metamodel.EAM_Landscape#getNsPrefix <em>Ns Prefix</em>}</li>
 * </ul>
 *
 * @see EAM_Metamodel.EAM_MetamodelPackage#getLandscape()
 * @model
 * @generated
 */
public interface EAM_Landscape extends EObject {
	/**
	 * Returns the value of the '<em><b>Node</b></em>' containment reference list.
	 * The list contents are of type {@link EAM_Metamodel.EAM_Node}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Node</em>' containment reference list.
	 * @see EAM_Metamodel.EAM_MetamodelPackage#getLandscape_Node()
	 * @model containment="true"
	 * @generated
	 */
	EList<EAM_Node> getNode();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see EAM_Metamodel.EAM_MetamodelPackage#getLandscape_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link EAM_Metamodel.EAM_Landscape#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Ns URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ns URI</em>' attribute.
	 * @see #setNsURI(String)
	 * @see EAM_Metamodel.EAM_MetamodelPackage#getLandscape_NsURI()
	 * @model
	 * @generated
	 */
	String getNsURI();

	/**
	 * Sets the value of the '{@link EAM_Metamodel.EAM_Landscape#getNsURI <em>Ns URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ns URI</em>' attribute.
	 * @see #getNsURI()
	 * @generated
	 */
	void setNsURI(String value);

	/**
	 * Returns the value of the '<em><b>Ns Prefix</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ns Prefix</em>' attribute.
	 * @see #setNsPrefix(String)
	 * @see EAM_Metamodel.EAM_MetamodelPackage#getLandscape_NsPrefix()
	 * @model
	 * @generated
	 */
	String getNsPrefix();

	/**
	 * Sets the value of the '{@link EAM_Metamodel.EAM_Landscape#getNsPrefix <em>Ns Prefix</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ns Prefix</em>' attribute.
	 * @see #getNsPrefix()
	 * @generated
	 */
	void setNsPrefix(String value);

} // EAM_Landscape
