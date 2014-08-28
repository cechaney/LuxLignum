package spm.com.luxlignum;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import spm.com.luxlignum.R;

public class PowerControl implements Serializable {

    public static enum POWER_STATE {
        ON,
        OFF
    };

    private POWER_STATE currentState = POWER_STATE.OFF;

    public POWER_STATE getCurrentState() {
        return currentState;
    }

    public void setCurrentState(POWER_STATE currentState) {
        this.currentState = currentState;
    }

    public boolean isOn(){
        if(this.currentState.equals(POWER_STATE.ON)){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isOff(){
        if(this.currentState.equals(POWER_STATE.OFF)){
            return true;
        }
        else{
            return false;
        }
    }


}
