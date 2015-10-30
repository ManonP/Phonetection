package events;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

public class UnlockObject {
    public class UnlockedEvent extends EventObject {

        public static final int PIN_UNLOCK = 1;
        public static final int PATTERN_UNLOCK = 2;
        public static final int IMAGE_UNLOCK = 3;

        private int type;

        public UnlockedEvent(Object source, int type) {
            super(source);
            this.type = type;
        }

        public int getType(){
            return this.type;
        }

    }

    public interface UnlockedEventListener extends EventListener {
        public void onUnlocked(UnlockedEvent ue);
    }


    public List<UnlockedEventListener> listeners = new ArrayList<>();
    public Object src;
    public int type;

    public UnlockObject(Object src, int type){
        this.src = src;
        this.type = type;
    }

    public synchronized void addUnlockedEventListener(UnlockedEventListener l) {
        listeners.add(l);
    }

    public synchronized void removeUnlockedEventListener(UnlockedEventListener l) {
        listeners.remove(l);
    }

    public synchronized void fireUnlockedEvent() {
        UnlockedEvent ue = new UnlockedEvent(src, type);
        Iterator iListeners = listeners.iterator();
        while(iListeners.hasNext()) {
            ((UnlockedEventListener) iListeners.next()).onUnlocked(ue);
        }
    }
}

