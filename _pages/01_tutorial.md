---
layout: page
title: Tutorial
permalink: /tutorial/
---

Here, we briefly describe how the <code>lvm4j</code> libary is used. Also make sure to check out the [```javadocs```](http://javadoc.io/doc/net.digital-alexandria/lvm4j).



#### Decomposition

Decomposition methods implement the `Decomposition`-interface and can be create using a static method from `DecompositionFactory`.
The means its suffices to initialize decomposition implementations using:

```java
  Decomposition decm = DecompositionFactory.method(...);
```

If you want to access all the methods, however, the following tutorial shows you, how it's done. All the decomposition classes need a matrix as argument.
This can either be a `double`-matrix or an `INDArray`. In the end it does not matter, what matrix you create, because we cast it to `INDArray` anyways.
INDArrays are implemented in the great  [```nd4j```](http://nd4j.org/) package which among other things does matrix computations using BLAS and LAPACK.

```java
  double[][] x = new double[100][10];
  for (int i = 0; i < x.length; i++)
    Arrays.fill(x[i], i);

  INDArray xi = Nd4j.create(X);
```

##### Principal component analysis

Initialize the `PCA` object and create a new `score`-matrix with a different number of components like this:

```java
    PCA pca = DecompositionFactory.pca(x);
    PCA pcai = DecompositionFactory.pca(xi);

    // here we take four components
    INDArray s = pca.run(4);

    // here we take two components
    INDArray si = pcai.run(2);
```


##### Factor analysis

If we use the same matrices as from above, factor analysis can be used with:

```java
    FactorAnalysis fa = DecompositionFactory.factorAnalysis(x);

    // here we take two components
    INDArray s = fa.run(2);
```


#### Markov models

##### Hidden Markov models

HMMs implement the `DiscreteStateMarkovModel` interface and can be instantiated using the `DiscreteStateMarkovModelFactory`.
Using an HMM (in v0.2) involves two steps: training of emission and transition probabilities and prediction of the latent state sequence.

##### Training

First initialize an HMM using:

```java
  char[] states = new char[]{'A', 'B', 'C'};
  char[] observations = new char[]{'X', 'Y', 'Z'};
  HMM hmm = DiscreteStateMarkovModelFactory.hmm(states, observations, 1);
```

It is easier though to take the constructor that takes a single string only that contains the path to an XML-file.

```java
  String xmlFile = "/src/test/resources/hmm.xml";
  HMM hmm = DiscreteStateMarkovModelFactory.hmm(xmlFile);
```

If you initialize the hmm with an `xml` you need to specify the following format:

```xml
  <hmm>
    <meta>
      <states>HEC</states>
      <observations>ZWD</observations>
      <order>2</order>
    </meta>
  </hmm>
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

Take care that <code>states</code> and <code>observations</code> have the same keys and equally long values.
You can write your trained HMM to a file using:

```java
  String outFile = "hmm.trained.xml";
  hmm.writeHMM(outFile);
```

That is it!

##### Prediction

First initialize the HMM again:

```java
  String xmlFile = "/src/test/resources/hmm.trained.xml";
  HMM hmm = DiscreteStateMarkovModelFactory.hmm(xmlFile)
```

Make sure to use the <code>hmm.trained.xml</code> file containing your trained HMM. Then make a prediction using:

```java
  Map<String, String> observations = new HashMap<>(){{
    	put("s1", "XYZYXZ");
	    put("s2", "XYZYXZ");
  }};
  Map<String, String> pred = hmm.predict(states, observations);
```
