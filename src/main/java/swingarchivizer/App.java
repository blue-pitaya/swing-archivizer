package swingarchivizer;

import swingarchivizer.context.AppContext;
import swingarchivizer.context.FetchItem;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Consumer;

public class App {
    public static void main(String[] args) {
        AppContext ctx = new AppContext();
        HttpService httpService = new HttpService();
        AppConfig config = new AppConfig();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Archivizer v1.0");
            MessageDialogHelper messageDialogHelper = new MessageDialogHelper(frame);
            UiBlocker uiBlocker = new UiBlocker();
            FetchService fetchService = new FetchService(ctx, messageDialogHelper, httpService, uiBlocker);

            DownloadPanel downloadPanel = new DownloadPanel(ctx, messageDialogHelper, fetchService);
            uiBlocker.add(downloadPanel);
            frame.add(downloadPanel, BorderLayout.CENTER);

            FetchItemsInfoFormPanel fetchItemsInfoFormPanel = getFetchItemsInfoFormPanel(config, fetchService, uiBlocker, downloadPanel::setItems);
            uiBlocker.add(fetchItemsInfoFormPanel);
            frame.add(fetchItemsInfoFormPanel, BorderLayout.NORTH);

            frame.setMinimumSize(new Dimension(100, 200));
            frame.pack();

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    private static FetchItemsInfoFormPanel getFetchItemsInfoFormPanel(
            AppConfig appConfig,
            FetchService fetchService,
            UiBlocker uiBlocker,
            Consumer<List<FetchItem>> onItemsFetched
    ) {
        DefaultDatesProvider ddp = new DefaultDatesProvider() {

            @Override
            public String getFromDate() {
                return appConfig.getDefaultFromDate().toString();
            }

            @Override
            public String getToDate() {
                return appConfig.getDefaultToDate().toString();
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

                uiBlocker.setBusy(true);
                fetchService.fetchItems(fromDate, toDate, onItemsFetched);
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
