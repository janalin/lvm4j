<h1 align="center">
lvm4j
</h1>


## Installation
 
Unzip file and change directory to ss-hmm-master:
```bash
cd lvm4j-master
```
Then call make:
```bash
./make.sh
```
Calling make will create the jars <code>lvm4j.jar</code>  in the current directory. 

## TODO

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