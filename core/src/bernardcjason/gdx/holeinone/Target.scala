package bernardcjason.gdx.holeinone

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import bernardcjason.gdx.basic.move.Movement
import bernardcjason.gdx.basic.shape.Ball
import bernardcjason.gdx.basic.shape.Basic
import bernardcjason.gdx.basic.Common

class Target(val startPosition: Vector3, val targetSize: Float) {

  val textureName = "data/textarget.png"
  
  val movement = new Movement {
    val START_DIRECTION=5f
    val speedTowardsPlayer=2f
    var direction = START_DIRECTION
    val here = new Vector3
    override def move(objects: List[Basic], me: Basic) = {
      me.instance.transform.trn(0, direction*Gdx.graphics.getDeltaTime,speedTowardsPlayer*Gdx.graphics.getDeltaTime)
      me.instance.transform.getTranslation(here)
      
      me.instance.transform.rotate(9, 1, 0, 1)
     
      if ( here.y <= FlatTerrain.terrainGroundZero+targetSize ) {
        direction=START_DIRECTION
      }
      if (  here.y >= targetSize*4 ) {
        direction= -START_DIRECTION
      }
      if ( here.z > 0 ) resetIt
    }
    override def collision(me: Basic) = {
      resetIt
       
    }
    
    def resetIt = {
      val marginEitherSideOfZeroX = 8
      
      startPosition.y=FlatTerrain.terrainGroundZero + targetSize+1
      val off = (Math.random * 100 % 3).asInstanceOf[Int] - 1
      startPosition.z = -(Math.random() * 10000 % 15 + 50).asInstanceOf[Int] 
      startPosition.x =  (Math.random() * 100000000 % marginEitherSideOfZeroX ).asInstanceOf[Int] * off *2

      aTarget.instance.transform.set(new Matrix4)
      aTarget.instance.transform.setTranslation(startPosition)
      direction = START_DIRECTION

    }
  }

  lazy val aTarget:Ball = Ball( textureName,startPosition, radius=targetSize, movement)
  
 
}