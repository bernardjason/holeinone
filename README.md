# holeinone

This is a simple demo game for using Scala with LibGDX to deploy on either desktop or Android device. 

### Building

I've used Gradle which is the default with LibGDX gdx-setup. 

so it is a case of

git clone https://github.com/bernardjason/holeinone.git

cd holeinone

./gradlew desktop:run

or to build and deploy to connected android device.

./gradlew android:installDebug android:run

To use eclipse, I've added the .classpath, .settings and .project files so you can try importing existing project into eclipse or run

./gradlew eclipse

then import core desktop project into eclipse.

See http://bjason.org/ for more information
