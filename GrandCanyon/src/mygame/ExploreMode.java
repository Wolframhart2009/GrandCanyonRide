/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import world.Sky;
import world.Water;
import world.World;

/**
 *
 * @author Graeme
 */
public class ExploreMode extends AbstractAppState{
    
    private Main app;
    private AppStateManager asm;
    private BulletAppState bullet;
    
    private DisplaySettings aps;
    
    private World w;
    private Sky s;
    private Water water;
    private Raft raft;
    
    boolean addedRapid = false;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = (Main) app;
        this.asm = stateManager;
        
        this.app.getFlyByCamera().setEnabled(true);
        
        initPhysics();
        
        w = new World(this.app, "Scenes/Grand_Canyon.jpg", 4097);
        
        aps = new DisplaySettings(this.app, w);
        w.addShadows(aps.getMainLight());
        
        s = new Sky(this.app, "Textures/Sky/Bright/BrightSky.dds", false, aps.getMainLight());
        water = new Water(this.app, w,  aps.getMainLight());
        w.attachWater(water);
        raft = new Raft(this.app, w);
   }
    
    private void initPhysics() {
        bullet = new BulletAppState();
        app.getStateManager().attach(bullet);
        bullet.setDebugEnabled(true);
    }
    
   @Override
   public void cleanup(){
        super.cleanup();
        app.getStateManager().detach(bullet);
        app.getStateManager().detach(this);
        app.getGuiNode().detachAllChildren();
        app.getRootNode().detachAllChildren();
    }

}
