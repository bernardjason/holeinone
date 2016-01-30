package bernardcjason.gdx.basic.shape

import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.math.collision.Ray
import bernardcjason.gdx.basic.move.Movement
import bernardcjason.gdx.basic.Common
import bernardcjason.gdx.basic.Log

abstract class Basic {
  val modelBuilder = new ModelBuilder();
  val startPosition: Vector3

  var movement: Movement
  val radius: Float
  val shape: CollideShape
  val genModel: Model

  lazy val instance = new ModelInstance(genModel);
  val raydirection = new Vector3(0, 0, 0)
  val oldPosition = new Matrix4
  val tmpPositionHolder = new Vector3
  val originalMatrix4 = new Matrix4

  private var flashCounter = 0

  val id = Basic.getId

  override def hashCode: Int = {
    return id
  }
  override def equals(that: Any): Boolean =
    that match {
      case that: Basic => that.id == this.id
      case _           => false
    }

  def flash = {
    flashCounter = 60
  }

  def display: Boolean = {
    if (flashCounter > 0) {
      flashCounter = flashCounter - 1
      if (flashCounter % 3 == 1) return false
    }
    true
  }
  def init {
    originalMatrix4.set(instance.transform)
  }

  def reset = {
    instance.transform.set(originalMatrix4)
    instance.transform.trn(startPosition)
  }

  def move(objects: List[Basic]): Unit

  def collision(other: Basic) {

  }

  def collisionCheck(objects: List[Basic]) = {
    var collided = false

    val ray = new Ray(instance.transform.getTranslation(tmpPositionHolder), raydirection)

    for (o <- objects) {
      if (this != o) {
        val len = o.shape.intersects(o.instance.transform, ray)
        if (len != Float.MaxValue && (len < radius || len < o.radius)) {
          println("-----------------",o,len,radius,o.radius)
          collision(o)
          o.collision(this)
          collided = true
        }
      }

    }
    if (collided == false && Common.terrain != null) {
      if (Common.terrain.hitMe(tmpPositionHolder, radius)) {
        collision(Common.terrain)
        Log.info("hit terrain")
      }
    }
  }

  def rollback = {
    instance.transform.set(oldPosition)
  }

  def distanceTravelled = {
    instance.transform.getTranslation(tmpPositionHolder)
    startPosition.dst(tmpPositionHolder)
  }
}

object Basic {
  var id = 0;
  def getId = {
    id = id + 1
    id - 1
  }
}