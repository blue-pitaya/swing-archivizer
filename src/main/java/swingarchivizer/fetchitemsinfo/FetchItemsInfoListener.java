package swingarchivizer.fetchitemsinfo;

import swingarchivizer.ValidationEvent;

interface FetchItemsInfoListener {

    void fromValidated(ValidationEvent event);

    void toValidated(ValidationEvent event);

    void errorOccurred(Exception e);
}
