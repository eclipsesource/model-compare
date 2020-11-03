/**
 */
package EAM_Metamodel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Application</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link EAM_Metamodel.EAM_Application#getType <em>Type</em>}</li>
 *   <li>{@link EAM_Metamodel.EAM_Application#getVersion <em>Version</em>}</li>
 *   <li>{@link EAM_Metamodel.EAM_Application#getApplication <em>Application</em>}</li>
 *   <li>{@link EAM_Metamodel.EAM_Application#getApplicationOpposite <em>Application Opposite</em>}</li>
 *   <li>{@link EAM_Metamodel.EAM_Application#getHost <em>Host</em>}</li>
 *   <li>{@link EAM_Metamodel.EAM_Application#getProduct <em>Product</em>}</li>
 * </ul>
 *
 * @see EAM_Metamodel.EAM_MetamodelPackage#getApplication()
 * @model
 * @generated
 */
public interface EAM_Application extends EAM_Node {
	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The literals are from the enumeration {@link EAM_Metamodel.ApplicationType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see EAM_Metamodel.ApplicationType
	 * @see #setType(ApplicationType)
	 * @see EAM_Metamodel.EAM_MetamodelPackage#getApplication_Type()
	 * @model
	 * @generated
	 */
	ApplicationType getType();

	/**
	 * Sets the value of the '{@link EAM_Metamodel.EAM_Application#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see EAM_Metamodel.ApplicationType
	 * @see #getType()
	 * @generated
	 */
	void setType(ApplicationType value);

	/**
	 * Returns the value of the '<em><b>Version</b></em>' attribute.
	 * The default value is <code>"1.0"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Version</em>' attribute.
	 * @see #setVersion(String)
	 * @see EAM_Metamodel.EAM_MetamodelPackage#getApplication_Version()
	 * @model default="1.0"
	 * @generated
	 */
	String getVersion();

	/**
	 * Sets the value of the '{@link EAM_Metamodel.EAM_Application#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version</em>' attribute.
	 * @see #getVersion()
	 * @generated
	 */
	void setVersion(String value);

	/**
	 * Returns the value of the '<em><b>Application</b></em>' reference list.
	 * The list contents are of type {@link EAM_Metamodel.EAM_Application}.
	 * It is bidirectional and its opposite is '{@link EAM_Metamodel.EAM_Application#getApplicationOpposite <em>Application Opposite</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Application</em>' reference list.
	 * @see EAM_Metamodel.EAM_MetamodelPackage#getApplication_Application()
	 * @see EAM_Metamodel.EAM_Application#getApplicationOpposite
	 * @model opposite="applicationOpposite"
	 * @generated
	 */
	EList<EAM_Application> getApplication();

	/**
	 * Returns the value of the '<em><b>Application Opposite</b></em>' reference list.
	 * The list contents are of type {@link EAM_Metamodel.EAM_Application}.
	 * It is bidirectional and its opposite is '{@link EAM_Metamodel.EAM_Application#getApplication <em>Application</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Application Opposite</em>' reference list.
	 * @see EAM_Metamodel.EAM_MetamodelPackage#getApplication_ApplicationOpposite()
	 * @see EAM_Metamodel.EAM_Application#getApplication
	 * @model opposite="application"
	 * @generated
	 */
	EList<EAM_Application> getApplicationOpposite();

	/**
	 * Returns the value of the '<em><b>Host</b></em>' reference list.
	 * The list contents are of type {@link EAM_Metamodel.EAM_Host}.
	 * It is bidirectional and its opposite is '{@link EAM_Metamodel.EAM_Host#getApplication <em>Application</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Host</em>' reference list.
	 * @see EAM_Metamodel.EAM_MetamodelPackage#getApplication_Host()
	 * @see EAM_Metamodel.EAM_Host#getApplication
	 * @model opposite="application"
	 * @generated
	 */
	EList<EAM_Host> getHost();

	/**
	 * Returns the value of the '<em><b>Product</b></em>' reference list.
	 * The list contents are of type {@link EAM_Metamodel.EAM_Product}.
	 * It is bidirectional and its opposite is '{@link EAM_Metamodel.EAM_Product#getApplication <em>Application</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Product</em>' reference list.
	 * @see EAM_Metamodel.EAM_MetamodelPackage#getApplication_Product()
	 * @see EAM_Metamodel.EAM_Product#getApplication
	 * @model opposite="application"
	 * @generated
	 */
	EList<EAM_Product> getProduct();

} // EAM_Application
