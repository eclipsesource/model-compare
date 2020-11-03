/**
 */
package EAM_Metamodel.impl;

import EAM_Metamodel.EAM_Application;
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
 * An implementation of the model object '<em><b>Product</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link EAM_Metamodel.impl.ProductImpl#getOwner <em>Owner</em>}</li>
 *   <li>{@link EAM_Metamodel.impl.ProductImpl#getDepartment <em>Department</em>}</li>
 *   <li>{@link EAM_Metamodel.impl.ProductImpl#getApplication <em>Application</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProductImpl extends NodeImpl implements EAM_Product {
	/**
	 * The default value of the '{@link #getOwner() <em>Owner</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwner()
	 * @generated
	 * @ordered
	 */
	protected static final String OWNER_EDEFAULT = "name";

	/**
	 * The cached value of the '{@link #getOwner() <em>Owner</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwner()
	 * @generated
	 * @ordered
	 */
	protected String owner = OWNER_EDEFAULT;

	/**
	 * The default value of the '{@link #getDepartment() <em>Department</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDepartment()
	 * @generated
	 * @ordered
	 */
	protected static final String DEPARTMENT_EDEFAULT = "DE01";

	/**
	 * The cached value of the '{@link #getDepartment() <em>Department</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDepartment()
	 * @generated
	 * @ordered
	 */
	protected String department = DEPARTMENT_EDEFAULT;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ProductImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EAM_MetamodelPackage.Literals.PRODUCT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOwner(String newOwner) {
		String oldOwner = owner;
		owner = newOwner;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EAM_MetamodelPackage.PRODUCT__OWNER, oldOwner,
					owner));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDepartment(String newDepartment) {
		String oldDepartment = department;
		department = newDepartment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EAM_MetamodelPackage.PRODUCT__DEPARTMENT,
					oldDepartment, department));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EAM_Application> getApplication() {
		if (application == null) {
			application = new EObjectWithInverseResolvingEList.ManyInverse<EAM_Application>(EAM_Application.class, this,
					EAM_MetamodelPackage.PRODUCT__APPLICATION, EAM_MetamodelPackage.APPLICATION__PRODUCT);
		}
		return application;
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
		case EAM_MetamodelPackage.PRODUCT__APPLICATION:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getApplication()).basicAdd(otherEnd, msgs);
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
		case EAM_MetamodelPackage.PRODUCT__APPLICATION:
			return ((InternalEList<?>) getApplication()).basicRemove(otherEnd, msgs);
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
		case EAM_MetamodelPackage.PRODUCT__OWNER:
			return getOwner();
		case EAM_MetamodelPackage.PRODUCT__DEPARTMENT:
			return getDepartment();
		case EAM_MetamodelPackage.PRODUCT__APPLICATION:
			return getApplication();
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
		case EAM_MetamodelPackage.PRODUCT__OWNER:
			setOwner((String) newValue);
			return;
		case EAM_MetamodelPackage.PRODUCT__DEPARTMENT:
			setDepartment((String) newValue);
			return;
		case EAM_MetamodelPackage.PRODUCT__APPLICATION:
			getApplication().clear();
			getApplication().addAll((Collection<? extends EAM_Application>) newValue);
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
		case EAM_MetamodelPackage.PRODUCT__OWNER:
			setOwner(OWNER_EDEFAULT);
			return;
		case EAM_MetamodelPackage.PRODUCT__DEPARTMENT:
			setDepartment(DEPARTMENT_EDEFAULT);
			return;
		case EAM_MetamodelPackage.PRODUCT__APPLICATION:
			getApplication().clear();
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
		case EAM_MetamodelPackage.PRODUCT__OWNER:
			return OWNER_EDEFAULT == null ? owner != null : !OWNER_EDEFAULT.equals(owner);
		case EAM_MetamodelPackage.PRODUCT__DEPARTMENT:
			return DEPARTMENT_EDEFAULT == null ? department != null : !DEPARTMENT_EDEFAULT.equals(department);
		case EAM_MetamodelPackage.PRODUCT__APPLICATION:
			return application != null && !application.isEmpty();
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
		result.append(" (owner: ");
		result.append(owner);
		result.append(", department: ");
		result.append(department);
		result.append(')');
		return result.toString();
	}

} //ProductImpl
