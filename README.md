# Spiderman
Spiderman is my NLP thesis project from Rose-Hulman.

For my senior thesis, I explored a new method of emotional classification for text analysis. The corpus was series of tweets compiled over two weeks. In the most basic terms, I used preclassified test data to build a bi-directional graph with emotional clusters of NER terms from the tweets. Each node in the graph is a term of phrase that occurs in a tweet and each edge indicates that a tweet had both terms in nodes in it. Emotional clusters are close groups of nodes that all have a similar emotion classification. 

To classify new tweets, a tweet would be parsed into relevant NER terms and then the average of the geodesic distance from each term to each emotional cluster is calculated. The closest geodesic score determines what the emotional classification of the tweet is.

There are a variety of pros and cons with this approach, as well as a multitude of enhancements that would greatly improve its accuracy. I'll upload my thesis paper in PDF one of these days.
