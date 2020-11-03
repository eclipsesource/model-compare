/**
 */
package EAM_Metamodel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Product</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link EAM_Metamodel.EAM_Product#getOwner <em>Owner</em>}</li>
 *   <li>{@link EAM_Metamodel.EAM_Product#getDepartment <em>Department</em>}</li>
 *   <li>{@link EAM_Metamodel.EAM_Product#getApplication <em>Application</em>}</li>
 * </ul>
 *
 * @see EAM_Metamodel.EAM_MetamodelPackage#getProduct()
 * @model
 * @generated
 */
public interface EAM_Product extends EAM_Node {
	/**
	 * Returns the value of the '<em><b>Owner</b></em>' attribute.
	 * The default value is <code>"name"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owner</em>' attribute.
	 * @see #setOwner(String)
	 * @see EAM_Metamodel.EAM_MetamodelPackage#getProduct_Owner()
	 * @model default="name"
	 * @generated
	 */
	String getOwner();

	/**
	 * Sets the value of the '{@link EAM_Metamodel.EAM_Product#getOwner <em>Owner</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owner</em>' attribute.
	 * @see #getOwner()
	 * @generated
	 */
	void setOwner(String value);

	/**
	 * Returns the value of the '<em><b>Department</b></em>' attribute.
	 * The default value is <code>"DE01"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Department</em>' attribute.
	 * @see #setDepartment(String)
	 * @see EAM_Metamodel.EAM_MetamodelPackage#getProduct_Department()
	 * @model default="DE01"
	 * @generated
	 */
	String getDepartment();

	/**
	 * Sets the value of the '{@link EAM_Metamodel.EAM_Product#getDepartment <em>Department</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Department</em>' attribute.
	 * @see #getDepartment()
	 * @generated
	 */
	void setDepartment(String value);

	/**
	 * Returns the value of the '<em><b>Application</b></em>' reference list.
	 * The list contents are of type {@link EAM_Metamodel.EAM_Application}.
	 * It is bidirectional and its opposite is '{@link EAM_Metamodel.EAM_Application#getProduct <em>Product</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Application</em>' reference list.
	 * @see EAM_Metamodel.EAM_MetamodelPackage#getProduct_Application()
	 * @see EAM_Metamodel.EAM_Application#getProduct
	 * @model opposite="product"
	 * @generated
	 */
	EList<EAM_Application> getApplication();

} // EAM_Product
