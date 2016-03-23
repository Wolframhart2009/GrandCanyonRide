package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import world.Sky;
import world.Water;
import world.World;

/**
 * 
 * @author Graeme
 */
public class Main extends SimpleApplication {
    
    DisplaySettings aps;
    
    World w;
    Sky s;
    Water water;
    Raft raft;
    
    public BulletAppState bullet;

    public static void main(String[] args) {
        Main app = new Main();
        DisplaySettings.initDisplay(app);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        aps = new DisplaySettings(this);
        
        initPhysics();
        
        w = new World(this, "Scenes/Grand_Canyon.jpg", 4097);

        s = new Sky(this, "Textures/Sky/Bright/BrightSky.dds", false);
        water = new Water(this, w, aps.getMainLight());
        
        raft = new Raft(this);
    }

    @Override
    public void simpleUpdate(float tpf) {
        
    }

    @Override
    public void simpleRender(RenderManager rm) {
        
    }
    
    private void initPhysics() {
        bullet = new BulletAppState();
        stateManager.attach(bullet);
        bullet.setDebugEnabled(true);
    }
}
