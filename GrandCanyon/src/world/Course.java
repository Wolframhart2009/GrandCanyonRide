/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package world;

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
import com.jme3.scene.shape.Box;
import mygame.CanyonMode;
import mygame.Main;

/**
 *
 * @author Robyn
 */
public class Course {
    private CanyonMode msa;
    private Material mat;
    private Node nodeStart, nodeFinish;
    private Geometry geomStart, geomFinish;
    private GhostControl physStart, physFinish;
    
    private Vector3f positions[];
    private int posCounter;
    private int posTraversal;
    
    private Vector3f startPos, finishPos;
    
    public Course(CanyonMode m, World w, int size) {
        msa = m;
        startPos = new Vector3f(-190f, w.getWaterHeight(), -135f);
//        startPos = new Vector3f(0f, w.getWaterHeight() + 5f, 20f);
        finishPos = new Vector3f(2090f, w.getWaterHeight(), 820f);
        
        positions = new Vector3f[size];
        posCounter = 0;
        posTraversal = 0;
        
        initCourse();
//        initPhysics();
    }
    
    private void initCourse() {
        
        mat = new Material(msa.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", new ColorRGBA(0.5f, 0.5f, 0.5f, 0.25f));
//        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        
        Box b = new Box(1,1,1);
        
        // place starting line
        Node nodeStart = new Node();
        nodeStart.setLocalTranslation(startPos);
        geomStart = new Geometry("start line", b);
        geomStart.scale(60,10,1);
        geomStart.setMaterial(mat);
//        geomStart.setQueueBucket(Bucket.Transparent);
        
        Quaternion rot = new Quaternion();
        rot.fromAngleAxis(-1 * FastMath.PI / 16, Vector3f.UNIT_Y);
        nodeStart.setLocalRotation(rot);
        
        nodeStart.attachChild(geomStart);
        msa.getRootNode().attachChild(nodeStart);
        
        // place finish line
        Node nodeFinish = new Node();
        nodeFinish.setLocalTranslation(finishPos);
        geomFinish = new Geometry("finish line", b);
        geomFinish.scale(70,10,1);
        geomFinish.setMaterial(mat);
        
//        rot = new Quaternion();
//        rot.fromAngleAxis(0, Vector3f.UNIT_Y);
//        nodeFinish.setLocalRotation(rot);
        
        nodeFinish.attachChild(geomFinish);
        msa.getRootNode().attachChild(nodeFinish);
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
    
    private void placeLine() {
        
        
    }
    
    
    
}
