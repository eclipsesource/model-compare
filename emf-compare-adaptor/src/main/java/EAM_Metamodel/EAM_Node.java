/**
 */
package EAM_Metamodel;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link EAM_Metamodel.EAM_Node#getId <em>Id</em>}</li>
 *   <li>{@link EAM_Metamodel.EAM_Node#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see EAM_Metamodel.EAM_MetamodelPackage#getNode()
 * @model
 * @generated
 */
public interface EAM_Node extends EObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see EAM_Metamodel.EAM_MetamodelPackage#getNode_Id()
	 * @model default="0"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link EAM_Metamodel.EAM_Node#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see EAM_Metamodel.EAM_MetamodelPackage#getNode_Name()
	 * @model default=""
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link EAM_Metamodel.EAM_Node#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // EAM_Node
