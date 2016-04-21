package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import obstacles.FallingRock;
import obstacles.FloatingLog;
import obstacles.Rapids;
import raft.Raft;
import world.Course;
import world.Sky;
import world.Water;
import world.World;

/**
 * 
 * @author Graeme
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        DisplaySettings.initDisplay(app);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        MainMenu menu = new MainMenu();
        stateManager.attach(menu);

    }

    @Override
    public void simpleUpdate(float tpf) {
        
    }

    @Override
    public void simpleRender(RenderManager rm) {
        
    }
    
}
