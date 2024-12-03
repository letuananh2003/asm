import java.util.*;

class Student {
    private String id;
    private String name;
    private double marks;
    private String rank;
// đóng gói
    public Student(String id, String name, double marks) {
        this.id = id;
        this.name = name;
        this.marks = marks;
        this.rank = calculateRank();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMarks() {
        return marks;
    }

    public String getRank() {
        return rank;
    }

    public void setMarks(double marks) {
        this.marks = marks;
        this.rank = calculateRank();
    }// de sap xep hạng điểm

    private String calculateRank() {
        if (marks >= 0 && marks <= 5.0) {
            return "Fail";
        } else if (marks > 5.0 && marks <= 6.5) {
            return "Medium";
        } else if (marks > 6.5 && marks <= 7.5) {
            return "Good";
        } else if (marks > 7.5 && marks <= 9.0) {
            return "Very Good";
        } else if (marks > 9.0 && marks <= 10.0) {
            return "Excellent";
        } else {
            return "Invalid Marks";
        }
    }// đieu kien

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Marks: " + marks + ", Rank: " + rank;
    }
}

public class StudentManagementSystem {
    private static ArrayList<Student> students = new ArrayList<>();// luu tru sinh vien
    private static Stack<String> actionHistory = new Stack<>();// để lưu
    private static Scanner scanner = new Scanner(System.in);// dể nhập dữ liệu

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\nStudent Management System");
            System.out.println("1. Add Student");
            System.out.println("2. Edit Student Marks");
            System.out.println("3. Delete Student");
            System.out.println("4. View Students");
            System.out.println("5. Sort Students by Marks (Bubble Sort)");
            System.out.println("6. Sort Students by Marks (Quick Sort)");
            System.out.println("7. Search Student by ID");
            System.out.println("8. Undo Last Action");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");
            try {
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {// gọi các phương thức
                    case 1:
                        addStudent();
                        break;
                    case 2:
                        editStudentMarks();
                        break;
                    case 3:
                        deleteStudent();
                        break;
                    case 4:
                        viewStudents();
                        break;
                    case 5:
                        bubbleSort(students);
                        System.out.println("Students sorted by marks using Bubble Sort.");
                        viewStudents();
                        break;
                    case 6:
                        quickSort(students, 0, students.size() - 1);
                        System.out.println("Students sorted by marks using Quick Sort.");
                        viewStudents();
                        break;
                    case 7:
                        searchStudentById();
                        break;
                    case 8:
                        undoLastAction();
                        break;
                    case 9:
                        System.out.println("Exiting...");//
                        break;
                    default:
                        System.out.println("Invalid choice. Try again."); // báo nhậo sai
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a valid number.");
                scanner.nextLine();
                choice = 0;
            }
        } while (choice != 9);
    }

    private static void addStudent() {// gồm các lỗi ngoài lề
        try {
            System.out.print("Enter Student ID: ");
            String id = scanner.nextLine().trim();
            if (id.isEmpty()) {//Kiểm tra xem mã số sinh viên có rỗng hay không.
                throw new IllegalArgumentException("Student ID cannot be empty.");
            }
            if (students.stream().anyMatch(student -> student.getId().equals(id))) {
                throw new IllegalArgumentException("Student ID already exists.");
            }

            System.out.print("Enter Student Name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Student name cannot be empty.");
            }

            System.out.print("Enter Student Marks: ");
            double marks = scanner.nextDouble();
            scanner.nextLine(); // Clear buffer

            if (marks < 0 || marks > 10) {
                throw new IllegalArgumentException("Marks should be between 0 and 10."); // loii ngoai le
            }

            students.add(new Student(id, name, marks));
            actionHistory.push("ADD " + id);
            System.out.println("Student added successfully.");
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input for marks. Please enter a numeric value.");
            scanner.nextLine(); // Clear buffer
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void editStudentMarks() {
        try {
            System.out.print("Enter Student ID to edit marks: ");
            String id = scanner.nextLine().trim();

            Optional<Student> studentOptional = students.stream()
                    .filter(student -> student.getId().equals(id))
                    .findFirst();

            if (studentOptional.isEmpty()) {
                System.out.println("Student not found.");
                return;
            }

            Student student = studentOptional.get();
            System.out.print("Enter new marks: ");
            double marks = scanner.nextDouble();
            scanner.nextLine(); // Clear buffer

            if (marks < 0 || marks > 10) {
                throw new IllegalArgumentException("Marks should be between 0 and 10.");
            }

            double oldMarks = student.getMarks();
            student.setMarks(marks);
            actionHistory.push("EDIT " + id + " " + oldMarks);
            System.out.println("Marks updated successfully.");
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input for marks. Please enter a numeric value.");
            scanner.nextLine(); // Clear buffer
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void deleteStudent() {
        System.out.print("Enter Student ID to delete: ");
        String id = scanner.nextLine().trim();
        Iterator<Student> iterator = students.iterator();
        while (iterator.hasNext()) {
            Student student = iterator.next();
            if (student.getId().equals(id)) {
                actionHistory.push("DELETE " + student.getId() + " " + student.toString());
                iterator.remove();
                System.out.println("Student deleted successfully.");
                return;
            }
        }
        System.out.println("Student not found.");
    }

    private static void viewStudents() {// xem thong tin sinh vien bang sbd
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            for (Student student : students) {
                System.out.println(student);
            }
        }
    }
/// phải sử dụng 2 vòng lặp
private static void bubbleSort(ArrayList<Student> arr) {
        // dung thuật tóan bbblesort đẻ sắp xếp điểm toán từ cao đến thấp
    for (int i = 0; i < arr.size() - 1; i++) {
        // chay vong lâp từ 0 đén size (), day so diem lon nhat len trên cùng // j là một chỉ số trong index
        for (int j = 0; j < arr.size() - i - 1; j++) {
            //so sánh các phần tử liền kề và hóan đổi vị trí, dua phan tu lớn nhất về đúng
            if (arr.get(j).getMarks() < arr.get(j + 1).getMarks()) {
                // skhiểm tra điêm toán nhỏ nhơn j + 1
                Collections.swap(arr, j, j + 1);
                // kiểm tra nếu điwwu kiện ở trên đúng thì hoán đổi vị trí cho đúng
            }
        }
    }
}

    private static void quickSort(ArrayList<Student> students, int low, int high) {
        if (low < high) {// low là phần tử đầu tiên còn hì là phần tử cuối cùng
            //NNẾU bên  phải lớn hơn bên trái thì k cần xếp
            // nếu trái nhỏ hơn phải thì phân đoạn xếp tiếp
            double pivot = students.get(high).getMarks();
            // lấy phần tử cuói cùng làm cọc  piivot có nghia là cọc
            int i = (low - 1);
// theo doi các phần tử bé hơn hoặc lớn hơn cọc để xếp
            for (int j = low; j < high; j++) {
                // là so sámh bên trái với bên phải với cọc xong xếp cho đúng
                if (students.get(j).getMarks() >= pivot) {// xác dịnh các nhóm lớn hhown hoặc bé hơn cọc
                    i++;//tan chhi so i de tiep tục sắp xếp
                    Collections.swap(students, i, j);// hoaan đôi vi tri i vs j
                }// swap là ngôn ngũ để hoán đổi vị trí
            }
            Collections.swap(students, i + 1, high); // hoan doii coc vơi phan tu cuoi voi phan tu i+1
            // de dua coc vao dung vii trii

            int pi = i + 1;
// lưu lại  vị trí sau khi hoán đổi
            quickSort(students, low, pi - 1); // tiep tuc so sanh bên trái với voi coc
            quickSort(students, pi + 1, high); // so sanh ben trai
        }
    }


    private static void searchStudentById() {
        System.out.print("Enter Student ID to search: ");
        String id = scanner.nextLine(); // tìm sinh viên theo mã á\\
        for (Student student : students) {
            if (student.getId().equals(id)) {
                System.out.println(student);
                return;
            }
        }
        System.out.println("Student not found.");
    }

    private static void undoLastAction() {// trở lại 1 bước
        if (actionHistory.isEmpty()) {
            System.out.println("No actions to undo.");
            return;
        }

        String lastAction = actionHistory.pop(); //Kiểm tra xem ngăn xếp lưu lịch sử hành động có rỗng hay không
        String[] actionDetails = lastAction.split(" ");
        String actionType = actionDetails[0];
        String studentId = actionDetails[1];

        switch (actionType) {// để hoàn tác một bước  sử dụng ngăn xếp
            case "ADD": //Lấy và xóa hành động cuối cùng trong ngăn xếp.
                students.removeIf(student -> student.getId().equals(studentId));
                System.out.println("Undo Add: Student with ID " + studentId + " removed.");
                break;
            case "EDIT": // thoát
                double oldMarks = Double.parseDouble(actionDetails[2]);
                for (Student student : students) {
                    if (student.getId().equals(studentId)) {
                        student.setMarks(oldMarks);
                        System.out.println("Undo Edit: Marks of student with ID " + studentId + " reverted to " + oldMarks + ".");
                        break;
                    }

                }
        }

 }   }
