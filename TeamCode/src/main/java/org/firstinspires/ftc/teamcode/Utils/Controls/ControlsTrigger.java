package org.firstinspires.ftc.teamcode.Utils.Controls;

import org.firstinspires.ftc.teamcode.Utils.GamepadButton;

public class ControlsTrigger {
    private Boolean _triggered = false;

    private Boolean _buttonPressed = false;
    private GamepadButton button;

    /**
     * Creates an instance of the trigger
    **/
    public ControlsTrigger(){  }
    /**
     * Creates an instance of the trigger with a specific button
     **/
    public ControlsTrigger(GamepadButton b){button = b;}

    public void UpdateTrigger(Boolean _pressed) {
        if (_pressed && !_buttonPressed){
            _triggered = true;
        }

        _buttonPressed = _pressed;
    }

    /**
     * Requires button to be specified on creation
     */
    public void UpdateTrigger() {
        if (button.isPressed() && !_buttonPressed){
            _triggered = true;
        }

        _buttonPressed = button.isPressed();
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
