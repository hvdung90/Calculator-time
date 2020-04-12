
# Calculator-time

[![Release](https://img.shields.io/badge/jCenter-1.3.1-brightgreen.svg)](https://bintray.com/hvdung-90/maven/Calculator-time)

Calculates the time from the selected date to the current date with timestamp.

## How to use it?
#### Example!
```kotlin
 var date = 1620443340000L
 val countDate = LocalDateCP.between(date)
 var text = "${d.minute} minutes , ${d.hour} hours , ${d.day} days , ${d.month} months , ${d.year} years"
```

## Download
Repository available on jCenter

```Gradle
implementation 'com.dunghv.libtime:libtime:1.0.0'
```
*If the dependency fails to resolve, add this to your project repositories*
```Gradle
repositories {
  maven {
      url  "http://dl.bintray.com/hvdung-90/maven"
  }
}
```

## License
```
Copyright 2020 Hoang Van Dung

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
