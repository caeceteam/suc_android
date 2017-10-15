package com.example.suc.suc_android_solution.Enumerations;

import android.content.res.Resources;

/**
 * Created by efridman on 14/10/17.
 */

public enum MailType {
    REGISTRATION(0),
    REGISTRATION_APPROVED(1),
    REGISTRATION_REJECTED(2),
    NO_VALIDATABLE_REGISTRATION(3);

    private final Integer value;

    public Integer getValue(){
        return this.value;
    }

    MailType(Integer _value) {
        this.value = _value;
    }

    public static MailType from(Integer _value) throws Resources.NotFoundException {
        for (MailType mailType: values()) {
            if(mailType.value.equals(_value)){
                return mailType;
            }
        }

        throw new Resources.NotFoundException(String.format("No se encontro concordancia entre %d y los mail type de la clase", _value));
    }
}
