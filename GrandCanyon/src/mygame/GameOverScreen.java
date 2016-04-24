/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioNode;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.ui.Picture;
import gui.Button;

/**
 *
 * @author Robyn
 */
public class GameOverScreen extends AbstractAppState {
    public static final float BUTTON_PADDING = 100;
    
    private AppStateManager asm;
    private Main app;
    
    private Picture menuImage;
    private BitmapFont lFont;
    private BitmapFont sFont;
    private BitmapText text, scoreText;  
    private Button[] buttons = new Button[2];
    
    private AudioNode mainMusic;
    private AudioNode buttonClick;
    
    private float score = 0;
    
    public GameOverScreen(float score) {
        this.score = score;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = (Main) app;
        this.asm = stateManager;
        
        this.app.getFlyByCamera().setEnabled(false);
        
        initImage();
        initText();
        initButtons();
        initAudio();
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
        text.setText("Game Over");
        text.setLocalTranslation(DisplaySettings.screenX/2 - 60, DisplaySettings.screenY - 50, 0);
        text.move(0, 0, 1); //Move to foreground
        app.getGuiNode().attachChild(text);
        
        if(score > 0) {
            scoreText = new BitmapText(lFont, false);
            scoreText.setColor(ColorRGBA.White);
            scoreText.setSize(40f);
            String str = String.format("Your score: %3.2f sec", score);
            scoreText.setText(str);
            scoreText.setLocalTranslation(DisplaySettings.screenX/2 - 150, DisplaySettings.screenY - 150, 0);
            scoreText.move(0, 0, 1); //Move to foreground
            app.getGuiNode().attachChild(scoreText);
        }
    }
    
    private void initButtons(){        
//        buttons[0] = new Button(app, app.getGuiNode(), DisplaySettings.screenX/4 + (BUTTON_PADDING * 1.5f), DisplaySettings.screenY/2 - BUTTON_PADDING, 50, 256);
//        buttons[0].setIdle("Interface/Button/TestIdle.png", false);
//        buttons[0].setActive("Interface/Button/TestActive.png", false);
//        buttons[0].setText(lFont, "Main Menu");
//        buttons[0].addClickTransition(this, new MainMenu());
//        buttons[0].addClickSound(app.getRootNode(), "Sound/Button/buttonclick.wav");
//        buttons[0].finish();
    
//        buttons[1] = new Button(app, app.getGuiNode(), DisplaySettings.screenX/4 + (BUTTON_PADDING * 1.5f), DisplaySettings.screenY/2 - BUTTON_PADDING * 2, 50, 256);
//        buttons[1].setIdle("Interface/Button/TestIdle.png", false);
//        buttons[1].setActive("Interface/Button/TestActive.png", false);
//        buttons[1].setText(lFont, "Quit Game");
//        buttons[1].addClickSound(app.getRootNode(), "Sound/Button/buttonclick.wav");
//        buttons[1].finish();
    }
    
    public void initAudio(){
        mainMusic = new AudioNode(app.getAssetManager(), "Music/Mainmenu/Relax_music.wav", false);
        mainMusic.setLooping(true);
        mainMusic.setPositional(false);
        mainMusic.setVolume(3.0f);
        app.getRootNode().attachChild(mainMusic);
        mainMusic.play();
    }
    
    public void setScore(float score) {
        this.score = score;
    }
    
    @Override
    public void cleanup(){
        super.cleanup();
        mainMusic.stop();
        app.getStateManager().detach(this);
        app.getGuiNode().detachAllChildren();
        app.getRootNode().detachAllChildren();
    }

    @Override
    public void update(float tpf) {
//        if(buttons[1].isClicked()){
//            this.cleanup();
//            System.exit(0);
//        }
    } 
    
}