package bernardcjason.gdx.holeinone

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.input.GestureDetector
import bernardcjason.gdx.basic.BaseController
import bernardcjason.gdx.basic.Log

class HoleInOneGame extends ApplicationAdapter {

  val thiscontroller: BaseController = Controller

  override def create() {

    Log.tag = "HoldInOneGame"
    thiscontroller.create

    val multiplexer = new InputMultiplexer();

    multiplexer.addProcessor(new GestureDetector(InputHandler));
    multiplexer.addProcessor(InputHandler)
    Gdx.input.setInputProcessor(multiplexer);

  }

  override def render() {

  	Gdx.gl.glClearColor(0.3f, 0.5f, 0.5f, 1.0f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

    thiscontroller.render()

  }

  override def pause() {
    thiscontroller.pause()
  }
}
