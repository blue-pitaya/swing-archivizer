package swingarchivizer;

interface FetchItemsInfoHandler {

    void submitForm(String from, String to);

    void setListener(FetchItemsInfoListener listener);
}
