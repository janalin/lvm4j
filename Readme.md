ssHMM
======

A simple protein secondary structure predictor using a n-th order HMM. 

## Installation
 
Unzip file and change directory to ss-hmm-master:
```bash
cd ss-hmm-master
```
Then call make:
```bash
./make.sh
```
Calling make will create the jars <code>ss-hmm.jar</code>, <code>ss-hmm-trainer.jar</code> and <code>ss-hmm-predictor.jar</code>  in the current directory. 

## Required files

<code>ss-hmm-trainer</code> and <code>ss-hmm-predictor</code> need several files. Folder <code>data</code> contains examples of all relevant files.
For training a file of observations (fasta.txt) and states (seqs.txt) is required. Furthermore a file that specifies the structure of the HMM is needed (hmm.xml).
For prediction a file of observations (fasta.txt) and the trained HMM are needed (hmm.trained.xml).
If you are using your own files, make sure that they have the same format as the provided ones. 


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

In order to train you HMM, call: 
```java
java -cp ss-hmm-trainer.jar net.digital_alexandria.sshmm_predictor.Main -a observations-file -s states-file --hmm hmm-file -o output-file
```
