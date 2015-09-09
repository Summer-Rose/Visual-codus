# Visualizing Epicodus

#### Epicodus group project #1 using Java and Postgres, 09.09.2015
**_By Summer Brochtrup, Perry Eising, Momo Ozawa, & Juliana Suzuki_**

A web app visualizing the demographic breakdown at Epicodus.
* Gradle build automation
* Java 8
* Spark web framework
* JUnit testing framework
* FluentLenium integration testing framework

## Description
* Create, read, update, and delete entries for both students and courses.
* Data visualizations based on student data.


## Database Design
![Database image](https://github.com/SummerBr/visual-codus/blob/master/database-design.png)


## Setup
Clone this repository:
```
$ cd ~/Desktop
$ git clone https://github.com/SummerBr/visual-codus.git
$ cd visual-codus/
```

Open a second tab in your terminal window and run `postgres`. Open a third tab in your terminal and run `psql`. Create our `visual_codus` database:
```
$USER=# CREATE DATABASE visual_codus;
```

Switch back to the first tab and populate the database:
```
$ psql visual_codus < visual-codus.sql
```

Finally, to run this web app:
```
$ gradle run
```


## Legal
Copyright (c) 2015 **Summer Brochtrup, Perry Eising, Momo Ozawa, & Juliana Suzuki**

This software is licensed under the MIT license.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
