package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import world.Sky;
import world.World;

/**
 * 
 * @author Graeme
 */
public class Main extends SimpleApplication {
    
    DisplaySettings aps;
    
    World w;
    Sky s;

    public static void main(String[] args) {
        Main app = new Main();
        DisplaySettings.initDisplay(app);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        aps = new DisplaySettings(this);
        
        w = new World(this, "Scenes/Grand_Canyon.jpg" ,4097);

        s = new Sky(this, "Textures/Sky/Bright/BrightSky.dds", false);
    }

    @Override
    public void simpleUpdate(float tpf) {
        
    }

    @Override
    public void simpleRender(RenderManager rm) {
        
    }
}
