package com.noahcharlton.robogeddon.ui.mainmenu;

import com.noahcharlton.robogeddon.settings.KeySetting;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.event.KeyEvent;
import com.noahcharlton.robogeddon.ui.widget.TextButton;

public class KeySettingButton extends TextButton {

    private final KeySetting setting;

    public KeySettingButton(String text, KeySetting setting) {
        super(text);

        this.setting = setting;
    }

    @Override
    public void update() {
        super.update();

        if(isCurrentlyEditing()){
            setText("Press Any Key");
        }else{
            setText(setting.getButtonText());
        }
    }

    public boolean isCurrentlyEditing() {
        return client.getUi().getKeyFocus() == this;
    }

    @Override
    protected void onClick(ClickEvent event) {
        event.getUi().setKeyFocus(isCurrentlyEditing() ? null : this);
    }

    @Override
    public void onKeyEvent(KeyEvent keyEvent) {
        if(keyEvent.getKeyCode() != KeyEvent.NO_KEYCODE){
            keyEvent.getUi().setKeyFocus(null);
            setting.setValue(keyEvent.getKeyCode());
        }
    }
}
