package bernardcjason.gdx.holeinone

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Vector3
import bernardcjason.gdx.basic.shape.Bullet
import com.badlogic.gdx.math.Quaternion
import bernardcjason.gdx.basic.move.Missile
import com.badlogic.gdx.input.GestureDetector.GestureListener
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector2
import bernardcjason.gdx.basic.Log
import com.badlogic.gdx.InputProcessor
import bernardcjason.gdx.basic.shape.Bullet

object InputHandler extends GestureDetector.GestureAdapter with InputProcessor {

  val userAction = new Vector3

  def keyDown(c: Int): Boolean = {
    if (Gdx.input.isKeyPressed(Keys.LEFT)) {
      userAction.x = userAction.x - 1
    }
    if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
      userAction.x = userAction.x + 1
    }
    if (Gdx.input.isKeyPressed(Keys.DOWN)) {
      userAction.y = userAction.y - 1
    }
    if (Gdx.input.isKeyPressed(Keys.UP)) {
      userAction.y = userAction.y + 1
    }

    if (Gdx.input.isKeyPressed(Keys.SPACE)) {
      startGunFire = System.currentTimeMillis()
    }

    true
  }

  override def pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean = {

    userAction.x = deltaX
    userAction.y = -deltaY

    return false;
  }
  override def panStop(x: Float, y: Float, pointer: Int, button: Int): Boolean = {

    userAction.setZero()

    return false;
  }

  var diff: Float = 0f
  def keyUp(c: Int): Boolean = {
    if (c == Keys.SPACE) {
      diff = System.currentTimeMillis - startGunFire
      if (diff == 0) diff = 0.01f
      fireGun(diff)
    } else
      userAction.setZero()

    true
  }

  var startGunFire = 0L

  def fireGun(howLong: Float) = {
    var rotation = new Quaternion
    Controller.gun.instance.transform.getRotation(rotation)
    val axis = new Vector3(0, 0, 1)

    val a = rotation.getAngle - 180
    axis.rotate(a, 1, 1, 0)
    axis.x = 0
    val position = new Vector3
    Controller.gun.instance.transform.getTranslation(position)
    position.add(0, axis.y * 5, axis.z * 5)
    val b = Bullet(position, axis, Controller, MAX_DISTANCE = 300f)
    b.movement = Missile(b, axis, Controller, speed=20f, howLong / 100)

    Controller.addNewBasic(b)
  }

  def keyTyped(x$1: Char): Boolean = true
  def mouseMoved(x$1: Int, x$2: Int): Boolean = true
  def scrolled(x$1: Int): Boolean = true
  def touchDown(x$1: Int, x$2: Int, x$3: Int, x$4: Int): Boolean = {
    startGunFire = System.currentTimeMillis()
    true
  }
  def touchDragged(x$1: Int, x$2: Int, x$3: Int): Boolean = true
  def touchUp(x$1: Int, x$2: Int, x$3: Int, x$4: Int): Boolean = {
    diff = System.currentTimeMillis - startGunFire
    if (diff > 100) fireGun(diff)
    true
  }

}