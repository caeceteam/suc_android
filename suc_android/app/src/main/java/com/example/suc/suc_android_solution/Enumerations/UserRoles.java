package com.example.suc.suc_android_solution.Enumerations;

import android.content.res.Resources;

/**
 * Created by efridman on 27/8/17.
 */

public enum UserRoles {
    EMPLOYEE("employee",1),
    COLABORATOR("colaborator",2);

    private final String role;
    private final Integer roleValue;

    UserRoles(String _role, Integer _roleValue) {
        this.role = _role;
        this.roleValue = _roleValue;
    }

    public String getRole(){
        return this.role;
    }

    public static UserRoles from(String _role) throws Resources.NotFoundException {
        for (UserRoles role: values()) {
            if(role.role.equalsIgnoreCase(_role)){
                return role;
            }
        }

        throw new Resources.NotFoundException(String.format("No se encontro concordancia entre %s y los roles de la clase", _role));
    }

    public static UserRoles from(Integer _roleValue) throws Resources.NotFoundException {
        for (UserRoles role: values()) {
            if(role.roleValue.equals(_roleValue)){
                return role;
            }
        }

        throw new Resources.NotFoundException(String.format("No se encontro concordancia entre %d y los roles de la clase", _roleValue));
    }
}
