/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import obstacles.FallingRock;
import obstacles.FloatingLog;
import raft.Raft;
import world.Course;
import world.Sky;
import world.Water;
import world.World;

/**
 *
 * @author Graeme
 */
public class ExploreMode extends CanyonMode{
    
    public ExploreMode(){
        super();
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
    }

    @Override
    public void cleanup() {
        super.cleanup();
    }
    
    

}
