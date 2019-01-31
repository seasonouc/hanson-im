#WILCOME TO hanson-im project

hanson-im project is based on telegram protocol
an open source project

now this support the following function 

- [x] user register
- [x] user login
- [x] p2p encrypt chat 
- [x] group encrypt chat
- [ ] picture encrypt
- [ ] file encrypt
- [ ] server extension
- [ ] storage  extension
- [ ] server gateway
- [ ] server high available
- [ ] SSL certificate identify



The whole project is built on Diffie-Hellam algorithm.

***So,what is Diffie-Hellman algorithm?***

[the article about the Diffie-Hellman protocol](http://cs.indstate.edu/~skallam/doc.pdf )

![](/book/protocol1.0_1.png)

this is about tow nodes communication,what about three?four? or more?

we can use the algorithm in another way!

For example,if we have a group of three people,we can exchange the key like this way

**1.the first stage**

![](/book/protocol1.0_2.png)

**2.the second stage**

![](/book/protocol1.0_3.png)

So in such way can get the final  encryption key.

We suppose there are n (n>=2) people,they should exchange the encrypt key n times 

so that they can get the final key.

Base on the theory,we can build a group chat privately ,and meanwhile ,
the most of the data are store in the client storage.
So it dosen't  matter that if the sever was attacked.In someway,it is decentralized.





why I name it hanson,because my name is hanson

@touch us if you have interests season_ouc@126.com


