package world;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
/**
 *
 * @author Graeme
 */
public class World {
    
    private Texture hMapImage;
    
    private AbstractHeightMap map;
    private Material mapMat;
    
    private SimpleApplication sa;
    
    public World(SimpleApplication sa){
        this.sa = sa;
        
        initMapTextures();
        initMap();
    }
    
    /*
     * Create the physical height map out  of the already inited 
     * height map image.
    */
    private void initMap(){
        //Create the height map out of the image we have
        map = new ImageBasedHeightMap(hMapImage.getImage(), 1.0f);
        map.load();
    }
    
    private void initMapTextures(){
        //First load the Height Map Image
        hMapImage = sa.getAssetManager().loadTexture("Scenes/Grand_Canyon.jpg");
        
        //Second init the material framework
        mapMat = new Material(sa.getAssetManager(), "Common/MatDefs/Terrain/Terrain.j3md");
        
        //Third assign our materials to the varying levels of the heightmap;
        Texture grass = sa.getAssetManager().loadTexture("Textures/Terrain/splat/grass.jpg");
        grass.setWrap(Texture.WrapMode.Repeat);
        mapMat.setTexture("Tex1", grass);
        mapMat.setFloat("Tex1Scale", 64f);
        
        Texture rock = sa.getAssetManager().loadTexture("Textures/Terrain/splat/road.jpg");
        grass.setWrap(Texture.WrapMode.Repeat);
        mapMat.setTexture("Tex2", grass);
        mapMat.setFloat("Tex2Scale", 96f);
        
        Texture dirt = sa.getAssetManager().loadTexture("Textures/Terrain/splat/dirt.jpg");
        grass.setWrap(Texture.WrapMode.Repeat);
        mapMat.setTexture("Tex3", grass);
        mapMat.setFloat("Tex3Scale", 32f);
        
    }
}
