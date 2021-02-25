import java.io.*;
import java.util.*;

public class Main {

    /**
     *
     *
     * 6 2 7  There are 6 books, 2 libraries, and 7 days for scanning.
     *
     * 1 2 3 6 5 4  The scores of the books are 1, 2, 3, 6, 5, 4 (in order)
     *
     * 5 2 2 Library 0 has 5 books, the signup process takes 2 days, and the library
     * can ship 2 books per day.
     *
     * 0 1 2 3 4 The books in library 0 are: book 0, book 1, book 2, book 3, and book 4
     *
     * 4 3 1 Library 1 has 4 books, the signup process takes 3 days, and the library can
     * ship 1 book per day.
     *
     * 3 2 5 0 The books in library 1 are: book 3, book 2, book 5 and book 0.
     *
     * Your submission describes which books to ship from which library and the order in
     * which libraries are signed up
     *
     * 2 Two libraries will be signed up for scanning.
     *
     * 1 3  The rst library to do the signup process is library 1. Aer
     * the signup process it will send 3 books for scanning.
     *
     * 5 2 3 Library 1 will send book 5, book 2, and book 3 in order
     *
     * 0 5 The second library to do the signup process is library 0.
     * Aer the signup process it will send 5 books.
     *
     * 0 1 2 3 4 Library 0 will send book 0, book 1, book 2, book 3 and
     * book 4 in that order.
     */

    static int numberOfDays=0;

    public static void main(String[] args) {


        // The name of the file to open.

        //String fileNamePrefix = "a_example";
       // String fileNamePrefix = "b_read_on";
       //String fileNamePrefix = "c_incunabula";
        //String fileNamePrefix = "d_tough_choices";
        //String fileNamePrefix = "e_so_many_books";
        String fileNamePrefix = "f_libraries_of_the_world";

        //String fileName = fileNamePrefix + ".txt";
        String fileName = fileNamePrefix + ".txt";

        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String firstLine = bufferedReader.readLine(); //There are 6 books, 2 libraries, and 7 days for scanning.

            String[] firstLineArr = firstLine.split(" ");

            int numberOfBook = Integer.parseInt(firstLineArr[0]);
            int numberOfLibraries = Integer.parseInt(firstLineArr[1]);
            numberOfDays = Integer.parseInt(firstLineArr[2]);

            System.out.println("Number of Books: " + numberOfBook);
            System.out.println("Number of Libraries: " + numberOfLibraries);
            System.out.println("Number of Days: " + numberOfDays);

            String secondLine = bufferedReader.readLine(); // The scores of the books are 1, 2, 3, 6, 5, 4 (in order)
            String [] secondLineArr = secondLine.split(" ");

            Map<Integer, Integer> bookScore = new HashMap<>();

            for(int i=0;i<secondLineArr.length; i++){
                bookScore.put(i, Integer.parseInt(secondLineArr[i]));
                System.out.println("index: " + i + " score: " + secondLineArr[i]);
            }

            List<Library> libraries = new ArrayList<>();
           // Map<Integer, List<Book>>  bookMap = new HashMap<>(); // libid, books

            for(int i = 0;i<numberOfLibraries;i++){
                // Library 0 has 5 books, the signup process takes 2 days, and the library
                // can ship 2 books per day.
                String libraryInfo = bufferedReader.readLine();
                String []libraryInfoArr = libraryInfo.split(" ");
                Library library = new Library(i,
                        Integer.parseInt(libraryInfoArr[0]),
                        Integer.parseInt(libraryInfoArr[1]),
                        Integer.parseInt(libraryInfoArr[2]));

                // The books in library 0 are: book 0, book 1, book 2, book 3, and book 4
                String booksinfo = bufferedReader.readLine();
                String [] booksArr = booksinfo.split(" ");

                List<Book> books = new ArrayList<>();

                for(int j=0;j<booksArr.length ;j++){
                    Book bookItem = new Book(Integer.parseInt(booksArr[j]), bookScore.get(Integer.parseInt(booksArr[j])));
                    books.add(bookItem);
                }

                Collections.sort(books, new BookCompator());

                for(int j=0;j<books.size() ;j++){
                    System.out.println("book id: " + books.get(j).getId() + " score: " + books.get(j).getScore());
                }


                library.setBooks(books);



                //bookMap.put(library.libId, books);

                libraries.add(library);
            }

            //Collections.sort(libraries, new LibraryCompator());


            /*Set<Integer> addedBook = new HashSet<>();

            for(int i=0;i<libraries.size();i++){
                List<Book> libBooks = new ArrayList<>();
                for(int j=0;j<bookMap.get(libraries.get(i).getLibId()).size();j++){
                    if(!addedBook.contains(bookMap.get(libraries.get(i).getLibId()).get(j))){
                        libBooks.add(bookMap.get(libraries.get(i).getLibId()).get(j));
                    }
                }

                libraries.get(i).setBooks(libBooks);

            }*/

            Collections.sort(libraries, new LibraryExtendedCompator());

            // Always close files.
            bufferedReader.close();

            FileWriter fileWriter = new FileWriter(fileNamePrefix + ".out");
            PrintWriter printWriter = new PrintWriter(fileWriter);

           int numberOfScannableLibs = 0;

           int tempNumberOfDays = numberOfDays;

           while(tempNumberOfDays>=0 && numberOfScannableLibs< libraries.size()){
               if(libraries.get(numberOfScannableLibs).getSignupTime() <= tempNumberOfDays){

                   tempNumberOfDays -= libraries.get(numberOfScannableLibs).getSignupTime();
                   numberOfScannableLibs++;
               }else{
                   break;
               }
           }

           System.out.println("number of scannable libs: " + numberOfScannableLibs); // First output line
            printWriter.println(numberOfScannableLibs);


            Set<Integer> scannedBook = new HashSet<>(); // keeps book id


            for(int i = 0; i< numberOfScannableLibs; i++){
                Library library = libraries.get(i);

                numberOfDays -= library.getSignupTime();

                int libraryBookDay = numberOfDays;

                int bookCount = 0;

                List<Integer> bookIds = new ArrayList<>();

                for(int j = 0; j< library.getBooks().size() && libraryBookDay > 0; j++){

                    if(!scannedBook.contains(library.getBooks().get(j).getId())){
                        scannedBook.add(library.getBooks().get(j).getId());

                        bookIds.add(library.getBooks().get(j).getId());
                        bookCount++;

                        if(bookCount % library.getMaxScanPerDay() == 0) {
                            libraryBookDay--;
                        }

                    }

                }

                System.out.println("library id: " + library.getLibId() + " number of books : " + bookCount);

                if(bookCount == 0){
                    bookCount = 1;
                    bookIds.add(library.getBooks().get(0).getId());
                }

                printWriter.println(library.getLibId() + " " + bookCount);

                for(int j=0;j<bookCount; j++){

                    System.out.print(bookIds.get(j) + " ");

                    if(j -1 < bookCount)
                        printWriter.print(bookIds.get(j) + " ");
                    else
                        printWriter.print(bookIds.get(j));
                }

                System.out.println("");
                printWriter.println("");

            }


            printWriter.close();

        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        }
        catch(IOException ex) {
            System.out.println("Error reading file '"+ fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
    }

    private static class Book{
        int id;
        int score;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public Book(int id, int score) {
            this.id = id;
            this.score = score;

        }

        public Book(){

        }
    }

    private static class Library{
        int libId;
        int numberOfBooks;
        int signupTime;
        int maxScanPerDay;
        List<Book> books;

        public int getLibId() {
            return libId;
        }

        public void setLibId(int libId) {
            this.libId = libId;
        }

        public int getNumberOfBooks() {
            return numberOfBooks;
        }

        public void setNumberOfBooks(int numberOfBooks) {
            this.numberOfBooks = numberOfBooks;
        }

        public int getSignupTime() {
            return signupTime;
        }

        public void setSignupTime(int signupTime) {
            this.signupTime = signupTime;
        }

        public int getMaxScanPerDay() {
            return maxScanPerDay;
        }

        public void setMaxScanPerDay(int maxScanPerDay) {
            this.maxScanPerDay = maxScanPerDay;
        }

        public List<Book> getBooks() {
            return books;
        }

        public void setBooks(List<Book> books) {
            this.books = books;
        }

        public Library(int id, int numberOfBooks, int signupTime, int maxScanPerDay) {
            this.libId = id;
            this.numberOfBooks = numberOfBooks;
            this.signupTime = signupTime;
            this.maxScanPerDay = maxScanPerDay;
            System.out.println("number of books: " + numberOfBooks +
                    " signuptime: " + signupTime+
                    " maxScanCountPerDay: " + maxScanPerDay);
        }

        public Library(){

        }
    }

    public static class BookCompator extends Book implements Comparator<Book>{

        @Override
        public int compare(Book b1, Book b2) {
            if(b1.getScore() < b2.getScore())
                return 1;
            else if(b1.getScore() > b2.getScore())
                return -1;
            else
                return 0;
        }
    }

    public static class LibraryCompator extends Library implements Comparator<Library>{

        @Override
        public int compare(Library b1, Library b2) {
            if(b1.getSignupTime() > b2.getSignupTime())
                return 1;
            else if(b1.getSignupTime() < b2.getSignupTime())
                return -1;
            else
                return 0;
        }
    }

    public static class LibraryExtendedCompator extends Library implements Comparator<Library>{

        @Override
        public int compare(Library b1, Library b2) {
            int readibility1 = (numberOfDays- b1.getSignupTime())*b1.maxScanPerDay;
            int k1 = readibility1 < b1.getBooks().size() ? readibility1 : b1.getBooks().size();

            int score1 = 0;

            for(int i=0; i<k1 && b1.getBooks() != null && b1.getBooks().size() != 0 ;i++){
                score1 += b1.getBooks().get(i).score;
            }

            int readibility2 = (numberOfDays- b2.getSignupTime())*b2.maxScanPerDay;
            int k2 = readibility2 < b2.getBooks().size() ? readibility2 : b2.getBooks().size();
            int score2 = 0;

            for(int i=0; i<k2 && b2.getBooks() != null && b2.getBooks().size() != 0;i++){
                score2 += b2.getBooks().get(i).score;
            }



            if(b1.getSignupTime()*score2 > b2.getSignupTime()*score1){
                return 1;
            }else if(b1.getSignupTime()*score2 < b2.getSignupTime()*score1){
                return -1;
            }
            else {
                return 0;
            }
        }
    }
}
