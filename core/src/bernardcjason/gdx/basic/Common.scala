package bernardcjason.gdx.basic

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Vector3
import bernardcjason.gdx.basic.shape.Basic
import bernardcjason.gdx.basic.shape.BaseTerrain
import com.badlogic.gdx.Gdx

object Common {
  implicit class StringImprovements(val s: String) {
        def increment = s.map(c => (c + 1).toChar)
    }
  
  var terrain:BaseTerrain=null
  
  var assets = new AssetManager(); ;  
  
  def loadAssetsForMe(list:Array[ scala.Tuple2[String,Class[_]] ]) {
    
    for(l <- list) {
      assets.load(l._1,l._2)
      println(l)
      Log.info(s"loading ${l._1} ${l._2}")
    }
      
    assets.finishLoading()
  }
  

    
  def rotate(src:Pixmap ,angle:Float,srcX:Int,srcY:Int,width:Int,height:Int) = {

    val rotated = new Pixmap(width, height, src.getFormat());

    val radians = Math.toRadians(angle)
    val cos = Math.cos(radians)
    val sin = Math.sin(radians);

    if ( angle != 0 ) {
      for (x <- 0f to width.asInstanceOf[Float] by 1) {
          for (y <- 0f to height by 1) {
  
              val centerx = width/2
              val centery = height / 2
              val m = x - centerx
              val n = y - centery
              val j = (m * cos + n * sin) + centerx
              val k = (n * cos - m * sin) + centery
              if (j >= 0 && j < width && k >= 0 && k < height){
                var pixel = src.getPixel( (k+srcX).asInstanceOf[Int], (j+srcY).asInstanceOf[Int])
                rotated.drawPixel(width-x.asInstanceOf[Int], y.asInstanceOf[Int], pixel);
              }
          }
      }
    } else {
      rotated.drawPixmap(src, 0,0,srcX, srcY,width,height)
    }
    rotated;
  }
  
  
   def getObject (screenX:Int,  screenY:Int , cam:Camera , objects:List[Basic]) = {
        val ray = cam.getPickRay(screenX, screenY);
        var result = -1;
        var distance = -1f;
        val position = new Vector3
        val centre = new Vector3
        for (i <- 0 until objects.size ) {
            val o = objects(i);
            o.instance.transform.getTranslation(position);
            //o.bounds.getCenter(centre)
            position.add(centre);
            val dist2 = ray.origin.dst2(position);
            if ( !(distance >= 0f && dist2 > distance))  {
              if (Intersector.intersectRaySphere(ray, position, o.radius*0.5f, null)) {
                  result = i;
                  distance = dist2;
              }
            }
        }
        result;
    }
  
}