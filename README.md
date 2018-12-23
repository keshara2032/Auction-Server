
# Auction-Server

Multi-threaded java program to handle multiple clients in an auction

This program is a server-client model where clients can connect to the server using netcat or telnet and bid on stock items in the server.

## Getting Started


1. First compile and run the Main.class file
2. This will open up a GUI
3. Then Select Server -> Start Server from the MenuBar
4. Thereafter use another terminal to log in to the server as a client(Use the port 2000).
5. Only within the same LAN is allowed to connect to the server at this stage.
6. After that enter your name to begin bidding.
7. After entering your name, enter the symbol of the company you want to bid.
8. If the given symbol is valid and it is in the database, You will be provided with the current stock market price.
9. You will have to enter a higher price than the current price since its following the convention of an auction.
10. If you need to bid again for the item then again enter the symbol and after that enter your price. Like wise you can bid for any item in client mode.
11. If you need to find a current pice of a item which is not displayed in the GUI Top Part, You may search from the list.
12. Your bidding history will be recorded in the server.
13. Only the highest price you bid will be saved with the company information and time.
14. If you want to logout in the client mode then type 'quit'.

### Prerequisites

You need to have Java installed and Netcat or telnet in order to connect to the server



## Built With

* [Java](https://www.java.com/) - Development Platform
* [Maven](https://maven.apache.org/) - Dependency Management



## Authors

* **Keshara Weerasinghe** 


## License

This project is available for everyone to try and learn.

