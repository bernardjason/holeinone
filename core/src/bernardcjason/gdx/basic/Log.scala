package bernardcjason.gdx.basic

import com.badlogic.gdx.Gdx

object Log {
  
  var tag:String  = "none"
  
  def info(message:String) {
    Gdx.app.log(tag, message)
  }
}