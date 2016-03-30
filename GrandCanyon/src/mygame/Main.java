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
    public static final float RESTITUTION = 0.5f;

    public static void main(String[] args) {
        Main app = new Main();
        DisplaySettings.initDisplay(app);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        initPhysics();
        
        w = new World(this, "Scenes/Grand_Canyon.jpg", 4097);
        
        aps = new DisplaySettings(this, w);
        
        s = new Sky(this, "Textures/Sky/Bright/BrightSky.dds", false, aps.getMainLight());
        water = new Water(this, w,  aps.getMainLight());
        w.attachWater(water);
        raft = new Raft(this, w);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //w.setWaterHeight(tpf, 190);
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
