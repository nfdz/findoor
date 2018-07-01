# findoor

Simple Android Library for Indoor Wi-Fi Navigation. It makes it easy to work with the Wi-Fi navigation in any kind of scenarios.

<p align="center">
  <img src="dev/dummy-scenario.png?raw=true" alt="Dummy scenario"/>
</p>

## Library

### Key points
The learning curve of this library is very fast, the classes and concepts with which you have to learn to work are the following:
 * [`Record`](https://github.com/nfdz/findoor/blob/master/findoor/src/main/java/io/github/nfdz/findoor/model/Record.java) 
 * [`LocationComparison`](https://github.com/nfdz/findoor/blob/master/findoor/src/main/java/io/github/nfdz/findoor/model/LocationComparison.java)
 * [`FindoorRecorder`](https://github.com/nfdz/findoor/blob/master/findoor/src/main/java/io/github/nfdz/findoor/FindoorRecorder.java)
 * [`FindoorProcessor`](https://github.com/nfdz/findoor/blob/master/findoor/src/main/java/io/github/nfdz/findoor/FindoorProcessor.java)

### Download: Jitpack

It is very use integrate this library in your project as a dependency of your build system thanks to Jitpack. If you use gradle, you just have to add the following in your 'build.gradle' file:

   ```gradle
   allprojects {
	 repositories {
	 ...
         maven { url 'https://jitpack.io' }
      }
   }
   ...
   dependencies {
      implementation 'com.github.nfdz:TODO'
   }
   ```

Jitpack works with several build systems, please checkout the [documentation](https://jitpack.io/docs/BUILDING/) if you need help with yours.

## Sample app

This app shows several uses cases of this library and it is totally functional. Feel free to use it as you need. For example, you could use this app in order to get records of location spots you need and serve or embed them in your production app.

### Download

<p align="center"><a href="https://play.google.com/store/apps/details?id=io.github.nfdz.TODO">
  <img width="250" src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png?raw=true" alt="Get it on Google Play"/>
</a></p>

## License

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

