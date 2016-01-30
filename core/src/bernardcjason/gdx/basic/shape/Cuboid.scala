package bernardcjason.gdx.basic.shape

import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.math.Vector3
import bernardcjason.gdx.basic.move._
import bernardcjason.gdx.basic.Common
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g3d.Attribute
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute

case class Cuboid(val textureName:String,val startPosition:Vector3 = new Vector3,val dimensions:Vector3,
    val radius:Float=4f,var movement:Movement) extends Basic {
  
  lazy val texture = createBlockTexture(textureName)
  lazy val genModel = makeCuboid(texture,dimensions.x,dimensions.y,dimensions.z) 
  val shape:CollideShape  = new CollideSphere(radius)
 
  override def move(objects:List[Basic])  = {
    movement.move(objects, this)  
  }
  
  override def collision(other:Basic) {
    movement.collision(this)
  }
  
  def dispose() {
    genModel.dispose();
  }

  def makeCuboid(texture:Texture, x:Float=2f,y:Float,z:Float): Model = {
    val attr = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates;
    
    val textureBlockWidth = texture.getWidth/6
    val textureBlockHeight = texture.getHeight
    
    modelBuilder.begin();
    
    val mesh = modelBuilder.part("box", GL20.GL_TRIANGLES, attr, new Material(TextureAttribute.createDiffuse(texture)));
   
    val textureregion = Array(
        new TextureRegion(texture,textureBlockWidth*0,0,textureBlockWidth,textureBlockHeight),
        new TextureRegion(texture,textureBlockWidth*1,0,textureBlockWidth,textureBlockHeight), 
        new TextureRegion(texture,textureBlockWidth*2,0,textureBlockWidth,textureBlockHeight),
        new TextureRegion(texture,textureBlockWidth*3,0,textureBlockWidth,textureBlockHeight),
        new TextureRegion(texture,textureBlockWidth*4,0,textureBlockWidth,textureBlockHeight),
        new TextureRegion(texture,textureBlockWidth*5,0,textureBlockWidth,textureBlockHeight)
      )
    mesh.setUVRange(textureregion(0));
    mesh.rect(-x,-y,-z,   -x,y,-z,  x,y,-z, x,-y,-z, 0,0,-1);
    mesh.setUVRange(textureregion(1));
    mesh.rect(-x,y,z, -x,-y,z,  x,-y,z, x,y,z, 0,0,1);
    mesh.setUVRange(textureregion(2));
    mesh.rect(-x,-y,z, -x,-y,-z,  x,-y,-z, x,-y,z, 0,-1,0);
    mesh.setUVRange(textureregion(3));
    mesh.rect(-x,y,-z, -x,y,z,  x,y,z, x,y,-z, 0,1,0);
    mesh.setUVRange(textureregion(4));
    mesh.rect(-x,-y,z, -x,y,z,  -x,y,-z, -x,-y,-z, -1,0,0);
    mesh.setUVRange(textureregion(5));
    mesh.rect(x,-y,-z, x,y,-z,  x,y,z, x,-y,z, 1,0,0);
    
    val model = modelBuilder.end();
    model.materials.get(0).set(new BlendingAttribute(true,1))

    model
  }

  

/** creates a texture for use in this class that has had the list of regions rotated to match expectation.
 *  imagine a jpg with a list of 6 areas, 1 2 3 4 5 6.   Method here in case some manipulation wanted
 * @param textureName which is expected to be a loaded asset
 * @return
 */
def createBlockTexture(textureName: String) = {
    val pixmap = Common.assets.get(textureName,classOf[Pixmap])
    
    val textureBlockWidth=pixmap.getWidth/6
    val textureBlockHeight=pixmap.getHeight
        
    val texture = new Texture(pixmap.getWidth,pixmap.getHeight,pixmap.getFormat)

    for(i <- 0 to 5 ) {
      val pixmapX=i*textureBlockWidth
         
      val oneBitOfTexture = new Pixmap(textureBlockWidth, textureBlockHeight, pixmap.getFormat);
      oneBitOfTexture.drawPixmap(pixmap, 0,0,pixmapX, 0,textureBlockWidth,textureBlockHeight)     
      
      texture.draw(oneBitOfTexture, pixmapX, 0)
      oneBitOfTexture.dispose()
    } 
    texture
  }
}