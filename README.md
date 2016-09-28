<h1 align="center"> lvm4j </h1>

[![Build Status](https://travis-ci.org/dirmeier/lvm4j.svg?branch=master)](https://travis-ci.org/dirmeier/lvm4j.svg?branch=master)
[![codecov](https://codecov.io/gh/dirmeier/lvm4j/branch/master/graph/badge.svg)](https://codecov.io/gh/dirmeier/lvm4j)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/28c9723c26b04237b94895f035dc5b32)](https://www.codacy.com/app/simon-dirmeier/lvm4j?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=dirmeier/lvm4j&amp;utm_campaign=Badge_Grade)
[![Javadocs](http://javadoc.io/badge/net.digital-alexandria/lvm4j.svg)](http://javadoc.io/doc/net.digital-alexandria/lvm4j)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.digital-alexandria/lvm4j/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.digital-alexandria/lvm4j)

Latent variable models in Java.

## Introduction

Latent variable models are well-established statistical models where some of the data are not observed. I demonstrate implementations of some of these models in Java. For the sake of simplicity I refer to every model as latent if it consists of two disjoint sets of variables, one that is observed and one that is hidden (e.g. we don't have data or they are just not observable at all). 

The most famous and magnificient of them all, the <i>Hidden Markov Model</i>, is applicable to a diverse number of fields (e.g. for secondary structure prediction or alignment of viral RNA to a reference genome). 

<i>Principal Component Analysis</i> is a simple (probably the simplest) method for dimension reduction. Here you try to find a linear orthogonal transformation onto a new feature space where every basis vector has maximal variance. It's open to debate if this is a *true* latent variale model.

With new versions I will try to cover more latent variable models in <code>lvm4j</code>.

## Installation
 
You can either install the package by hand if you do not want to use maven (why would you?) or just use the standard waytogo installation using a maven project (and pom.xml).

### Install the package using Maven

If you use Maven just put this into your pom.xml:

```xml
<dependency>
    <groupId>net.digital-alexandria</groupId>
    <artifactId>lvm4j</artifactId>
    <version>0.1</version>
</dependency>
```

### Install the package manually

You can also build the <code>jar</code> and then include it in your package.

1) Clone the github repository:

    $ git clone https://github.com/dirmeier/lvm4j.git

2) Then build the package:
 
    $ mvn clean package -P standalone

3) This gives you a <code>lvm4j-standalone.jar</code> that can be added to your project (make sure to call this correctly).


## Usage

Here, we briefly describe how the <code>lvm4j</code> libary is used. Also make sure to check out the [```javadocs```](http://javadoc.io/doc/net.digital-alexandria/lvm4j).

So far the following latent variable models are implemented:

* HMM (a discrete-state-discrete-observation latent variable model)
* PCA (a dimension reduction method with latent *loadings* and observable *scores*)

### How to use the HMM

Using an HMM (in v0.1) involves two steps: training of emission and transition probabilities and prediction of the latent state sequence.

#### Training

First initialize an HMM using:

```java
char[] states = new char[]{'A', 'B', 'C'};
char[] observations = new char[]{'X', 'Y', 'Z'};
HMM hmm = HMMFactory.instance().hmm(states, observations, 1);
```

It is easier though to take the constructor that takes a single string only that contains the path to an XML-file.

```java
String xmlFile = "/src/test/resources/hmm.xml";
HMM hmm = HMMFactory.instance().hmm(xmlFile);
```

Having the HMM initialized, training is done like this:

```java
Map<String, String> states = new HashMap<>(){{
	put("s1", "ABCABC");
	put("s2", "ABCCCC");
}};
Map<String, String> observations = new HashMap<>(){{
	put("s1", "XYZYXZ");
	put("s2", "XYZYXZ");
}};
hmm.train(states, observations);
```

Take care that <code>states</code> and <code>observations</code> have the same keys and equally long values. You can write your trained HMM to a file using:

```java
String outFile = "hmm.trained.xml";
hmm.writeHMM(outFile);
```

That is it! 

#### Prediction

First initialize the HMM again:

```java
String xmlFile = "/src/test/resources/hmm.trained.xml";
HMM hmm = HMMFactory.instance().hmm(xmlFile)
```

Make sure to use the <code>hmm.trained.xml</code> file containing your trained HMM. Then make a prediction using:

```java
Map<String, String> observations = new HashMap<>(){{
	put("s1", "XYZYXZ");
	put("s2", "XYZYXZ");
}};
Map<String, String> pred = hmm.predict(states, observations);
```

Congrats! That concludes the tutorial on HMMs. 

### How to use PCA

TODO

## Author

* Simon Dirmeier <a href="mailto:simon.dirmeier@gmx.de">simon.dirmeier@gmx.de</a>
