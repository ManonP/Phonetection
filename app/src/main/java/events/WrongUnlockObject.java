package events;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

public class WrongUnlockObject {
    public class WrongUnlockedEvent extends EventObject {

        public static final int PIN_WRONG_UNLOCK = 1;
        public static final int PATTERN_WRONG_UNLOCK = 2;
        public static final int IMAGE_WRONG_UNLOCK = 3;

        private int type;

        public WrongUnlockedEvent(Object source, int type) {
            super(source);
            this.type = type;
        }

        public int getType(){
            return this.type;
        }

    }

    public interface WrongUnlockedEventListener extends EventListener {
        public void onWrongUnlocked(WrongUnlockedEvent ue);
    }


    public List<WrongUnlockedEventListener> listeners = new ArrayList<>();
    public Object src;
    public int type;

    public WrongUnlockObject(Object src, int type){
        this.src = src;
        this.type = type;
    }

    public synchronized void addWrongUnlockedListener(WrongUnlockedEventListener l) {
        listeners.add(l);
    }

    public synchronized void removeWrongUnlockedListener(WrongUnlockedEventListener l) {
        listeners.remove(l);
    }

    public synchronized void fireWrongUnlockedEvent() {
        WrongUnlockedEvent ue = new WrongUnlockedEvent(src, type);
        Iterator iListeners = listeners.iterator();
        while(iListeners.hasNext()) {
            ((WrongUnlockedEventListener) iListeners.next()).onWrongUnlocked(ue);
        }
    }
}
