package bernardcjason.gdx.basic

import com.badlogic.gdx.graphics.g3d.utils.CameraInputController
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import scala.collection.mutable.ArrayBuffer
import bernardcjason.gdx.basic.shape.Basic

trait BaseController {
  lazy val environment = new Environment();
  lazy val cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

  def cameraEnvironment {
   environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.set(new ColorAttribute(ColorAttribute.Fog, 0.07f, 0.07f, 0.07f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		
    cam.position.set(0, 0, 0);
    cam.lookAt(0, 0, 0);
    cam.near = 1f;
    cam.far = 800;
    cam.update();

  }

  val objects: ArrayBuffer[Basic] = ArrayBuffer()
  val deadlist: ArrayBuffer[Basic] = ArrayBuffer()
  val newlist: ArrayBuffer[Basic] = ArrayBuffer()

  def addNewBasic(basic:Basic) {
    newlist += basic
  }
  
  def addToDead(remove:Basic) {
    deadlist += remove
  }
  
  def doDeadList {
    for(d <- deadlist) {      
      objects -= d
    }
    deadlist.clear()
  }
  
  def doNewList {
    for(b <-  newlist ) {
      b.init
      b.reset
      objects += b
    }
    newlist.clear()
  }
  def create() = {}

  def render() {}
  
  def pause() {
    System.exit(0)
  }
}