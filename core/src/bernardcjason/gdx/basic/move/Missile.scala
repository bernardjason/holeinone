package bernardcjason.gdx.basic.move

import bernardcjason.gdx.basic.shape.Basic
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3
import bernardcjason.gdx.basic.BaseController
import bernardcjason.gdx.basic.Log

case class Missile(objectToControl: Basic, direction: Vector3, controller: BaseController, val speed: Float = 1f, val force: Float) extends Movement {

  val gravity = 0.03f
  var forceCounter = force
  private val translation = new Vector3

  override def move(objects: List[Basic], me: Basic) {
    me.raydirection.set(direction)

    me.oldPosition.set(me.instance.transform)

    translation.set(direction)
    translation.scl(Gdx.graphics.getDeltaTime() * speed * force)
    translation.x = 0

    me.instance.transform.translate(translation)
    me.raydirection.set(translation)

    direction.y = direction.y - gravity

    me.collisionCheck(objects)

  }

  override def collision(me: Basic) {
    Log.info(s"Missile exploded!!!!!!! ${me}")
    translation.setZero()
    controller.addToDead(objectToControl)
  }
}
