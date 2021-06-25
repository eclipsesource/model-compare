/**
 */
package EAM_Metamodel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Host</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link EAM_Metamodel.EAM_Host#getVendor <em>Vendor</em>}</li>
 *   <li>{@link EAM_Metamodel.EAM_Host#getApplication <em>Application</em>}</li>
 * </ul>
 *
 * @see EAM_Metamodel.EAM_MetamodelPackage#getHost()
 * @model
 * @generated
 */
public interface EAM_Host extends EAM_Node {
	/**
	 * Returns the value of the '<em><b>Vendor</b></em>' attribute.
	 * The default value is <code>"vendor 1"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vendor</em>' attribute.
	 * @see #setVendor(String)
	 * @see EAM_Metamodel.EAM_MetamodelPackage#getHost_Vendor()
	 * @model default="vendor 1"
	 * @generated
	 */
	String getVendor();

	/**
	 * Sets the value of the '{@link EAM_Metamodel.EAM_Host#getVendor <em>Vendor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vendor</em>' attribute.
	 * @see #getVendor()
	 * @generated
	 */
	void setVendor(String value);

	/**
	 * Returns the value of the '<em><b>Application</b></em>' reference list.
	 * The list contents are of type {@link EAM_Metamodel.EAM_Application}.
	 * It is bidirectional and its opposite is '{@link EAM_Metamodel.EAM_Application#getHost <em>Host</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Application</em>' reference list.
	 * @see EAM_Metamodel.EAM_MetamodelPackage#getHost_Application()
	 * @see EAM_Metamodel.EAM_Application#getHost
	 * @model opposite="host"
	 * @generated
	 */
	EList<EAM_Application> getApplication();

} // EAM_Host
