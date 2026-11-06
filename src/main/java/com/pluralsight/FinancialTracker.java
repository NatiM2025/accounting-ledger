package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;


public class FinancialTracker {

    /* ------------------------------------------------------------------
       Shared data and formatters
       ------------------------------------------------------------------ */
    private static final ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern(TIME_PATTERN);

    /* ------------------------------------------------------------------
       Main menu  Starting the program and showing the main menu.
       ------------------------------------------------------------------ */
    public static void main(String[] args) {
        loadTransactions(FILE_NAME);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D" -> addDeposit(scanner);
                case "P" -> addPayment(scanner);
                case "L" -> ledgerMenu(scanner);
                case "X" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
        scanner.close();
    }

    /* ------------------------------------------------------------------
        Loading saved transactions from the CSV file.
       ------------------------------------------------------------------ */
    public static void loadTransactions(String fileName) {
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

    /* ------------------------------------------------------------------
       Loading saved transactions from the CSV file.
       ------------------------------------------------------------------ */
    private static void addDeposit(Scanner scanner) {
        System.out.println("Deposit");
        boolean exit = false;
        while (!exit) {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME, true));
                //saving to CSV file.
                Transaction transaction = transactionInfo(scanner);
                transaction.setAmount("+" + transaction.getAmount());

                bufferedWriter.write(transaction.getDate() + " | " + transaction.getTime() + " | " + transaction.getDescription() + " | "
                        + transaction.getVendor() + " | " + transaction.getAmount());

                bufferedWriter.newLine();
                transactions.add(transaction);
                bufferedWriter.close();
                System.out.println(transaction + " Deposited successfully\n");
                System.out.println("X TO exit or anything for new deposit:");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("x")){
                    exit = true;
                }


            } catch (Exception e) {
                System.out.println("Some thing went wrong " + e);
            }
        }
    }

    // Getting payment information from the user and saving it to the file.
    private static void addPayment(Scanner scanner) {
        System.out.println("Payment");
        boolean exit = false;
        while (!exit){
        try {

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME,true));
            Transaction transaction = transactionInfo(scanner);
            transaction.setAmount("-" + transaction.getAmount());

            bufferedWriter.write(transaction.getDate() + " | " + transaction.getTime() + " | " + transaction.getDescription() + " | "
                    + transaction.getVendor() + " | "  + transaction.getAmount());
            bufferedWriter.newLine();
            transactions.add(transaction);
            bufferedWriter.close();
            System.out.println(transaction + " Payed Successfully\n");
            System.out.println("X TO exit or anything for new payment:");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("x")){
                exit = true;
            }
            } catch (Exception e) {
               System.out.println("something went wrong " + e);
            }
        }
    }

    /* ------------------------------------------------------------------
       Showing the ledger menu and getting the user choice.
       ------------------------------------------------------------------ */
    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A" -> displayLedger();
                case "D" -> displayDeposits();
                case "P" -> displayPayments();
                case "R" -> reportsMenu(scanner);
                case "H" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    /* ------------------------------------------------------------------
        Showing all transactions, starting with the newest one.
       ------------------------------------------------------------------ */
    private static void displayLedger() {
        System.out.println("All transactions");
        if(transactions.isEmpty()){
            System.out.println("No transaction found");
            return;
        }
        for (int i = transactions.size()- 1; i>= 0; i--) {
            Transaction transaction = transactions.get(i);
            System.out.println("Date: " + transaction.getDate() + " Time: " + transaction.getTime() + " Vendor: " + transaction.getVendor() + " Description: " + transaction.getDescription() + " Amount: $" + transaction.getAmount());

        }

    }
    // Showing only deposits from the transaction list.
    private static void displayDeposits() {
        System.out.println("Deposits");
        for (int i = transactions.size()-1; i >=0 ; i--) {
            Transaction transaction = transactions.get(i);
            if (!transaction.getAmount().startsWith("-")){
                System.out.println(transaction);
            }

        }
    }

    // Showing only payments from the transaction list.
    private static void displayPayments() {
        System.out.println("Payments");
        for (int i = transactions.size()-1; i >=0 ; i--) {
            Transaction transaction = transactions.get(i);
            if (transaction.getAmount().startsWith("-")){
                System.out.println(transaction);
            }

        }
    }

    /* ------------------------------------------------------------------
       Showing the reports menu and letting the user choose a report.
       ------------------------------------------------------------------ */
    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> {monthToDateReport();}
                case "2" -> {previousMonthReport(); }
                case "3" -> {yearToDateReport();}
                case "4" -> {previousYearReport(); }
                case "5" -> {
                    System.out.println("Pleas enter the vendor\nVendor:");
                   String userVendor = scanner.nextLine();
                   filterTransactionsByVendor(userVendor);
                }
                case "6" -> customSearch(scanner);
                case "0" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    /* ------------------------------------------------------------------
      Finding transactions between the start date and end date.
       ------------------------------------------------------------------ */
    private static void filterTransactionsByDate(LocalDate start, LocalDate end) {
        for (int i = transactions.size() -1; i >= 0 ; i--) {
            Transaction transaction = transactions.get(i);
            LocalDate transactionDate = parseDate(transaction.getDate());
            if(transactionDate != null && !transactionDate.isBefore(start) && !transactionDate.isAfter(end)){
                System.out.println(transaction);

            }
        }
    }
    // Finding transactions using the vendor name.
    private static void filterTransactionsByVendor(String vendor) {
        for (int i = transactions.size() -1 ; i >=0 ; i--) {
            Transaction transaction = transactions.get(i);
           String  transactionVendor = transaction.getVendor();
           if (transactionVendor.equalsIgnoreCase(vendor)){
               System.out.println(transaction);
           }

        }
    }
    // Letting the user search transactions using different filters.
    private static void customSearch(Scanner scanner) {
        LocalDate startDate = null;
        LocalDate endDate = null;
        Double amount = null;
        System.out.println("pleas enter Your filter");
        while (true){
            System.out.println("yyy-mm-dd");
            System.out.print("start date: ");
            String userStartDate = scanner.nextLine();
            if (userStartDate.isEmpty()){
                break;
            }
             try {
                startDate = LocalDate.parse(userStartDate);
                break;
             }catch (Exception e){
                System.out.println("Invalid input!");
             }

        } while (true) {
            System.out.println("yyy-mm-dd");
            System.out.print("End date: ");
            String userEndDate = scanner.nextLine();

            if(userEndDate.isEmpty()){
                break;
            }
            try {
                endDate = LocalDate.parse(userEndDate);
                break;
            } catch (Exception e) {
                System.out.println("Invalid input!");

            }
        }

        System.out.print("Vendor: ");
        String userVendor = scanner.nextLine();
        System.out.print("Description: ");
        String userDescription = scanner.nextLine();
        System.out.print("Exact amount: ");
        String userAmount = scanner.nextLine();

        if (!userAmount.isEmpty()){
            amount = Math.abs(Double.parseDouble(userAmount));
        }

        for(Transaction transaction : transactions){
            LocalDate savedDate = LocalDate.parse(transaction.getDate());
            double transactionAmount = Math.abs(Double.parseDouble(transaction.getAmount()));
            if (!userAmount.isEmpty() && (transactionAmount != amount)){
                continue;
            }
            if (!userDescription.isEmpty() && (!transaction.getDescription().equalsIgnoreCase(userDescription))){
                continue;
            }if (!userVendor.isEmpty() && (!transaction.getVendor().equalsIgnoreCase(userVendor))){
                continue;
            }

            if (startDate != null && savedDate.isBefore(startDate) ){
                continue;
            }if (endDate != null && savedDate.isAfter(endDate) ){
                continue;
            }
            System.out.println(transaction);

        }

    }
    /* ------------------------------------------------------------------
       Converting the date from String to LocalDate.
       ------------------------------------------------------------------ */
    private static LocalDate parseDate(String s) {
        try {
            return LocalDate.parse(s);
        }catch (Exception e){
            return null;
        }
    }
    // Converting the amount from String to Double.
    private static Double parseDouble(String s) {
        try {
            return Double.parseDouble(s);
        }catch (Exception e){
        return null;
        }
    }
    // getting user information to reuse there info
    public static Transaction transactionInfo (Scanner scanner){

        LocalDate dateNow = LocalDate.now();
        LocalTime timeNow = LocalTime.now();

        //Validating user input if it is number or not
        System.out.println();
        String amount = "";
        while (true){
            System.out.print("Please Enter amount\nAmount: ");
           if (scanner.hasNextDouble()){
               amount = scanner.nextLine();
               Double userAmount = parseDouble(amount);
               if(userAmount != null && userAmount > 0 ){
                   break;
               }

           }else {
               scanner.nextLine();
               System.out.println("Invalid input ");
           }
        }

        System.out.print("Please Enter the vendor\nVendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();

        return new Transaction(dateNow.toString(),timeNow.format(TIME_FMT),description,vendor,amount);
    }
    // Showing transactions from the beginning of this month until today.
    public static void monthToDateReport (){
        LocalDate today = LocalDate.now();
        LocalDate start = today.withDayOfMonth(1);
        filterTransactionsByDate(start,today);
    }
    // Showing all transactions from the previous month.
    public static void previousMonthReport (){
        LocalDate today = LocalDate.now();
        LocalDate previousMonth =today.minusMonths(1);
        LocalDate start = previousMonth.withDayOfMonth(1);
        LocalDate end = previousMonth.withDayOfMonth(previousMonth.lengthOfMonth());
        filterTransactionsByDate(start,end);
    }
    // Showing transactions from the beginning of this year until today.
    public static void yearToDateReport (){
        LocalDate today = LocalDate.now();
        LocalDate start = today.withDayOfYear(1);
        filterTransactionsByDate(start,today);
    }
    // Showing all transactions from the previous year.
    public static void previousYearReport (){
        LocalDate today = LocalDate.now();
        LocalDate previousYear = today.minusYears(1);
        LocalDate start = previousYear.withDayOfYear(1);
        LocalDate end = previousYear.withDayOfYear(previousYear.lengthOfYear());

        filterTransactionsByDate(start,end);
    }
}
