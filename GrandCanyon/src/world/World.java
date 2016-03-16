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
    public static final float NORMALIZE = 512;
    
    private Texture hMapImage;
    
    TerrainQuad physWorld;
    private AbstractHeightMap map;
    private Material mapMat;
    
    private SimpleApplication sa;
    
    public World(SimpleApplication sa, String path, int size){
        this.sa = sa;
        
        initMapTextures(path);
        initMap(NORMALIZE);
        initWorld(size);
    }
    
    /*
     * 
     */
    private void initWorld(int s){
        physWorld = new TerrainQuad("Image_Terrain", 65, s, map.getHeightMap());
        physWorld.setMaterial(mapMat);
        physWorld.setLocalTranslation(0, -NORMALIZE, 0);
        physWorld.setLocalScale(4f, 1f, 4f); //Random Scale not set in stone
        
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
    private void initMapTextures(String path){
        //First load the Height Map Image
        hMapImage = sa.getAssetManager().loadTexture(path);
        
        //Second init the material framework
        mapMat = new Material(sa.getAssetManager(), "Common/MatDefs/Terrain/HeightBasedTerrain.j3md");
        
        //Third assign our materials to the varying levels of the heightmap;
        Texture rock = sa.getAssetManager().loadTexture("Textures/splat/sandstone.jpg");
        rock.setWrap(Texture.WrapMode.Repeat);
        mapMat.setTexture("region1ColorMap", rock);
        
        Texture dirt = sa.getAssetManager().loadTexture("Textures/Terrain/splat/dirt.jpg");
        dirt.setWrap(Texture.WrapMode.Repeat);
        mapMat.setTexture("region2ColorMap", dirt);
        
        mapMat.setVector3("region1", new Vector3f(0, 5, 32f)); //startheight, endheight, scale
        mapMat.setVector3("region2", new Vector3f(5, NORMALIZE, 32f)); //startheight, endheight, scale
        
        mapMat.setFloat("terrainSize", 4096);
        mapMat.setFloat("slopeTileFactor", 16f);
        
    }
}
