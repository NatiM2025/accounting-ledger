package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Capstone skeleton – personal finance tracker.
 * ------------------------------------------------
 * File format  (pipe-delimited)
 *     yyyy-MM-dd|HH:mm:ss|description|vendor|amount
 * A deposit has a positive amount; a payment is stored
 * as a negative amount.
 *
 * TODO: create the Transaction class in this package first.
 *       One object = one line of the file. Decide its fields from
 *       the file format above, add a constructor and getters.
 *       This class does not compile until Transaction exists.
 */
public class FinancialTracker {

    /* ------------------------------------------------------------------
       Shared data and formatters
       ------------------------------------------------------------------ */
    private static final ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.csv";

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String DATETIME_PATTERN = DATE_PATTERN + " " + TIME_PATTERN;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern(TIME_PATTERN);
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    /* ------------------------------------------------------------------
       Main menu
       ------------------------------------------------------------------ */
   public static String lastTransaction;    // Holds latest transaction
    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        //displayLedger();
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
       File I/O
       ------------------------------------------------------------------ */

    /**
     * Load transactions from FILE_NAME.
     * • If the file doesn’t exist, create an empty one so that future writes succeed.
     * • Each line looks like: date|time|description|vendor|amount
     */
    //listing all the csv file history.
    public static void loadTransactions(String fileName) {
        // TODO: create file if it does not exist, then read each line,
        //       parse the five fields, build a Transaction object,
        //       and add it to the transactions list.

        try {
            // creates new file if there is no file created before.
            transactions.clear();
            File file = new File(FILE_NAME);
            if (!file.exists()){
                file.createNewFile();
            }

            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;
            String amount = null;
            // splitting the line
            while ((line = bufferedReader.readLine()) != null){
                String[] parts = line.split("\\|");

                if (parts.length != 5) {
                    System.out.println("Invalid transaction record: " + line);
                    continue;
                }
                Transaction transaction = new Transaction(parts[0].trim(),parts[1].trim(),parts[2].trim(),parts[3].trim(),parts[4].trim());
                transactions.add(transaction);
            }

        } catch (Exception e) {
            System.out.println("Something went wrong " + e.getMessage());
        }
    }

    /* ------------------------------------------------------------------
       Add new transactions
       ------------------------------------------------------------------ */

    /**
     * Prompt for ONE date+time string in the format
     * "yyyy-MM-dd HH:mm:ss", plus description, vendor, amount.
     * Validate that the amount entered is positive.
     * Store the amount as-is (positive) and append to the file.
     */
    // This method asks the user for deposit information and saves it to CSV file.
    private static void addDeposit(Scanner scanner) {
        System.out.println("Deposit");
        boolean exit = false;
        while (!exit) {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME, true));

                //saving to CSV file.
                Transaction transaction = transactionInfo(scanner);
                bufferedWriter.write(transaction.getDate() + " | " + transaction.getTime() + " | " + transaction.getDescription() + " | "
                        + transaction.getVendor() + " | " + "+" + transaction.getAmount());

                bufferedWriter.newLine();
                transactions.add(transaction);
                bufferedWriter.close();
                loadTransactions(FILE_NAME);
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

    /**
     * Same prompts as addDeposit.
     * Amount must be entered as a positive number,
     * then converted to a negative amount before storing.
     */
    private static void addPayment(Scanner scanner) {
        // TODO
        // saving to file
        System.out.println("Payment");
        boolean exit = false;
        while (!exit){
        try {

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME,true));
            Transaction transaction = transactionInfo(scanner);

            bufferedWriter.write(transaction.getDate() + " | " + transaction.getTime() + " | " + transaction.getDescription() + " | "
                    + transaction.getVendor() + " | " + "-" + transaction.getAmount());

            transactions.add(transaction);
            bufferedWriter.newLine();
            bufferedWriter.close();

            loadTransactions(FILE_NAME);
            System.out.println(transaction + " Payed Successfully");
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
       Ledger menu
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
       Display helpers: show data in neat columns
       ------------------------------------------------------------------ */
    private static void displayLedger() { /* TODO – print all transactions in column format, newest first */

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
    //display Deposit using "-" to identify and list the newer first.
    private static void displayDeposits() { /* TODO – only amount > 0               */
        System.out.println("Deposits");
        for (int i = transactions.size()-1; i >=0 ; i--) {
            Transaction transaction = transactions.get(i);
            if (!transaction.getAmount().startsWith("-")){
                System.out.println(transaction);
            }

        }
    }

    //display payment using "-" to identify and list the newer first.
    private static void displayPayments() { /* TODO – only amount < 0               */
        System.out.println("Payments");
        for (int i = transactions.size()-1; i >=0 ; i--) {
            Transaction transaction = transactions.get(i);
            if (transaction.getAmount().startsWith("-")){
                System.out.println(transaction);
            }

        }
    }

    /* ------------------------------------------------------------------
       Reports menu
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
                case "1" -> {monthToDateReport();/* TODO – month-to-date report */ }
                case "2" -> {previousMonthReport();/* TODO – previous month report */ }
                case "3" -> {yearToDateReport();/* TODO – year-to-date report   */ }
                case "4" -> {previousYearReport();/* TODO – previous year report  */ }
                case "5" -> {
                    System.out.println("Pleas enter the vendor\nVendor:");
                   String userVendor = scanner.nextLine();
                   filterTransactionsByVendor(userVendor);
                    /* TODO – prompt for vendor then report */ }
                case "6" -> customSearch(scanner);
                case "0" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    /* ------------------------------------------------------------------
       Reporting helpers
       ------------------------------------------------------------------ */
    private static void filterTransactionsByDate(LocalDate start, LocalDate end) {
        // TODO – iterate transactions, print those within the range
        for (int i = transactions.size() -1; i >= 0 ; i--) {
            Transaction transaction = transactions.get(i);
            LocalDate transactionDate = parseDate(transaction.getDate());
            if(transactionDate != null && !transactionDate.isBefore(start) && !transactionDate.isAfter(end)){
                System.out.println(transaction);

            }
        }
    }

    private static void filterTransactionsByVendor(String vendor) {
        // TODO – iterate transactions, print those with matching vendor
        for (int i = transactions.size() -1 ; i >=0 ; i--) {
            Transaction transaction = transactions.get(i);
           String  transactionVendor = transaction.getVendor();
           if (transactionVendor.equalsIgnoreCase(vendor)){
               System.out.println(transaction);
           }

        }
    }

    private static void customSearch(Scanner scanner) {
        // TODO – prompt for any combination of date range, description,
        //        vendor, and exact amount, then display matches
    }
    /* ------------------------------------------------------------------
       Utility parsers (you can reuse in many places)
       ------------------------------------------------------------------ */
    private static LocalDate parseDate(String s) {
        /* TODO – return LocalDate or null */
        try {
            return LocalDate.parse(s);
        }catch (Exception e){
            return null;
        }
    }

    private static Double parseDouble(String s) {
        /* TODO – return Double   or null */
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
               break;
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

    public static void monthToDateReport (){
        LocalDate today = LocalDate.now();
        LocalDate start = today.withDayOfMonth(1);
        filterTransactionsByDate(start,today);
    }
    public static void previousMonthReport (){
        LocalDate today = LocalDate.now();
        LocalDate previousMonth =today.minusMonths(1);
        LocalDate start = previousMonth.withDayOfMonth(1);
        LocalDate end = previousMonth.withDayOfMonth(previousMonth.lengthOfMonth());
        filterTransactionsByDate(start,end);
    }
    public static void yearToDateReport (){
        LocalDate today = LocalDate.now();
        LocalDate start = today.withDayOfYear(1);
        filterTransactionsByDate(start,today);
    }

    public static void previousYearReport (){
        LocalDate today = LocalDate.now();
        LocalDate previousYear = today.minusYears(1);
        LocalDate start = previousYear.withDayOfYear(1);
        LocalDate end = previousYear.withDayOfMonth(previousYear.lengthOfYear());

        filterTransactionsByDate(start,end);
    }
}
