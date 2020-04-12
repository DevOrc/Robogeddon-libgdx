package com.noahcharlton.robogeddon.ui.ingame;

import com.noahcharlton.robogeddon.ui.UIAssets;
import com.noahcharlton.robogeddon.ui.background.ColorBackground;
import com.noahcharlton.robogeddon.ui.widget.Label;
import com.noahcharlton.robogeddon.ui.widget.Stack;
import com.noahcharlton.robogeddon.util.Selectable;

public class SelectableMenu extends Stack {

    private Selectable selectable;
    private final Label title = new Label().setFont(UIAssets.smallFont);
    private final Label desc = new Label().setFont(UIAssets.smallFont);
    private final Label details = new Label().setFont(UIAssets.smallFont);

    public SelectableMenu() {
        setBackground(new ColorBackground());
        setMinWidth(250);

        add(title.pad().top(10));
        add(desc.pad().bottom(10));
        add(details.pad().bottom(20));
    }

    @Override
    public void update() {
        if(this.selectable != client.getProcessor().getSelectable()){
            selectable = client.getProcessor().getSelectable();
            invalidate();
        }

        if(this.selectable != null && this.selectable.isInfoInvalid()){
            invalidate();
        }
    }

    @Override
    public void layout() {
        if(this.selectable == null)
            return;

        this.selectable.onInfoValidated();
        title.setText(selectable.getTitle());
        desc.setText(selectable.getDesc());
        details.setText(formatDebugInfo());

        super.layout();
    }

    private String formatDebugInfo() {
        StringBuilder builder = new StringBuilder();

        for(String string : selectable.getDetails()){
            builder.append(string);
            builder.append('\n');
        }

        return builder.toString();
    }
}
