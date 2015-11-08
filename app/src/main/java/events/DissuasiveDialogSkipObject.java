package events;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Dimitri on 08/11/2015.
 */
public class DissuasiveDialogSkipObject {
    public class DissuasiveDialogSkippedEvent extends EventObject {

        public DissuasiveDialogSkippedEvent(Object source) {
            super(source);
        }

    }

    public interface DissuasiveDialogSkippedEventListener extends EventListener {
        void onDissuasiveDialogSkipped(DissuasiveDialogSkippedEvent ddse);
    }


    public List<DissuasiveDialogSkippedEventListener> listeners = new ArrayList<>();
    public Object src;

    public DissuasiveDialogSkipObject(Object src){
        this.src = src;
    }

    public synchronized void addDissuasiveDialogSkippedEventListener(DissuasiveDialogSkippedEventListener l) {
        listeners.add(l);
    }

    public synchronized void removeDissuasiveDialogSkippedEventListener(DissuasiveDialogSkippedEventListener l) {
        listeners.remove(l);
    }

    public synchronized void fireDissuasiveDialogSkippedEvent() {
        DissuasiveDialogSkippedEvent ue = new DissuasiveDialogSkippedEvent(src);
        Iterator iListeners = listeners.iterator();
        while(iListeners.hasNext()) {
            ((DissuasiveDialogSkippedEventListener) iListeners.next()).onDissuasiveDialogSkipped(ue);
        }
    }
}
