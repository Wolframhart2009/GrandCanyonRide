package world;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
/**
 *
 * @author Graeme
 */
public class World {
    public static final float NORMALIZE = 255;
    
    private Texture hMapImage;
    
    TerrainQuad physWorld;
    private AbstractHeightMap map;
    private Material mapMat;
    
    private SimpleApplication sa;
    
    public World(SimpleApplication sa){
        this.sa = sa;
        
        initMapTextures();
        initMap(NORMALIZE);
        initWorld();
    }
    
    /*
     * 
     */
    private void initWorld(){
        physWorld = new TerrainQuad("Grand_Canyon", 65, 4097, map.getHeightMap());
        physWorld.setMaterial(mapMat);
        physWorld.setLocalTranslation(0, -NORMALIZE, 0);
        physWorld.setLocalScale(2f, 1f, 2f); //Random Scale not set in stone
        
        TerrainLodControl control = new TerrainLodControl(physWorld, sa.getCamera());
        physWorld.addControl(control);
        
        sa.getRootNode().attachChild(physWorld);
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
    private void initMapTextures(){
        //First load the Height Map Image
        hMapImage = sa.getAssetManager().loadTexture("Scenes/Grand_Canyon.jpg");
        
        //Second init the material framework
        mapMat = new Material(sa.getAssetManager(), "Common/MatDefs/Terrain/HeightBasedTerrain.j3md");
        
        //Third assign our materials to the varying levels of the heightmap;
        Texture grass = sa.getAssetManager().loadTexture("Textures/Terrain/splat/grass.jpg");
        grass.setWrap(Texture.WrapMode.Repeat);
        mapMat.setTexture("region1ColorMap", grass);
        
        Texture rock = sa.getAssetManager().loadTexture("Textures/Terrain/splat/road.jpg");
        rock.setWrap(Texture.WrapMode.Repeat);
        mapMat.setTexture("region2ColorMap", rock);
        
        Texture dirt = sa.getAssetManager().loadTexture("Textures/Terrain/splat/dirt.jpg");
        dirt.setWrap(Texture.WrapMode.Repeat);
        mapMat.setTexture("region3ColorMap", dirt);
        
        mapMat.setVector3("region1", new Vector3f(0, 31, 32f)); //startheight, endheight, scale
        mapMat.setVector3("region2", new Vector3f(32, 100, 32)); //startheight, endheight, scale
        mapMat.setVector3("region3", new Vector3f(101, NORMALIZE, 32f)); //startheight, endheight, scale
        
        mapMat.setFloat("terrainSize", 4096);
        mapMat.setFloat("slopeTileFactor", 32f);
        
    }
}
