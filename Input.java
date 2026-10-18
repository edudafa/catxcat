package com.edudafa.catxcat;

import android.graphics.Rect;


public class Input {
    private boolean
            isEnabled;
    int
            x1,
            x2,
            y1,
            y2;
    Rect
            rect;


    public Input(int x1, int y1, int x2, int y2){
        this.x1 = (int) (x1 * GraphicEngine.zoomX);
        this.y1 = (int) (y1 * GraphicEngine.zoomY);
        this.x2 = (int) (x2 * GraphicEngine.zoomX);
        this.y2 = (int) (y2 * GraphicEngine.zoomY);
        rect = new Rect(this.x1, this.y1, this.x2, this.y2);
    }

    public boolean isTouched() {
        if(isEnabled)
            if(GraphicEngine.pressed)
                return rect.contains((int) GraphicEngine.pressedX, (int) GraphicEngine.pressedY);
            else
                return false;
        else
            return false;
    }

    public void setEnabled() { isEnabled = true; }
    public void setDisabled() { isEnabled = false; }
}