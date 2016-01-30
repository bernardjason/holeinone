package bernardcjason.gdx.holeinone

import bernardcjason.gdx.basic.shape.BaseTerrain



case class FlatTerrain(textureName: String) extends BaseTerrain(textureName)   {

  val blockSize =FlatTerrain.blockSize
  val terrainGroundZero = FlatTerrain.terrainGroundZero
  val terrainSize = FlatTerrain.terrainSize
  
  override def setupHeightMatrix {
    var up = true
    for (zz <- 0 until terrainSize / blockSize) {
      for (xx <- 0 until terrainSize / blockSize) {

        matrix(zz)(xx) = terrainGroundZero
      }
    }
  }
}

object FlatTerrain extends ((String) => FlatTerrain ) {
  val blockSize: Int = 32
  val terrainGroundZero: Int = -5
  val terrainSize: Int = 512
}