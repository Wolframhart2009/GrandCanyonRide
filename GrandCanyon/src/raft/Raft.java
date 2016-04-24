/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package raft;

import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Cylinder;
import mygame.CanyonMode;
import world.World;

/**
 *
 * @author Robyn
 */
public class Raft {
    private CanyonMode msa;
    private Material matRaft;
    private Node nodeRaft;
    private Geometry[] geoms; // holds a geometry for each log in the raft
    private Vector3f startPosition;
    
    private VehicleControl raftVehicleControl;
    private final float accelerationForce = 5000.0f;
    private float steeringValue;
    private float accelerationValue;
    private float speed;
    
    private static final int NUM_LOGS_IN_RAFT = 5;
    private static final float RAFT_LENGTH = 5;
    private static final float RAFT_WIDTH = 3;
    private static final float RAFT_HEIGHT = 1;
    private static final float MAX_SPEED = 0;
    
    public Raft(CanyonMode m, World w) {
        msa = m;
        startPosition = new Vector3f(0f, w.getWaterHeight() + 1f, 20f);
        steeringValue = 0;
        accelerationValue = 0;
        
        initRaft();
        initKeys();
        initPhysics();
    }
    
    private void initRaft() {
        geoms = new Geometry[NUM_LOGS_IN_RAFT];
        
        nodeRaft = new Node("Raft");
//        nodeRaft.setLocalTranslation(startPosition);
        
        matRaft = new Material(msa.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        matRaft.setTexture("DiffuseMap", msa.getAssetManager().loadTexture("Textures/log1.jpg"));
        
        Cylinder log = new Cylinder(32,32,1,10,true);
        
        for(int i = 0; i < NUM_LOGS_IN_RAFT; i++) {
            geoms[i] = new Geometry("log" + i, log);
            geoms[i].setMaterial(matRaft);
//            geoms[i].setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
            geoms[i].setLocalTranslation(-2.5f + (1.25f * i), 0f, 0f);
        }
        
        for(Geometry geom : geoms) {
            nodeRaft.attachChild(geom);
        }
    }
    
    private void initKeys() {
        msa.getInputManager().addMapping("Forward", new KeyTrigger(KeyInput.KEY_I));
        msa.getInputManager().addMapping("Back", new KeyTrigger(KeyInput.KEY_K));
        msa.getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_J));
        msa.getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_L));
        msa.getInputManager().addMapping("Pos", new KeyTrigger(KeyInput.KEY_P));
        msa.getInputManager().addListener(actionListener, new String[]{"Forward", "Back", "Left", "Right", "Pos"});
//        msa.getInputManager().addListener(analogListener, new String[]{"Forward", "Back"});
    }
    
    private void initPhysics() {
//        CollisionShape raftShape = CollisionShapeFactory.createMeshShape(nodeRaft);
//        raftVehicleControl = new VehicleControl(raftShape, 400f);
//        nodeRaft.addControl(raftVehicleControl);
        
        CompoundCollisionShape raftShape = new CompoundCollisionShape();
        BoxCollisionShape box = new BoxCollisionShape(new Vector3f(RAFT_WIDTH, RAFT_HEIGHT, RAFT_LENGTH));
        raftShape.addChildShape(box, new Vector3f(0, 0, 0));
        raftVehicleControl = new VehicleControl(raftShape, 800f);
        nodeRaft.addControl(raftVehicleControl);
        
        float stiffness = 100.0f;//200=f1 car
        float compValue = .3f; //(should be lower than damp)
        float dampValue = 10f;
        raftVehicleControl.setSuspensionCompression(compValue * 2.0f * FastMath.sqrt(stiffness));
        raftVehicleControl.setSuspensionDamping(dampValue * 2.0f * FastMath.sqrt(stiffness));
        raftVehicleControl.setSuspensionStiffness(stiffness);
        raftVehicleControl.setMaxSuspensionForce(10000.0f);
        
        //Create four wheels and add them at their locations
        Vector3f wheelDirection = new Vector3f(0, -1, 0); // was 0, -1, 0
        Vector3f wheelAxle = new Vector3f(-1, 0, 0); // was -1, 0, 0
        float radius = 1.75f;
        float restLength = 0.3f;
        float yOff = 0.8f;
        float xOff = 2.5f;
        float zOff = 4f;
        Vector3f wheelPosition;
        
        Cylinder wheelMesh = new Cylinder(16, 16, radius, radius * 0.6f, true);
        
        Material mat = new Material(msa.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", ColorRGBA.Red);

        Node node1 = new Node("wheel 1 node");
        Geometry wheel1 = new Geometry("wheel 1", wheelMesh);
        wheel1.rotate(0, FastMath.HALF_PI, 0);
        wheel1.setMaterial(mat);
        wheelPosition = new Vector3f(-xOff, yOff, zOff);
        node1.attachChild(wheel1);
        raftVehicleControl.addWheel(node1, wheelPosition, wheelDirection, 
                wheelAxle, restLength, radius, true);

        Node node2 = new Node("wheel 2 node");
        Geometry wheel2 = new Geometry("wheel 2", wheelMesh);
        wheel2.rotate(0, FastMath.HALF_PI, 0);
        wheel2.setMaterial(mat);
        wheelPosition = new Vector3f(xOff, yOff, zOff);
        node2.attachChild(wheel2);
        raftVehicleControl.addWheel(node2, wheelPosition, wheelDirection, 
                wheelAxle, restLength, radius, true);

        Node node3 = new Node("wheel 3 node");
        Geometry wheel3 = new Geometry("wheel 3", wheelMesh);
        wheel3.rotate(0, FastMath.HALF_PI, 0);
        wheel3.setMaterial(mat);
        wheelPosition = new Vector3f(-xOff, yOff, -zOff);
        node3.attachChild(wheel3);
        raftVehicleControl.addWheel(node3, wheelPosition, wheelDirection, 
                wheelAxle, restLength, radius, false);

        Node node4 = new Node("wheel 4 node");
        Geometry wheel4 = new Geometry("wheel 4", wheelMesh);
        wheel4.rotate(0, FastMath.HALF_PI, 0);
        wheel4.setMaterial(mat);
        wheelPosition = new Vector3f(xOff, yOff, -zOff);
        node4.attachChild(wheel4);
        raftVehicleControl.addWheel(node4, wheelPosition, wheelDirection, 
                wheelAxle, restLength, radius, false);

        nodeRaft.attachChild(node1);
        nodeRaft.attachChild(node2);
        nodeRaft.attachChild(node3);
        nodeRaft.attachChild(node4);
        msa.getRootNode().attachChild(nodeRaft);
        
        node1.setCullHint(Spatial.CullHint.Always);
        node2.setCullHint(Spatial.CullHint.Always);
        node3.setCullHint(Spatial.CullHint.Always);
        node4.setCullHint(Spatial.CullHint.Always);
        
        msa.bullet.getPhysicsSpace().add(raftVehicleControl);
        raftVehicleControl.setPhysicsLocation(startPosition);
        
        raftVehicleControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_03);
        raftVehicleControl.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_02);
        raftVehicleControl.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_01);
    }
    
    public Vector3f getPos(){
        return nodeRaft.getLocalTranslation();
    }
    
    public Quaternion getRot(){
        return nodeRaft.getLocalRotation();
    }
    
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String binding, boolean isPressed, float tpf) {
            if (binding.equals("Left")) {
                if (isPressed) {
                    steeringValue += .5f;
                } else {
                    steeringValue += -.5f;
                }
                raftVehicleControl.steer(steeringValue);
            } else if (binding.equals("Right")) {
                if (isPressed) {
                    steeringValue += -.5f;
                } else {
                    steeringValue += .5f;
                }
                raftVehicleControl.steer(steeringValue);
            } else if (binding.equals("Forward")) {
                if (isPressed) {
                    accelerationValue += accelerationForce;
                } else {
                    accelerationValue -= accelerationForce;
                }
                speed = raftVehicleControl.getCurrentVehicleSpeedKmHour();
//                System.out.println("current speed = " + speed);
                raftVehicleControl.accelerate(accelerationValue);
            } else if (binding.equals("Back")) {
                if (isPressed) {
                    accelerationValue -= accelerationForce;
                } else {
                    accelerationValue += accelerationForce;
                }
                speed = raftVehicleControl.getCurrentVehicleSpeedKmHour();
//                System.out.println("current speed = " + speed);
                raftVehicleControl.accelerate(accelerationValue);
            } else if (binding.equals("Pos")) {
                if (isPressed) {
//                    System.out.println(nodeRaft.getLocalTranslation());
                }
            }
        }
    };

}
