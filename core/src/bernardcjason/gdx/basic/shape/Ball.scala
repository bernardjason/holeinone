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
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.VertexAttributes.Usage

case class Ball(val textureName:String,val startPosition: Vector3 = new Vector3, val radius: Float = 4f, var movement: Movement) extends Basic {

  lazy val texture = Common.assets.get(textureName,classOf[Texture])

  lazy val material = new Material(TextureAttribute.createDiffuse(texture), ColorAttribute.createSpecular(1, 1, 1, 1),
    FloatAttribute.createShininess(8f));

  val attributes = Usage.Position | Usage.Normal | Usage.TextureCoordinates 

  lazy val genModel = modelBuilder.createSphere(radius, radius, radius, 8, 8, material, attributes)

  val shape: CollideShape = new CollideSphere(radius)

  override def move(objects: List[Basic]) = {
    movement.move(objects, this)
  }

  override def collision(other: Basic) {
    movement.collision(this)
  }

  def dispose() {
    genModel.dispose();
  }

}