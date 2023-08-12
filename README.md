# Semantic Web Service Discovery Using Wordnet - Open Source Algorithm and Benchmark

Introduction
Semantic Web service discovery is about the discovery of web services using the semantic Web, the algorithm utilizes the linguistics WordNet ontology in order to enhance finding of services at the level of input and output parameters.


Requirements
The algorithm is implemented in Java lanaguage, in order to run the code you have to dowalod the WordNet DB this can be done by following the steps below:

1. download the following two jars: 
   a. jawjaw-1.0.2.jar - https://code.google.com/p/jawjaw/downloads/list
   b. ws4j-1.0.1.jar - https://code.google.com/p/ws4j/downloads/list

2. Add them to the project path.
3. compile the code

Benchmark
 A query is described by the user by giving the names of input and output parameters along with the similarity threshold he wants to use for semantically matching the available services. In our experiments and as shown in the mentioned table we fixed the threshold to a value of 0.8 which is selected experimentally in order to receive reasonably matched services, however, if an exact match is required by the user, then the highest degree of 1.0 can be provided. The last column in the table represents the ground truth for the number of services available in the service registry for each query. This number is calculated by human experts after investigating the registry for each given query, we also provided the names of available services in our GitHub link.
