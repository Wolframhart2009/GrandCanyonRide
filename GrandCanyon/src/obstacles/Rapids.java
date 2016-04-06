package obstacles;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import world.World;

public class Rapids extends Node{
    public static final Box RapidMesh = new Box(2f, .25f, 2f);
    
    private SimpleApplication sa;
    private World world;
    
    private Geometry rapidGeom;
    private Material rapidMat;
    private RapidEmitter[] emit = new RapidEmitter[2];
            
    
    public Rapids(SimpleApplication s, World w, Vector3f location){
        this.sa = s;
        this.world = w;
        
        initMat();
        initGeom(location);
        initEmitters(location);
        System.out.println("Rapid added at: " + location);
    }
    
    private void initGeom(Vector3f loc){
        rapidGeom = new Geometry("Rapids", RapidMesh);
        rapidGeom.setMaterial(rapidMat);
        this.attachChild(rapidGeom);
        
        sa.getRootNode().attachChild(this);
        rapidGeom.setLocalTranslation(loc);
        //rapidGeom.setCullHint(Spatial.CullHint.Always); //This makes it invisible
    }
    
    private void initEmitters(Vector3f loc){
        emit[0] = new RapidEmitter(sa, this);
        emit[1] = new RapidEmitter(sa, this);
        
        emit[0].setLocalTranslation(2,
                                     -.5f,
                                    2);
        
        emit[0].setLocalTranslation(-2,
                                     -.5f,
                                    -2);
        
//        emit[0].setLocalTranslation(loc.x + 1.0f,
//                                    -.5f,
//                                    loc.z + 1.0f);
//        
//        emit[0].setLocalTranslation(loc.x - 1.0f,
//                                    -.5f,
//                                    loc.z - 1.0f);
                
    }
    
    private void initMat(){
        rapidMat = new Material(sa.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        rapidMat.setColor("ColorMap", ColorRGBA.Blue);
    }
}
