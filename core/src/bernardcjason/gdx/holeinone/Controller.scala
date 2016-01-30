package bernardcjason.gdx.holeinone

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3

import bernardcjason.gdx.basic.BaseController
import bernardcjason.gdx.basic.Common
import bernardcjason.gdx.basic.move.Movement
import bernardcjason.gdx.basic.shape.Basic
import bernardcjason.gdx.basic.shape.Cuboid

object Controller extends BaseController {
  lazy val modelBatch = new ModelBatch();

  lazy val myAssets: Array[scala.Tuple2[String, Class[_]]] = Array(
    ("data/terrain.jpg", classOf[Texture]),
    ("data/textarget.png", classOf[Texture]),
    ("data/gun.jpg", classOf[Pixmap]))

  Common.terrain = FlatTerrain("data/terrain.jpg")

  lazy val gun: Cuboid = Cuboid("data/gun.jpg", new Vector3(0, 0, -10), new Vector3(1, 1, 4), movement = new Movement {
    override def move(objects: List[Basic], me: Basic) = {
      val speed = 4f
      val oldGun = new Matrix4
      val hereNow = new Vector3
      oldGun.set(Controller.gun.instance.transform)
      gun.instance.transform.translate(InputHandler.userAction.x * Gdx.graphics.getDeltaTime * speed, 0, 0)
      gun.instance.transform.rotate(1, 0, 0, InputHandler.userAction.y)
      var rotation = new Quaternion
      gun.instance.transform.getRotation(rotation)

      gun.instance.transform.getTranslation(hereNow)

      cam.project(hereNow)

      if (rotation.getPitch < 0 || rotation.getRoll != 0 ||
        hereNow.x <= 0 || hereNow.x >= Gdx.graphics.getWidth) {
        gun.instance.transform.set(oldGun)
      }
    }
    override def collision(me: Basic) = {}
  }, radius = 0)

  val targetSize = 8f;
  val targetStart = new Vector3(0, Common.terrain.terrainGroundZero + targetSize, -30)
  lazy val target = new Target(targetStart, targetSize)

  objects ++= Array(
    Common.terrain, gun, target.aTarget)

  def maxObjects = objects.size

  override def create {
    Common.loadAssetsForMe(myAssets)

    cameraEnvironment

    for (o <- objects) o.init

    for (o <- objects) o.reset

    this.cam.position.set(0, 10, 5)
    this.cam.update()
  }

  override def render() {

    for (o <- objects) o.move(objects.toList)

    modelBatch.begin(cam);

    for (o <- objects) {
      if (o.display) modelBatch.render(o.instance, environment);
    }

    modelBatch.end();

    doDeadList
    doNewList
  }

  def dispose {
    modelBatch.dispose();

  }

}