package swingarchivizer.fetchitemsinfo;

import swingarchivizer.common.ValidationEvent;

public interface FetchItemsInfoListener {

    void fromValidated(ValidationEvent event);

    void toValidated(ValidationEvent event);

    void errorOccurred(Exception e);
}
