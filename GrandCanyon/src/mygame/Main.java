package mygame;

import raft.Raft;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import obstacles.FallingRock;
import obstacles.FloatingLog;
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

    private FallingRock rocks[];
    private FloatingLog logs[];
    
    private static int NUM_ROCKS = 1;
    private static int NUM_LOGS = 1;
    
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
        
        initFallingRocks();
        initFloatingLogs();
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
    
    private void initFallingRocks() {
        rocks = new FallingRock[NUM_ROCKS];
        
        for(int i = 0; i < NUM_ROCKS; i++) {
            rocks[i] = new FallingRock(this, w);
        }
    }
    
    private void initFloatingLogs() {
        logs = new FloatingLog[NUM_LOGS];
        
        for(int i = 0; i < NUM_LOGS; i++) {
            logs[i] = new FloatingLog(this, w);
        }
    }
}
