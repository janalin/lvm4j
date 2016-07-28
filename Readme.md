<h1 align="center"> lvm4j </h1>

## Introduction

Latent variable models are well-established statistical models that consider that some of the data are often not observed. Ill' try to give some basic ideas of how a minority of those models can be implemented in Java. For the sake of simplicity I call every model to be latent if there are two disjoint sets of variables involved, one that is observed and one that is hidden (e.g. we don't have data or they are just not an observable variable at all). 

The most famous and magnificient of them all, the <i>Hidden Markov Model</i>, has so many different applicable fields that it is still routinely found in research (e.g. for secondary structure prediction or alignment of viral RNA to a reference genome). With new patches I try to cover more and more latent variable models. For now only HMMs are implemented and an example of how they are used.

## Installation
 
You can either install the package by hand if you do not want to use maven (why would you?) or just use the standard waytogo installation using a maven project.

### Install package without maven

1) 	Call 'mvn pacakge' from the source folder.
2) 	Include 'lvm4j.jar' as external library in your Java project.

### Install package with maven

1)	Include my maven repository in your 'pom.xml':
	
		<repositories>
        	<repository>
            	<id>central</id>
            	<url>http://digital-alexandria.net:8081/artifactory/libs-release</url>
            	<snapshots>
            	    <enabled>false</enabled>
            	</snapshots>
        	</repository>
    	</repositories>

2)	Include the dependency in your 'pom.xml':

	 	<dependency>
    	    <groupId>net.digital_alexandria</groupId>
    	    <artifactId>commandline-parser</artifactId>
    	    <version>1.1.2</version>
        </dependency>

3)	That's it. Info of how to use it under README.md and USAGE.txt

## Usage

Here, we briefly describe how the <code>lvm4j</code> libary is used. So far the following latent variable models are implemented.

* HMM (a discrete-state-discrete-observation latent variable model)

### HMM
