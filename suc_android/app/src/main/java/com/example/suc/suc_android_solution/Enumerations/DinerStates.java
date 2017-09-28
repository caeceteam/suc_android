package com.example.suc.suc_android_solution.Enumerations;

import android.content.res.Resources;

/**
 * Created by efridman on 24/9/17.
 */

public enum DinerStates {
    INACTIVE("inactive",-1),
    PENDING("pending",0),
    APPROVED("approved",1),
    REJECTED("rejected",2);

    private final String state;
    private final Integer stateValue;

    DinerStates(String _state, Integer _stateValue) {
        this.state = _state;
        this.stateValue = _stateValue;
    }

    public String getState(){
        return this.state;
    }

    public static DinerStates from(String _state) throws Resources.NotFoundException {
        for (DinerStates state: values()) {
            if(state.state.equalsIgnoreCase(_state)){
                return state;
            }
        }

        throw new Resources.NotFoundException(String.format("No se encontro concordancia entre %s y los roles de la clase", _state));
    }

    public static DinerStates from(Integer _stateValue) throws Resources.NotFoundException {
        for (DinerStates state: values()) {
            if(state.stateValue.equals(_stateValue)){
                return state;
            }
        }

        throw new Resources.NotFoundException(String.format("No se encontro concordancia entre %d y los roles de la clase", _stateValue));
    }
}
