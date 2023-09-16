import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.InputMismatchException;
import java.util.Scanner;

final class Cinema{
    public String entrance;
    public String exit;
    public JSONArray rows;

    public Cinema(String entrance, String exit, JSONArray rows){
        this.entrance = entrance;
        this.exit = exit;
        this.rows = rows;
    }
}

public class Main {
    public static Cinema getDetails(String filename) throws Exception {
        try {
            Object obj = new JSONParser().parse(new FileReader(filename + ".json"));
            JSONObject jo = (JSONObject) obj;

            String entrance = (String) jo.get("entrance");
            String exit = (String) jo.get("exit");
            JSONArray rows = (JSONArray) jo.get("rows");

            return new Cinema(entrance, exit, rows);
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }

        System.exit(0);
        return null;
    }

    public static void checkJSONValid(String entrance, String exit, JSONArray rows){
        int totalSize = 0;
        int rowSize = rows.size();

        for (int i = 0; i < rowSize; i++) {
            totalSize += Integer.parseInt(rows.get(i).toString());
            if (totalSize == 0){
                System.out.println("JSON file input invalid!");
                System.out.print("\033[3mCinema has no seats!\033[0m");
                System.exit(0);
            }
        }

        if (!((entrance.equals("r") && exit.equals("r")) || (entrance.equals("l") && exit.equals("l")) || (entrance.equals("r") && exit.equals("l")) || (entrance.equals("l") && exit.equals("r")))){
            System.out.println("JSON file input invalid!");
            System.out.print("\033[3mEntrance & exit should be 'l/r' only!\033[0m");
            System.exit(0);
        }
    }

    public static ArrayList<ArrayList<String>> createList(JSONArray rows) {
        int rowSize = rows.size();
        ArrayList<ArrayList<String>> lists = new ArrayList<>();

        for (int i = 0; i < rowSize; i++) {
            lists.add(new ArrayList<>());
        }

        return lists;
    }

    public static ArrayList<ArrayList<String>> fillList(int row, String name, String entrance, String exit, JSONArray rows, ArrayList<ArrayList<String>> lists) {
        int listSize = lists.get(row).size();
        int maxListSize = Integer.parseInt(rows.get(row).toString());

        if ((entrance.equals("r") && exit.equals("r")) || (entrance.equals("r") && exit.equals("l"))) {
            if (listSize < maxListSize) {
                lists.get(row).add(name);
            }
        } else if ((entrance.equals("l") && exit.equals("l")) || (entrance.equals("l") && exit.equals("r"))) {
            if (listSize < maxListSize) {
                lists.get(row).add(0, name);
            }
        }
        return lists;
    }

    public static void printList(JSONArray rows, ArrayList<ArrayList<String>> lists) {
        int rowSize = rows.size();

        for (int i = 0; i < rowSize; i++) {
            System.out.print("\t" + lists.get(i));
            System.out.println("[" + Integer.parseInt(rows.get(i).toString()) + "]");
        }
    }

    public static void printListStackExit(JSONArray rows, ArrayList<ArrayList<String>> stacks) {
        int rowSize = rows.size();
        int max = stacks.get(0).size();

        for (int i = 0; i < rowSize; i++) {
            if (max < stacks.get(i).size()) {
                max = stacks.get(i).size();
            }
        }

        for (int j = 0; j < max; j++) {
            for (int i = 0; i < rowSize; i++) {
                if (j <= stacks.get(i).size() - 1) {
                    int last = stacks.get(i).size() - 1;
                    System.out.print(stacks.get(i).get(last - j));
                }
            }
            System.out.println();
        }
    }

    public static void printListQueueExit(JSONArray rows, ArrayList<ArrayList<String>> queues) {
        int rowSize = rows.size();
        int max = queues.get(0).size();

        for (int i = 0; i < rowSize; i++) {
            if (max < queues.get(i).size()) {
                max = queues.get(i).size();
            }
        }

        for (int j = 0; j < max; j++) {
            for (int i = 0; i < rowSize; i++) {
                if (j <= queues.get(i).size() - 1) {
                    System.out.print(queues.get(i).get(j));
                }
            }
            System.out.println();
        }
    }

    public static String getName(JSONArray rows, ArrayList<ArrayList<String>> lists) {
        Scanner nameObj = new Scanner(System.in);
        System.out.print("What is the customer's name?: ");
        String name = nameObj.nextLine();

        if (lists != null) {
            int rowSize = rows.size();
            int max = lists.get(0).size();
            boolean nameTaken = true;

            for (int i = 0; i < rowSize; i++) {
                if (max < lists.get(i).size()) {
                    max = lists.get(i).size();
                }
            }

            do {
                for (int j = 0; j < max; j++) {
                    for (int i = 0; i < rowSize; i++) {
                        if (j <= lists.get(i).size() - 1) {
                            if (lists.get(i).get(j).equals(name)) {
                                System.out.println("Name already exists!");
                                System.out.print("\033[3mPlease enter a different name: \033[0m");
                                name = nameObj.nextLine();
                                System.out.println();
                                nameTaken = true;
                            } else if (!lists.get(i).get(j).equals(name)) {
                                nameTaken = false;
                            }
                        }
                    }
                }
            } while (nameTaken);
        }
        return name;
    }

    public static int getRow(JSONArray rows, ArrayList<ArrayList<String>> list) {
        try {
            int row;
            int customers = 0;
            int maxCustomers = 0;
            int rowSize = rows.size();
            boolean full;

            Scanner rowObj = new Scanner(System.in);
            System.out.print("What is the customer's row?: ");
            row = rowObj.nextInt() - 1;
            System.out.println();

            for (int i = 0; i < rowSize; ++i) {
                customers += list.get(i).size();
                maxCustomers += Integer.parseInt(rows.get(i).toString());
            }

            int listSize = list.get(row).size();
            int maxListSize = Integer.parseInt(rows.get(row).toString());

            do {
                if (customers == maxCustomers) {
                    System.out.println("Sorry, the cinema is already full!");
                    System.out.println();
                    full = false;
                } else if (listSize == maxListSize) {
                    System.out.println("Sorry, that row is full!");
                    System.out.print("\033[3mPlease enter a different row: \033[0m");
                    row = rowObj.nextInt() - 1;
                    System.out.println();
                    listSize = list.get(row).size();
                    maxListSize = Integer.parseInt(rows.get(row).toString());
                    full = true;
                } else {
                    full = false;
                }
            } while (full);
            return row;
        } catch (ArrayIndexOutOfBoundsException | InputMismatchException | NullPointerException e){
            System.out.println("\033[3mPlease enter a valid name and/or row!\033[0m");
            System.out.println();
        }
        return 0;
    }

    public static int getMax(JSONArray rows){
        int rowSize = rows.size();
        int max = Integer.parseInt(rows.get(0).toString());

        for (int i = 0; i < rowSize; i++) {
            if (max < Integer.parseInt(rows.get(i).toString())) {
                max = Integer.parseInt(rows.get(i).toString());
            }
        }
        return max;
    }

    public static void main(String[] args) throws Exception {
        Scanner fileObj = new Scanner(System.in);
        System.out.print("Please input the JSON file name ");
        System.out.print("\033[3m(Do not include '.json'): \033[0m");
        String filename = fileObj.nextLine();

        Cinema cinema = getDetails(filename);

        boolean condition = true;
        boolean rowValid;
        boolean nameValid;

        ArrayList<ArrayList<String>> list = createList(cinema.rows);
        ArrayList<ArrayList<String>> filledList = null;
        checkJSONValid(cinema.entrance, cinema.exit, cinema.rows);

        do {
            System.out.println();
            System.out.println("[1] Input a customer");
            System.out.println("[2] Print exit plan");
            System.out.println("[3] Exit");
            System.out.println();

            Scanner choiceObj = new Scanner(System.in);

            while (!choiceObj.hasNextInt()) {
                System.out.println("\033[3mPlease enter a valid choice!\033[0m");
                System.out.println();
                choiceObj.nextLine();
            }

            int choice = choiceObj.nextInt();

            switch (choice) {
                case 1:
                    String name = getName(cinema.rows, filledList);
                    int row = getRow(cinema.rows, list);
                    int max = getMax(cinema.rows);

                    int rowSize = cinema.rows.size();

                    rowValid = row >= 0 && row < rowSize;
                    nameValid = name.matches("[A-Za-z]");

                    if (nameValid && rowValid) {
                        if (cinema.entrance.equals("r") && cinema.exit.equals("r")) {
                            for (int i = 0; i <= max; ++i) {
                                System.out.print("\t");
                            }

                            System.out.println("\uD83D\uDEAA");

                            filledList = fillList(row, name, cinema.entrance, cinema.exit, cinema.rows, list);
                            printList(cinema.rows, filledList);

                        } else if (cinema.entrance.equals("l") && cinema.exit.equals("l")) {
                            System.out.println("\uD83D\uDEAA");

                            filledList = fillList(row, name, cinema.entrance, cinema.exit, cinema.rows, list);
                            printList(cinema.rows, filledList);

                        } else if (cinema.entrance.equals("r") && cinema.exit.equals("l")) {
                            System.out.print("\uD83D\uDEAA");
                            System.out.print("\u2B05\uFE0F");

                            for (int i = 0; i < max; ++i) {
                                System.out.print("   ");
                            }

                            System.out.print("\u2B05\uFE0F");
                            System.out.println("\uD83D\uDEAA");

                            filledList = fillList(row, name, cinema.entrance, cinema.exit, cinema.rows, list);
                            printList(cinema.rows, filledList);

                        } else if (cinema.entrance.equals("l") && cinema.exit.equals("r")) {
                            System.out.print("\uD83D\uDEAA");
                            System.out.print("\u27A1\uFE0F");

                            for (int i = 0; i < max; ++i) {
                                System.out.print("   ");
                            }

                            System.out.print("\u27A1\uFE0F");
                            System.out.println("\uD83D\uDEAA");

                            filledList = fillList(row, name, cinema.entrance, cinema.exit, cinema.rows, list);
                            printList(cinema.rows, filledList);
                        }
                    } else {
                        System.out.println("\033[3mPlease enter a valid name and/or row!\033[0m");
                    }
                    break;

                    case 2:
                        if (filledList != null) {
                            if (cinema.entrance.equals("r") && cinema.exit.equals("r")) {
                                printListStackExit(cinema.rows, filledList);
                            } else if (cinema.entrance.equals("l") && cinema.exit.equals("l")) {
                                printListQueueExit(cinema.rows, filledList);
                            } else if (cinema.entrance.equals("r") && cinema.exit.equals("l")) {
                                printListQueueExit(cinema.rows, filledList);
                            } else if (cinema.entrance.equals("l") && cinema.exit.equals("r")) {
                                printListStackExit(cinema.rows, filledList);
                            }
                        } else {
                            System.out.println("\033[3mThere are no customers!\033[0m");
                        }
                        break;

                    case 3:
                        condition = false;
                        break;

                    default:
                        System.out.println("\033[3mPlease enter a valid choice:\033[0m");
                }
            } while (condition);
    }
}

