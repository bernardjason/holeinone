package bernardcjason.gdx.basic.move

import bernardcjason.gdx.basic.shape.Basic
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3
import bernardcjason.gdx.basic.BaseController
import bernardcjason.gdx.basic.Log

case class BulletMovement(objectToControl: Basic, direction: Vector3, controller: BaseController, val speed: Float = 10f) extends Movement {

  private val translation = new Vector3
  override def move(objects: List[Basic], me: Basic) {

    me.oldPosition.set(me.instance.transform)

    translation.set(direction)
    translation.scl(Gdx.graphics.getDeltaTime() * speed)

    me.instance.transform.translate(translation)

    me.collisionCheck(objects)
  }

  override def collision(me: Basic) {
    Log.info(s"Exploded!!!!!!! ${me}")
    translation.setZero()
    controller.addToDead(objectToControl)
  }
}
