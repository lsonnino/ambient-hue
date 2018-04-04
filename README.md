# Ambient Hue

**Ambient Hue** is a real time average color synchronisation of your main screen and a [Philips Hue light](https://www2.meethue.com).

It analyses your screen and sends the current color and brightness to a selected [Philips Hue lamp](https://www2.meethue.com) up to 10 times per second. You can configure the speed of the colour change and maximal brightness for your personal experience.

# Sponsorship

**Ambient Hue** is open source software. Ongoing development is made possible by generous contributions.

# Documentation

**Ambient Hue** is written in Java and uses JavaFX to display the user interface shown below:
![Ambient Hue main interface](https://github.com/lsonnino/ambient-hue/blob/master/screenshots/Ambient_Hue_active.png?raw=true)

The first slider goes from 100 milliseconds to 2 seconds and represents the refresh rate. The second slider is used to decrease the brightness of the light (0% means no light and 100% means maximum brightness).

![Settings](https://github.com/lsonnino/ambient-hue/blob/master/screenshots/Ambient_Hue_settings.png?raw=true)

**Ambient Hue** currently supports HTTP Put requests, used to directly access your [Philips Hue Bridge](https://www2.meethue.com/en-us/p/hue-bridge/046677458478); see the official [Philips Hue API](https://www.developers.meethue.com) for further information. The HTTPS Post request is useful when accessing the bridge through a proxy (e.g., to access your lights from outside your local network).

The full source code is available [here](https://github.com/lsonnino/ambient-hue/tree/master/AmbientHue/src/main/java) while the current release is available [here](https://github.com/lsonnino/ambient-hue/tree/master/release).

# Credits

 - [Alberto Sonnino](https://github.com/asonnino)
 - [Lorenzo Sonnino](https://github.com/lsonnino)

# License

[The GPLv3 license](https://www.gnu.org/licenses/gpl-3.0.en.html)
