# OOP-Final-Project

This is a Java console application for booking and managing plane tickets. Users can search for flights, book tickets, cancel bookings, and view their flight information. The application follows a three-layer architecture (Controller-Service-DAO) and stores data in files for persistence.


   ##  📌 *Key Features*

   
•	Online Flight Board – View all flights departing from Kyiv in the next 24 hours.

•	Flight Search & Booking – Search flights by destination, date, and passenger count.

•	Booking Management – Cancel bookings using a booking ID.

•	Personal Flight Info – View all bookings by entering a passenger’s full name.

•	File-Based Database – Data is stored in files and loaded on startup.

•	Error Handling – Custom exceptions for invalid inputs.

•	Unit Tests – Test coverage for Controllers, Services, and DAO classes




##  🛠️ *Technologies & Structure*
  
  
•	Java 8+ (Streams, Optional, Lambda expressions)

•	Three-Layer Architecture (Controller → Service → DAO)

•	File I/O (For data persistence)

•	JUnit (For unit testing)

•	Git & GitHub (Branching, Pull Requests, Code Review)



##  📂  *Project Structure*


OOP-Final-Project/  
├── Databases/                      
│   ├── HoneyIndex.json           
│   ├── Users.json                
│   └── flights.txt                
├── logs/  
│   └── log.txt                
├── src/   
│   ├── main/java/  
│   │   ├── DAO/                 
│   │   ├── console/              
│   │   ├── controller/            
│   │   ├── entity/                
│   │   ├── exception/            
│   │   ├── logging/              
│   │   ├── service/               
│   │   └── main.java             
│   └── test/java/                
│       ├── DAO/                   
│       ├── controller/           
│       └── service/               
├── target/                        # Compiled classes (Maven output)  
├── pom.xml                        # Maven configuration  
└── README.md                      




## 🚀 *Getting Started*

1. Clone the repository:
   ```bash
   git clone https://github.com/AytajjAbdullayeva/OOP-Final-Project.git

2. Open in your preferred IDE (e.g., IntelliJ IDEA, VS Code, Eclipse)
3. Run main.java to test.



