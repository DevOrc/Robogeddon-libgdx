package com.noahcharlton.robogeddon.ui.widget;

public class PairLayout extends Widget {

    private Widget left;
    private Widget right;
    private int spacing = 10;

    @Override
    public void layout() {
        var height = Math.max(left.getHeight(), right.getHeight());
        var width = spacing + left.getWidth() + right.getWidth();

        left.setX(getX());
        left.setY(getY());
        right.setX(getX() + left.getWidth() + spacing);
        right.setY(getY());

//        System.out.println("Right: " + right + " " + right.getWidth() + "  left" + left.getWidth() );
        setWidthScaled(width);
        setHeightScaled(height);
    }

    private void updateChildren(){
        getChildren().clear();

        if(left != null)
            add(left);

        if(right != null)
            add(right);
    }

    public PairLayout setSpacing(int spacing) {
        this.spacing = spacing;
        invalidate();

        return this;
    }

    public PairLayout setLeft(Widget left) {
        this.left = left;
        invalidateParent();
        updateChildren();

        return this;
    }

    public PairLayout setRight(Widget right) {
        this.right = right;
        invalidateParent();
        updateChildren();

        return this;
    }
}
