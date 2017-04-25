<h1 align="center"> lvm4j </h1>

[![Project Status](http://www.repostatus.org/badges/latest/active.svg)](http://www.repostatus.org/#active)
[![Build Status](https://travis-ci.org/dirmeier/lvm4j.svg?branch=master)](https://travis-ci.org/dirmeier/lvm4j)
[![codecov](https://codecov.io/gh/dirmeier/lvm4j/branch/master/graph/badge.svg)](https://codecov.io/gh/dirmeier/lvm4j)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/28c9723c26b04237b94895f035dc5b32)](https://www.codacy.com/app/simon-dirmeier/lvm4j?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=dirmeier/lvm4j&amp;utm_campaign=Badge_Grade)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.digital-alexandria/lvm4j/badge.svg)](https://mvnrepository.com/artifact/net.digital-alexandria/lvm4j)
[![Javadocs](http://javadoc.io/badge/net.digital-alexandria/lvm4j.svg)](http://javadoc.io/doc/net.digital-alexandria/lvm4j)

Latent variable models in Java.

## Introduction

Latent variable models (*LVM*s) are well-established statistical models where some of the variables are not observed. ```lvm4j``` implements popular *LVM*s in the ```Java``` programming language. For the sake of simplicity I refer to every model as latent if it consists of two disjoint sets of variables, one that is observed and one that is hidden (e.g. we don't have data or they are just not observable at all). 

With new versions I will try to cover more latent variable models in <code>lvm4j</code>.

### Implemented models

 * One of the most famous and magnificient of them all, the <i>Hidden Markov Model</i>, is applicable to a diverse number of fields (e.g. for secondary structure prediction or alignment of viral RNA to a reference genome). 

* <i>Principal Component Analysis</i> is a simple (probably the simplest) method for dimension reduction. We try to find a linear orthogonal transformation onto a new feature space where every basis vector has maximal variance. It's open to debate if this is a *true* latent variale model.

* <i>Factor Analysis</i> is essentially a propabilistic version of PCA (and closely resembles PPCA). Here we additionally include Gaussian noise since our data-points don't exactly lie on a linear subspace.

## Installation
 
You can either install the package by hand if you do not want to use maven (why would you?) or just use the standard waytogo installation using a maven project (and pom.xml).

### Install the package using Maven

If you use Maven just put this into your pom.xml:

```xml
<dependency>
    <groupId>net.digital-alexandria</groupId>
    <artifactId>lvm4j</artifactId>
    <version>0.2</version>
</dependency>
```

### Install the package manually

You can also build the <code>jar</code> and then include it in your package.

1) Clone the github repository:

  ```sh
     git clone https://github.com/dirmeier/lvm4j.git
  ```
2) Then build the package:

   ```sh
      mvn clean package -P standalone
   ```

3) This gives you a <code>lvm4j-standalone.jar</code> that can be added to your project (make sure to call this correctly).


## Usage

For usage check out [```javadocs```](http://javadoc.io/doc/net.digital-alexandria/lvm4j) and the [```tutorial```](http://dirmeier.github.io/lvm4j).

## Author

* Simon Dirmeier <a href="mailto:mail@simon-dirmeier.net">mail@simon-dirmeier.net</a>
