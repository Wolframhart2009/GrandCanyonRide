/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package world;

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
        initPhysics();
    }
    
    private void initCourse() {
        
        mat = new Material(msa.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat.setTexture("DiffuseMap", msa.getAssetManager().loadTexture("Textures/checkeredflag1.jpg"));
        
        Box b = new Box(1,1,1);        
        
        // place starting line
        nodeStart = new Node();
        nodeStart.setLocalTranslation(startPos);
        geomStartBox = new Geometry("line start", b);
        geomStartBox.scale(60,5,1);
        geomStartBox.setMaterial(mat);
//        geomStartBox.setCullHint(Spatial.CullHint.Always);
        
        Quaternion rot = new Quaternion();
        rot.fromAngleAxis(-1 * FastMath.PI / 16, Vector3f.UNIT_Y);
        nodeStart.setLocalRotation(rot);
        
        nodeStart.attachChild(geomStartBox);
        msa.getRootNode().attachChild(nodeStart);
        
        // place finish line
        nodeFinish = new Node();
        nodeFinish.setLocalTranslation(finishPos);
        geomFinishBox = new Geometry("line finish", b);
//        geomFinishBox.scale(70,5,1); // original finish line dimensions
        geomFinishBox.scale(60,5,1);
        geomFinishBox.setMaterial(mat);
//        geomFinishBox.setCullHint(Spatial.CullHint.Always);
        
        rot = new Quaternion();
        rot.fromAngleAxis(-1 * FastMath.PI / 4, Vector3f.UNIT_Y);
        nodeFinish.setLocalRotation(rot);
        geomFinishBox.setMaterial(mat);
        
        nodeFinish.attachChild(geomFinishBox);
        msa.getRootNode().attachChild(nodeFinish);
    }
    
    private void initPhysics() {
        physStart = new CourseLineGhostControl(msa, this, new BoxCollisionShape(geomStartBox.getLocalScale()));
        geomStartBox.addControl(physStart);
        msa.bullet.getPhysicsSpace().add(physStart);
        physStart.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_04);
        physStart.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_03);
        
        physFinish = new CourseLineGhostControl(msa, this, new BoxCollisionShape(geomFinishBox.getLocalScale()));
        geomFinishBox.addControl(physFinish);
        msa.bullet.getPhysicsSpace().add(physFinish);
        physFinish.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_04);
        physFinish.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_03);
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
    
    public void hideLine(String lineName) {
        if(lineName.equals("line start")) {
            msa.bullet.getPhysicsSpace().remove(physStart);
            geomStartBox.setCullHint(Spatial.CullHint.Always);
        } else if(lineName.equals("line finish")) {
            msa.bullet.getPhysicsSpace().remove(physFinish);
            geomFinishBox.setCullHint(Spatial.CullHint.Always);
        }
    }
}
