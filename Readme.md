<h1 align="center"> lvm4j </h1>

## Introduction

Latent variable models are well-established statistical models that consider that some of the data are often not observed. Ill' try to give some basic ideas of how a minority of those models can be implemented in Java. For the sake of simplicity I call every model to be latent if there are two disjoint sets of variables involved, one that is observed and one that is hidden (e.g. we don't have data or they are just not an observable variable at all). 

The most famous and magnificient of them all, the <i>Hidden Markov Model</i>, has so many different applicable fields that it is still routinely found in research (e.g. for secondary structure prediction or alignment of viral RNA to a reference genome). With new patches I try to cover more and more latent variable models. For now only HMMs are implemented and an example of how they are used.

## Installation
 
Unzip file and change directory to lvm4j-master:
```bash
cd lvm4j-master
```
Then call make:
```bash
./make.sh
```
Calling make will create the jars <code>lvm4j.jar</code>  in the current directory. 

## HMM

##### Observations/states files

Multiple IDs and observation/state sequences, newline separated.
* IDs start with ">" and are following by identifier.
* Observation/state sequence is a string of characters.

##### HMM file

The HMM xml consists of three major xml-tags:
* states: all possible hidden states given as concatenated string. In the example file these are H, E and C.
* observations: all possible observations as concatenated string. In the example file these are Z, W and D.
* order: the order of the Markov chain, i.e. the prediction of a new state depends on the <i>order</i> previous states. In the example file I used an order of 2.

## Usage

TODO
