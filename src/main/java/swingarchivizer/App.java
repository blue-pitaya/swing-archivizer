package swingarchivizer;

import swingarchivizer.context.AppContext;
import swingarchivizer.presenters.DownloadPresenter;
import swingarchivizer.views.DownloadPanel;
import swingarchivizer.workers.FetchItemsWorker;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.function.Consumer;

public class App {
    public static void main(String[] args) {
        AppContext ctx = new AppContext();
        HttpService httpService = new HttpService();
        AppConfig config = new AppConfig();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Archivizer v1.0");
            MessageDialogHelper messageDialogHelper = new MessageDialogHelper(frame);

            FetchItemsInfoFormPanel fetchItemsInfoFormPanel = getFetchItemsInfoFormPanel(config, ctx, messageDialogHelper, httpService);
            frame.add(fetchItemsInfoFormPanel, BorderLayout.NORTH);

            DownloadPanel downloadPanel = new DownloadPanel();
            new DownloadPresenter(downloadPanel, ctx, messageDialogHelper, httpService);
            frame.add(downloadPanel.getComponent(), BorderLayout.CENTER);

            frame.setMinimumSize(new Dimension(100, 200));
            frame.pack();

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    private static FetchItemsInfoFormPanel getFetchItemsInfoFormPanel(
            AppConfig appConfig,
            AppContext ctx,
            MessageDialogHelper messageDialogHelper,
            HttpService httpService
    ) {
        DefaultDatesProvider ddp = new DefaultDatesProvider() {

            @Override
            public String getFromDate() {
                return null;
            }

            @Override
            public String getToDate() {
                return null;
            }
        };

        FetchItemsInfoHandler handler = new FetchItemsInfoHandler() {
            private Consumer<ValidationEvent> fromValidationListener;
            private Consumer<ValidationEvent> toValidationListener;

            @Override
            public void submitForm(String from, String to) {
                LocalDate fromDate = null;
                try {
                    fromDate = LocalDate.parse(from);
                    fromValidationListener.accept(ValidationEvent.ok());
                } catch (DateTimeParseException e) {
                    fromValidationListener.accept(ValidationEvent.message("Nieprawidłowa data"));
                }

                LocalDate toDate = null;
                try {
                    toDate = LocalDate.parse(to);
                    toValidationListener.accept(ValidationEvent.ok());
                } catch (DateTimeParseException e) {
                    toValidationListener.accept(ValidationEvent.message("Nieprawidłowa data"));
                }

                if (fromDate == null || toDate == null) {
                    return;
                }

                if (fromDate.isAfter(toDate)) {
                    toValidationListener.accept(ValidationEvent.message("Nieprawidłowa data"));
                    return;
                }

                new FetchItemsWorker(ctx, messageDialogHelper, httpService, fromDate, toDate).execute();
            }

            @Override
            public void setFromValidationListener(Consumer<ValidationEvent> listener) {
                fromValidationListener = listener;
            }

            @Override
            public void setToValidationListener(Consumer<ValidationEvent> listener) {
                toValidationListener = listener;
            }
        };

        return new FetchItemsInfoFormPanel(ddp, handler);
    }
}
