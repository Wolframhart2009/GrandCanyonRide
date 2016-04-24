/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import com.jme3.ui.Picture;
import java.awt.Font;
import mygame.DisplaySettings;


/**
 *
 * @author Graeme
 */
public class Button extends Node implements AnalogListener{
    
    Application sa;
    private Node pNode;
    private float x, y;
    private float height, width;
    
    private BitmapText text;
    private Picture idle;
    private Picture active;
    private boolean clicked, inside;
    
    private AudioNode click;
    
    AbstractAppState oldState;
    AbstractAppState newState;

    public Button(Application s, Node parent, float x, float y, float height, float width){
        this.sa = s;
        this.pNode = parent;
        this.x = x; 
        this.y = y;
        this.height = height;
        this.width = width;
        this.clicked = false; //Have I been clicked in the appropriate place
        this.inside = false; //Am I currently inside my button
        
        this.idle = new Picture("Idle Button");
        this.active = new Picture("Active Button");
        
        initMouse();
        
        attachToGUI();                
        this.setLocalTranslation(x, y, 0);
    }
    
    public void setIdle(String path, boolean useAlpha){
        idle.setImage(sa.getAssetManager(), path, useAlpha);
        idle.setHeight(height);
        idle.setWidth(width);
    }
    
    public void setActive(String path, boolean useAlpha){
        active.setImage(sa.getAssetManager(), path, useAlpha);
        active.setHeight(height);
        active.setWidth(width);
    }
    
    public void setText(BitmapFont font, String text){
        this.text = new BitmapText(font);
        this.text.setColor(ColorRGBA.Black);
        this.text.setText(text);
        this.text.setLocalTranslation(width/8, height, 0);
    }
    
    public void addClickSound(Node parent, String path){
        click = new AudioNode(sa.getAssetManager(), "Sound/Button/buttonclick.wav", false);
        click.setLooping(false);
        click.setPositional(false);
        click.setVolume(3.0f);
        parent.attachChild(click);
    }
    
    public void addClickTransition(AbstractAppState oldState, AbstractAppState newState){
        this.oldState = oldState;
        this.newState = newState;
    }
    
    public void finish(){
       this.addControl(new ButtonControl(this));
    }
    
    public boolean isClicked(){
        return clicked;
    }
    
    private void attachToGUI(){
        this.pNode.attachChild(this);
    }
    
    private void initMouse(){
        if(!sa.getInputManager().hasMapping("clicked")){
            sa.getInputManager().addMapping("clicked", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        }
        sa.getInputManager().addListener(this, "clicked");        
    }

    public void onAnalog(String name, float value, float tpf) {
        if(name.equals("clicked") && inside){
            clicked = true;
            if(click != null){
                click.playInstance();
            }
            if(oldState != null && newState != null){
                sa.getStateManager().detach(oldState);
                sa.getStateManager().attach(newState);
            }
        }
    }
    
    
    class ButtonControl extends AbstractControl{
        
        private Node controlNode;
        
        public ButtonControl(Node parent){
            this.controlNode = parent;
        }

        @Override
        protected void controlUpdate(float tpf) {
            float xPos = sa.getInputManager().getCursorPosition().x;
            float yPos = sa.getInputManager().getCursorPosition().y;
                        
            //Check if mouse is inside the button
            if((xPos > x && xPos < x + width) && (yPos > y && yPos < y + height)){
                controlNode.detachAllChildren();
                controlNode.attachChild(active);
                controlNode.attachChild(text);
                inside = true;
            }
            else
            {
                controlNode.detachAllChildren();
                controlNode.attachChild(idle);
                controlNode.attachChild(text);
                inside = false;
                clicked = false;
            }
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp){
            
        }
        
    }
    
}
