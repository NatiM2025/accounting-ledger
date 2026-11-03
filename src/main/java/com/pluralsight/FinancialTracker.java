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
    LocalDate dateNow = LocalDate.now();
   public static String lastTransaction;    // Holds latest transaction
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

            BufferedWriter bufferedWriter =new BufferedWriter(new FileWriter(FILE_NAME,true));
            bufferedWriter.newLine();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;
            String amount = null;
            while ((line = bufferedReader.readLine()) != null){
                String[] parts =line.split("\\|");
                Transaction transaction = new Transaction(parts[0],parts[1],parts[2],parts[3]," $"+ parts[4]);
                lastTransaction = transaction.toString();
                amount = "$" + parts[4];

            }
            if (lastTransaction != null){
                System.out.println("Latest Transaction:");
                System.out.println(amount);
            }else {
                System.out.println("no transaction yet");
            }

        } catch (Exception e) {
            System.out.println("Something went wrong " + e);
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
        boolean exit = false;
        while (!exit) {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("transactions.csv", true));

                //saving to CSV file.
                Transaction transaction = new Transaction(transactionInfo(scanner).date,transactionInfo(scanner).time,
                        transactionInfo(scanner).amount,transactionInfo(scanner).vendor,transactionInfo(scanner).description);

                transactions.add(transaction);
                bufferedWriter.write(transaction.getDate() + " | " + transaction.getTime() + " | " + transaction.getDescription() + " | "
                        + transaction.getVendor() + " |" + "+" + transaction.getAmount());

                bufferedWriter.newLine();
                bufferedWriter.close();
                loadTransactions(FILE_NAME);
                System.out.println(lastTransaction + " Deposited successfully\n");
                System.out.println("X TO exit");
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
    private static void displayLedger() { /* TODO – print all transactions in column format, newest first */ }

    private static void displayDeposits() { /* TODO – only amount > 0               */ }

    private static void displayPayments() { /* TODO – only amount < 0               */ }

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
                case "1" -> {/* TODO – month-to-date report */ }
                case "2" -> {/* TODO – previous month report */ }
                case "3" -> {/* TODO – year-to-date report   */ }
                case "4" -> {/* TODO – previous year report  */ }
                case "5" -> {/* TODO – prompt for vendor then report */ }
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
    }

    private static void filterTransactionsByVendor(String vendor) {
        // TODO – iterate transactions, print those with matching vendor
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
        return null;
    }

    private static Double parseDouble(String s) {
        /* TODO – return Double   or null */
        return null;
    }
    //Validating user input if it is number or not
    public static boolean isnumber (String userInput){
        try {
            Double.parseDouble(userInput);
            return true;
        }catch (Exception e){
            return false;
        }

    }
    public static Transaction transactionInfo (Scanner scanner){

        LocalDate dateNow = LocalDate.now();
        LocalTime timeNow = LocalTime.now();

        System.out.println();
        System.out.println("Please Enter amount:\nAmount: ");
        String amount =  scanner.nextLine();

        if (!isnumber(amount)){
            System.out.println("Invalid input pleas use number more than 0!");
            return null;
        }
        System.out.println("Please Enter the vendor:\nVendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();

        return new Transaction(dateNow.toString(),timeNow.format(TIME_FMT),amount,vendor,description);
    }
}
