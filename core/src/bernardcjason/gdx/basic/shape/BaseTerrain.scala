package bernardcjason.gdx.basic.shape

import scala.Array.ofDim

import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.Ray

import bernardcjason.gdx.basic.Common
import bernardcjason.gdx.basic.move.Movement
import bernardcjason.gdx.basic.move.NoMovement

abstract class BaseTerrain(textureName: String) extends Basic {

  val startPosition: Vector3 = new Vector3
  val radius: Float = 0f
  var movement: Movement = NoMovement
  val terrainGroundZero: Int
  val terrainSize: Int
  val blockSize: Int
  lazy val matrix = ofDim[Float](terrainSize / blockSize, terrainSize / blockSize)

  Array.fill(terrainGroundZero)(matrix)
  lazy val texture = Common.assets.get(textureName, classOf[Texture])
  lazy val genModel = makeGround(texture)

  override def init {
    startPosition.set(-terrainSize / 2, 0, -terrainSize / 2)
  }
  override def move(objects: List[Basic]) = {
    movement.move(objects, this)
  }

  override def collision(other: Basic) {
    movement.collision(this)
  }

  def dispose() {
    genModel.dispose();
  }

  val shape = new CollideShape {
    val radius = 0f
    override def intersects(transform: Matrix4, ray: Ray): Float = {
      Float.MaxValue
    }
  }

  val rayDown = new Vector3(0, -1, 0)
  val intersection = new Vector3
  val adjPos = new Vector3
  def hitMe(what: Vector3, radius: Float): Boolean = {

    val xx = what.x + terrainSize / 2
    val zz = what.z + terrainSize / 2
    adjPos.x = xx
    adjPos.y = what.y
    adjPos.z = zz

    if (xx < terrainSize && zz < terrainSize && xx > 0 && zz > 0) {
      val bx = (xx / blockSize).asInstanceOf[Int]
      val bz = (zz / blockSize).asInstanceOf[Int]

      val ray = new Ray(adjPos, rayDown)
      val points = Array((bx) * blockSize, matrix(bz)(bx), (bz) * blockSize,
        (bx + 1) * blockSize, matrix(bz + 1)(bx + 1), (bz + 1) * blockSize,
        (bx + 1) * blockSize, matrix(bz)(bx + 1), (bz) * blockSize,

        (bx) * blockSize, matrix(bz + 1)(bx), (bz + 1) * blockSize,
        (bx + 1) * blockSize, matrix(bz + 1)(bx + 1), (bz + 1) * blockSize,
        (bx) * blockSize, matrix(bz)(bx), (bz) * blockSize)

      Intersector.intersectRayTriangles(ray, points, intersection)
      val dst = adjPos.dst(intersection)

      if (dst < radius / 2) return true

    }
    false
  }
  def makeGround(texture: Texture): Model = {
    val attr = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates;

    val textureBlockWidth = texture.getWidth / 6
    val textureBlockHeight = texture.getHeight

    modelBuilder.begin();

    val mesh = modelBuilder.part("box", GL20.GL_TRIANGLES, attr, new Material(TextureAttribute.createDiffuse(texture)));

    val textureregion = Array(
      new TextureRegion(texture, textureBlockWidth * 0, 0, textureBlockWidth, textureBlockHeight),
      new TextureRegion(texture, textureBlockWidth * 1, 0, textureBlockWidth, textureBlockHeight),
      new TextureRegion(texture, textureBlockWidth * 2, 0, textureBlockWidth, textureBlockHeight),
      new TextureRegion(texture, textureBlockWidth * 3, 0, textureBlockWidth, textureBlockHeight),
      new TextureRegion(texture, textureBlockWidth * 4, 0, textureBlockWidth, textureBlockHeight),
      new TextureRegion(texture, textureBlockWidth * 5, 0, textureBlockWidth, textureBlockHeight))

    setupHeightMatrix

    val c0 = new Vector3
    val c1 = new Vector3
    val c2 = new Vector3
    val c3 = new Vector3
    val normal = new Vector3(0, -1, 0)
    var c = 0
    for (zz <- 1 until (terrainSize - 1) / blockSize) {
      for (xx <- 1 until (terrainSize - 1) / blockSize) {
        val x = xx * blockSize
        val z = zz * blockSize
        c0.set(x, matrix(zz)(xx), z)
        c1.set(x + blockSize, matrix(zz)(xx + 1), z)
        c2.set(x, matrix(zz + 1)(xx), z + blockSize)
        c3.set(x + blockSize, matrix(zz + 1)(xx + 1), z + blockSize)
        mesh.setUVRange(textureregion(c));
        mesh.rect(c0, c2, c3, c1, normal);

        c = c + 1
        if (c > 5) c = 0
      }
    }

    modelBuilder.end();
  }

  def setupHeightMatrix() {

  }
}