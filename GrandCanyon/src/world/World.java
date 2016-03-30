package world;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.collision.shapes.HeightfieldCollisionShape;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import mygame.Main;
/**
 *
 * @author Graeme
 */
public class World {
    public static final float NORMALIZE = 512;
    public static final int PATCH_SIZE = 65;
    public static final float INIT_WATER_HEIGHT = -(World.NORMALIZE)  + 150;
    public static final float TOLEARANCE = .05f;
    
    private Texture hMapImage;
    
    public int size;
    
    private float waterHeight;
    private Water water;
    private TerrainQuad physWorld;
    private AbstractHeightMap map;
    private Material mapMat;
    
    
    private Main msa;
    
    public World(Main m, String path, int size){
        msa = m;
        this.size = size;
        this.waterHeight = INIT_WATER_HEIGHT;
        
        initMapTextures(path);
        initMap(NORMALIZE);
        initWorld(size);
        initPhysics();
    }
    
    /*
     * 
     */
    private void initWorld(int s){
        physWorld = new TerrainQuad("Image_Terrain", PATCH_SIZE, s, map.getHeightMap());
        physWorld.setMaterial(mapMat);
        physWorld.setLocalTranslation(0, -NORMALIZE, 0);
        physWorld.setLocalScale(4f, 1f, 4f); //Random Scale not set in stone
        
        physWorld.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

        
        TerrainLodControl control = new TerrainLodControl(physWorld, msa.getCamera());
        physWorld.addControl(control);
        
        msa.getRootNode().attachChild(physWorld);
    }
    
    /*
     * Create the physical height map out  of the already inited 
     * height map image.
    */
    private void initMap(float normalVal){
        //Create the height map out of the image we have
        map = new ImageBasedHeightMap(hMapImage.getImage(), 1.0f); 
        map.load();
        map.normalizeTerrain(normalVal);
    }
    
    /*
     * Load all of the textures we will be using including the grayscale
     * heightmap and the ground textures.
    */  
    private void initMapTextures(String path){
        //First load the Height Map Image
        hMapImage = msa.getAssetManager().loadTexture(path);
        
        //Second init the material framework
        mapMat = new Material(msa.getAssetManager(), "Common/MatDefs/Terrain/HeightBasedTerrain.j3md");
        
        //Third assign our materials to the varying levels of the heightmap;
        Texture rock = msa.getAssetManager().loadTexture("Textures/splat/sandstone.jpg");
        rock.setWrap(Texture.WrapMode.Repeat);
        mapMat.setTexture("region1ColorMap", rock);
        
        Texture dirt = msa.getAssetManager().loadTexture("Textures/Terrain/splat/dirt.jpg");
        dirt.setWrap(Texture.WrapMode.Repeat);
        mapMat.setTexture("region2ColorMap", dirt);
        
        mapMat.setVector3("region1", new Vector3f(0, 5, 32f)); //startheight, endheight, scale
        mapMat.setVector3("region2", new Vector3f(5, NORMALIZE, 32f)); //startheight, endheight, scale
        
        mapMat.setFloat("terrainSize", 4096);
        mapMat.setFloat("slopeTileFactor", 16f);
        
    }
    
    private void initPhysics() {
//        MeshCollisionShape terrainShape = (MeshCollisionShape) CollisionShapeFactory.createMeshShape((Node) physWorld);
//        HeightfieldCollisionShape terrainShape = (HeightfieldCollisionShape) CollisionShapeFactory.createMeshShape((Node) physWorld);
//        MeshCollisionShape terrainShape =  new MeshCollisionShape(((Node)physWorld).getMesh());
//        RigidBodyControl terrainPhys = new RigidBodyControl(terrainShape, 0.0f);
        RigidBodyControl terrainPhys = new RigidBodyControl(0f);
        physWorld.addControl(terrainPhys);
        msa.bullet.getPhysicsSpace().add(terrainPhys);
    }
    
    public void attachWater(Water w){
        this.water = w;
    }
    
    public float getWaterHeight(){
        return this.waterHeight;
    }
    
    public void setWaterHeight(float value){
        //For creating a fast update to a given value
        this.waterHeight = value;
        if(this.water != null){
            water.updateWaterHeight();
        }
    }
    
    public void setWaterHeight(float tpf, float target){
        //For creating a slow update to target. Pass tpf.
        
        //System.out.printf("This is tolerance check value to see if we should move: %f\n", FastMath.abs(this.waterHeight - target));
        //System.out.printf("Old height: %f || New Height: %f || target: %f\n", this.waterHeight, this.waterHeight + tpf, target);
        
        if(!(FastMath.abs(this.waterHeight - target) <= TOLEARANCE)){
            if(target > this.waterHeight){
                this.waterHeight = this.waterHeight + tpf;
            }
            else{
                this.waterHeight = this.waterHeight - tpf;
            }

            if(this.water != null){
                water.updateWaterHeight();
            }
        }
    }
}
