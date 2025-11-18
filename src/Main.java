import controller.LibraryController;
import service.LibraryService;

public class Main {
    public static void main(String[] args) {
        LibraryService service = new LibraryService();
        LibraryController controller = new LibraryController(service);
        controller.start();
    }
}
