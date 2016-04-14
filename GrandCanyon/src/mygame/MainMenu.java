/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.jme3.ui.Picture;
import gui.Button;

/**
 *
 * @author Graeme
 */
public class MainMenu extends AbstractAppState{
    public static final float BUTTON_PADDING = 100;
    
    private AppStateManager asm;
    private Main app;
    
    private Picture menuImage;
    private BitmapFont lFont;
    private BitmapFont sFont;
    private BitmapText text;  
    private Button[] buttons = new Button[3];
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = (Main) app;
        this.asm = stateManager;
        
        this.app.getFlyByCamera().setEnabled(false);
        
        initImage();
        initText();
        initButtons();
        
    }
    
    private void initImage(){
        menuImage = new Picture("Start Screen");
        menuImage.setImage(app.getAssetManager(), "Interface/Introsplash.jpg", true);
        menuImage.setWidth(DisplaySettings.screenX);
        menuImage.setHeight(DisplaySettings.screenY);
        menuImage.move(0, 0, -1); //Move to background
        this.app.getGuiNode().attachChild(menuImage);
    }
    
    private void initText(){
        this.lFont = app.getAssetManager().loadFont("Interface/Fonts/CenturySchoolbook.fnt");
        this.sFont = null; //If we need to assign a smaller font
                
        text = new BitmapText(lFont, false);
        text.setColor(ColorRGBA.Black);
        text.setSize(40f);
        text.setText("Grand Canyon Run");
        text.setLocalTranslation(DisplaySettings.screenX/2 - 150, DisplaySettings.screenY - 50, 0);
        text.move(0, 0, 1); //Move to foreground
        app.getGuiNode().attachChild(text);
    }
    
    private void initButtons(){
        buttons[0] = new Button(app, app.getGuiNode(), DisplaySettings.screenX/4 + (BUTTON_PADDING * 1.5f), DisplaySettings.screenY/2, 50, 256);
        buttons[0].setIdle("Interface/Button/TestIdle.png", false);
        buttons[0].setActive("Interface/Button/TestActive.png", false);
        buttons[0].setText(lFont, "River Run");
        buttons[0].finish();
        
        buttons[1] = new Button(app, app.getGuiNode(), DisplaySettings.screenX/4 + (BUTTON_PADDING * 1.5f), DisplaySettings.screenY/2 - BUTTON_PADDING, 50, 256);
        buttons[1].setIdle("Interface/Button/TestIdle.png", false);
        buttons[1].setActive("Interface/Button/TestActive.png", false);
        buttons[1].setText(lFont, "Exploration");
        buttons[1].addClickTransition(this, new ExploreMode());
        buttons[1].finish();
    
        buttons[2] = new Button(app, app.getGuiNode(), DisplaySettings.screenX/4 + (BUTTON_PADDING * 1.5f), DisplaySettings.screenY/2 - BUTTON_PADDING * 2, 50, 256);
        buttons[2].setIdle("Interface/Button/TestIdle.png", false);
        buttons[2].setActive("Interface/Button/TestActive.png", false);
        buttons[2].setText(lFont, "Quit Game");
        buttons[2].finish();
    }
    
    @Override
    public void cleanup(){
        super.cleanup();
        app.getStateManager().detach(this);
        app.getGuiNode().detachAllChildren();
        app.getRootNode().detachAllChildren();
    }

    @Override
    public void update(float tpf) {
        if(buttons[2].isClicked()){
            this.cleanup();
            System.exit(0);
        }
    } 
    
}
