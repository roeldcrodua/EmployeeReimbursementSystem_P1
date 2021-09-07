# Project 1: Expense Reimbursement System

## Project Summary
The Expense Reimbursement System (ERS) will manage the process of reimbursing employees for expenses incurred while on company time. All employees in the
company can login and submit requests for reimbursement and view their past tickets and pending requests. Finance managers can log in and view all reimbursement
requests and past history for all employees in the company. Finance managers are authorized to approve and deny requests for expense reimbursement.

## Flow Diagram

![image](https://user-images.githubusercontent.com/65931708/132281265-e071a15a-d745-41d5-9af5-b10f4254ea61.png)

## Case Diagram

![image](https://user-images.githubusercontent.com/65931708/132281360-08a01b6a-772c-44c2-8326-3d191696f9a3.png)

## Technical Requirements

- Use the concept of “Separation of concerns”. Organize your code to make it neat.
- The application shall deploy onto a Server.
- The back-end system shall use JDBC to connect to a PostgreSQL database.
- Your API endpoints should be deployed using servlets.
- The front-end view should use HTML, CSS, and JavaScript to present the	application to the end user.
- Your app should use AJAX calls to access your server-side functionality.
- The web pages should look presentable (try using css and bootstrap); Rather	not see a website from 1995.
- TDD should be implemented
- (Optional) Passwords should be hashed in Java and securely stored in the	database
- (Optional) Users can upload a document or image of their receipt when	submitting reimbursements
- (Optional) The application will send an email to employees letting them know that	they have been registered as a new user, giving them their temporary password

## Tips on how to start
If you’re having trouble wrapping your head around how to start, here is a preference for starting project 1:

1. Start with your SQL schema. 
    - Create all your tables (entities) and entity relationships. 
    - You should be able to populate your entities with data before even touching javascript or html 
    - Attempt to run simple CRUD operations on each of your tables to verify that your SQL statements are firing.
			a. The “User Role”, “Reimbursement Type”, and “Reimbursement Status” tables are all LOOK UP TABLES (enum values). 
      ATTENTION: the Database Administrator (you) will need to pre populate these look up tables with data before doing anything else because they will have not null foreign keys pointing them.
      
2. Once your database is working properly, go to Java and create your model layer (aka model files aka class files). 
    - So create the necessary object representations of your database tables.
	    a. In our case we’ll be creating classes for “User”, “Reimbursement”, “ReimbursementStatus”, “ReimbursementType”, and “UserRole”
	    b. (you may find that you don’t HAVE to create classes for Status, Type, and Role depending on how you implement your server)
      
3. Once your models exist, set up your JDBC to interact with your DB. 
    - Don’t make or setup Servlets...1 step at a time because if you throw too much in then you won’t know what is causing the issue when an issue arises...JUST do your JDBC for now. Make sure the basic crud methods work.
    - Just run the Java file without making the server.
   
4. Once DB schema and JDBC is functioning, create API endpoints using Servlets. I then test the endpoints to ensure they work INDEPENDENTLY of HTML pages.
   

## Possible servelets endpoints:
- /api/signup POST
- /api/login POST
- /api/check-session GET
- /api/logout GET
- /api/role GET
- /api/users GET
- /api/reimbursement
   - POST - addNewReimbursement
   - PATCH - editAReimbursement
   - DELETE - deleteAReimbursement
   - GET - getOneReimbursement
- /api/reimbursements
   - GET - getAllOwnReimbursement
- /api/resolve
    - PUT - resolveAReimbursement
    - GET - getAllListOfResolvedReimbursement
- /api/manager
    GET - getListOfAllPendingReimbursements  

## Features
### TODOs Have Done:
- Created the DB schema via DBeaver
- Created models
- Created Dao interface and DaoImplementations
- Created Services that calls the Dao execution methods
- Created the Dispatcher class which will handle the restful endpoints from the client.
- Created the Controller classes that handles the HttpRequest and HttpResponse from the Dispatcher
- Built the front-end modules stuff like the html, css and javascript files.
- Built some front-end javascript functions that will handle the inputs from the user
- Able to complete the major requirements of the project
- Added Mockito with JUnit testing 
- Added optional functionalities for email
- Added optional password hashing
- Create a password generator
- Get the User full name and display in the client side
- Applied Responsive website design

### TODOs to be done
- Upload attachment via S3

## Application Snippet
### Login Form Page
![image](https://user-images.githubusercontent.com/65931708/132282629-8e213e44-9676-4f9a-8ecf-e8a1418d35fb.png)

### Employee Dashboard Page
![image](https://user-images.githubusercontent.com/65931708/132282744-c534c01c-281a-4bf8-b116-6115722824ae.png)

### Finance Manager Dashboard Page
![image](https://user-images.githubusercontent.com/65931708/132283096-4749733c-7a39-4cc9-8075-8fbecec0cc27.png)

### Creating/Viewing/Editing/Processing Reimbursement Form
![image](https://user-images.githubusercontent.com/65931708/132282966-6efad2b9-d0a6-4d8f-81e0-197f0b0a24a2.png)

### Reset Password Page
![image](https://user-images.githubusercontent.com/65931708/132283038-2a0a9b83-f48b-4285-91c2-d1024e796a59.png)

### Register new user employee
![image](https://user-images.githubusercontent.com/65931708/132283437-9e076ac2-0726-441c-a38b-8c144225b587.png)
