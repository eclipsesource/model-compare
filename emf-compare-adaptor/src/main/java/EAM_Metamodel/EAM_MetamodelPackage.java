/**
 */
package EAM_Metamodel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see EAM_Metamodel.EAM_MetamodelFactory
 * @model kind="package"
 * @generated
 */
public interface EAM_MetamodelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "EAM_Metamodel";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.example.org/EAM_Metamodel";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "EAM_Metamodel";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	EAM_MetamodelPackage eINSTANCE = EAM_Metamodel.impl.MetamodelPackageImpl.init();

	/**
	 * The meta object id for the '{@link EAM_Metamodel.impl.NodeImpl <em>Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see EAM_Metamodel.impl.NodeImpl
	 * @see EAM_Metamodel.impl.MetamodelPackageImpl#getNode()
	 * @generated
	 */
	int NODE = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__ID = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__NAME = 1;

	/**
	 * The number of structural features of the '<em>Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link EAM_Metamodel.impl.ProductImpl <em>Product</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see EAM_Metamodel.impl.ProductImpl
	 * @see EAM_Metamodel.impl.MetamodelPackageImpl#getProduct()
	 * @generated
	 */
	int PRODUCT = 1;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRODUCT__ID = NODE__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRODUCT__NAME = NODE__NAME;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRODUCT__OWNER = NODE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Department</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRODUCT__DEPARTMENT = NODE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Application</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRODUCT__APPLICATION = NODE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Product</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRODUCT_FEATURE_COUNT = NODE_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Product</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRODUCT_OPERATION_COUNT = NODE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link EAM_Metamodel.impl.ApplicationImpl <em>Application</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see EAM_Metamodel.impl.ApplicationImpl
	 * @see EAM_Metamodel.impl.MetamodelPackageImpl#getApplication()
	 * @generated
	 */
	int APPLICATION = 2;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APPLICATION__ID = NODE__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APPLICATION__NAME = NODE__NAME;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APPLICATION__TYPE = NODE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APPLICATION__VERSION = NODE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Application</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APPLICATION__APPLICATION = NODE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Application Opposite</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APPLICATION__APPLICATION_OPPOSITE = NODE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Host</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APPLICATION__HOST = NODE_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Product</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APPLICATION__PRODUCT = NODE_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Application</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APPLICATION_FEATURE_COUNT = NODE_FEATURE_COUNT + 6;

	/**
	 * The number of operations of the '<em>Application</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APPLICATION_OPERATION_COUNT = NODE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link EAM_Metamodel.impl.HostImpl <em>Host</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see EAM_Metamodel.impl.HostImpl
	 * @see EAM_Metamodel.impl.MetamodelPackageImpl#getHost()
	 * @generated
	 */
	int HOST = 3;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOST__ID = NODE__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOST__NAME = NODE__NAME;

	/**
	 * The feature id for the '<em><b>Vendor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOST__VENDOR = NODE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Application</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOST__APPLICATION = NODE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Host</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOST_FEATURE_COUNT = NODE_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Host</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOST_OPERATION_COUNT = NODE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link EAM_Metamodel.impl.DataCenterImpl <em>Data Center</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see EAM_Metamodel.impl.DataCenterImpl
	 * @see EAM_Metamodel.impl.MetamodelPackageImpl#getDataCenter()
	 * @generated
	 */
	int DATA_CENTER = 4;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_CENTER__ID = NODE__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_CENTER__NAME = NODE__NAME;

	/**
	 * The feature id for the '<em><b>Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_CENTER__LOCATION = NODE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Host</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_CENTER__HOST = NODE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Data Center</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_CENTER_FEATURE_COUNT = NODE_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Data Center</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_CENTER_OPERATION_COUNT = NODE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link EAM_Metamodel.impl.LandscapeImpl <em>Landscape</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see EAM_Metamodel.impl.LandscapeImpl
	 * @see EAM_Metamodel.impl.MetamodelPackageImpl#getLandscape()
	 * @generated
	 */
	int LANDSCAPE = 5;

	/**
	 * The feature id for the '<em><b>Node</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LANDSCAPE__NODE = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LANDSCAPE__NAME = 1;

	/**
	 * The feature id for the '<em><b>Ns URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LANDSCAPE__NS_URI = 2;

	/**
	 * The feature id for the '<em><b>Ns Prefix</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LANDSCAPE__NS_PREFIX = 3;

	/**
	 * The number of structural features of the '<em>Landscape</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LANDSCAPE_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Landscape</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LANDSCAPE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link EAM_Metamodel.ApplicationType <em>Application Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see EAM_Metamodel.ApplicationType
	 * @see EAM_Metamodel.impl.MetamodelPackageImpl#getApplicationType()
	 * @generated
	 */
	int APPLICATION_TYPE = 6;

	/**
	 * Returns the meta object for class '{@link EAM_Metamodel.EAM_Node <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Node</em>'.
	 * @see EAM_Metamodel.EAM_Node
	 * @generated
	 */
	EClass getNode();

	/**
	 * Returns the meta object for the attribute '{@link EAM_Metamodel.EAM_Node#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see EAM_Metamodel.EAM_Node#getId()
	 * @see #getNode()
	 * @generated
	 */
	EAttribute getNode_Id();

	/**
	 * Returns the meta object for the attribute '{@link EAM_Metamodel.EAM_Node#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see EAM_Metamodel.EAM_Node#getName()
	 * @see #getNode()
	 * @generated
	 */
	EAttribute getNode_Name();

	/**
	 * Returns the meta object for class '{@link EAM_Metamodel.EAM_Product <em>Product</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Product</em>'.
	 * @see EAM_Metamodel.EAM_Product
	 * @generated
	 */
	EClass getProduct();

	/**
	 * Returns the meta object for the attribute '{@link EAM_Metamodel.EAM_Product#getOwner <em>Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Owner</em>'.
	 * @see EAM_Metamodel.EAM_Product#getOwner()
	 * @see #getProduct()
	 * @generated
	 */
	EAttribute getProduct_Owner();

	/**
	 * Returns the meta object for the attribute '{@link EAM_Metamodel.EAM_Product#getDepartment <em>Department</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Department</em>'.
	 * @see EAM_Metamodel.EAM_Product#getDepartment()
	 * @see #getProduct()
	 * @generated
	 */
	EAttribute getProduct_Department();

	/**
	 * Returns the meta object for the reference list '{@link EAM_Metamodel.EAM_Product#getApplication <em>Application</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Application</em>'.
	 * @see EAM_Metamodel.EAM_Product#getApplication()
	 * @see #getProduct()
	 * @generated
	 */
	EReference getProduct_Application();

	/**
	 * Returns the meta object for class '{@link EAM_Metamodel.EAM_Application <em>Application</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Application</em>'.
	 * @see EAM_Metamodel.EAM_Application
	 * @generated
	 */
	EClass getApplication();

	/**
	 * Returns the meta object for the attribute '{@link EAM_Metamodel.EAM_Application#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see EAM_Metamodel.EAM_Application#getType()
	 * @see #getApplication()
	 * @generated
	 */
	EAttribute getApplication_Type();

	/**
	 * Returns the meta object for the attribute '{@link EAM_Metamodel.EAM_Application#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see EAM_Metamodel.EAM_Application#getVersion()
	 * @see #getApplication()
	 * @generated
	 */
	EAttribute getApplication_Version();

	/**
	 * Returns the meta object for the reference list '{@link EAM_Metamodel.EAM_Application#getApplication <em>Application</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Application</em>'.
	 * @see EAM_Metamodel.EAM_Application#getApplication()
	 * @see #getApplication()
	 * @generated
	 */
	EReference getApplication_Application();

	/**
	 * Returns the meta object for the reference list '{@link EAM_Metamodel.EAM_Application#getApplicationOpposite <em>Application Opposite</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Application Opposite</em>'.
	 * @see EAM_Metamodel.EAM_Application#getApplicationOpposite()
	 * @see #getApplication()
	 * @generated
	 */
	EReference getApplication_ApplicationOpposite();

	/**
	 * Returns the meta object for the reference list '{@link EAM_Metamodel.EAM_Application#getHost <em>Host</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Host</em>'.
	 * @see EAM_Metamodel.EAM_Application#getHost()
	 * @see #getApplication()
	 * @generated
	 */
	EReference getApplication_Host();

	/**
	 * Returns the meta object for the reference list '{@link EAM_Metamodel.EAM_Application#getProduct <em>Product</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Product</em>'.
	 * @see EAM_Metamodel.EAM_Application#getProduct()
	 * @see #getApplication()
	 * @generated
	 */
	EReference getApplication_Product();

	/**
	 * Returns the meta object for class '{@link EAM_Metamodel.EAM_Host <em>Host</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Host</em>'.
	 * @see EAM_Metamodel.EAM_Host
	 * @generated
	 */
	EClass getHost();

	/**
	 * Returns the meta object for the attribute '{@link EAM_Metamodel.EAM_Host#getVendor <em>Vendor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Vendor</em>'.
	 * @see EAM_Metamodel.EAM_Host#getVendor()
	 * @see #getHost()
	 * @generated
	 */
	EAttribute getHost_Vendor();

	/**
	 * Returns the meta object for the reference list '{@link EAM_Metamodel.EAM_Host#getApplication <em>Application</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Application</em>'.
	 * @see EAM_Metamodel.EAM_Host#getApplication()
	 * @see #getHost()
	 * @generated
	 */
	EReference getHost_Application();

	/**
	 * Returns the meta object for class '{@link EAM_Metamodel.EAM_DataCenter <em>Data Center</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Center</em>'.
	 * @see EAM_Metamodel.EAM_DataCenter
	 * @generated
	 */
	EClass getDataCenter();

	/**
	 * Returns the meta object for the attribute '{@link EAM_Metamodel.EAM_DataCenter#getLocation <em>Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Location</em>'.
	 * @see EAM_Metamodel.EAM_DataCenter#getLocation()
	 * @see #getDataCenter()
	 * @generated
	 */
	EAttribute getDataCenter_Location();

	/**
	 * Returns the meta object for the containment reference list '{@link EAM_Metamodel.EAM_DataCenter#getHost <em>Host</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Host</em>'.
	 * @see EAM_Metamodel.EAM_DataCenter#getHost()
	 * @see #getDataCenter()
	 * @generated
	 */
	EReference getDataCenter_Host();

	/**
	 * Returns the meta object for class '{@link EAM_Metamodel.EAM_Landscape <em>Landscape</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Landscape</em>'.
	 * @see EAM_Metamodel.EAM_Landscape
	 * @generated
	 */
	EClass getLandscape();

	/**
	 * Returns the meta object for the containment reference list '{@link EAM_Metamodel.EAM_Landscape#getNode <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Node</em>'.
	 * @see EAM_Metamodel.EAM_Landscape#getNode()
	 * @see #getLandscape()
	 * @generated
	 */
	EReference getLandscape_Node();

	/**
	 * Returns the meta object for the attribute '{@link EAM_Metamodel.EAM_Landscape#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see EAM_Metamodel.EAM_Landscape#getName()
	 * @see #getLandscape()
	 * @generated
	 */
	EAttribute getLandscape_Name();

	/**
	 * Returns the meta object for the attribute '{@link EAM_Metamodel.EAM_Landscape#getNsURI <em>Ns URI</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ns URI</em>'.
	 * @see EAM_Metamodel.EAM_Landscape#getNsURI()
	 * @see #getLandscape()
	 * @generated
	 */
	EAttribute getLandscape_NsURI();

	/**
	 * Returns the meta object for the attribute '{@link EAM_Metamodel.EAM_Landscape#getNsPrefix <em>Ns Prefix</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ns Prefix</em>'.
	 * @see EAM_Metamodel.EAM_Landscape#getNsPrefix()
	 * @see #getLandscape()
	 * @generated
	 */
	EAttribute getLandscape_NsPrefix();

	/**
	 * Returns the meta object for enum '{@link EAM_Metamodel.ApplicationType <em>Application Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Application Type</em>'.
	 * @see EAM_Metamodel.ApplicationType
	 * @generated
	 */
	EEnum getApplicationType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	EAM_MetamodelFactory getMetamodelFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link EAM_Metamodel.impl.NodeImpl <em>Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see EAM_Metamodel.impl.NodeImpl
		 * @see EAM_Metamodel.impl.MetamodelPackageImpl#getNode()
		 * @generated
		 */
		EClass NODE = eINSTANCE.getNode();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NODE__ID = eINSTANCE.getNode_Id();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NODE__NAME = eINSTANCE.getNode_Name();

		/**
		 * The meta object literal for the '{@link EAM_Metamodel.impl.ProductImpl <em>Product</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see EAM_Metamodel.impl.ProductImpl
		 * @see EAM_Metamodel.impl.MetamodelPackageImpl#getProduct()
		 * @generated
		 */
		EClass PRODUCT = eINSTANCE.getProduct();

		/**
		 * The meta object literal for the '<em><b>Owner</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PRODUCT__OWNER = eINSTANCE.getProduct_Owner();

		/**
		 * The meta object literal for the '<em><b>Department</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PRODUCT__DEPARTMENT = eINSTANCE.getProduct_Department();

		/**
		 * The meta object literal for the '<em><b>Application</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRODUCT__APPLICATION = eINSTANCE.getProduct_Application();

		/**
		 * The meta object literal for the '{@link EAM_Metamodel.impl.ApplicationImpl <em>Application</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see EAM_Metamodel.impl.ApplicationImpl
		 * @see EAM_Metamodel.impl.MetamodelPackageImpl#getApplication()
		 * @generated
		 */
		EClass APPLICATION = eINSTANCE.getApplication();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute APPLICATION__TYPE = eINSTANCE.getApplication_Type();

		/**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute APPLICATION__VERSION = eINSTANCE.getApplication_Version();

		/**
		 * The meta object literal for the '<em><b>Application</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference APPLICATION__APPLICATION = eINSTANCE.getApplication_Application();

		/**
		 * The meta object literal for the '<em><b>Application Opposite</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference APPLICATION__APPLICATION_OPPOSITE = eINSTANCE.getApplication_ApplicationOpposite();

		/**
		 * The meta object literal for the '<em><b>Host</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference APPLICATION__HOST = eINSTANCE.getApplication_Host();

		/**
		 * The meta object literal for the '<em><b>Product</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference APPLICATION__PRODUCT = eINSTANCE.getApplication_Product();

		/**
		 * The meta object literal for the '{@link EAM_Metamodel.impl.HostImpl <em>Host</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see EAM_Metamodel.impl.HostImpl
		 * @see EAM_Metamodel.impl.MetamodelPackageImpl#getHost()
		 * @generated
		 */
		EClass HOST = eINSTANCE.getHost();

		/**
		 * The meta object literal for the '<em><b>Vendor</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HOST__VENDOR = eINSTANCE.getHost_Vendor();

		/**
		 * The meta object literal for the '<em><b>Application</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference HOST__APPLICATION = eINSTANCE.getHost_Application();

		/**
		 * The meta object literal for the '{@link EAM_Metamodel.impl.DataCenterImpl <em>Data Center</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see EAM_Metamodel.impl.DataCenterImpl
		 * @see EAM_Metamodel.impl.MetamodelPackageImpl#getDataCenter()
		 * @generated
		 */
		EClass DATA_CENTER = eINSTANCE.getDataCenter();

		/**
		 * The meta object literal for the '<em><b>Location</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATA_CENTER__LOCATION = eINSTANCE.getDataCenter_Location();

		/**
		 * The meta object literal for the '<em><b>Host</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_CENTER__HOST = eINSTANCE.getDataCenter_Host();

		/**
		 * The meta object literal for the '{@link EAM_Metamodel.impl.LandscapeImpl <em>Landscape</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see EAM_Metamodel.impl.LandscapeImpl
		 * @see EAM_Metamodel.impl.MetamodelPackageImpl#getLandscape()
		 * @generated
		 */
		EClass LANDSCAPE = eINSTANCE.getLandscape();

		/**
		 * The meta object literal for the '<em><b>Node</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LANDSCAPE__NODE = eINSTANCE.getLandscape_Node();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LANDSCAPE__NAME = eINSTANCE.getLandscape_Name();

		/**
		 * The meta object literal for the '<em><b>Ns URI</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LANDSCAPE__NS_URI = eINSTANCE.getLandscape_NsURI();

		/**
		 * The meta object literal for the '<em><b>Ns Prefix</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LANDSCAPE__NS_PREFIX = eINSTANCE.getLandscape_NsPrefix();

		/**
		 * The meta object literal for the '{@link EAM_Metamodel.ApplicationType <em>Application Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see EAM_Metamodel.ApplicationType
		 * @see EAM_Metamodel.impl.MetamodelPackageImpl#getApplicationType()
		 * @generated
		 */
		EEnum APPLICATION_TYPE = eINSTANCE.getApplicationType();

	}

} //EAM_MetamodelPackage
