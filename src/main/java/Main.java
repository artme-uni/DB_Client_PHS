import connectivity.SQLQueryCreator;
import presenter.Presenter;
import view.ClientGUI;

public class Main {
    public static void main(String[] args) {

        Presenter presenter = new Presenter();
        ClientGUI gui = new ClientGUI(presenter);
        presenter.addErrorHandler(gui);
    }
}
