package obstacles;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Box;
import java.util.Random;
import mygame.CanyonMode;
import world.World;

public class Rapids extends Node{
    public static final Box RapidMesh = new Box(4f, .25f, 4f);
    
    private CanyonMode sa;
    private World world;
    
    private Geometry rapidGeom;
    private Material rapidMat;
    private RapidEmitter[] emit = new RapidEmitter[4];
            
    
    public Rapids(CanyonMode s, World w, Vector3f location){
        this.sa = s;
        this.world = w;
        
        initMat();
        initGeom();
        initEmitters();
        this.setLocalTranslation(location);
        //System.out.println("Rapid added at: " + location);
    }
    
    private void initGeom(){
        rapidGeom = new Geometry("Rapids", RapidMesh);
        rapidGeom.setMaterial(rapidMat);
        this.attachChild(rapidGeom);
        
        sa.getRootNode().attachChild(this);
        rapidGeom.setLocalTranslation(0, 0, 0);
        rapidGeom.setCullHint(Spatial.CullHint.Always); //This makes it invisible
    }
    
    private void initEmitters(){
        Random ran = new Random();
        
        emit[0] = new RapidEmitter(sa, this);
        emit[1] = new RapidEmitter(sa, this);
        emit[2] = new RapidEmitter(sa, this);
        emit[3] = new RapidEmitter(sa, this);
        
        for(int i = 0; i < 4; i ++){
            float ranX = (ran.nextFloat() * 10) - 5;
            float ranY = (ran.nextFloat() * 10) - 5;
            
            emit[i].setLocalTranslation(ranX,
                                     -.5f,
                                        ranY);
        }
       
    }
    
    private void initMat(){
        rapidMat = new Material(sa.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        rapidMat.setColor("Color", ColorRGBA.Blue);
    }
}
