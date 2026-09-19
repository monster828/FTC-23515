package org.firstinspires.ftc.teamcode.Utils.Controls;

public class ControlsTrigger {
    private Boolean _triggered = false;

    private Boolean _buttonPressed = false;

    /**
     * Creates an instance of the trigger
    **/
    public ControlsTrigger(){  }

    public void UpdateTrigger(Boolean _pressed){
        if (_pressed && !_buttonPressed){
            _triggered = true;
        }

        _buttonPressed = _pressed;
    }

    /**
     * This function will return true once before it returns false
     * **/
    public Boolean IsTriggered(){
        Boolean _returnTrigger = _triggered;
        _triggered = false;
        return _returnTrigger;
    }

    /**
     * This will just return if the triggered button was pressed
     * **/
    public Boolean IsButtonPressed(){
        return _buttonPressed;
    }
}
