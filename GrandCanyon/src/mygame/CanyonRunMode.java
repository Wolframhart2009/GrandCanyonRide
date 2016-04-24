/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import gui.Bar;
//import gui.Timer;
import java.util.Random;

/**
 *
 * @author Graeme
 */
public class CanyonRunMode extends CanyonMode{
    private AudioNode damage1;
    private AudioNode damage2;
    private int hitpoints;
    private Bar hitpointsBar;
//    private Timer timerGUI;
    
    private BitmapFont timerFont;
    private BitmapText timerText;
    
    private float time;
    private float timer, score;
    private boolean gameOver;
    private boolean recovery;
    private boolean courseActive;

    boolean counting = false;
    
    public CanyonRunMode(){
        super();
        this.hitpoints = 11; //Remember to change the bar if one gives more or less than this
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        initHitBar();
        initTimerText();
        this.gameOver = false;
        
        this.initAudio();
        this.initCourse();
        this.initRapids();
        this.initFallingRocks();
        this.initFloatingLogs();
    }
    
    private void initAudio(){
        damage1 = new AudioNode(app.getAssetManager(), "Sound/Raft/Damage_1.wav", false);
        damage1.setLooping(false);
        damage1.setPositional(false);
        damage1.setVolume(5.0f);
        app.getRootNode().attachChild(damage1);
        
        damage2 = new AudioNode(app.getAssetManager(), "Sound/Raft/Damage_2.wav", false);
        damage2.setLooping(false);
        damage2.setPositional(false);
        damage2.setVolume(5.0f);
        app.getRootNode().attachChild(damage2);    
    }
    
    private void initHitBar(){
        hitpointsBar = new Bar(app, app.getGuiNode(), 
                DisplaySettings.screenX/8 - 100, DisplaySettings.screenY/8 - 75, 
                50, 256, hitpoints + 1, false);
        
        hitpointsBar.initTotal("Interface/Bar/Bar", ".png");
        hitpointsBar.setCurrentDisplay(hitpoints);
        hitpointsBar.finish();
    }
    
    public int getHitpoints(){
        return hitpoints;
    }
    
    public void adjustHitPoints(int newValue){
        hitpoints = newValue;
        hitpointsBar.setCurrentDisplay(newValue);
        if(hitpoints > 7){
            damage1.playInstance();
        } else{
            damage2.playInstance();
        }
    }
    
    public void decHitPoints(){
        adjustHitPoints(hitpoints - 1);
    }
    
    private void initTimerText() {
//        System.out.println("initializing timer");
        timerFont = app.getAssetManager().loadFont("Interface/Fonts/CenturySchoolbook.fnt");
        timerText = new BitmapText(timerFont, false);
        timerText.setColor(ColorRGBA.White);
        timerText.setSize(40f);
        timerText.setText("Time: 0.00 sec");
        timerText.setLocalTranslation(DisplaySettings.screenX/2 - 150, DisplaySettings.screenY, 0);
        timerText.move(0, 0, 1); //Move to foreground
        app.getGuiNode().attachChild(timerText);
    }
    
    public void setRecovery(){
        this.recovery = true;
    }
    
    public boolean getRecovery(){
        return this.recovery;
    }
        
    public void startTimer() {
        System.out.println("starting timer, " + timer);
        timer = 0;
        courseActive = true;
    }
    
    public void stopTimer() {
        System.out.println("stopping timer, " + timer);
        courseActive = false;
        score = timer;
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        
        if(courseActive) {
            timer += tpf;
//            System.out.println("Elapsed time: " + timer);
            
            String str = String.format("Time: %3.2f sec", timer);
            timerText.setText(str);
            
            float randomFloat = random.nextFloat();
            if(randomFloat < ROCKFALL_PROBABILITY) {
                this.addRock(raft.getPos());
            }
        }
        
        if(!courseActive && timer > 0) {
            gameOver = true;
        }
        
        if(!(tpf > 1.0)){
           time+= tpf;
        }
        
//        if(time >= 2 && gameOver == false){
//            time = 0;
//            this.decHitPoints();
//        }
        
        if(gameOver == false && recovery == true){
            
            if(counting == false){
                counting = true;
                time = 0;
            }
            
            if(time > 5){
                recovery = false;
                counting = false;
            }
        }
        
        if(hitpoints == 0 && gameOver == false){
            time = 0;
            gameOver = true;
        }
        
        if(gameOver){
            if(time >= 5.0){
                GameOverScreen gos = new GameOverScreen(timer);
                
                this.cleanup();
                asm.detach(this);
                asm.attach(gos);
            }
        }
    }
    
    
}
