/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import gui.Bar;

/**
 *
 * @author Graeme
 */
public class CanyonRunMode extends CanyonMode{

    private int hitpoints;
    private Bar hitpointsBar;
    
    private float time;
    private boolean gameOver;
    
    public CanyonRunMode(){
        super();
        this.hitpoints = 11; //Remember to change the bar if one gives more or less than this
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        initHitBar();
        gameOver = false;
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
        
    }
    
    public void decHitPoints(){
        adjustHitPoints(hitpoints - 1);
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
    }

    @Override
    public void update(float tpf) {
        if(!(tpf > 1.0)){
           time+= tpf;
        }
        
//        if(time >= 2 && gameOver == false){
//            time = 0;
//            this.decHitPoints();
//        }
        
        if(hitpoints == 0 && gameOver == false){
            time = 0;
            gameOver = true;
        }
        
        if(gameOver){
            if(time >= 5.0){
                this.cleanup();
                asm.detach(this);
                asm.attach(new MainMenu());
            }
        }
    }
    
    
}
