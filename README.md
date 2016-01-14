# 


Requirements:
JVM V1.8+

In the root directory run the following:

**gradlew jar**

A jar file should be created under the folder build/lib.

Run java -jar \<jar file\> to fire up the server listening on port 9090 and bob's your uncle :)

Commands samples:

http://localhost:9090/user3/login (get)
http://localhost:9090/2/bid?sessionKey=59hmnoaj9ccjf858j069569esn (post with body of type number)  
http://localhost:9090/1/topBidList (get)