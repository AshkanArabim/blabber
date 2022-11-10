/* ASHKAN ARABI-MIANROODI
[CS1101 - FA22] Comprehensive Lab 2
This work is to be done individually. It is not 
permitted to share, reproduce, or alter any 
part of this
assignment for any purpose. Students are not 
permitted from sharing code, uploading this
assignment online in any form, or 
viewing/receiving/modifying code written from 
anyone else. This
assignment is part of an academic course at 
The University of Texas at El Paso and a 
grade will be
assigned for the work produced individually 
by the student. */

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Arabi_Ashkan_Blabber {
    public static void main(String[] args) {
        try {
            // if users.txt doesn't exist, create it
            File usersFile = new File("./users.txt");
            usersFile.createNewFile();
            // if blabs.txt doesn't exist, create it
            File blabsFile = new File ("./blabs.txt");
            blabsFile.createNewFile();
            // if the userFollows folder doesn't exist, create it
            File userFollows = new File ("./userFollows");
            userFollows.mkdir();
        } catch (Exception e) {
            System.out.println(e);
        }

        while (true) {
            clear();
            logo();
            out("Options: \n -CreateAccount [account name] \n -SignIn [account name] \n -Exit");
            input();
            String line = scan();
            String command = getCmd(line);
            String[] arguments = getArgs(line.toLowerCase());
            String[] users = getUsers("./users.txt", numLines("./users.txt"));
            createUserFiles(users); // creates an empty follow file for each user to avoid exceptions in the future
            switch (command) {
                case "CreateAccount": {
                    // check if username provided
                    // check if more than one username provided
                    // check if alphanumeric
                    // checks if length > 1
                    // checks if name is blabs
                    // checks if username exists
                    // if all goes smoothly, creates account
                    if (arguments.length == 0) {
                        noArgsErr();
                    } else if (arguments.length > 1) {
                        manyArgsErr();
                    } else if (!isAlphanumeric(arguments[0], 0)) {
                        out("Username is not Alphanumeric!");
                    } else if (arguments[0].length() <= 1) {
                        out("Username should be longer than 1 character!");
                    } else if (arguments[0].equals("blabs")) {
                        out("Username cannot be 'blabs'!");
                    } else if (accountExists(arguments[0], users)) {
                        out("User already exists!");
                    } else {
                        createAccount(arguments[0], users); // problem: account not written to users.txt
                        out("User " + arguments[0] + " created. Use the 'SignIn' command to use the account.");
                    }
                    break;
                }
                case "SignIn": {
                    // check if username provided
                    // check if more than one username provided
                    // check if account exists
                    // if no problems, sign in
                    if (arguments.length == 0) {
                        noArgsErr();
                    } else if (arguments.length > 1) {
                        manyArgsErr();
                    } else if (!accountExists(arguments[0], users)) {
                        noUserErr();
                    } else {
                        signedIn(arguments[0], users);
                        out("Signed out of " + arguments[0]);
                    }
                    break;
                }
                case "Exit": {
                    if (arguments.length > 0) {
                        argsNotAllowedErr();
                    } else {
                        System.exit(0);
                    }
                    break;
                }
                default:
                    badCommand();
            }
            enter();
        }
    }

    public static void signedIn(String username, String[] users) {
        while (true) {
            clear();
            logo();
            out("Signed in as " + username);
            out("Options: \n -ShowFollowing \n -FollowAccount [acc1] [acc2] ... \n -UnfollowAccount [acc] \n -PostBlab \n -ViewTimline \n -ViewTimelineReverse \n -SignOut \n -DeleteAccount");
            input();
            String line = scan();
            String command = getCmd(line);
            String[] arguments = getArgs(line.toLowerCase());
            switch (command) {
                case "ShowFollowing": {
                    if (arguments.length > 0) {
                        // anything that looks like this throws an error if user provides any arguments
                        // commands like this don't take any arguments.
                        argsNotAllowedErr();
                    } else {
                        showFollowing(username);
                    }
                    break;
                }
                case "FollowAccount": {
                    // check if no arguments provided
                    // if no problem, follow
                    if (arguments.length == 0) {
                        noArgsErr();
                    } else {
                        // the rest of the errors checked inside followAccount.
                        // why? because we'll just need one for loop for everything else.
                        for (int i = 0; i < arguments.length; i++) {
                            followAccount(username, arguments[i], users);
                        }
                    }
                    break;
                }
                case "UnfollowAccount": {
                    // test cases:
                    // username doesn't exist
                    // mors than one argument
                    // no argument
                    // if all dope, unfollow account.
                    if (!accountExists(username, users)) {
                        noUserErr();
                    } else if (arguments.length > 1) {
                        manyArgsErr();
                    } else if (arguments.length == 0) {
                        noArgsErr();
                    } else {
                        UnfollowAccount(username, arguments[0]);
                    }
                    break;
                }
                case "PostBlab": {
                    if (arguments.length > 0) {
                        argsNotAllowedErr();
                    } else {
                        postBlab(username);
                    }
                    break;
                }
                case "ViewTimeline": {
                    if (arguments.length > 0) {
                        argsNotAllowedErr();
                    } else {
                        out("");
                        viewTimeline(username, getBlabs(), getBlabs().length - 1);
                        out("");
                    }
                    break;
                }
                case "ViewTimelineReverse": {
                    if (arguments.length > 0) {
                        argsNotAllowedErr();
                    } else {
                        out("");
                        viewTimelineReverse(username, getBlabs(), 0);
                        out("");
                    }
                    break;
                }
                case "DeleteAccount": {
                    // check if arguments provided
                    if (arguments.length > 0) {
                        argsNotAllowedErr();
                    } else {
                        // confirmation
                        oneLine("Are you ABSOLUTELY SURE you want to delete your account? If so, type your username in UPPERCASE: ");
                        if (scan().equals(username.toUpperCase())) {
                            deleteAccount(username, users);
                            out("Account successfully deleted.");
                            return; // forcefully signs user out
                        } else {
                            out("Name doesn't match. Operation aborted.");
                        }
                    }
                    break;
                }
                case "SignOut": {
                    if (arguments.length > 0) {
                        argsNotAllowedErr();
                    } else {
                        return;
                    }
                    break;
                }
                default:
                    badCommand();
            }
            enter();
        }
    }

    public static void createUserFiles(String[] users) {
        // create a following file for each user 
        for (int i = 0; i < users.length; i++) {
            try {
                File userFile = new File("./userFollows/" + users[i] + ".txt");
                userFile.createNewFile();
            } catch (Exception e) {
                return;
            }
        }
    }

    public static String[] getUsers(String filename, int size) {
        try {
            File users = new File(filename);
            Scanner userScanner = new Scanner(users);
            String[] output = new String[size];
            for (int i = 0; i < size; i++) {
                String line = userScanner.nextLine();
                output[i] = line.split(" ")[0];
            }
            userScanner.close();
            return output;
        } catch (Exception e) {
            System.out.println(e);
        }
        return new String[1]; // just in case no users exist.
    }

    public static int numLines(String fileName) {
        int size = 0;
        try {
            File users = new File(fileName);
            Scanner userScanner = new Scanner(users);

            while (userScanner.hasNextLine()) {
                size++;
                userScanner.nextLine();
            }
            userScanner.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return size;
    }

    public static boolean isAlphanumeric(String phrase, int index) {
        char[] allowedChars = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
                'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6',
                '7', '8', '9' };

        if (index == phrase.length()) {
            return true;
        } else {
            for (int i = 0; i < allowedChars.length; i++) {
                if (phrase.charAt(index) == allowedChars[i]) {
                    return isAlphanumeric(phrase, index + 1);
                }
            }
            return false;
        }
    }

    public static void createAccount(String username, String[] users) {
        try {
            FileWriter userWriter = new FileWriter(new File("./users.txt"), true);
            userWriter.write(username + "\n");
            userWriter.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static boolean accountExists(String username, String[] users) {
        boolean accountExists = false;
        for (int i = 0; i < users.length; i++) {
            if (users[i].toLowerCase().equals(username))
                accountExists = true;
        }
        return accountExists;
    }

    public static void followAccount(String username, String usernameToFollow, String[] users) {
        try {
            // check if following self
            oneLine(usernameToFollow + " --> ");
            if (username.equals(usernameToFollow)) {
                out("You can't follow yourself!");
            }
            // check if username exists
            else if (!accountExists(usernameToFollow, users)) {
                noUserErr();
            }
            // check if already followed
            else if (checkFollowers(username, usernameToFollow)) {
                out("You already follow this account!");
            }
            // if no problem, follow
            else {
                File followingList = new File("./userFollows/" + username + ".txt");
                FileWriter followWriter = new FileWriter(followingList, true);
                followWriter.write(usernameToFollow + "\n");
                followWriter.close();
                out("Successfully followed " + usernameToFollow);
            }
        } catch (Exception e) {
            // error is thrown if file doesn't exist, but just ignore it :>
            System.out.println(e);
        }
    }

    public static void UnfollowAccount(String username, String usernameToUnfollow) {
        try {
            String fileName = "./userFollows/" + username + ".txt";
            deleteLineFromFile(usernameToUnfollow, fileName);

            out("Successfully unfollowed " + usernameToUnfollow + "!");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static boolean checkFollowers(String username, String usernameToFollow) {
        // This method is also used when checking which blab to display
        boolean alreadyFollowed = false;
        try {
            File followingList = new File("./userFollows/" + username + ".txt");
            Scanner followReader = new Scanner(followingList);
            while (followReader.hasNext()) {
                String follows = followReader.next();
                if (follows.equals(usernameToFollow)) {
                    alreadyFollowed = true;
                }
            }
            followReader.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return alreadyFollowed;
    }

    public static void postBlab(String username) {
        try {
            FileWriter blabWriter = new FileWriter(new File("./blabs.txt"), true);
            out("Please enter your blab. When done, press Enter.");
            String blab = scan();
            blabWriter.write(username + " " + blab + "\n");
            blabWriter.close();
            out("Blab successfully posted.");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static String[] getBlabs() {
        try {
            Scanner blabScanner = new Scanner(new File("./blabs.txt"));
            String[] blabList = new String[numLines("./blabs.txt")]; // 3: correct
            int i = 0;
            while (blabScanner.hasNextLine()) {
                String test = blabScanner.nextLine();
                blabList[i] = test;
                i++;
            }
            return blabList;
        } catch (Exception e) {
            System.out.println();
        }
        return new String[1];
    }

    public static void viewTimeline(String username, String[] blabs, int idx) {
        if (idx < 0) {
            out("\n **Most recent blabs are on the top.**");
            return;
        } else {
            printBlab(username, blabs[idx]);
            viewTimeline(username, blabs, idx - 1);
        }
    }

    public static void viewTimelineReverse(String username, String[] blabs, int idx) {
        if (idx > blabs.length - 1) {
            out("\n **Most recent blabs are on the bottom.**");
            return;
        } else {
            printBlab(username, blabs[idx]);
            viewTimelineReverse(username, blabs, idx + 1);
        }
    }

    public static void printBlab(String username, String blab) {
        Scanner blabScanner = new Scanner(blab);
        String author = blabScanner.next();
        String content = blabScanner.nextLine();
        if (checkFollowers(username, author) || author.equals(username)) {
            out("[ " + author + " ] " + content);
        }
        blabScanner.close();
    }

    public static void showFollowing(String username) {
        try {
            File following = new File("./userFollows/" + username + ".txt");
            Scanner followingScanner = new Scanner(following);
            // the following code runs at least once:
            // if the file is empty, it throws a NoSuchElementException error
            // if that happens, we tell the user that they haven't followed anyone.
            do {
                out(followingScanner.next());
            } while (followingScanner.hasNext());

            followingScanner.close();
        } catch (NoSuchElementException e) {
            System.out.println("You haven't followed any accounts yet!");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void deleteAccount(String username, String[] users) { // IN PROGRESS
        // delete all posted blabs
        deleteLineFromFile(username, "./blabs.txt");

        // delete name from all following lists
        for (int i = 0; i < users.length; i++) {
            deleteLineFromFile(username, "./userFollows/" + users[i] + ".txt");
        }

        // delete name from users file
        deleteLineFromFile(username, "./users.txt");

        // delete following file
        try{
            Files.delete(Paths.get("./userFollows/" + username + ".txt"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void deleteLineFromFile(String keyword, String fileName) {
        // get file
        // put all lines in array
        // see if first word of line equals keyword
        // if not, write and put \n in the end

        try {
            File file = new File(fileName);
            Scanner reader = new Scanner(file);
            String[] list = new String[numLines(fileName)];
            for (int i = 0; i < list.length; i++) {
                list[i] = reader.nextLine();
            }

            // erase file to rewrite later
            clearFile(file);

            FileWriter writer = new FileWriter(file);
            for (int i = 0; i < list.length; i++) {
                Scanner lineReader = new Scanner(list[i]);
                if (lineReader.next().equals(keyword)) {
                    continue;
                } else {
                    writer.write(list[i] + "\n");
                }
                lineReader.close();
            }
            reader.close();
            writer.close();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static String scan() {
        Scanner consoleScan = new Scanner(System.in);
        return consoleScan.nextLine();
    }

    public static String[] splitString(String str) {
        return str.split(" ");
    }

    public static String getCmd(String inputLine) {
        return splitString(inputLine)[0];
    }

    public static String[] getArgs(String inputLine) {
        String[] splittedLine = splitString(inputLine);
        String[] args = new String[splitString(inputLine).length - 1];
        for (int i = 1; i < splittedLine.length; i++) {
            args[i - 1] = splittedLine[i];
        }
        return args;
    }

    public static void clearFile(File file) {
        try {
            FileWriter fw = new FileWriter(file);
            fw.write("");
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void out(String message) {
        System.out.println(message);
    }

    public static void oneLine(String message) {
        System.out.print(message);
    }

    public static void enter() {
        out("Press Enter to continue...");
        scan();
    }

    public static void badCommand() {
        out("Invalid command!");
    }

    public static void clear() {
        oneLine("\033[H\033[2J");
        System.out.flush();
    }

    public static void logo() {
        out("                                                \n,--.   ,--.        ,--.   ,--.                  \n|  |-. |  | ,--,--.|  |-. |  |-.  ,---. ,--.--. \n| .-. '|  |' ,-.  || .-. '| .-. '| .-. :|  .--' \n| `-' ||  |\\ '-'  || `-' || `-' |\\   --.|  |    \n `---' `--' `--`--' `---'  `---'  `----'`--'");
    }

    public static void input() {
        oneLine(" > ");
    }

    public static void noArgsErr() {
        out("Please provide an argument!");
    }

    public static void manyArgsErr() {
        out("Too many arguments provided!");
    }

    public static void noUserErr() {
        out("User doesn't exist!");
    }

    public static void argsNotAllowedErr() {
        out("This command doesn't take any arguments!");
    }
}
