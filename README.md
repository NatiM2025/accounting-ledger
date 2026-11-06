# Financial Tracker 

A java console application that helps the users keep track of their deposits, payments, and transactions.

## Description of the Project

Financial Tracker is a simple application that helps users manage their money. 
Users can add deposits and payments and see their transaction history. 
They can also search for transactions and view reports by date. 
The application saves the transactions in a file so users can see them again when they run the program.

## Features

- Add a deposit.
- Add a payment.
- View all transactions.
- View deposits only.
- View payments only.
- Search transactions by vendor.
- Search transactions using different filters.
- View month-to-date report.
- View previous month report.
- View year-to-date report.
- View previous year report.

## User Stories

- As a user, I want to easily see all available options so that I can navigate through the application and perform the actions I need.
- As a user, I want to deposit the amount of money I choose so that I can safely add money to my account.
- As a user, I want to make a payment by entering the amount and payment details so that I can record my payments and keep track of my expenses.
- As a user, I want to see different ledger options so that I can easily choose how to view my financial transactions.
- As a user, I want to generate reports and search my financial transactions so that I can easily track my deposits, payments, and spending history.


## Setup

### Prerequisites

- Java 17 (Amazon Corretto), the version used in class.
- IntelliJ IDEA Community Edition: <https://www.jetbrains.com/idea/download/>

### Running the application

1. Clone this repository, or download it as a zip and unzip it.
2. In IntelliJ: File > Open, pick the project folder (the one that holds `pom.xml`), press Open.
3. Wait for IntelliJ to finish indexing (progress bar at the bottom).
4. Open `src/main/java/com/pluralsight/YourMainClassName.java`. <!-- replace YourMainClassName with the class that has main -->
5. Right-click inside the file and choose Run 'YourMainClassName.main()'.

### Data and configuration

- transactions.csv is used to save the transactions.
- The application loads the saved transactions when it starts.
- New deposits and payments are saved in the file.
- Each transaction has a date, time, description, vendor, and amount.
- Deposits are positive amounts, and payments are negative amounts.

## Technologies Used

- Java 17
- IntelliJ IDEA

## Demo

#### Main
![Main menu](/home/natanim/Desktop/YUU/pluralsight/capstones/accounting-ledger/images/menu.png)
#### depositing
![One feature in action](/home/natanim/Desktop/YUU/pluralsight/capstones/accounting-ledger/images/imageDeposit.png)
#### Error or invalid
![Error](/home/natanim/Desktop/YUU/pluralsight/capstones/accounting-ledger/images/image error.png)

## A piece of code I am proud of

```
 try {
            // creates new file if there is no file created before.
            transactions.clear();
            File file = new File(FILE_NAME);
            if (!file.exists()){
                file.createNewFile();
            }

            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                String[] parts = line.split("\\|");

                if (parts.length != 5) {
                    System.out.println("Invalid transaction record: " + line);
                    continue;
                }
                Transaction transaction = new Transaction(parts[0].trim(),parts[1].trim(),parts[2].trim(),parts[3].trim(),parts[4].trim());
                transactions.add(transaction);

            }
              bufferedReader.close();
        } catch (Exception e) {
            System.out.println("Something went wrong " + e.getMessage());
        }
    }
```

## Future Work

- Make the application more attractive with a better visual design.
- Make the application more interactive and easier for users to use.
- Add more report options.


## Resources

- Java YearUp Fall 2026 hub: <https://raymaroun.github.io/yearup-java-fall2026/>

## Author

- **Natanim Mekuria**

## Thanks

- Thank you to my instructor and classmates for helping me during this project.