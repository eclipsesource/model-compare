/**
 */
package EAM_Metamodel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Center</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link EAM_Metamodel.EAM_DataCenter#getLocation <em>Location</em>}</li>
 *   <li>{@link EAM_Metamodel.EAM_DataCenter#getHost <em>Host</em>}</li>
 * </ul>
 *
 * @see EAM_Metamodel.EAM_MetamodelPackage#getDataCenter()
 * @model
 * @generated
 */
public interface EAM_DataCenter extends EAM_Node {
	/**
	 * Returns the value of the '<em><b>Location</b></em>' attribute.
	 * The default value is <code>"Germany"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Location</em>' attribute.
	 * @see #setLocation(String)
	 * @see EAM_Metamodel.EAM_MetamodelPackage#getDataCenter_Location()
	 * @model default="Germany"
	 * @generated
	 */
	String getLocation();

	/**
	 * Sets the value of the '{@link EAM_Metamodel.EAM_DataCenter#getLocation <em>Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Location</em>' attribute.
	 * @see #getLocation()
	 * @generated
	 */
	void setLocation(String value);

	/**
	 * Returns the value of the '<em><b>Host</b></em>' containment reference list.
	 * The list contents are of type {@link EAM_Metamodel.EAM_Host}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Host</em>' containment reference list.
	 * @see EAM_Metamodel.EAM_MetamodelPackage#getDataCenter_Host()
	 * @model containment="true"
	 * @generated
	 */
	EList<EAM_Host> getHost();

} // EAM_DataCenter
