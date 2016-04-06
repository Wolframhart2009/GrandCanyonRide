package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import obstacles.Rapids;
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
    
            boolean addedRapid = false;
    
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
        w.addShadows(aps.getMainLight());
        
        s = new Sky(this, "Textures/Sky/Bright/BrightSky.dds", false, aps.getMainLight());
        water = new Water(this, w,  aps.getMainLight());
        w.attachWater(water);
        raft = new Raft(this, w);
    }

    @Override
    public void simpleUpdate(float tpf) {
        if(!(tpf > 1.0) && addedRapid == false){
            //w.setWaterHeight(tpf, -350);
            new Rapids(this, w, new Vector3f(0, -360, 20));
            addedRapid = true;
        }
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
