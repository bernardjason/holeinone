package bernardcjason.gdx.basic.shape

import com.badlogic.gdx.math.collision.Ray
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox
import com.badlogic.gdx.graphics.Camera

trait CollideShape {
  
  def intersects(transform:Matrix4 , ray:Ray ) :Float 
  
  val position = new Vector3();
  val center = new Vector3();
  val radius:Float
  
  def isVisible(transform:Matrix4, cam:Camera ) : Boolean =   {
    cam.frustum.sphereInFrustum(transform.getTranslation(position).add(center), radius);
  }
}

case class CollideSphere(radius:Float) extends CollideShape {
  val intersection = new Vector3
  override def intersects(transform:Matrix4 , ray:Ray ) :Float = {
        transform.getTranslation(position).add(center);
        
        if ( Intersector.intersectRaySphere(ray, position, radius, intersection) == true ) {
          val len = ray.direction.dot(position.x-ray.origin.x, position.y-ray.origin.y, position.z-ray.origin.z);
          if ( len < 0 ) return Float.MaxValue
          return len
        } 
        Float.MaxValue
    }  
}