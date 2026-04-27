# POS System
## Requirements
1. XAMPP
2. Netbeans IDE
3. java jdk and java sdk
4. mysql connector (zip file in requirements folder)

## How to Setup
1. git clone https://github.com/Tlpczdqp/POS-system.git or download the zip in code
2. Open XAMPP and start Apache and MySQL
3. Select shell > mysql -u root 
4. Create the database (database schema in requirements folder)
5. Next open the project in Netbeans IDE
6. Setup the database connection
7. Run the program. If prompt which main to start, select the option with LoginForm

## Database driver setup
1. Extract mysql-connector-j-9.7.0.zip
2. Right click the project and select Properties ![select properties](<requirements/images/Screenshot (3586).png>)
3. Select "Libraries" Then select the plus icon (+) inline with Classpath ![plus icon](<requirements/images/Screenshot (3588).png>)
4. Then click "Add jar/folder" ![add jar/folder](<requirements/images/Screenshot (3590).png>)
5. Navigate to your folder and select the mysql-connector-j-9.7.0.jar ![select .jar](<requirements/images/Screenshot (3589).png>)
6. Click Ok and now you are ready to run the system.

## Sample Account <br>
(included in the database schema)<br>

Username: admin <br>
Password: admin123


