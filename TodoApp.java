import java.io.*;
import java.util.*;

public class TodoApp {

    private static final String FILE_NAME = "tasks.txt";
    private static List<Task> tasks = new ArrayList<>();
    private static int nextId = 1;

    // Inner class for Task
    static class Task {
        int id;
        String description;
        boolean isCompleted;

        Task(int id, String description, boolean isCompleted) {
            this.id = id;
            this.description = description;
            this.isCompleted = isCompleted;
        }

        @Override
        public String toString() {
            return "[" + (isCompleted ? "X" : " ") + "] " + id + ": " + description;
        }
    }

    public static void main(String[] args) {
        loadTasksFromFile();
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== TODO APP ===");
            System.out.println("1. Add Task");
            System.out.println("2. List Tasks");
            System.out.println("3. Mark Task as Complete");
            System.out.println("4. Delete Task");
            System.out.println("5. Exit");
            System.out.print("Choose: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    addTask(sc);
                    break;
                case "2":
                    listTasks();
                    break;
                case "3":
                    markTaskComplete(sc);
                    break;
                case "4":
                    deleteTask(sc);
                    break;
                case "5":
                    saveTasksToFile();
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }

        sc.close();
    }

    private static void addTask(Scanner sc) {
        System.out.print("Enter task description: ");
        String desc = sc.nextLine().trim();
        if (desc.isEmpty()) {
            System.out.println("Task cannot be empty.");
            return;
        }
        Task task = new Task(nextId++, desc, false);
        tasks.add(task);
        System.out.println("Task added.");
    }

    private static void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }
        System.out.println("\nYour Tasks:");
        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    private static void markTaskComplete(Scanner sc) {
        System.out.print("Enter task ID to mark complete: ");
        String input = sc.nextLine();
        try {
            int id = Integer.parseInt(input);
            for (Task task : tasks) {
                if (task.id == id) {
                    if (task.isCompleted) {
                        System.out.println("Task already completed.");
                    } else {
                        task.isCompleted = true;
                        System.out.println("Task marked as complete.");
                    }
                    return;
                }
            }
            System.out.println("Task ID not found.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private static void deleteTask(Scanner sc) {
        System.out.print("Enter task ID to delete: ");
        String input = sc.nextLine();
        try {
            int id = Integer.parseInt(input);
            Iterator<Task> it = tasks.iterator();
            while (it.hasNext()) {
                Task task = it.next();
                if (task.id == id) {
                    it.remove();
                    System.out.println("Task deleted.");
                    return;
                }
            }
            System.out.println("Task ID not found.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private static void loadTasksFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int maxId = 0;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", 3);
                if (parts.length == 3) {
                    int id = Integer.parseInt(parts[0]);
                    boolean completed = parts[1].equals("1");
                    String desc = parts[2];
                    tasks.add(new Task(id, desc, completed));
                    maxId = Math.max(maxId, id);
                }
            }
            nextId = maxId + 1;
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
    }

    private static void saveTasksToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Task task : tasks) {
                String line = task.id + "|" + (task.isCompleted ? "1" : "0") + "|" + task.description;
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }
}