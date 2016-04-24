/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package world;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import mygame.CanyonMode;
import mygame.CanyonRunMode;

/**
 *
 * @author Robyn
 */
public class Course {
    private CanyonMode msa;
    private Material mat;
    private Node nodeStart, nodeFinish;
    private Geometry geomStartBox, geomFinishBox;
    private GhostControl physStart, physFinish;
    private StartLine startLine;
    private FinishLine finishLine;
    
    private Vector3f positions[];
    private int posCounter;
    private int posTraversal;
    
    private Vector3f startPos, finishPos;
    
    public Course(CanyonMode m, World w, int size) {
        msa = m;
        startPos = new Vector3f(-190f, w.getWaterHeight(), -135f);
//        startPos = new Vector3f(0f, w.getWaterHeight() + 5f, 20f);
//        finishPos = new Vector3f(2090f, w.getWaterHeight(), 820f); // original finish line
        finishPos = new Vector3f(660f, w.getWaterHeight(), -1066f);
        
        positions = new Vector3f[size];
        posCounter = 0;
        posTraversal = 0;
        
        initCourse();
    }
    
    private void initCourse() {
        
        mat = new Material(msa.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat.setTexture("DiffuseMap", msa.getAssetManager().loadTexture("Textures/checkeredflag1.jpg"));
        
        startLine = new StartLine(msa, startPos, mat);
        finishLine = new FinishLine(msa, finishPos, mat);
    }
    
    public void addPosition(Vector3f pos){
        positions[posCounter] = pos;
    }
    
    public void addPositions(Vector3f pos[]){
        for(int i = 0; i < pos.length; i++){
            positions[posCounter] = pos[i];
            posCounter++;
        }
    }
    
    public Vector3f getNextPos(){
       Vector3f ret = positions[posTraversal];
       posTraversal++;
       return ret;
    }    
}
