package spm.com.luxlignum;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.List;


public class MainActivity extends Activity {

    private static String PC_KEY = "PC_KEY";

    private PowerControl pc;
    private Camera cam;
    private SurfaceTexture st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(savedInstanceState != null && savedInstanceState.get(PC_KEY) != null){
            this.pc = (PowerControl) savedInstanceState.getSerializable(PC_KEY);
        } else{
            this.pc = new PowerControl();
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if(pc != null){
            outState.putSerializable(PC_KEY, pc);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {

        if(!this.getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
            //Show a dialog that the app won't work because no flashlight is available and quit the app on confirmation
            showUnsupportedDialog();
        }

        if(pc != null){
            switch(pc.getCurrentState()){
                case ON:
                    turnOnCamera();
                    break;
                case OFF:
                    turnOffCamera();
                    break;
                default:
                    turnOffCamera();
                    break;
            }
        }

        super.onStart();

    }

    @Override
    protected void onPause() {

        turnOffCamera();

        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void powerButtonClicked(View view){

        if(pc.isOff()){

            if(turnOnCamera()){
                pc.setCurrentState(PowerControl.POWER_STATE.ON);
            }

        } else{

            if(turnOffCamera()) {
                pc.setCurrentState(PowerControl.POWER_STATE.OFF);
            }

        }

        updatePowerButton(pc.getCurrentState());

    }

    private void updatePowerButton(PowerControl.POWER_STATE state){

        ImageButton powerButton = (ImageButton)findViewById(R.id.powerButton);

        switch(state){
            case ON:
                powerButton.setBackgroundResource(R.drawable.on_button);
                break;
            case OFF:
                powerButton.setBackgroundResource(R.drawable.off_button);
                break;
            default:
                powerButton.setBackgroundResource(R.drawable.off_button);
                break;
        }
    }

    private boolean turnOffCamera(){

        if(cam != null){
            cam.stopPreview();
            cam.release();
            cam = null;
            st = null;

            return true;
        }
        else{
            return false;
        }

    }

    private boolean turnOnCamera(){

        cam = Camera.open();
        Camera.Parameters camParams = cam.getParameters();

        List<String> flashModes = camParams.getSupportedFlashModes();

        //Camera has to support FLASH_MODE_TORCH
        if(camParams.getFlashMode() == null || !flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)){
            showUnsupportedDialog();
            turnOffCamera();
            return false;
        }

        camParams.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        cam.setParameters(camParams);

        try {
            st = new SurfaceTexture(0);
            cam.setPreviewTexture(st);
        }
        catch(IOException ioe){
            return false;
        }

        cam.startPreview();

        return true;

    }

    private void showUnsupportedDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.unsupported);

        builder.setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog unsupportedDialog = builder.create();

        unsupportedDialog.show();
    }

}
