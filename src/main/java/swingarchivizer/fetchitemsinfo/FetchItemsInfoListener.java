package swingarchivizer.fetchitemsinfo;

import swingarchivizer.ui.ValidationEvent;

public interface FetchItemsInfoListener {

    void fromValidated(ValidationEvent event);

    void toValidated(ValidationEvent event);

    void errorOccurred(Exception e);
}
