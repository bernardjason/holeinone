package bernardcjason.gdx.basic.shape

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.VertexAttributes.Usage
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.math.Vector3
import bernardcjason.gdx.basic.move._
import bernardcjason.gdx.basic.BaseController

case class Bullet(val startPosition: Vector3 = new Vector3, val startdirection: Vector3, controller: BaseController, val MAX_DISTANCE: Float = 100f) extends Basic {
  val radius: Float = 4f
  val ballRadius = radius / 2f
  val direction = startdirection.cpy()
  var movement: Movement = BulletMovement(this, direction, controller)
  val material = new Material(ColorAttribute.createDiffuse(Color.RED))
  val shape: CollideShape = new CollideSphere(radius)

  val attributes = Usage.Position | Usage.Normal | Usage.TextureCoordinates;

  lazy val genModel = modelBuilder.createSphere(ballRadius, ballRadius, ballRadius, 4, 4, material, attributes)

  override def move(objects: List[Basic]) = {
    movement.move(objects, this)
    if (distanceTravelled > MAX_DISTANCE) {
      controller.addToDead(this)
    }
  }

  override def collision(other: Basic) {
    movement.collision(this)
  }

  def dispose() {
    genModel.dispose();
  }
}