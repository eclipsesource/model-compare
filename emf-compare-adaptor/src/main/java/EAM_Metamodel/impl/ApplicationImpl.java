/**
 */
package EAM_Metamodel.impl;

import EAM_Metamodel.ApplicationType;
import EAM_Metamodel.EAM_Application;
import EAM_Metamodel.EAM_Host;
import EAM_Metamodel.EAM_MetamodelPackage;
import EAM_Metamodel.EAM_Product;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Application</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link EAM_Metamodel.impl.ApplicationImpl#getType <em>Type</em>}</li>
 *   <li>{@link EAM_Metamodel.impl.ApplicationImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link EAM_Metamodel.impl.ApplicationImpl#getApplication <em>Application</em>}</li>
 *   <li>{@link EAM_Metamodel.impl.ApplicationImpl#getApplicationOpposite <em>Application Opposite</em>}</li>
 *   <li>{@link EAM_Metamodel.impl.ApplicationImpl#getHost <em>Host</em>}</li>
 *   <li>{@link EAM_Metamodel.impl.ApplicationImpl#getProduct <em>Product</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ApplicationImpl extends NodeImpl implements EAM_Application {
	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final ApplicationType TYPE_EDEFAULT = ApplicationType.GATEWAY;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected ApplicationType type = TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String VERSION_EDEFAULT = "1.0";

	/**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected String version = VERSION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getApplication() <em>Application</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getApplication()
	 * @generated
	 * @ordered
	 */
	protected EList<EAM_Application> application;

	/**
	 * The cached value of the '{@link #getApplicationOpposite() <em>Application Opposite</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getApplicationOpposite()
	 * @generated
	 * @ordered
	 */
	protected EList<EAM_Application> applicationOpposite;

	/**
	 * The cached value of the '{@link #getHost() <em>Host</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHost()
	 * @generated
	 * @ordered
	 */
	protected EList<EAM_Host> host;

	/**
	 * The cached value of the '{@link #getProduct() <em>Product</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProduct()
	 * @generated
	 * @ordered
	 */
	protected EList<EAM_Product> product;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ApplicationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EAM_MetamodelPackage.Literals.APPLICATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ApplicationType getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(ApplicationType newType) {
		ApplicationType oldType = type;
		type = newType == null ? TYPE_EDEFAULT : newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EAM_MetamodelPackage.APPLICATION__TYPE, oldType,
					type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVersion(String newVersion) {
		String oldVersion = version;
		version = newVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EAM_MetamodelPackage.APPLICATION__VERSION, oldVersion,
					version));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EAM_Application> getApplication() {
		if (application == null) {
			application = new EObjectWithInverseResolvingEList.ManyInverse<EAM_Application>(EAM_Application.class, this,
					EAM_MetamodelPackage.APPLICATION__APPLICATION,
					EAM_MetamodelPackage.APPLICATION__APPLICATION_OPPOSITE);
		}
		return application;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EAM_Application> getApplicationOpposite() {
		if (applicationOpposite == null) {
			applicationOpposite = new EObjectWithInverseResolvingEList.ManyInverse<EAM_Application>(
					EAM_Application.class, this, EAM_MetamodelPackage.APPLICATION__APPLICATION_OPPOSITE,
					EAM_MetamodelPackage.APPLICATION__APPLICATION);
		}
		return applicationOpposite;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EAM_Host> getHost() {
		if (host == null) {
			host = new EObjectWithInverseResolvingEList.ManyInverse<EAM_Host>(EAM_Host.class, this,
					EAM_MetamodelPackage.APPLICATION__HOST, EAM_MetamodelPackage.HOST__APPLICATION);
		}
		return host;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EAM_Product> getProduct() {
		if (product == null) {
			product = new EObjectWithInverseResolvingEList.ManyInverse<EAM_Product>(EAM_Product.class, this,
					EAM_MetamodelPackage.APPLICATION__PRODUCT, EAM_MetamodelPackage.PRODUCT__APPLICATION);
		}
		return product;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case EAM_MetamodelPackage.APPLICATION__APPLICATION:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getApplication()).basicAdd(otherEnd, msgs);
		case EAM_MetamodelPackage.APPLICATION__APPLICATION_OPPOSITE:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getApplicationOpposite()).basicAdd(otherEnd,
					msgs);
		case EAM_MetamodelPackage.APPLICATION__HOST:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getHost()).basicAdd(otherEnd, msgs);
		case EAM_MetamodelPackage.APPLICATION__PRODUCT:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getProduct()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case EAM_MetamodelPackage.APPLICATION__APPLICATION:
			return ((InternalEList<?>) getApplication()).basicRemove(otherEnd, msgs);
		case EAM_MetamodelPackage.APPLICATION__APPLICATION_OPPOSITE:
			return ((InternalEList<?>) getApplicationOpposite()).basicRemove(otherEnd, msgs);
		case EAM_MetamodelPackage.APPLICATION__HOST:
			return ((InternalEList<?>) getHost()).basicRemove(otherEnd, msgs);
		case EAM_MetamodelPackage.APPLICATION__PRODUCT:
			return ((InternalEList<?>) getProduct()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case EAM_MetamodelPackage.APPLICATION__TYPE:
			return getType();
		case EAM_MetamodelPackage.APPLICATION__VERSION:
			return getVersion();
		case EAM_MetamodelPackage.APPLICATION__APPLICATION:
			return getApplication();
		case EAM_MetamodelPackage.APPLICATION__APPLICATION_OPPOSITE:
			return getApplicationOpposite();
		case EAM_MetamodelPackage.APPLICATION__HOST:
			return getHost();
		case EAM_MetamodelPackage.APPLICATION__PRODUCT:
			return getProduct();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case EAM_MetamodelPackage.APPLICATION__TYPE:
			setType((ApplicationType) newValue);
			return;
		case EAM_MetamodelPackage.APPLICATION__VERSION:
			setVersion((String) newValue);
			return;
		case EAM_MetamodelPackage.APPLICATION__APPLICATION:
			getApplication().clear();
			getApplication().addAll((Collection<? extends EAM_Application>) newValue);
			return;
		case EAM_MetamodelPackage.APPLICATION__APPLICATION_OPPOSITE:
			getApplicationOpposite().clear();
			getApplicationOpposite().addAll((Collection<? extends EAM_Application>) newValue);
			return;
		case EAM_MetamodelPackage.APPLICATION__HOST:
			getHost().clear();
			getHost().addAll((Collection<? extends EAM_Host>) newValue);
			return;
		case EAM_MetamodelPackage.APPLICATION__PRODUCT:
			getProduct().clear();
			getProduct().addAll((Collection<? extends EAM_Product>) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case EAM_MetamodelPackage.APPLICATION__TYPE:
			setType(TYPE_EDEFAULT);
			return;
		case EAM_MetamodelPackage.APPLICATION__VERSION:
			setVersion(VERSION_EDEFAULT);
			return;
		case EAM_MetamodelPackage.APPLICATION__APPLICATION:
			getApplication().clear();
			return;
		case EAM_MetamodelPackage.APPLICATION__APPLICATION_OPPOSITE:
			getApplicationOpposite().clear();
			return;
		case EAM_MetamodelPackage.APPLICATION__HOST:
			getHost().clear();
			return;
		case EAM_MetamodelPackage.APPLICATION__PRODUCT:
			getProduct().clear();
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case EAM_MetamodelPackage.APPLICATION__TYPE:
			return type != TYPE_EDEFAULT;
		case EAM_MetamodelPackage.APPLICATION__VERSION:
			return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
		case EAM_MetamodelPackage.APPLICATION__APPLICATION:
			return application != null && !application.isEmpty();
		case EAM_MetamodelPackage.APPLICATION__APPLICATION_OPPOSITE:
			return applicationOpposite != null && !applicationOpposite.isEmpty();
		case EAM_MetamodelPackage.APPLICATION__HOST:
			return host != null && !host.isEmpty();
		case EAM_MetamodelPackage.APPLICATION__PRODUCT:
			return product != null && !product.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (type: ");
		result.append(type);
		result.append(", version: ");
		result.append(version);
		result.append(')');
		return result.toString();
	}

} //ApplicationImpl
