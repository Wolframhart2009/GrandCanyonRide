/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.jme3.app.Application;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import mygame.DisplaySettings;

/**
 *
 * @author Robyn
 */
public class Timer extends Node {
    Application sa;
    private Node pNode;
    private float x, y;
    private float height, width;
    private BitmapFont font;
    private BitmapText text;
    
    public Timer(Application s, Node parent, float x, float y, float height, float width) {
        this.sa = s;
        this.pNode = parent;
        this.x = x; 
        this.y = y;
        this.height = height;
        this.width = width;
        this.font = sa.getAssetManager().loadFont("Interface/Fonts/CenturySchoolbook.fnt");
        
        initText();
    }
    
    private void initText() {
        text = new BitmapText(font);
        text.setColor(ColorRGBA.White);
        text.setSize(40f);
        text.setText("Time: 0.00 sec");
        float margin = (text.getLineHeight() / 2);
        float lineX = DisplaySettings.screenX/2 + margin;
        float lineY = DisplaySettings.screenY/2 + margin;
        text.setLocalTranslation(lineX, lineY, 0f);
        text.move(0, 0, 1); //Move to foreground
        this.pNode.attachChild(this);
    }
    
    public void setText(float time){
        text = new BitmapText(font);
        text.setColor(ColorRGBA.White);
        text.setSize(40f);
        String str = String.format("Time: %3.2f sec", time);
        text.setText(str);
    }
    
    public void finish() {
        parent.attachChild(this);
    }
}
