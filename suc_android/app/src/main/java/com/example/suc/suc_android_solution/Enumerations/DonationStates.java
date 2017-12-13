package com.example.suc.suc_android_solution.Enumerations;

import android.content.res.Resources;

/**
 * Created by efridman on 13/12/17.
 */

public enum DonationStates {
    PENDING("Pendiente",0),
    APPROVED("Aprobada",1),
    REJECTED("Rechazada",2);

    private final String state;
    private final Integer stateValue;

    DonationStates(String _state, Integer _stateValue) {
        this.state = _state;
        this.stateValue = _stateValue;
    }

    public String getState(){
        return this.state;
    }

    public static DonationStates from(String _state) throws Resources.NotFoundException {
        for (DonationStates state: values()) {
            if(state.state.equalsIgnoreCase(_state)){
                return state;
            }
        }

        throw new Resources.NotFoundException(String.format("No se encontro concordancia entre %s y los estados de la clase", _state));
    }

    public static DonationStates from(Integer _stateValue) throws Resources.NotFoundException {
        for (DonationStates state: values()) {
            if(state.stateValue.equals(_stateValue)){
                return state;
            }
        }

        throw new Resources.NotFoundException(String.format("No se encontro concordancia entre %d y los estados de la clase", _stateValue));
    }
}
