package com.employee.enums;
/**
 * 
 * @author lapto
 *
 */
public enum EmployeeStatusEnum {
	ADDED,
	IN_CHECK,
	APPROVED,
	ACTIVE;

	EmployeeStatusEnum() {
    }

    public String getStatus() {
        return this.name();
    }
    
    public static boolean isType(String type) {
    	EmployeeStatusEnum[] types = EmployeeStatusEnum.values();
        for (EmployeeStatusEnum t : types) {
            if (t.name().equals(type)) {
                return true;
            }
        }
        return false;
    }
    
    public static EmployeeStatusEnum getType(String type) {
    	EmployeeStatusEnum[] types = EmployeeStatusEnum.values();
        for (EmployeeStatusEnum t : types) {
            if (t.name().equals(type)) {
                return t;
            }
        }
        return null;
    }
}
