package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import world.World;

/**
 * 
 * @author Graeme
 */
public class Main extends SimpleApplication {
    
    DisplaySettings aps;
    
    World w;

    public static void main(String[] args) {
        Main app = new Main();
        DisplaySettings.initDisplay(app);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        aps = new DisplaySettings(this);
        
        w = new World(this);
    }

    @Override
    public void simpleUpdate(float tpf) {
        
    }

    @Override
    public void simpleRender(RenderManager rm) {
        
    }
}
